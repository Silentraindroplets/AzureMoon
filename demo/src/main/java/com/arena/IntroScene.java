package com.arena;

import com.arena.ui.Sprite;
import com.arena.ui.Sprites;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

/** Full-screen visual-novel intro that plays before each battle. */
public class IntroScene extends StackPane {

    private static class Line {
        String speaker, text, spriteKey, colorHex;
        Line(String speaker, String text, String spriteKey, String colorHex) {
            this.speaker = speaker; this.text = text;
            this.spriteKey = spriteKey; this.colorHex = colorHex;
        }
    }

    private final List<Line> script = new ArrayList<>();
    private int lineIndex = 0;

    private final StackPane spriteArea;
    private final Label speakerLabel;
    private final Label textLabel;
    private final Sprite currentSprite;

    public IntroScene(Game game, int numPlayers) {
        setStyle("-fx-background-color: #0a0c13;");

        // Background gradient
        Rectangle bg = new Rectangle();
        bg.widthProperty().bind(widthProperty());
        bg.heightProperty().bind(heightProperty());
        bg.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#0a0c13")),
                new Stop(0.5, Color.web("#1a1030")),
                new Stop(1, Color.web("#0a0c13"))));

        // Soft spotlight on the portrait area
        Rectangle spotlight = new Rectangle(600, 520);
        spotlight.setFill(new javafx.scene.paint.RadialGradient(0, 0, 0.5, 0.5, 0.5, true,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#7c5cff", 0.20)),
                new Stop(1, Color.TRANSPARENT)));
        spotlight.setMouseTransparent(true);

        // Portrait
        currentSprite = new Sprite(Sprites.get("knight"), Sprites.palette(), 12);
        currentSprite.setEffect(new DropShadow(40, Color.web("#7c5cff", 0.85)));
        spriteArea = new StackPane(currentSprite);
        spriteArea.setAlignment(Pos.CENTER);
        spriteArea.setPrefHeight(560);

        VBox topContent = new VBox(20, spotlight, spriteArea);
        topContent.setAlignment(Pos.CENTER);

        // Text box
        speakerLabel = new Label();
        speakerLabel.setStyle("-fx-text-fill: #facc15; -fx-font-size: 19px; -fx-font-weight: bold; " +
                "-fx-font-family: 'Georgia';");

        textLabel = new Label();
        textLabel.setWrapText(true);
        textLabel.setStyle("-fx-text-fill: #e2e8f0; -fx-font-size: 18px; -fx-font-family: 'Georgia'; " +
                "-fx-line-spacing: 4;");
        textLabel.setMaxWidth(940);
        textLabel.setMinHeight(60);

        Label continueHdr = new Label("▼  Click or press SPACE to continue");
        continueHdr.setStyle("-fx-text-fill: #64748b; -fx-font-size: 11px; -fx-font-style: italic;");

        VBox textInner = new VBox(10, speakerLabel, textLabel, continueHdr);
        textInner.setAlignment(Pos.TOP_LEFT);
        textInner.setPadding(new Insets(22, 32, 20, 32));
        textInner.setMaxWidth(1000);
        textInner.setStyle("-fx-background-color: rgba(10,12,20,0.94); " +
                "-fx-border-color: #7c5cff; -fx-border-width: 2; " +
                "-fx-border-radius: 14; -fx-background-radius: 14; " +
                "-fx-effect: dropshadow(gaussian, #000, 30, 0.5, 0, -6);");

        VBox textHolder = new VBox(textInner);
        textHolder.setAlignment(Pos.CENTER);
        textHolder.setPadding(new Insets(0, 0, 40, 0));

        StackPane lowerLayer = new StackPane(topContent, textHolder);
        lowerLayer.setAlignment(Pos.CENTER);

        // Vignette
        Rectangle vignette = new Rectangle();
        vignette.widthProperty().bind(widthProperty());
        vignette.heightProperty().bind(heightProperty());
        vignette.setFill(new javafx.scene.paint.RadialGradient(0, 0, 0.5, 0.5, 0.9, true,
                CycleMethod.NO_CYCLE,
                new Stop(0.55, Color.TRANSPARENT),
                new Stop(1, Color.web("#000000", 0.9))));
        vignette.setMouseTransparent(true);

        getChildren().addAll(bg, lowerLayer, vignette);

        // Build the dialogue script based on mode
        if (numPlayers == 1) {
            script.add(new Line("Narrator", "The gates of the Arena of Echoes groan open for the first time in a century.", "knight", "#94a3b8"));
            script.add(new Line("Narrator", "You step forward alone. The crowd's roar fades into static.", "knight", "#94a3b8"));
            script.add(new Line("Knight", "Just me and the dark, then.", "knight", "#7c5cff"));
            script.add(new Line("Knight", "I've trained for this. Let the Echoes sing.", "knight", "#7c5cff"));
            script.add(new Line("???", "Foolish mortal. The Demon Lord waits at the end of this arena.", "demon", "#dc2626"));
            script.add(new Line("Knight", "Then I'll carve my way to him.", "knight", "#7c5cff"));
        } else {
            script.add(new Line("Narrator", "Two figures approach the Arena of Echoes side by side.", "knight", "#94a3b8"));
            script.add(new Line("Knight", "Stay close, mage. The Echoes are treacherous this time of year.", "knight", "#7c5cff"));
            script.add(new Line("Mage", "I'll burn anything that gets within arm's reach.", "mage", "#38bdf8"));
            script.add(new Line("Knight", "Good. Then let's begin.", "knight", "#7c5cff"));
            script.add(new Line("???", "Ah... the little heroes come to die together. How quaint.", "demon", "#dc2626"));
            script.add(new Line("Mage", "We'll see who dies today, demon.", "mage", "#38bdf8"));
        }

        setFocusTraversable(true);
        setOnMouseClicked(e -> advance(game, numPlayers));
        setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SPACE || e.getCode() == KeyCode.ENTER
                    || e.getCode() == KeyCode.ESCAPE) {
                advance(game, numPlayers);
            }
        });

        showLine();
        javafx.application.Platform.runLater(this::requestFocus);
    }

    private void showLine() {
        if (lineIndex >= script.size()) return;
        Line line = script.get(lineIndex);

        speakerLabel.setText(line.speaker);
        speakerLabel.setStyle("-fx-text-fill: " + line.colorHex + "; -fx-font-size: 19px; " +
                "-fx-font-weight: bold; -fx-font-family: 'Georgia';");
        textLabel.setText(line.text);

        // Fade out old sprite
        if (!spriteArea.getChildren().isEmpty() && spriteArea.getChildren().get(0) != currentSprite) {
            spriteArea.getChildren().clear();
        }
        spriteArea.getChildren().clear();

        Sprite sprite = new Sprite(Sprites.get(line.spriteKey), Sprites.palette(), 12);
        sprite.setEffect(new DropShadow(40, Color.web(line.colorHex, 0.85)));
        sprite.setOpacity(0);
        sprite.setTranslateY(20);
        spriteArea.getChildren().add(sprite);

        FadeTransition ft = new FadeTransition(Duration.millis(400), sprite);
        ft.setToValue(1);
        TranslateTransition tt = new TranslateTransition(Duration.millis(400), sprite);
        tt.setToY(0);
        ft.play();
        tt.play();

        // Fade text in
        textLabel.setOpacity(0);
        speakerLabel.setOpacity(0);
        FadeTransition ftText = new FadeTransition(Duration.millis(280), textLabel);
        ftText.setToValue(1);
        FadeTransition ftSpk = new FadeTransition(Duration.millis(280), speakerLabel);
        ftSpk.setToValue(1);
        ftText.play();
        ftSpk.play();
    }

    private void advance(Game game, int numPlayers) {
        lineIndex++;
        if (lineIndex >= script.size()) {
            game.startBattleWithCinematic(numPlayers);
        } else {
            showLine();
        }
    }
}