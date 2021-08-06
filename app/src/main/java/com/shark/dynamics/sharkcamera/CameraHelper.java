package com.shark.dynamics.sharkcamera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.util.Log;
import android.util.Size;
import android.view.Surface;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CameraHelper {

    private static final String TAG = "Camera";

    private static CameraManager mCameraManager;
    private int mCameraId = 1;
    private List<Size> mOutputSizes;
    private Size mPhotoSize;
    private CameraDevice mCameraDevice;
    private CameraCaptureSession mCaptureSession;
    private CaptureRequest.Builder mPreviewRequestBuilder;
    private CaptureRequest mPreviewRequest;
    private Surface mSurface;
    private SurfaceTexture mSurfaceTexture;

    private GLSurfaceView mGLSurfaceView;
    private CamPreviewRenderer mCamPreviewRenderer;

    private Context mContext;

    public CameraHelper(Context context) {
        mContext = context;
    }

    public void initCamera() {
        mCameraManager = (CameraManager)mContext.getSystemService(Context.CAMERA_SERVICE);
        mOutputSizes = getCameraOutputSizes(mCameraId, SurfaceTexture.class);
        mPhotoSize = mOutputSizes.get(7);
    }

    @SuppressLint("MissingPermission")
    public void openCamera() {
        try {
            mCameraManager.openCamera(String.valueOf(mCameraId), mCameraStateCallback, null);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "openCamera fail : " + e.getMessage());
        }
    }

    public void closeCamera() {
        mCameraDevice.close();
    }

    public void setGLSurfaceView(GLSurfaceView sv) {
        mGLSurfaceView = sv;
    }

    public void setCamPreviewRenderer(CamPreviewRenderer renderer) {
        mCamPreviewRenderer = renderer;
    }

    public List<Size> getCameraOutputSizes(int cameraId, Class<?> clz) {
        try {
            CameraCharacteristics characteristics = mCameraManager.getCameraCharacteristics(String.valueOf(cameraId));
            StreamConfigurationMap configs = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            List<Size> sizes = Arrays.asList(configs.getOutputSizes(clz));
            Collections.sort(sizes, (o1, o2) -> o1.getWidth() * o1.getHeight() - o2.getWidth() * o2.getHeight());
            Collections.reverse(sizes);
            return sizes;
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private final CameraCaptureSession.StateCallback mSessionsStateCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(CameraCaptureSession session) {
            if (null == mCameraDevice) {
                return;
            }

            mCaptureSession = session;
            try {
                mCaptureSession.setRepeatingRequest(mPreviewRequest,
                        null,
                        null);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onConfigureFailed(CameraCaptureSession session) {
        }
    };


    private final CameraDevice.StateCallback mCameraStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            mSurfaceTexture = mCamPreviewRenderer.getSurfaceTexture();
            if (mSurfaceTexture == null) {
                return;
            }

            Log.i(TAG, "size : " + mPhotoSize.getWidth() + " " + mPhotoSize.getHeight());
            mSurfaceTexture.setDefaultBufferSize(mPhotoSize.getWidth(), mPhotoSize.getHeight());
            mSurfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
                @Override
                public void onFrameAvailable(final SurfaceTexture surfaceTexture) {
                    mGLSurfaceView.requestRender();
                }
            });
            mSurface = new Surface(mSurfaceTexture);

            try {
                mCameraDevice = camera;
                mPreviewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                mPreviewRequestBuilder.addTarget(mSurface);
                mPreviewRequest = mPreviewRequestBuilder.build();

                mCameraDevice.createCaptureSession(Arrays.asList(mSurface), mSessionsStateCallback, null);
            } catch (CameraAccessException e) {
                e.printStackTrace();
                Log.e(TAG, "Failed : " + e.getMessage());
            }
        }

        @Override
        public void onDisconnected(CameraDevice camera) {

        }

        @Override
        public void onError(CameraDevice camera, int error) {
            Log.e(TAG, "Open onError : " + error);

        }
    };

}
