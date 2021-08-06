package com.shark.dynamics.sharkcamera.effect;

import android.opengl.GLES20;
import android.opengl.GLES30;

import com.shark.dynamics.graphics.renderer.r2d.Sprite;
import com.shark.dynamics.graphics.renderer.r2d.anim.FrameAnimation;
import com.shark.dynamics.graphics.renderer.r2d.bezier.BezierPointGenerator;

import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.List;

public class BigImageTestEffect extends IEffect {

    private Sprite mSprite;

    private FrameAnimation mFrameAnim;

    public BigImageTestEffect() {

    }

    @Override
    public void init() {
        mSprite = new Sprite("anim/changjinglu_1_anim.png");
        mSprite.scaleTo(1.0f);

        mFrameAnim = new FrameAnimation("images/anim.png", 4, 3);
        mFrameAnim.setPerFrameTime(120);
        mFrameAnim.setRunTime(10000);
        mFrameAnim.setReverseAnim(true);
        mFrameAnim.setTintColor(new Vector3f(0.8f, 0.9f, 0.2f));
        mFrameAnim.scaleTo(2.0f);
        List<Vector2f> path = BezierPointGenerator.gen3Bezier(new Vector2f(100, 100), new Vector2f(600, 600), new Vector2f(800, 2000), new Vector2f(1000, 100), 0.001f);
        mFrameAnim.setBezier(path);

        mInit = true;
    }

    @Override
    public void render(float delta) {
        mSprite.render(delta);
        mFrameAnim.render(delta);
    }

    @Override
    public void dispose() {
        mSprite.dispose();
    }
}
