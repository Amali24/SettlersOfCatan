
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

            November 23, 2016           AT          * Added comments to every method
                                                    * Fixed some small logical errors
                                                      in build functions
                                                    * Added known issue of not sizing clickable
                                                      nodes after they are clicked to tracker

           November 24, 2016            OB          * Created panes that holds player's info
                                                    * Added methods that initialize JPanels, 
                                                      so they display player's info 
                                                        ~createAndSetStatsPanel(final SwingNode, int) 
                                                             - for displaying particular player's stats
                                                        ~createAndSetResourcePanel(final SwingNode) 
                                                             - for displaying current player available resuources
                                                        ~createAndSetButtonPanel(final SwingNode) 
                                                             - for displaying action-buttons


                                                    
                                                    

                                                    
				


 */
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import static javafx.scene.layout.BorderStroke.*;
import static javafx.scene.layout.BorderStrokeStyle.*;
import static javafx.scene.layout.CornerRadii.*;
import static javafx.scene.paint.Color.*;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

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
        
        
        VBox leftStatsVBox = new VBox(50);
        VBox rightStatsVBox = new VBox(50);
        
        final SwingNode player1Stats = new SwingNode();
        final SwingNode player2Stats = new SwingNode();
        final SwingNode player3Stats = new SwingNode();
        final SwingNode player4Stats = new SwingNode();
        
        // Initializing stats panels 
        createAndSetStatsPanel(player1Stats, 0);
        createAndSetStatsPanel(player2Stats, 1);
        createAndSetStatsPanel(player3Stats, 2);
        createAndSetStatsPanel(player4Stats, 3);
        
        
        leftStatsVBox.getChildren().addAll(player1Stats , player2Stats);
        rightStatsVBox.getChildren().addAll(player3Stats, player4Stats);
     
        HBox bottomHBox = new HBox(50);
        
        final SwingNode resourcePanel = new SwingNode();
        createAndSetResourcePanel(resourcePanel);

        final SwingNode buttonPanel = new SwingNode();
        createAndSetButtonPanel(buttonPanel);
        
        bottomHBox.getChildren().addAll(resourcePanel, buttonPanel);
        bottomHBox.setAlignment(Pos.CENTER);
        
        bp.setBottom(bottomHBox);
        bp.setLeft(leftStatsVBox);
        bp.setRight(rightStatsVBox);
        
        
        // Set up scene size
        Scene scene = new Scene(bp, 1280, 720);

        primaryStage.setScene(scene);

        primaryStage.show();
    }
    
    private void createAndSetResourcePanel(final SwingNode swingNode) {
             SwingUtilities.invokeLater(new Runnable() {
                 @Override
                 public void run() {
                     
                    JLabel labelBrick = new JLabel("Brick");
                    JLabel labelLumber = new JLabel("Lumber");
                    JLabel labelOre = new JLabel("Ore");
                    JLabel labelWheat = new JLabel("Wheat");
                    JLabel labelWool = new JLabel("Wool");
                    
                    JLabel labelBrickCount = new JLabel("");
                    labelBrickCount.setText(String.valueOf(GameManager.players[GameManager.activePlayerID]
                                    .resourceMaterials[GameManager.BRICK]));
                    
                    JLabel labelLumberCount = new JLabel("");
                    labelLumberCount.setText(String.valueOf(GameManager.players[GameManager.activePlayerID]
                                    .resourceMaterials[GameManager.LUMBER]));
                  
                    JLabel labelOreCount = new JLabel("");
                    labelOreCount.setText(String.valueOf(GameManager.players[GameManager.activePlayerID]
                                    .resourceMaterials[GameManager.ORE]));
                    
                    JLabel labelWheatCount = new JLabel("");
                    labelWheatCount.setText(String.valueOf(GameManager.players[GameManager.activePlayerID]
                                    .resourceMaterials[GameManager.WHEAT]));
                    
                    JLabel labelWoolCount = new JLabel("");
                    labelWoolCount.setText(String.valueOf(GameManager.players[GameManager.activePlayerID]
                                    .resourceMaterials[GameManager.WOOL]));
                    
    
                     // create a new panel with GridBagLayout manager
                    JPanel reseourcesPanel = new JPanel(new GridBagLayout());
                    
                    GridBagConstraints constraints = new GridBagConstraints();
                    constraints.anchor = GridBagConstraints.WEST;
                    constraints.insets = new Insets(10, 10, 10, 10);
         
                    // add components to the panel
                    constraints.gridx = 0;
                    constraints.gridy = 0;     
                    reseourcesPanel.add(labelBrick, constraints);
                    
                    constraints.gridy = 1;
                    reseourcesPanel.add(labelBrickCount, constraints);
                    
                    constraints.gridx = 1;
                    constraints.gridy = 0;     
                    reseourcesPanel.add(labelLumber, constraints);
                    
                    constraints.gridy = 1;
                    reseourcesPanel.add(labelLumberCount, constraints);
                    
                    constraints.gridx = 2;
                    constraints.gridy = 0;     
                    reseourcesPanel.add(labelOre, constraints);
                    
                    constraints.gridy = 1;
                    reseourcesPanel.add(labelOreCount, constraints);
                    
                    constraints.gridx = 3;
                    constraints.gridy = 0;     
                    reseourcesPanel.add(labelWheat, constraints);
                    
                    constraints.gridy = 1;
                    reseourcesPanel.add(labelWheatCount, constraints);
                    
                    constraints.gridx =4;
                    constraints.gridy = 0;     
                    reseourcesPanel.add(labelWool, constraints);
                    
                    constraints.gridy = 1;
                    reseourcesPanel.add(labelWoolCount, constraints);
                    
                     // set border for the panel
                    reseourcesPanel.setBorder(BorderFactory.createTitledBorder(
                            BorderFactory.createEtchedBorder(), "Player " + (GameManager.activePlayerID + 1) + " (current) " + "Resources" ));

                    JPanel panel = new JPanel();
                    panel.add(reseourcesPanel);
                    swingNode.setContent(panel);
                 }
             });
         }
    
    private void createAndSetStatsPanel(final SwingNode swingNode, int playerID) {
             SwingUtilities.invokeLater(new Runnable() {
                 @Override
                 public void run() {
                     
                    JLabel labelResource = new JLabel("Resource Count:");
                    JLabel labelDevCards = new JLabel("Development Cards:");
                    JLabel labelVictroyPoints = new JLabel("Victory Points:");
                    JLabel labelKnightCards = new JLabel("Knight Cards:");
                    JLabel labelRoad = new JLabel("Road Count:");
                    
                    JLabel labelResourceCount = new JLabel("");
                    labelResourceCount.setText(String.valueOf(GameManager.players[playerID].getResourceTotal()));
                    
                    JLabel labelDevCardsCount = new JLabel("");
                    labelDevCardsCount.setText(String.valueOf(GameManager.players[playerID].getDevelopmentCardCount()));
                  
                    JLabel labelVictroyPointsCount = new JLabel("");
                    labelVictroyPointsCount.setText(String.valueOf(GameManager.players[playerID].getTotalVictoryPoints()));
                    
                    JLabel labelKnightCardsCount = new JLabel("");
                    labelKnightCardsCount.setText(String.valueOf(GameManager.players[playerID].getKnightCards()));
                    
                    JLabel labelRoadCount = new JLabel("");
                    labelRoadCount.setText(String.valueOf(GameManager.players[playerID].getRoadCount()));
                    
    
                     // create a new panel with GridBagLayout manager
                    JPanel reseourcesPanel = new JPanel(new GridBagLayout());
                    
                    GridBagConstraints constraints = new GridBagConstraints();
                    constraints.anchor = GridBagConstraints.WEST;
                    constraints.insets = new Insets(10, 10, 10, 10);
         
                    // add components to the panel
                    constraints.gridx = 0;
                    constraints.gridy = 0;     
                    reseourcesPanel.add(labelResource, constraints);
                    
                    constraints.gridx = 1;
                    reseourcesPanel.add(labelResourceCount, constraints);
                    
                    constraints.gridx = 0;
                    constraints.gridy = 1;     
                    reseourcesPanel.add(labelDevCards, constraints);
                    
                    constraints.gridx = 1;
                    reseourcesPanel.add(labelDevCardsCount, constraints);
                    
                    constraints.gridx = 0;
                    constraints.gridy = 2;     
                    reseourcesPanel.add(labelVictroyPoints, constraints);
                    
                    constraints.gridx = 1;
                    reseourcesPanel.add(labelVictroyPointsCount, constraints);
                    
                    constraints.gridx = 0;
                    constraints.gridy = 3;     
                    reseourcesPanel.add(labelKnightCards, constraints);
                    
                    constraints.gridx = 1;
                    reseourcesPanel.add(labelKnightCardsCount, constraints);
                    
                    constraints.gridx =0;
                    constraints.gridy = 4;     
                    reseourcesPanel.add(labelRoad, constraints);
                    
                    constraints.gridx = 1;
                    reseourcesPanel.add(labelRoadCount, constraints);
                    
                     // set border for the panel
                    reseourcesPanel.setBorder(BorderFactory.createTitledBorder(
                            BorderFactory.createEtchedBorder(), "Player " + (playerID + 1)  + " Resources"));

                    JPanel panel = new JPanel();
                    panel.add(reseourcesPanel);
                    swingNode.setContent(panel);
                 }
             });
         }
    
    private void createAndSetButtonPanel(final SwingNode swingNode) {
             SwingUtilities.invokeLater(new Runnable() {
                 @Override
                 public void run() {
                     
                    JButton btnBuy = new JButton("Buy");
                    JButton btnRoll = new JButton("Roll");
                    JButton btnTrade = new JButton("Trade");
                    JButton btnEndTurn = new JButton("End Turn");

                     // create a new panel with GridBagLayout manager
                    JPanel buttonPanel = new JPanel(new GridBagLayout());
                    
                    GridBagConstraints constraints = new GridBagConstraints();
                    constraints.anchor = GridBagConstraints.WEST;
                    constraints.insets = new Insets(5, 5, 5, 5);
         
                    // add components to the panel
                    constraints.gridx = 0;
                    constraints.gridy = 0;     
                    buttonPanel.add(btnBuy, constraints);
                    
                    constraints.gridx = 1;
                    buttonPanel.add(btnRoll, constraints);
                    
                    constraints.gridx = 0;
                    constraints.gridy = 1;     
                    buttonPanel.add(btnTrade, constraints);
                    
                    constraints.gridx = 1;
                    buttonPanel.add(btnEndTurn, constraints);
                    
                     // set border for the panel
                    buttonPanel.setBorder(BorderFactory.createTitledBorder(
                            BorderFactory.createEtchedBorder(), "Actions:"));

                    JPanel panel = new JPanel();
                    panel.add(buttonPanel);
                    swingNode.setContent(panel);
                 }
             });
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
