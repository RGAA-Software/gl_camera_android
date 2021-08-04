package com.shark.dynamics.sharkcamera;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;

import java.io.IOException;
import java.util.Optional;

public class CameraHelper {

    private Camera mCamera;

    private boolean mIsPreviewing;

    public CameraHelper() {

    }

    public void openCamera() {
        mCamera = Camera.open();
    }

    public void startPreview(SurfaceTexture texture) {
        try {
            Camera.Parameters cp = mCamera.getParameters();
            mCamera.setPreviewTexture(texture);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCamera.startPreview();
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {

            }
        });
        mIsPreviewing = true;
    }

    public void destroyCamera() {
        if (mCamera == null) {
            return;
        }
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
        mIsPreviewing = false;
    }

    public boolean isPreviewing() {
        return mIsPreviewing;
    }

}
