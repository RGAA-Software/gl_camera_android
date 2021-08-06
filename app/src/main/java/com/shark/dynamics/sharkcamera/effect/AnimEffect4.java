package com.shark.dynamics.sharkcamera.effect;

import com.shark.dynamics.graphics.renderer.r2d.anim.FrameAnimation;

public class AnimEffect4 extends IEffect {

    private FrameAnimation mFrameAnim;

    public AnimEffect4() {

    }

    @Override
    public void init() {

        mFrameAnim = new FrameAnimation("anim/changjinglu_1_anim.png", 2, 1);
        mFrameAnim.setPerFrameTime(60);
        mFrameAnim.setRunTime(10000);
        mFrameAnim.scaleTo(1.5f);
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
