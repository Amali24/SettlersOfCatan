
/*  
                    	Gameboard Tiles  - Settlers of Catan

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
            HexTile.java (Current File)
            Intersection.java
            Player.java
            Trade.java
            READ_THIS_FIRST.txt
            CatanGameboard.jpeg


Classes:    HexTile


                                    Summary:
The following code contains the HexTile Class for the computerized game, 
Settlers of Catan. This class facilitates most of the game play by setting up
the gameboard, handling player turns and dice rice rolls, and allocating resource
materials. Ultimately this class connects the player experience with the logic
of the game.


Activity:	  -Date-             -Person-               -Updates-
            September 25, 2016		AT          *Created HexTile CLass
                                                    *Submitted as AT's Project 1

            November 7, 2016   		AS          *Standardized Documentation 
                                                     and Formatting 
												
            November 9, 2016		AS          *Updated yieldResources().
                                                     It now works with player class.

            November 18, 2016           AS          *Added Javadoc documentation

            November 23, 2016           AT          * Minor comment addition

            November 25, 2016           AS          *Added Polygon hexagon

            December 5, 2016            OB          *Added centerCoordinate property
                                                    *Modified Constructor, so it 
                                                     calculates centerCoordinate
                                                     

  

 */
import javafx.scene.shape.Polygon;
import static javafx.scene.paint.Color.*;
import javafx.scene.shape.Circle;

/**
 *
 * @author d101-22
 */
public class HexTile {

//                              Class Properties
// _____________________________________________________________________________
    private int resourceYield; // Holds the type of resource tile will yield if numRoll is rolled
    private int numRoll; // Number if rolled, resources yielded
    private Intersection[] intersections; // Corner nodes for each corner of the HexTile
    private boolean robber; // is robber currently on tile?
    private boolean center; // center has no yield

    Polygon hexagon = new Polygon();

    Coordinate centerCoordinates;

//                               Constructors
// _____________________________________________________________________________
    HexTile(Intersection[] intersections) {
        robber = false;
        center = false;
        this.intersections = intersections;

        double tempX = 0.0;
        double tempY = 0.0;

        for (Intersection i : intersections) {
            hexagon.getPoints().add(i.getLocation().getUIX());
            hexagon.getPoints().add(i.getLocation().getUIY());

            // Summing all x- and y- coodrinates
            tempX += i.getLocation().getUIX();
            tempY += i.getLocation().getUIY();
        }

        /* Getting avarage of all values to get center coordinates for each tile.
        
           This is how I thought it should be done: 
        
                * tempX = tempX / 6;
                * tempY = tempY / 6;
                *
                * centerCoordinates = new Coordinates(tempX, tempY);
        
            And we should be all set to go. 
            But it wasn't working how I expected. 
            So, I came up with this...
        
         */
        
     //   tempX = tempX / 210 - 1.3;
      //  tempY = tempY / 210 - 0.5;

        tempX = tempX / 210 - 1.3;
        tempY = tempY / 210 - 2;
        
        centerCoordinates = new Coordinate(tempX, tempY);
       

        hexagon.setFill(ALICEBLUE);
    }

//                          Accessors and Mutators
// _____________________________________________________________________________
    /**
     *
     * @return
     */
    public int getResourceYield() {
        return this.resourceYield;
    }

    // randomly assigns index value of resources array as resourceYield
    /**
     *
     */
    public void setResourceYield() {
        int rand = getRandInt(0, 4);
        this.resourceYield = rand;
    }

    /**
     *
     * @param i
     */
    public void setResourceYieldM(int i) {
        resourceYield = i;
    }

    /**
     *
     * @return
     */
    public int getNumRoll() {
        return numRoll;
    }

    // numRoll must be between 2 and 12 (double dice roll), but not 7 (robber)
    /**
     *
     */
    public void setNumRoll() {
        int randInt = getRandInt(2, 12);

        while (randInt == 7) {
            randInt = getRandInt(2, 12);
        }
        numRoll = randInt;
    }

    /**
     *
     * @param i
     */
    public void setNumRollM(int i) {
        numRoll = i;
    }

    /**
     *
     * @return
     */
    public boolean hasRobber() {
        return robber;
    }

    // called when robber is moved on (true) or off (false)
    /**
     *
     * @param robber
     */
    public void setRobber(boolean robber) {
        this.robber = robber;
    }

    /**
     *
     * @return
     */
    public boolean isCenter() {
        return center;
    }

    /**
     *
     * @param center
     */
    public void setCenter(boolean center) {
        this.center = center;
    }

//                                 Methods
// _____________________________________________________________________________
    /**
     *
     */
    public void yieldResources() {

        // Tiles with robber and center tile do not yield resources
        if (!this.hasRobber() && !this.isCenter()) {
            // For each intersection in array of intersections, give the occupying player(s) (if any) resources
            for (Intersection intersection : getIntersections()) {
                // If intersection has a settlement on it
                if (intersection.occupied()) {

                    // Player object for ease of use, represents owning player
                    Player currentPlayer = GameManager.players[intersection.getPlayer()];
                    // Yield resources corresponding to type of settlement
                    int yieldSize = intersection.getSettlementType();

                    // Add the relevant resource in the relevant quantity
                    currentPlayer.addResource(this.getResourceYield(), yieldSize);

                }
            }
        }
    }

    /**
     *
     * @param min
     * @param max
     * @return
     */
    public static int getRandInt(int min, int max) {
        int random;

        // Math.random() returns a double in [0, 1)
        // multiplying this by one more than the difference between min and max
        // yields a double in [min, max)
        // typecast to int and return
        random = min + (int) (Math.random() * (max - min + 1));

        return random;
    }

    /**
     *
     * @return
     */
    public Intersection[] getIntersections() {
        return intersections;
    }

    /**
     *
     * @param intersections
     */
    public void setIntersections(Intersection[] intersections) {
        this.intersections = intersections;
    }

}
