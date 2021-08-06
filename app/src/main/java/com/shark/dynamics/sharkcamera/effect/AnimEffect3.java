package com.shark.dynamics.sharkcamera.effect;

import com.shark.dynamics.graphics.renderer.r2d.anim.FrameAnimation;
import com.shark.dynamics.graphics.renderer.r2d.bezier.BezierPointGenerator;

import org.joml.Vector2f;

import java.util.List;

public class AnimEffect3 extends IEffect {

    private FrameAnimation mFrameAnim;

    public AnimEffect3() {

    }

    @Override
    public void init() {

        mFrameAnim = new FrameAnimation("anim/yueliang4_anim.png", 6, 7);
        mFrameAnim.setPerFrameTime(60);
        mFrameAnim.setRunTime(10000);
        mFrameAnim.scaleTo(2.5f);
        mFrameAnim.translateTo(300, 900, 0);
        mFrameAnim.setMovable(false);

        mInit = true;
    }

    @Override
    public void render(float delta) {
        if (mFrameAnim != null) {
            mFrameAnim.render(delta);
        }
    }

    @Override
    public void dispose() {
        if (mFrameAnim != null) {
            mFrameAnim.dispose();
        }
    }
}
