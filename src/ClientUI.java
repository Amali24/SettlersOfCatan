
/*  
                    Client User Interface - Settlers of Catan

Class:      Adjvanced Java - CIT-285-01
            Professor Miller, Fall 2016

Group:      RARA - Settlers of Catan
            Ruchelly Almeida
            Alessandra Shipman     
            Oleksii Butakov
            Andrew Thomas

Files:      Bank.java
            Boundary.java
            ClientUI.java (Current File)
            Coordinate.java 
            DevelopmentCard.java 
            GameManager.java
            HexTile.java
            Intersection.java 
            Player.java
            Trade.java 
            READ_THIS_FIRST.txt
            CatanGameboard.jpeg


Classes:    ClientUI

                                    Summary:
This class sets up the client-side user interface. It displays the game board,
player information, dialog box prompts, buttons for play and various menus.

 
Activity:	  -Date-             -Person-               -Updates-
            November 20, 2016		AS          *Created ClientUI class
                                                    *Created primaryStage titled
                                                     "Settlers of Catan"
                                                    *Created StackPane gameBoard
                                        
                                        AT          * Added lines and circles from
                                                      Boundary and Intersection
                                                      classes to draw gameboard
                                                    * Added scaleFactor, circleSize,
                                                      xOffset, and yOffset properties
                                                      for ease of UI manipulation
                                                    * Added findBuildableRoads
                                                      and buildARoad method
                                                      to allow UI-driven buying of
                                                      roads
                                                    
				


 */
import java.util.ArrayList;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import static javafx.scene.layout.BorderStroke.*;
import static javafx.scene.layout.BorderStrokeStyle.*;
import static javafx.scene.layout.CornerRadii.*;
import static javafx.scene.paint.Color.*;
import javafx.scene.shape.*;
import javafx.stage.Stage;

public class ClientUI extends Application {

    // Scale factor multiplies the coordinates of each point by a consistent ratio
    // To scale UI up to desired size
    static double scaleFactor = 35;
    // X and Y offsets are a hacky way to center the game board
    // Will hopefully come up with a better and more scalable way to do this
    static double xOffset = 1.3;
    static double yOffset = .5;
    // Default size for circles
    static double circleSize = 5.0;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Settlers of Catan");
        BorderPane bp = new BorderPane();
        Pane gameBoard = new Pane();
        // Min size and max size are currently the same
        // Will hopefully allow resizing eventually
        gameBoard.setMaxSize(700, 600);
        gameBoard.setMinSize(700, 600);
       
        // Creates a black boundary
        Border b = new Border(new BorderStroke(BLACK, SOLID, EMPTY, DEFAULT_WIDTHS));
        gameBoard.setBorder(b);

        // Iterate over all boundaries and add their respective lines to the GUI
        for (Boundary boundary : GameManager.boundaries) {
            gameBoard.getChildren().add(boundary.getLine());
        }
        
        // Iterate over intersections and add their circles to the GUI
        for (Intersection intersection : GameManager.intersections) {
            Circle circle = intersection.getCircle();
            
            // Set all circles to hollow black
            circle.setStroke(BLACK);
            circle.setFill(WHITE);
            
            gameBoard.getChildren().add(circle);
        }

        // Put game board at center of GUI frame
        bp.setCenter(gameBoard);
        // Set up scene size
        Scene scene = new Scene(bp, 1280, 720);

        primaryStage.setScene(scene);

        primaryStage.show();
    }

    ArrayList<Boundary> findBuildableRoads(int currentPlayerID) {

        // Create an ArrayList to hold roads the active player can build on
        ArrayList<Boundary> buildableRoads = new ArrayList<>();

        for (Boundary b : GameManager.boundaries) {
            if (b.isOccupiable(currentPlayerID)) {
                // For each boundary, if it is buildable, add it to the ArrayList
                buildableRoads.add(b);
            }
        }

        // Return the ArrayList
        return buildableRoads;
    }

    void buildARoad(ArrayList<Boundary> buildableRoads, Boundary[] boundaries, int activePlayerID) {
        
        // For each boundary in the game,
        for (Boundary b : boundaries) {
            // Check against every buildableRoad (number should be relatively low)
            for (Boundary road : buildableRoads) {
                // Create a Line object for ease of use
                Line line = b.getLine();
                
                // If the buildable road is equal to the current road
                if (road.getLine() == line) {
                    // Make it wider and setOnClick to GUIBuildRoad method
                    line.setStrokeWidth(4);
                    line.setOnMouseClicked(e -> Bank.GUIBuildRoad(activePlayerID, road));
                }
            }
        }
    }

    ArrayList<Intersection> findBuildableSettlements(int currentPlayerID, boolean setUpPhase) {
        
       // Create an ArrayList to hold Intersections the active player can build on
        ArrayList<Intersection> buildableIntersections = new ArrayList<>();

        for (Intersection i : GameManager.intersections) {
            if (i.isOccupiable(currentPlayerID, setUpPhase)) {
                // If the intersection is occupiable, add it
                buildableIntersections.add(i);
            }
        }

        // Return list
        return buildableIntersections;
    }

    void buildASettlement(ArrayList<Intersection> buildableSettlements, Intersection[] intersections, int activePlayerID) {

        for (Intersection i : intersections) {
            // For each intersection in the game, create a Circle from its circle
            Circle circle = i.getCircle();
            
            for (Intersection intersection : buildableSettlements) {
                // Compare each circle to buildableSettlements
                if (intersection.getCircle() == circle) {
                    // Make stroke thicker and set clickable if its buildable
                    circle.setStrokeWidth(4);
                    circle.setOnMouseClicked(e -> Bank.GUIBuildSettlement(activePlayerID, intersection));
                }
            }
        }
    }

    ArrayList<Intersection> findBuildableCities(int currentPlayerID) {

        ArrayList<Intersection> buildableCities = new ArrayList<>();

        for (Intersection i : GameManager.intersections) {
            if (i.getPlayer() == currentPlayerID) {
                // You can only build a city where you already have a settlement
                buildableCities.add(i);
            }
        }
        // Return ArrayList of all buildable locations for cities
        return buildableCities;
    }

    void buildACity(ArrayList<Intersection> buildableCities, Intersection[] intersections, int activePlayerID) {

        for (Intersection i : intersections) {
            // For every intersection create a circle from its circle
            Circle circle = i.getCircle();
            // Compare to the buildable cities
            for (Intersection intersection : buildableCities) {
                if (intersection.getCircle() == circle) {
                    // Make it bigger and clickable
                    circle.setStrokeWidth(4);
                    circle.setOnMouseClicked(e -> Bank.GUIBuildCity(activePlayerID, intersection));
                }
            }
        }
    }

}
