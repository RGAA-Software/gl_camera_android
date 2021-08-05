package com.shark.dynamics.sharkcamera;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import com.shark.dynamics.graphics.Director;
import com.shark.dynamics.graphics.renderer.r2d.Sprite;
import com.shark.dynamics.graphics.renderer.r2d.anim.FrameAnimation;
import com.shark.dynamics.graphics.renderer.r2d.bezier.BezierPointGenerator;
import com.shark.dynamics.graphics.renderer.r3d.model.Model;
import com.shark.dynamics.sharkcamera.effect.IEffect;

import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class CamPreviewRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "Render";

    private final Context mContext;

    private long mLastRenderTime = 0;

    private CamSprite mCamSprite;
    private SurfaceTexture mSurfaceTexture;
    private int mSurfaceTextureId;
    private final float[] mCamTransformMatrix = new float[16];

    private final List<IEffect> mEffects = new ArrayList<>();

    public CamPreviewRenderer(Context context) {
        mContext = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mSurfaceTextureId = genExternalTexture();
        mSurfaceTexture = new SurfaceTexture(mSurfaceTextureId);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES30.glViewport(0, 0, width, height);

        Director.getInstance().init(mContext, width, height);

        GLES30.glBindVertexArray(0);
        mCamSprite = new CamSprite(mSurfaceTextureId);

        initEffects();

        GLES30.glBindVertexArray(0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES30.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);

        GLES30.glEnable(GLES20.GL_BLEND);
        GLES30.glBlendFunc(GLES30.GL_SRC_ALPHA, GLES30.GL_ONE_MINUS_SRC_ALPHA);

        if (mLastRenderTime == 0) {
            mLastRenderTime = System.currentTimeMillis();
        }
        long currentTime = System.currentTimeMillis();
        float delta = currentTime - mLastRenderTime;
        delta = delta*1.0f/1000;

        mSurfaceTexture.updateTexImage();
        mSurfaceTexture.getTransformMatrix(mCamTransformMatrix);

        mCamSprite.updateTransformMatrix(mCamTransformMatrix);
        mCamSprite.render();

        renderEffects(delta);

        mLastRenderTime = currentTime;
    }

    public SurfaceTexture getSurfaceTexture() {
        return mSurfaceTexture;
    }

    public int genExternalTexture() {
        int[] texture = new int[1];
        GLES30.glGenTextures(1, texture, 0);
        GLES30.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0]);
        GLES30.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST);
        GLES30.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);
        GLES30.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE);
        GLES30.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE);
        GLES30.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0);
        return texture[0];
    }

    private void initEffects() {
        for (IEffect effect : mEffects) {
            if (!effect.isInit()) {
                effect.init();
            }
        }
    }

    private void renderEffects(float delta) {
        for (IEffect e : mEffects) {
            e.render(delta);
        }
    }

    public void addEffect(IEffect effect) {
        mEffects.add(effect);
    }

    public void clearEffects() {
        mEffects.clear();
    }

}
