package core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author admin
 */
public class Keyboard implements KeyListener {

    public static boolean[] keyPressed = new boolean[256];
    
    public static boolean isKeyPressed(int keyCode) {
        return keyPressed[keyCode];
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keyPressed[e.getKeyCode()] = true;
        
        if (e.getKeyCode() == KeyEvent.VK_C) {
            Sprite.DRAW_COLLIDERS = !Sprite.DRAW_COLLIDERS;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyPressed[e.getKeyCode()] = false;
    }
    
}
