package com.almasb.fxglgames.flappy;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.input.virtual.VirtualButton;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.util.Map;
import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxglgames.flappy.EntityType.*;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */

public class FlappyBirdApp extends GameApplication {

    //various variables used later in the program.
    private PlayerComponent playerComponent;
    private boolean requestNewGame = false;
    int level = 1;
    boolean firstTime = true;
    boolean removeSlow = false;
    boolean removeLife = false;
    int lives = 0;

    //initialize settings method.
    @Override
    protected void initSettings(GameSettings settings) {
        //sets width, height, window name, and enables menus.
        settings.setWidth(1600);
        settings.setHeight(900);
        settings.setTitle("Flappy Bird");
        settings.setVersion("");
        settings.setMainMenuEnabled(true);
        settings.setGameMenuEnabled(true);
    }

    //initialize input method.
    @Override
    protected void initInput() {
        //keycode to control bird jumping when up arrow key is pressed.
        getInput().addAction(new UserAction("Jump") {
            @Override
            protected void onActionBegin() {
                playerComponent.jump();
            }
        }, KeyCode.UP, VirtualButton.UP);

    }

    //initialize  game variables method.
    @Override
    protected void initGameVars(Map<String, Object> vars) {
        //initializes stage color and score variables.
        vars.put("stageColor", Color.GREEN);
        vars.put("score", -2);
    }

    //initializes music.
    @Override
    protected void onPreInit() {
        //loops music.
        loopBGM("bgm.mp3");
    }

    //initializes game.
    @Override
    protected void initGame() {
        //runs initialize background and player methods.
        initBackground();
        initPlayer();
    }

    //initialize physics method.
    @Override
    protected void initPhysics() {
        //sets lifeImage to the life.png image.
        var lifeImage = getAssetLoader().loadTexture("life.png");
        //collision handler for the player and a wall/pillar.
        onCollisionBegin(PLAYER, WALL, (player, wall) -> {
            //shows game over if the player has 0 lives.
            if (lives == 0) {
                showGameOver(false);
            //or if the player has collected a power up for a life, it will let the player go through the pillar but remove the life.
            } else {
                lives = 0;
                lifeImage.setVisible(false);
            }
        });
        //collision handler for the player and the finish line.
        onCollisionBegin(PLAYER, FINISH, (player, finish) -> {
            //runs show game over method, sets various variables to defaults to prepare for next level or end of game.
            showGameOver(true);
            firstTime = true;
            lives = 0;
            lifeImage.setVisible(false);
        });
        //collision handler for the player and the slow power up.
        onCollisionBegin(PLAYER, SLOW, (player, slow) -> {
            //runs method to edit speed of player and sets a boolean to true.
            playerComponent.slowHit();
            removeSlow = true;
        });
        //collision handler for the player and life power up.
        onCollisionBegin(PLAYER, LIFE, (player, life) -> {
            //changes various variables, and sets the image of the life/heart in the top left to show that the player has an extra life.
            removeLife = true;
            lives = 1;
            lifeImage.setTranslateX(-360);
            lifeImage.setTranslateY(-360);
            lifeImage.setScaleX(.06);
            lifeImage.setScaleY(.06);
            addUINode(lifeImage);
        });
    }

    //initialize UI method.
    @Override
    protected void initUI() {
        //sets the text "Score" at a certain position, size, etc.
        Text uiScore = new Text("");
        uiScore.setFont(Font.font(50));
        uiScore.setTranslateX(15);
        uiScore.setTranslateY(210);
        uiScore.fillProperty().set(Color.RED);
        uiScore.textProperty().set("Score: ");
        addUINode(uiScore);
        //sets the text for the actual changing score at a certain position, size, etc.
        Text uiScoreChanging = new Text("");
        uiScoreChanging.setFont(Font.font(50));
        uiScoreChanging.setTranslateX(165);
        uiScoreChanging.setTranslateY(210);
        uiScoreChanging.fillProperty().set(Color.RED);
        uiScoreChanging.textProperty().bind(getip("score").asString());
        addUINode(uiScoreChanging);
        //sets the text for the changing level at a certain position, size, etc.
        Text uiLevel = new Text("");
        uiLevel.setFont(Font.font(50));
        uiLevel.setTranslateX(15);
        uiLevel.setTranslateY(130);
        uiLevel.fillProperty().set(Color.RED);
        uiLevel.textProperty().set("Level: " + level);
        addUINode(uiLevel);
        //enables dpad on bottom left of UI.
        Group dpadView = getInput().createVirtualDpadView();
        addUINode(dpadView, -60, 800);
    }

    //onUpdate method.
    @Override
    protected void onUpdate(double tpf) {
        //if the game has just started (score starts at -2 since it adds 2 to score every time in the method, so the score shows at 0 at the start), and if the first time boolean is true.
        if (getScore() == -2 && firstTime) {
            //it displays the level in a message box, and sets first time boolean to false.
            getDialogService().showMessageBox(("Level " + level), getGameController()::resumeEngine);
            firstTime = false;
        }
        //increases score by 2 everytime.
        inc("score", +2);
        //starts new game and sets request new game to false if request new game is true.
        if (requestNewGame) {
            requestNewGame = false;
            getGameController().startNewGame();
        }
    }

    //initialize background method.
    private void initBackground() {
        //creates background rectangle
        Rectangle rect = new Rectangle(getAppWidth(), getAppHeight(), Color.DEEPSKYBLUE);
        //creates background as entity.
        Entity bg = entityBuilder()
            .view(rect)
            .with("rect", rect)
            .buildAndAttach();
        //binds x and y properties to the viewport x and y properties.
        bg.xProperty().bind(getGameScene().getViewport().xProperty());
        bg.yProperty().bind(getGameScene().getViewport().yProperty());
    }

    //initialize player method.
    private void initPlayer() {
        //creates new player component.
        playerComponent = new PlayerComponent();
        //creates player entity, setting position, dimensions, etc.
        Entity player = entityBuilder()
            .at(300, 200)
            .type(PLAYER)
            .bbox(new HitBox(BoundingShape.box(70, 60)))
            //sets the animation for the bird, resulting in it flapping its wings.
            .view(texture("bird.png").toAnimatedTexture(2, Duration.seconds(0.5)).loop())
            .collidable()
            .with(playerComponent, new BuildingComponent())
            .build();
        //sets bounds for game scene view port.
        getGameScene().getViewport().setBounds(0, 0, Integer.MAX_VALUE, getAppHeight());
        getGameScene().getViewport().bindToEntity(player, getAppWidth() / 3, getAppHeight() / 2);
        //spawns player.
        spawnWithScale(player, Duration.seconds(0.86), Interpolators.BOUNCE.EASE_OUT());
    }

    //get score method.
    public int getScore() {
        //returns score.
        return geti("score");
    }

    //show game over method.
    public void showGameOver(boolean levelDone) {
        //if the level is done,
        if (levelDone) {
            //if the level is not 3,
            if (level != 3) {
                //displays that the player has beat the level, and increases the level by 1.
                getDialogService().showMessageBox("You beat level " + level + "!", getGameController()::startNewGame);
                level++;
            //else, if the level is 3,
            } else {
                //displays that the player has beat the game.
                getDialogService().showMessageBox("You beat the game! Your Score: " + (geti("score")), getGameController()::gotoMainMenu);
            }
        //else, if the level isn't done,
        } else {
            //displays that the player died and should restart the level.
            getDialogService().showMessageBox("You died. Your Score: " + (geti("score") + 2) + ". Try the level again!", getGameController()::startNewGame);
        }
    }

    //main method.
    public static void main(String[] args) {
        //launches program.
        launch(args);
    }
}