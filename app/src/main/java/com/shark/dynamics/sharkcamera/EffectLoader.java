package com.shark.dynamics.sharkcamera;


import com.shark.dynamics.sharkcamera.effect.AnimEffect1;
import com.shark.dynamics.sharkcamera.posteffect.BlurEffect;
import com.shark.dynamics.sharkcamera.posteffect.GrayEffect;

import java.util.ArrayList;
import java.util.List;

public class EffectLoader {

    public static List<EffectItem> loadPrevEffects() {
        List<EffectItem> effects = new ArrayList<>();
        effects.add(new EffectItem("Test", new AnimEffect1()));
        return effects;
    }

    public static List<EffectItem> loadPostEffects() {
        List<EffectItem> pes = new ArrayList<>();
        pes.add(new EffectItem("Gray", new GrayEffect()));
        pes.add(new EffectItem("Blur", new BlurEffect()));
        return pes;
    }

}
