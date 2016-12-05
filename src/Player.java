
import javafx.scene.paint.Color;
import static javafx.scene.paint.Color.*;

/*  
                        Player - Settlers of Catan

Class:      Advanced Java - CIT-285-01
            Professor Miller, Fall 2016

Group:      RARA - Settlers of Catan
            Ruchelly Almeida
            Alessandra Shipman     
            Oleksii Butakov
            Andrew Thomas

Files:      Bank.java
            Boundary.java
            Coordinate.java
            ClientUI.java
            DevelopmentCard.java 
            GameManager.java
            HexTile.java
            Intersection.java
            Player.java (Current File)
            Trade.java
            READ_THIS_FIRST.txt
            CatanGameboard.jpeg



Classes:    Player


				Summary:
The following code contains the player  Class for the computerized game, 
Settlers of Catan. This class records the information unique to each
player. It also evaluates each player's victory points based on their assets.


Activity:	  -Date-             -Person-               -Updates-
            September 28, 2016          RA          *Created player class
                                                    *Submitted as RA's Project 1
												
            October 28, 2016		RA          *Developed into working class

            November 7, 2016   		AS          *Standardized Documentation 
                                                     and Formatting 
                                                    *Standardized variable names
                                                    *Added DevelopmentCard connections
                                                    *Added Victory Point logic

            November 14, 2016		AS          *Added player ID field, it is now
                                                     a parameter of the constructor
                                                    *Wrote printResources Method
                                                    *Prevent overdrawing resource in 
                                                     deductResource method


            November 18, 2016           AS          *Changed resourceTotal to be 
                                                     updated in addResource() and
                                                   deductResource()

            November 19,2016            AS          *Added developmentCardCount,
                                                     getDevelopmentCardCount, 
                                                     addDevelopmentCard,
                                                     and deductDelemopmntCard
                                                    *Added getters and setters
                                                     for largestArmy and 
                                                     longestRoad


	    November 20, 2016           AT          * Added color field for UI elements
                                                      along with necessary imports and
                                                      methods

            November 23, 2016           AT          * Minor commenting
			
	    November 23, 2016           AS          *Switched order of wool and wood
                                                     in printResources method
			
	    November 24, 2016		RA	    * Corrected the maximum number of buildings
	    					      according to boardgame rules
	    					    * Validated addRoad, addSettlement and 
						      addCity methods
	    					    * Created printRoads, printSettlement and 
						      printCities methods
						      
	    December 4, 2016		RA	    * Created arrays roadList, settlementList
	    					      and cityList that store the location of 
						      each of player's settlements
						    * Created add method to these arrays that
						      increment each settlements count
						    * Updated print methods to use arrays
						    
 */
public class Player {

//  				Class Variables
//_____________________________________________________________________________
    private int playerID = -1;

    // number of buildings a player has
    private int roadCount = 0; 			//max of 15
    private int settlementCount = 0;            //max of 5
    private int cityCount = 0; 			//max of 4
	
    // Location of buildings a player has
    private Boundary[] roadList = new Boundary[14];
    private Intersection[] settlementList = new Intersection[4];
    private Intersection[] cityList = new Intersection[3];
	
    private int developmentCardCount; //number of unplayed development cards held by this player

    //victory points are earned by players during the game. 1st player w/ 10 pts wins the game
    //other players are able to see how may victory points a player has, except for those through victory point cards
    //total victory points = visible + cards
    private int visibleVictoryPoints = 0;
    private int victoryPointCards = 0;

    private int knightCards = 0; //number of played knight cards

    //resource material array 
    //the constant value associated with this array is the position of that resource in this array
    int resourceMaterials[] = {0, 0, 0, 0, 0, 0};
    int resourceTotal = 0;

    //Achievements
    private boolean longestRoad = false; //has built most roads
    private boolean largestArmy = false; //played the most knight cards
    
    private Color color;

// 				Constructors
//_____________________________________________________________________________
    Player(int id) {
        playerID = id;
        switch (id) {
            // Assign a color based upon playerID for UI
            case 0:
                color = BLUE;
                break;
            case 1:
                color = RED;
                break;
            case 2:
                color = GREEN;
                break;
            case 3:
                color = YELLOW;
                break;
            default:
                color = BLACK;
        }
    }

//                          Accessors and Mutators
//_____________________________________________________________________________
    public int getPlayerID() {
         return playerID;
    }

    public int getRoadCount() {
	    
        return roadCount;
    }

    public void addRoad() {
        if(roadCount >= 15){
 		System.out.println("You have exceeded the number of roads available");
		return;
	}
	else
	    roadCount++;
    }

    public int getSettlementCount() {
        return settlementCount;
    }

    public void addSettlement() {
	if(settlementCount >= 5){
		System.out.println("You have exceeded the number of settlements available");
	        return;
	}
	else
            settlementCount++;
    }

    public int getCityCount() {	
            return cityCount;
    }

    public void addCity() {
	if(cityCount >= 4){
		System.out.println("You have exceeded the number of cities available");
	        return;
	}
	else       
            cityCount++;
    }

    public Boundary[] getRoadList(){
        return roadList;
    }
    
    // adds road location to array of roads locations, increments roadCount
    public void addRoadList(Boundary road){
        if(roadCount >= 15){
		System.out.println("You have exceeded the number of roads available");
		return;
	}
        else{
            for(int i = 0; i <= 14; i++){
                if(roadList[i] == null){
                    roadList[i] = road;
                    roadCount++;
                    break;
                }        
            }
        }
    }
    
    public Intersection[] getSettlementList(){
        return settlementList;
    }
    
    // add settlement location to array of settlem. locations, increments settlementCount
    public void addSettlementList(Intersection settlement) {
        if (settlementCount >= 5) {
            System.out.println("You have exceeded the number of settlements available");
            return;
        } 
        else{
            for(int i = 0; i <= 4; i++){
                if(settlementList[i] == null){
                    settlementList[i] = settlement;
                    settlementCount++;
                    break;
                }        
            }
        }
    }

    public Intersection[] getCitytList(){
        return cityList;
    }
    
    // adds city location to array of cities locations, increments cityCount
    public void addCitytList(Intersection city) {
        if (cityCount >= 4) {
            System.out.println("You have exceeded the number of cities available");
            return;
        } 
        else{
            for(int i = 0; i <= 3; i++){
                if(settlementList[i] == null){
                    cityList[i] = city;
                    cityCount++;
                    break;
                }        
            }
        }   
    }
            
    public int getDevelopmentCardCount() {
        return developmentCardCount;
    }

    public void addDevelopmentCard() {
        developmentCardCount++;
    }

    public void deductDevelopmentCard() {
        developmentCardCount--;
    }

    public int getVisibleVictoryPoints() {

        //one point is given for settlements that are not cities
        //two points are given for cities
        visibleVictoryPoints = (settlementCount - cityCount) + 2 * cityCount;

        //Having the longest road or largest army awards 2 points
        if (longestRoad) {
            visibleVictoryPoints += 2;
        }
	    
        if (largestArmy) {
            visibleVictoryPoints += 2;
        }

        return visibleVictoryPoints;
    }

    public int getVictoryPointCards() {
        return victoryPointCards;
    }

    public void addVictoryPointCard() {
        victoryPointCards++;
    }

    public int getTotalVictoryPoints() {
        return visibleVictoryPoints + victoryPointCards;
    }

    public int getKnightCards() {
        return knightCards;
    }

    public void addKnightCard() {
        knightCards++;
    }

    public int getResourceCount(int position) {

        //if statement prevents index out of bounds exception
        if (position >= 0) {
            return resourceMaterials[position];
        } else {
            return -1;
        }
    }

    public int getResourceTotal() {

        return resourceTotal;
    }

    public boolean isLongestRoad() {

	    return longestRoad;
    }

    public void setLongestRoad(boolean longestRoad) {
        this.longestRoad = longestRoad;
    }

    public boolean isLargestArmy() {
        return largestArmy;
    }

    public void setLargestArmy(boolean largestArmy) {
        this.largestArmy = largestArmy;
    }

    public Color getColor() {
        return color;
    }

//                                 Methods
// _____________________________________________________________________________
    public void addResource(int position, int amount) {
        //add amount to resource at position
        resourceMaterials[position] += amount;
        resourceTotal += amount;

    }

    public void deductResource(int position, int amount) {

        if (amount <= resourceMaterials[position]) {
            // Deduct amount from resource at position
            resourceMaterials[position] -= amount;
            resourceTotal -= amount;

        } else {
            // Prevent negative amounts
            System.out.println("You cannot deduct more resources than you have");
        }
    }

    public void resetResource(int position) {
        resourceMaterials[position] = 0;
    }

    public void printResources() {
        // Prints player's resource total for each resource
        System.out.println("\nPlayer " + (playerID + 1) + "'s current Resources:\n"
                + "_____________________________\n"
                + resourceMaterials[0] + " Brick\n"
                + resourceMaterials[1] + " Lumber\n"
                + resourceMaterials[2] + " Ore\n"
                + resourceMaterials[3] + " Wheat\n"
                + resourceMaterials[4] + " Wool\n");


    }
    
     public String toStringResources() {
        
        String resources = "";
        
        // Creates a string with player's resource total
        resources = "\nPlayer " + (playerID + 1) + "'s current Resources:\n"
                + "_____________________________\n"
                + resourceMaterials[0] + " Brick\n"
                + resourceMaterials[1] + " Lumber\n"
                + resourceMaterials[2] + " Ore\n"
                + resourceMaterials[3] + " Wheat\n"
                + resourceMaterials[4] + " Wool\n";
        
        return resources;

    }
	
    public void printRoads() { 
	 // Prints player's current number of roads  
        System.out.println("\nPlayer " + (playerID + 1) + " 's current number"
                + " of roads: " + roadCount + "\n Located at points: \n");

        // Prints the location of player's roads
        System.out.println(Arrays.toString(roadList)+ " ");	    
        
    }
			    
    public void printSettlements() {
	 // Prints player's current number of settlements  
        System.out.println("\nPlayer " + (playerID + 1) + " 's current number "
                + "of settlements: " + settlementCount + "\nLocated at points: ");

        // Prints the location of player's settlements
        System.out.println(Arrays.toString(settlementList)+ " "); 
    }
			    
    public void printCities() {
	// Prints player's current number of cities 
        System.out.println("\nPlayer " + (playerID + 1) + " 's current number "
                + "of cities:\n" + cityCount + "\nLocated at points: ");

        // Prints the location of player's cities
        System.out.println(Arrays.toString(cityList)+ " ");
    }
}
