package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

public class Physics {
    
    /* Gravitational Constant */
    private static final float G = (float)6.67408e2; /* Actual: 6.67408e-11 */

    /* Calculates the force on mass m1 by mass m2 */
    public static Vector2 calculateGravitationalForce(Mass m1, Mass m2) {
        Vector2 displacement = m2.getPosition().sub(m1.getPosition());
        float force = G*m1.getMass()*m2.getMass()/displacement.len();
        Vector2 forceVector = displacement.nor().setLength(force);
        return forceVector;
    }
    
}
