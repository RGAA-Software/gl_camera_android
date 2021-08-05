package com.shark.dynamics.sharkcamera.posteffect;

import com.shark.dynamics.graphics.shader.Shader;

public abstract class IPostEffect {

    protected boolean mInit = false;

    protected Shader mShader;

    public IPostEffect() {

    }

    public abstract void init();

    public boolean isInit() {
        return mInit;
    }

    public Shader getShader() {
        return mShader;
    }

}
