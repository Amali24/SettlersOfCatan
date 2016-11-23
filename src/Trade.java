
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
            READ_THIS_FIRST.txt
            CatanGameboard.jpeg

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
                                                     
                                                     




 */
import java.util.Scanner;

class Trade {

    // "Constants" used to refer to resource type in various methods
    // general harbor is used in determining port placement
    static final int NO_RESOURCE = -1;
    static final int BRICK = 0;
    static final int LUMBER = 1;
    static final int ORE = 2;
    static final int WHEAT = 3;
    static final int WOOL = 4;
    static final int GENERAL_HARBOR = 5;

    private boolean initialOffering;
    private boolean accepted;

    private boolean bankTrade;

    private int requestingPlayer;
    private int tradingPartner;
    private int offeredResource;
    private int offeredAmount;
    private int requestedResource;
    private int requestedAmount;

    Trade() {
        accepted = false;
    }

    Trade(int requestingPlayer) {
        this.requestingPlayer = requestingPlayer;
        this.initialOffering = true;
        this.accepted = false;

    }

    /*
	Trade(Trade initialOffer){
		
		this.initialOffering = false;
		this.accepted = true;
		this.offeredAmount = initialOffer.getRequestedAmount();
		this.requestedAmount = initialOffer.getOfferedAmount();
		
		if(initialOffer.requestedResource != -1){
			this.requestedResource = initialOffer.getOfferedResource();
		}
		if(initialOffer.offeredResource != -1){
			this.offeredResource = initialOffer.getRequestedResource();
		}
		
	}

     */
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

    public void distributeTradeRequest() {

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
                    }

                }
            }

            int numAccepted = 0;

            System.out.println("BACK TO PLAYER " + (requestingPlayer + 1) + "\n");

            for (int i = 0; i < offerResponses.length; i++) {

                Trade offer = offerResponses[i];

                if (i != requestingPlayer && offer.isAccepted()) {
                    if (offeredResource == -1 || requestedResource == -1) {
                        System.out.println("Player " + (i + 1) + " has made a counter offer.");
                        System.out.println("They have offered to trade you " + offer.offeredAmount + " of resource " + offer.offeredResource
                                + " in exchange for " + offer.requestedAmount + " of resource " + offer.requestedResource);

                        numAccepted++;
                    } else {
                        System.out.println("Player " + (i + 1) + " has accepted your offer");
                        numAccepted++;
                    }

                }
            }
            int finalResponse;

            if (numAccepted == 0) {
                System.out.println("No one has accepted your trade");
            } else {
                System.out.println("Enter the number of the player you would like to trade with.");
                System.out.println("If you reject these offers, enter -1.");

                finalResponse = Integer.parseInt(sc.nextLine());

                if (finalResponse != -1) {

                    Trade finalTrade = offerResponses[finalResponse];

                    if (tradingPartner == -1) {
                        finalTrade.setTradingPartner(finalResponse);
                    }

                    executeTrade(finalTrade);

                }

            }

        } else if (requestedResource == -1 || offeredResource == -1) {
            Trade counterOffer = tradePrompt(tradingPartner);

            System.out.println("BACK TO PLAYER " + (requestingPlayer + 1) + "\n");

            if (counterOffer.isAccepted()) {

                System.out.println("Player " + (counterOffer.requestingPlayer + 1) + " has made a counter offer.");
                System.out.println("They have offered to trade you " + counterOffer.offeredAmount + " of resource " + counterOffer.offeredResource
                        + " in exchange for " + counterOffer.requestedAmount + " of resource " + counterOffer.requestedResource);

                System.out.println("Do you accept this offer?");

                String accept = sc.nextLine();

                if (accept.equals("y")) {
                    executeTrade(counterOffer);
                }

            } else {
                if (tradePrompt(tradingPartner).isAccepted()) {
                    executeTrade(this);
                } else {
                    System.out.println("Your trade has been denied");
                }
            }
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
                        }

                    }
                }

                int numAccepted = 0;

                System.out.println("BACK TO PLAYER " + (requestingPlayer + 1) + "\n");

                for (int i = 0; i < offerResponses.length; i++) {

                    Trade offer = offerResponses[i];

                    if (i != requestingPlayer && offer.isAccepted()) {
                        if (offeredResource == -1 || requestedResource == -1) {
                            System.out.println("Player " + (i + 1) + " has made a counter offer.");
                            System.out.println("They have offered to trade you " + offer.offeredAmount + " of resource " + offer.offeredResource
                                    + " in exchange for " + offer.requestedAmount + " of resource " + offer.requestedResource);

                            numAccepted++;
                        } else {
                            System.out.println("Player " + (i + 1) + " has accepted your offer");
                            numAccepted++;
                        }

                    }
                }
                int finalResponse;

                if (numAccepted == 0) {
                    System.out.println("No one has accepted your trade");
                } else {
                    System.out.println("Enter the number of the player you would like to trade with.");
                    System.out.println("If you reject these offers, enter -1.");

                    finalResponse = Integer.parseInt(sc.nextLine());

                    if (finalResponse != -1) {

                        Trade finalTrade = offerResponses[finalResponse];

                        if (tradingPartner == -1) {
                            finalTrade.setTradingPartner(finalResponse);
                        }

                        executeTrade(finalTrade);

                    }

                }

            } else if (requestedResource == -1 || offeredResource == -1) {
                counterOffer = tradePrompt(tradingPartner);

                System.out.println("BACK TO PLAYER " + (requestingPlayer + 1) + "\n");

                if (counterOffer.isAccepted()) {

                    System.out.println("Player " + (counterOffer.requestingPlayer + 1) + " has made a counter offer.");
                    System.out.println("They have offered to trade you " + counterOffer.offeredAmount + " of resource " + counterOffer.offeredResource
                            + " in exchange for " + counterOffer.requestedAmount + " of resource " + counterOffer.requestedResource);

                    System.out.println("Do you accept this offer?");

                    String accept = sc.nextLine();

                    if (accept.equals("y")) {
                        executeTrade(counterOffer);
                    }

                } else {
                    if (tradePrompt(tradingPartner).isAccepted()) {
                        executeTrade(this);
                    } else {
                        System.out.println("Your trade has been denied");
                    }
                }
            }
        }
    }

    public Trade tradePrompt(int currentTradingPartner) {

        System.out.println("\nTRADE REQUEST FOR PLAYER " + (currentTradingPartner + 1)
                + "\n__________________________\n");

        boolean tradeOption = false;
        boolean receiveOption = false;
        String acceptTrade = "y";

        Scanner sc = new Scanner(System.in);

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

        if (acceptTrade.equals("y")) {

            Trade counterOffer = new Trade();
            counterOffer.setInitialOffering(false);
            counterOffer.setAccepted(true);
            counterOffer.setRequestingPlayer(tradingPartner);
            counterOffer.setTradingPartner(requestingPlayer);
            counterOffer.setOfferedAmount(requestedAmount);
            counterOffer.setRequestedAmount(offeredAmount);

            if (receiveOption) {
                counterOffer.setRequestedResource(offeredResource);
            }

            if (tradeOption) {
                counterOffer.setOfferedResource(requestedResource);
            }

            if (receiveOption) {
                System.out.println("Which resource would you like to offer?");
                counterOffer.setOfferedResource(Integer.parseInt(sc.nextLine()));

            }

            if (tradeOption) {
                System.out.println("Which resource would you like in return?");
                counterOffer.setRequestedResource(Integer.parseInt(sc.nextLine()));
            }

            if (receiveOption || tradeOption) {
                return counterOffer;

            } else {
                accepted = true;
                return this;
            }

        } else {
            System.out.println("Your denial has been sent.\n\n");
            Trade denied = new Trade();
            return denied;

        }

    }

    public static void executeTrade(Trade finalTrade) {

        Player rPlayer = GameManager.players[finalTrade.getRequestingPlayer()];
        
        rPlayer.deductResource(finalTrade.offeredResource, finalTrade.offeredAmount);
        rPlayer.addResource(finalTrade.requestedResource, finalTrade.requestedAmount);
        
        System.out.println("Trade Complete.");
        rPlayer.printResources();
        
        if(!finalTrade.isBankTrade()){
        Player tPartner = GameManager.players[finalTrade.getTradingPartner()];
        tPartner.deductResource(finalTrade.requestedResource, finalTrade.requestedAmount);
        tPartner.addResource(finalTrade.offeredResource, finalTrade.offeredAmount);
        tPartner.printResources();
        }
    }

}
