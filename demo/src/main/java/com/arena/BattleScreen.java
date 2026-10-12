package com.arena;

import com.arena.model.Combatant;
import com.arena.model.Enemy;
import com.arena.model.Hero;
import com.arena.model.Skill;
import com.arena.puzzles.Puzzle;
import com.arena.puzzles.PuzzleBank;
import com.arena.ui.Battler;
import com.arena.ui.PauseMenu;
import com.arena.ui.PuzzleModal;
import com.arena.ui.Sprite;
import com.arena.ui.Sprites;
import com.arena.ui.StatBar;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BattleScreen extends StackPane {

    private static final int MAX_WAVES    = 5;
    private static final int SPRITE_SCALE = 6;

    private final Random rng = new Random();
    private final Game game;
    private final Settings settings;

    private final List<Hero> heroes = new ArrayList<>();
    private final List<Enemy> enemies = new ArrayList<>();
    private final List<Battler> heroBattlers = new ArrayList<>();
    private final List<Battler> enemyBattlers = new ArrayList<>();

    private List<Combatant> turnQueue = new ArrayList<>();
    private int turnIndex = 0;
    private int round = 0;
    private int wave = 0;
    private Combatant current;
    private Enemy selectedEnemy;

    private boolean awaitingInput = false;
    private boolean gameOver = false;
    private boolean paused = false;
    private boolean showingIntro = false;

    private StackPane overlayLayer;
    private StackPane modalLayer;
    private PuzzleModal puzzleModal;
    private PauseMenu pauseMenu;

    private Pane arenaPane;
    private HBox turnOrderBar;
    private VBox turnBanner;
    private Label turnBannerMain, turnBannerSub;
    private VBox logBox;
    private ScrollPane logScroll;
    private HBox actionBar;
    private HBox heroStatBar;
    private Label waveLabel, roundLabel, turnLabel;
    private Button pauseBtn;

    public BattleScreen(Game game, int numPlayers) {
        this.game = game;
        this.settings = game.getSettings();

        BorderPane main = new BorderPane();
        main.setTop(buildHeader());
        main.setCenter(buildArena());
        main.setBottom(buildBottom());

        overlayLayer = new StackPane();
        overlayLayer.setVisible(false);
        overlayLayer.setPickOnBounds(true);

        puzzleModal = new PuzzleModal();
        pauseMenu = new PauseMenu(settings, this::resumeFromPause, () -> game.showMenu());

        modalLayer = new StackPane(puzzleModal, pauseMenu);
        modalLayer.setPickOnBounds(false);

        getChildren().addAll(main, overlayLayer, modalLayer);

        heroes.add(Hero.knight());
        if (numPlayers >= 2) heroes.add(Hero.mage());

        setFocusTraversable(true);
        setOnKeyPressed(this::handleKey);

        initGame();
        Platform.runLater(() -> { requestFocus(); layoutBattlers(); });
    }

    // ===================================================================
    //  HEADER
    // ===================================================================

    private Node buildHeader() {
        HBox header = new HBox(14);
        header.setPadding(new Insets(10, 22, 8, 22));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: #0a0d1a; -fx-border-color: #232a44; -fx-border-width: 0 0 1 0;");

        Label title = new Label("ARENA OF ECHOES");
        title.setStyle("-fx-text-fill: #c4b5fd; -fx-font-size: 15px; -fx-font-weight: bold; -fx-font-family: 'Georgia';");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        turnLabel  = headLabel("—", "#38bdf8");
        roundLabel = headLabel("ROUND 1", "#94a3b8");
        waveLabel  = headLabel("WAVE 1 / " + MAX_WAVES, "#a78bfa");

        pauseBtn = new Button("❚❚  Pause  (Esc)");
        pauseBtn.setStyle(pauseStyle(false));
        pauseBtn.setOnMouseEntered(e -> { if (!pauseBtn.isDisabled()) pauseBtn.setStyle(pauseStyle(true)); });
        pauseBtn.setOnMouseExited(e  -> pauseBtn.setStyle(pauseStyle(false)));
        pauseBtn.setOnAction(e -> togglePause());
        pauseBtn.setDisable(true);

        header.getChildren().addAll(title, spacer, turnLabel, sep(), roundLabel, sep(), waveLabel, pauseBtn);
        return header;
    }

    private Label sep() {
        Label l = new Label("·");
        l.setStyle("-fx-text-fill: #334155; -fx-font-size: 13px;");
        return l;
    }

    private Label headLabel(String text, String color) {
        Label l = new Label(text);
        l.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 12.5px; -fx-font-weight: bold;");
        return l;
    }

    private String pauseStyle(boolean hover) {
        String c = "#94a3b8";
        return "-fx-background-color: " + (hover ? c : "rgba(255,255,255,0.03)") + ";" +
               "-fx-border-color: " + c + ";" +
               "-fx-border-width: 1;" +
               "-fx-border-radius: 6;" +
               "-fx-background-radius: 6;" +
               "-fx-text-fill: " + (hover ? "#0a0c13" : c) + ";" +
               "-fx-font-size: 11px;" +
               "-fx-font-weight: bold;" +
               "-fx-padding: 4 12;" +
               "-fx-cursor: hand;";
    }

    // ===================================================================
    //  ARENA
    // ===================================================================

    private Node buildArena() {
        arenaPane = new Pane();
        arenaPane.setStyle("-fx-background-color: linear-gradient(to bottom, #0b0e1e 0%, #141a30 55%, #221a44 100%);");

        // Ground plane
        Rectangle ground = new Rectangle();
        ground.widthProperty().bind(arenaPane.widthProperty());
        ground.yProperty().bind(arenaPane.heightProperty().multiply(0.75));
        ground.heightProperty().bind(arenaPane.heightProperty().multiply(0.25));
        ground.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#3b2d6b", 0.85)),
                new Stop(1, Color.web("#0b0e1e", 0.15))));
        arenaPane.getChildren().add(ground);

        // Horizon line
        Rectangle horizon = new Rectangle();
        horizon.widthProperty().bind(arenaPane.widthProperty());
        horizon.yProperty().bind(arenaPane.heightProperty().multiply(0.748));
        horizon.setHeight(2);
        horizon.setFill(Color.web("#7c5cff", 0.55));
        arenaPane.getChildren().add(horizon);

        // Perspective tiles on the floor
        for (int i = 1; i <= 8; i++) {
            Line tile = new Line();
            tile.setStroke(Color.web("#7c5cff", 0.14));
            tile.setStrokeWidth(1);
            tile.startYProperty().bind(arenaPane.heightProperty().multiply(0.75));
            tile.endYProperty().bind(arenaPane.heightProperty());
            tile.startXProperty().bind(arenaPane.widthProperty().multiply(0.5));
            final double xi = i;
            tile.endXProperty().bind(arenaPane.widthProperty().multiply(0.5 + (xi - 4.5) * 0.16));
            arenaPane.getChildren().add(tile);
        }
        // Horizontal depth lines on the floor
        for (int i = 0; i < 4; i++) {
            final int idx = i;
            Line depth = new Line();
            depth.setStroke(Color.web("#7c5cff", 0.10));
            depth.setStrokeWidth(1);
            depth.startXProperty().bind(arenaPane.widthProperty().multiply(0.05));
            depth.endXProperty().bind(arenaPane.widthProperty().multiply(0.95));
            depth.startYProperty().bind(arenaPane.heightProperty().multiply(0.75 + idx * 0.065));
            depth.endYProperty().bind(arenaPane.heightProperty().multiply(0.75 + idx * 0.065));
            arenaPane.getChildren().add(depth);
        }

        // Side pillars (drawn as dark rectangles with a glowing edge)
        for (int side = 0; side < 2; side++) {
            Rectangle pillar = new Rectangle(28, 320);
            pillar.setFill(new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.web("#0a0c13", 0.0)),
                    new Stop(0.5, Color.web("#3b2d6b", 0.85)),
                    new Stop(1, Color.web("#0a0c13", 0.0))));
            pillar.xProperty().bind(side == 0
                    ? arenaPane.widthProperty().multiply(0.02)
                    : arenaPane.widthProperty().multiply(0.98).subtract(28));
            pillar.yProperty().bind(arenaPane.heightProperty().multiply(0.20));
            arenaPane.getChildren().add(pillar);

            Rectangle pillarGlow = new Rectangle(3, 320);
            pillarGlow.setFill(Color.web("#a78bfa", 0.65));
            pillarGlow.xProperty().bind(side == 0
                    ? arenaPane.widthProperty().multiply(0.02).add(25)
                    : arenaPane.widthProperty().multiply(0.98).subtract(30));
            pillarGlow.yProperty().bind(arenaPane.heightProperty().multiply(0.20));
            arenaPane.getChildren().add(pillarGlow);
        }

        // Ambient particles (static — subtle)
        for (int i = 0; i < 60; i++) {
            Ellipse dot = new Ellipse(rng.nextInt(1200), rng.nextInt(400), 1.2, 1.2);
            dot.setFill(Color.web("#c4b5fd", 0.10 + rng.nextDouble() * 0.30));
            arenaPane.getChildren().add(dot);
        }

        // Turn order preview
        turnOrderBar = new HBox(6);
        turnOrderBar.setAlignment(Pos.CENTER_LEFT);
        turnOrderBar.setLayoutX(16);
        turnOrderBar.setLayoutY(12);
        turnOrderBar.setPadding(new Insets(6, 10, 6, 10));
        turnOrderBar.setStyle("-fx-background-color: rgba(15,23,42,0.85); " +
                "-fx-background-radius: 10; -fx-border-color: #334155; " +
                "-fx-border-radius: 10; -fx-border-width: 1;");
        arenaPane.getChildren().add(turnOrderBar);

        // Turn banner (top-center)
        turnBannerMain = new Label("READY");
        turnBannerMain.setStyle("-fx-text-fill: #38bdf8; -fx-font-size: 22px; -fx-font-weight: bold; " +
                "-fx-font-family: 'Georgia';");
        turnBannerSub = new Label("");
        turnBannerSub.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 12px;");

        turnBanner = new VBox(2, turnBannerMain, turnBannerSub);
        turnBanner.setAlignment(Pos.CENTER);
        turnBanner.setPadding(new Insets(8, 30, 10, 30));
        turnBanner.setMouseTransparent(true);
        turnBanner.setStyle("-fx-background-color: rgba(15,23,42,0.92); " +
                "-fx-background-radius: 12; -fx-border-color: #38bdf8; " +
                "-fx-border-width: 2; -fx-border-radius: 12;");

        turnBanner.layoutXProperty().bind(
                arenaPane.widthProperty().subtract(turnBanner.widthProperty()).divide(2));
        turnBanner.layoutYProperty().bind(arenaPane.heightProperty().multiply(0.03));
        arenaPane.getChildren().add(turnBanner);

        arenaPane.widthProperty().addListener((o, ov, nv) -> layoutBattlers());
        arenaPane.heightProperty().addListener((o, ov, nv) -> layoutBattlers());

        return arenaPane;
    }

    private void layoutBattlers() {
        double w = arenaPane.getWidth();
        double h = arenaPane.getHeight();
        if (w <= 0 || h <= 0) return;

        int nh = heroBattlers.size();
        if (nh > 0) {
            double[][] hr = nh == 1
                    ? new double[][] { { 0.06, 0.53 } }
                    : new double[][] { { 0.03, 0.56 }, { 0.24, 0.48 } };
            for (int i = 0; i < nh; i++) {
                heroBattlers.get(i).setLayoutX(w * hr[i][0]);
                heroBattlers.get(i).setLayoutY(h * hr[i][1]);
            }
        }

        int ne = enemyBattlers.size();
        if (ne > 0) {
            double[][] er;
            if (ne == 1)      er = new double[][] { { 0.72, 0.10 } };
            else if (ne == 2) er = new double[][] { { 0.58, 0.17 }, { 0.80, 0.09 } };
            else              er = new double[][] { { 0.47, 0.20 }, { 0.68, 0.11 }, { 0.87, 0.20 } };
            for (int i = 0; i < ne; i++) {
                enemyBattlers.get(i).setLayoutX(w * er[i][0]);
                enemyBattlers.get(i).setLayoutY(h * er[i][1]);
            }
        }
    }

    // ===================================================================
    //  BOTTOM
    // ===================================================================

    private Node buildBottom() {
        VBox bottom = new VBox(0);
        bottom.setStyle("-fx-background-color: #0a0d1a;");

        logBox = new VBox(2);
        logBox.setPadding(new Insets(6, 18, 6, 18));
        logBox.setStyle("-fx-background-color: #0e1220;");

        logScroll = new ScrollPane(logBox);
        logScroll.setFitToWidth(true);
        logScroll.setPrefHeight(74);
        logScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        logScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        logScroll.setStyle("-fx-background: #0e1220; -fx-background-color: #0e1220; " +
                "-fx-border-color: #232a44; -fx-border-width: 1 0 0 0;");

        heroStatBar = new HBox(14);
        heroStatBar.setAlignment(Pos.CENTER_LEFT);
        heroStatBar.setPadding(new Insets(10, 12, 12, 22));
        HBox.setHgrow(heroStatBar, Priority.ALWAYS);

        actionBar = new HBox(8);
        actionBar.setAlignment(Pos.CENTER_RIGHT);
        actionBar.setPadding(new Insets(10, 22, 12, 12));
        actionBar.setMinWidth(520);

        HBox row = new HBox(0, heroStatBar, actionBar);
        row.setStyle("-fx-background-color: #0e1220; -fx-border-color: #232a44; -fx-border-width: 1 0 0 0;");
        row.setAlignment(Pos.CENTER_LEFT);

        bottom.getChildren().addAll(logScroll, row);
        return bottom;
    }

    // ===================================================================
    //  INIT + WAVE FLOW
    // ===================================================================

    private void initGame() {
        gameOver = false; awaitingInput = false; paused = false;
        wave = 0; round = 0; turnIndex = 0;
        selectedEnemy = null; current = null;

        heroBattlers.clear();
        for (Hero h : heroes) {
            heroBattlers.add(new Battler(h, true, null, SPRITE_SCALE, "P" + (heroBattlers.size() + 1)));
        }

        log("The arena gates slam shut behind you.", "#94a3b8");
        log("When an enemy attacks, a CODE CHALLENGE appears.", "#facc15");
        log("Answer correctly to block the blow.", "#94a3b8");
        log("", "#000000");

        nextWave();
    }

    /** Runs the cinematic, then spawns enemies, then starts the turn loop. */
    private void nextWave() {
        wave++;
        waveLabel.setText("WAVE " + wave + " / " + MAX_WAVES);

        // Pre-compute this wave's enemies so the cinematic can preview them
        double s = (1.0 + 0.13 * (wave - 1)) * settings.enemyScale();
        List<Enemy> spawn = new ArrayList<>();
        switch (wave) {
            case 1 -> { spawn.add(Enemy.goblin(s)); spawn.add(Enemy.goblin(s)); }
            case 2 -> { spawn.add(Enemy.orc(s));    spawn.add(Enemy.goblin(s)); }
            case 3 -> { spawn.add(Enemy.skeleton(s)); spawn.add(Enemy.skeleton(s)); spawn.add(Enemy.darkMage(s)); }
            case 4 -> { spawn.add(Enemy.orc(s));    spawn.add(Enemy.darkMage(s)); spawn.add(Enemy.skeleton(s)); }
            default -> { spawn.add(Enemy.demon(s)); spawn.add(Enemy.goblin(s));   spawn.add(Enemy.goblin(s)); }
        }

        // Hide the arena's battlers while the intro plays
        showingIntro = true;
        for (Battler b : heroBattlers) arenaPane.getChildren().remove(b);
        for (Battler b : enemyBattlers) arenaPane.getChildren().remove(b);

        List<String> spriteKeys = new ArrayList<>();
        List<String> colors = new ArrayList<>();
        for (Enemy e : spawn) { spriteKeys.add(e.spriteKey()); colors.add(e.colorHex()); }

        BattleIntro intro = new BattleIntro("WAVE " + wave, spriteKeys, colors, () -> {
            getChildren().remove(getChildren().size() - 1); // remove intro overlay
            showingIntro = false;
            doSpawnWave(spawn);
            beginTurn();
        });
        getChildren().add(intro);
    }

    private void doSpawnWave(List<Enemy> spawn) {
        for (Battler b : enemyBattlers) arenaPane.getChildren().remove(b);
        enemies.clear(); enemyBattlers.clear();

        for (Enemy e : spawn) {
            enemies.add(e);
            Battler b = new Battler(e, false, this::selectEnemy, SPRITE_SCALE, null);
            enemyBattlers.add(b);
            arenaPane.getChildren().add(b);
        }
        selectedEnemy = enemies.get(0);

        // Add heroes back
        for (Battler b : heroBattlers) if (!arenaPane.getChildren().contains(b)) {
            arenaPane.getChildren().add(b);
        }

        layoutBattlers();

        StringBuilder sb = new StringBuilder("Wave " + wave + " — ");
        for (int i = 0; i < spawn.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(spawn.get(i).name);
        }
        sb.append(" appear!");
        log(sb.toString(), wave == MAX_WAVES ? "#dc2626" : "#f87171");
        log("", "#000000");

        refreshUI();
    }

    // -------------------------------------------------------------------
    //  TURN ENGINE
    // -------------------------------------------------------------------

    private void buildQueue() {
        turnQueue = new ArrayList<>();
        for (Hero h : heroes) if (h.isAlive()) turnQueue.add(h);
        for (Enemy e : enemies) if (e.isAlive()) turnQueue.add(e);
        turnQueue.sort((a, b) -> Integer.compare(b.spd, a.spd));
        turnIndex = 0;
    }

    private void beginTurn() {
        if (gameOver || paused || showingIntro) return;
        if (checkBattleEnd()) return;

        while (true) {
            if (turnQueue == null || turnQueue.isEmpty() || turnIndex >= turnQueue.size()) {
                round++;
                roundLabel.setText("ROUND " + round);
                buildQueue();
                log("── Round " + round + " ──", "#475569");
            }
            if (turnIndex >= turnQueue.size()) return;
            if (turnQueue.get(turnIndex).isAlive()) break;
            turnIndex++;
        }

        current = turnQueue.get(turnIndex);
        if (current.defending) current.defending = false;
        current.tickCooldowns();

        if (current.poison > 0) {
            current.poison--;
            int pd = 8;
            current.takeDamage(pd);
            log(current.name + " suffers " + pd + " poison damage (" + current.poison + " turns left).", "#a3e635");
            Battler b = battlerFor(current);
            if (b != null) b.popText("-" + pd, Color.web("#a3e635"));
            refreshUI();

            if (!current.isAlive()) {
                log(current.name + " succumbs to the poison!", "#a3e635");
                refreshUI();
                if (checkBattleEnd()) return;
                turnIndex++;
                PauseTransition p = new PauseTransition(Duration.millis(550));
                p.setOnFinished(e -> beginTurn());
                p.play();
                return;
            }
        }

        highlightCurrent();
        updateTurnBanner(current);
        updateTurnOrderPreview();

        if (current instanceof Hero hero) {
            awaitingInput = true;
            pauseBtn.setDisable(false);
            buildActionBar(hero);
            refreshUI();
            Platform.runLater(this::requestFocus);
        } else {
            awaitingInput = false;
            pauseBtn.setDisable(true);
            showWaitingBar();
            refreshUI();
            PauseTransition p = new PauseTransition(Duration.millis(600));
            p.setOnFinished(e -> enemyAction((Enemy) current));
            p.play();
        }
    }

    private void endTurn() {
        refreshUI();
        if (checkBattleEnd()) return;
        turnIndex++;
        PauseTransition p = new PauseTransition(Duration.millis(450));
        p.setOnFinished(e -> beginTurn());
        p.play();
    }

    private boolean checkBattleEnd() {
        boolean allHeroesDead = heroes.stream().noneMatch(Combatant::isAlive);
        if (allHeroesDead) { defeat(); return true; }

        boolean anyEnemyAlive = enemies.stream().anyMatch(Combatant::isAlive);
        if (anyEnemyAlive) return false;

        if (wave >= MAX_WAVES) { victory(); return true; }

        log("", "#000000");
        log("Wave " + wave + " cleared!", "#4ade80");
        for (Hero h : heroes) {
            if (!h.isAlive()) continue;
            int heal = (int) (h.maxHp * 0.30);
            h.heal(heal);
            h.restoreMp(15);
            h.potions++;
        }
        log("The party recovers: +30% HP, +15 MP, +1 Potion each.", "#4ade80");
        log("", "#000000");

        awaitingInput = false;
        refreshUI();

        PauseTransition p = new PauseTransition(Duration.millis(1400));
        p.setOnFinished(e -> {
            turnQueue = new ArrayList<>();
            turnIndex = 0;
            nextWave();
        });
        p.play();
        return true;
    }

    // -------------------------------------------------------------------
    //  TURN BANNER
    // -------------------------------------------------------------------

    private void updateTurnBanner(Combatant c) {
        String color, main, sub;
        if (c instanceof Hero h) {
            int pidx = heroes.indexOf(h) + 1;
            main = "▶  PLAYER " + pidx + "'S TURN";
            sub  = h.name + "  ·  Lv " + h.level;
            color = h.colorHex();
        } else {
            main = "▶  ENEMY'S TURN";
            sub  = c.name;
            color = "#f87171";
        }

        turnBannerMain.setText(main);
        turnBannerMain.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 22px; " +
                "-fx-font-weight: bold; -fx-font-family: 'Georgia';");
        turnBannerSub.setText(sub);
        turnBanner.setStyle("-fx-background-color: rgba(15,23,42,0.92); " +
                "-fx-background-radius: 12; -fx-border-color: " + color + "; " +
                "-fx-border-width: 2; -fx-border-radius: 12;");

        FadeTransition ft = new FadeTransition(Duration.millis(220), turnBanner);
        ft.setFromValue(0.35); ft.setToValue(1.0);
        ScaleTransition st = new ScaleTransition(Duration.millis(220), turnBanner);
        st.setFromX(0.95); st.setFromY(0.95);
        st.setToX(1.0); st.setToY(1.0);
        ft.play(); st.play();

        turnLabel.setText(c instanceof Hero h
                ? "PLAYER " + (heroes.indexOf(h) + 1)
                : "ENEMY");
        turnLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 12.5px; -fx-font-weight: bold;");
    }

    // -------------------------------------------------------------------
    //  ACTION BAR
    // -------------------------------------------------------------------

    private void buildActionBar(Hero hero) {
        actionBar.getChildren().clear();

        // Ultimate button (leftmost, big)
        if (hero.ultimate != null) {
            boolean ready = hero.hasUlt();
            Button ult = new Button(ready
                    ? "★ " + hero.ultimate.name.toUpperCase() + "  (ULT)"
                    : "★ ULT  " + (int) (hero.ultRatio() * 100) + "%");
            ult.setPrefHeight(46);
            ult.setStyle(ultStyle(ready, false));
            ult.setOnMouseEntered(e -> ult.setStyle(ultStyle(ready, true)));
            ult.setOnMouseExited(e  -> ult.setStyle(ultStyle(ready, false)));
            ult.setDisable(!ready || !awaitingInput || gameOver);
            ult.setOnAction(e -> doHeroUltimate(hero));
            actionBar.getChildren().add(ult);
        }

        int shortcut = 1;
        for (Skill s : hero.skills) {
            int cd = hero.cooldownOf(s);
            boolean canUse = !gameOver && hero.mp >= s.mpCost && cd == 0;
            String label = "[" + shortcut + "] " + s.name + (s.mpCost > 0 ? "  ·  " + s.mpCost + " MP" : "");
            if (cd > 0) label += "  (CD " + cd + ")";
            Button b = makeActionButton(label, hero.colorHex(), canUse);
            b.setDisable(!canUse);
            b.setOnAction(e -> doHeroSkill(hero, s));
            actionBar.getChildren().add(b);
            shortcut++;
        }

        Button pot = makeActionButton("Potion ×" + hero.potions, "#4ade80", hero.potions > 0);
        pot.setDisable(hero.potions <= 0);
        pot.setOnAction(e -> doHeroPotion(hero));
        actionBar.getChildren().add(pot);

        Button eth = makeActionButton("Ether ×" + hero.ethers, "#38bdf8", hero.ethers > 0);
        eth.setDisable(hero.ethers <= 0);
        eth.setOnAction(e -> doHeroEther(hero));
        actionBar.getChildren().add(eth);
    }

    private String ultStyle(boolean ready, boolean hover) {
        String c = ready ? "#facc15" : "#475569";
        String fill = hover && ready ? c : "rgba(250,204,21,0.08)";
        String fg = hover && ready ? "#0a0c13" : c;
        return "-fx-background-color: " + fill + ";" +
               "-fx-border-color: " + c + ";" +
               "-fx-border-width: 2;" +
               "-fx-border-radius: 8;" +
               "-fx-background-radius: 8;" +
               "-fx-text-fill: " + fg + ";" +
               "-fx-font-size: 11.5px;" +
               "-fx-font-weight: bold;" +
               "-fx-padding: 6 14;" +
               "-fx-cursor: hand;";
    }

    private void showWaitingBar() {
        actionBar.getChildren().clear();
        Label l = new Label("· · ·  enemy is acting  · · ·");
        l.setStyle("-fx-text-fill: #475569; -fx-font-size: 14px; -fx-font-style: italic;");
        actionBar.getChildren().add(l);
    }

    private Button makeActionButton(String text, String color, boolean enabled) {
        Button b = new Button(text);
        b.setPrefHeight(40);
        b.setStyle(actionStyle(color, false, enabled));
        b.setOnMouseEntered(e -> { if (!b.isDisabled()) b.setStyle(actionStyle(color, true, true)); });
        b.setOnMouseExited(e  -> b.setStyle(actionStyle(color, false, !b.isDisabled())));
        return b;
    }

    private String actionStyle(String c, boolean hover, boolean enabled) {
        String fill = hover ? c : "rgba(255,255,255,0.02)";
        String fg   = hover ? "#0a0c13" : c;
        return "-fx-background-color: " + fill + ";" +
               "-fx-border-color: " + (enabled ? c : "#475569") + ";" +
               "-fx-border-width: 1.5;" +
               "-fx-border-radius: 8;" +
               "-fx-background-radius: 8;" +
               "-fx-text-fill: " + (enabled ? fg : "#475569") + ";" +
               "-fx-font-size: 11.5px;" +
               "-fx-font-weight: bold;" +
               "-fx-padding: 6 12;" +
               "-fx-cursor: hand;";
    }

    // -------------------------------------------------------------------
    //  HERO ACTIONS
    // -------------------------------------------------------------------

    private void doHeroSkill(Hero hero, Skill s) {
        if (!awaitingInput || paused || showingIntro) return;
        awaitingInput = false;
        pauseBtn.setDisable(true);

        hero.mp -= s.mpCost;
        if (s.cooldown > 0) hero.cooldowns.put(s, s.cooldown + 1);
        hero.grantUltCharge(10); // using skills charges ult

        Battler hb = battlerFor(hero);
        log("", "#000000");

        switch (s.type) {
            case ATTACK_ONE -> {
                Enemy target = currentEnemyTarget();
                if (target == null) { awaitingInput = true; return; }
                log(hero.name + " uses " + s.name + " on " + target.name + "!", hero.colorHex());
                if (hb != null) hb.playAttack(() -> impactEnemy(hero, target, s));
                else impactEnemy(hero, target, s);
            }
            case ATTACK_ALL -> {
                log(hero.name + " uses " + s.name + "!", hero.colorHex());
                if (hb != null) hb.playAttack(() -> impactAllEnemies(hero, s));
                else impactAllEnemies(hero, s);
            }
            case HEAL_SELF -> {
                hero.heal(s.healAmount);
                log(hero.name + " uses " + s.name + " and recovers " + s.healAmount + " HP.", "#4ade80");
                if (hb != null) hb.popText("+" + s.healAmount, Color.web("#4ade80"));
                refreshUI(); endTurn();
            }
            case GUARD -> {
                hero.defending = true;
                hero.restoreMp(10);
                log(hero.name + " raises a guard. (+10 MP)", "#38bdf8");
                if (hb != null) hb.popText("GUARD", Color.web("#38bdf8"));
                refreshUI(); endTurn();
            }
            default -> endTurn();
        }
    }

    // -------- ULTIMATE --------

    private void doHeroUltimate(Hero hero) {
        if (!awaitingInput || !hero.hasUlt() || paused || showingIntro) return;
        awaitingInput = false;
        pauseBtn.setDisable(true);
        hero.spendUlt();

        playUltimateCinematic(hero);
    }

    /** Big cinematic flash + name card, then resolves the ultimate's damage. */
    private void playUltimateCinematic(Hero hero) {
        Rectangle flash = new Rectangle();
        flash.widthProperty().bind(widthProperty());
        flash.heightProperty().bind(heightProperty());
        flash.setFill(Color.web("#facc15"));
        flash.setOpacity(0);
        flash.setMouseTransparent(true);

        Label ultName = new Label(hero.ultimate.name.toUpperCase());
        ultName.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 76px; -fx-font-weight: bold; " +
                "-fx-font-family: 'Georgia';");
        ultName.setEffect(new DropShadow(34, Color.web("#facc15")));
        ultName.setOpacity(0);
        ultName.setMouseTransparent(true);

        Label ultOwner = new Label(hero.name.toUpperCase() + "  ·  ULTIMATE");
        ultOwner.setStyle("-fx-text-fill: #facc15; -fx-font-size: 15px; -fx-font-weight: bold; " +
                "-fx-font-family: 'Consolas';");
        ultOwner.setOpacity(0);
        ultOwner.setMouseTransparent(true);
        ultOwner.setTranslateY(55);

        VBox textStack = new VBox(6, ultName, ultOwner);
        textStack.setAlignment(Pos.CENTER);
        textStack.setMouseTransparent(true);

        getChildren().addAll(flash, textStack);

        FadeTransition flashIn = new FadeTransition(Duration.millis(150), flash);
        flashIn.setToValue(0.80);
        FadeTransition flashOut = new FadeTransition(Duration.millis(500), flash);
        flashOut.setToValue(0);

        FadeTransition nameIn = new FadeTransition(Duration.millis(280), ultName);
        nameIn.setToValue(1);
        FadeTransition ownerIn = new FadeTransition(Duration.millis(280), ultOwner);
        ownerIn.setToValue(1);
        FadeTransition nameOut = new FadeTransition(Duration.millis(380), ultName);
        nameOut.setToValue(0);
        FadeTransition ownerOut = new FadeTransition(Duration.millis(380), ultOwner);
        ownerOut.setToValue(0);

        SequentialTransition seq = new SequentialTransition(
                flashIn,
                new javafx.animation.ParallelTransition(nameIn, ownerIn),
                new PauseTransition(Duration.millis(560)),
                new javafx.animation.ParallelTransition(nameOut, ownerOut),
                flashOut
        );
        seq.setOnFinished(e -> {
            getChildren().remove(flash);
            getChildren().remove(textStack);
            applyUltimateDamage(hero);
        });
        seq.play();
    }

    private void applyUltimateDamage(Hero hero) {
        Battler hb = battlerFor(hero);
        Runnable impact = () -> {
            log("★ " + hero.name + " unleashes " + hero.ultimate.name + "! ★", "#facc15");

            if (hero.ultimate.type == Skill.Type.ULTIMATE) {
                // Ultimates always hit all enemies (both current ults are AoE)
                for (Enemy e : enemies) {
                    if (!e.isAlive()) continue;
                    int dmg = rollDamage(hero, e, hero.ultimate.multiplier, hero.ultimate.pierce, true);
                    e.takeDamage(dmg);
                    log("   → " + e.name + " takes " + dmg + " damage.", "#e2e8f0");
                    Battler b = battlerFor(e);
                    if (b != null) { b.popText("-" + dmg, Color.web("#facc15")); b.playHurt(); }
                    if (hero.ultimate.poisonTurns > 0 && e.isAlive()) {
                        e.poison = hero.ultimate.poisonTurns;
                        log("   → " + e.name + " is poisoned!", "#a3e635");
                    }
                }
                if (hero.ultimate.healAmount > 0) {
                    hero.heal(hero.ultimate.healAmount);
                    log("   → " + hero.name + " recovers " + hero.ultimate.healAmount + " HP.", "#4ade80");
                    if (hb != null) hb.popText("+" + hero.ultimate.healAmount, Color.web("#4ade80"));
                }
            }

            refreshUI();
            for (Enemy e : enemies) handleDeath(e);
            endTurn();
        };

        if (hb != null) hb.playAttack(impact);
        else impact.run();
    }

    private void impactEnemy(Hero hero, Enemy target, Skill s) {
        int dmg = rollDamage(hero, target, s.multiplier, s.pierce, true);
        target.takeDamage(dmg);
        log("   → " + target.name + " takes " + dmg + " damage.", "#e2e8f0");
        Battler tb = battlerFor(target);
        if (tb != null) { tb.popText("-" + dmg, Color.web("#f87171")); tb.playHurt(); }
        if (s.poisonTurns > 0 && target.isAlive()) {
            target.poison = s.poisonTurns;
            log("   → " + target.name + " is poisoned!", "#a3e635");
        }
        hero.grantUltCharge(15);
        refreshUI();
        handleDeath(target);
        endTurn();
    }

    private void impactAllEnemies(Hero hero, Skill s) {
        List<Enemy> hit = new ArrayList<>();
        for (Enemy e : enemies) if (e.isAlive()) hit.add(e);
        if (hit.isEmpty()) { endTurn(); return; }
        for (Enemy e : hit) {
            int dmg = rollDamage(hero, e, s.multiplier, s.pierce, true);
            e.takeDamage(dmg);
            log("   → " + e.name + " takes " + dmg + " damage.", "#e2e8f0");
            Battler b = battlerFor(e);
            if (b != null) { b.popText("-" + dmg, Color.web("#f87171")); b.playHurt(); }
        }
        hero.grantUltCharge(15);
        refreshUI();
        for (Enemy e : hit) handleDeath(e);
        endTurn();
    }

    private void doHeroPotion(Hero hero) {
        if (!awaitingInput || hero.potions <= 0) return;
        awaitingInput = false; pauseBtn.setDisable(true);
        hero.potions--;
        int heal = 55;
        hero.heal(heal);
        log(hero.name + " drinks a potion and recovers " + heal + " HP.", "#4ade80");
        Battler b = battlerFor(hero);
        if (b != null) b.popText("+" + heal, Color.web("#4ade80"));
        refreshUI(); endTurn();
    }

    private void doHeroEther(Hero hero) {
        if (!awaitingInput || hero.ethers <= 0) return;
        awaitingInput = false; pauseBtn.setDisable(true);
        hero.ethers--;
        int restore = 40;
        hero.restoreMp(restore);
        log(hero.name + " drinks an ether and restores " + restore + " MP.", "#38bdf8");
        Battler b = battlerFor(hero);
        if (b != null) b.popText("+" + restore + " MP", Color.web("#38bdf8"));
        refreshUI(); endTurn();
    }

    // -------------------------------------------------------------------
    //  ENEMY AI + PUZZLE
    // -------------------------------------------------------------------

    private void enemyAction(Enemy en) {
        if (gameOver || paused || showingIntro || !en.isAlive()) { turnIndex++; beginTurn(); return; }

        if (en.healCd > 0) en.healCd--;

        if (en.healer && en.hp < en.maxHp * 0.45 && en.healCd <= 0) {
            int amount = 26;
            en.heal(amount);
            en.healCd = 3;
            log(en.name + " chants a DARK MEND and recovers " + amount + " HP.", "#f472b6");
            Battler b = battlerFor(en);
            if (b != null) b.popText("+" + amount, Color.web("#4ade80"));
            refreshUI();
            endEnemyTurn();
            return;
        }

        List<Hero> living = new ArrayList<>();
        for (Hero h : heroes) if (h.isAlive()) living.add(h);
        if (living.isEmpty()) { endEnemyTurn(); return; }
        Hero victim = living.get(rng.nextInt(living.size()));

        boolean useSpecial = en.special != null
                && en.cooldownOf(en.special) == 0
                && rng.nextDouble() < en.specialChance;

        if (settings.shouldTriggerPuzzle(rng)) {
            Puzzle p = PuzzleBank.random(settings.difficulty, rng);
            puzzleModal.show(p, settings.puzzleTimer, settings.puzzleTimeSeconds,
                    correct -> resolveEnemyAttack(en, victim, useSpecial, correct == 1));
        } else {
            resolveEnemyAttack(en, victim, useSpecial, false);
        }
    }

    private void resolveEnemyAttack(Enemy en, Hero victim, boolean useSpecial, boolean blocked) {
        if (useSpecial) en.cooldowns.put(en.special, en.special.cooldown + 1);
        Battler eb = battlerFor(en);
        Runnable impact = () -> {
            if (useSpecial) applyEnemySpecial(en, victim, blocked);
            else            applyEnemyBasic(en, victim, blocked);

            if (blocked) {
                Battler vb = battlerFor(victim);
                if (vb != null) vb.popText("BLOCK!", Color.web("#4ade80"));
                log("   → The attack is BLOCKED by your coding!", "#4ade80");
                for (Hero h : heroes) if (h.isAlive()) h.grantXp(5);
            }

            refreshUI();
            for (Hero h : heroes) if (!h.isAlive()) log(h.name + " has fallen!", "#ef4444");
            endEnemyTurn();
        };
        if (eb != null) eb.playAttack(impact);
        else impact.run();
    }

    private void applyEnemyBasic(Enemy en, Hero victim, boolean blocked) {
        if (blocked) return;
        int dmg = rollDamage(en, victim, 1.0, false, false);
        victim.takeDamage(dmg);
        if (victim instanceof Hero h) h.grantUltCharge(8);
        log(en.name + " attacks " + victim.name + " for " + dmg + " damage.", "#fca5a5");
        Battler vb = battlerFor(victim);
        if (vb != null) { vb.popText("-" + dmg, Color.web("#f87171")); vb.playHurt(); }
    }

    private void applyEnemySpecial(Enemy en, Hero victim, boolean blocked) {
        Skill s = en.special;
        log(en.name + " uses " + s.name + "!", "#ef4444");
        if (blocked) return;
        switch (s.type) {
            case ATTACK_ONE -> {
                int dmg = rollDamage(en, victim, s.multiplier, s.pierce, false);
                victim.takeDamage(dmg);
                if (victim instanceof Hero h) h.grantUltCharge(8);
                log("   → " + victim.name + " takes " + dmg + " damage.", "#e2e8f0");
                Battler vb = battlerFor(victim);
                if (vb != null) { vb.popText("-" + dmg, Color.web("#f87171")); vb.playHurt(); }
                if (s.poisonTurns > 0 && victim.isAlive()) {
                    victim.poison = s.poisonTurns;
                    log("   → " + victim.name + " is poisoned!", "#a3e635");
                }
            }
            case ATTACK_ALL -> {
                for (Hero h : heroes) {
                    if (!h.isAlive()) continue;
                    int dmg = rollDamage(en, h, s.multiplier, s.pierce, false);
                    h.takeDamage(dmg);
                    h.grantUltCharge(8);
                    log("   → " + h.name + " takes " + dmg + " damage.", "#e2e8f0");
                    Battler vb = battlerFor(h);
                    if (vb != null) { vb.popText("-" + dmg, Color.web("#f87171")); vb.playHurt(); }
                    if (s.poisonTurns > 0 && h.isAlive()) h.poison = s.poisonTurns;
                }
            }
            default -> { }
        }
    }

    private void endEnemyTurn() {
        refreshUI();
        if (checkBattleEnd()) return;
        turnIndex++;
        PauseTransition p = new PauseTransition(Duration.millis(600));
        p.setOnFinished(e -> beginTurn());
        p.play();
    }

    // -------------------------------------------------------------------
    //  PAUSE
    // -------------------------------------------------------------------

    private void togglePause() {
        if (!awaitingInput || gameOver || showingIntro) return;
        paused = true;
        pauseMenu.setVisible(true);
    }

    private void resumeFromPause() {
        paused = false;
        pauseMenu.setVisible(false);
        Platform.runLater(this::requestFocus);
    }

    private void handleKey(javafx.scene.input.KeyEvent e) {
        if (e.getCode() == KeyCode.ESCAPE && awaitingInput && !gameOver) {
            togglePause();
            return;
        }
        if (!awaitingInput || paused || gameOver || showingIntro) return;
        if (!(current instanceof Hero hero)) return;

        // 'Q' or '0' triggers ultimate
        if ((e.getCode() == KeyCode.Q || e.getCode() == KeyCode.DIGIT0) && hero.hasUlt()) {
            doHeroUltimate(hero);
            return;
        }

        int idx = -1;
        switch (e.getCode()) {
            case DIGIT1 -> idx = 0;
            case DIGIT2 -> idx = 1;
            case DIGIT3 -> idx = 2;
            case DIGIT4 -> idx = 3;
            case DIGIT5 -> idx = 4;
            case DIGIT6 -> idx = 5;
        }
        if (idx >= 0 && idx < hero.skills.size()) {
            Skill s = hero.skills.get(idx);
            if (hero.mp >= s.mpCost && hero.cooldownOf(s) == 0) doHeroSkill(hero, s);
        }
    }

    // -------------------------------------------------------------------
    //  TARGETING + DEATH
    // -------------------------------------------------------------------

    private void selectEnemy(Combatant c) {
        if (c instanceof Enemy e && e.isAlive()) {
            selectedEnemy = e;
            updateTargetHighlight();
        }
    }

    private Enemy currentEnemyTarget() {
        if (selectedEnemy != null && selectedEnemy.isAlive()) return selectedEnemy;
        for (Enemy e : enemies) if (e.isAlive()) return e;
        return null;
    }

    private void handleDeath(Enemy e) {
        if (e.isAlive()) return;
        log(e.name + " is defeated!", "#4ade80");
        int xp = (int) (e.xpReward * settings.xpScale());

        for (Hero h : heroes) {
            if (!h.isAlive()) continue;
            int levels = h.grantXp(xp);
            h.grantUltCharge(20);
            log("   → " + h.name + " gains " + xp + " XP.", "#a78bfa");
            if (levels > 0) {
                log("   → " + h.name + " LEVELS UP! Now level " + h.level + ".", "#facc15");
                Battler hb = battlerFor(h);
                if (hb != null) hb.popText("LEVEL UP!", Color.web("#facc15"));
            }
        }

        Battler b = battlerFor(e);
        if (b != null) b.playDeath();

        if (selectedEnemy == e) {
            selectedEnemy = null;
            for (Enemy other : enemies) if (other.isAlive()) { selectedEnemy = other; break; }
        }
        refreshUI();
    }

    // -------------------------------------------------------------------
    //  COMBAT MATH
    // -------------------------------------------------------------------

    private int rollDamage(Combatant attacker, Combatant target, double mult, boolean pierce, boolean isHeroAttack) {
        if (mult == 0) return 0;
        int offense = isHeroAttack ? Math.max(attacker.atk, attacker.mag) : attacker.atk;
        double base = offense * mult;
        double mitigation = pierce ? target.def * 0.15 : target.def * 0.55;
        double raw = base - mitigation;
        raw *= 0.88 + rng.nextDouble() * 0.24;

        boolean crit = rng.nextDouble() < 0.12;
        if (crit) raw *= 1.75;
        if (target.defending) raw *= 0.45;

        int dmg = (int) Math.max(1, Math.round(raw));
        if (crit) {
            Battler c = battlerFor(target);
            if (c != null) c.popText("CRIT!", Color.web("#fbbf24"));
        }
        return dmg;
    }

    // -------------------------------------------------------------------
    //  END STATES
    // -------------------------------------------------------------------

    private void victory() {
        gameOver = true; awaitingInput = false;
        pauseBtn.setDisable(true);
        actionBar.getChildren().clear();
        log("", "#000000");
        log(">>> VICTORY! All waves have been defeated! <<<", "#4ade80");
        showOverlay("VICTORY", "The Demon Lord falls. The arena is yours.", "#4ade80");
    }

    private void defeat() {
        gameOver = true; awaitingInput = false;
        pauseBtn.setDisable(true);
        actionBar.getChildren().clear();
        log("", "#000000");
        log(">>> Your party has fallen... <<<", "#ef4444");
        showOverlay("DEFEAT", "Your party was slain on wave " + wave + ".", "#ef4444");
    }

    private void showOverlay(String title, String subtitle, String colorHex) {
        overlayLayer.getChildren().clear();
        Region dim = new Region();
        dim.setStyle("-fx-background-color: rgba(5,7,14,0.86);");
        Label t = new Label(title);
        t.setStyle("-fx-text-fill: " + colorHex + "; -fx-font-size: 56px; -fx-font-weight: bold; -fx-font-family: 'Georgia';");
        Label s = new Label(subtitle);
        s.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 15px;");

        Button again = new Button("PLAY AGAIN");
        again.setPrefSize(240, 52);
        again.setStyle(overlayButtonStyle("#a78bfa", false));
        again.setOnMouseEntered(e -> again.setStyle(overlayButtonStyle("#a78bfa", true)));
        again.setOnMouseExited(e  -> again.setStyle(overlayButtonStyle("#a78bfa", false)));
        again.setOnAction(e -> game.showMenu());

        Button menu = new Button("MAIN MENU");
        menu.setPrefSize(240, 52);
        menu.setStyle(overlayButtonStyle("#38bdf8", false));
        menu.setOnMouseEntered(e -> menu.setStyle(overlayButtonStyle("#38bdf8", true)));
        menu.setOnMouseExited(e  -> menu.setStyle(overlayButtonStyle("#38bdf8", false)));
        menu.setOnAction(e -> game.showMenu());

        HBox buttons = new HBox(14, again, menu);
        buttons.setAlignment(Pos.CENTER);
        VBox box = new VBox(18, t, s, buttons);
        box.setAlignment(Pos.CENTER);
        overlayLayer.getChildren().addAll(dim, box);
        overlayLayer.setVisible(true);
    }

    private String overlayButtonStyle(String c, boolean hover) {
        return "-fx-background-color: " + (hover ? c : "rgba(255,255,255,0.02)") + ";" +
               "-fx-border-color: " + c + ";" +
               "-fx-border-width: 2;" +
               "-fx-border-radius: 10;" +
               "-fx-background-radius: 10;" +
               "-fx-text-fill: " + (hover ? "#0a0c13" : c) + ";" +
               "-fx-font-size: 15px;" +
               "-fx-font-weight: bold;" +
               "-fx-cursor: hand;";
    }

    // -------------------------------------------------------------------
    //  REFRESH
    // -------------------------------------------------------------------

    private void refreshUI() {
        for (Battler b : heroBattlers) {
            Hero h = (Hero) b.getCombatant();
            b.getHpBar().setRatio(h.hpRatio(), true);
            if (b.getMpBar() != null) b.getMpBar().setRatio(h.mpRatio(), true);
            b.setDead(!h.isAlive());
            b.getStatusLabel().setText(statusOf(h));
            b.refreshUlt();
            b.setUltReady(h.hasUlt());
        }
        for (Battler b : enemyBattlers) {
            Enemy e = (Enemy) b.getCombatant();
            b.getHpBar().setRatio(e.hpRatio(), true);
            b.setDead(!e.isAlive());
            b.getStatusLabel().setText(statusOf(e));
        }

        heroStatBar.getChildren().clear();
        for (int i = 0; i < heroes.size(); i++) {
            heroStatBar.getChildren().add(buildHeroStatPanel(heroes.get(i), i + 1));
        }

        if (awaitingInput && current instanceof Hero h) buildActionBar(h);

        updateTargetHighlight();
        updateTurnOrderPreview();
    }

    private String statusOf(Combatant c) {
        if (!c.isAlive()) return "Down";
        if (c.poison > 0) return "Poisoned (" + c.poison + ")";
        if (c.defending)  return "Guarding";
        if (c instanceof Hero h && h.hasUlt()) return "★ ULT READY";
        if (c instanceof Enemy e && e.special != null && e.cooldownOf(e.special) > 0)
            return "Charging " + e.special.name;
        return "Ready";
    }

    private Node buildHeroStatPanel(Hero h, int playerNumber) {
        boolean isActive = h == current && awaitingInput;

        Label pBadge = new Label("P" + playerNumber);
        pBadge.setStyle("-fx-background-color: " + h.colorHex() + ";" +
                "-fx-text-fill: #0a0c13; -fx-font-size: 10px; -fx-font-weight: bold;" +
                "-fx-padding: 2 6; -fx-background-radius: 4;");

        Label name = new Label(h.name.toUpperCase() + "  Lv" + h.level);
        name.setStyle("-fx-text-fill: " + h.colorHex() + "; -fx-font-size: 11.5px; -fx-font-weight: bold;");

        HBox titleRow = new HBox(8, pBadge, name);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        Label hpText = new Label(h.hp + " / " + h.maxHp);
        hpText.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 10px; -fx-font-family: 'Consolas';");
        Label mpText = new Label(h.mp + " / " + h.maxMp);
        mpText.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 10px; -fx-font-family: 'Consolas';");

        StatBar hpBar = new StatBar(160, 8, Color.web("#22c55e"));
        hpBar.setRatio(h.hpRatio(), false);
        StatBar mpBar = new StatBar(160, 6, Color.web("#3b82f6"));
        mpBar.setRatio(h.mpRatio(), false);

        HBox hpRow = new HBox(6, smallTag("HP", "#22c55e"), hpBar, hpText);
        hpRow.setAlignment(Pos.CENTER_LEFT);
        HBox mpRow = new HBox(6, smallTag("MP", "#3b82f6"), mpBar, mpText);
        mpRow.setAlignment(Pos.CENTER_LEFT);

        // Ult status line
        Label ultTag = new Label(h.hasUlt() ? "★ ULT READY" : "ULT " + (int) (h.ultRatio() * 100) + "%");
        ultTag.setStyle("-fx-text-fill: " + (h.hasUlt() ? "#facc15" : "#64748b") + ";" +
                "-fx-font-size: 10px; -fx-font-weight: bold;");

        VBox panel = new VBox(3, titleRow, hpRow, mpRow, ultTag);
        panel.setPadding(new Insets(6, 10, 6, 10));

        String borderColor = isActive ? "#facc15" : h.colorHex() + "44";
        double borderWidth = isActive ? 2 : 1;
        String bg = isActive ? "rgba(250,204,21,0.10)" : "rgba(255,255,255,0.025)";
        panel.setStyle("-fx-background-color: " + bg + "; -fx-background-radius: 8; " +
                "-fx-border-color: " + borderColor + "; -fx-border-radius: 8; -fx-border-width: " + borderWidth + ";");

        return panel;
    }

    private Label smallTag(String t, String c) {
        Label l = new Label(t);
        l.setStyle("-fx-text-fill: " + c + "; -fx-font-size: 10px; -fx-font-weight: bold;");
        l.setMinWidth(20);
        return l;
    }

    private void updateTargetHighlight() {
        Enemy t = currentEnemyTarget();
        for (Battler b : enemyBattlers) b.setSelected(b.getCombatant() == t && awaitingInput);
    }

    private void highlightCurrent() {
        for (Battler b : heroBattlers)  b.setActive(b.getCombatant() == current);
        for (Battler b : enemyBattlers) b.setActive(b.getCombatant() == current);
    }

    private void updateTurnOrderPreview() {
        if (turnOrderBar == null) return;
        turnOrderBar.getChildren().clear();

        Label title = new Label("TURN ORDER");
        title.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 9px; -fx-font-weight: bold;");
        turnOrderBar.getChildren().add(title);

        List<Combatant> preview = new ArrayList<>();
        for (int i = turnIndex; i < turnQueue.size() && preview.size() < 6; i++) {
            Combatant c = turnQueue.get(i);
            if (c.isAlive()) preview.add(c);
        }

        for (int i = 0; i < preview.size(); i++) {
            Combatant c = preview.get(i);
            boolean isNow = i == 0;

            Sprite mini = new Sprite(Sprites.get(c.spriteKey()), Sprites.palette(), 2);
            mini.setMirror(!(c instanceof Hero));
            mini.setOpacity(isNow ? 1.0 : 0.55);

            StackPane holder = new StackPane(mini);
            holder.setPrefSize(30, 34);
            holder.setStyle(isNow
                    ? "-fx-border-color: #facc15; -fx-border-width: 2; -fx-border-radius: 5; -fx-padding: 2; " +
                      "-fx-background-color: rgba(250,204,21,0.12); -fx-background-radius: 5;"
                    : "-fx-border-color: #334155; -fx-border-width: 1; -fx-border-radius: 5; -fx-padding: 2;");

            turnOrderBar.getChildren().add(holder);
        }
    }

    private Battler battlerFor(Combatant c) {
        for (Battler b : heroBattlers)  if (b.getCombatant() == c) return b;
        for (Battler b : enemyBattlers) if (b.getCombatant() == c) return b;
        return null;
    }

    // -------------------------------------------------------------------
    //  LOGGING
    // -------------------------------------------------------------------

    private void log(String message, String colorHex) {
        Label l = new Label(message);
        l.setWrapText(true);
        l.setStyle("-fx-text-fill: " + colorHex + ";" +
                   "-fx-font-size: 12px;" +
                   "-fx-font-family: 'Consolas', 'Menlo', monospace;");
        l.setOpacity(0);
        logBox.getChildren().add(l);

        FadeTransition ft = new FadeTransition(Duration.millis(180), l);
        ft.setToValue(1.0);
        ft.play();

        while (logBox.getChildren().size() > 180) logBox.getChildren().remove(0);
        Platform.runLater(() -> logScroll.setVvalue(1.0));
    }
}