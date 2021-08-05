package com.shark.dynamics.sharkcamera.posteffect;

import com.shark.dynamics.graphics.Director;
import com.shark.dynamics.graphics.shader.Shader;

public class BlurEffect extends IPostEffect {

    @Override
    public void init() {
        String vs = Director.getInstance().loaderShaderFromAssets("shader/base_2d_vs.glsl");
        String fs = Director.getInstance().loaderShaderFromAssets("shader/texture_2d/blur_tex_fs.glsl");
        mShader = new Shader(vs, fs);
        mInit = true;
    }
}
