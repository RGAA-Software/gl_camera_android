package com.shark.dynamics.sharkcamera;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.util.Log;
import android.util.Size;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.List;

public class CameraHelper {

    private static final String TAG = "Camera";

    private Camera mCamera;

    public CameraHelper() {

    }

    public void openCamera(SurfaceHolder holder) {
        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
        Camera.Parameters cp = mCamera.getParameters();
        List<Camera.Size> sizes = cp.getSupportedPreviewSizes();
        Camera.Size size = sizes.get(sizes.size()/2);
        cp.setPreviewSize(size.width, size.height);
        cp.setPreviewFormat(ImageFormat.NV21);
        mCamera.setParameters(cp);

        int bufSize = size.width*size.height*3/2;
        mCamera.addCallbackBuffer(new byte[bufSize]);
        mCamera.setPreviewCallback(new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {
                Log.i(TAG, "camera buffer size : " + data.length);
            }
        });
        try {
            mCamera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mCamera.startPreview();
    }

    public void releaseCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
        }
    }


}
