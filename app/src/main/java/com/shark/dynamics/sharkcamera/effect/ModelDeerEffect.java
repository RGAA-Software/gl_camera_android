package com.shark.dynamics.sharkcamera.effect;

import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLES32;

import com.shark.dynamics.graphics.Director;
import com.shark.dynamics.graphics.renderer.r2d.Sprite;
import com.shark.dynamics.graphics.renderer.r3d.light.PointLight;
import com.shark.dynamics.graphics.renderer.r3d.model.Model;
import com.shark.dynamics.graphics.renderer.r3d.model.ModelLoader;

import org.joml.Vector3f;

public class ModelDeerEffect extends IEffect {

    private Model mDeer;

    private float mRotate;

    public ModelDeerEffect() {

    }

    @Override
    public void init() {
        mDeer = ModelLoader.loadModel(Director.getInstance().getDevice().getCachePath()+"/deer_r.obj");
        float scale = 1.002f;
        mDeer.scaleTo(scale, scale, scale);
        mDeer.translateTo(0, -2.5f, 0);
        mDeer.rotateTo(-30, 0, 1, 0);
        mDeer.addPointLight(new PointLight(new Vector3f(1.0f, 1.0f, 1.0f), new Vector3f(1f, 1f, 1f)));
        mDeer.addPointLight(new PointLight(new Vector3f(-1.0f, 1.0f, 1.0f), new Vector3f(1f, 1f, 1f)));
        mDeer.setShowPointLights(false);
        mInit = true;

    }

    @Override
    public void render(float delta) {
        GLES32.glEnable(GLES20.GL_DEPTH_TEST);
        GLES32.glClear(GLES20.GL_DEPTH_BUFFER_BIT);
        mRotate += delta * 10;
        if (mDeer != null) {
            mDeer.render(delta);
            mDeer.rotateTo(mRotate, 0, 1, 0);
        }

        GLES32.glDisable(GLES20.GL_DEPTH_TEST);
    }

    @Override
    public void dispose() {
        mDeer.dispose();
    }
}
