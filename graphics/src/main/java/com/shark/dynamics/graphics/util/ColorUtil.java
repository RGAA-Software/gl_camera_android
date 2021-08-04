package com.shark.dynamics.graphics.util;

import org.joml.Vector3f;

public class ColorUtil {

    public static Vector3f LinearGradient(Vector3f from, Vector3f to, int total, int idx) {
        if (idx >= total) {
            idx = total;
        }
        float percent = idx * 1.0f / total;
        Vector3f result = new Vector3f();
        result.x = calculateDelta(from.x, to.x, percent);
        result.y = calculateDelta(from.y, to.y, percent);
        result.z = calculateDelta(from.z, to.z, percent);
        return result;
    }

    private static float calculateDelta(float valFrom, float valTo, float percent) {
        return valFrom + (valTo - valFrom) * percent;
    }

    public static Vector3f parseColor(int color) {
        Vector3f retColor = new Vector3f();
        retColor.x = (color >> 16 & 0xFF) * 1.0f / 255.0f;
        retColor.y = (color >> 8 & 0xFF) * 1.0f / 255.0f;
        retColor.z = (color >> 0 & 0xFF) * 1.0f / 255.0f;
        return retColor;
    }

}
