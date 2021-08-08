package com.shark.dynamics.sharkcamera.posteffect;

import com.shark.dynamics.graphics.Director;
import com.shark.dynamics.graphics.renderer.framebuffer.FrameBuffer;
import com.shark.dynamics.graphics.shader.Shader;

public class ShakeEffect extends IPostEffect {

    private float mTexOffset;

    @Override
    public void init(FrameBuffer frameBuffer) {
        super.init(frameBuffer);
        String vs = Director.getInstance().loaderShaderFromAssets("shader/base_2d_vs.glsl");
        String fs = Director.getInstance().loaderShaderFromAssets("shader/texture_2d/shake_tex_fs.glsl");
        mShader = new Shader(vs, fs);
        mInit = true;
    }

    @Override
    public void updateParams(float delta) {
        super.updateParams(delta);
        if (mShader == null) {
            return;
        }

        mTexOffset += delta/35.0f;

        mShader.setUniformFloat("texOffset", mTexOffset);

        if (mTexOffset > 0.01250f) {
            mTexOffset = 0.0f;
        }
    }
}
