package com.shark.dynamics.sharkcamera.posteffect;

import com.shark.dynamics.graphics.Director;
import com.shark.dynamics.graphics.renderer.framebuffer.FrameBuffer;
import com.shark.dynamics.graphics.shader.Shader;

public class PixelEffect extends IPostEffect {

    @Override
    public void init(FrameBuffer frameBuffer) {
        super.init(frameBuffer);
        String vs = Director.getInstance().loaderShaderFromAssets("shader/base_2d_vs.glsl");
        String fs = Director.getInstance().loaderShaderFromAssets("shader/texture_2d/pixel_tex_fs.glsl");
        mShader = new Shader(vs, fs);
        mInit = true;
    }

}
