/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package othello_5;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Ethan
 */
public class Winner extends Application {
    
    private boolean repeat = false;
    
    /**
     *
     * @param primaryStage
     * @param winMsg
     */
    public void start(Stage primaryStage, String winMsg) {
       
        StackPane root = new StackPane();
        
        
        Scene scene = new Scene(root, 400, 200);
        scene.getStylesheets().add("/resources/winStyle.css");
        
        Image party = new Image("resources/balloons.jpg");
        
        Color temp = new Color (1, 1, 1, .1);
        Rectangle rect = new Rectangle(0, 0, 400, 200);
        rect.setFill(temp);
        root.setBackground(new Background(new BackgroundImage(party, BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
        root.getChildren().add(rect);
        
        Label lblMsg = new Label(winMsg);
        lblMsg.getStyleClass().add("winnerMsg");
        lblMsg.setWrapText(true);
        lblMsg.setTextAlignment(TextAlignment.CENTER);
        Button [] btActions = new Button[2];
        btActions[0]=new Button("Play Again");
        btActions[1]=new Button("Quit");
        setupButtons(btActions,primaryStage);
        HBox hbButtonHolder = new HBox(10);
        hbButtonHolder.getChildren().addAll(btActions);
        hbButtonHolder.setAlignment(Pos.CENTER);
        
        VBox action = new VBox(15);
        action.setAlignment(Pos.CENTER);
        action.getChildren().add(lblMsg);
        action.getChildren().add(hbButtonHolder);
        
        root.getChildren().add(action);
        
        primaryStage.setTitle("Winner");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.initModality(Modality.APPLICATION_MODAL);

        primaryStage.show();
    }

    /**
     * This method just launches another window for user options
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void setupButtons(Button[] options, Stage windowToClose){
        options[0].setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent me){
//                ((Stage)((Scene)((Object)((StackPane)((VBox)((HBox)((Button)(me.getSource())).getParent()).getParent()).getParent()).getParent())).getWindow()).close();
                windowToClose.close();
            }
        });
        options[1].setOnAction(e->Platform.exit());
    }
    
  
}
