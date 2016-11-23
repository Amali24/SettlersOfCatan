/*  
                    	Game Manager - Settlers of Catan

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
            GameManager.java (Current File)
            HexTile.java
            Intersection.java
            Player.java
            Trade.java
            READ_THIS_FIRST.txt
            CatanGameboard.jpeg

Classes:    GameManager


                                    Summary:
The following code contains the GameManager Class for the computerized game, 
Settlers of Catan. This class facilitates most of the game play by setting up
the gameboard, handling player turns and dice rice rolls, and allocating resource
materials. Ultimately this class connects the player experience with the logic
of the game.


Activity:	  -Date-             -Person-               -Updates-
            October 21, 2016		AS          *Hardcoded Intersections,
                                                     adjacentIntersections, and
                                                     Boundaries
                                                    *Logic for adjacent boundaries
                                                    *Logic for assigning harbors

            October 25, 2016		AT          *Hardcoded HexTiles
                                                    *Logic for Desert
                                                    *Logic for Robber
                                                    *Logic for dice rolls
                                                    *Logic for database writing
	
            November 7, 2016   		AS          *Standardized Documentation and 
                                                     Formatting 
												
            November 9, 2016		AS          *Added interaction with Bank class
                                                    *Development cards are now created
                                                     at startup
				 								
            November 14, 2016		AS          *Added intersections, boundaries,
                                                     external boundaries, and tiles arrays
                                                     as class properties
                                                    *Added error instances of the Intersection,
                                                     Boundaries, and Hextile classes
                                                    *Wrote buildSettlement method
                                                    *Wrote buildRoad method

            November 18, 2016           AT          * Fixed error intersection coordinates to
                                                      (-1, -1)
                                                    * Started writing debug mode
                                                    * Moved buildRoad and buildSettlement to Bank

            November 19, 2016           AT          * Continued writing debug mode
                                                    * Nearly finished - needs testing
                                                    * Wrote robberSteal method
                                                    * Tweaked moveRobber method

            November 20, 2016           AS          *Moved code for debugging to
                                                     debugMode method
                                                    *Changed banker to be a static,
                                                     class level variable

            November 23, 2016           AT          * Moved GUI launch to after debug menu
                                                    * Added comments to all methods
                                                    * Fixed some logical errors



 */

import java.sql.SQLException;
import java.util.Scanner;
import javafx.application.Application;

public class GameManager {

//  				Class Properties
//_____________________________________________________________________________
    // Static array of players to hold 4 active game players
    static Player[] players = {new Player(0), new Player(1), new Player(2), new Player(3)};
    static int activePlayerID = 0;

    // "Constants" used to refer to resource type in various methods
    // general harbor is used in determining port placement
    static final int NO_RESOURCE = -1;
    static final int BRICK = 0;
    static final int LUMBER = 1;
    static final int ORE = 2;
    static final int WHEAT = 3;
    static final int WOOL = 4;
    static final int GENERAL_HARBOR = 5;

    // "Constant" doubles refer to mathematical ratio of hexagon sides. Will be used more in GUI
    // But are helpful to have here as they make variable naming more intuitive
    static final double A = 0;
    static final double B = Math.sqrt(3);
    static final double C = Math.sqrt(12);
    static final double D = Math.sqrt(27);
    static final double E = Math.sqrt(48);
    static final double F = Math.sqrt(75);
    static final double G = Math.sqrt(108);
    static final double H = Math.sqrt(147);
    static final double I = Math.sqrt(192);
    static final double J = Math.sqrt(243);
    static final double K = Math.sqrt(300);

    //these are class properties so that other classes may easily access them
    static Intersection[] intersections = new Intersection[54];
    static Boundary[] boundaries = new Boundary[72];
    static Boundary[] externalBoundaries = new Boundary[30];
    static HexTile[] tiles = new HexTile[19];

    //the following variables are to be used as error values for and methods that need to return their type
    static Intersection errorIntersection;
    static Boundary errorBoundary;
    static HexTile errorTile;

    //During the first two rounds of the game, the "set up phase", the gameplay is different
    static boolean isSetUpPhase = true;

    static Bank banker = new Bank();

//  				Methods
//_____________________________________________________________________________
    public static void main(String[] args) {
        // build the game board with randomly generated yields and numbers to roll
        buildGameboard();
        // create the deck of development cards [not random]
        banker.generateDevelopmentCards();
        // Open debug mode, a "no" answer skips this
        debugMode();
        // Launch GUI shell
        Application.launch(ClientUI.class);

        // Scanner not used, but is opened in order to ensure all scanners closed
        Scanner sc = new Scanner(System.in);
        // Close scanner
        sc.close();
    }

    static void debugMode() {
        String quit = "n";

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter debug mode? y/n (A \"n\" answer will launch GUI: ");
        boolean debug = (sc.nextLine().equals("y"));

        // If debug mode is selected
        if (debug) {
            // Loop through menu until user chooses to quit
            while (quit.equals("n")) {
                // Print menu
                System.out.print("\nPlease select a debug option"
                        + "\n1 - Change Resource Totals"
                        + "\n2 - Change Active Player"
                        + "\n3 - Build Infrastructure Items"
                        + "\n4 - Trade"
                        + "\n5 - Development cards"
                        + "\n6 - Roll Dice"
                        + "\n7 - Print Player Information"
                        + "\n8 - Reset All (Reverts to Default Game State)"
                        + "\n9 - Quit\n"
                );
                // Get menu choice
                short menuChoice = (short) Integer.parseInt(sc.nextLine());
                // Boolean flag which will allow returning to main menu
                boolean goBack = false;
                // Switch on menu choice
                switch (menuChoice) {
                    case 1:
                        // While user has not elected to return to main menu
                        while (!goBack) {
                            // Print sub-menu 1 (resource manipulation)
                            System.out.print("\nSelect a resource option: "
                                    + "\n1 - Add Resources"
                                    + "\n2 - Remove Resources"
                                    + "\n3 - Zero All Resources"
                                    + "\n4 - Return to Main Menu\n"
                            );
                            // Get menuChoice
                            menuChoice = (short) Integer.parseInt(sc.nextLine());

                            // Switch on menu choice
                            switch (menuChoice) {
                                case 1:
                                    // Add resources to a specific player
                                    // Select player
                                    System.out.println("Select player to add resources to");
                                    int playerID = Integer.parseInt(sc.nextLine());
                                    // Select resource
                                    System.out.println("Select resource to add");
                                    int resourceToAdd = Integer.parseInt(sc.nextLine());
                                    // Select amount
                                    System.out.println("Select amount to add");
                                    int amountToAdd = Integer.parseInt(sc.nextLine());

                                    // Add resources and print new totals
                                    players[playerID].addResource(resourceToAdd, amountToAdd);
                                    players[playerID].printResources();

                                    break;
                                case 2:
                                    // Deduct resources
                                    // Select player
                                    System.out.println("Select player to deduct resources from");
                                    playerID = Integer.parseInt(sc.nextLine());
                                    // Select resource
                                    System.out.println("Select resource to remove");
                                    int resourceToRemove = Integer.parseInt(sc.nextLine());
                                    // Select amount
                                    System.out.println("Select amount to remove");
                                    int amountToRemove = Integer.parseInt(sc.nextLine());

                                    // Deduct resources and print new totals
                                    players[playerID].deductResource(resourceToRemove, amountToRemove);
                                    players[playerID].printResources();

                                    break;
                                case 3:
                                    // Zero all resources for all players
                                    System.out.println("Zeroing All Resources");
                                    // For each player
                                    for (Player player : players) {
                                        // For each resource
                                        for (int i = 0; i < WOOL; i++) {
                                            // Reset to 0
                                            player.resetResource(i);
                                        }
                                        // Print new total
                                        player.printResources();
                                    }
                                    break;
                                case 4:
                                    // Return to main menu
                                    goBack = true;
                                    break;
                            }
                        }
                        break;
                    case 2:
                        // Select a new active player
                        System.out.println("Which player would you like to set to active?");
                        activePlayerID = Integer.parseInt(sc.nextLine());
                        // Print change to active player
                        System.out.println("Player " + (activePlayerID + 1) + " is now active.");
                        break;
                    case 3:
                        // Sub menu 3 - Building
                        goBack = false;
                        while (!goBack) {
                            System.out.print("\nChoose an Object to Build: "
                                    + "\n1 - Settlement"
                                    + "\n2 - City"
                                    + "\n3 - Road"
                                    + "\n4 - Return to Main Menu"
                            );
                            menuChoice = (short) Integer.parseInt(sc.nextLine());
                            switch (menuChoice) {
                                case 1:
                                    // Build settlement for active player (not startup mode)
                                    Bank.buildSettlement(activePlayerID, false);
                                    // Print current resource totals
                                    players[activePlayerID].printResources();
                                    break;
                                case 2:
                                    // Build city for active player
                                    Bank.buildCity(activePlayerID);
                                    // Print current resource totals
                                    players[activePlayerID].printResources();
                                case 3:
                                    // Build road for active player
                                    Bank.buildRoad(activePlayerID);
                                    // Print new resource totals
                                    players[activePlayerID].printResources();
                                    break;
                                case 4:
                                    // Return to main menu
                                    goBack = true;
                                    break;
                            }
                        }
                        break;
                    case 4:
                        // Trading
                        System.out.print("\nTrade with:"
                                + "\n1 - Player"
                                + "\n2 - Bank"
                                + "\n3 - Return to Main Menu\n"
                        );
                        while (!goBack) {
                            menuChoice = (short) Integer.parseInt(sc.nextLine());
                            switch (menuChoice) {
                                case 1:
                                    // Trade with player
                                    Bank.playerTrade(activePlayerID);
                                    break;
                                case 2:
                                    // Trade with bank
                                    Bank.bankTrade(activePlayerID);
                                    break;
                                case 3:
                                    // Return to main menu
                                    goBack = true;
                                    break;
                            }
                        }
                        break;
                    case 5:
                        // Development cards
                        while (!goBack) {
                            System.out.print("\nChoose Option:"
                                    + "\n1 - Buy Development Card"
                                    + "\n2 - Play Development Card"
                                    + "\n3 - Return to Main Menu\n"
                            );
                            menuChoice = (short) Integer.parseInt(sc.nextLine());
                            switch (menuChoice) {
                                case 1:
                                    // Buy a card
                                    banker.buyDevelopmentCard(activePlayerID);
                                    break;
                                case 2:
                                    // Play the cards you have
                                    boolean haveCard = banker.findDevelopmentCards(activePlayerID);
                                    if (!haveCard) {
                                        System.out.println("Player " + (activePlayerID + 1) + "does not have any development cards.");
                                    }
                                    break;
                                case 3:
                                    // Return to main menu
                                    goBack = true;
                                    break;
                            }
                        }
                        break;
                    case 6:
                        // Roll dice

                        // Roll die 1
                        int die1 = HexTile.getRandInt(1, 6);
                        // Roll die 2
                        int die2 = HexTile.getRandInt(1, 6);
                        // Add dice together
                        int sum = die1 + die2;
                        // Print number rolled
                        System.out.println("You rolled a " + sum);
                        if (sum == 7) {
                            // If a 7 is rolled, the robber steals from everyone
                            robberSteal();
                            boolean moved = false;
                            // And the active player has to move the robber
                            while (!moved) {
                                System.out.println("You Rolled a 7. Choose where to move the robber");
                                int tileChoice = Integer.parseInt(sc.nextLine());
                                moved = moveRobber(tileChoice, activePlayerID);
                            }
                        } else {
                            // If anything other than a seven is, yield resources from all matching tiles
                            for (HexTile tile : tiles) {
                                if (tile.getNumRoll() == sum) {
                                    tile.yieldResources();
                                }
                            }
                        }
                        break;
                    case 7:
                        // Print info
                        System.out.print("Choose information to print: "
                                + "\n1 - Resources"
                                + "\n2 - Settlements"
                                + "\n3 - Roads"
                                + "\n4 - Return to Main Menu"
                        );
                        menuChoice = (short) Integer.parseInt(sc.nextLine());
                        while (!goBack) {
                            switch (menuChoice) {
                                case 1:
                                    // Print all players' resources
                                    for (Player p : players) {
                                        p.printResources();
                                    }
                                    break;
                                case 2:
                                    // For each occupied intersection, print the owning player and the type of settlement
                                    for (Intersection i : intersections) {
                                        if (i.occupied()) {
                                            System.out.println("Player " + (i.getPlayer() + 1) + " has a " + (i.getSettlementType() == 1 ? "Settlement" : "City")
                                                    + " on point " + sqrtToAlpha(i.getLocation().getX()) + i.getLocation().getY()
                                            );
                                        }
                                    }
                                    break;
                                case 3:
                                    for (Boundary b : boundaries) {
                                        // For each occupied road, print the owning player
                                        if (b.occupied()) {
                                            System.out.println("Player " + (b.getPlayer() + 1) + " has a road on boundary"
                                                    + sqrtToAlpha(b.getEndpointA().getLocation().getX()) + b.getEndpointA().getLocation().getY()
                                                    + sqrtToAlpha(b.getEndpointB().getLocation().getX()) + b.getEndpointB().getLocation().getY()
                                            );
                                        }
                                    }
                                    break;
                                case 4:
                                    // Return to main menu
                                    goBack = true;
                                    break;
                            }
                        }
                        break;
                    case 8:
                        // Resets everything (except board set up) to default

                        // Reset all resources for all players to zero
                        for (Player p : players) {
                            for (int i = 0; i > 5; i++) {
                                p.resetResource(i);
                            }
                        }

                        // Clear all settlements
                        for (Intersection i : intersections) {
                            i.setPlayer(-1);
                            i.setSettlementType(0);
                        }

                        // Clear all roads
                        for (Boundary b : boundaries) {
                            b.setPlayer(-1);
                        }
                        System.out.println("Game Status Reset");
                        break;
                    case 9:
                        // Quit menu and move on to GUI
                        quit = "y";
                        break;
                }
            }
        }
    }

    static boolean moveRobber(int tileChoice, int playerID) {

        // Tile object for ease of use
        HexTile tile = tiles[tileChoice];

        // if the tile doesn't already have the robber
        if (tile.hasRobber() == false) {
            // put the robber on it
            tile.setRobber(true);
            // Inform player where robber was moved
            System.out.println("Robber moved to tile " + tileChoice + ".");
            // Boolean flags for which players are on the tile
            boolean player0 = false, player1 = false, player2 = false, player3 = false;
            for (Intersection i : tile.getIntersections()) {

                switch (i.getPlayer()) {
                    case -1:
                        break;
                    case 0:
                        player0 = true;
                        break;
                    case 1:
                        player1 = true;
                        break;
                    case 2:
                        player2 = true;
                        break;
                    case 3:
                        player3 = true;
                        break;
                }
            }

            // If there are no settlements on the tile, inform the player of such
            if (!player0 && !player1 && !player2 && !player3) {
                System.out.println("There are no settlements on this tile.");
                return true;
            }

            // Print which players are present and prompt for choice
            System.out.println("Player(s) " + (player0 ? "0 " : "") + (player1 ? "1 " : "") + (player2 ? "2 " : "") + (player3 ? "3 " : "")
                    + " own(s) settlements on this tile.\nChoose one to steal from: ");

            Scanner sc = new Scanner(System.in);
            // Get choice
            int playerChoice = Integer.parseInt(sc.nextLine());

            // Inform player they cannot steal from themselves
            if (playerChoice == playerID) {
                System.out.println("You cannot steal from yourself.");
            } else {
                // Create player objects for gaining and losing player
                Player playerLosing = players[playerChoice];
                Player playerGaining = players[playerID];

                // Pick a random resource
                int resourceSteal = HexTile.getRandInt(BRICK, WOOL);
                // While the losing player doesn't have that resource, pick a new one
                while (playerLosing.getResourceCount(resourceSteal) <= 0) {
                    resourceSteal = HexTile.getRandInt(BRICK, WOOL);
                }

                // Deduct one of the resource of losing player
                playerLosing.deductResource(resourceSteal, 1);
                // Add one of the resource to gaining player
                playerGaining.addResource(resourceSteal, 1);

                // Print both players' resources
                playerLosing.printResources();
                playerGaining.printResources();
            }

            return true;

        } else {
            // Tell the player the robber is already there and return false
            System.out.println("The robber is already on tile " + tileChoice + ".");
            return false;
        }
    }

    // Converts a single letter string into a corresponding square root value
    // See CatanGameboard.jpeg for further information
    // This allows the player to enter A, B, or C instead of an irrational decimal
    static double alphaToSqrt(String alpha) {
        switch (alpha) {
            case "A":
                return 0;
            case "B":
                return Math.sqrt(3);
            case "C":
                return Math.sqrt(12);
            case "D":
                return Math.sqrt(27);
            case "E":
                return Math.sqrt(48);
            case "F":
                return Math.sqrt(75);
            case "G":
                return Math.sqrt(108);
            case "H":
                return Math.sqrt(147);
            case "I":
                return Math.sqrt(192);
            case "J":
                return Math.sqrt(243);
            case "K":
                return Math.sqrt(300);
            default:
                return -1;
        }
    }

    // Same as above, but in reverse
    // Allows printing to console of A,B,C, etc
    // Instead of irrational square root of arbitrary length
    static char sqrtToAlpha(double sqrt) {
        int sqrtInt = (int) sqrt;
        switch (sqrtInt) {
            case 0:
                return 'A';
            case 1:
                return 'B';
            case 3:
                return 'C';
            case 5:
                return 'D';
            case 6:
                return 'E';
            case 8:
                return 'F';
            case 10:
                return 'G';
            case 12:
                return 'H';
            case 13:
                return 'I';
            case 15:
                return 'J';
            case 17:
                return 'K';
            default:
                return 'Z';
        }
    }

    static void writeToDB(HexTile[] tiles, Boundary[] boundaries, Intersection[] intersections) throws SQLException, ClassNotFoundException {
        // TODO: Write method to save to database
        // Will be called automatically upon the end of each turn once GUI is instated
        // This will enable autosaving and not require the user to save the game manually
    }

    static void robberSteal() {
        // When you roll a 7 the robber steals from any player with over 7 total resources
        Scanner sc = new Scanner(System.in);
        // For each player, check their total
        for (Player player : players) {
            // If it's over 7
            if (player.resourceTotal > 7) {
                // They must lose half of them (integer division rounds down by default
                int resourcesToLose = player.resourceTotal / 2;
                // As long as you still have to lose resources
                while (resourcesToLose > 0) {
                    System.out.println("Player " + (player.getPlayerID() + 1) + " Choose a resource to surrender:");
                    // Choose type of resource to surrender one of
                    int resourceSurrendered = Integer.parseInt(sc.nextLine());

                    if (player.resourceMaterials[resourceSurrendered] > 0) {
                        // Surrender one of those and decremement resources left to lose
                        player.deductResource(resourceSurrendered, 1);
                        resourcesToLose--;
                    }
                }
            }
        }
    }

    static int buildGameboard() {
        System.out.println("Setting up game board");

        //~~~~~~~~~~~~~ Creating Intersections~~~~~~~~~~~~~~~~\\
        // These have to be hardcoded so their variable names \\
        // can be used in various places throughout the       \\
        // program - this will make keeping track of their    \\
        // location infiniteley easier - and, they will never,\\
        // ever change for any reason                         \\
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\\
        //Error intersection will be used as an error value in methods that return Intersections
        errorIntersection = new Intersection(new Coordinate(-1, -1));

        Intersection A7 = new Intersection(new Coordinate(A, 7));
        Intersection A9 = new Intersection(new Coordinate(A, 9));
        Intersection B4 = new Intersection(new Coordinate(B, 4));
        Intersection B6 = new Intersection(new Coordinate(B, 6));
        Intersection B10 = new Intersection(new Coordinate(B, 10));
        Intersection B12 = new Intersection(new Coordinate(B, 12));
        Intersection C1 = new Intersection(new Coordinate(C, 1));
        Intersection C3 = new Intersection(new Coordinate(C, 3));
        Intersection C7 = new Intersection(new Coordinate(C, 7));
        Intersection C9 = new Intersection(new Coordinate(C, 9));
        Intersection C13 = new Intersection(new Coordinate(C, 13));
        Intersection C15 = new Intersection(new Coordinate(C, 15));
        Intersection D0 = new Intersection(new Coordinate(D, 0));
        Intersection D4 = new Intersection(new Coordinate(D, 4));
        Intersection D6 = new Intersection(new Coordinate(D, 6));
        Intersection D10 = new Intersection(new Coordinate(D, 10));
        Intersection D12 = new Intersection(new Coordinate(D, 12));
        Intersection D16 = new Intersection(new Coordinate(D, 16));
        Intersection E1 = new Intersection(new Coordinate(E, 1));
        Intersection E3 = new Intersection(new Coordinate(E, 3));
        Intersection E7 = new Intersection(new Coordinate(E, 7));
        Intersection E9 = new Intersection(new Coordinate(E, 9));
        Intersection E13 = new Intersection(new Coordinate(E, 13));
        Intersection E15 = new Intersection(new Coordinate(E, 15));
        Intersection F0 = new Intersection(new Coordinate(F, 0));
        Intersection F4 = new Intersection(new Coordinate(F, 4));
        Intersection F6 = new Intersection(new Coordinate(F, 6));
        Intersection F10 = new Intersection(new Coordinate(F, 10));
        Intersection F12 = new Intersection(new Coordinate(F, 12));
        Intersection F16 = new Intersection(new Coordinate(F, 16));
        Intersection G1 = new Intersection(new Coordinate(G, 1));
        Intersection G3 = new Intersection(new Coordinate(G, 3));
        Intersection G7 = new Intersection(new Coordinate(G, 7));
        Intersection G9 = new Intersection(new Coordinate(G, 9));
        Intersection G13 = new Intersection(new Coordinate(G, 13));
        Intersection G15 = new Intersection(new Coordinate(G, 15));
        Intersection H0 = new Intersection(new Coordinate(H, 0));
        Intersection H4 = new Intersection(new Coordinate(H, 4));
        Intersection H6 = new Intersection(new Coordinate(H, 6));
        Intersection H10 = new Intersection(new Coordinate(H, 10));
        Intersection H12 = new Intersection(new Coordinate(H, 12));
        Intersection H16 = new Intersection(new Coordinate(H, 16));
        Intersection I1 = new Intersection(new Coordinate(I, 1));
        Intersection I3 = new Intersection(new Coordinate(I, 3));
        Intersection I7 = new Intersection(new Coordinate(I, 7));
        Intersection I9 = new Intersection(new Coordinate(I, 9));
        Intersection I13 = new Intersection(new Coordinate(I, 13));
        Intersection I15 = new Intersection(new Coordinate(I, 15));
        Intersection J4 = new Intersection(new Coordinate(J, 4));
        Intersection J6 = new Intersection(new Coordinate(J, 6));
        Intersection J10 = new Intersection(new Coordinate(J, 10));
        Intersection J12 = new Intersection(new Coordinate(J, 12));
        Intersection K7 = new Intersection(new Coordinate(K, 7));
        Intersection K9 = new Intersection(new Coordinate(K, 9));

        // For the same reasons mentioned above, adjacent intersections must
        // be set manually. This is far simpler than devising some crazy
        // way to have the intersections determine their own neighbors
        A7.setAdjacentIntersections(A9, B6);
        A9.setAdjacentIntersections(A7, B10);
        B4.setAdjacentIntersections(B6, C3);
        B6.setAdjacentIntersections(A7, B4, C7);
        B10.setAdjacentIntersections(A9, B12, C9);
        B12.setAdjacentIntersections(B10, C13);
        C1.setAdjacentIntersections(C3, D0);
        C3.setAdjacentIntersections(B4, C1, D4);
        C7.setAdjacentIntersections(B6, C9, D6);
        C9.setAdjacentIntersections(B10, C7, D10);
        C13.setAdjacentIntersections(B12, C15, D12);
        C15.setAdjacentIntersections(C13, D16);
        D0.setAdjacentIntersections(C1, E1);
        D4.setAdjacentIntersections(C3, D6, E3);
        D6.setAdjacentIntersections(C7, D4, E7);
        D10.setAdjacentIntersections(C9, D12, E9);
        D12.setAdjacentIntersections(C13, D10, E13);
        D16.setAdjacentIntersections(C15, E15);
        E1.setAdjacentIntersections(D0, F0);
        E3.setAdjacentIntersections(D4, E1, F4);
        E7.setAdjacentIntersections(D6, E9, F6);
        E9.setAdjacentIntersections(D10, E7, F10);
        E13.setAdjacentIntersections(D12, E15, F12);
        E15.setAdjacentIntersections(D16, E13, F16);
        F0.setAdjacentIntersections(E1, G1);
        F4.setAdjacentIntersections(E3, F6, G3);
        F6.setAdjacentIntersections(E7, F4, G7);
        F10.setAdjacentIntersections(E9, F12, G9);
        F12.setAdjacentIntersections(E13, F10, G13);
        F16.setAdjacentIntersections(E15, G15);
        G1.setAdjacentIntersections(F0, H0);
        G3.setAdjacentIntersections(F4, G1, H4);
        G7.setAdjacentIntersections(F6, G9, H6);
        G9.setAdjacentIntersections(F10, G7, H10);
        G13.setAdjacentIntersections(F12, G15, H12);
        G15.setAdjacentIntersections(F16, G13, H16);
        H0.setAdjacentIntersections(G1, I1);
        H4.setAdjacentIntersections(G3, H6, I3);
        H6.setAdjacentIntersections(G7, H4, I7);
        H10.setAdjacentIntersections(G9, H12, I9);
        H12.setAdjacentIntersections(G13, H10, I13);
        H16.setAdjacentIntersections(G15, I15);
        I1.setAdjacentIntersections(H0, I3);
        I3.setAdjacentIntersections(H4, I1, J4);
        I7.setAdjacentIntersections(H6, I9, J6);
        I9.setAdjacentIntersections(H10, I7, J10);
        I13.setAdjacentIntersections(H12, I15, J12);
        I15.setAdjacentIntersections(H16, I13);
        J4.setAdjacentIntersections(I3, J6);
        J6.setAdjacentIntersections(I7, J4, K7);
        J10.setAdjacentIntersections(I9, J12, K9);
        J12.setAdjacentIntersections(I13, J10);
        K7.setAdjacentIntersections(J6, K9);
        K9.setAdjacentIntersections(J10, K7);

        // Same for boundaries
        //Error boundary will be used as an error value in methods that return boundaries
        errorBoundary = new Boundary(errorIntersection, errorIntersection);

        Boundary A7A9 = new Boundary(A7, A9);
        Boundary A7B6 = new Boundary(A7, B6);
        Boundary A9B10 = new Boundary(A9, B10);
        Boundary B4B6 = new Boundary(B4, B6);
        Boundary B4C3 = new Boundary(B4, C3);
        Boundary B6C7 = new Boundary(B6, C7);
        Boundary B10B12 = new Boundary(B10, B12);
        Boundary B10C9 = new Boundary(B10, C9);
        Boundary B12C13 = new Boundary(B12, C13);
        Boundary C1C3 = new Boundary(C1, C3);
        Boundary C1D0 = new Boundary(C1, D0);
        Boundary C3D4 = new Boundary(C3, D4);
        Boundary C7C9 = new Boundary(C7, C9);
        Boundary C7D6 = new Boundary(C7, D6);
        Boundary C9D10 = new Boundary(C9, D10);
        Boundary C13C15 = new Boundary(C13, C15);
        Boundary C13D12 = new Boundary(C13, D12);
        Boundary C15D16 = new Boundary(C15, D16);
        Boundary D0E1 = new Boundary(D0, E1);
        Boundary D4D6 = new Boundary(D4, D6);
        Boundary D4E3 = new Boundary(D4, E3);
        Boundary D6E7 = new Boundary(D6, E7);
        Boundary D10D12 = new Boundary(D10, D12);
        Boundary D10E9 = new Boundary(D10, E9);
        Boundary D12E13 = new Boundary(D12, E13);
        Boundary D16E15 = new Boundary(D16, E15);
        Boundary E1E3 = new Boundary(E1, E3);
        Boundary E1F0 = new Boundary(E1, F0);
        Boundary E3F4 = new Boundary(E3, F4);
        Boundary E7E9 = new Boundary(E7, E9);
        Boundary E7F6 = new Boundary(E7, F6);
        Boundary E9F10 = new Boundary(E9, F10);
        Boundary E13E15 = new Boundary(E13, E15);
        Boundary E13F12 = new Boundary(E13, F12);
        Boundary E15F16 = new Boundary(E15, F16);
        Boundary F0G1 = new Boundary(F0, G1);
        Boundary F4F6 = new Boundary(F4, F6);
        Boundary F4G3 = new Boundary(F4, G3);
        Boundary F6G7 = new Boundary(F6, G7);
        Boundary F10F12 = new Boundary(F10, F12);
        Boundary F10G9 = new Boundary(F10, G9);
        Boundary F12G13 = new Boundary(F12, G13);
        Boundary F16G15 = new Boundary(F16, G15);
        Boundary G1G3 = new Boundary(G1, G3);
        Boundary G1H0 = new Boundary(G1, H0);
        Boundary G3H4 = new Boundary(G3, H4);
        Boundary G7G9 = new Boundary(G7, G9);
        Boundary G7H6 = new Boundary(G7, H6);
        Boundary G9H10 = new Boundary(G9, H10);
        Boundary G13G15 = new Boundary(G13, G15);
        Boundary G13H12 = new Boundary(G13, H12);
        Boundary G15H16 = new Boundary(G15, H16);
        Boundary H0I1 = new Boundary(H0, I1);
        Boundary H4H6 = new Boundary(H4, H6);
        Boundary H4I3 = new Boundary(H4, I3);
        Boundary H6I7 = new Boundary(H6, I7);
        Boundary H10H12 = new Boundary(H10, H12);
        Boundary H10I9 = new Boundary(H10, I9);
        Boundary H12I13 = new Boundary(H12, I13);
        Boundary H16I15 = new Boundary(H16, I15);
        Boundary I1I3 = new Boundary(I1, I3);
        Boundary I3J4 = new Boundary(I3, J4);
        Boundary I7I9 = new Boundary(I7, I9);
        Boundary I7J6 = new Boundary(I7, J6);
        Boundary I9J10 = new Boundary(I9, J10);
        Boundary I13I15 = new Boundary(I13, I15);
        Boundary I13J12 = new Boundary(I13, J12);
        Boundary J4J6 = new Boundary(J4, J6);
        Boundary J6K7 = new Boundary(J6, K7);
        Boundary J10J12 = new Boundary(J10, J12);
        Boundary J10K9 = new Boundary(J10, K9);
        Boundary K7K9 = new Boundary(K7, K9);

        // Hard-coded tiles for similar reasons to boundaries and intersections
        //Error tile will be used as an error value in methods that return tiles
        errorTile = new HexTile(new Intersection[]{errorIntersection, errorIntersection, errorIntersection, errorIntersection, errorIntersection, errorIntersection});

        HexTile T1 = new HexTile(new Intersection[]{C1, D0, E1, E3, D4, C3});
        HexTile T2 = new HexTile(new Intersection[]{E1, F0, G1, G3, F4, E3});
        HexTile T3 = new HexTile(new Intersection[]{G1, H0, I1, I3, H4, G3});
        HexTile T4 = new HexTile(new Intersection[]{B4, C3, D4, D6, C7, B6});
        HexTile T5 = new HexTile(new Intersection[]{D4, E3, F4, F6, E7, D6});
        HexTile T6 = new HexTile(new Intersection[]{F4, G3, H4, H6, G7, F6});
        HexTile T7 = new HexTile(new Intersection[]{H4, I3, J4, J6, I7, H6});
        HexTile T8 = new HexTile(new Intersection[]{A7, B6, C7, C9, B10, A9});
        HexTile T9 = new HexTile(new Intersection[]{C7, D6, E7, E9, D10, C9});
        HexTile T10 = new HexTile(new Intersection[]{E7, F6, G7, G9, F10, E9});
        HexTile T11 = new HexTile(new Intersection[]{G7, H6, I7, I9, H10, G9});
        HexTile T12 = new HexTile(new Intersection[]{I7, J6, K7, K9, J10, I9});
        HexTile T13 = new HexTile(new Intersection[]{B10, C9, D10, D12, C13, B12});
        HexTile T14 = new HexTile(new Intersection[]{D10, E9, F10, F12, E13, D12});
        HexTile T15 = new HexTile(new Intersection[]{F10, G9, H10, H12, G13, F12});
        HexTile T16 = new HexTile(new Intersection[]{H10, I9, J10, J12, I13, H12});
        HexTile T17 = new HexTile(new Intersection[]{C13, D12, E13, E15, D16, C15});
        HexTile T18 = new HexTile(new Intersection[]{E13, F12, G13, G15, F16, E15});
        HexTile T19 = new HexTile(new Intersection[]{G13, H12, I13, I15, H16, G15});

        // Array of intersections to allow simple iteration
        Intersection[] intersectionsTemp = {
            A7, A9, B4, B6, B10, B12, C1, C3, C7, C9, C13, C15, D0, D4, D6, D10, D12, D16, E1, E3, E7, E9,
            E13, E15, F0, F4, F6, F10, F12, F16, G1, G3, G7, G9, G13, G15, H0, H4, H6, H10, H12, H16, I1,
            I3, I7, I9, I13, I15, J4, J6, J10, J12, K7, K9};

        //set intersections equal to intersectionsTemp to easily populate
        intersections = intersectionsTemp.clone();

        // Array of boundaries will be used to iterate through boundaries
        Boundary[] boundariesTemp = {A7A9, A7B6, A9B10, B4B6, B4C3, B6C7, B10B12, B10C9, B12C13, C1C3, C1D0, C3D4,
            C7C9, C7D6, C9D10, C13C15, C13D12, C15D16, D0E1, D4D6, D4E3, D6E7, D10D12, D10E9, D12E13, D16E15, E1E3,
            E1F0, E3F4, E7E9, E7F6, E9F10, E13E15, E13F12, E15F16, F0G1, F4F6, F4G3, F6G7, F10F12, F10G9,
            F12G13, F16G15, G1G3, G1H0, G3H4, G7G9, G7H6, G9H10, G13G15, G13H12, G15H16, H0I1, H4H6,
            H4I3, H6I7, H10H12, H10I9, H12I13, H16I15, I1I3, I3J4, I7I9, I7J6, I9J10, I13I15, I13J12,
            J4J6, J6K7, J10J12, J10K9, K7K9};

        //set boundaries equal to boundariesTemp to easily populate
        boundaries = boundariesTemp.clone();

        //array of all external boundaries
        Boundary[] externalBoundariesTemp = {A7A9, A7B6, A9B10, B4B6, B4C3, B10B12, B12C13, C1C3, C1D0, C13C15, C15D16, D0E1, D16E15, E1F0, E15F16,
            F0G1, F16G15, G1H0, G15H16, H0I1, H16I15, I1I3, I3J4, I13I15, I13J12, J4J6, J6K7, J10K9, J10J12, K7K9};

        //set externalBoundaries equal to externalBoundariesTemp to easily populate
        externalBoundaries = externalBoundariesTemp.clone();

        // Array of hex tiles to easily loop through
        HexTile[] tilesTemp = {T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13,
            T14, T15, T16, T17, T18, T19};

        //set tiles equal to tilesTemp to easily populate
        tiles = tilesTemp.clone();

        //For-loop finds the adjacent boundaries of every intersection
        for (Intersection intersection : intersections) {

            for (Boundary boundary : boundaries) {

                //if either if the current boundary's endpoints are the same as the current point,
                //and if it has not already been included as an adjacent boundary
                //then the current boundary is added to the ArrayList adjacent boundary
                if (boundary.getEndpointA() == intersection || boundary.getEndpointB() == intersection) {
                    if (!intersection.getAdjacentBoundaries().contains(boundary)) {
                        intersection.addAdjacentBoundary(boundary);
                    }
                }
            }
        }

        //For-loop assigned 9 ports (4 general and 5 specialty) to random external boundaries
        for (int i = 0; i < 9; i++) {

            //generates a random int between 0 and the length of the externalBoundaraies array
            int randPosition = (int) (Math.random() * (externalBoundaries.length));

            //uses random int as an index in the externalBoundaries array to get a random boundary
            Boundary chosenBoundary = externalBoundaries[randPosition];

            //if condition ensures that the chosen boundary has not already been assigned a harbor
            if (chosenBoundary.getHarbor() == -1) {

                //switch statement assigns the first 4 boundaries a general harbor, and one specialty harbor to the remainder
                switch (i) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                        chosenBoundary.setHarbor(GENERAL_HARBOR);
                        System.out.println(i + ": Exterior Border " + randPosition + " has a general harbor.");
                        break;
                    case 4:
                        chosenBoundary.setHarbor(BRICK);
                        System.out.println(i + ": Exterior Border " + randPosition + " has a brick harbor.");
                        break;
                    case 5:
                        chosenBoundary.setHarbor(LUMBER);
                        System.out.println(i + ": Exterior Border " + randPosition + " has a lumber harbor.");
                        break;
                    case 6:
                        chosenBoundary.setHarbor(ORE);
                        System.out.println(i + ": Exterior Border " + randPosition + " has an ore harbor.");
                        break;
                    case 7:
                        chosenBoundary.setHarbor(WHEAT);
                        System.out.println(i + ": Exterior Border " + randPosition + " has a wheat harbor.");
                        break;
                    case 8:
                        chosenBoundary.setHarbor(WOOL);
                        System.out.println(i + ": Exterior Border " + randPosition + " has a wool harbor.");
                        break;
                }
            } else {
                //if a border is chosen twice, i is decremented so that the full 9 harbors are made
                i--;
            }

        }
        // T10 will always be the center (desert) tile
        T10.setCenter(true);
        // The robber always starts in the center
        T10.setRobber(true);

        // Establish counters and max values for each type of tile
        // Board game version of Catan only allows a certain number
        // of wool, lumber, etc. yielding tiles for game balance purposes
        int brickTiles = 0, lumberTiles = 0, oreTiles = 0, wheatTiles = 0, woolTiles = 0;
        int maxBrick = 3, maxLumber = 4, maxOre = 3, maxWheat = 4, maxWool = 4;

        // Loop through every tile
        for (HexTile tile : tiles) {

            // T10, the center tile, does not need anything further done
            if (tile != T10) {

                // Two boolean flags for determining if a valid value for
                // each randomly generated value has been found
                boolean yieldIsValid = false;
                boolean rollIsValid = false;

                // As long as a valid value has not been set,
                while (!yieldIsValid) {
                    // set a new resource to yield
                    tile.setResourceYield();
                    // increment the resource that is yielded and set flag to true
                    // assuming the maximum is not yet exceeded
                    switch (tile.getResourceYield()) {
                        case BRICK:
                            if (brickTiles < maxBrick) {
                                brickTiles++;
                                yieldIsValid = true;
                            }
                            break;
                        case LUMBER:
                            if (lumberTiles < maxLumber) {
                                lumberTiles++;
                                yieldIsValid = true;
                            }
                            break;
                        case ORE:
                            if (oreTiles < maxOre) {
                                oreTiles++;
                                yieldIsValid = true;
                            }
                            break;
                        case WHEAT:
                            if (wheatTiles < maxWheat) {
                                wheatTiles++;
                                yieldIsValid = true;
                            }
                            break;
                        case WOOL:
                            if (woolTiles < maxWool) {
                                woolTiles++;
                                yieldIsValid = true;
                            }
                            break;
                        default:
                            // Error message in case invalid number found somehow
                            System.out.println("ERROR: Invalid Resource (" + tile.getResourceYield() + ")");
                            break;
                    }
                }
                // This while loop and switch function identically to the ones above
                // for similar reasons
                while (!rollIsValid) {
                    int num2 = 0, num3 = 0, num4 = 0, num5 = 0, num6 = 0, num8 = 0, num9 = 0, num10 = 0, num11 = 0, num12 = 0;
                    // Max Values 2 = 1, 3 = 2, 4 = 3, 5 = 4, 6 = 5, 7, 8 = 5, 9 = 4, 10 = 3, 11 = 2, 12 = 1
                    tile.setNumRoll();
                    switch (tile.getNumRoll()) {
                        case 2:
                            if (num2 < 1) {
                                num2++;
                                rollIsValid = true;
                            }
                            break;
                        case 3:
                            if (num3 < 2) {
                                num3++;
                                rollIsValid = true;
                            }
                            break;
                        case 4:
                            if (num4 < 3) {
                                num4++;
                                rollIsValid = true;
                            }
                            break;
                        case 5:
                            if (num5 < 4) {
                                num5++;
                                rollIsValid = true;
                            }
                            break;
                        case 6:
                            if (num6 < 5) {
                                num3++;
                                rollIsValid = true;
                            }
                            break;
                        case 8:
                            if (num8 < 5) {
                                num8++;
                                rollIsValid = true;
                            }
                            break;
                        case 9:
                            if (num9 < 4) {
                                num9++;
                                rollIsValid = true;
                            }
                            break;
                        case 10:
                            if (num10 < 3) {
                                num10++;
                                rollIsValid = true;
                            }
                            break;
                        case 11:
                            if (num11 < 2) {
                                num11++;
                                rollIsValid = true;
                            }
                            break;
                        case 12:
                            if (num12 < 1) {
                                num12++;
                                rollIsValid = true;
                            }
                            break;
                    }
                }
            }
        }

        // Print message indicating board is set up and the game can begin
        System.out.println("Board Set Up. Ready to Begin Game.");

        return 0;
    }
}
