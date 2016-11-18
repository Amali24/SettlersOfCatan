/*  
                Location Resource Classes - Settlers of Catan

Class:      Adjvanced Java - CIT-285-01
            Professor Miller, Fall 2016

Group:      RARA - Settlers of Catan
            Ruchelly Almeida
            Alessandra Shipman     
            Oleksii Butakov
            Andrew Thomas

Files:      Bank.java
            DevelopmentCard.java 
            GameManager.java
            HexTile.java
            Intersection.java (Current File)
            Player.java


Classes:    Intersection
            Boundary
            Coordinate

                                    Summary:
 The following code includes three classes to be used in the computerized game 
 Settlers of Catan. The first two classes, Intersection and Boundary, are 
 resources for the game. The Intersection class represents the corners of each 
 hexagonal tile on the gameboard. The Boundary class represents the edges of 
 these hexagons. The last class, Coordinate, sets up an x-y ordered pair to be 
 used as the physical location for an intersection. Inside of the zip file 
 submission for this project, I've included a diagram of the gameboard and an 
 MS Word document containing the UML diagrams for these classes. There are print
 statements throughout this code meant to display progress statements to the 
 console for testing purposes. They are clearly marked with the words
 "Message for Testing" and are meant to be deleted before this code is 
 integrated into a larger project. 
 
 
Activity:	  -Date-             -Person-               -Updates-
            October 20, 2016		AS          *Created Intersection Class
                                                    *Created Boundary Class
                                                    *Created Coordinate Class
                                                    *Submitted as AS's project 1
				
            November 7, 2016		AS          *Standardized Documentation 
                                                     and Formatting 

            November 14, 2016		AS          *added searchIntersections and
                                                     searchBoundary methods

 */

import java.util.ArrayList;

/*______________________________________________________________________________

                              INTERSECTION CLASS                             
 Each instance of this class will represent a unique corner of a hexagonal tile 
 on the gameboard. The exact location of an intersection is set by passing a 
 coordinate position to its constructor. Players have the option of settling an
 intersection provided that no one else has settled it, no one has settled
 any adjacent intersections, and they have a road that connects to its position 
 (this road requirement is dropped durring the first 2 round of the game).
 _______________________________________________________________________________
 */
public class Intersection {

//                              Class Properties
// _____________________________________________________________________________
    private Coordinate location;        //Holds coordinate position of object
    //Will be used when constructing GUI

    private int player = -1;            //Player who has settled intersection
    //-1 value represents no player

    private int settlementType = 0;     //0 value = no settlement
    //1 value = base settlement
    //2 value = city

    private Intersection[] adjacentIntersections = new Intersection[3];
    private int adjacentIntersectionCount;
    private ArrayList<Boundary> adjacentBoundaries = new ArrayList<>();

//                               Constructors
// _____________________________________________________________________________
    Intersection(Coordinate a) {
        location = a;
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

    //Overloaded method for setting an intersections ajacent intersection
    //An intersection can have either 2 or 3 adjacent intersections
    //The integer adjacentIntersectionCount is set accordingly
    void setAdjacentIntersections(Intersection a, Intersection b) {

        adjacentIntersections[0] = a;
        adjacentIntersections[1] = b;

        adjacentIntersectionCount = 2;
    }

    void setAdjacentIntersections(Intersection a, Intersection b, Intersection c) {

        adjacentIntersections[0] = a;
        adjacentIntersections[1] = b;
        adjacentIntersections[2] = c;

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

//                                 Methods
// _____________________________________________________________________________
    //Returns a boolean indicating if the intersection has already been settled
    //If the player number is => 0 (i.e. its not -1), returns true
    boolean occupied() {
        return player >= 0;
    }

    //Returns a boolean indicating if a given player may settle this intersection
    //To settle an intersetion, the intersection must:
    //1. Be unoccupied
    //2. Not be adjacent to a settlement
    //3. Be on a road built by the player (Waived durring first 2 rounds, the "setupPhase")
    boolean isOccupiable(int playerID, boolean setupPhase) {

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

