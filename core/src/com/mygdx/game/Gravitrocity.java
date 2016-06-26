package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import java.util.ArrayList;

public class Gravitrocity extends ApplicationAdapter {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private ArrayList<Mass> masses = new ArrayList<Mass>(0);
    private long time;
    private TimeUtils time_utils = new TimeUtils();
    private boolean clicked = false;
    private float screenHeight, screenWidth;
	
    @Override
    public void create () {
        batch = new SpriteBatch();
        screenHeight = Gdx.graphics.getHeight();
        screenWidth = Gdx.graphics.getWidth();
        //masses.add(new Mass("Yellow",1,new Vector2(250,300),new Vector2(0,-68)));
        time = time_utils.millis();
        camera = new OrthographicCamera(screenHeight, screenWidth);
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        float timeStep = ((float)time_utils.timeSinceMillis(time))/((float)1000);
        time = time_utils.millis();
        
        Mass massesArray[] = masses.toArray(new Mass[masses.size()]);

        for (Mass m : masses)
            m.calculateForce(massesArray);

        for (Mass m : masses) {
            m.stepForward(timeStep);
            m.getSprite().draw(batch);
        }
        
        processInput();

        batch.end();
    }
	
    @Override
    public void dispose () {
        batch.dispose();
    }

    private void processInput() {
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (!clicked || Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                Vector3 input = new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
                camera.unproject(input);
                masses.add(new Mass("Red", 1, new Vector2(input.x,input.y), new Vector2(0,0)));
                clicked = true;
            }
        } else if (clicked) {
            clicked = false;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.zoom += 0.02;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            camera.zoom -= 0.02;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            camera.translate(-3, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            camera.translate(3, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camera.translate(0, -3, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            camera.translate(0, 3, 0);
        }
camera.update();
    }
}
