package com.shark.dynamics.sharkcamera.posteffect;

import android.opengl.GLES20;
import android.opengl.GLES32;

import com.shark.dynamics.graphics.Director;
import com.shark.dynamics.graphics.renderer.framebuffer.FrameBuffer;
import com.shark.dynamics.graphics.renderer.texture.Image;
import com.shark.dynamics.graphics.renderer.texture.Texture;
import com.shark.dynamics.graphics.shader.Shader;

public class LUTEffect2 extends IPostEffect {

    private static final String TAG = "Expand";

    private float mTimeLapses;

    private Texture mTexture;

    public LUTEffect2() {

    }

    @Override
    public void init(FrameBuffer frameBuffer) {
        super.init(frameBuffer);

        String vs = Director.getInstance().loaderShaderFromAssets("shader/base_2d_vs.glsl");
        String fs = Director.getInstance().loaderShaderFromAssets("shader/texture_2d/lut1_tex_fs.glsl");
        mShader = new Shader(vs, fs);

        Image image = Director.getInstance().getImageLoader().loadFromAssets("images/purity.png", false);
        mTexture = new Texture(image);

        mInit = true;
    }

    @Override
    public void updateParams(float delta) {
        super.updateParams(delta);
        if (mShader == null) {
            return;
        }

        GLES32.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, mTexture.getTextureId());
        mShader.setUniformInt("lut", 1);

    }

    @Override
    public void render(float delta) {
        super.render(delta);

        float speed = 2.0f;
        mTimeLapses += delta*speed;

    }
}
