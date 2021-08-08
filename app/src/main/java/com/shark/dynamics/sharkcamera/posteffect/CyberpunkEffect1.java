package com.shark.dynamics.sharkcamera.posteffect;

import com.shark.dynamics.graphics.Director;
import com.shark.dynamics.graphics.renderer.framebuffer.FrameBuffer;
import com.shark.dynamics.graphics.shader.Shader;

import java.util.Random;

public class CyberpunkEffect1 extends IPostEffect {

    private static final int kMaxValue = 1000;

    private float mTexOffset;
    private float[] mOffsetMaxValues = new float[kMaxValue];
    private float[] mOffsetValues = new float[kMaxValue];

    @Override
    public void init(FrameBuffer frameBuffer) {
        super.init(frameBuffer);
        String vs = Director.getInstance().loaderShaderFromAssets("shader/base_2d_vs.glsl");
        String fs = Director.getInstance().loaderShaderFromAssets("shader/texture_2d/cbpk1_tex_fs.glsl");
        mShader = new Shader(vs, fs);

        Random random = new Random();
        for (int i = 0; i < mOffsetMaxValues.length; i++) {
            float val = random.nextFloat();
            mOffsetMaxValues[i] = val;
        }

        mInit = true;
    }

    @Override
    public void updateParams(float delta) {
        super.updateParams(delta);
        if (mShader == null) {
            return;
        }

        mTexOffset += delta/15.0f;

        mShader.setUniformFloat("texOffset", mTexOffset);
        mShader.setUniformFloatArray("offsetLines", mOffsetValues);


        for (int i = 0; i < kMaxValue; i++) {
            mOffsetValues[i] += delta/2.0f;
            if (mOffsetValues[i] > mOffsetMaxValues[i]) {
                mOffsetValues[i] = 0;
            }
        }

        if (mTexOffset > 0.02250f) {
            mTexOffset = 0.0f;
        }
    }
}
