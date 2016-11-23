
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

    static double scaleFactor = 35;
    static double xOffset = 1.3;
    static double yOffset = .5;
    static double circleSize = 5.0;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Settlers of Catan");
        BorderPane bp = new BorderPane();
        Pane gameBoard = new Pane();
        gameBoard.setMaxSize(700, 600);
        gameBoard.setMinSize(700, 600);
        Border b = new Border(new BorderStroke(BLACK, SOLID, EMPTY, DEFAULT_WIDTHS));
        gameBoard.setBorder(b);

        ArrayList<Line> lines = new ArrayList<>();

        for (Boundary boundary : GameManager.boundaries) {
            lines.add(boundary.getLine());
        }

        for (Line line : lines) {
            gameBoard.getChildren().add(line);
        }

        for (Intersection intersection : GameManager.intersections) {
            Circle circle = intersection.getCircle();
            circle.setStroke(BLACK);
            circle.setFill(WHITE);
            gameBoard.getChildren().add(circle);
        }

        bp.setCenter(gameBoard);
        Scene scene = new Scene(bp, 1280, 720);

        primaryStage.setScene(scene);

        primaryStage.show();
    }

    ArrayList<Boundary> findBuildableRoads(int currentPlayerID) {

        ArrayList<Boundary> buildableRoads = new ArrayList<>();

        for (Boundary b : GameManager.boundaries) {
            if (b.isOccupiable(currentPlayerID)) {
                buildableRoads.add(b);
            }
        }

        return buildableRoads;
    }

    void buildARoad(ArrayList<Boundary> buildableRoads, ArrayList<Line> lines, int activePlayerID) {

        for (Line line : lines) {
            for (Boundary road : buildableRoads) {
                if (road.getLine() == line) {
                    line.setStrokeWidth(4);
                    line.setOnMouseClicked(e -> Bank.GUIBuildRoad(activePlayerID, road));
                }
            }
        }
    }

    ArrayList<Intersection> findBuildableSettlements(int currentPlayerID, boolean setUpPhase) {

        ArrayList<Intersection> buildableIntersections = new ArrayList<>();

        for (Intersection i : GameManager.intersections) {
            if (i.isOccupiable(currentPlayerID, setUpPhase)) {
                buildableIntersections.add(i);
            }
        }

        return buildableIntersections;
    }

    void buildASettlement(ArrayList<Intersection> buildableSettlements, ArrayList<Circle> circles, int activePlayerID) {

        for (Circle circle : circles) {
            for (Intersection intersection : buildableSettlements) {
                if (intersection.getCircle() == circle) {
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
                buildableCities.add(i);
            }
        }

        return buildableCities;
    }

    void buildACity(ArrayList<Intersection> buildableCities, ArrayList<Circle> circles, int activePlayerID) {

        for (Circle circle : circles) {
            for (Intersection intersection : buildableCities) {
                if (intersection.getCircle() == circle) {
                    circle.setStrokeWidth(4);
                    circle.setOnMouseClicked(e -> Bank.GUIBuildCity(activePlayerID, intersection));
                }
            }
        }
    }

}
