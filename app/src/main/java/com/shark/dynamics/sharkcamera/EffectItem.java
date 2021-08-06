package com.shark.dynamics.sharkcamera;

import com.shark.dynamics.sharkcamera.effect.IEffect;
import com.shark.dynamics.sharkcamera.posteffect.IPostEffect;

public class EffectItem {

    public EffectItem(String n, IEffect e) {
        name = n;
        this.e = e;
    }

    public EffectItem(String n, IPostEffect pe) {
        name = n;
        this.pe = pe;
    }

    public String name;
    public IEffect e;
    public IPostEffect pe;
    public boolean selected = false;

}
