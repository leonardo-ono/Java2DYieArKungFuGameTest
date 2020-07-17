package core;

import core.Collider.Type;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

/**
 *
 * @author admin
 */
public class Player extends Entity {

    public static enum State { WALK_LEFT, WALK_RIGHT, WALKING, JUMPING, CROUCHED, 
        JUMPING_KICK,
        KICKING_STANDING_HIGH, KICKING_STANDING_LOW, KICKING_CROUCHED_LOW, 
        PUNCHING_STANDING_MIDDLE, PUNCHING_CROUCHED_MIDDLE,
        LOSE, WIN,
        TAKE_DISTANCE }
    
    public State state = State.WALKING;
    
    private long attackingTime;
    private boolean jumpKickConsumed;
    
    public int life = 10;

    public boolean needsFlip = false;
    public boolean flipped = false;
    
    public Player() {
    }
    
    public void setFlipped(boolean flipped) {
        if (this.flipped != flipped) {
            this.flipped = flipped;
            needsFlip = true;
        }
    }
    
    @Override
    public void start() {
        Sprite s;
        
        // 0
        s = addFrame("player_0", 10, 33);
        s.addCollider(Type.BODY, 4, 0, 13, 19);
        s.addCollider(Type.BODY, 17, 10, 7, 6);
        s.addCollider(Type.BODY, 2, 20, 18, 13);
            
        // 1
        s = addFrame("player_1", 10, 33);
        s.addCollider(Type.BODY, 4, 0, 13, 32);
        s.addCollider(Type.BODY, 17, 10, 7, 6);

        // 2
        s = addFrame("player_crouched", 12, 25);
        s.addCollider(Type.BODY, 9, 0, 10, 8);
        s.addCollider(Type.BODY, 4, 8, 21, 17);

        // 3
        s = addFrame("player_standing_high_kick", 0, 34);
        s.addCollider(Type.ATTACK, 30, 3, 4, 4);
        s.addCollider(Type.BODY, 3, 0, 5, 33);
        s.addCollider(Type.BODY, 8, 3, 10, 20);
        s.addCollider(Type.BODY, 18, 8, 11, 7);
        
        // 4
        s = addFrame("player_crouched_low_kick", 5, 28);
        s.addCollider(Type.ATTACK, 34, 24, 4, 4);
        s.addCollider(Type.BODY, 1, 1, 15, 27);
        s.addCollider(Type.BODY, 16, 5, 8, 5);
        s.addCollider(Type.BODY, 17, 17, 15, 9);

        // 5
        s = addFrame("player_crouched_middle_punch", 6, 29);
        s.addCollider(Type.ATTACK, 28, 13, 4, 4);
        s.addCollider(Type.BODY, 11, 1, 10, 10);
        s.addCollider(Type.BODY, 10, 11, 17, 18);   
        s.addCollider(Type.BODY, 1, 21, 9, 8);
        
        // 6
        s = addFrame("player_standing_low_kick", 0, 34);
        s.addCollider(Type.ATTACK, 29, 27, 4, 4);
        s.addCollider(Type.BODY, 1, 1, 8, 33);
        s.addCollider(Type.BODY, 9, 2, 7, 23);
        s.addCollider(Type.BODY, 16, 17, 11, 10);
        s.addCollider(Type.BODY, 16, 17, 11, 10);
        s.addCollider(Type.BODY, 16, 4, 8, 7);
        
        // 7
        s = addFrame("player_standing_middle_punch", 6, 34);
        s.addCollider(Type.ATTACK, 26, 13, 4, 4);
        s.addCollider(Type.BODY, 9, 11, 16, 22);
        s.addCollider(Type.BODY, 9, 1, 10, 10);
        s.addCollider(Type.BODY, 2, 25, 7, 8);   
        
        // 8
        s = addFrame("player_jumping_kick", 13, 28);
        s.addCollider(Type.ATTACK, 33, 20, 4, 4);
        s.addCollider(Type.BODY, 8, 1, 12, 26);
        s.addCollider(Type.BODY, 1, 7, 28, 8);
        s.addCollider(Type.BODY, 19, 15, 13, 7);   

        // 9
        addFrame("player_lose_0", 20, 17);
        // 10
        addFrame("player_lose_1", 20, 17);
        
        // 11 
        addFrame("player_win", 10, 33);
        
        x = 100;
        y = 176;
    }

    @Override
    public void update() {
        switch (state) {
            case WALKING: updateWalking(); break;
            case JUMPING: updateJumping(); break;
            case CROUCHED: updateCrouched(); break;
            case JUMPING_KICK: updateJumpingKick(); break;
            case KICKING_STANDING_HIGH: updateKickingStandingHigh(); break;
            case KICKING_STANDING_LOW: updateKickingStandingLow(); break;
            case KICKING_CROUCHED_LOW: updateKickingCrouchedLow(); break;
            case PUNCHING_STANDING_MIDDLE: updatePunchingStandingMiddle(); break;
            case PUNCHING_CROUCHED_MIDDLE: updatePunchingCrouchedMiddle(); break;
            case LOSE: updateLose(); break;
            case WIN: updateWin(); break;
        }

        switch (state) {
            case WALKING: updateWalkingAnimation(); break;
            case JUMPING: updateJumpingAnimation(); break;
            case CROUCHED: break;
            case JUMPING_KICK: break;
            case KICKING_STANDING_HIGH: break;
            case KICKING_STANDING_LOW: break;
            case KICKING_CROUCHED_LOW: break;
            case PUNCHING_STANDING_MIDDLE: break;
            case PUNCHING_CROUCHED_MIDDLE: break;
            case LOSE: updateLose(); break;
        }
        
        if (Keyboard.isKeyPressed(KeyEvent.VK_ENTER)) {
            lose();
        }

        // flip only when he is on floor
        if (needsFlip && y == 176) {
            flipAllFrames(flipped);
            needsFlip = false;
        }
    }

    // ------------------- control

    public void moveStop() {
        vx = 0;
    }

    public void moveLeft() {
        vx = -2;
    }
    
    public void moveRight() {
        vx = 2;
    }
    
    public void jump() {
        vy = -5;
        state = State.JUMPING;
        jumpKickConsumed = false;
    }
    
    public void crouch() {
        state = State.CROUCHED;
        frameIndex = 2;
    }

    public void kickStandingLow() {
        state = State.KICKING_STANDING_LOW;
        attackingTime = System.currentTimeMillis() + 400;
        vx = 0;
    }

    public void kickStandingHigh() {
        state = State.KICKING_STANDING_HIGH;
        attackingTime = System.currentTimeMillis() + 400;
        vx = 0;
    }

    public void punchStandingMiddle() {
        state = State.PUNCHING_STANDING_MIDDLE;
        attackingTime = System.currentTimeMillis() + 400;
        vx = 0;
    }

    private void kickJumping() {
        if (!jumpKickConsumed) {
            state = State.JUMPING_KICK;
            attackingTime = System.currentTimeMillis() + 400;
            jumpKickConsumed = true;
        }
    }

    private void kickCrounchedLow() {
        state = State.KICKING_CROUCHED_LOW;
        attackingTime = System.currentTimeMillis() + 400;
    }

    private void punchCrouchedMiddle() {
        state = State.PUNCHING_CROUCHED_MIDDLE;
        attackingTime = System.currentTimeMillis() + 400;
    }
    
    public void hit() {
        life--;
        if (life == 0) {
            lose();
        }
    }
    
    private void updateLose() {
        vy += 0.2;
        
        updatePosition();
        
        if (y >= 176) {
            y = 176;
            vy = 0;
            vx = 0;
        }
        
        if (frameIndex < 9) {
            frameIndex = 9;
        }
        frameIndex += 0.2;
        if (frameIndex >= 11) {
            frameIndex = 9;
        }
    }


    private void updateWin() {
        vy += 0.2;
        
        updatePosition();
        
        if (y >= 176) {
            y = 176;
            vy = 0;
            vx = 0;
        }
        
        frameIndex = 11;
    }

    public void lose() {
        state = State.LOSE;
        // y = 176;
    }

    public void win() {
        state = State.WIN;
        // y = 176;
    }
    
    // -------------------
    
    private void updateWalking() {
        moveStop();
        
        if (Keyboard.isKeyPressed(KeyEvent.VK_LEFT)) {
            moveLeft();
        }
        else if (Keyboard.isKeyPressed(KeyEvent.VK_RIGHT)) {
            moveRight();
        }

        if (Keyboard.isKeyPressed(KeyEvent.VK_UP)) {
            jump();
        }
        else if (Keyboard.isKeyPressed(KeyEvent.VK_DOWN)) {
            crouch();
        }
        else if (Keyboard.isKeyPressed(KeyEvent.VK_Z) && (Keyboard.isKeyPressed(KeyEvent.VK_LEFT) || Keyboard.isKeyPressed(KeyEvent.VK_RIGHT))) {
            kickStandingLow();
        }
        else if (Keyboard.isKeyPressed(KeyEvent.VK_Z)) {
            kickStandingHigh();
        }
        else if (Keyboard.isKeyPressed(KeyEvent.VK_X)) {
            punchStandingMiddle();
        }
    }

    private void updateWalkingAnimation() {
        if (vx != 0) {
            frameIndex += 0.2;
            if (frameIndex >= 2) {
                frameIndex = 0;
            }
        }
        else {
            frameIndex = 1;
        }
        updatePosition();
    }
    
    private void updateJumping() {
        if (y >= 176) {
            vy = 0;
            y = 176;
            state = State.WALKING;
        }
        
        if (Keyboard.isKeyPressed(KeyEvent.VK_Z)) {
            kickJumping();
        }
    }

    private void updateJumpingAnimation() {
        frameIndex = 2;
        vy += 0.2;
        updatePosition();
    }
    
    private void updateCrouched() {
        if (Keyboard.isKeyPressed(KeyEvent.VK_DOWN)) {
            
            if (Keyboard.isKeyPressed(KeyEvent.VK_Z)) {
                kickCrounchedLow();
            }
            else if (Keyboard.isKeyPressed(KeyEvent.VK_X)) {
                punchCrouchedMiddle();
            }
            
        }
        else {
            state = State.WALKING;
        }
    }
    
    private void updateJumpingKick() {
        vy += 0.2;

        updatePosition();
        
        if (y >= 176) {
            vy = 0;
            y = 176;
            state = State.WALKING;
        }
        else if (System.currentTimeMillis() < attackingTime) {
            frameIndex = 8;
        }
        else {
            state = State.JUMPING;
            frameIndex = 2;
        }
    }
    
    private void updateKickingStandingHigh() {
        if (System.currentTimeMillis() < attackingTime) {
            frameIndex = 3;
        }
        else {
            state = State.WALKING;
            frameIndex = 0;
        }
    }
    
    private void updateKickingStandingLow() {
        if (System.currentTimeMillis() < attackingTime) {
            frameIndex = 6;
        }
        else {
            state = State.WALKING;
            frameIndex = 0;
        }
    }
    
    private void updateKickingCrouchedLow() {
        if (System.currentTimeMillis() < attackingTime) {
            frameIndex = 4;
        }
        else {
            state = State.CROUCHED;
            frameIndex = 2;
        }
    }

    private void updatePunchingStandingMiddle() {
        if (System.currentTimeMillis() < attackingTime) {
            frameIndex = 7;
        }
        else {
            state = State.WALKING;
            frameIndex = 1;
        }
    }


    private void updatePunchingCrouchedMiddle() {
        if (System.currentTimeMillis() < attackingTime) {
            frameIndex = 5;
        }
        else {
            state = State.CROUCHED;
            frameIndex = 2;
        }
    }

    @Override
    public void draw(Graphics2D g) {
        super.draw(g);
    }
    
    
}
