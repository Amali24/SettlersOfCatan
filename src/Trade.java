
/*  
                            Trade - Settlers of Catan

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
            Player.java
            Trade.java (Current File)

Classes:    Trade

                                    Summary:
This class sets up trades for players. It allows a player to create a trade object
that includes all the terms for a trade, then disutribute that trade request to 
other players. When a trade is agreed upon by all parties, it executes the trade
deducting and addint resources to players' supplies according to the trade terms.
This class is also used to execute trades between a player and the bank.

Activity:	  -Date-             -Person-               -Updates-
            November 17, 2016           AS          *Class Created
                                                    *Created variables and 
                                                     getters and setters
                                                    *Wrote methods:
                                                     distributeTradeRequest()
                                                     tradePrompt()
                                                     executeTrade()

            November 19, 2016           AS          *Wrote documentation header
                                                    *Added bank trading capabilities
                                                     with bankTrade boolean and
                                                     changes to executeTrade()
            
            November 23, 2016           AS          *Standardied class formatting
                                                    *Added comments
                                                    *Added Javadoc documentation
                                                    *Overhauled, improved, rewrote,
                                                     and debugged code
                                                     
                                                     




 */
import java.util.Scanner;

/**
 * <code> Trade </code> class facilitates trade for players both with other
 * players and with the bank.
 */
class Trade {

//                              Class Properties
// _____________________________________________________________________________
    // "Constants" used to refer to resource type in various methods
    // general harbor is used in determining port placement
    static final int NO_RESOURCE = -1;
    static final int BRICK = 0;
    static final int LUMBER = 1;
    static final int ORE = 2;
    static final int WHEAT = 3;
    static final int WOOL = 4;
    static final int GENERAL_HARBOR = 5;

    private boolean bankTrade;          //is this a trade with the bank
    private boolean initialOffering;    //is this the first offer in a trade deal
    private boolean accepted;           //has the tradeing partner accepted the offer

    private int requestingPlayer;       //ID of player who set terms of trade
    private int tradingPartner;         //ID of player to whom trade is offered
    private int offeredResource;        //Resource offered in return for another
    private int offeredAmount;          //Quantity of offeredResource
    private int requestedResource;      //Resource asked for in return
    private int requestedAmount;        //Quantity of requestedResource

//                               Constructors
// _____________________________________________________________________________
    /**
     * <code> Trade </code> constructor used when making an offer response.
     */
    Trade() {
        accepted = false;
    }

    /**
     * <code> Trade </code> constructor used when making an initial offer.
     *
     * @param int ID of player requesting the trade
     */
    Trade(int requestingPlayer) {
        this.requestingPlayer = requestingPlayer;
        this.initialOffering = true;
        this.accepted = false;

    }

//                          Accessors and Mutators
// _____________________________________________________________________________
    public boolean isInitialOffering() {
        return initialOffering;
    }

    public void setInitialOffering(boolean initial0ffering) {
        this.initialOffering = initial0ffering;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean isBankTrade() {
        return bankTrade;
    }

    public void setBankTrade(boolean bankTrade) {
        this.bankTrade = bankTrade;
    }

    public int getRequestingPlayer() {
        return requestingPlayer;
    }

    public void setRequestingPlayer(int requestingPlayer) {
        this.requestingPlayer = requestingPlayer;
    }

    public int getTradingPartner() {
        return tradingPartner;
    }

    public void setTradingPartner(int tradingPartner) {
        this.tradingPartner = tradingPartner;
    }

    public int getOfferedResource() {
        return offeredResource;
    }

    public void setOfferedResource(int offeredResource) {
        this.offeredResource = offeredResource;
    }

    public int getOfferedAmount() {
        return offeredAmount;
    }

    public void setOfferedAmount(int offeredAmount) {
        this.offeredAmount = offeredAmount;
    }

    public int getRequestedResource() {
        return requestedResource;
    }

    public void setRequestedResource(int requestedResource) {
        this.requestedResource = requestedResource;
    }

    public int getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(int requestedAmount) {
        this.requestedAmount = requestedAmount;
    }

//                                 Methods
// _____________________________________________________________________________
    /**
     * <code> cloneTrade </code> method takes one <code> Trade </clone> object
     * and makes it a clone of another.
     *
     * @param clonedTrade
     * @param originalTrade
     */
    public static void cloneTrade(Trade clonedTrade, Trade originalTrade) {

        clonedTrade.setAccepted(originalTrade.isAccepted());
        clonedTrade.setBankTrade(originalTrade.isBankTrade());
        clonedTrade.setInitialOffering(originalTrade.isInitialOffering());
        clonedTrade.setOfferedAmount(originalTrade.getOfferedAmount());
        clonedTrade.setOfferedResource(originalTrade.getOfferedResource());
        clonedTrade.setRequestedAmount(originalTrade.getRequestedAmount());
        clonedTrade.setRequestedResource(originalTrade.getRequestedResource());
        clonedTrade.setRequestingPlayer(originalTrade.getRequestingPlayer());
        clonedTrade.setTradingPartner(originalTrade.getTradingPartner());

    }

    /**
     * <code> distributeTradeRequest </code> method sends a trade offer to a
     * specific player, or, if no player is specified, to all eligible players.
     */
    public void distributeTradeRequest() {

        //Holds the responses of all players prompted
        //each index corresponds with a player ID
        Trade[] offerResponses = new Trade[4];

        Scanner sc = new Scanner(System.in);

        //if no trading partner was specified, a for loops executes to prompt a trade with each player 
        if (tradingPartner == -1) {

            for (Player p : GameManager.players) {

                int currentTradingPartner = p.getPlayerID();
                //does not execute if on the iteration of the requestingPlayer

                if (currentTradingPartner != requestingPlayer) {
                    //Checks that player either has enough of the requested resource, if specified.
                    //If there is no specified requested resource, then a player in prompted as long as they have ANY resources to trade
                    if (p.getResourceCount(requestedResource) >= requestedAmount || (requestedResource == -1 && p.getResourceTotal() > 0)) {
                        offerResponses[currentTradingPartner] = tradePrompt(currentTradingPartner);
                    } else {
                        //if the player does not have enough of the resource 
                        //their index of the array is filled with a Trade object that has the accepted field set to false 
                        offerResponses[currentTradingPartner] = new Trade();
                    }

                }
            }

        } else {

            //if only one player is specified to play with, only they get the trade prompt
            offerResponses[tradingPartner] = tradePrompt(tradingPartner);

            //all other indexes are set to a rejected trade object
            for (Player p : GameManager.players) {
                if (p.getPlayerID() != tradingPartner) {
                    offerResponses[p.getPlayerID()] = new Trade();
                }
            }

        }

        //keeps track of how many players have accepted the offer
        int numAccepted = 0;

        //Now that people have responded, the original player is shown their responses
        System.out.println("BACK TO PLAYER " + (requestingPlayer + 1) + "\n");

        //iterates through the offer responses
        for (int i = 0; i < offerResponses.length; i++) {

            //offer is set to the current response
            Trade offer = offerResponses[i];

            //if statement checks to make sure that the current offer is accepted
            //and that the current index does not correspond with the requestingPlayer
            //because the requestingPLayer would not have been prompted to generate an offer response
            if (i != requestingPlayer && offer.isAccepted()) {

                //this if statement checks to see if the initial trade offer did not specify a resource
                //in which case this offer response is not a simple yes or no to the original offer,
                //but a counter offer, a new Trade object, with a newly sepcified resource 
                //that must be accepted or rejected by the original player
                if (offeredResource == -1 || requestedResource == -1) {

                    //if statement checkes that, if a new requestedResource was specified, the original player has that resource     
                    if (GameManager.players[offer.getTradingPartner()].getResourceCount(offer.getRequestedResource()) >= offer.getRequestedAmount()) {

                        //informs original player of a viable counter offer
                        System.out.println("Player " + (i + 1) + " has made a counter offer.");
                        System.out.println("They have offered to trade you " + offer.getOfferedAmount() + " of resource " + offer.getOfferedResource()
                                + " in exchange for " + offer.getRequestedAmount() + " of resource " + offer.getRequestedResource());

                        numAccepted++;
                    }

                } else {

                    //if there were no unspecified resources in the original trade
                    //this simple accepted statement is printed
                    System.out.println("Player " + (i + 1) + " has accepted your offer");
                    numAccepted++;
                }

            }
        }

        //by this point all trade deals have been either accepted and rejected and the terms have been finalized
        //now they just need to be executed, and if there were vairable terms in the original offer, the
        //original player needs to approve them
        int finalResponse;  //holds the final choice of the original player

        //if no one accepted the trade and/or there were no viable counter offers
        if (numAccepted == 0) {

            //print appropriate rejection statement
            if (tradingPartner == -1) {
                System.out.println("No one has accepted your trade.");
            } else {
                System.out.println("Player " + (tradingPartner + 1) + " rejected your trade request.");
            }

        } else if (tradingPartner != -1 && offeredResource != 1 && requestedResource != -1) {

            //if there were no variable terms in the original trade offer,
            //then an acepted offer automatically goes through without
            //checking with the original player
            Trade finalTrade = offerResponses[tradingPartner];
            executeTrade(finalTrade);

        } else {

            //this else handles all casses in which there was a variable term in the original trade offer
            //and it was accepted
            //then the original player can choose which person's offer to accept
            System.out.println("Enter the number of the player you would like to trade with.");
            System.out.println("If you reject these offers, enter -1.");

            finalResponse = Integer.parseInt(sc.nextLine())-1;

            //if final response  = -1, all trades have been rejected so none of this executes
            if (finalResponse != -1) {

                //find which trade has been accepted
                Trade finalTrade = offerResponses[finalResponse];

                //if no tradingPartner was originally specified
                if (tradingPartner == -1) {

                    //if no resource was modified, the trading partner of the chosen trade is set here
                    if (requestedResource != -1 && offeredResource != -1) {
                        finalTrade.setTradingPartner(finalResponse);
                    } else {
                        //else, the requesting player is set
                        finalTrade.setRequestingPlayer(finalResponse);
                    }
                }

                executeTrade(finalTrade);

            }

            //end of if no trading partner was specified
        }

    }

    /**
     * <code> tradePrompt </code> method prompts players to accept or reject
     * trades and revise the terms.
     *
     * @param currentTradingPartner
     * @return
     */
    public Trade tradePrompt(int currentTradingPartner) {

        System.out.println("\nTRADE REQUEST FOR PLAYER " + (currentTradingPartner + 1)
                + "\n__________________________\n");

        boolean tradeOption = false;    //true if original offer did not set offeredResource
        boolean receiveOption = false;  //true if original offer did not set requestedResource
        String acceptTrade;

        Scanner sc = new Scanner(System.in);

        //the following print statements print out the offer that is being made
        if (requestedResource == NO_RESOURCE) {
            System.out.println("Player " + (requestingPlayer + 1) + " requests you trade " + requestedAmount + " of any resource");
            receiveOption = true;
        } else {
            System.out.println("Player " + (requestingPlayer + 1) + " requests you trade " + requestedAmount + " of resource " + requestedResource);
        }

        if (offeredResource == NO_RESOURCE) {
            System.out.println("in return for " + offeredAmount + " of any resource.");
            tradeOption = true;
        } else {
            System.out.println("in return for " + offeredAmount + " of resource " + offeredResource);
        }

        System.out.println("Do you wish to accept this trade?");
        acceptTrade = sc.nextLine();

        //if player wishes to accept the offer, statement executes
        if (acceptTrade.equals("y")) {

            if (receiveOption || tradeOption) {
                //if receiveOption or trade option are true, then a new Trade object named
                //counterOffer is created and the <code> tradePrompt </code>
                Trade counterOffer = new Trade();

                counterOffer.setInitialOffering(false);
                counterOffer.setAccepted(true);

                //Requesting and trading player are swapped
                counterOffer.setRequestingPlayer(tradingPartner);
                counterOffer.setTradingPartner(requestingPlayer);

                //requested amount and offered amount are swapped
                counterOffer.setOfferedAmount(requestedAmount);
                counterOffer.setRequestedAmount(offeredAmount);

                //if receiveOption is true, then the counterOffer's requestedResource
                //is set to the original trade's offeredResource and players choice is
                //set as offeredResource
                if (receiveOption) {
                    counterOffer.setRequestedResource(offeredResource);

                    System.out.println("Which resource would you like to offer?");
                    counterOffer.setOfferedResource(Integer.parseInt(sc.nextLine())-1);
                }

                //if tradeOption is true, then the counterOffer's offeredResource
                //is set to the original trade's requestedResource and players choice is
                //set as requestedResource
                if (tradeOption) {
                    counterOffer.setOfferedResource(requestedResource);

                    System.out.println("Which resource would you like in return?");
                    counterOffer.setRequestedResource(Integer.parseInt(sc.nextLine())-1);
                }

                return counterOffer;

            } else {
                //if there were no variable terms of the original trade to be changed
                //a clone of the original trade is made and the Accepted field is 
                //set to true.

                Trade clonedTrade = new Trade();
                cloneTrade(clonedTrade, this);

                clonedTrade.setAccepted(true);

                //return cloned Trade
                return clonedTrade;
            }

        } else {
            //if player did not accept trade...
            System.out.println("Your denial has been sent.\n\n");

            //Trade() constructor created empty Trade object, but boolean Accepted field is set to false
            Trade denied = new Trade();

            return denied;

        }

    }

    /**
     * <code> executeTrade </code> method alters players' resources according to
     * the terms of the Trade object passed as parameter and prints out the
     * resources of the involved players.
     *
     * @param finalTrade
     */
    public static void executeTrade(Trade finalTrade) {

        //rPlayer equals the player ojbect whose playerID is held in requestingPLayer
        Player rPlayer = GameManager.players[finalTrade.getRequestingPlayer()];

        //deduct and add the appropriate resources 
        rPlayer.deductResource(finalTrade.offeredResource, finalTrade.offeredAmount);
        rPlayer.addResource(finalTrade.requestedResource, finalTrade.requestedAmount);

        System.out.println("Trade Complete.");
        rPlayer.printResources();

        //if the trade between two players, rather than with the bank
        //the tradingPartner's resources are updated and printed too
        if (!finalTrade.isBankTrade()) {

            Player tPartner = GameManager.players[finalTrade.getTradingPartner()];
            tPartner.deductResource(finalTrade.requestedResource, finalTrade.requestedAmount);
            tPartner.addResource(finalTrade.offeredResource, finalTrade.offeredAmount);
            tPartner.printResources();
        }
    }

}
