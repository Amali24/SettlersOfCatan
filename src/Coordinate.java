/*

 COORDINATE CLASS
 This is a relatively simple class. Its purpose is simply to accept an x and y
 coordinate value which will then be used as the postition of intersection 
 objects and, by extension, boundaries.
 _______________________________________________________________________________
 */
class Coordinate {

//                              Class Properties
// _____________________________________________________________________________
    //These hold the x and y coordinates of an ordered pair
    private final double x;
    private final double y;

//                               Constructors
// _____________________________________________________________________________
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

}
