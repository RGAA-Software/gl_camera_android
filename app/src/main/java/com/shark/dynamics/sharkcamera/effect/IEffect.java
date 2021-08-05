package com.shark.dynamics.sharkcamera.effect;

public abstract class IEffect {

    protected boolean mInit = false;

    public void render(float delta) {

    }

    public abstract void init();

    public boolean isInit() {
        return mInit;
    }

}
