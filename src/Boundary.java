
import javafx.scene.shape.Line;

/*  
                        Boundary - Settlers of Catan

Class:      Adjvanced Java - CIT-285-01
            Professor Miller, Fall 2016

Group:      RARA - Settlers of Catan
            Ruchelly Almeida
            Alessandra Shipman     
            Oleksii Butakov
            Andrew Thomas

Files:      Bank.java
            Boundary.java (Current File)
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


Classes:    Boundary

                                    Summary:
 Each instance of this class will represent a unique edge (boundary) of a hexagonal 
 tile on the gameboard. In this code, a boundary is defined by its two endpoints,
 both of which are Intersection objects. A player has the option of building a 
 road on each boundary provided that no one else has already built a road there
 and the new road connects to either a road or settlement built by this player.
 Some external boundaries have harbors wich give players advantags when trading. 
 Harbors are randomly assigned to boarders at the start of each game. The code 
 for assigning these harbors to a boundary is contained within the GameManager
 class.

 
Activity:	  -Date-             -Person-               -Updates-
            October 20, 2016		AS          *Created Boundary class 
                                                     within the Intersection.java 
                                                     file
				
            November 7, 2016		AS          *Standardized Documentation 
                                                     and Formatting 

            November 14, 2016		AS          *added searchBoundary method
                                       
            November 18, 2016           AS          *Moved Boundary class to this
                                                     file
                                                    *Added Javadoc documentation
            
            November 19, 2016           AT          * Added line property for UI
                                                      including constructor changes
                                                      and getter

            November 23, 2016           AT          * Added comments regarding Line object
                                                    * Made Line object final
						    
  	    November 24, 2016		RA	    * Added validation statement from line 
	    					      186 to 193 to make sure player does
						      not build more roads than the maximum
						      amount allowed
 */
/**
 * The <code> Boundary </code> class represents the edges of the hexagonal game
 * board tiles, defined by two <code> Intersection </code> endpoints, on which
 * players can build roads.
 *
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

    private final Line line;

//                               Constructors
// _____________________________________________________________________________
    /**
     * <code> Boundary </code> Constructor
     *
     * @param e1 First endpoint
     * @param e2 Second endpoint
     */
    Boundary(Intersection e1, Intersection e2) {

        endpointA = e1;
        endpointB = e2;
        
        // Creates a Line object at the location of the road for GUI
        double lineStartX = endpointA.getLocation().getUIX();
        double lineStartY = endpointA.getLocation().getUIY();
        double lineEndX = endpointB.getLocation().getUIX();
        double lineEndY = endpointB.getLocation().getUIY();

        line = new Line(lineStartX, lineStartY, lineEndX, lineEndY);

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
    
    // returns GUI Line object
    Line getLine(){
        return line;
    }

//                                 Methods
// _____________________________________________________________________________
    /**
     * The <code> occupied </code> method returns a boolean indicating if the
     * <code> Boundary </code> belongs to a player.
     *
     * @return boolean
     */
    boolean occupied() {
        return player >= 0;
    }

    /**
     * The <code> isOccupiable </code> method determines whether a specific
     * player may occupy (build a road on) a <code> Boundary </code>.
     *
     * @param playerID unique <code> int </code> associated with player
     * @return boolean
     */
    boolean isOccupiable(int playerID) {

        Player playerInfo = GameManager.players[playerID];
        
        //If road already exists, returns false
        if (occupied()) {

            //Message for Testing
            System.out.println("Player " + (playerID + 1)
                    + " is unable to build a road here because "
                    + (player + 1) + " already has.");
            return false;

        } 
	   // If player already has the maximum amount of roads, returns false
	   else if (playerInfo.getRoadCount() >= 15) {
		System.out.println("Player " + (playerID + 1)
                    + " is unable to build a road here because"
		    + " it has exceeded the amount of roads available.");
		return false;
		
	} 
	    else {

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
                + " she has no adjacent roads or settlements.");
        return false;

    }

    /**
     * Searches for a <code> Boundary </code> based on given endpoints If found,
     * that <code> Boundary </code> is returned; otherwise, 
     * <code> errorBoundary </code> instance is returned.
     *
     * @param e1 First endpoint
     * @param e2 Second endpoint
     * @return Boundary
     */
    static Boundary searchBoundary(Intersection e1, Intersection e2) {

        //for loop iterates through each boundary and compares its endpoints to the given intersections
        //if the endpoints and intersections match, that boundary is returned
        for (Boundary b : GameManager.boundaries) {

            if ((b.endpointA == e1 && b.endpointB == e2) || (b.endpointA == e2 && b.endpointB == e1)) {
                System.out.println("Boundary found.");
                return b;
            }

        }

        System.out.println("Error, no boundary found with that cooridante position.");
        return GameManager.errorBoundary;
    }
}
