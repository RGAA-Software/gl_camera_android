package com.shark.dynamics.sharkcamera;


import com.shark.dynamics.sharkcamera.effect.AnimEffect1;
import com.shark.dynamics.sharkcamera.effect.AnimEffect2;
import com.shark.dynamics.sharkcamera.effect.AnimEffect3;
import com.shark.dynamics.sharkcamera.effect.AnimEffect4;
import com.shark.dynamics.sharkcamera.effect.SnowEffect;
import com.shark.dynamics.sharkcamera.posteffect.BlurEffect;
import com.shark.dynamics.sharkcamera.posteffect.GrayEffect;
import com.shark.dynamics.sharkcamera.posteffect.NoneEffect;

import java.util.ArrayList;
import java.util.List;

public class EffectLoader {

    public static List<EffectItem> loadPrevEffects() {
        List<EffectItem> effects = new ArrayList<>();
        effects.add(new EffectItem("Anim1", new AnimEffect1()));
        effects.add(new EffectItem("Anim2", new AnimEffect2()));
        effects.add(new EffectItem("Anim3", new AnimEffect3()));
        effects.add(new EffectItem("Anim4", new AnimEffect4()));
        effects.add(new EffectItem("Snow", new SnowEffect()));
        return effects;
    }

    public static List<EffectItem> loadPostEffects() {
        List<EffectItem> pes = new ArrayList<>();
        pes.add(new EffectItem("None", new NoneEffect()));
        pes.add(new EffectItem("Gray", new GrayEffect()));
        pes.add(new EffectItem("Blur", new BlurEffect()));
        return pes;
    }

}
