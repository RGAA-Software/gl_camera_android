package com.shark.dynamics.sharkcamera;

import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLES32;

import com.shark.dynamics.graphics.Director;
import com.shark.dynamics.graphics.shader.Shader;
import com.shark.dynamics.graphics.util.BufferUtil;
import com.shark.dynamics.graphics.util.GLUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class CamSprite {

    private final IntBuffer mIndexBuffer;
    private float[] mTransformMatrix;
    private final int mTextureId;

    private final Shader mShader;

    private static final int[] kIndices = {
            0,1,2,
            2,3,0,
    };

    public CamSprite(int textureId) {
        mTextureId = textureId;

        float[] kPos = new float[]{
                -1.0f, -1.0f,
                1.0f, -1.0f,
                1.0f, 1.0f,
                -1.0f, 1.0f,
        };
        FloatBuffer vertexBuffer = BufferUtil.createFloatBuffer(kPos);

        float[] kTex = {
                0.0f, 0.0f,
                1.0f, 0.0f,
                1.0f, 1.0f,
                0.0f, 1.0f,
        };
        FloatBuffer texBuffer = BufferUtil.createFloatBuffer(kTex);

        mIndexBuffer = BufferUtil.createIntBuffer(kIndices);

        String vs = Director.getInstance().loaderShaderFromAssets("camera/cam_perv_vs.glsl");
        String fs = Director.getInstance().loaderShaderFromAssets("camera/cam_prev_fs.glsl");
        mShader = new Shader(vs, fs);

        GLES30.glEnableVertexAttribArray(0);
        GLES30.glVertexAttribPointer(0, 2, GLES30.GL_FLOAT, false, 0, vertexBuffer);

        GLES30.glEnableVertexAttribArray(1);
        GLES30.glVertexAttribPointer(1, 2, GLES30.GL_FLOAT, false, 0, texBuffer);

        IntBuffer elemBuffer = BufferUtil.createIntBuffer(kIndices);
        int elemBufferHandle = GLUtil.glGenBuffer();
        GLES32.glBindBuffer(GLES32.GL_ELEMENT_ARRAY_BUFFER, elemBufferHandle);
        GLES32.glBufferData(GLES32.GL_ELEMENT_ARRAY_BUFFER, kIndices.length*4, elemBuffer, GLES32.GL_STATIC_DRAW);

    }

    public void render() {
        mShader.use();
        GLES30.glBindVertexArray(0);

        GLES30.glActiveTexture(GLES30.GL_TEXTURE5);
        GLES30.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mTextureId);
        mShader.setUniformInt("frameImage", 5);

        if (mTransformMatrix != null) {
            mShader.setUniformMatrix4fv("uCamTexMatrix", mTransformMatrix);
        }
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, kIndices.length, GLES30.GL_UNSIGNED_INT, 0);
        GLES30.glBindVertexArray(0);
    }

    public void updateTransformMatrix(float[] matrix) {
        mTransformMatrix = matrix;
    }

}
