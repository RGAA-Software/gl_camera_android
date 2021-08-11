package com.shark.dynamics.sharkcamera;


import com.shark.dynamics.sharkcamera.effect.AnimEffect1;
import com.shark.dynamics.sharkcamera.effect.AnimEffect2;
import com.shark.dynamics.sharkcamera.effect.AnimEffect3;
import com.shark.dynamics.sharkcamera.effect.AnimEffect4;
import com.shark.dynamics.sharkcamera.posteffect.CyberpunkEffect1;
import com.shark.dynamics.sharkcamera.posteffect.CyberpunkEffect2;
import com.shark.dynamics.sharkcamera.posteffect.ExpandEffect;
import com.shark.dynamics.sharkcamera.effect.FlowerEffect;
import com.shark.dynamics.sharkcamera.effect.ModelDeerEffect;
import com.shark.dynamics.sharkcamera.effect.SnowEffect;
import com.shark.dynamics.sharkcamera.posteffect.BlurEffect;
import com.shark.dynamics.sharkcamera.posteffect.CircleEffect;
import com.shark.dynamics.sharkcamera.posteffect.ColorRegionEffect;
import com.shark.dynamics.sharkcamera.posteffect.FourMirrorEffect;
import com.shark.dynamics.sharkcamera.posteffect.GrayEffect;
import com.shark.dynamics.sharkcamera.posteffect.InverseEffect;
import com.shark.dynamics.sharkcamera.posteffect.NoneEffect;
import com.shark.dynamics.sharkcamera.posteffect.PixelEffect;
import com.shark.dynamics.sharkcamera.posteffect.ShakeEffect;
import com.shark.dynamics.sharkcamera.posteffect.ShakeExpandEffect;

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
        effects.add(new EffectItem("Flower", new FlowerEffect()));
        effects.add(new EffectItem("Deer", new ModelDeerEffect()));
        return effects;
    }

    public static List<EffectItem> loadPostEffects() {
        List<EffectItem> pes = new ArrayList<>();
        pes.add(new EffectItem("None", new NoneEffect()));
        pes.add(new EffectItem("Gray", new GrayEffect()));
        pes.add(new EffectItem("Inverse", new InverseEffect()));
        pes.add(new EffectItem("Blur", new BlurEffect()));
        pes.add(new EffectItem("Pixel", new PixelEffect()));
        pes.add(new EffectItem("C-Region", new ColorRegionEffect()));
        pes.add(new EffectItem("Circle", new CircleEffect()));
        pes.add(new EffectItem("4-Mirror", new FourMirrorEffect()));
        pes.add(new EffectItem("Expand", new ExpandEffect()));
        pes.add(new EffectItem("Shake", new ShakeEffect()));
        pes.add(new EffectItem("ShakeExpand", new ShakeExpandEffect()));
        pes.add(new EffectItem("cbpk1", new CyberpunkEffect1()));
        pes.add(new EffectItem("cbpk2", new CyberpunkEffect2()));
        return pes;
    }

}
