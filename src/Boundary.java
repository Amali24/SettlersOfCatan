/*_____________________________________________________________________________

                                 BOUNDARY CLASS
 Each instance of this class will represent a unique edge of a hexagonal tile 
 on the gameboard. In this code, a boundary is defined by its two endpoints,
 both of which are Intersection objects. A player has the option of building a 
 road on each boundary provided that no one else has already built a road there
 and the new road connects to either a road or settlement built by this player.
 Some external boundaries have harbors wich give players advantags when trading. 
 Harbors are randomly assigned to boarders at the start of each game (The code 
 for this is not contained in this class, but the harbor information is).
 _______________________________________________________________________________
 */
class Boundary {

//                              Class Properties
// _____________________________________________________________________________
    private final Intersection endpointA;
    private final Intersection endpointB;
    private int player = -1;    //Player who has settled intersection
    //-1 value represents no player

    private int harbor = -1;    //-1 value = no harbor
    //0 value = brick harbor
    //1 value = lumber harbor
    //2 value = ore harbor
    //3 value = wheat harbor
    //4 value = wool harbor
    //5 value = general harbor

//                               Constructors
// _____________________________________________________________________________
    Boundary(Intersection a, Intersection b) {

        endpointA = a;
        endpointB = b;

    }

//                          Accessors and Mutators
// _____________________________________________________________________________
    //Returns first endpoint (they are stored in alphabetical order)
    Intersection getEndpointA() {
        return endpointA;
    }

    //Returns second endpoint (they are stored in alphabetical order)
    Intersection getEndpointB() {
        return endpointB;
    }

    //Set ID of player who owns road
    void setPlayer(int p) {
        player = p;
        System.out.println("Road built successfully!");

    }

    //returns ID of player who owns road
    int getPlayer() {
        return player;
    }

    //Sets type of harbor boundary contains, if any 
    void setHarbor(int h) {
        harbor = h;
    }

    //returns type of harbor
    int getHarbor() {
        return harbor;
    }

//                                 Methods
// _____________________________________________________________________________
    //Returns a boolean indicating if the intersection has already been settled
    boolean occupied() {
        return player >= 0;
    }

    //Method returns boolean value indicating if a a player may build a road
    //To build a road, the boundary must:
    //1. Be unoccupied
    //2. Be adjacent to a settlement or road owned by the player
    boolean isOccupiable(int playerID) {

        //If road already exists, returns false
        if (occupied()) {

            //Message for Testing
            System.out.println("Player " + (playerID + 1)
                    + " is unable to build a road here because "
                    + (player + 1) + " already has.");
            return false;

        } else {

            //if any adjacent adjacent boundary or intersection has a road or 
            //settlement belonging to the current player, returns true
            if (endpointA.getAdjacentBoundaries().stream().anyMatch((borderA)
                    -> (borderA.getPlayer() == playerID))) {
                return true;
            }
            if (endpointB.getAdjacentBoundaries().stream().anyMatch((borderB)
                    -> (borderB.getPlayer() == playerID))) {
                return true;
            }
            if (endpointA.getPlayer() == playerID
                    || endpointB.getPlayer() == playerID) {
                return true;
            }

        }

        //If no true condition is met, false is returned 
        //Message for Testing
        System.out.println("Player " + (playerID + 1)
                + " is unable to build a road here because"
                + " she has no adjactent roads or settlements.");
        return false;

    }

    //Searches for a boundary based on its endpoints
    //Returns that boundary if successful, otherwise returns error
    static Boundary searchBoundary(Intersection i1, Intersection i2) {

        //for loop iterates through each boundary and compares its endpoints to the given intersections
        //if the endpoints and intersections match, that boundary is returned
        for (Boundary b : GameManager.boundaries) {

            if ((b.endpointA == i1 && b.endpointB == i2) || (b.endpointA == i2 && b.endpointB == i1)) {
                System.out.println("Boundary found.");
                return b;
            }

        }

        System.out.println("Error, no boundary found with that cooridante position.");
        return GameManager.errorBoundary;
    }
}

