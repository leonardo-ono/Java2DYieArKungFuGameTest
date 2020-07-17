package core;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin
 */
public class Entity {

    public List<Sprite> frames = new ArrayList<>();
    public double frameIndex;

    public double vx;
    public double vy;

    public double x;
    public double y;
    
    public Entity() {
    }
    
    public void flipAllFrames(boolean flipped) {
        frames.forEach(sprite -> sprite.setFlipped(flipped));
    }
            
    public Sprite addFrame(String res, int originX, int originY) {
        Sprite frame = new Sprite(res, originX, originY);
        frames.add(frame);
        return frame;
    }
    
    public void start() {
    }

    public void update() {
    }
    
    public void updateAnimation() {
        frameIndex += 0.2;
        if (frameIndex >= frames.size()) {
            frameIndex = 0;
        }
    }
    
    public void updatePosition() {
        x += vx;
        y += vy;
        
        if (x < 16) {
            x = 16;
            vx *= -1;
        }
        else if (x > 256 - 16) {
            x = 256 - 16;
            vx *= -1;
        }
    }
    
    public void draw(Graphics2D g) {
        frames.get((int) frameIndex).draw(g, (int) x, (int) y);
    }

    public Sprite getCurrentFrame() {
        return frames.get((int) frameIndex);
    }
    
}
