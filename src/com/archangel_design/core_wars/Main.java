package com.archangel_design.core_wars;

import com.archangel_design.core_wars.model.MainWindowModel;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    MainWindowModel mainWindowModel = new MainWindowModel();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(mainWindowModel.getScene());
        primaryStage.setTitle("Core Wars 4");
        primaryStage.show();
    }
}
