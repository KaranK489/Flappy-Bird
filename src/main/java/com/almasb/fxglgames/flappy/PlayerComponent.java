package com.almasb.fxglgames.flappy;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */

//player component class.
public class PlayerComponent extends Component {
    //sets various variables used later in the program.
    private Vec2 acceleration = new Vec2(0, 0);
    boolean firstTime = true;
    boolean slow = false;
    //onUpdate method.
    @Override
    public void onUpdate(double tpf) {
        //if the level is 1,
        if (FXGL.<FlappyBirdApp>getAppCast().level == 1) {
            //if first time boolean is true,
            if (firstTime){
                //set the acceleration x to 3, and first time boolean to false.
                acceleration.x = 3;
                firstTime = false;
            }
            //if slow boolean is true,
            if (slow){
                //decrease acceleration x by 1, and set slow boolean to false,
                acceleration.x -=1;
                slow = false;
            }
            //multiply acceleration x by .05 and acceleration y by 10.
            acceleration.x += tpf * 0.05;
            acceleration.y += tpf * 10;
        //else, if the level is 2,
        } else if (FXGL.<FlappyBirdApp>getAppCast().level == 2){
            //if first time boolean is true,
            if (firstTime){
                //set the acceleration x to 4, and first time boolean to false.
                acceleration.x = 4;
                firstTime = false;
            }
            //if slow boolean is true,
            if (slow){
                //decrease acceleration x by 1, and set slow boolean to false,
                acceleration.x -=1;
                slow = false;
            }
            //multiply acceleration x by .1 and acceleration y by 9.
            acceleration.x += tpf * 0.1;
            acceleration.y += tpf * 9;
        //else, if the level is 3,
        } else if (FXGL.<FlappyBirdApp>getAppCast().level == 3){
            //if first time boolean is true,
            if (firstTime){
                //set the acceleration x to 5, and first time boolean to false.
                acceleration.x = 5;
                firstTime = false;
            }
            //if slow boolean is true,
            if (slow){
                //decrease acceleration x by 1, and set slow boolean to false,
                acceleration.x -=1;
                slow = false;
            }
            //multiply acceleration x by .15 and acceleration y by 8.
            acceleration.x += tpf * 0.15;
            acceleration.y += tpf * 8;
        }
        //prevents the bird from infinitely increasing y acceleration.
        if (acceleration.y < -5)
            acceleration.y = -5;
        //prevents the bird from infinitely decreasing y acceleration.
        if (acceleration.y > 5)
            acceleration.y = 5;
        //translates entities new acceleration values.
        entity.translate(acceleration.x, acceleration.y);
        //if the entity goes below or above the app height,
        if (entity.getBottomY() > getAppHeight() || entity.getY() < 0) {
            //runs show game over function, since the person has failed the level.
            FXGL.<FlappyBirdApp>getAppCast().showGameOver(false);
        }
    }

    //jump method.
    public void jump() {
        //adds negative y acceleration to manage jump.
        acceleration.addLocal(0, -5);
        //plays sound effect to accompany jump.
        play("jump.wav");
    }

    //slow hit method.
    public void slowHit(){
        //sets slow boolean to true.
        slow = true;
    }
}