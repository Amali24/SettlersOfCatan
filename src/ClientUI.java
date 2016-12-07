
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
            November 20, 2016		AS          * Created ClientUI class
                                                    * Created primaryStage titled
                                                      "Settlers of Catan"
                                                    * Created StackPane gameBoard
                                        
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

            November 30, 2016           OB          * Added Resources Panel 
                                                    * Added CSS styling for buttons 

                                                    
	    November 26, 2016		RA	    * Created createStatsPanel method, lines 164
	    					      to 212 and placed it in a StackPane 
						      Added panels to scene 	

            November 27, 2016           AT          * Added background
                                                    * Added new gameboard button 
                                                      to allow users to randomly
                                                      generate new gameboard if 
                                                      desired
                                                    * Added src Images file
                                        
                                        AS&AT       * Edited images to better fit
                                                      HexTiles
            
            December 5, 2016           OB          * Added methods for various types
                                                     of pop-up windows
                                                   * Added numbered circles for each tile

            December 6, 2016           AS          * Added Trade Menu
                                                    * Added Player Trade Menu
                                                    * Added Bank Trade Menu
                                                    * Player and bank trade 
                                                      menu methods agregate 
                                                      user input and call trade 
                                                      methods

            December 6, 2016            AT         * Added start-up phase
                                                   * Added prompt box
                                                   * Added moveRobber and stealFrom
                                                   
                                                    

 */
import java.io.*;
import java.util.*;
import java.util.logging.*;

import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import static javafx.scene.layout.BackgroundPosition.*;
import static javafx.scene.layout.BackgroundRepeat.*;
import static javafx.scene.layout.BorderStroke.*;
import static javafx.scene.layout.BorderStrokeStyle.*;
import static javafx.scene.layout.CornerRadii.*;
import javafx.scene.paint.Color;
import static javafx.scene.paint.Color.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.stage.*;

public class ClientUI extends Application {

    // Scale factor multiplies the coordinates of each point by a consistent ratio
    // To scale UI up to desired size
    static double scaleFactor = 35;
    // X and Y offsets are a hacky way to center the game board
    // Will hopefully come up with a better and more scalable way to do this
    static double xOffset = 1.3;
    static double yOffset = 2;
    // Default size for circles
    static double circleSize = 5.0;
    static double hexCircleSize = 20.0;
    // Window sizes
    private double maxSizeX = 900;
    private double minSizeX = 700;
    private double maxSizeY = 800;
    private double minSizeY = 600;

    // Strings for prompts during turns
    static String setUpPhase = "Setup Phase\n"
            + "During set up phase, you will build one settlement and then one road per turn.\n\n"
            + "Turns are serpentine during this phase, and each player will get two turns.\n\n"
            + "It is not necessary to click end turn during this phase.";

    static String startTurn = "Start of Turn Phase\n"
            + "At the start of your turn you can roll the dice by clicking the "
            + "roll button, or play development cards by clicking the Development "
            + "Card button.";

    static String rolledSeven = "Select a tile to move the robber to.\n\n"
            + "The robber will steal one resource (at random) from a player you select on that tile";

    static String afterRoll = "\nYou may now trade, build, buy development cards, or end your turn";

    static String gameOver = "";

    // UI elements
    Insets insets = new Insets(12);
    DropShadow ds = new DropShadow();

    // ArrayLists of UI elements for intersections and boundaries
    ArrayList<Circle> circles = new ArrayList<>();
    ArrayList<Line> lines = new ArrayList<>();

    // Box to prompt players with available actions
    TextArea promptBox = new TextArea();

    // Boolean holds whether a player has rolled this turn (only one roll per turn)
    static boolean playerRolled = false;

    // Button styling using CSS. 
    String btnStyle
            = "-fx-text-fill: white;\n"
            + "-fx-font-family: \"Arial Narrow\";\n"
            + "-fx-font-weight: bold;\n"
            + "-fx-font-size: 11pt;\n"
            + "-fx-background-color: linear-gradient(#61a2b1, #2A5058);\n"
            + "-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );\n"
            + "-fx-background-color: linear-gradient(#2A5058, #61a2b1);\n";

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Settlers of Catan");
        Pane bgPane = new Pane();
        BorderPane bp = new BorderPane();
        Pane gameBoard = new Pane();

        bgPane.getChildren().add(bp);

        // Panel to hold Player's information
        StackPane player1Panel = new StackPane();
        StackPane player2Panel = new StackPane();
        StackPane player3Panel = new StackPane();
        StackPane player4Panel = new StackPane();

        try {
            // Creates player's information panels (Pane, playerId, background)
            createStatsPanel(player1Panel, 0, "Images/bluePlayer3.png");
            createStatsPanel(player2Panel, 1, "Images/redPlayer3.png");
            createStatsPanel(player3Panel, 2, "Images/greenPlayer2.png");
            createStatsPanel(player4Panel, 3, "Images/yellowPlayer.png");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ClientUI.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Displays panels of players 1 and  3
        VBox left = new VBox();

        // Set size of prompt box
        promptBox.setMaxWidth(255);
        promptBox.setMinWidth(255);
        promptBox.setMaxHeight(255);
        promptBox.setMinHeight(255);

        // Prompt box starts with setupPhase text
        promptBox.setText(setUpPhase);
        promptBox.setWrapText(true);

        // Add resource panels and prompt box to left side
        left.getChildren().addAll(player1Panel, promptBox, player3Panel);
        left.setAlignment(Pos.TOP_LEFT);
        left.setSpacing(50);

        // Displays panels of players 2 and 4
        VBox right = new VBox();
        right.getChildren().addAll(player2Panel, player4Panel);
        right.setAlignment(Pos.TOP_RIGHT);
        right.setSpacing(355);
      

        // Min size and max size are currently the same
        // Will hopefully allow resizing eventually
        gameBoard.setMaxSize(900, 800);
        gameBoard.setMinSize(700, 600);

        // Creates a black boundary
        Border b = new Border(new BorderStroke(BLACK, SOLID, EMPTY, DEFAULT_WIDTHS));
        gameBoard.setBorder(b);

        // Iterate over Hexes and adds them to the GUI
        for (HexTile tile : GameManager.tiles) {
            Polygon hex = tile.hexagon;

            gameBoard.getChildren().add(hex);

        }

        // Iterate over Hexes and adds numbered circles for each hex
        for (HexTile tile : GameManager.tiles) {

            double centerX = tile.centerCoordinates.getUIX();
            double centerY = tile.centerCoordinates.getUIY();

            if (!tile.isCenter()) {
                Circle circle = new Circle(centerX, centerY, hexCircleSize);
                circle.setFill(WHITE);
                circle.setStroke(Color.web("black", 1.0));
                circle.setStrokeWidth(2);

                Text text = new Text(String.valueOf(tile.getNumRoll()));
                text.setFont(Font.font(null, FontWeight.BOLD, 16));

                text.setX(centerX - 5);
                text.setY(centerY + 3);

                text.setBoundsType(TextBoundsType.VISUAL);

                gameBoard.getChildren().addAll(circle, text);
            }
        }

        // Iterate over all boundaries and add their respective lines to the GUI
        for (Boundary boundary : GameManager.boundaries) {
            lines.add(boundary.getLine());
            gameBoard.getChildren().add(boundary.getLine());
        }

        // Iterate over intersections and add their circles to the GUI
        for (Intersection intersection : GameManager.intersections) {
            Circle circle = intersection.getCircle();

            // Set all circles to hollow black
            circle.setStroke(BLACK);
            circle.setFill(WHITE);

            circles.add(circle);

            gameBoard.getChildren().add(circle);
        }

        // Put game board at center of GUI frame
        bp.setCenter(gameBoard);

        // ___________________________  Buttons ___________________________
        HBox hBoxButtons = new HBox(25);

        Button btnRoll = new Button("Roll");
        // Applying predefined Style for each button
        btnRoll.setStyle(btnStyle);
        btnRoll.setOnAction(e
                -> {
            // If it is the START_TURN phase and you have not alreaady rolled
            if (GameManager.gamePhase == GameManager.START_TURN && !playerRolled) {
                // set rolled to true
                playerRolled = true;
                // roll the dice
                int diceRoll = GameManager.rollDice();
                // If a 7 is rolled, initiate robber functions
                if (diceRoll == 7) {
                    // Inform player
                    promptBox.setText("You Rolled a 7\n");
                    // Check all the players for > 7 resource and "steal" half
                    for (Player p : GameManager.players) {
                        if (p.getResourceTotal() > 7) {
                            promptBox.appendText("The Robber stole "
                                    + p.getResourceTotal() / 2 + " resources from player "
                                    + p.getPlayerID() + 1);
                        }
                    }
                    // Steal the resources
                    GameManager.robberSteal();
                    // Inform user of rolledSeven actions
                    promptBox.appendText(rolledSeven);

                    for (HexTile tile : GameManager.tiles) {
                        // for every tile, set on click to move robber
                        tile.hexagon.setOnMouseClicked(e1 -> {
                            if (tile.hasRobber()) {
                                // if the tile already has the robber, inform player
                                // to try again
                                promptBox.appendText("You must move the robber");
                            } else {
                                // move robber
                                GUImoveRobber(tile);
                            }
                        });
                    }
                } else {
                    // any roll other than a 7 yields resources
                    // tell player what they rolled
                    promptBox.setText("You rolled a " + diceRoll);

                    for (HexTile tile : GameManager.tiles) {
                        if (tile.getNumRoll() == diceRoll) {
                            // yield the resources of every tile that matches the die roll
                            tile.yieldResources();
                            for (Intersection i : tile.getIntersections()) {
                                // If it's occupied, print which players gained which resources
                                if (i.occupied()) {
                                    promptBox.appendText("\nPlayer " + (i.getPlayer() + 1) + " received " + i.getSettlementType() + " ");
                                    switch (tile.getResourceYield()) {
                                        case 0:
                                            promptBox.appendText("Brick");
                                            break;
                                        case 1:
                                            promptBox.appendText("Lumber");
                                            break;
                                        case 2:
                                            promptBox.appendText("Ore");
                                            break;
                                        case 3:
                                            promptBox.appendText("Wheat");
                                            break;
                                        case 4:
                                            promptBox.appendText("Wool");
                                            break;

                                    }
                                }
                            }
                        }
                    }
                    // Add afterRoll text to promptBox
                    GameManager.gamePhase = GameManager.AFTER_ROLL;
                    promptBox.appendText(afterRoll);
                }
            }
        });

        // Build stuff button
        Button btnBuild = new Button("Build Improvements");
        btnBuild.setStyle(btnStyle);
        btnBuild.setOnAction(e -> {
            if (GameManager.gamePhase == GameManager.AFTER_ROLL) {
                openBuildMenu();
            }
        });
        Button btnDevCards = new Button("Development Cards");
        btnDevCards.setOnAction(e -> {
            if (GameManager.gamePhase != GameManager.SETUP) {
                // DevCard stuff
            }
        });
        btnDevCards.setStyle(btnStyle);

        // Trade Button
        Button btnTrade = new Button("Trade");
        btnTrade.setOnAction(e -> openTradeMenu());
        btnTrade.setStyle(btnStyle);

        // End Turn Button
        Button btnEndTurn = new Button("End Turn");
        btnEndTurn.setStyle(btnStyle);
        btnEndTurn.setOnAction(e -> {
            // End turn button not usable during setup phase
            if (!GameManager.isSetUpPhase) {
                // Call end turn method
                GameManager.endTurn(GameManager.isSetUpPhase);
                // Set rolled flag back to false
                playerRolled = false;
                // Display start of turn text
                GameManager.gamePhase = GameManager.START_TURN;
                promptBox.setText(startTurn);
            }
        });

        // Error window test button
        Button btnTest = new Button("TEST");
        btnTest.setOnAction(e -> {
            showWarningDialog("TEST");
            showErrorDialog("TEST");
        });

        // Add all buttons to screen
        hBoxButtons.getChildren().addAll(btnRoll, btnBuild, btnDevCards, btnTrade, btnEndTurn, btnTest);
        hBoxButtons.setAlignment(Pos.CENTER);

        // ________________________  Resource Panel ____________________________
        // VBox that holds Button and Available Resources Panels, 
        // and located at the bottom of BorderPanel
        VBox vBoxBottom = new VBox(5);

        // This HBox holds StackPane that displays available resources 
        // for the Active Player
        HBox resourcesHBox = new HBox(300);

        // Panel that deisplays available resources to the active player
        StackPane resourcePanel = new StackPane();
        resourcePanel.setAlignment(new Label("Available Resources"), Pos.CENTER);

        // Calling method that fills our Resource Pane with up-to-date information
        createResoursePanel(resourcePanel);

        resourcesHBox.getChildren().add(resourcePanel);
        resourcesHBox.setAlignment(Pos.CENTER);

        // Adding Resources and Button panels to the VBox
        vBoxBottom.getChildren().addAll(resourcesHBox, hBoxButtons);
        // _____________________________________________________________________

        // Put players 1 and 3 information panels on the left of frame
        bp.setLeft(left);
        // Put players 2 and 4 information panels on the right of frame
        bp.setRight(right);
        // vBoxBottom contains buttons and current player's Resource panel
        bp.setBottom(vBoxBottom);

        Image woodImg = new Image(this.getClass().getClassLoader().getResourceAsStream("Images/background.jpg"));
        BackgroundImage bgWood = new BackgroundImage(woodImg, NO_REPEAT, NO_REPEAT, CENTER, BackgroundSize.DEFAULT);

        bgPane.setBackground(new Background(bgWood));

        // Set up scene size
        Scene scene = new Scene(bgPane, 1210, 845); // previous width is 1280

        primaryStage.setScene(scene);

        primaryStage.show();

        // This calls the setUpPhase method as long as we are in set up phase
        // There's a weird sort of recursion here in the buildSettlement
        // and buildRoad methods
        if (GameManager.isSetUpPhase) {
            setUpPhase();
        }
    }

    // Creates panels with player's information during game
    public void createStatsPanel(Pane pane, int playerId, String backgroundAddress) throws FileNotFoundException {

        Image image = new Image(this.getClass().getClassLoader().getResourceAsStream(backgroundAddress));
        ImageView imageView = new ImageView(image);

        // Creates text for player number
        Text txtPlayer = new Text("   Player " + (playerId + 1));
        txtPlayer.setFill(WHITE);
        txtPlayer.setCache(true);
        txtPlayer.setEffect(ds);
        txtPlayer.setFont(Font.font(null, FontWeight.BOLD, 18));
        txtPlayer.setTextAlignment(TextAlignment.CENTER);

        // Creates grid to hold player's informations
        GridPane gridPane = new GridPane();
        gridPane.setPadding(insets);
        gridPane.add(txtPlayer, 0, 0);
        gridPane.add(new Label(" "), 0, 1);
        gridPane.add(new Text("Resource Count: "), 0, 2);
        gridPane.add(new Label(String.valueOf(GameManager.players[playerId].
                getResourceTotal())), 1, 2);
        gridPane.add(new Text("Dev. Cards: "), 0, 3);
        gridPane.add(new Label(String.valueOf(GameManager.players[playerId].
                getDevelopmentCardCount())), 1, 3);
        gridPane.add(new Text("Victory Points: "), 0, 4);
        gridPane.add(new Label(String.valueOf(GameManager.players[playerId].
                getVisibleVictoryPoints())), 1, 4);
        gridPane.add(new Text("Knight Cards: "), 0, 5);
        gridPane.add(new Label(String.valueOf(GameManager.players[playerId].
                getKnightCards())), 1, 5);
        gridPane.add(new Text("Roads Count: "), 0, 6);
        gridPane.add(new Label(String.valueOf(GameManager.players[playerId].
                getRoadCount())), 1, 6);

        // Adds a border to the pane(panel)
        final String cssDefault = "-fx-border-color: firebrick;\n"
                + "-fx-border-insets: 2;\n"
                + "-fx-border-width: 10;\n"
                + "-fx-background-radius: 5;\n";
        pane.setStyle(cssDefault);

        // Adds background(imageView) and player's info(gridPane) to pane
        pane.getChildren().add(imageView);
        pane.getChildren().add(gridPane);

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
                    line.setStrokeWidth(8);
                    line.setOnMouseClicked(e -> {
                        Bank.GUIBuildRoad(activePlayerID, road);
                        restoreUIElements(circles, lines, GameManager.tiles);
                        if (GameManager.isSetUpPhase) {
                            if (GameManager.gamePhase == GameManager.START_TURN) {
                                promptBox.setText(startTurn);
                            }
                            GameManager.endTurn(GameManager.isSetUpPhase);
                            setUpPhase();
                        }
                    });
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
                    circle.setOnMouseClicked(e -> {
                        Bank.GUIBuildSettlement(activePlayerID, intersection, GameManager.isSetUpPhase);
                        restoreUIElements(circles, lines, GameManager.tiles);
                        // If setup phase, allow user to also build road
                        if (GameManager.isSetUpPhase) {
                            ArrayList<Boundary> buildableRoads = findBuildableRoads(activePlayerID);
                            buildARoad(buildableRoads, GameManager.boundaries, activePlayerID);
                        }
                    });
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
                    circle.setOnMouseClicked(e -> {
                        Bank.GUIBuildCity(activePlayerID, intersection);
                        restoreUIElements(circles, lines, GameManager.tiles);
                    });
                }
            }
        }
    }

    private void openBuildMenu() {
        Player activePlayer = GameManager.players[GameManager.activePlayerID];

        // Build Menu will be in a new stage
        Stage buildMenu = new Stage();

        // Create an HBox for buttons
        HBox btnBox = new HBox(25);
        Button btnBuildRoad = new Button("Build a Road");
        btnBuildRoad.setOnAction(e1 -> {
            if (activePlayer.getResourceCount(GameManager.BRICK) >= 1 && activePlayer.getResourceCount(GameManager.LUMBER) >= 1) {
                // When you click build a road,
                // It will check which roads are buildable, and set those clickable
                ArrayList<Boundary> buildableRoads = findBuildableRoads(GameManager.activePlayerID);
                buildARoad(buildableRoads, GameManager.boundaries, GameManager.activePlayerID);
                // And close the window
                buildMenu.close();
            }else{
                promptBox.appendText("\nYou must have one brick and one lumber to build a road");
            }
        });
        btnBuildRoad.setStyle(btnStyle);

        Button btnBuildSettlement = new Button("Build a Settlement");
        btnBuildSettlement.setOnAction(e1 -> {
            if (activePlayer.getResourceCount(GameManager.BRICK) >= 1 && activePlayer.getResourceCount(GameManager.LUMBER) >= 1
                    && activePlayer.getResourceCount(GameManager.WOOL) >= 1 && activePlayer.getResourceCount(GameManager.WHEAT) >= 1) {
                // Similar to build road button above
                ArrayList<Intersection> buildableSettlements = findBuildableSettlements(GameManager.activePlayerID, GameManager.isSetUpPhase);
                buildASettlement(buildableSettlements, GameManager.intersections, GameManager.activePlayerID);
                buildMenu.close();
            }
            else{
                promptBox.appendText("\nYou must have one brick, one lumber, one wool, and one wheat to build a settlement");
            }
        });
        btnBuildSettlement.setStyle(btnStyle);

        Button btnBuildCity = new Button("Build a City");
        btnBuildCity.setOnAction(e1 -> {
            if (activePlayer.getResourceCount(GameManager.ORE) >= 3 && activePlayer.getResourceCount(GameManager.WHEAT) >= 2) {
                // Similar to build road button above
                ArrayList<Intersection> buildableCities = findBuildableSettlements(GameManager.activePlayerID, GameManager.isSetUpPhase);
                buildACity(buildableCities, GameManager.intersections, GameManager.activePlayerID);
                buildMenu.close();
            }else{
                promptBox.appendText("\nYou must have three ore, and two wheat to build a city");
            }
        });
        btnBuildCity.setStyle(btnStyle);

        Button btnCancel = new Button("Cancel");
        btnCancel.setOnAction(e -> buildMenu.close());
        btnCancel.setStyle(btnStyle);

        btnBox.getChildren().addAll(btnBuildRoad, btnBuildSettlement, btnBuildCity, btnCancel);

        Text txtBuild = new Text("Select a type of Improvement to Build:");
        txtBuild.setFont(new Font(14));
        txtBuild.setTextAlignment(TextAlignment.CENTER);

        BorderPane boPa = new BorderPane();
        boPa.setCenter(btnBox);
        boPa.setTop(txtBuild);

        buildMenu.initModality(Modality.APPLICATION_MODAL);

        buildMenu.setScene(new Scene(boPa));
        buildMenu.setTitle("Build Menu");
        buildMenu.show();
    }

    private void openTradeMenu() {
        Stage tradeMenu = new Stage();
        tradeMenu.setTitle("Trade Menu");

        HBox btnBox = new HBox(25);
        Button btnBankTrade = new Button("Trade with Bank");

        btnBankTrade.setOnAction(e -> {
            bankTradeMenu();
            tradeMenu.close();
        });

        Button btnPlayerTrade = new Button("Trade with Player");
        btnPlayerTrade.setOnAction(e1 -> {
            playerTradeMenu();
            tradeMenu.close();

        });

        Button btnCancel = new Button("Cancel");
        btnCancel.setOnAction(e -> tradeMenu.close());

        btnBox.getChildren().addAll(btnBankTrade, btnPlayerTrade, btnCancel);

        tradeMenu.initModality(Modality.APPLICATION_MODAL);

        tradeMenu.setScene(new Scene(btnBox));

        tradeMenu.show();
    }

    private void bankTradeMenu() {

        int activePlayerID = GameManager.activePlayerID;
        Player activePlayer = GameManager.players[activePlayerID];
        int exchangeRates[] = Bank.exchangeRates(activePlayerID);

        Stage bankTradeMenu = new Stage();
        bankTradeMenu.setTitle("Bank Trade");

        VBox vBox = new VBox();
        HBox offeredBox = new HBox();
        HBox requestedBox = new HBox();

        HBox btnBox = new HBox();

        //placing the buttons into each toggle group means that only one can be selected
        ToggleGroup offeredGroup = new ToggleGroup();
        ToggleGroup requestedGroup = new ToggleGroup();

        //spinners for resource selection
        Spinner<Integer> offeredSpinner = new Spinner<>();

        Trade tradeDeal = new Trade(activePlayerID);
        tradeDeal.setBankTrade(true);

        //Text intructions
        Text offerText = new Text("Select the type and quantity of the resource you would like to trade away:");
        Text requestText = new Text("\n\nSelect the resource you would like in return:");

        offerText.setFont(new Font(14));
        offerText.setTextAlignment(TextAlignment.CENTER);

        requestText.setFont(new Font(14));
        requestText.setTextAlignment(TextAlignment.CENTER);

        //these are the buttons in the offered resource group
        RadioButton oBrickBtn = new RadioButton("Brick");
        oBrickBtn.setOnAction(e -> {
            int rate = exchangeRates[0];
            int max = activePlayer.getResourceCount(0) / rate * rate;
            offeredSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, max, 0, rate));
            requestText.setText("\nFor every " + rate + " bricks, you will receive 1 of the following resources. \n Select which resource you would like in return.");
            if (activePlayer.getResourceCount(0) != 0) {
                tradeDeal.setOfferedResource(0);
            }
        });
        oBrickBtn.setToggleGroup(offeredGroup);

        RadioButton oLumberBtn = new RadioButton("Lumber");
        oLumberBtn.setOnAction(e -> {
            int rate = exchangeRates[1];
            int max = activePlayer.getResourceCount(1) / rate * rate;
            offeredSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, max, 0, rate));
            requestText.setText("\nFor every " + rate + " lumber, you wil receive 1 of the following resources. \n Select which resource you would like in return.");
            if (activePlayer.getResourceCount(1) != 0) {
                tradeDeal.setOfferedResource(1);
            }
        });
        oLumberBtn.setToggleGroup(offeredGroup);

        RadioButton oOreBtn = new RadioButton("Ore");
        oOreBtn.setOnAction(e -> {
            int rate = exchangeRates[2];
            int max = activePlayer.getResourceCount(2) / rate * rate;
            offeredSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, max, 0, rate));
            requestText.setText("\nFor every " + rate + " ore, you will receive 1 of the following resources. \n Select which resource you would like in return.");
            if (activePlayer.getResourceCount(2) != 0) {
                tradeDeal.setOfferedResource(2);
            }
        });
        oOreBtn.setToggleGroup(offeredGroup);

        RadioButton oWheatBtn = new RadioButton("Wheat");
        oWheatBtn.setOnAction(e -> {
            int rate = exchangeRates[3];
            int max = activePlayer.getResourceCount(3) / rate * rate;
            offeredSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, max, 0, rate));
            requestText.setText("\nFor every " + rate + " wheat, you will receive 1 of the following resources. \n Select which resource you would like in return.");
            if (activePlayer.getResourceCount(3) != 0) {
                tradeDeal.setOfferedResource(3);
            }
        });
        oWheatBtn.setToggleGroup(offeredGroup);

        RadioButton oWoolBtn = new RadioButton("Wool");
        oWoolBtn.setOnAction(e -> {
            int rate = exchangeRates[4];
            int max = activePlayer.getResourceCount(4) / rate * rate;
            offeredSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, max, 0, rate));
            requestText.setText("\nFor every " + rate + " wool, you can receive 1 of the following resources. \n Select which resource you would like in return.");
            if (activePlayer.getResourceCount(4) != 0) {
                tradeDeal.setOfferedResource(4);
            }
        });
        oWoolBtn.setToggleGroup(offeredGroup);

        offeredBox.getChildren().addAll(oBrickBtn, oLumberBtn, oOreBtn, oWheatBtn, oWoolBtn, offeredSpinner);

        //these are the buttons in the requested resource group
        RadioButton rBrickBtn = new RadioButton("Brick");
        rBrickBtn.setOnAction(e -> {
            tradeDeal.setRequestedResource(0);
        });
        rBrickBtn.setToggleGroup(requestedGroup);

        RadioButton rLumberBtn = new RadioButton("Lumber");
        rLumberBtn.setOnAction(e -> {
            tradeDeal.setRequestedResource(1);
        });
        rLumberBtn.setToggleGroup(requestedGroup);

        RadioButton rOreBtn = new RadioButton("Ore");
        rOreBtn.setOnAction(e -> {
            tradeDeal.setRequestedResource(2);
        });
        rOreBtn.setToggleGroup(requestedGroup);

        RadioButton rWheatBtn = new RadioButton("Wheat");
        rWheatBtn.setOnAction(e -> {
            tradeDeal.setRequestedResource(3);
        });
        rWheatBtn.setToggleGroup(requestedGroup);

        RadioButton rWoolBtn = new RadioButton("Wool");
        rWoolBtn.setOnAction(e -> {
            tradeDeal.setRequestedResource(4);
        });
        ;

        requestedBox.getChildren().addAll(rBrickBtn, rLumberBtn, rOreBtn, rWheatBtn, rWoolBtn);

        //ungrouped buttons
        Button submitBtn = new Button("Request Trade");
        submitBtn.setOnAction(e -> {
            tradeDeal.setOfferedAmount(offeredSpinner.getValue());
            if (tradeDeal.getOfferedResource() != -2 && tradeDeal.getRequestedResource() != -2 && tradeDeal.getOfferedAmount() != 0) {
                tradeDeal.setRequestedAmount(tradeDeal.getOfferedAmount() / exchangeRates[tradeDeal.getRequestedResource()]);

                Trade.executeTrade(tradeDeal);
            }
        });
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setOnAction(e -> {

            bankTradeMenu.close();
        });

        btnBox.getChildren().addAll(submitBtn, cancelBtn);

        //add HBoxes to VBox
        vBox.getChildren().addAll(offerText, offeredBox, requestText, requestedBox, btnBox);

        bankTradeMenu.initModality(Modality.APPLICATION_MODAL);
        bankTradeMenu.setScene(new Scene(vBox));
        bankTradeMenu.show();

    }

    private void playerTradeMenu() {

        int activePlayerID = GameManager.activePlayerID;
        Player activePlayer = GameManager.players[activePlayerID];

        Stage playerTradeMenu = new Stage();
        playerTradeMenu.setTitle("PlayerTrade");

        VBox vBox = new VBox();
        HBox offeredBox = new HBox();
        HBox requestedBox = new HBox();
        HBox tradingPartnersBox = new HBox();
        HBox btnBox = new HBox();

        //placing the buttons into each toggle group means that only one can be selected
        ToggleGroup offeredGroup = new ToggleGroup();
        ToggleGroup requestedGroup = new ToggleGroup();
        ToggleGroup tradingPartners = new ToggleGroup();

        //spinners for resource selection
        Spinner<Integer> offeredSpinner = new Spinner<>();
        Spinner<Integer> requestedSpinner = new Spinner<>();

        Trade tradeDeal = new Trade(activePlayerID);

        //these are the buttons in the offered resource group
        RadioButton oBrickBtn = new RadioButton("Brick");
        oBrickBtn.setOnAction(e -> {
            offeredSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, activePlayer.getResourceCount(0)));
            tradeDeal.setOfferedResource(0);
        });
        oBrickBtn.setToggleGroup(offeredGroup);

        RadioButton oLumberBtn = new RadioButton("Lumber");
        oLumberBtn.setOnAction(e -> {
            offeredSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, activePlayer.getResourceCount(1)));
            tradeDeal.setOfferedResource(1);
        });
        oLumberBtn.setToggleGroup(offeredGroup);

        RadioButton oOreBtn = new RadioButton("Ore");
        oOreBtn.setOnAction(e -> {
            offeredSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, activePlayer.getResourceCount(2)));
            tradeDeal.setOfferedResource(2);
        });
        oOreBtn.setToggleGroup(offeredGroup);

        RadioButton oWheatBtn = new RadioButton("Wheat");
        oWheatBtn.setOnAction(e -> {
            offeredSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, activePlayer.getResourceCount(3)));
            tradeDeal.setOfferedResource(3);
        });
        oWheatBtn.setToggleGroup(offeredGroup);

        RadioButton oWoolBtn = new RadioButton("Wool");
        oWoolBtn.setOnAction(e -> {
            offeredSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, activePlayer.getResourceCount(4)));
            tradeDeal.setOfferedResource(4);
        });
        oWoolBtn.setToggleGroup(offeredGroup);

        RadioButton oUnknownBtn = new RadioButton("Unknown");
        oUnknownBtn.setOnAction(e -> {
            offeredSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, activePlayer.maxResourceCount()));
            tradeDeal.setOfferedResource(-1);
        });
        oUnknownBtn.setToggleGroup(offeredGroup);

        offeredBox.getChildren().addAll(oBrickBtn, oLumberBtn, oOreBtn, oWheatBtn, oWoolBtn, oUnknownBtn, offeredSpinner);

        //these are the buttons in the requested resource group
        RadioButton rBrickBtn = new RadioButton("Brick");
        rBrickBtn.setOnAction(e -> {
            requestedSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10));
            tradeDeal.setRequestedResource(0);
        });
        rBrickBtn.setToggleGroup(requestedGroup);

        RadioButton rLumberBtn = new RadioButton("Lumber");
        rLumberBtn.setOnAction(e -> {
            requestedSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10));
            tradeDeal.setRequestedResource(1);
        });
        rLumberBtn.setToggleGroup(requestedGroup);

        RadioButton rOreBtn = new RadioButton("Ore");
        rOreBtn.setOnAction(e -> {
            requestedSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10));
            tradeDeal.setRequestedResource(2);
        });
        rOreBtn.setToggleGroup(requestedGroup);

        RadioButton rWheatBtn = new RadioButton("Wheat");
        rWheatBtn.setOnAction(e -> {
            requestedSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10));
            tradeDeal.setRequestedResource(3);
        });
        rWheatBtn.setToggleGroup(requestedGroup);

        RadioButton rWoolBtn = new RadioButton("Wool");
        rWoolBtn.setOnAction(e -> {
            requestedSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10));
            tradeDeal.setRequestedResource(4);
        });
        rWoolBtn.setToggleGroup(requestedGroup);

        RadioButton rUnknownBtn = new RadioButton("Unknown");

        rUnknownBtn.setOnAction(e -> {
            requestedSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10));
            tradeDeal.setRequestedResource(-1);
        });
        rUnknownBtn.setToggleGroup(requestedGroup);

        requestedBox.getChildren().addAll(rBrickBtn, rLumberBtn, rOreBtn, rWheatBtn, rWoolBtn, rUnknownBtn, requestedSpinner);

        //these are the buttons in the tradingPartners group
        RadioButton player1Btn = new RadioButton(GameManager.players[0].getName());
        player1Btn.setOnAction(e -> {
            tradeDeal.setTradingPartner(0);
        });
        player1Btn.setToggleGroup(tradingPartners);

        RadioButton player2Btn = new RadioButton(GameManager.players[1].getName());
        player2Btn.setOnAction(e -> {
            tradeDeal.setTradingPartner(1);
        });
        player2Btn.setToggleGroup(tradingPartners);

        RadioButton player3Btn = new RadioButton(GameManager.players[2].getName());
        player3Btn.setOnAction(e -> {
            tradeDeal.setTradingPartner(2);
        });
        player3Btn.setToggleGroup(tradingPartners);

        RadioButton player4Btn = new RadioButton(GameManager.players[3].getName());
        player4Btn.setOnAction(e -> {
            tradeDeal.setTradingPartner(3);
        });
        player4Btn.setToggleGroup(tradingPartners);

        RadioButton playerUnknownBtn = new RadioButton("Unknown");

        playerUnknownBtn.setOnAction(e -> {
            tradeDeal.setTradingPartner(-1);
        });
        player4Btn.setToggleGroup(tradingPartners);

        if (GameManager.activePlayerID != 0) {
            tradingPartnersBox.getChildren().add(player1Btn);
        }

        if (GameManager.activePlayerID != 1) {
            tradingPartnersBox.getChildren().add(player2Btn);
        }

        if (GameManager.activePlayerID != 2) {
            tradingPartnersBox.getChildren().add(player3Btn);
        }

        if (GameManager.activePlayerID != 3) {
            tradingPartnersBox.getChildren().add(player4Btn);
        }

        tradingPartnersBox.getChildren().add(playerUnknownBtn);

        //ungrouped buttons
        Button submitBtn = new Button("Request Trade");
        submitBtn.setOnAction(e -> {
            if (tradeDeal.getOfferedResource() != -2 && tradeDeal.getRequestedResource() != -2 && tradeDeal.getTradingPartner() != -2) {
                tradeDeal.setOfferedAmount(offeredSpinner.getValue());
                tradeDeal.setRequestedAmount(requestedSpinner.getValue());
                tradeDeal.distributeTradeRequest();
            }
        });
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setOnAction(e -> {

            playerTradeMenu.close();
        });

        btnBox.getChildren().addAll(submitBtn, cancelBtn);

        //Text intructions
        Text offerText = new Text("Select the type and quantity of the resource you would like to trade away:");
        Text requestText = new Text("Select the type and quantity of the resource you would like in return:");
        Text partnerText = new Text("Select the player you would like to offer this trade to:");

        offerText.setFont(new Font(14));
        offerText.setTextAlignment(TextAlignment.CENTER);

        requestText.setFont(new Font(14));
        requestText.setTextAlignment(TextAlignment.CENTER);

        partnerText.setFont(new Font(14));
        partnerText.setTextAlignment(TextAlignment.CENTER);

        //add HBoxes to VBox
        vBox.getChildren().addAll(offerText, offeredBox, requestText, requestedBox, partnerText, tradingPartnersBox, btnBox);

        playerTradeMenu.initModality(Modality.APPLICATION_MODAL);
        playerTradeMenu.setScene(new Scene(vBox));
        playerTradeMenu.show();

    }

    private void restoreUIElements(ArrayList<Circle> intersections, ArrayList<Line> boundaries, HexTile[] tiles) {
        for (Circle i : intersections) {
            // For every circle, restore to strokeWidth 1
            i.setRadius(circleSize);
            if (i.getStroke() == BLACK) {
                // only if it is still BLACK ie unowned
                i.setStrokeWidth(1);
            }
            // Set to do nothing on click
            i.setOnMouseClicked(e -> doNothing());
        }

        for (HexTile t : tiles) {
            // Make hexes not clickable
            t.hexagon.setOnMouseClicked(e -> doNothing());
        }

        for (Line l : boundaries) {
            // Set all lines non-clickable
            l.setOnMouseClicked(e -> doNothing());
            l.setStrokeWidth(4);
        }
    }

    private void doNothing() {
        // does nothing
    }

    private void setUpPhase() {
        // PlayerID is equal to active player (cuts code clutter)
        int playerID = GameManager.activePlayerID;
        // clutter removed by storing in variable
        boolean setupPhase = GameManager.isSetUpPhase;
        // ditto above
        Intersection[] intersections = GameManager.intersections;

        // Call the build settlement methods first, as a settlement must be built first
        ArrayList<Intersection> buildableIntersections = findBuildableSettlements(playerID, setupPhase);
        buildASettlement(buildableIntersections, intersections, playerID);
    }

    // Creates Pane that contains information about the resources 
    // of the current player
    public void createResoursePanel(Pane pane) {
        // Creates grid to hold player's informations
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);;
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(5.0, 5.0, 5.0, 5.0));

        gridPane.add(new Label("Brick"), 0, 0);
        gridPane.add(new Label("  " + String.valueOf(GameManager.players[GameManager.activePlayerID].resourceMaterials[GameManager.BRICK])), 0, 1);

        gridPane.add(new Label("Lumber"), 1, 0);
        gridPane.add(new Label("  " + String.valueOf(GameManager.players[GameManager.activePlayerID].resourceMaterials[GameManager.LUMBER])), 1, 1);

        gridPane.add(new Label("Ore"), 2, 0);
        gridPane.add(new Label("  " + String.valueOf(GameManager.players[GameManager.activePlayerID].resourceMaterials[GameManager.ORE])), 2, 1);

        gridPane.add(new Label("Wheat"), 3, 0);
        gridPane.add(new Label("  " + String.valueOf(GameManager.players[GameManager.activePlayerID].resourceMaterials[GameManager.WHEAT])), 3, 1);

        gridPane.add(new Label("Wool"), 4, 0);
        gridPane.add(new Label("  " + String.valueOf(GameManager.players[GameManager.activePlayerID].resourceMaterials[GameManager.WOOL])), 4, 1);

        // Adds a border to the pane(panel)
        final String cssDefault
                = "-fx-border-color: #C8C8C8;\n"
                + "-fx-border-insets: 2;\n"
                + "-fx-font-size: 12pt;\n"
                + "-fx-border-width: 5;\n"
                + "-fx-box-shadow: 5px;\n"
                + "-fx-background-color: linear-gradient(white,#DDDDDD);\n"
                + "-fx-background-radius: 5;\n";
        pane.setStyle(cssDefault);

        pane.getChildren().add(gridPane);
    }

    void showErrorDialog(String text) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText("An Error Accured");
        alert.setContentText(text);

        alert.showAndWait();
    }

    void showWarningDialog(String text) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Warning Dialog");
        alert.setHeaderText("Warning!");
        alert.setContentText(text);

        alert.showAndWait();
    }

    private void GUImoveRobber(HexTile tile) {
        // Iterate through all hexTiles

        for (HexTile t : GameManager.tiles) {
            t.setRobber(false);
        }
        // the one you click on gets robber
        tile.setRobber(true);
        // output feedback
        promptBox.setText("Robber moved sucessfully\n"
                + "Select a player to steal from, if there are any "
                + "settlements on the tile.");
        // Check all intersections on tile for players
        for (Intersection i : tile.getIntersections()) {
            // If the tile is occupied, allow to steal from them
            if (i.occupied()) {
                i.getCircle().setRadius(circleSize * 2.5);
                i.getCircle().setOnMouseClicked(eh -> {
                    stealFrom(i.getPlayer());
                    promptBox.setText("Successfully stole from player " + (i.getPlayer() + 1));
                    promptBox.appendText(afterRoll);
                     // Set UI Elements back to defaults
                    restoreUIElements(circles, lines, GameManager.tiles);
                });
               
                
            }
        }
    }

    private void stealFrom(int playerID) {
        Player stolenFrom = GameManager.players[playerID];
        Player activePlayer = GameManager.players[GameManager.activePlayerID];

        int resourceToSteal = HexTile.getRandInt(GameManager.BRICK, GameManager.WOOL);

        stolenFrom.deductResource(resourceToSteal, 1);
        activePlayer.addResource(resourceToSteal, 1);
    }

}
