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
            ClientUI.java
            DevelopmentCard.java 
            GameManager.java
            HexTile.java
            Intersection.java
            Player.java
            Trade.java
            READ_THIS_FIRST.txt
            CatanGameboard.jpeg


Classes:    Bank


                                    Summary:
The following code contains the Bank Class for the computerized game, Settlers 
of Catan. This class facilitates trade of resource materials. This includes both 
trade between multiple players and trade between a single player and the bank. 
This class also manages the quantity and distribution of development cards, 
the building of roads, settlements, and cities, and it calculates which player
has the longest road and the largest army.



Activity:	  -Date-             -Person-               -Updates-
            November 7, 2016   		AS          * Created Bank Class
                                                    * DevelopmentCard Methods

            November 14, 2016           AS          * Made buyDevelopmentCard method
                                                    * Made findDevlopmentCard method
                                                    * Made exchangeRates method
                                                    * Made bankTrade method
                                                    * Made playerTrade method
                                                    * Made tradePrompt method

                                                    

                                        AT          * Debugged test code and the 
                                                      find/buy methods
            
            November 17, 2016           AS          * Created new class Trade
                                                      to facilitate trading
                                                    * Moved tradePrompt() to trade 
                                                      class
                                                    * Altered playerTrade() to use
                                                      Trade class

            November 18, 2016           AT          * Moved buildRoad and 
                                                      buildSettlement here
                                                    * Made build methods deduct
                                                      appropriate resources
                                                    * Changed some properties to
                                                      Not be static to allow for
                                                      multiple simultaneous games
                                                      on server eventually

                                        AS          * Altered bankTrade() to
                                                      use Trade class
                                                    * Added calculateLongestRoad()
                                                      and calculateLargestArmy()
                                                      methods
                                                    * calculateLoungestRoad() is
                                                      called inside buildRoad()
                                        
            November 20, 2016           AT          * Added GUIBuildRoad method
                                                      to allow building of roads
                                                      without using console
                                                    * Also tweaked console method
                                                      to prevent errors with nextDouble
                                                      method
                                                    * Added GUIBuildSettlement method
                                                      to allow building of roads
                                                      without using console

            November 23, 2016           AT          * Added many method comments
                                                    * Tweaked buildSettlement method
                                                      to properly check if the settlement
                                                      is occupiable
                                                    * Completely reworked buildCity method
                                                    * Fixed slight logical errors in
                                                      longestRoad and largestArmy methods

            December 04, 2016           AS          * Added Javadoc documentation
                                                    * Included new add methods
                                                      from the player class to
                                                      every build method here
                                                    * Included setup phase
                                                      condition to the 
                                                      GUIBuildSettlement method
 */

import java.util.Scanner;

/**
 * The <code>Bank</code> class facilitates transactions of resources including
 * the trading of resources between players, the purchace of development cards,
 * and the building of roads and settlements.
 *
 */
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
    private DevelopmentCard[] developmentCards = new DevelopmentCard[DEVELOPMENT_CARD_COUNT];
    private int remainingCards = 25;

//                                  Methods
//_____________________________________________________________________________
    /**
     * Method handles the building of settlements for the text based game by
     * deducting the appropriate resources from the given player, marking the
     * intersection as occupied, and updating the player's information.
     *
     * @param playerID
     * @param setupPhase
     * @return Returns 0 if no error is met.
     */
    public static int buildSettlement(int playerID, boolean setupPhase) {
        // Create player object based upon integer playerID passed for use throughout method
        Player activePlayer = GameManager.players[playerID];

        Intersection settlementLocation;

        // Check if player has adequate resources for building a settlement
        // (1 Brick, 1 Lumber, 1 Wool, 1 Wheat
        if (activePlayer.resourceMaterials[GameManager.BRICK] >= 1 || activePlayer.resourceMaterials[GameManager.LUMBER] >= 1
                || activePlayer.resourceMaterials[GameManager.WOOL] >= 1 || activePlayer.resourceMaterials[GameManager.WHEAT] >= 1) {

            //Scanners and print lines will be obsolete after GUI is made
            Scanner sc = new Scanner(System.in);

            //Gets coordinate location for intersection
            System.out.println("Enter the x and y values of the location of your settlement:");
            Double xVal = GameManager.alphaToSqrt(sc.nextLine());
            Double yVal = Double.parseDouble(sc.nextLine());

            // Search intersections array for Intersection at given coordinate
            settlementLocation = Intersection.searchIntersections(new Coordinate(xVal, yVal));

            //if there was a problem finding the intersection, -1 is returned
            if (settlementLocation == GameManager.errorIntersection) {
                return -1;
            }

            // If the given settlement is occupiable, build on it
            if (settlementLocation.isOccupiable(playerID, setupPhase)) {
                settlementLocation.setPlayer(playerID);
                settlementLocation.setSettlementType(1);

                //deduct approprate resources from player
                activePlayer.deductResource(BRICK, 1);
                activePlayer.deductResource(LUMBER, 1);
                activePlayer.deductResource(WOOL, 1);
                activePlayer.deductResource(WHEAT, 1);

                //update player's settlements
                activePlayer.addSettlement(settlementLocation);

                return 0;
            } else // otherwise print error message
            {
                System.out.println("You are unable to build on that settlement");
                return -1;
            }
            // If inadequate resources, inform which resource is missing
        } else if (activePlayer.resourceMaterials[BRICK] < 1) {
            System.out.println("You do not have enough Brick");
            return -1;
        } else if (activePlayer.resourceMaterials[LUMBER] < 1) {
            System.out.println("You do not have enough Lumber");
            return -1;
        } else if (activePlayer.resourceMaterials[WOOL] < 1) {
            System.out.println("You do not have enough Wool");
            return -1;
        } else if (activePlayer.resourceMaterials[WHEAT] < 1) {
            System.out.println("You do not have enough Wheat");
            return -1;
        } else {
            System.out.println("Unknown error.¯\\_(ツ)_/¯");
            return -1;
        }
    }

    /**
     * Method handles the building of settlements for the graphics format game
     * by deducting the appropriate resources from the given player, marking the
     * intersection as occupied, and updating the player's information.
     *
     * @param activePlayerID
     * @param settlementToBuy
     * @param setupPhase
     */
    public static void GUIBuildSettlement(int activePlayerID, Intersection settlementToBuy, boolean setupPhase) {
        // Create Player object for ease of use
        Player activePlayer = GameManager.players[activePlayerID];
        // Set settlement to owned by player
        settlementToBuy.setPlayer(activePlayerID);
        settlementToBuy.setSettlementType(1);
        // Set circle stroke to player color
        settlementToBuy.getCircle().setStroke(activePlayer.getColor());
        //update player's settlements
        activePlayer.addSettlement(settlementToBuy);

        // Deduct resources if not setup phase
        if (!setupPhase) {
            GameManager.players[activePlayerID].deductResource(BRICK, 1);
            GameManager.players[activePlayerID].deductResource(LUMBER, 1);
            GameManager.players[activePlayerID].deductResource(WOOL, 1);
            GameManager.players[activePlayerID].deductResource(WHEAT, 1);
        }

        // Resource checking, etc. is not necessary in the GUI as only buildable
        // settlements will be clickable
    }

    /**
     * Method handles the building of cities for the text based game by
     * deducting the appropriate resources from the given player, marking the
     * intersection as occupied, and updating the player's information.
     *
     * @param playerID
     *
     */
    public static int buildCity(int playerID) {

        Player activePlayer = GameManager.players[playerID];

        Intersection settlementLocation;

        // Check if player has the required 3 Ore and 2 Wheat
        if (activePlayer.resourceMaterials[GameManager.ORE] >= 3 || activePlayer.resourceMaterials[GameManager.WHEAT] >= 2) {

            //Scanners and print lines will be obsolete after Gui is made
            Scanner sc = new Scanner(System.in);

            //Gets coordinate location for intersection
            System.out.println("Enter the x and y values of the location of your settlement:");
            Double xVal = GameManager.alphaToSqrt(sc.nextLine());
            Double yVal = Double.parseDouble(sc.nextLine());

            settlementLocation = Intersection.searchIntersections(new Coordinate(xVal, yVal));

            if (settlementLocation.getPlayer() != playerID) {
                System.out.println("you must have a settlement on the location before you can build a city");
                return -1;
            }

            //if there was a problem finding the intersection, -1 is returned
            if (settlementLocation == GameManager.errorIntersection) {
                return -1;
            }

            // Change settlement type to 2 (city)
            settlementLocation.setSettlementType(2);

            // deduct resources
            activePlayer.deductResource(ORE, 3);
            activePlayer.deductResource(WHEAT, 2);

            //update player's cities
            activePlayer.addCity(settlementLocation);
            return 0;

            // If inadequate resources, inform which    
        } else if (activePlayer.resourceMaterials[ORE] < 3) {
            System.out.println("You do not have enough Ore");
            return -1;
        } else if (activePlayer.resourceMaterials[WHEAT] < 2) {
            System.out.println("You do not have enough Wheat");
            return -1;
        } else {
            System.out.println("Unknown error.¯\\_(ツ)_/¯");
            return -1;
        }

    }

    /**
     * Method handles the building of cities for the graphics format of the game
     * by deducting the appropriate resources from the given player, marking the
     * intersection as occupied, and updating the player's information.
     *
     * @param activePlayerID
     * @param settlementToBuy
     */
    public static void GUIBuildCity(int activePlayerID, Intersection settlementToBuy) {
        // Player object for ease of use
        Player activePlayer = GameManager.players[activePlayerID];
        // Set settlement type to city (2)
        settlementToBuy.setSettlementType(2);
        // Fill in circle - stroke will already be set as a settlement is required to build city
        settlementToBuy.getCircle().setFill(activePlayer.getColor());
        // Deduct resources
        activePlayer.deductResource(ORE, 3);
        activePlayer.deductResource(WHEAT, 2);

        //update player's cities
        activePlayer.addCity(settlementToBuy);

        // As with settlement, extensive checking is not required in this method
        // As the UI will handle that
    }

    /**
     * Method handles the building of roads for the text based game by deducting
     * the appropriate resources from the given player, marking the intersection
     * as occupied, and updating the player's information.
     *
     * @param playerID
     *
     */
    public static int buildRoad(int playerID) {

        // A road requires two endpoints
        Intersection endpointA;
        Intersection endpointB;
        // Boundary will be the location of the road, once found
        Boundary roadLocation;

        // Player object for ease of use
        Player activePlayer = GameManager.players[playerID];

        // Check if player has required 1 brick and 1 lumber
        if (activePlayer.resourceMaterials[GameManager.BRICK] >= 1 || activePlayer.resourceMaterials[GameManager.LUMBER] >= 1) {

            //Scanners and print lines will be obsolete after Gui is made
            Scanner sc = new Scanner(System.in);

            //Gets coordinate location for first  intersection at one end of road
            System.out.println("Enter the x and y values of the first coordinate on one side of the road:");
            Double xVal = Double.parseDouble(sc.nextLine());
            Double yVal = Double.parseDouble(sc.nextLine());

            //Finds first intersection and sets endpointA
            endpointA = Intersection.searchIntersections(new Coordinate(xVal, yVal));

            //if there was a problem finding the endpoint intersection, -1 is returned
            if (endpointA == GameManager.errorIntersection) {
                return -1;
            }

            //Gets coordinate location for second intersection at other end of road
            System.out.println("Enter the x and y values of the coordinate of the other side of the road:");

            Double xVal2 = Double.parseDouble(sc.nextLine());
            Double yVal2 = Double.parseDouble(sc.nextLine());

            //Finds second intersection and sets endpointB
            endpointB = Intersection.searchIntersections(new Coordinate(xVal2, yVal2));

            //if there was a problem finding the endpoint intersection, -1 is returned
            if (endpointB == GameManager.errorIntersection) {
                return -1;
            }

            //Finds road based on endpoints and sets roadLocation
            roadLocation = Boundary.searchBoundary(endpointA, endpointB);

            //if there was a problem finding the road, -1 is returned
            if (endpointB == GameManager.errorIntersection) {
                return -1;
            }

            //checks to see if road is occupiable
            //If occupiable, ownership of the road is set to the appropriate player
            if (roadLocation.isOccupiable(playerID)) {
                roadLocation.setPlayer(playerID);

                //increment the player's road number
                activePlayer.addRoad(roadLocation);
                //recalculate longest road
                calculateLongestRoad();

                // Deduct resources
                activePlayer.deductResource(LUMBER, 1);
                activePlayer.deductResource(BRICK, 1);
            }
            return 0;
            // Inform player which resource was not found
        } else if (activePlayer.resourceMaterials[GameManager.LUMBER] < 1) {
            System.out.println("You do not enough Lumber");
            return -1;
        } else if (activePlayer.resourceMaterials[GameManager.BRICK] < 1) {
            System.out.println("You do not enough Brick");
            return -1;
        } else {
            System.out.println("Unknown Error. ¯\\_(ツ)_/¯");
            return -1;
        }
    }

    /**
     * Method handles the building of roads for the graphics format of the game
     * by deducting the appropriate resources from the given player, marking the
     * intersection as occupied, and updating the player's information.
     *
     * @param activePlayerID
     * @param roadToBuy
     */
    public static void GUIBuildRoad(int activePlayerID, Boundary roadToBuy) {
        // Player object for ease of use
        Player activePlayer = GameManager.players[activePlayerID];
        // Make road appear thicker
        roadToBuy.getLine().setStrokeWidth(4);
        // Change color to player's color
        roadToBuy.getLine().setStroke(activePlayer.getColor());
        // Set the road to owned by the player
        roadToBuy.setPlayer(activePlayerID);
        // Deduct resource
        if (!GameManager.isSetUpPhase) {
            activePlayer.deductResource(LUMBER, 1);
            activePlayer.deductResource(BRICK, 1);
        }

        //update player's roads
        activePlayer.addRoad(roadToBuy);

        calculateLongestRoad();
        // As with settlement, extensive checking is not required in this method
        // As the UI will handle that
    }

    /**
     * Method compares the road count of each player in the game to determine
     * who has the most and sets the longestRoad field for each player
     * accordingly.
     */
    public static void calculateLongestRoad() {
        // NOTE: our method simplifies longest road from the board game version
        // our method would be more aptly titled "owner of most roads"
        // as it only checks the number of roads, and ignores if they are contiguous
        // or have forks, as the official rules call for

        int longestRoadLength = 0;
        int roadBuilderSupreme = -1;

        // For each player, check if they have more roads than the longest road value
        // If they do, their total becomes the new longest road value
        // And they become the "roadbuildersupreme"
        for (Player p : GameManager.players) {
            if (p.getRoadCount() > longestRoadLength) {
                roadBuilderSupreme = p.getPlayerID();
            }
        }
        // Set player's longestRoad field to true
        GameManager.players[roadBuilderSupreme].setLongestRoad(true);

        // Print which player has the longest road
        System.out.println("Player " + (roadBuilderSupreme + 1) + " has the longest road.");
    }

    /**
     * Method compares the army size of each player in the game to determine who
     * has the most and sets the largestArmy field for each player accordingly.
     */
    public static void calculateLargestArmy() {

        int largestArmySize = 0;
        int warMonger = -1;

        // For each player, check if they played have more knights than the current max
        // If they do, their total becomes the new max
        // And they become the "warMonger"
        for (Player p : GameManager.players) {
            if (p.getKnightCards() > largestArmySize) {
                warMonger = p.getPlayerID();

            }
        }
        // Set player's longestRoad field to true
        GameManager.players[warMonger].setLongestRoad(true);
        // Print owner of largest army
        System.out.println("Player " + (warMonger + 1) + " has the largest army.");
    }

    // Creates "deck" of DevelopmentCards
    /**
     * Method creates a "deck" of 25 development cards.
     */
    public void generateDevelopmentCards() {
        // Sets up deck the same way every game
        // No need to shuffle as cards are assigned at random
        for (int i = 0; i < DEVELOPMENT_CARD_COUNT; i++) {

            // 14 Knight cards
            // 5 Victory Point cards
            // 2 Road cards
            // 2 Monopoly cards
            // 2 Year of Plenty Cards
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

    }

    /**
     * Method deducts the appropriate resources from the active player and
     * rewards in return a randomly selected development card.
     *
     * @param playerID
     */
    public void buyDevelopmentCard(int playerID) {
        // Player object for ease of use
        Player currentPlayer = GameManager.players[playerID];

        // Check player has the required 1 ore, 1 wool, 1 wheat
        if (currentPlayer.getResourceCount(ORE) < 1
                || currentPlayer.getResourceCount(WOOL) < 1
                || currentPlayer.getResourceCount(WHEAT) < 1) {
            // If not, inform them they don't have enough
            System.out.println("You do not have adequate resources to buy a development card.");

        } else if (remainingCards == 0) {
            // If there are no cards left, alert user
            System.out.println("There are no remaining development cards to buy.");

        } else {

            // boolean flag will control loop
            boolean cardAssigned = false;

            while (!cardAssigned) {
                // As long as a valid card has not been assigned
                // Select a random card number (0 - 24)
                int card = (int) (Math.random() * 25);
                // Find card in "deck"
                DevelopmentCard d = developmentCards[card];
                // If it is unowned,
                if (d.getPlayer() == -1) {
                    // assign it to current player
                    d.setPlayer(playerID);
                    // set loop control to true
                    cardAssigned = true;
                    // Inform player of purchase
                    System.out.println("You have purchased a " + d.getTitle() + " Card.");

                    // Auto-play victory point card
                    if (d.getTitle().equals("Victory Point")) {
                        System.out.println("Victory Point Cards play automatically");
                        ((VictoryPointCard) d).play(playerID);
                    }

                }

            }

            // Deduct number of cards remaining
            remainingCards--;
            // Deduct resources
            currentPlayer.deductResource(ORE, 1);
            currentPlayer.deductResource(WOOL, 1);
            currentPlayer.deductResource(WHEAT, 1);

        }

    }

    // Searches "deck" for cards owned by player
    /**
     * Method searches through all development cards for cards belonging to the
     * active player.
     *
     * @param playerID
     * @return
     */
    public boolean findDevelopmentCards(int playerID) {
        // Flag alerts player when a card is found that they own
        boolean haveCard = false;

        for (DevelopmentCard d : developmentCards) {

            // If an owned, unplayed card is found
            if (d.getPlayer() == playerID && !d.isPlayed()) {
                // Print the title and ask user if they want to play it
                String cardType = d.getTitle();

                System.out.println("You have a playable " + cardType + " Card. Would you like to play it? ");

                haveCard = true;

                Scanner sc = new Scanner(System.in);
                String play = sc.next();

                // If user says yes, play the card
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

        return haveCard;
    }

    /**
     * Method determines the exchange rates unique to each player based on the
     * player's settlement of harbors.
     *
     * @param playerID
     * @return Array of exchange rates for each resource
     */
    public static int[] exchangeRates(int playerID) {

        //each index of this array corresponds with the resources in the resource array
        //all exchange rates begin as a 4:1 ratio
        int[] exchangeRates = {4, 4, 4, 4, 4};

        for (Boundary b : GameManager.externalBoundaries) {

            // Iterate through the arraay of external Boundaries
            // (i.e. the ones that may have harbors)
            // Assign the harbor value to harbor
            int harbor = b.getHarbor();

            // If the harbor is owned by the player and is actually a harbor
            if (b.getPlayer() == playerID && harbor != NO_RESOURCE) {

                // If it's a general harbor
                if (harbor == GENERAL_HARBOR) {
                    // Change all exchange rates from 4 to 3
                    for (int i : exchangeRates) {
                        // This check avoids accidentally overwriting a specific resource harbor that
                        // has already set the exchange rate to 2
                        if (i == 4) {
                            i = 3;
                        }
                    }
                } else {
                    // Otherwise, it must be a specific resource
                    // Set that resource's exchange rate to 2
                    exchangeRates[harbor] = 2;
                }

            }
        }

        return exchangeRates;
    }

    /**
     * Method facilitates trade between a player and the bank utilizing fixed
     * exchange rates.
     *
     * @param playerID
     */
    public static void bankTrade(int playerID) {

        String continueTrading = "y";

        // exchangeRates are used for trade with bank
        int exchangeRates[] = exchangeRates(playerID).clone();

        while (continueTrading.equals("y")) {

            // Create new Trade object and set it to a bank trade
            Trade tradeDeal = new Trade(playerID);
            tradeDeal.setBankTrade(true);

            Scanner sc = new Scanner(System.in);

            // Ask resource to give
            System.out.println("Which resource would you like to trade away?");
            tradeDeal.setOfferedResource(Integer.parseInt(sc.nextLine()) - 1);

            // Get exchange rate for that resource
            int xrate = exchangeRates[tradeDeal.getOfferedResource()];

            // Prompt user to trade away in multiples of exchange rate
            // ex: if exchange rate is 3, they must enter 3,6, or 9
            System.out.println(xrate + " of this resource is enough to get one resource from the bank\n "
                    + "How much of that resource would you like to trade? (enter multiples of " + xrate + ")");
            tradeDeal.setOfferedAmount(Integer.parseInt(sc.nextLine()));

            // Ask player for the resource to receive
            System.out.println("Which resource would you like in return?");
            tradeDeal.setRequestedResource(Integer.parseInt(sc.nextLine()) - 1);

            // Set requested amount to amount offered divided by exchange rate
            // NOTE: these are integers and if you enter 7 instead of 6,
            // you will just lose that one extra resource
            tradeDeal.setRequestedAmount(tradeDeal.getOfferedAmount() / xrate);

            // Execute trade
            Trade.executeTrade(tradeDeal);

            // Offer option to conduct another trade
            System.out.println("Would you like to continue trading with the bank?");

            continueTrading = sc.nextLine();
        }

    }

    /**
     * Method facilitates trade between players.
     *
     * @param playerID
     */
    public static void playerTrade(int playerID) {

        String continueTrading = "y";

        while (continueTrading.equals("y")) {

            Trade tradeDeal = new Trade(playerID);

            Scanner sc = new Scanner(System.in);

            // Prompt player to select resource to GIVE
            System.out.println("Which resource would you like to trade away? Enter 0 to represent any resource.");
            tradeDeal.setOfferedResource(Integer.parseInt(sc.nextLine()) - 1);

            // Amount to GIVE
            System.out.println("How much of this resource would you like to trade?");
            tradeDeal.setOfferedAmount(Integer.parseInt(sc.nextLine()));

            // Type to RECEIVE
            System.out.println("Which resource would you like in return? Enter 0 to represent any resource.");
            tradeDeal.setRequestedResource(Integer.parseInt(sc.nextLine()) - 1);

            // Amount to RECEIVE
            System.out.println("How much of this resource would you like in return?");
            tradeDeal.setRequestedAmount(Integer.parseInt(sc.nextLine()));

            // Player to offer trade to
            System.out.println("To whom would you like to offer this trade? Enter 0 to represent all other players.");
            tradeDeal.setTradingPartner(Integer.parseInt(sc.nextLine()) - 1);

            // Offer trade (includes getting response/counter-offer
            tradeDeal.distributeTradeRequest();

            // Allow option of additional trades
            System.out.println("Would you like to make another player trade");
            continueTrading = sc.nextLine();
        }
    }

}
