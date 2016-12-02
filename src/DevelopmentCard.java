
import java.util.Scanner;

/*  
                Development Card Logic - Settlers of Catan

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
            DevelopmentCard.java (Current File)
            GameManager.java
            HexTile.java
            Intersection.java 
            Player.java
            Trade.java
            READ_THIS_FIRST.txt
            CatanGameboard.jpeg

Classes:    DevelopmentCard
            KnightCard
            VictoryPointCard
            RoadBuildingCard
            MonopolyCard
            YearOfPlenty

                                    Summary:
The following code sets up the logic for development cards. A development card 
is a resource in the game Settlers of Catan that may be purchased for the price 
of an ore, wool, and grain. There are 25 development cards in the game, 14 Knight 
Cards, 5 Victory Point Cards, 2 Road Building, 2 Monopoly Cards, and 2 Year of 
Plenty Cards. Each type of development card will have its own class to handle
its specific purpose. The first class in this file is an abstract class,
DevelopmentCard, that sets up the shared logic applicable to all development
cards. Following that class are the classes for each type of development card,
all of which inherit the DevelopmentCard superclass.


Activity:	  -Date-             -Person-               -Updates-
            November 7, 2016   		AS          *Created DevelopmentCard class
                                                    *Created KnightCard class
                                                    *Created VictoryPointCard class
                                                    *Created RoadBuildingCard class
                                                    *Created MonopolyCard class
                                                    *Created YearOfPlentyCard class
 				
            November 14, 2016		AS          *Updated RoadBuildingCard class
                                                     to work with new buildRoad 
                                                     method in GameManager

                                        AT          *Debugged test code and 
                                                     various methods

            November 19, 2016           AS          *All child card classes 
                                                     except victoryPointCard
                                                     now deduct the current
                                                     players's developmentCardCount
                                                     at the end of their play methods
                                                    *Monopoly and Year of Plenty 
                                                     Card classes now print
                                                     the updated resources
                                                    *KnightCard class calls 
                                                     Bank.calculateLargestArmy()
                                                     inside play() method
            
            November 23, 2016           AT          * Added many method comments
                                                    * Changed nextInt calls to
                                                      parseInt calls to avoid
                                                      scanner errors
 											

 */

 /*_____________________________________________________________________________

                             DEVELOPMENTCARD CLASS
 This is an abstract class that will be inherited by each specific development
 card class. It contains the shared attributes of each development card such as 
 a name and description field...

 _______________________________________________________________________________
 */
public abstract class DevelopmentCard {

    //  			Class Variables
    //_____________________________________________________________________________
    private String title;
    private String description;
    private int player = -1;
    boolean played;

    //                      Accessors and Mutators
    //_____________________________________________________________________________
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int playerID) {
        this.player = playerID;
    }

    public boolean isPlayed() {
        return played;
    }

    public void setPlayed(boolean played) {
        this.played = played;
    }

}


/*_____________________________________________________________________________

                              KNIGHTCARD CLASS

This class is a subclass of DevelopmentCard and contains the attributes and
methods unique to the Knight Card. There are 14 Knight Cards in each game of
Settlers of Catan. Each Knight Card allows a player to move the thief to a
different tile on the gameboard and then steal a random material from a 
player settled on a corner of that tile.

_______________________________________________________________________________
 */
class KnightCard extends DevelopmentCard {

//   				Constructors
//_____________________________________________________________________________
    KnightCard() {

        this.setTitle("Knight");
        this.setDescription("Move the Robber to a new tile. Steal a resource "
                + "material from a player with an adjacent settlement.");
        this.setPlayed(false);

    }

//                                Methods
//_____________________________________________________________________________
    public void play(int playerID) {
        System.out.println("\n\t\tPLAYING KNIGHT CARD");

        // Player object for ease of use
        Player currentPlayer = GameManager.players[playerID];

        // If card is already played, print that to console
        if (this.isPlayed() == true) {
            System.out.println("This development card was already played.");
        } else {
            Scanner sc = new Scanner(System.in);

            int tile;

            boolean moved = false;

            // Loop until player selects a valid location (not the current one)
            while (!moved) {
                // Allow player to select tile to move robber to
                System.out.println("Select a tile to move robber to.");
                tile = Integer.parseInt(sc.nextLine());
                moved = GameManager.moveRobber(tile, playerID);
            }

            // Add card to player's total played
            currentPlayer.addKnightCard();
            // Deduct card from the number the player has available
            currentPlayer.deductDevelopmentCard();
            // Re-calculate largest army
            Bank.calculateLargestArmy();
            // Mark card as played
            this.setPlayed(true);
        }
    }

}


/*_____________________________________________________________________________

                           VICTORY POINT CARD CLASS

This class is a subclass of DevelopmentCard and contains the attributes and
methods unique to the Victory Point Card. There are 5 Victory Point Cards in 
each game of Settlers of Catan. Each Victory Point Card gives a player an
extra victory point that cannot be seen by the other players until the end
of the game.

_______________________________________________________________________________
 */
class VictoryPointCard extends DevelopmentCard {

//	  			Constructors
//_____________________________________________________________________________
    VictoryPointCard() {

        this.setTitle("Victory Point");
        this.setDescription("This card provides you with an additional victory point. "
                + "This point remains invisible to your opponents until the end of the game");
        this.setPlayed(false);

    }

//                                 Methods
//_____________________________________________________________________________
    public void play(int playerID) {

        System.out.println("\n\t\tPLAYING VICTORY POINT CARD");

        Player currentPlayer = GameManager.players[playerID];

        if (this.isPlayed() == true) {
            System.out.println("This development card was already played.");
        }
        currentPlayer.addVictoryPointCard();

        System.out.println("Victory point added.");

        this.setPlayed(true);
    }

}

/*_____________________________________________________________________________

                            ROAD BUILDING CARD CLASS

This class is a subclass of DevelopmentCard and contains the attributes and
methods unique to the Road Building Card. There are 2 Road Building Cards in 
each game of Settlers of Catan. Each Road Building Card allows a player to 
build 2 new roads at the beginning of a turn without having to trade in
resource materials.
_______________________________________________________________________________
 */
class RoadBuildingCard extends DevelopmentCard {

//				  Constructors
//_____________________________________________________________________________
    RoadBuildingCard() {

        this.setTitle("Road Building");
        this.setDescription("This card allows you to build two new roads.");
        this.setPlayed(false);

    }

//                                  Methods
//_____________________________________________________________________________
    public void play(int playerID) {

        System.out.println("\n\t\tPLAYING ROAD BUILDING CARD");

        Player currentPlayer = GameManager.players[playerID];

        if (this.isPlayed() == true) {
            System.out.println("This development card was already played.");
        } else {

            System.out.println("Build first road.");
            Bank.buildRoad(playerID);

            System.out.println("Build second road.");
            Bank.buildRoad(playerID);

            currentPlayer.deductDevelopmentCard();
            this.setPlayed(true);
        }
    }

}


/*_____________________________________________________________________________

                            MONOPOLY CARD CLASS

This class is a subclass of DevelopmentCard and contains the attributes and
methods unique to the Monopoly Card. There are 2 Monopoly Cards in each game 
of Settlers of Catan. Each Monopoly Card allows a player to select a type of 
resource material to take from all of their opponents. Each opponent will
lose their ENTIRE supply of this material to the player with the card.

_______________________________________________________________________________
 */
class MonopolyCard extends DevelopmentCard {

//			      Class Variables
//_____________________________________________________________________________
    int totalResourceCount = 0;

//       			Constructors
//_____________________________________________________________________________
    MonopolyCard() {

        this.setTitle("Monopoly");
        this.setDescription("Select a type of resource material. "
                + "All other players will give you their entire supply of this material.");
        this.setPlayed(false);

    }

//                                 Methods
//_____________________________________________________________________________
    public void play(int playerID) {

        System.out.println("\t\tPLAYING MONOPOLY CARD");
        System.out.println("Enter the resource you would like to take from everyone: ");

        Scanner sc = new Scanner(System.in);
        int resource = Integer.parseInt(sc.nextLine());

        Player currentPlayer = GameManager.players[playerID];

        if (this.isPlayed() == true) {
            System.out.println("This development card was already played.");
        } else {
            for (Player player : GameManager.players) {
                // Calculate the total number of the chosen resource that all players have
                // Including the current player
                totalResourceCount += player.getResourceCount(resource);
                // Set every player's current number of resources to zero
                player.resetResource(resource);
            }

            // Add the "pool" of resources to the player who played the card
            currentPlayer.addResource(resource, totalResourceCount);
            // Print the player's resources after stealing
            currentPlayer.printResources();
            // Deduct the card
            currentPlayer.deductDevelopmentCard();
            // Mark card as played
            this.setPlayed(true);
        }

    }
}


/*_____________________________________________________________________________

                            YEAR OF PLENTY CARD CLASS

This class is a subclass of DevelopmentCard and contains the attributes and
methods unique to the Year of Plenty Card. There are 2 Year of Plenty Cards 
in each game of Settlers of Catan. Each Year of Plenty Card allows a player 
to select two resource materials to be added to their supply.

_______________________________________________________________________________
 */
class YearOfPlentyCard extends DevelopmentCard {

//				  Constructors
//_____________________________________________________________________________
    YearOfPlentyCard() {

        this.setTitle("Year of Plenty");
        this.setDescription("Choose two resource materials to recieve from the bank.");
        this.setPlayed(false);

    }

//				    Methods
//_____________________________________________________________________________
    public void play(int playerID) {

        System.out.println("\n\t\tPLAYING YEAR OF PLENTY CARD");
        System.out.println("Enter the integer values for the two resources you would like: ");
        
        // Player selects two resources
        Scanner sc = new Scanner(System.in);
        int resource1 = Integer.parseInt(sc.nextLine());
        int resource2 = Integer.parseInt(sc.nextLine());

        Player currentPlayer = GameManager.players[playerID];

        if (this.isPlayed() == true) {
            System.out.println("This development card was already played.");
        } else {
            // Gain one of each of the resources selected
            currentPlayer.addResource(resource1, 1);
            currentPlayer.addResource(resource2, 1);
            
            // Print resource total
            currentPlayer.printResources();
            // Deduct card
            currentPlayer.deductDevelopmentCard();
            // Mark card as played
            this.setPlayed(true);
        }

    }
}
