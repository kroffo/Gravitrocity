package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.Gdx;
import java.util.ArrayList;

public class Mass {
    private Sprite sprite;
    private float mass;
    private float scale = (float)0.25;
    private Vector2 velocity;
    private Vector2 position;
    private Vector2 force;
    private boolean fixed;
    
    /* List to hold masses collided with until collision is gone */
    private ArrayList<Mass> colliders;

    public Mass(String color, double mass, Vector2 position, Vector2 velocity, boolean fixed) {
        if (mass == 0)
            mass = 1;
        this.mass = (float)mass;
        this.position = position.cpy();
        this.velocity = velocity.cpy();
        this.colliders = new ArrayList<Mass>();
        this.fixed = fixed;
        String image_name = "RedMass.png";
        if (color.equalsIgnoreCase("red"))
            image_name = "RedMass.png";
        else if (color.equalsIgnoreCase("blue"))
            image_name = "BlueMass.png";
        else if (color.equalsIgnoreCase("yellow"))
            image_name = "YellowMass.png";
        sprite = new Sprite(new Texture(image_name));
        sprite.setScale(scale);
        sprite.setOriginCenter();
        sprite.setPosition(position.x, position.y);
    }

    public void setScale(double scale) {
        this.scale = (float)scale;
        sprite.setScale(this.scale);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public float getMass() {
        return mass;
    }
    
    public Vector2 getPosition() {
        return position.cpy();
    }

    public Vector2 getVelocity() {
        return velocity.cpy();
    }

    /* Calculates the force applied to the mass *
     * This should be done for all masses       *
     * then the stepForward should be called.   *
     * This will ensure a mass does not change  *
     * before other masses take it into account *
     *                                          *
     * masses should be an array of all the     *
     * masses this mass is affected by          *
     */
    public void calculateForce(Mass [] masses) {
        if (!fixed) {
            force = new Vector2(0,0);
            for (Mass m : masses) {
                if (m != this) {
                    // /* Collision Detection */
                    // if (getPosition().sub(m.getPosition()).len() <= (this.getSprite().getWidth()/2 + m.getSprite().getWidth()/2)*0.25) {
                    //     if (!colliders.contains(m))  {
                    //         /* Perfectly Elastic Collision Calculation */
                    //         //velocity = velocity.scl(mass - m.getMass()).add(m.getVelocity().scl(2*m.getMass())).scl(1/(mass + m.getMass()));
                    //         colliders.add(m);
                    //         force = new Vector2(0,0);
                    //         break;
                    //     }
                    // } else if (colliders.contains(m))
                    //     colliders.remove(m);
                    force.add(Physics.calculateGravitationalForce(this,m));
                }
            }
        }
    }

    public void stepForward(float timeStep) {
        if (!fixed) {
            position.x = position.x + velocity.x*timeStep;
            position.y = position.y + velocity.y*timeStep;
            
            sprite.setPosition(position.x, position.y);
            
            Vector2 acceleration = force.scl(1/mass);
            velocity.x = velocity.x  + acceleration.x*timeStep;
            velocity.y = velocity.y + acceleration.y*timeStep;        
        }
    }
}
