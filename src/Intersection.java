/*  
                Intersection - Settlers of Catan

Class:      Adjvanced Java - CIT-285-01
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
            Intersection.java (Current File)
            Player.java
            Trade.java
            READ_THIS_FIRST.txt
            CatanGameboard.jpeg

Classes:    Intersection
            Boundary (Moved to Boundary.java file)
            Coordinate (Moved to Coordinate.java file)

                                    Summary:
 The following code the class Intersection the computerized game Settlers of 
 Catan. Each instance of this class will represent a unique corner of a hexagonal 
 tile on the gameboard. The exact location of an intersection is set by passing a 
 coordinate position to its constructor. Players have the option of settling an
 intersection provided that no one else has settled it, no one has settled
 any adjacent intersections, and they have a road that connects to its position 
 (this road requirement is dropped during the first 2 round of the game).
 
  
Activity:	  -Date-             -Person-               -Updates-
            October 20, 2016		AS          *Created Intersection Class
                                                    *Created Boundary Class
                                                    *Created Coordinate Class
                                                    *Submitted as AS's project 1
				
            November 7, 2016		AS          *Standardized Documentation 
                                                    and Formatting 

            November 14, 2016		AS          *added searchIntersections and
                                                     searchBoundary methods
                                       
            November 18, 2016           AS          *Boundary and coordinate 
                                                     classes moved to their 
                                                     own .java files 
                                                    *Added Javadoc documentation

            November 19, 2016           AT          * Added getLocation method

            November 20, 2016           AT          * Added Circle field for UI
                                                      including getter and 
                                                      constructor change

            November 23, 2016           AT          * Added minor commenting
                                                    * Changed location, 
                                                      adjacentIntersections
                                                      and adjacentBoundaries 
                                                      to final
						      
 	    November 24, 2016		RA	    * Added validation statements   
                                                      to make sure settlements and 
                                                      cities have not exceeded 
                                                      maximum amount

            December 2, 2016            AS  `       * Updated Javadoc documentation
 */

import java.util.ArrayList;
import javafx.scene.shape.Circle;

/**
 * The <code> Intersection </code> class represents the corners of the hexagonal
 * game board tiles, defined by <code> Coordinate </code> objects that establish
 * physical game board location.
 *
 */
public class Intersection {

//                              Class Properties
// _____________________________________________________________________________
    private final Coordinate location;        //Holds coordinate position of object
    //Will be used when constructing GUI

    private int player = -1;            //Player who has settled intersection
    //-1 value represents no player

    private int settlementType = 0;     //0 value = no settlement
    //1 value = base settlement
    //2 value = city

    private final Intersection[] adjacentIntersections = new Intersection[3];
    private int adjacentIntersectionCount;
    private final ArrayList<Boundary> adjacentBoundaries = new ArrayList<>();

    private final Circle circle;

//                               Constructors
// _____________________________________________________________________________
    /**
     * Constructor creates a new <code> Intersection </code> located at the given 
     * <code> Coordinate </code> and generates a <code> JavaFX Circle </code> 
     * to be used by the GUI.
     *
     * @param c <code> Coordinate </code> location
     */
    Intersection(Coordinate c) {
        location = c;
        // Create circle at location c for UI
        circle = new Circle(location.getUIX(), location.getUIY(), ClientUI.circleSize);
       
    }

//                          Accessors and Mutators
// _____________________________________________________________________________
    //For setting to the conquering player's ID when intersection is settled
    void setPlayer(int p) {
        player = p;

        //Message for Testing
        System.out.println("Intersection settled successfully!");
    }

    //Returns the playerID of the intersections settler
    //If intersection is unsettled, returns -1
    int getPlayer() {
        return player;
    }

    //Sets Settlement Type
    //0 = no settlement, 1 = base settlement, 2 = city
    void setSettlementType(int s) {
        settlementType = s;
    }

    //Returns Settlement Type
    //0 = no settlement, 1 = base settlement, 2 = city
    int getSettlementType() {
        return settlementType;
    }

    Coordinate getLocation() {
        return location;
    }

    /**
     * This overloaded <code> setAdjacentIntersections </code> method sets an 
     * <code> Intersection </code> object's <code> adjacentIntersections
     * </code> array when there are two adjacent <code> Intersections </code>.
     * Seconds sentence test.
     *
     * @param a First adjacent <code> Intersection </code>
     * @param b Second adjacent <code> Intersection </code>
     */
    void setAdjacentIntersections(Intersection a, Intersection b) {

        adjacentIntersections[0] = a;
        adjacentIntersections[1] = b;

        //The integer adjacentIntersectionCount is set accordingly
        adjacentIntersectionCount = 2;
    }

    /**
     * This overloaded <code> setAdjacentIntersections </code> method sets an 
     * <code> Intersection </code> object's <code> adjacentIntersections
     * </code> array when there are three adjacent <code> Intersections </code>.
     *
     * @param a First adjacent <code> Intersection </code>
     * @param b Second adjacent <code> Intersection </code>
     * @param c Third adjacent <code> Intersection </code>
     */
    void setAdjacentIntersections(Intersection a, Intersection b, Intersection c) {

        adjacentIntersections[0] = a;
        adjacentIntersections[1] = b;
        adjacentIntersections[2] = c;

        //The integer adjacentIntersectionCount is set accordingly
        adjacentIntersectionCount = 3;
    }

    //Returns ArrayList of adjacent boundaries
    ArrayList<Boundary> getAdjacentBoundaries() {
        return adjacentBoundaries;
    }

    //Adds an adjacent boundary to the ArrayList adjacentBoundaries
    void addAdjacentBoundary(Boundary a) {
        adjacentBoundaries.add(a);
    }

    Circle getCircle() {
        return circle;
    }

//                                 Methods
// _____________________________________________________________________________
    /**
     * Returns a boolean indicating whether the <code> Intersection </code> is settled.
     *
     * @return boolean
     */
    boolean occupied() {
        return player >= 0;
    }

    /**
     * Returns a boolean indicating whether the given player may occupy 
     * (build a settlement on) the <code> Intersection </code>.
     * 
     * The method checks that the following conditions are met before returning
     * true. If any of the following conditions are not met, method returns false.
     * 1. Intersection is unoccupied
     * 2. Player has fewer than 5 general settlements
     * 3. Player has fewer than 4 cities
     * 4. Not be adjacent to a settled Intersection
     * 5. intersection is on a road built by the player 
     *    (Waived during first 2 rounds, the "setupPhase")
     *
     * @param playerID unique <code> int </code> associated with player
     * @return boolean - True indicates Intersection occupiable 
     */
    boolean isOccupiable(int playerID, boolean setupPhase) {

        Player playerInfo = GameManager.players[playerID];

        boolean hasRoadAccess = false;

        //If intersection is already occupied, returns false
        if (occupied()) {

            //Message for Testing
            System.out.println("Player " + (playerID + 1)
                    + " is unable to settle this intersection "
                    + "because it is already settled by player "
                    + (player + 1) + ".");

            return false;

        } 
        
        //TODO: settlement type requested must be passed as a parameter to method
        // If player wants to build a settlement, but alreary has 5, returns false
        else if (settlementType == 1 && playerInfo.getSettlementCount() >= 5) {

            System.out.println("Player " + (playerID + 1)
                    + " is unable to settle this intersection because it "
                    + " has exceeded the amount of settlements available.");

            return false;
        } // If player wants to build a city, but alreary has 4, returns false
        else if (settlementType == 2 && playerInfo.getCityCount() >= 4) {

            System.out.println("Player " + (playerID + 1)
                    + " is unable to settle this intersection because it "
                    + " has exceeded the amount of cities available.");

            return false;

        }

        //For-loop iterates through adjacent intersections and boundaries
        for (int i = 0; i < adjacentIntersectionCount; i++) {

            //If any adjacent intersection is occupied, returns false 
            if (adjacentIntersections[i].occupied()) {

                //Message for Testing
                System.out.println("Player " + (playerID + 1)
                        + " is unable to settle this intersection "
                        + "because an adjacent intersection is already settled.");

                return false;
            }

            //If the player has built at least one road on an adjacent boundary,
            //or the game is in the setupPhase, the flag is set to true
            if (playerID == adjacentBoundaries.get(i).getPlayer() || setupPhase) {
                hasRoadAccess = true;
            }
        }

        //If none of the adjacent boundaries have a road built by this player,
        //then false is returned
        if (!hasRoadAccess) {

            //Message for Testing
            System.out.println("Player " + (playerID + 1)
                    + " is unable to settle this intersection "
                    + "because it is not the setup phase and "
                    + "she has not constructed a roadway to this point ");

            return false;
        }

        //If none of the false conditions are hit, true is returned
        return true;
    }

    //Searches for an intersection based on its coordinate point
    //Returns that intersection if successful, otherwise returns error
    /**
     * The <code> searchIntersections </code> method searches for an 
     * <code> Intersection </code> based on its <code> Coordinate </code>
     * location - Returns that intersection if successful, otherwise returns
     * error.
     *
     * @param coordinate
     * @return Intersection
     */
    static Intersection searchIntersections(Coordinate coordinate) {

        //for loop iterates through each intersection  and compares its location to the given coordinate
        //if the location and coordinate match, that intersection is returned
        for (Intersection i : GameManager.intersections) {
            if (i.location.getX() == coordinate.getX() && i.location.getY() == coordinate.getY()) {
                System.out.println("Intersection found.");
                return i;

            }

        }
        System.out.println("Error, no intersection found with that coordinate position.");
        return GameManager.errorIntersection;
    }

}
