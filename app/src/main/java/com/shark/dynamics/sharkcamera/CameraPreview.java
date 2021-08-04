package com.shark.dynamics.sharkcamera;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES11Ext;
import android.opengl.GLES32;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.shark.dynamics.graphics.Director;
import com.shark.dynamics.graphics.renderer.r2d.Sprite;
import com.shark.dynamics.graphics.renderer.texture.Texture;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CameraPreview extends GLSurfaceView implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener {

    private Context mContext;
    private Sprite mSprite;
    private SurfaceTexture mSurfaceTexture;

    private CameraHelper mCameraHelper;
    private DirectDrawer mDirectDrawer;

    public CameraPreview(Context context) {
        super(context);
        init(context);
    }

    public CameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        setEGLContextClientVersion(3);
        setRenderer(this);
        setRenderMode(RENDERMODE_CONTINUOUSLY);
    }

    // renderer begin

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        int texId = createTextureID();
        mSurfaceTexture = new SurfaceTexture(texId);
        mSurfaceTexture.setOnFrameAvailableListener(this);
        mDirectDrawer = new DirectDrawer(texId);

        mCameraHelper.openCamera();

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES32.glViewport(0, 0, width, height);

        if (!mCameraHelper.isPreviewing()) {
            mCameraHelper.startPreview(mSurfaceTexture);
        }

        Director.getInstance().init(mContext, width, height);
        //
        //mSprite = new Sprite("images/background.jpg");
        //mSprite.scaleTo(0.3f);
    }

    float[] mtx = new float[16];

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES32.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT | GLES32.GL_DEPTH_BUFFER_BIT);
        mSurfaceTexture.updateTexImage();

        mSurfaceTexture.getTransformMatrix(mtx);
        mDirectDrawer.draw(mtx);

        //mSprite.render(0);
    }

    // renderer end

    // surface texture begin

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        this.requestRender();
    }

    // surface texture end

    private int createTextureID() {
        int[] texture = new int[1];
        GLES32.glGenTextures(1, texture, 0);
        GLES32.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0]);
        GLES32.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR);
        GLES32.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLES32.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        GLES32.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
        return texture[0];
    }

    // other

    public void setCameraHelper(CameraHelper ch) {
        mCameraHelper = ch;
    }


}
