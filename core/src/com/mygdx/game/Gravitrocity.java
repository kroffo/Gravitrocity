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
    private ArrayList<Mass> masses;
    private long time;
    private TimeUtils time_utils = new TimeUtils();
    private boolean clicked, settingV;
    private Vector2 vStart;
    private float screenHeight, screenWidth, maxStep;
    
    /* Game mode:
     *   - 0 : Free mode
     */
    private int mode = 0;
	
    @Override
    public void create () {
        batch = new SpriteBatch();
        screenHeight = Gdx.graphics.getHeight();
        screenWidth = Gdx.graphics.getWidth();
        masses = new ArrayList<Mass>(0);
        maxStep = (float)0.0001;
        clicked = false;
        camera = new OrthographicCamera(screenHeight, screenWidth);
        time = time_utils.millis();
    }

    private void loadMode(int mode) {
        this.mode = mode;
        if (mode == 0) {
            maxStep = (float)0.0001;
            time = time_utils.millis();
            masses = new ArrayList<Mass>(0);
        }
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

        float passed = (float)0.0;

        if (timeStep > 0.05) {
            maxStep *= 10;
        }
        
        while (passed < timeStep) {
        
            float delta = timeStep - passed;
            float smallStep = maxStep;
            if (delta < smallStep)
                smallStep = delta;

            for (Mass m : masses)
                m.calculateForce(massesArray);
            
            for (Mass m : masses) {
                m.stepForward(smallStep);
            }
            passed += smallStep;
        }

        for (Mass m : masses) {
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

                if (settingV) {
                    masses.add(new Mass("Yellow", 1, new Vector2(vStart.x-16,vStart.y-11), new Vector2(input.x-vStart.x,input.y-vStart.y), false));
                    settingV = false;
                } else if (!clicked && Gdx.input.isKeyPressed(Input.Keys.V)) {
                    settingV = true;
                    vStart = new Vector2(input.x,input.y);
                } else if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT))
                    masses.add(new Mass("Blue", 1, new Vector2(input.x-16,input.y-11), new Vector2(0,0), true));
                else
                    masses.add(new Mass("Red", 1, new Vector2(input.x-16,input.y-11), new Vector2(0,0), false));
                    
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
        if ((Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) && Gdx.input.isKeyPressed(Input.Keys.R)) {
            // At the moment this will get called several times when Shift + R is pressed because the code executes while the buttons are held down.
            loadMode(mode);
        }
camera.update();
    }
}
