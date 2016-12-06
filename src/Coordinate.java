/*  
                        Coordinate - Settlers of Catan

Class:      Adjvanced Java - CIT-285-01
            Professor Miller, Fall 2016

Group:      RARA - Settlers of Catan
            Ruchelly Almeida
            Alessandra Shipman     
            Oleksii Butakov
            Andrew Thomas

Files:      Bank.java
            Boundary.java
            ClientUI.java
            Coordinate.java (Current File)
            DevelopmentCard.java 
            GameManager.java
            HexTile.java
            Intersection.java 
            Player.java
            Trade.java 
            READ_THIS_FIRST.txt
            CatanGameboard.jpeg


Classes:    Coordinate

                                    Summary:
 This is a relatively simple class. Its purpose is simply to accept an x and y
 coordinate value which will then be used as the postition of intersection 
 objects and, by extension, boundaries.

 
Activity:	  -Date-             -Person-               -Updates-
            October 20, 2016		AS          * Created Coordinate class 
                                                      within the Intersection.java 
                                                      file
				
            November 7, 2016		AS          * Standardized Documentation 
                                                      and Formatting 
                                       
            November 18, 2016           AS          * Moved Coordinate class to 
                                                      this file
                                                    * Added JavaDoc documentation

            November 20, 2016           AT          * Added getUIX and getUIY 
                                                      methods which return X and 
                                                      Y coordinates including UI 
                                                      offsets and scale factors

            November 23, 2016           AT          * Added comments for getUIX 
                                                      and getUIY

            December 2, 2016            AS  `       * Updated Javadoc documentation

 */

/**
* The <code> Coordinate </code> class accepts x and y coordinate values and is 
* used to represent locations on the game board.
*/
class Coordinate {

//                              Class Properties
// _____________________________________________________________________________
    //These hold the x and y coordinates of an ordered pair
    private final double x;
    private final double y;

//                               Constructors
// _____________________________________________________________________________
   
    /**
     * <code> Coordinate </code> Constructor
     * 
     * @param xValue x coordinate value
     * @param yValue y coordinate value
     */
    
    Coordinate(double xValue, double yValue) {
        x = xValue;
        y = yValue;
    }

//                          Accessors and Mutators
// _____________________________________________________________________________
   
    //returns x value
    double getX() {
        return x;
    }
    
    //returns y value
    double getY() {
        return y;
    }
    
    // returns x value adjusted for GUI
    double getUIX(){
        return (x + ClientUI.xOffset) * ClientUI.scaleFactor;
    }
    
    // returns y value adjusted for GUI
    double getUIY(){
        return (y + ClientUI.yOffset) * ClientUI.scaleFactor;
    }

}
