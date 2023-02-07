package com.almasb.fxglgames.flappy;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.CollidableComponent;
import javafx.scene.shape.Rectangle;
import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */

//building component class.
public class BuildingComponent extends Component {

    //various variables used later in the program.
    private double lastWall = 1000;
    private boolean runOnceSlow = true;
    private boolean runOnceLife = true;
    Entity slow;
    Entity life;
    private boolean buildStuff = true;

    //onUpdate method.
    @Override
    public void onUpdate(double tpf) {
        //sets level integer of this class to level integer from flappy bird app class.
        int level = FXGL.<FlappyBirdApp>getAppCast().level;
        //if build stuff boolean is true,
        if (buildStuff) {
            //if the level is 1,
            if (level == 1) {
                //runs build walls method to build 10 walls.
                buildWalls(10);
                //creates finish line entity at the end with specific coordinates based on the level.
                entityBuilder()
                    .type(EntityType.FINISH)
                    .at( 6500, 0)
                    .scale(2,5)
                    .viewWithBBox("finish.jpg")
                    .with(new CollidableComponent(true))
                    .buildAndAttach();
            //else, if the level is 2,
            } else if (level == 2) {
                //runs build walls method to build 20 walls.
                buildWalls(20);
                //creates finish line entity at the end with specific coordinates based on the level.
                entityBuilder()
                    .type(EntityType.FINISH)
                    .at( 11500, 0)
                    .scale(2,5)
                    .viewWithBBox("finish.jpg")
                    .with(new CollidableComponent(true))
                    .buildAndAttach();
            //else, if the level is 3,
            } else if (level == 3){
                //runs build walls method to build 30 walls.
                buildWalls(30);
                //creates finish line entity at the end with specific coordinates based on the level.
                entityBuilder()
                    .type(EntityType.FINISH)
                    .at( 16500, 0)
                    .scale(2,5)
                    .viewWithBBox("finish.jpg")
                    .with(new CollidableComponent(true))
                    .buildAndAttach();
            }
            //sets build stuff boolean to false.
            buildStuff = false;
        }
        //if the remove slow boolean is true in the flappy bird app class,
        if (FXGL.<FlappyBirdApp>getAppCast().removeSlow){
            //sets the boolean to false and removes the slow power up from world, as it has been used by the player.
            FXGL.<FlappyBirdApp>getAppCast().removeSlow = false;
            slow.removeFromWorld();
        }
        //if the remove life boolean is true in the flappy bird app class,
        if (FXGL.<FlappyBirdApp>getAppCast().removeLife){
            //sets the boolean to false and removes the life power up from world, as it has been used by the player.
            FXGL.<FlappyBirdApp>getAppCast().removeLife = false;
            life.removeFromWorld();
        }
    }

    //wallView method.
    private Rectangle wallView(double width, double height) {
        //creates rectangle and creates curves on the rectangle to make it look nicer.
        Rectangle wall = new Rectangle(width, height);
        wall.setArcWidth(15);
        wall.setArcHeight(15);
        wall.fillProperty().bind(FXGL.getWorldProperties().objectProperty("stageColor"));
        return wall;
    }

    //build walls method.
    private void buildWalls(int a) {
        //various variables used later in the program.
        int level = FXGL.<FlappyBirdApp>getAppCast().level;
        double height = FXGL.getAppHeight();
        double distance = height / 2;
        int heightChanger = 0;
        double scale = 0;
        int randomNumSlow = 0;
        int randomNumLife = 0;
        //if the level is 1,
        if (level == 1){
            //sets various variables to specific values corresponding to the level.
            heightChanger = 100;
            scale = .3;
            randomNumSlow = random(1,a);
            boolean test = true;
            //runs a loop to find position for life power up.
            while (test){
                randomNumLife =  random(1,a/2);
                //this makes sure the power ups do not spawn in the same spot and clash with each other.
                if (randomNumLife != randomNumSlow){
                    test = false;
                }
            }
        //else, if the level is 2,
        } else if (level == 2){
            //sets various variables to specific values corresponding to the level.
            heightChanger = 150;
            scale = .2;
            randomNumSlow = random(1,a);
            boolean test = true;
            //runs a loop to find position for life power up.
            while (test){
                randomNumLife =  random(1,a/2);
                //this makes sure the power ups do not spawn in the same spot and clash with each other.
                if (randomNumLife != randomNumSlow){
                    test = false;
                }
            }
        //else, if the level is 3,
        } else if (level == 3){
            //sets various variables to specific values corresponding to the level.
            heightChanger = 200;
            scale = .1;
            randomNumSlow = random(1,a);
            boolean test = true;
            //runs a loop to find position for life power up.
            while (test){
                randomNumLife =  random(1,a/2);
                //this makes sure the power ups do not spawn in the same spot and clash with each other.
                if (randomNumLife != randomNumSlow){
                    test = false;
                }
            }
        }
        //loop that runs a times, which is the parameter passed in based on the level.
        for (int i = 1; i <= a; i++) {
            //sets top height double.
            double topHeight = Math.random() * (height - distance);
            //builds wall entity on the top.
            entityBuilder()
                .at(lastWall + i * 500, -25)
                .type(EntityType.WALL)
                .viewWithBBox(wallView(70, topHeight+heightChanger))
                .with(new CollidableComponent(true))
                .buildAndAttach();
            //builds wall entity on the bottom, with the same y value as the previous wall built.
            entityBuilder()
                .at(lastWall + i * 500, topHeight + distance +25)
                .type(EntityType.WALL)
                .viewWithBBox(wallView(70, height - distance - topHeight))
                .with(new CollidableComponent(true))
                .buildAndAttach();
            //if the slow power up integer is equal to i and the run once slow boolean is true.
            if (randomNumSlow == i  && runOnceSlow){
                //sets run once slow boolean to false.
                runOnceSlow = false;
                //creates slow power up entity, spawning in between the 2 pillars at the given random point.
                slow = entityBuilder()
                    .type(EntityType.SLOW)
                    .at( (lastWall + i * 500)-230.5+40,  ((topHeight+heightChanger-25) + (((topHeight + distance+25) - (topHeight+heightChanger-25))/2))-124)
                    .scale(scale, scale)
                    .viewWithBBox("slow.png")
                    .with(new CollidableComponent(true))
                    .buildAndAttach();
            }
            //if the life power up integer is equal to i and the run once life boolean is true.
            if (randomNumLife == i  && runOnceLife){
                //sets run once life boolean to false.
                runOnceLife = false;
                //creates life power up entity, spawning in between the 2 pillars at the given random point.
                life = entityBuilder()
                    .type(EntityType.LIFE)
                    .at( (lastWall + i * 500)-400+40,  ((topHeight+heightChanger-25) + (((topHeight + distance+25) - (topHeight+heightChanger-25))/2))-400)
                    .scale(scale*0.3, scale*0.3)
                    .viewWithBBox("life.png")
                    .with(new CollidableComponent(true))
                    .buildAndAttach();
            }
        }
        //increases last wall integer by 5000.
        lastWall += 10 * 500;
    }
}