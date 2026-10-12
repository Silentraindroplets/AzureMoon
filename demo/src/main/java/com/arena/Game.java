package com.arena;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Game {
    private final Stage stage;
    private final StackPane root;
    private final Settings settings = new Settings();

    public Game(Stage stage) {
        this.stage = stage;
        this.root = new StackPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #0a0c13, #141a2c);");
    }

    public Settings getSettings() { return settings; }

    public void start() {
        Scene scene = new Scene(root, 1280, 900);
        stage.setScene(scene);
        stage.setTitle("Arena of Echoes — A Coding RPG");
        stage.setResizable(true);
        stage.setMinWidth(1000);
        stage.setMinHeight(760);
        stage.show();
        showMenu();
    }

    public void showMenu() {
        root.getChildren().clear();
        root.getChildren().add(new MenuScreen(this));
    }

    /** Menu -> VN intro -> battle. */
    public void startBattleWithIntro(int numPlayers) {
        root.getChildren().clear();
        root.getChildren().add(new IntroScene(this, numPlayers));
    }

    /** Called by IntroScene once dialogue finishes: goes straight to battle + cinematic. */
    public void startBattleWithCinematic(int numPlayers) {
        root.getChildren().clear();
        root.getChildren().add(new BattleScreen(this, numPlayers));
    }

    public void startBattle(int numPlayers) {
        startBattleWithIntro(numPlayers);
    }
}