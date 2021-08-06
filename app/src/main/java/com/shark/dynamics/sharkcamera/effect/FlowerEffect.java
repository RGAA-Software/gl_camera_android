package com.shark.dynamics.sharkcamera.effect;

import com.shark.dynamics.graphics.renderer.r2d.particlesystem.ParticleSystem;
import com.shark.dynamics.graphics.renderer.r2d.particlesystem.ParticleType;

import org.joml.Vector3f;

public class FlowerEffect extends IEffect {

    private ParticleSystem mParticleSystem;

    @Override
    public void init() {

        mParticleSystem = new ParticleSystem("images/ying_hua.png");
        mParticleSystem.setParticleType(ParticleType.kSnow);
        mParticleSystem.setGenDuration(300);
        mParticleSystem.setGenParticleCount(1);
        mParticleSystem.setColorOverlay(false);
        mParticleSystem.setTintColor(new Vector3f(1,1,1));
        mParticleSystem.setBaseScale(3.6f);

        mInit = true;
    }

    @Override
    public void render(float delta) {
        if (mParticleSystem != null) {
            mParticleSystem.render(delta);
        }
    }

    @Override
    public void dispose() {
        if (mParticleSystem != null) {
            mParticleSystem.dispose();
        }
    }
}
