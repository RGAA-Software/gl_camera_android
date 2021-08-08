package com.shark.dynamics.sharkcamera.posteffect;

import com.shark.dynamics.graphics.Director;
import com.shark.dynamics.graphics.renderer.framebuffer.FrameBuffer;
import com.shark.dynamics.graphics.renderer.r2d.Sprite;
import com.shark.dynamics.graphics.renderer.texture.Texture;
import com.shark.dynamics.graphics.shader.Shader;

public class ShakeExpandEffect extends IPostEffect {

    private float mTexOffset;
    private Sprite mSprite;
    private float mTimeLapses;
    private float mAlpha;

    @Override
    public void init(FrameBuffer frameBuffer) {
        super.init(frameBuffer);
        String vs = Director.getInstance().loaderShaderFromAssets("shader/base_2d_vs.glsl");
        String fs = Director.getInstance().loaderShaderFromAssets("shader/texture_2d/shake_tex_fs.glsl");
        mShader = new Shader(vs, fs);

        vs = Director.getInstance().loaderShaderFromAssets("shader/base_2d_vs.glsl");
        fs = Director.getInstance().loaderShaderFromAssets("shader/texture_2d/alpha_tex_fs.glsl");
        mSprite = new Sprite(
                new Texture(mFrameBuffer.getFrameBufferTexId(), mFrameBuffer.getWidth(), mFrameBuffer.getHeight()),
                Sprite.SpriteType.kRect, vs, fs);
        mSprite.enableCenterRotating();

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

    @Override
    public void render(float delta) {
        super.render(delta);

        float speed = 2.0f;
        mTimeLapses += delta*speed;

        mAlpha = Math.max(0.0f, 1.0f - mTimeLapses);

        if (mSprite != null) {
            mSprite.scaleTo(1 + mTimeLapses/speed);

            Shader shader = mSprite.getShader();
            shader.use();
            shader.setUniformFloat("alpha", mAlpha);
            mSprite.render(delta);
        }

        if (mAlpha <= 0) {
            mTimeLapses = 0;
        }
    }
}
