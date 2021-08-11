package com.shark.dynamics.sharkcamera;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLES31Ext;
import android.opengl.GLES32;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.shark.dynamics.graphics.Director;
import com.shark.dynamics.graphics.renderer.ErrorCheck;
import com.shark.dynamics.graphics.renderer.framebuffer.FrameBuffer;
import com.shark.dynamics.graphics.renderer.r2d.Sprite;
import com.shark.dynamics.graphics.renderer.texture.Texture;
import com.shark.dynamics.graphics.shader.Shader;
import com.shark.dynamics.graphics.util.GLUtil;
import com.shark.dynamics.sharkcamera.effect.IEffect;
import com.shark.dynamics.sharkcamera.posteffect.IPostEffect;

import org.joml.Vector2f;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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

    private FrameBuffer mEffectFrameBuffer;
    private final List<IEffect> mEffects = new ArrayList<>();

    private IPostEffect mPostEffect = null;
    private Sprite mPostSprite;

    private CameraHelper2 mCameraHelper;
    private GLSurfaceView mGLSurfaceView;

    private int mPrevWidth;
    private int mPrevHeight;

    public CamPreviewRenderer(Context context) {
        mContext = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES32.glBindVertexArray(0);
        mSurfaceTextureId = genExternalTexture();
        mSurfaceTexture = new SurfaceTexture(mSurfaceTextureId);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.i(TAG, "onSurfaceChanged, width : " + width + " height : " + height);

        GLES30.glViewport(0, 0, width, height);

        Director.getInstance().init(mContext, width, height);

        GLES30.glBindVertexArray(0);
        //mCamSprite = new CamSprite(mSurfaceTextureId, width, height);
        mCamSprite = new CamSprite(-1, width, height);

        mEffectFrameBuffer = new FrameBuffer();
        mEffectFrameBuffer.init(width, height);

        mPostSprite = new Sprite(
                new Texture(mEffectFrameBuffer.getFrameBufferTexId(), width, height), Sprite.SpriteType.kRect);
        Vector2f sc = Director.getInstance().getDevice().getScreenUsableSize();

        ErrorCheck.checkError();
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        initEffectsIfNeeded();
        initPostEffectsIfNeeded();
        //genTextureIfNeeded();

        mEffectFrameBuffer.begin();

        GLES30.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);

        if (mLastRenderTime == 0) {
            mLastRenderTime = System.currentTimeMillis();
        }
        long currentTime = System.currentTimeMillis();
        float delta = currentTime - mLastRenderTime;
        delta = delta*1.0f/1000;

        //updateYUVDataIfNeeded();
        GLES32.glBindVertexArray(0);
        mSurfaceTexture.updateTexImage();
        mSurfaceTexture.getTransformMatrix(mCamTransformMatrix);
        mCamSprite.updateTransformMatrix(mCamTransformMatrix);

        mCamSprite.render();
        renderEffects(delta);
        mEffectFrameBuffer.end();

        GLES30.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);

        synchronized (CamPreviewRenderer.class) {
            if (mPostEffect != null) {
                Shader shader = mPostEffect.getShader();
                if (shader == null || !mPostEffect.isInit()) {
                    mPostEffect.init(mEffectFrameBuffer);
                }
                if (shader != null) {
                    mPostSprite.updateShader(shader);
                    updatePostParams(delta);
                }
            }
        }

        mPostSprite.render(delta);
        renderPostEffects(delta);

        mLastRenderTime = currentTime;

        GLES32.glBindVertexArray(0);
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

    private void initEffectsIfNeeded() {
        synchronized (this) {
            for (IEffect effect : mEffects) {
                if (!effect.isInit()) {
                    effect.init();
                }
            }
        }
    }

    private void renderEffects(float delta) {
        synchronized (this) {
            for (IEffect e : mEffects) {
                e.render(delta);
            }
        }
    }

    public void addEffect(IEffect effect) {
        synchronized (this) {
            mEffects.add(effect);
        }
    }

    public void clearEffects() {
        synchronized (this) {
            mEffects.clear();
        }
    }

    // post effect

    private void initPostEffectsIfNeeded() {
        synchronized (CamPreviewRenderer.class) {
            if (mPostEffect != null && !mPostEffect.isInit()) {
                mPostEffect.init(mEffectFrameBuffer);
            }
        }
    }

    public void setPostEffect(IPostEffect pe) {
        synchronized (CamPreviewRenderer.class) {
            mPostEffect = pe;
        }
    }

    public void renderPostEffects(float delta) {
        if (mPostEffect != null) {
            mPostEffect.render(delta);
        }
    }

    public void updatePostParams(float delta) {
        if (mPostEffect != null) {
            mPostEffect.updateParams(delta);
        }
    }

    public void setCameraHelper(CameraHelper2 ch) {
        mCameraHelper = ch;
        mCameraHelper.setPrevSizeCallback(new CameraHelper2.IPrevSizeCallback() {
            @Override
            public void onPrevSize(int w, int h) {
                mPrevWidth = w;
                mPrevHeight = h;
                Log.i(TAG, "prev width : " + mPrevWidth + " " + mPrevHeight);
            }
        });
        mCameraHelper.setDataCallback(new CameraHelper2.IDataCallback() {
            @Override
            public void onDataCallback(ByteBuffer y, ByteBuffer u, ByteBuffer v) {
                Log.i(TAG, "y length : " + y.capacity());
                synchronized (mBufferLock) {
                    if (mYTextureBuffer == null) {
                        return;
                    }
                    mYTextureBuffer.position(0);
                    mYTextureBuffer.put(y.duplicate());
                }
            }
        });
    }

    private int mYTexture = -1;
    private ByteBuffer mYTextureBuffer;
    private final Object mBufferLock = new Object();

    private void genTextureIfNeeded() {
        if (mYTexture != -1) {
            return;
        }
        mYTextureBuffer = ByteBuffer.allocate(mPrevHeight * mPrevWidth)
                .order(ByteOrder.nativeOrder());
        mYTexture = GLUtil.genTexture();
        GLES32.glBindTexture(GLES20.GL_TEXTURE_2D, mYTexture);
        GLES32.glTexImage2D(GLES32.GL_TEXTURE_2D, 0, GLES32.GL_RED, mPrevWidth, mPrevHeight, 0, GLES32.GL_RED, GLES32.GL_UNSIGNED_BYTE, mYTextureBuffer);
        GLES30.glTexParameterf(GLES32.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST);
        GLES30.glTexParameterf(GLES32.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);
        GLES30.glTexParameterf(GLES32.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE);
        GLES30.glTexParameterf(GLES32.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE);
        GLES32.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

    private void updateYUVDataIfNeeded() {
        if (mYTexture == -1) {
            return;
        }

        Shader shader = mCamSprite.getShader();
        shader.use();

        GLES32.glActiveTexture(GLES20.GL_TEXTURE1);
        shader.setUniformInt("yImage", 1);
        GLES32.glBindTexture(GLES20.GL_TEXTURE_2D, mYTexture);
        synchronized (mBufferLock) {
            mYTextureBuffer.position(0);
            GLES32.glTexImage2D(GLES32.GL_TEXTURE_2D, 0, GLES32.GL_RED, mPrevWidth, mPrevHeight, 0, GLES32.GL_RED, GLES32.GL_UNSIGNED_BYTE, mYTextureBuffer);
        }
    }

    public void setSurfaceView(GLSurfaceView view) {
        mGLSurfaceView = view;
    }

    public void setPrevSize(int width, int height) {
        mPrevWidth = width;
        mPrevHeight = height;
    }

}
