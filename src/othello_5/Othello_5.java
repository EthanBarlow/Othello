/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package othello_5;


import java.util.Optional;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Othello is a board game in which two players have different color chips and the goal is to end the game with the highest number of chips on the board.
 * If a black chip flanks a white chip meaning that the placed chip encloses a white chip between another black chip,
 * that white chip becomes a black chip. The game ends if all the chips on the board are one color or if the board is completely full.
 * The game can also reach a state where one player has no valid moves available, so that player's turn is skipped.
 * 
 * The program is broken into 3 java classes and 3 css stylesheets.
 * The main method is Othello_5 which is the GUI for the entire game.
 * BoardModel is the java class that handles all the game logic in the back end
 * Winner is the java class that creates a winner dialog box and allows the user to choose to play again or quit
 * 
 * Othello_5 uses the Alert class to create popup boxes when a move is invalid or a turn is about to be skipped
 * 
 * Othello_5 also contains a runOptionsMenu method that allows the user to choose what color chips to use.
 * 
 * The project is named Othello_5 because it is the 5th version in the line of Othello products
 * 
 * @author Ethan
 */
public class Othello_5 extends Application {

    /**
     * firstStyle and secondStyle are strings representing that css class to use with each "chip"
     * SIDE_LENGTH is a final integer simply representing the number of squares on one side of the board
     * playerPrompts is an array of strings that holds prompts for both player 1 and player 2
     * rect[] is an array of Rectangles that holds the Rectangles where the score count is displayed
     * winningMsg[] is an array of strings that is used to display the winning message in the Winner class
     */
    private String firstStyle;
    private String secondStyle;
    private final int SIDE_LENGTH = 8;
    private final String [] playerPrompts = {"Player 1's turn", "Player 2's turn"};
    private final Rectangle rect[];
    private final String [] winningMsg = {"Congratulations Player 1", "Congratulations Player 2", "A Tie..."};
    
     final private int numBtns = 4;
    
    /**
     *This is the default constructor of Othello_5
     */
    public Othello_5() {
        this.rect = new Rectangle[2];
        this.rect[0] = new Rectangle(40,40);
        this.rect[0].setArcHeight(10);
        this.rect[0].setArcWidth(10);
        this.rect[0].setFill(Paint.valueOf("#000000"));
        //this.rect[0].getStyleClass().add("countBlack");
        this.rect[1] = new Rectangle(40,40);
        this.rect[1].setArcHeight(10);
        this.rect[1].setArcWidth(10);
        this.rect[1].setFill(Paint.valueOf("#ffffff"));
        //this.rect[1].getStyleClass().add("countWhite");
    }
    
    
    @Override
    public void start(Stage primaryStage) {
        
        BoardModel game = new BoardModel();
        
        Button [][] btns = new Button[SIDE_LENGTH][SIDE_LENGTH];
        Button btnMenu = new Button();
        Button btRestart = new Button("Restart");
        btRestart.getStyleClass().add("button-options");
        Button btSkip = new Button("Skip");
        btSkip.getStyleClass().add("button-options");
        btnMenu.getStyleClass().add("button-options");
        
        
        ImageView gear = new ImageView("/resources/wheel.png");
        gear.setFitHeight(25);
        gear.setPreserveRatio(true);
        
        btnMenu.setGraphic(gear);
        
        BorderPane bpaneBottomBar = new BorderPane();
        HBox leftCorner=new HBox(10);
        leftCorner.getChildren().add(btnMenu);
        leftCorner.getChildren().add(btRestart);
        leftCorner.getChildren().add(btSkip);
     //   bpaneBottomBar.setPadding(new Insets(15,15,15,15));
        bpaneBottomBar.setLeft(leftCorner);
        
        
        Label lblPrompt = new Label(playerPrompts[0]);
        lblPrompt.setId("playerPrompt");
        bpaneBottomBar.setCenter(lblPrompt);
              
        setFirstStyle("space-black");
        setSecondStyle("space-white");
               
        Label lblP1 = new Label();
        lblP1.getStyleClass().add("countBlack");
        Label lblP2 = new Label();
        lblP2.getStyleClass().add("countWhite");
        
        btnMenu.setOnAction(e->{
            runOptionsMenu(btnMenu, btns, game, lblP1, lblP2);
            btnMenu.setDisable(false);
        });
        
        
        HBox scoreBox = new HBox(20);
        StackPane chipAndTextP1 = new StackPane();
        chipAndTextP1.getChildren().addAll(rect[0],lblP1);
        StackPane chipAndTextP2 = new StackPane();
        chipAndTextP2.getChildren().addAll(rect[1],lblP2);
        
        scoreBox.getChildren().addAll(chipAndTextP1,chipAndTextP2);
        
        bpaneBottomBar.setRight(scoreBox);
        
        BorderPane base = new BorderPane();
        base.getStyleClass().add("board-background");
        base.setPadding(new Insets(15,15,15,15));
        base.setBottom(bpaneBottomBar);
//        base.getBottom().minWidth(500);
//        base.getBottom().setLayoutX(base.getLayoutX()/2);
//        btnMenu.setAlignment(Pos.BASELINE_CENTER);
        
        
        
        
        GridPane root = new GridPane();
        root.setVgap(10);
        root.setHgap(10);
        
        base.setCenter(root);
        
        setupGame(btns, game, root, lblPrompt, lblP1, lblP2);
        btRestart.setOnAction(e->{
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("You are about to restart the game...");
            alert.setContentText("Are you sure that you would like to continue?");
            
            Optional<ButtonType>result=alert.showAndWait();
            if(result.get()==ButtonType.OK){
                restart(btns, game, lblP1, lblP2, lblPrompt);
            }
        });
        btSkip.setOnAction(e->{
           Alert alert = new Alert(AlertType.CONFIRMATION);
           alert.setTitle("Warning!");
           alert.setHeaderText("You are about to skip your turn!");
           alert.setContentText("This action should never be performed if a valid move is possible! Only use this method if you have no valid moves whatsoever, but you still have chips on the board.");
        
           Optional<ButtonType>result=alert.showAndWait();
           if(result.get()==ButtonType.OK){
               game.skipCurrentTurn();
               switchPrompt(lblPrompt);
           }
        
        });

                       
        Scene scene = new Scene(base, 500, 550);
        scene.getStylesheets().add("/resources/defaultStyle.css");
        //winScreen(winningMsg[0],btns,game,lblP1,lblP2,lblPrompt); --- for testing purposes only
        primaryStage.setTitle("Othello");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    /**
     *
     * @param grid is the local variable name for the array of Buttons that has been passed in. The Button array is passed in so that the styling of each button can be updated based on the location of player chips.
     * @param game is the local variable name for the BoardModel object that is passed in. This is the backend holding the underlying logic and the array of actual data representing the othello board.
     * @param lblPointsP1 is the local variable name for the label that displays the current score for player 1, and this is updated every time changeColor is called.
     * @param lblPointsP2 is the local variable name for the label that displays the current score for player 2, and this is updated every time changeColor is called.
     * @param prompt is the local variable name for the label that tells the players who's turn it is. This should be switched every time the changeColor method is called
     */
    public void changeColor(Button[][] grid, BoardModel game, Label lblPointsP1, Label lblPointsP2, Label prompt){
        int whiteChips = 0;
        int blackChips = 0;
        for(int i = 0; i<SIDE_LENGTH;i++){
            for(int j=0;j<SIDE_LENGTH;j++){
                switch (game.getSpace(i,j)) {
                    case BLACK:
                        grid[i][j].getStyleClass().clear();
                        grid[i][j].getStyleClass().add(getFirstStyle()); 
                        blackChips++;
                        break;
                    case WHITE:
                        grid[i][j].getStyleClass().clear();
                        grid[i][j].getStyleClass().add(getSecondStyle());
                        whiteChips++;
                        break;
                    case EMPTY:
                        grid[i][j].getStyleClass().clear();
                        grid[i][j].getStyleClass().add("button");
                        break;
                }

            }
        }
        lblPointsP1.setText(Integer.toString(blackChips));
//        rect[0].getStyleClass().clear();
//        rect[0].getStyleClass().add(getFirstStyle());

        lblPointsP2.setText(Integer.toString(whiteChips));
//        rect[1].getStyleClass().clear();
//        rect[1].getStyleClass().add(getSecondStyle());


//winning conditions

        if(blackChips==0){
            winScreen(winningMsg[1],grid,game,lblPointsP1,lblPointsP2,prompt);
        }
        
        else if(whiteChips==0){
            winScreen(winningMsg[0],grid,game,lblPointsP1,lblPointsP2,prompt);
        }

        if(whiteChips+blackChips==SIDE_LENGTH*SIDE_LENGTH){
            if(blackChips>whiteChips){
                winScreen(winningMsg[0],grid,game,lblPointsP1,lblPointsP2,prompt);
            }
            else if(blackChips<whiteChips){
                winScreen(winningMsg[1],grid,game,lblPointsP1,lblPointsP2,prompt);
            }
            
            else{
                winScreen(winningMsg[2],grid,game,lblPointsP1,lblPointsP2,prompt);
            }
        }
        
    }
    
    /**
     *
     * @param btns is the Button array for the GUI and will be setup in this function
     * @param game is the name of the backend model (BoardModel) that is used in setup here
     * @param root is the local variable name for the GridPane that will contain all the buttons
     * @param lblMsg is the local name for the Label that prompts the user to make his/her move
     * @param lblPointsP1 is the local variable name for the label that displays the current score for player 1, and this is updated every time changeColor is called.
     * @param lblPointsP2 is the local variable name for the label that displays the current score for player 2, and this is updated every time changeColor is called.
     */
    public void setupGame(Button[][]btns, BoardModel game, GridPane root, Label lblMsg, Label lblPointsP1, Label lblPointsP2){   

        for(int x = 0;x<SIDE_LENGTH;x++){
            for(int y = 0;y<SIDE_LENGTH;y++){
                    Button temp = new Button(" ");
                    temp.setMinSize(50,50);
                    
                    final int row = x;
                    final int col = y;

                    //temp.setText(Integer.toString(row)+" "+Integer.toString(col)); --- for testing
                    //System.out.println("Buttons" + x + " " + y);
                    temp.setOnAction(e->{
                            if(!game.makeMove(row, col)){
                                Alert alert = new Alert(AlertType.WARNING);
                                alert.setTitle("Invalid Dialog");
                                alert.setHeaderText("That was an invalid move");
                                alert.setContentText("Your chip must flank your opponent's chips and cause them to change color for the move to be valid. ");
                                alert.initModality(Modality.APPLICATION_MODAL);
                                alert.showAndWait();
//                                System.out.println("That is not a valid move");
                            }
                            
                            else{
                                switchPrompt(lblMsg);
                            }
                            
                    });

                    btns[x][y]=temp;
                    root.add(btns[x][y],x,y);
                   
            }
        }   

//        changes the color after setting up the gui
        changeColor(btns,game, lblPointsP1, lblPointsP2,lblMsg);
        
        for(int x=0;x<SIDE_LENGTH;x++){
            for(int y=0;y<SIDE_LENGTH;y++){
                btns[x][y].setOnMouseReleased(e->changeColor(btns,game, lblPointsP1, lblPointsP2,lblMsg));
            }
        }

    }//end setupGame function
    
    /**
     *
     * @return the String that represents the CSS styling tag to be applied to the first player's chips
     */
    public String getFirstStyle() {
        return firstStyle;
    }

    /**
     *
     * @param firstStyle is the String representation of the CSS style tag that will be set for player 1's chips
     */
    void setFirstStyle(String firstStyle) {
        this.firstStyle = firstStyle;
    }

    /**
     *
     * @return the String that represents the CSS styling tag to be applied to the second player's chips
     */
    public String getSecondStyle() {
        return secondStyle;
    }

    /**
     *
     * @param secondStyle is the String representation of the CSS style tag that will be set for player 2's chips
     */
    public void setSecondStyle(String secondStyle) {
        this.secondStyle = secondStyle;
    }
    
    /**
     * This method uses a ternary operator to set the player prompt
     * @param lblMsg is the local variable name for the Label displaying the prompt for who's turn it is
     */
    public void switchPrompt(Label lblMsg){
        lblMsg.setText(lblMsg.getText().equals(playerPrompts[0]) ? playerPrompts[1] : playerPrompts[0]);
    }
    
    /**
     *
     * @param winMsg is a String congratulating the appropriate winner that will be displayed on the win screen
     * @param grid is the Button array, and it is not used in this function except to pass it into the restart method.
     * @param game is the BoardModel object that is not used in this method except to pass it into the restart method.
     * @param lblPointsP1 simply passed to the restart method
     * @param lblPointsP2 simply passed to the restart method
     * @param prompt simply passed to the restart method
     */
    public void winScreen(String winMsg,Button[][] grid, BoardModel game, Label lblPointsP1, Label lblPointsP2, Label prompt){
        Stage stage = new Stage();
        stage.setAlwaysOnTop(true);
        Winner newGame = new Winner();
        newGame.start(stage, winMsg);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
          public void handle(WindowEvent we) {
             
                  restart(grid,game,lblPointsP1,lblPointsP2, prompt);
              
          }
      });
        
        
    }
    
    /**
     * @param grid is the Button array and is passed into the changeColor method after the backend has been reset
     * @param game is the BoardModel object, and the BoardModel.reset() method is called therefore reseting the entire backend and preparing for the next game.
     * @param lblPointsP1 is passed into the changeColor method after the backend has been reset
     * @param lblPointsP2 is passed into the changeColor method after the backend has been reset
     * @param prompt is reset so that a new game can be started
     */
    public void restart(Button[][] grid, BoardModel game, Label lblPointsP1, Label lblPointsP2,Label prompt){
        game.reset();
        changeColor(grid,game,lblPointsP1,lblPointsP2,prompt);
        prompt.setText(playerPrompts[0]);
    }
    
    /**
     * This method is used to update the colors on the board after the user chooses a new color from the options menu. 
     * The method works similar to the changeColor method, but it does not handle any of the game logic or change the prompt for the current player's turn.
     * @param grid is the Button array and each button's styling will be changed accordingly (based on the selections made by the user on the options screen). 
     * @param game is the BoardModel object that is passed in so that the status of each piece on the board can be checked
     */
    public void userChangeColor(Button[][] grid, BoardModel game){
        
        for(int i = 0; i<SIDE_LENGTH;i++){
            for(int j=0;j<SIDE_LENGTH;j++){
                switch (game.getSpace(i,j)) {
                    case BLACK:
                        grid[i][j].getStyleClass().clear();
                        grid[i][j].getStyleClass().add(getFirstStyle()); 
                        break;
                    case WHITE:
                        grid[i][j].getStyleClass().clear();
                        grid[i][j].getStyleClass().add(getSecondStyle());
                        break;
                    case EMPTY:
                        grid[i][j].getStyleClass().clear();
                        grid[i][j].getStyleClass().add("button");
                        break;
                }

            }
        }
      
    }

    
    //launches the second window and will handle styling updates
    //does not currently change the css styling

    /**
     *
     * @param btn is the local variable name for the button that is used to bring up the menu options. It is passed in and then disabled until the window is closed so that the user cannot open an infinite number of menu windows.
     * @param grid is the Button array and it is passed in to this function so that it can be handed off to the userChangeColor method
     * @param game is the BoardModel object that is passed in and handed off to the userChangeColor method
     * @param lbl1 is the Label that displays the score of player 1, and it is handed off to setupCounts for styling
     * @param lbl2 is the Label that displays the score of player 2, and it is handed off to setupCounts for styling
     */
    public void runOptionsMenu(Button btn, Button[][] grid, BoardModel game, Label lbl1, Label lbl2){
        
        
        Stage stage = new Stage();
        btn.setDisable(true);
        stage.setAlwaysOnTop(true);
        
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
          public void handle(WindowEvent we) {
            btn.setDisable(false);
          }
      });
        
        RadioButton [] btnArr1 = new RadioButton[numBtns];     
        RadioButton [] btnArr2 = new RadioButton[numBtns];
        
        Button btnSubmit = new Button("Submit");
        
        setupButtons(btnArr1);
        setupButtons(btnArr2);
        
        ToggleGroup player1 = new ToggleGroup();
        ToggleGroup player2 = new ToggleGroup();
        ToggleGroup tgMaster = new ToggleGroup();
                
        setupToggles(player1,btnArr1);
        setupToggles(player2,btnArr2);
        
        //setup initial choices
        player1.selectToggle(btnArr1[0]);
        player2.selectToggle(btnArr2[1]);
        
        //setup ToggleGroup listeners to make sure that the same choices are not selected
        player1.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            @Override
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle){
                if(new_toggle.getToggleGroup().getSelectedToggle().toString().equals(player2.getSelectedToggle().toString())){
                    btnSubmit.setDisable(true);
                }
                else{
                    btnSubmit.setDisable(false);
                }
            }
        });
        
        player2.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            @Override
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle){
//                System.out.println(new_toggle.getToggleGroup().getSelectedToggle().toString());
                if(new_toggle.getToggleGroup().getSelectedToggle().toString().equals(player1.getSelectedToggle().toString())){
                    btnSubmit.setDisable(true);
                }
                else{
                    btnSubmit.setDisable(false);
                }
            }
        });
        
        //btnSubmit action will save the selected buttons to the strings that are passed in to the start function
        btnSubmit.setOnAction(e->{
            setFirstStyle(((RadioButton)player1.getSelectedToggle()).getId());
            setupCounts(0, lbl1);
            //System.out.println(((RadioButton)player1.getSelectedToggle()).getId());
            setSecondStyle(((RadioButton)player2.getSelectedToggle()).getId());
            setupCounts(1, lbl2);
            //System.out.println(((RadioButton)player2.getSelectedToggle()).getId());
            userChangeColor(grid,game);
            stage.close();
        });

        
        //rest of gui
        
        VBox left = new VBox(5);
        VBox right = new VBox(5);
        
        left.getChildren().add(new Label("Player 1's color:"));
        right.getChildren().add(new Label("Player 2's color:"));
        
        setupPlayers(left,btnArr1);
        setupPlayers(right,btnArr2);
        
        HBox menuOps = new HBox(10);
        menuOps.getChildren().add(left);
        menuOps.getChildren().add(right);
        
        BorderPane root = new BorderPane();
        Label header = new Label("Options");
        header.getStyleClass().add("heading");
//        header.setMinSize(400, 50);
        
        root.setTop(header);
        root.setCenter(menuOps);
        root.setBottom(btnSubmit);
        root.setPadding(new Insets(15,15,15,15));
        
        Scene scene = new Scene(root, 250, 200);
        
        scene.getStylesheets().add("/resources/menuStyleSheet.css");
        
        stage.setTitle("Menu Options");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
         
    }
    
    /**
     * @param btnSet is the array of RadioButton(s), and the individual elements have their text and id set. The Id corresponds to a CSS styling tag
     */
    public void setupButtons(RadioButton [] btnSet){
        
        for(int x = 0;x<numBtns;x++){
            btnSet[x]=new RadioButton();
            switch (x) {
                    case 0:
                        btnSet[x].setId("space-black");
                        btnSet[x].setText("Black");
                        break;
                    case 1:
                        btnSet[x].setId("space-white");
                        btnSet[x].setText("White");
                        break;
                    case 2:
                        btnSet[x].setId("space-blue");
                        btnSet[x].setText("Blue");
                        break;
                    case 3:
                        btnSet[x].setId("space-red");
                        btnSet[x].setText("Red");
                        break;
            }
        }//end for loop
        
    }//end setupButtons function
    
    /**
     * this will change the color of the rectangle holding the score
     * @param i will be the integer used to determine which rectangle from the array is being accessed
     * @param lblCount is the local variable name for the label that displays a player's current score. This is passed in so that the styling can be updated
     */
    public void setupCounts(int i, Label lblCount){
        lblCount.getStyleClass().clear();
            switch (i==0 ? getFirstStyle() : getSecondStyle()) {
                    case "space-black":
                        this.rect[i].setFill(Paint.valueOf("#000000"));
                        lblCount.getStyleClass().add("countBlack");
                        break;
                    case "space-white":
                        this.rect[i].setFill(Paint.valueOf("#ffffff"));
                        lblCount.getStyleClass().add("countWhite");
                        break;
                    case "space-blue":
                        this.rect[i].setFill(Paint.valueOf("#0000ff"));
                        lblCount.getStyleClass().add("countBlue");
                        break;
                    case "space-red":
                        this.rect[i].setFill(Paint.valueOf("#ff0000"));
                        lblCount.getStyleClass().add("countRed");
                        break;
            }//end switch
        
        
    }//end setupCounts function
    
    /**
     *
     * @param tgSet is the local variable name for a ToggleGroup that will display the coloring options
     * @param btnSet is the local variable name for the actual RadioButton array, and each of the RadioButtons in this array will be added to the ToggleGroup tgSet
     */
    public void setupToggles(ToggleGroup tgSet, RadioButton[] btnSet){
        for(int x=0;x<numBtns;x++){
            tgSet.getToggles().add(btnSet[x]);
        }
    }
    
    /**
     * This method is used to add all of the RadioButton(s) in the RadioButton array, btnSet, to the VBox, box, that will contain them in the GUI
     * @param box is a VBox that will be placed in the menu options GUI
     * @param btnSet is the RadioButton array that holds all the Button(s) for color options
     */
    public void setupPlayers(VBox box, RadioButton[] btnSet){
        for(int x=0;x<numBtns;x++){
             box.getChildren().add(btnSet[x]);
        }
    }
       
    
}
