package com.shark.dynamics.sharkcamera.posteffect;

import com.shark.dynamics.graphics.renderer.framebuffer.FrameBuffer;
import com.shark.dynamics.graphics.shader.Shader;

public abstract class IPostEffect {

    protected boolean mInit = false;

    protected Shader mShader;
    protected FrameBuffer mFrameBuffer;

    public IPostEffect() {

    }

    public void init(FrameBuffer frameBuffer) {
        mFrameBuffer = frameBuffer;
    }

    public boolean isInit() {
        return mInit;
    }

    public Shader getShader() {
        return mShader;
    }

    public void render(float delta) {

    }

    public void updateParams(float delta) {
        if (mShader != null) {
            mShader.use();
        }
    }

}
