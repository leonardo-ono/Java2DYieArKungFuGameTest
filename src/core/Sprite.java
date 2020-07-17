package core;


import core.Collider.Type;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;


/**
 *
 * @author admin
 */
public class Sprite {
    
    public static boolean DRAW_COLLIDERS = true;
    
    private BufferedImage image;
    public int originX;
    public int originY;
    public List<Collider> colliders = new ArrayList<>();
    public boolean flipped;
    
    public Sprite(String res, int originX, int originY) {
        this.originX = originX;
        this.originY = originY;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/" + res + ".png"));
        } catch (IOException ex) {
            Logger.getLogger(Sprite.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        }
    }

    public boolean isFlipped() {
        return flipped;
    }

    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }
    
    public void draw(Graphics2D g, int x, int y) {
        if (flipped) {
            drawFlipped(g, x, y);
        }
        else {
            drawNormal(g, x, y);
        }
    }

    private void drawFlipped(Graphics2D g, int x, int y) {
        int sx =  x - (image.getWidth() - originX);
        int sy =  y - originY;
        
        int dx1 = sx + image.getWidth();
        int dy1 = sy; 
        int dx2 = sx;
        int dy2 = dy1 + image.getHeight();
        int sx1 = 0;
        int sy1 = 0;
        int sx2 = image.getWidth();
        int sy2 = image.getHeight();
        g.drawImage(image, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
        
        if (DRAW_COLLIDERS) {
            colliders.forEach(
                collider -> {
                    if (collider.type == Type.ATTACK) {
                        g.setColor(Color.RED);
                    }
                    else {
                        g.setColor(Color.BLUE);
                    }
                    // 00000000001111111111 image.width = 20
                    // 01234567890123456789
                    //               ssssss x=14 width=6
                    //               flippedColliderX = image.width - (x + width)    
                    int flippedColliderX = image.getWidth() - (collider.x + collider.width);
                    g.drawRect(sx + flippedColliderX, sy + collider.y, collider.width, collider.height);
                });
        }
    }
    
    private void drawNormal(Graphics2D g, int x, int y) {
        int sx =  x - originX;
        int sy =  y - originY;
        g.drawImage(image, sx, sy, null);
        if (DRAW_COLLIDERS) {
            colliders.forEach(
                collider -> {
                    if (collider.type == Type.ATTACK) {
                        g.setColor(Color.RED);
                    }
                    else {
                        g.setColor(Color.BLUE);
                    }
                    g.drawRect(sx + collider.x, sy + collider.y, collider.width, collider.height);
                });
        }
    }
    
    public void addCollider(Type type, int x, int y, int width, int height) {
        colliders.add(new Collider(type, x, y, width, height));
    }
    
    private final Collider colliderTmp = new Collider();

    public Collider getScreenSpaceCollider(int colliderIndex, int x, int y) {
        Collider collider = colliders.get(colliderIndex);
        if (!flipped) {
            int sx =  x - originX;
            int sy =  y - originY;
            colliderTmp.setBounds(sx + collider.x, sy + collider.y, collider.width, collider.height);
        }
        else {
            int sx =  x - (image.getWidth() - originX);
            int sy =  y - originY;
            int flippedColliderX = image.getWidth() - (collider.x + collider.width);
            colliderTmp.setBounds(sx + flippedColliderX, sy + collider.y, collider.width, collider.height);
        }
        colliderTmp.setType(collider.getType());
        return colliderTmp;
    }
    
    public boolean collides(int bx, int by, Sprite attackSprite, int ax, int ay, Collider colliderResult) {
        for (int aci = 0; aci < attackSprite.colliders.size(); aci++) {
            Collider attack = attackSprite.getScreenSpaceCollider(aci, ax, ay);
            if (attack.type == Type.ATTACK) {
                for (int bci = 0; bci < colliders.size(); bci++) {
                    Collider body = getScreenSpaceCollider(bci, bx, by);
                    if (body.type == Type.BODY) {
                        if (attack.intersects(body)) {
                            colliderResult.setBounds(attack);
                            colliderResult.setType(Type.ATTACK);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
}
