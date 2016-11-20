


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


Classes:    ClientUI

                                    Summary:
This class sets up the client-side user interface. It displays the game board,
player information, dialog box prompts, buttons for play and various menus.

 
Activity:	  -Date-             -Person-               -Updates-
            November 20, 2016		AS          *Created ClientUI class
                                                    *Created primaryStage titled
                                                     "Settlers of Catan"
                                                    *Created StackPane gameBoard
                                                    
				


 */

import java.awt.Color;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import static javafx.scene.layout.BorderStroke.*;
import static javafx.scene.layout.BorderStrokeStyle.*;
import static javafx.scene.layout.CornerRadii.*;
import javafx.scene.layout.StackPane;
import static javafx.scene.paint.Color.*;
import javafx.stage.Stage;
/**
 *
 */
public class ClientUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Settlers of Catan");
        BorderPane bp = new BorderPane();
        StackPane gameBoard = new StackPane();
        Border b =new Border(new BorderStroke(BLACK, SOLID, EMPTY, DEFAULT_WIDTHS));
        gameBoard.setBorder(b);
        
        
        bp.setCenter(gameBoard);
        Scene scene = new Scene(bp, 1280, 720);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
