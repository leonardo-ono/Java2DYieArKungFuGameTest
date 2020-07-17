/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.awt.Rectangle;

/**
 *
 * @author admin
 */
public class Collider extends Rectangle {
    
    public static enum Type { ATTACK, BODY }
    public Type type;

    public Collider() {
    }

    public Collider(Type type, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
    
}
