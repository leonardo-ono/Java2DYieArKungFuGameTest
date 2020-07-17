package core;


import core.Player.State;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author admin
 */
public class View extends Canvas implements Runnable {
    
    private BufferStrategy bs;
    private boolean running;
    
    private BufferedImage offscreen;
    private List<Entity> entities = new ArrayList<>();
    private Sprite background;
    
    private Player player1;
    private PlayerTestAI2 playerAI;
    private Shuriken shuriken;
    
    private Collider playerHitResult = new Collider();
    private Collider playerAIHitResult = new Collider();
    private Collider shurikenHitResult = new Collider();
    private long hitTime;
    private boolean playerHit;
    private boolean playerAIHit;
    private boolean shurikenHit;
    
    private Sprite hitSprite;
    
    public View() {
        offscreen = new BufferedImage(256, 192, BufferedImage.TYPE_INT_RGB);
        background = new Sprite("background", 0, 0);
        hitSprite = new Sprite("hit", 0, 0);
    }
    
    public void start() {
        createBufferStrategy(1);
        bs = getBufferStrategy();
        createAllEntities();
        startAllEntities();
        new Thread(this).start();
        addKeyListener(new Keyboard());
    }

    private void createAllEntities() {
        shuriken = new Shuriken();
        entities.add(player1 = new Player());
        entities.add(playerAI = new PlayerTestAI2(player1, shuriken));
        entities.add(shuriken);
        entities.add(new Hud(player1, playerAI));
    }

    private void startAllEntities() {
        entities.forEach(entity -> entity.start());
    }
    
    @Override
    public void run() {
        running = true;
        while (running) {
            update();
            draw((Graphics2D) offscreen.getGraphics());
            Graphics2D g = (Graphics2D) bs.getDrawGraphics();
            g.scale(2, 2);
            g.drawImage(offscreen, 0, 0, null);
            g.dispose();
            bs.show();
            
            try {
                Thread.sleep(1000 / 30);
            } catch (InterruptedException ex) {
            }
        }
    }

    private void update() {
        if (System.currentTimeMillis() < hitTime) {
            return;
        }
        playerHit = false;
        playerAIHit = false;
        shurikenHit = false;
        
        entities.forEach(entity -> entity.update());
        
        if (player1.state == State.LOSE || playerAI.state == State.LOSE) {
            return;
        }
        
        playerHit = player1.getCurrentFrame().collides((int) player1.x, (int) player1.y, playerAI.getCurrentFrame(), (int) playerAI.x, (int) playerAI.y, playerHitResult);
        playerAIHit = playerAI.getCurrentFrame().collides((int) playerAI.x, (int) playerAI.y, player1.getCurrentFrame(), (int) player1.x, (int) player1.y, playerAIHitResult);
        
        if (!shuriken.isAvailable()) {
            shurikenHit = player1.getCurrentFrame().collides((int) player1.x, (int) player1.y, shuriken.getCurrentFrame(), (int) shuriken.x, (int) shuriken.y, shurikenHitResult);
        }
                
        //if (playerHit && playerAIHit ) {
        //    System.out.println("");
        //    playerHit = player1.getCurrentFrame().collides((int) player1.x, (int) player1.y, playerAI.getCurrentFrame(), (int) playerAI.x, (int) playerAI.y, playerHitResult);
        //    playerAIHit = playerAI.getCurrentFrame().collides((int) playerAI.x, (int) playerAI.y, player1.getCurrentFrame(), (int) player1.x, (int) player1.y, playerAIHitResult);
        //}
        
        if (playerHit) {
            player1.hit();
            if (player1.life == 0) {
                playerAI.win();
            }
            hitTime = System.currentTimeMillis() + 500;
        }

        if (playerAIHit) {
            playerAI.hit();
            if (playerAI.life == 0) {
                player1.win();
            }
            hitTime = System.currentTimeMillis() + 500;
        }
        
        if (shurikenHit) {
            shuriken.destroy();
            player1.hit();
            if (player1.life == 0) {
                playerAI.win();
            }
            hitTime = System.currentTimeMillis() + 500;
        }
        

        if (!player1.flipped && player1.x > playerAI.x) {
            player1.setFlipped(true);
            playerAI.setFlipped(false);
        }

        if (player1.flipped && player1.x < playerAI.x) {
            player1.setFlipped(false);
            playerAI.setFlipped(true);
        }
                
    }
    
    private void draw(Graphics2D g) {
        g.clearRect(0, 0, 256, 240);
        background.draw(g, 0, 0);
        entities.forEach(entity -> entity.draw(g));
        
        if (playerHit) {
            hitSprite.draw(g, playerHitResult.x, playerHitResult.y);
        }

        if (playerAIHit) {
            hitSprite.draw(g, playerAIHitResult.x, playerAIHitResult.y);
        }        

        if (shurikenHit) {
            hitSprite.draw(g, shurikenHitResult.x, shurikenHitResult.y);
        }        
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                View view = new View();
                view.setPreferredSize(new Dimension(256 * 2, 192 * 2));
                JFrame frame = new JFrame();
                frame.setTitle("");
                frame.getContentPane().add(view);
                frame.setResizable(false);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
                view.requestFocus();
                view.start();
            }
        });
    }

}
