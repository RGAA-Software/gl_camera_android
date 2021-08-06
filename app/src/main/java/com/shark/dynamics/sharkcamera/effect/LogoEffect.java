package com.shark.dynamics.sharkcamera.effect;

import android.opengl.GLES20;
import android.opengl.GLES30;

import com.shark.dynamics.graphics.renderer.r2d.Sprite;
import com.shark.dynamics.graphics.renderer.r2d.anim.FrameAnimation;
import com.shark.dynamics.graphics.renderer.r2d.bezier.BezierPointGenerator;

import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.List;

public class LogoEffect extends IEffect {

    private Sprite mSprite;

    public LogoEffect() {

    }

    @Override
    public void init() {
        mSprite = new Sprite("images/logo.png");
        mSprite.scaleTo(1.0f);

        mInit = true;
    }

    @Override
    public void render(float delta) {
        GLES30.glEnable(GLES20.GL_BLEND);
        GLES30.glBlendFunc(GLES30.GL_SRC_ALPHA, GLES30.GL_ONE_MINUS_SRC_ALPHA);
        mSprite.render(delta);
        GLES30.glDisable(GLES20.GL_BLEND);
    }

    @Override
    public void dispose() {
        mSprite.dispose();
    }
}
