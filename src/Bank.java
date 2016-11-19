/*  
                            Bank - Settlers of Catan

Class:      Advanced Java - CIT-285-01
            Professor Miller, Fall 2016

Group:      RARA - Settlers of Catan
            Ruchelly Almeida
            Alessandra Shipman     
            Oleksii Butakov
            Andrew Thomas

Files:      Bank.java (Current File)
            Boundary.java
            Coordinate.java
            DevelopmentCard.java 
            GameManager.java
            HexTile.java
            Intersection.java
            Player.java
            Trade.java


Classes:    Bank


                                    Summary:
The following code contains the Bank Class for the computerized game, 
Settlers of Catan. This class facilitates trade of resource materials.
This includes both trade between multiple players and trade between a 
single player and the bank. This class also manages the quantity and 
distribution of development cards.


Activity:	  -Date-             -Person-               -Updates-
            November 7, 2016   		AS          *Created Bank Class
                                                    *DevelopmentCard Methods

            November 14, 2016           AS          *Made buyDevelopmentCard method
                                                    *Made findDevlopmentCard method
                                                    *Made exchangeRates method
                                                    *Made bankTrade method
                                                    *Made playerTrade method
                                                    *Made tradePrompt method
                                                    

                                        AT          *Debugged test code and the 
                                                     find/buy methods
            
            November 17, 2016           AS          *Moved most trade methods to
                                                     a new Trade class
                                                     




 */

import java.util.Scanner;
public class Bank {

//  				Class Properties
//_____________________________________________________________________________
    // "Constants" used to refer to resource type in various methods
    // general harbor is used in determining port placement
    static final int NO_RESOURCE = -1;
    static final int BRICK = 0;
    static final int LUMBER = 1;
    static final int ORE = 2;
    static final int WHEAT = 3;
    static final int WOOL = 4;
    static final int GENERAL_HARBOR = 5;  
    
    private static final int DEVELOPMENT_CARD_COUNT = 25;
    private static DevelopmentCard[] developmentCards = new DevelopmentCard[DEVELOPMENT_CARD_COUNT];
    private static int remainingCards = 25;


//                                  Methods
//_____________________________________________________________________________
    public void generateDevelopmentCards() {

        for (int i = 0; i < DEVELOPMENT_CARD_COUNT; i++) {

            if (i < 14) {
                developmentCards[i] = new KnightCard();

            } else if (i >= 14 && i < 19) {
                developmentCards[i] = new VictoryPointCard();

            } else if (i >= 19 && i < 21) {
                developmentCards[i] = new RoadBuildingCard();

            } else if (i >= 21 && i < 23) {
                developmentCards[i] = new MonopolyCard();

            } else if (i >= 23 && i < 26) {
                developmentCards[i] = new YearOfPlentyCard();
            }
        }

        System.out.println("Development Cards Generated.");

        int i = 0;
        for (DevelopmentCard d : developmentCards) {
            System.out.println(i++ + " " + d.getTitle());
        }
    }

    public void buyDevelopmentCard(int playerID) {

        Player currentPlayer = GameManager.players[playerID];

        if (currentPlayer.getResourceCount(ORE) < 1
                || currentPlayer.getResourceCount(WOOL) < 1
                || currentPlayer.getResourceCount(WHEAT) < 1) {

            System.out.println("You do not have adequate resources to buy a development card.");

        } else if (remainingCards == 0) {
            System.out.println("There are no remaining development cards to buy.");

        } else {

            boolean cardAssigned = false;

            while (!cardAssigned) {

                int card = (int) (Math.random() * 25);

                DevelopmentCard d = developmentCards[card];

                if (d.getPlayer() == -1) {

                    d.setPlayer(playerID);
                    cardAssigned = true;
                    System.out.println("You have purchased a " + d.getTitle() + " Card.");

                    if (d.getTitle().equals("Victory Point")) {
                        System.out.println("Victory Point Cards play automatically");
                        ((VictoryPointCard) d).play(playerID);
                    }

                }

            }

            remainingCards--;
            currentPlayer.deductResource(ORE, 1);
            currentPlayer.deductResource(WOOL, 1);
            currentPlayer.deductResource(WHEAT, 1);

        }

    }

    public void findDevelopmentCards(int playerID) {

        for (DevelopmentCard d : developmentCards) {

            if (d.getPlayer() == playerID && d.isPlayed() == false) {

                String cardType = d.getTitle();

                System.out.println("You have a playable " + cardType + " Card. Would you like to play it? ");

                Scanner sc = new Scanner(System.in);
                String play = sc.next();

                if (play.equals("y")) {

                    if (cardType.equals("Knight")) {
                        ((KnightCard) d).play(playerID);
                    } else if (cardType.equals("Road Building")) {
                        ((RoadBuildingCard) d).play(playerID);
                    } else if (cardType.equals("Monopoly")) {
                        ((MonopolyCard) d).play(playerID);
                    } else if (cardType.equals("Year of Plenty")) {
                        ((YearOfPlentyCard) d).play(playerID);

                    }

                }
            }
        }
    }
    
    public int[] exchangeRates(int playerID){

    	//each index of this array corresponds with the resources in the resource array
    	//all exchange rates begin as a 4:1 ratio
    	int[] exchangeRates = {4,4,4,4,4};

    	for(Boundary b : GameManager.externalBoundaries){

    		int harbor = b.getHarbor();

    		if(b.getPlayer() == playerID && harbor != NO_RESOURCE){

    			if(harbor == GENERAL_HARBOR){
    				for(int i : exchangeRates){

    					if(i == 4)
    						i = 3;
}
    			}else{
    				exchangeRates[harbor] = 2;
    			}

    		}
    	}
    	
    	return exchangeRates;
    }
    
    public void bankTrade(int playerID){
    	
    	String continueTrading = "y";
    	
    	int exchangeRates[] = exchangeRates(playerID).clone(); 
    	Player currentPlayer = GameManager.players[playerID];
    	
    	
    	
    	while(continueTrading.equals("y")){
    		
    		int offeredResource = -1;
    		int offeredAmount = -1;
    		int requestedResource = -1;
    		int requestedAmount = -1;
    		
    		Scanner sc = new Scanner(System.in);
    		
    	System.out.println("Which resource would you like to trade away?");
    	offeredResource = Integer.parseInt(sc.nextLine());
    	
    	int xrate = exchangeRates[offeredResource];
    	
    	System.out.println(xrate + " of this resource is enough to get one resource from the bank\n "
    			+ "How much of that resource would you like to trade? (enter multiples of " + xrate + ")");
    
    	offeredAmount = Integer.parseInt(sc.nextLine());
    	
    	
    	System.out.println("Which resource would you like in return?");
    	requestedResource = Integer.parseInt(sc.nextLine());
    	
    	requestedAmount  = offeredAmount/xrate;
    	
    	currentPlayer.deductResource(offeredResource, offeredAmount);
    	currentPlayer.addResource(requestedResource, requestedAmount);
    	
    	System.out.println("You have traded " + offeredAmount + " of Resource " + offeredResource 
    			+ "for " + requestedResource + "of Resource 1.");
    	currentPlayer.printResources();
    	
    	System.out.println("Would you like to continue trading with the bank?");
    	
    	continueTrading = sc.nextLine();
    	}

    }

    public void playerTrade(int playerID){

    	String continueTrading = "y";
    	Player currentPlayer = GameManager.players[playerID];

    	while(continueTrading.equals("y")){

    		Trade tradeDeal = new Trade(playerID); 


    		Scanner sc = new Scanner(System.in);

    		System.out.println("Which resource would you like to trade away? Enter -1 to represent any resource.");
    		tradeDeal.setOfferedResource(Integer.parseInt(sc.nextLine()));


    		System.out.println("How much of this resource would you like to trade?");
    		tradeDeal.setOfferedAmount(Integer.parseInt(sc.nextLine()));


    		System.out.println("Which resource would you like in return? Enter -1 to represent any resource.");
    		tradeDeal.setRequestedResource(Integer.parseInt(sc.nextLine()));

    		System.out.println("How much of this resource would you like in return?");
    		tradeDeal.setRequestedAmount(Integer.parseInt(sc.nextLine()));


    		System.out.println("To whom would you like to offer this trade? Enter -1 to represent all other players.");
    		tradeDeal.setTradingPartner(Integer.parseInt(sc.nextLine()));
    		
    		tradeDeal.distributeTradeRequest();

    		System.out.println("Would you like to make another player trade");
    		continueTrading = sc.nextLine();
    	}
    }

}







