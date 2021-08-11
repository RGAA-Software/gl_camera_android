package com.shark.dynamics.sharkcamera;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

//public class CamGLSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
//
//    private CameraHelper mCameraHelper;
//
//    public CamGLSurfaceView(Context context) {
//        super(context);
//        init();
//    }
//
//    public CamGLSurfaceView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init();
//    }
//
//    private void init() {
//        getHolder().addCallback(this);
//    }
//
//    public void setCameraHelper(CameraHelper helper) {
//        mCameraHelper = helper;
//    }
//
//    @Override
//    public void surfaceCreated(@NonNull SurfaceHolder holder) {
//
//    }
//
//    @Override
//    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
//        mCameraHelper.openCamera(holder);
//    }
//
//    @Override
//    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
//
//    }
//}

public class CamGLSurfaceView extends GLSurfaceView implements GLSurfaceView.Renderer {

    private static final String TAG = "Camera";

    private CameraHelper mCameraHelper;

    public CamGLSurfaceView(Context context) {
        super(context);
        init();
    }

    public CamGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setEGLContextClientVersion(3);
        setRenderer(this);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        getHolder().addCallback(this);
        Log.i(TAG, "holder : " + getHolder());
    }

    public void setCameraHelper(CameraHelper helper) {
        mCameraHelper = helper;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.i(TAG, "changed, holder : " + getHolder());
        mCameraHelper.openCamera(getHolder());
    }

    @Override
    public void onDrawFrame(GL10 gl) {

    }
}
