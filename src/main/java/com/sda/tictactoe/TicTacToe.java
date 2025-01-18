package com.sda.tictactoe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;

public class TicTacToe extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TicTacToe.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 850, 600);
        stage.setTitle("TicTacToe");
        stage.setScene(scene);
        stage.show();

        TicTacToeController controller = fxmlLoader.getController();

        Image bg_Image = new Image(getClass().getResourceAsStream("/img/game_Bg.jpg"));

        BackgroundImage bgImage = new BackgroundImage(
                bg_Image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT
        );

        controller.mainAnchorPane.setBackground(new Background(bgImage));
        controller.initList();
    }

    public static void main(String[] args) {
        launch();
    }


}