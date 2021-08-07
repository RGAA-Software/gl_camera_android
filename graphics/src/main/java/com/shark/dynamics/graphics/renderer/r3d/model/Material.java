package com.shark.dynamics.graphics.renderer.r3d.model;

import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Material {

    public List<ModelTexture> textures;
    public Vector3f color;

    public Material() {
        textures = new ArrayList<>();
    }


}
