package com.shark.dynamics.graphics.renderer;

import android.opengl.GLES32;
import android.util.Log;

public class ErrorCheck {

    private static final String TAG = "GLERR";

    public static void checkError() {
        int error = GLES32.glGetError();
        Log.i(TAG, "error : " + error);
    }

    public static void checkError(Class<?> c) {
        int error = GLES32.glGetError();
        Log.i(TAG, "class : " + c + " error : " + error);
    }

    public static void checkError(String msg) {
        int error = GLES32.glGetError();
        Log.i(TAG, "msg : " + msg + " error : " + Integer.toHexString(error));
    }
}
