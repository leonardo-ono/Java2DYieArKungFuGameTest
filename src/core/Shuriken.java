package core;

import core.Collider.Type;
import java.awt.Graphics2D;

/**
 *
 * @author admin
 */
public class Shuriken extends Entity {
    
    private boolean available;
    
    public Shuriken() {
    }

    public boolean isAvailable() {
        return available;
    }

    @Override
    public void start() {
        Sprite s = addFrame("shuriken_0", 5, 5);
        s.addCollider(Type.ATTACK, 2, 2, 6, 6);
        s = addFrame("shuriken_1", 5, 5);
        s.addCollider(Type.ATTACK, 2, 2, 6, 6);
        
        frameIndex = 0;
        y = 155;
        available = true;
    }

    @Override
    public void updateAnimation() {
        frameIndex += 0.5;
        if (frameIndex >= frames.size()) {
            frameIndex = 0;
        }
    }
    
    @Override
    public void update() {
        if (available) {
            return;
        }
        updateAnimation();
        x += vx;
        if (x < 0 || x > 320) {
            available = true;
        }
    }

    @Override
    public void draw(Graphics2D g) {
        if (available) {
            return;
        }
        super.draw(g); //To change body of generated methods, choose Tools | Templates.
    }

    public void throwLeft(double x) {
        this.x = x;
        vx = -4;
        available = false;
    }

    public void throwRight(double x) {
        this.x = x;
        vx = 4;
        available = false;
    }

    public void destroy() {
        available = true;
    }
    
}
