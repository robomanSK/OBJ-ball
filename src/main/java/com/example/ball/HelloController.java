package com.example.ball;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public class HelloController {

    @FXML
    private Pane plocha;

    @FXML
    public void initialize() {
        Ball ball = new Ball(plocha);
        plocha.getChildren().add(ball);
    }
}