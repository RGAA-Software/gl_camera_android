package com.shark.dynamics.sharkcamera.effect;

import com.shark.dynamics.graphics.renderer.r2d.Sprite;
import com.shark.dynamics.graphics.renderer.r2d.anim.FrameAnimation;
import com.shark.dynamics.graphics.renderer.r2d.bezier.BezierPointGenerator;

import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.List;

public class AnimEffect2 extends IEffect {

    private FrameAnimation mFrameAnim;

    public AnimEffect2() {

    }

    @Override
    public void init() {

//        mFrameAnim = new FrameAnimation("anim/yueliang4_anim.png", 6, 7);
        mFrameAnim = new FrameAnimation("anim/changjinglu_1_anim.png", 2, 1);
        mFrameAnim.setPerFrameTime(60);
        mFrameAnim.setRunTime(10000);
        //mFrameAnim.scaleTo(1.5f);
        mFrameAnim.translateTo(200, 1300, 0);
        mFrameAnim.setMovable(false);
        //mFrameAnim.setReverseAnim(true);
        //mFrameAnim.setTintColor(new Vector3f(0.8f, 0.9f, 0.2f));
        List<Vector2f> path = BezierPointGenerator.gen3Bezier(new Vector2f(0, 0), new Vector2f(0, 0), new Vector2f(0, 0), new Vector2f(0, 0), 0.001f);
        mFrameAnim.setBezier(path);

        mInit = true;
    }

    @Override
    public void render(float delta) {
        mFrameAnim.render(delta);
    }
}
