package com.shark.dynamics.sharkcamera;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.Surface;

import androidx.core.app.ActivityCompat;

import com.shark.dynamics.sharkcamera.effect.TestEffect;
import com.shark.dynamics.sharkcamera.posteffect.BlurEffect;
import com.shark.dynamics.sharkcamera.posteffect.GrayEffect;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class CamActivity extends Activity {
    private static final String TAG = "Camera";

    private static final int kPermCode = 10;

    private static CameraManager mCameraManager;
    private int mCameraId = 1;
    private List<Size> mOutputSizes;
    private Size mPhotoSize;
    private CameraDevice mCameraDevice;
    private CameraCaptureSession mCaptureSession;
    private CaptureRequest.Builder mPreviewRequestBuilder;
    private CaptureRequest mPreviewRequest;
    private Surface mSurface;
    private GLSurfaceView mGLSurfaceView;
    private SurfaceTexture mSurfaceTexture;
    private CamPreviewRenderer mCamPreviewRenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViews();
        requestPermission();
    }

    private void requestPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, kPermCode);
        } else {
            initCamera();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == kPermCode && grantResults != null && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initCamera();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        openCamera();
    }

    private void setupViews() {
        mGLSurfaceView = new GLSurfaceView(this);
        mGLSurfaceView.setEGLContextClientVersion(3);
        mCamPreviewRenderer = new CamPreviewRenderer(this);
        mCamPreviewRenderer.addEffect(new TestEffect());

        mCamPreviewRenderer.setPostEffect(new GrayEffect());
        mCamPreviewRenderer.setPostEffect(new BlurEffect());

        mGLSurfaceView.setRenderer(mCamPreviewRenderer);
        setContentView(mGLSurfaceView);
    }

    private final CameraCaptureSession.StateCallback nSessionsStateCallback = new CameraCaptureSession.StateCallback() {
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

                mCameraDevice.createCaptureSession(Arrays.asList(mSurface), nSessionsStateCallback, null);
            } catch (CameraAccessException e) {
                e.printStackTrace();
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

    private void initCamera() {
        mCameraManager = (CameraManager)this.getSystemService(Context.CAMERA_SERVICE);
        mOutputSizes = getCameraOutputSizes(mCameraId, SurfaceTexture.class);
        mPhotoSize = mOutputSizes.get(7);
    }

    @SuppressLint("MissingPermission")
    private void openCamera() {
        try {
            mCameraManager.openCamera(String.valueOf(mCameraId), mCameraStateCallback, null);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "openCamera fail : " + e.getMessage());
        }
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
}
