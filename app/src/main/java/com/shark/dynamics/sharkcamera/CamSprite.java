package com.shark.dynamics.sharkcamera;

import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLES32;

import com.shark.dynamics.graphics.Director;
import com.shark.dynamics.graphics.shader.Shader;
import com.shark.dynamics.graphics.util.BufferUtil;
import com.shark.dynamics.graphics.util.GLUtil;

import org.joml.Matrix4f;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static android.opengl.GLES20.glUniformMatrix4fv;

public class CamSprite {

    private float[] mTransformMatrix;
    private final int mTextureId;

    private final Shader mShader;
    private int mWidth;
    private int mHeight;

    private Matrix4f mModel = new Matrix4f();
    private float[] mModelBuffer = new float[16];

    private static final int[] kIndices = {
            0,1,2,
            2,3,0,
    };

    public CamSprite(int textureId, int width, int height) {
        GLES30.glBindVertexArray(0);
        mTextureId = textureId;
        mWidth = width;
        mHeight = height;
        mModel.identity();
//        float[] kPos = new float[]{
//                -1.0f, -1.0f,
//                1.0f, -1.0f,
//                1.0f, 1.0f,
//                -1.0f, 1.0f,
//        };
        float[] kPos = new float[]{
                0, 0,
                mWidth, 0,
                mWidth, mHeight,
                0, mHeight,
        };
        FloatBuffer vertexBuffer = BufferUtil.createFloatBuffer(kPos);

        float[] kTex = {
                0.0f, 0.0f,
                1.0f, 0.0f,
                1.0f, 1.0f,
                0.0f, 1.0f,
        };
        FloatBuffer texBuffer = BufferUtil.createFloatBuffer(kTex);

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

        glUniformMatrix4fv(mShader.getUniformLocation("proj"),
                1,
                false,
                Director.getInstance().getOrthographicMatrix());

        mModel.get(mModelBuffer);
        glUniformMatrix4fv(mShader.getUniformLocation("model"),
                1,
                false,
                mModelBuffer, 0);


        if (mTransformMatrix != null) {
            mShader.setUniformMatrix4fv("uCamTexMatrix", mTransformMatrix);
        }
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, kIndices.length, GLES30.GL_UNSIGNED_INT, 0);
    }

    public void updateTransformMatrix(float[] matrix) {
        mTransformMatrix = matrix;
    }

}
