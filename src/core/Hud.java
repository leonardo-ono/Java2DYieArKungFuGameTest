package core;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author admin
 */
public class Hud extends Entity {

    private Player player1;
    private PlayerTestAI2 playerAI;

    public Hud(Player player1, PlayerTestAI2 playerAI) {
        this.player1 = player1;
        this.playerAI = playerAI;
    }

    @Override
    public void start() {
    }
    
    @Override
    public void update() {
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.setColor(new Color(74, 104, 247, 255));
        int startX = 72 - (int) (7.2 * player1.life);
        g.fillRect(40 + startX, 9, (int) (7.2 * player1.life), 5);
        //g.setColor(Color.RED);
        g.fillRect(144, 9, (int) (7.2 * playerAI.life), 5);
    }
    
}
