
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
            
            Deceber 04, 2016            AS          * Added setup phase parameter
                                                      to call of GUIBuildSettlement
                                                      method 
 */
import java.io.*;
import java.util.*;
import java.util.logging.*;

import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.effect.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import static javafx.scene.layout.BackgroundPosition.*;
import static javafx.scene.layout.BackgroundRepeat.*;
import static javafx.scene.layout.BorderStroke.*;
import static javafx.scene.layout.BorderStrokeStyle.*;
import static javafx.scene.layout.CornerRadii.*;
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
    static double yOffset = .5;
    // Default size for circles
    static double circleSize = 5.0;

    Insets insets = new Insets(12);
    DropShadow ds = new DropShadow();

    ArrayList<Circle> circles = new ArrayList<>();
    ArrayList<Line> lines = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Settlers of Catan");
        BorderPane bp = new BorderPane();
        Pane gameBoard = new Pane();

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
        left.getChildren().addAll(player1Panel, player3Panel);
        left.setAlignment(Pos.TOP_LEFT);
        left.setSpacing(330);

        // Displays panels of players 2 and 4
        VBox right = new VBox();
        right.getChildren().addAll(player2Panel, player4Panel);
        right.setAlignment(Pos.TOP_RIGHT);
        right.setSpacing(330);

        Image waterImage = new Image(this.getClass().getClassLoader().getResourceAsStream("Images/waterCrop.jpg"));
        BackgroundImage bgWater = new BackgroundImage(waterImage, NO_REPEAT, NO_REPEAT, CENTER, BackgroundSize.DEFAULT);

        gameBoard.setBackground(new Background(bgWater));

        // Min size and max size are currently the same
        // Will hopefully allow resizing eventually
        gameBoard.setMaxSize(700, 600);
        gameBoard.setMinSize(700, 600);

        // Creates a black boundary
        Border b = new Border(new BorderStroke(BLACK, SOLID, EMPTY, DEFAULT_WIDTHS));
        gameBoard.setBorder(b);

        for (HexTile tile : GameManager.tiles) {
            Polygon hex = tile.hexagon;
            gameBoard.getChildren().add(hex);
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
        // Button styling using CSS. 
        String btnStyle
                = "-fx-text-fill: white;\n"
                + "-fx-font-family: \"Arial Narrow\";\n"
                + "-fx-font-weight: bold;\n"
                + "-fx-font-size: 11pt;\n"
                + "-fx-background-color: linear-gradient(#61a2b1, #2A5058);\n"
                + "-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );\n"
                + "-fx-background-color: linear-gradient(#2A5058, #61a2b1);\n";

        HBox hBoxButtons = new HBox(25);

        Button btnRoll = new Button("Roll");
        // Applying predefined Style for each button
        btnRoll.setStyle(btnStyle);
        btnRoll.setOnAction(e
                -> {
            int diceRoll = GameManager.rollDice();
            for (HexTile tile : GameManager.tiles) {
                if (tile.getNumRoll() == diceRoll) {
                    tile.yieldResources();
                }
            }
        });

        /*Button btnBuildRoad = new Button("Build a Road");
        btnBuildRoad.setOnAction(e
                -> {
            ArrayList<Boundary> buildableRoads = findBuildableRoads(GameManager.activePlayerID);
            buildARoad(buildableRoads, GameManager.boundaries, GameManager.activePlayerID);
        });*/
        Button btnBuild = new Button("Build Improvements");
        btnBuild.setStyle(btnStyle);
        btnBuild.setOnAction(e -> openBuildMenu());
        Button btnDevCards = new Button("Development Cards");
        btnDevCards.setStyle(btnStyle);

        Button btnTrade = new Button("Trade");
        btnTrade.setOnAction(e -> openTradeMenu());
        btnTrade.setStyle(btnStyle);

        Button btnEndTurn = new Button("End Turn");
        btnEndTurn.setOnAction(e -> GameManager.endTurn(GameManager.isSetUpPhase));
        btnEndTurn.setStyle(btnStyle);

        /*
        Delete This?
        For Debugging only
         */
        Button btnNewBoard = new Button("New Board");
        btnNewBoard.setOnAction(e -> {
            GameManager.gm1.buildGameboard();
            this.start(primaryStage);
        });
        btnNewBoard.setStyle(btnStyle);

        hBoxButtons.getChildren().addAll(btnRoll, btnBuild, btnDevCards, btnTrade, btnEndTurn, btnNewBoard);
        hBoxButtons.setAlignment(Pos.CENTER);

        // ________________________  Resource Panel ____________________________
        // VBox that holds Button and Available Resources Panels, 
        // and located at the bottom of BorderPanel
        VBox vBoxButtom = new VBox(5);

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
        vBoxButtom.getChildren().addAll(resourcesHBox, hBoxButtons);
        // _____________________________________________________________________

        // Adding vBoxButtom to the bottom of Boarder Pane
        bp.setBottom(vBoxButtom);

        // Put players 1 and 3 information panels on the left of frame
        bp.setLeft(left);
        // Put players 2 and 4 information panels on the right of frame
        bp.setRight(right);

        bp.setBottom(hBoxButtons);

        // Set up scene size
        Scene scene = new Scene(bp, 1280, 720);

        primaryStage.setScene(scene);

        primaryStage.show();
        
        if(GameManager.isSetUpPhase){
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
        gridPane.add(new Text("Devel. cards: "), 0, 3);
        gridPane.add(new Label(String.valueOf(GameManager.players[playerId].
                getDevelopmentCardCount())), 1, 3);
        gridPane.add(new Text("Victory Points: "), 0, 4);
        gridPane.add(new Label(String.valueOf(GameManager.players[playerId].
                getVisibleVictoryPoints())), 1, 4);
        gridPane.add(new Text("Knight cards: "), 0, 5);
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
                    line.setStrokeWidth(4);
                    line.setOnMouseClicked(e -> {
                        Bank.GUIBuildRoad(activePlayerID, road);
                        restoreUIElements(circles, lines);
                        if (GameManager.isSetUpPhase) {
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
                        restoreUIElements(circles, lines);
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
                        restoreUIElements(circles, lines);
                    });
                }
            }
        }
    }

    private void openBuildMenu() {
        Stage buildMenu = new Stage();

        HBox btnBox = new HBox(25);
        Button btnBuildRoad = new Button("Build a Road");
        
        btnBuildRoad.setOnAction(e1 -> {
            ArrayList<Boundary> buildableRoads = findBuildableRoads(GameManager.activePlayerID);
            buildARoad(buildableRoads, GameManager.boundaries, GameManager.activePlayerID);
            buildMenu.close();
        });

        Button btnBuildSettlement = new Button("Build a Settlement");
        btnBuildSettlement.setOnAction(e1 -> {
            ArrayList<Intersection> buildableSettlements = findBuildableSettlements(GameManager.activePlayerID, GameManager.isSetUpPhase);
            buildASettlement(buildableSettlements, GameManager.intersections, GameManager.activePlayerID);
            buildMenu.close();

        });

        Button btnBuildCity = new Button("Build a City");

        Button btnCancel = new Button("Cancel");
        btnCancel.setOnAction(e -> buildMenu.close());

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
    
    private void bankTradeMenu(){

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
        int max = activePlayer.getResourceCount(0)/rate*rate;
        offeredSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, max, 0, rate));
        requestText.setText("For every " + rate + " bricks, you will receive 1 of the following resources. \n Select which resource you would like in return.");
        if(activePlayer.getResourceCount(0) != 0 )
        tradeDeal.setOfferedResource(0);
    });
    oBrickBtn.setToggleGroup(offeredGroup);
    
    RadioButton oLumberBtn = new RadioButton("Lumber");
    oLumberBtn.setOnAction(e -> {
        int rate = exchangeRates[1];
        int max = activePlayer.getResourceCount(1)/rate*rate;
        offeredSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, max, 0, rate));
        requestText.setText("For every " + rate + " lumber, you wil receive 1 of the following resources. \n Select which resource you would like in return.");
        if(activePlayer.getResourceCount(1) != 0)
        tradeDeal.setOfferedResource(1);
    });
    oLumberBtn.setToggleGroup(offeredGroup);
   
    RadioButton oOreBtn = new RadioButton("Ore");
    oOreBtn.setOnAction(e -> {
        int rate = exchangeRates[2];
        int max = activePlayer.getResourceCount(2)/rate*rate;
        offeredSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, max, 0, rate));
        requestText.setText("For every " + rate + " ore, you will receive 1 of the following resources. \n Select which resource you would like in return.");
        if(activePlayer.getResourceCount(2) != 0)
        tradeDeal.setOfferedResource(2);
    });
    oOreBtn.setToggleGroup(offeredGroup);
    
    RadioButton oWheatBtn = new RadioButton("Wheat");
    oWheatBtn.setOnAction(e -> {
        int rate = exchangeRates[3];
        int max = activePlayer.getResourceCount(3)/rate*rate;
        offeredSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, max, 0, rate));
        requestText.setText("For every " + rate + " wheat, you will receive 1 of the following resources. \n Select which resource you would like in return.");
        if(activePlayer.getResourceCount(3) != 0)
        tradeDeal.setOfferedResource(3);
    });
    oWheatBtn.setToggleGroup(offeredGroup);
    
    RadioButton oWoolBtn = new RadioButton("Wool");
    oWoolBtn.setOnAction(e -> {
        int rate = exchangeRates[4];
        int max = activePlayer.getResourceCount(4)/rate*rate;
        offeredSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, max, 0, rate));
        requestText.setText("For every " + rate + " wool, you can receive 1 of the following resources. \n Select which resource you would like in return.");
        if(activePlayer.getResourceCount(4) != 0)
        tradeDeal.setOfferedResource(4);
    });
    oWoolBtn.setToggleGroup(offeredGroup);
  
    offeredBox.getChildren().addAll(oBrickBtn, oLumberBtn, oOreBtn, oWheatBtn, oWoolBtn,  offeredSpinner);
    

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
                if(tradeDeal.getOfferedResource()!= -2 && tradeDeal.getRequestedResource() != -2 && tradeDeal.getOfferedAmount() != 0){
                    tradeDeal.setRequestedAmount(tradeDeal.getOfferedAmount()/exchangeRates[tradeDeal.getRequestedResource()]);

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
    private void playerTradeMenu(){

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
        offeredSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,activePlayer.getResourceCount(0)));
        tradeDeal.setOfferedResource(0);
    });
    oBrickBtn.setToggleGroup(offeredGroup);
    
    RadioButton oLumberBtn = new RadioButton("Lumber");
    oLumberBtn.setOnAction(e -> {
        offeredSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,activePlayer.getResourceCount(1)));
        tradeDeal.setOfferedResource(1);
    });
    oLumberBtn.setToggleGroup(offeredGroup);
   
    RadioButton oOreBtn = new RadioButton("Ore");
    oOreBtn.setOnAction(e -> {
        offeredSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,activePlayer.getResourceCount(2)));
        tradeDeal.setOfferedResource(2);
    });
    oOreBtn.setToggleGroup(offeredGroup);
    
    RadioButton oWheatBtn = new RadioButton("Wheat");
    oWheatBtn.setOnAction(e -> {
        offeredSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,activePlayer.getResourceCount(3)));
        tradeDeal.setOfferedResource(3);
    });
    oWheatBtn.setToggleGroup(offeredGroup);
    
    RadioButton oWoolBtn = new RadioButton("Wool");
    oWoolBtn.setOnAction(e -> {
        offeredSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,activePlayer.getResourceCount(4)));
        tradeDeal.setOfferedResource(4);
    });
    oWoolBtn.setToggleGroup(offeredGroup);
    
    RadioButton oUnknownBtn = new RadioButton("Unknown");
    oUnknownBtn.setOnAction(e -> {
        offeredSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,activePlayer.maxResourceCount()));
        tradeDeal.setOfferedResource(-1);
    });
    oUnknownBtn.setToggleGroup(offeredGroup);
  

    offeredBox.getChildren().addAll(oBrickBtn, oLumberBtn, oOreBtn, oWheatBtn, oWoolBtn, oUnknownBtn, offeredSpinner);
    

    //these are the buttons in the requested resource group
    RadioButton rBrickBtn = new RadioButton("Brick");
    rBrickBtn.setOnAction(e -> {
        requestedSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,10));
        tradeDeal.setRequestedResource(0);
    });
    rBrickBtn.setToggleGroup(requestedGroup);
    
    RadioButton rLumberBtn = new RadioButton("Lumber");
    rLumberBtn.setOnAction(e -> {
        requestedSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,10));
        tradeDeal.setRequestedResource(1);
    });
    rLumberBtn.setToggleGroup(requestedGroup);
   
    RadioButton rOreBtn = new RadioButton("Ore");
    rOreBtn.setOnAction(e -> {
        requestedSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,10));
        tradeDeal.setRequestedResource(2);
    });
    rOreBtn.setToggleGroup(requestedGroup);
    
    RadioButton rWheatBtn = new RadioButton("Wheat");
    rWheatBtn.setOnAction(e -> {
        requestedSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,10));
        tradeDeal.setRequestedResource(3);
    });
    rWheatBtn.setToggleGroup(requestedGroup);
    
    RadioButton rWoolBtn = new RadioButton("Wool");
    rWoolBtn.setOnAction(e -> {
        requestedSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,10));
        tradeDeal.setRequestedResource(4);
    });
    rWoolBtn.setToggleGroup(requestedGroup);

    RadioButton rUnknownBtn = new RadioButton("Unknown");

    rUnknownBtn.setOnAction(e -> {
        requestedSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,10));
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
    
    if(GameManager.activePlayerID != 0)
        tradingPartnersBox.getChildren().add(player1Btn);
    
    if(GameManager.activePlayerID != 1)
        tradingPartnersBox.getChildren().add(player2Btn);
    
    
    if(GameManager.activePlayerID != 2)
        tradingPartnersBox.getChildren().add(player3Btn);
    
    if(GameManager.activePlayerID != 3)
        tradingPartnersBox.getChildren().add(player4Btn);
    
    tradingPartnersBox.getChildren().add(playerUnknownBtn);
    
    

    //ungrouped buttons
    Button submitBtn = new Button("Request Trade");
        submitBtn.setOnAction(e -> {
                if(tradeDeal.getOfferedResource()!= -2 && tradeDeal.getRequestedResource() != -2 && tradeDeal.getTradingPartner() != -2){
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

    private void restoreUIElements(ArrayList<Circle> intersections, ArrayList<Line> boundaries) {
        for (Circle i : intersections) {
            // For every circle, restore to strokeWidth 1
            if (i.getStroke() == BLACK) {
                // only if it is still BLACK ie unowned
                i.setStrokeWidth(1);
            }
            // Set to do nothing on click
            i.setOnMouseClicked(e -> doNothing());

        }
        for (Line l : boundaries) {
            // Set all lines non-clickable
            l.setOnMouseClicked(e -> doNothing());
        }
    }

    private void doNothing() {
        // does nothing
    }

    private void setUpPhase() {
        int playerID = GameManager.activePlayerID;
        boolean setupPhase = GameManager.isSetUpPhase;
        Intersection[] intersections = GameManager.intersections;

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
}
