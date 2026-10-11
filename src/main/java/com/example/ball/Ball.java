package com.example.ball;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Ball extends Canvas {

    private final GraphicsContext gc;
    private final Timeline timeline;

    private static final double PRIEMER = 50;
    private static final double POLOMER = PRIEMER / 2;

    private double x = 100;
    private double y = 100;

    private double rychlostX = 3;
    private double rychlostY = 3;

    private boolean pohybSpusteny = true;

    public Ball(Pane plocha) {
        widthProperty().bind(plocha.widthProperty());
        heightProperty().bind(plocha.heightProperty());

        gc = getGraphicsContext2D();

        setFocusTraversable(true);

        widthProperty().addListener((obs, oldValue, newValue) -> {
            upravPoziciu();
            prekresli();
        });

        heightProperty().addListener((obs, oldValue, newValue) -> {
            upravPoziciu();
            prekresli();
        });

        timeline = new Timeline(
                new KeyFrame(
                        Duration.millis(20),
                        event -> dalsiKrok()
                )
        );

        timeline.setCycleCount(Timeline.INDEFINITE);

        setOnMouseClicked(event -> {
            double dx = event.getX() - x;
            double dy = event.getY() - y;

            double vzdialenost =
                    Math.sqrt(dx * dx + dy * dy);

            if (vzdialenost <= POLOMER) {
                if (pohybSpusteny) {
                    zastav();
                } else {
                    spusti();
                }

                requestFocus();
            }
        });

        setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.F) {
                zrychli();
            } else if (event.getCode() == KeyCode.S) {
                spomal();
            }
        });

        prekresli();

        timeline.play();
    }

    private void dalsiKrok() {
        x += rychlostX;
        y += rychlostY;

        // Odraz od ľavého a pravého okraja
        if (x - POLOMER <= 0) {
            x = POLOMER;
            rychlostX = Math.abs(rychlostX);
        } else if (x + POLOMER >= getWidth()) {
            x = getWidth() - POLOMER;
            rychlostX = -Math.abs(rychlostX);
        }

        // Odraz od horného a dolného okraja
        if (y - POLOMER <= 0) {
            y = POLOMER;
            rychlostY = Math.abs(rychlostY);
        } else if (y + POLOMER >= getHeight()) {
            y = getHeight() - POLOMER;
            rychlostY = -Math.abs(rychlostY);
        }

        prekresli();
    }

    private void prekresli() {
        gc.clearRect(0, 0, getWidth(), getHeight());

        gc.setFill(Color.BLACK);
        gc.fillOval(
                x - POLOMER,
                y - POLOMER,
                PRIEMER,
                PRIEMER
        );
    }

    private void upravPoziciu() {
        if (getWidth() <= 0 || getHeight() <= 0) {
            return;
        }

        if (x - POLOMER < 0) {
            x = POLOMER;
        }

        if (x + POLOMER > getWidth()) {
            x = getWidth() - POLOMER;
        }

        if (y - POLOMER < 0) {
            y = POLOMER;
        }

        if (y + POLOMER > getHeight()) {
            y = getHeight() - POLOMER;
        }
    }

    private void zrychli() {
        rychlostX *= 1.2;
        rychlostY *= 1.2;
    }

    private void spomal() {
        rychlostX *= 0.8;
        rychlostY *= 0.8;
    }

    private void spusti() {
        pohybSpusteny = true;
        timeline.play();
    }

    private void zastav() {
        pohybSpusteny = false;
        timeline.pause();
    }
}