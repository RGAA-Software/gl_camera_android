package com.shark.dynamics.sharkcamera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.view.Surface;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CameraHelper2 {

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

    private ImageReader mImageReader;
    private IPrevSizeCallback mPrevSizeCallback;
    private IDataCallback mDataCallback;
    private Context mContext;
    private Handler mBackgroundHandler;

    public CameraHelper2(Context context) {
        mContext = context;
        HandlerThread ht = new HandlerThread("bg");
        ht.start();
        mBackgroundHandler = new Handler(ht.getLooper());
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

            Log.i(TAG, "preview, size : " + mPhotoSize.getWidth() + " " + mPhotoSize.getHeight());
            mPrevSizeCallback.onPrevSize(mPhotoSize.getWidth(), mPhotoSize.getHeight());
            mImageReader = ImageReader.newInstance(mPhotoSize.getWidth(), mPhotoSize.getHeight(), ImageFormat.YUV_420_888, 2);
            mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    Image image = reader.acquireLatestImage();
                    if (image == null) {
                        return;
                    }

                    Image.Plane[] planes = image.getPlanes();
                    if (mDataCallback != null) {
                        mDataCallback.onDataCallback(planes[0].getBuffer(), planes[1].getBuffer(), planes[2].getBuffer());
                    }

                    image.close();
                }
            }, mBackgroundHandler);
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
                //mPreviewRequestBuilder.addTarget(mImageReader.getSurface());
                mPreviewRequest = mPreviewRequestBuilder.build();

                //mCameraDevice.createCaptureSession(Arrays.asList(mSurface, mImageReader.getSurface()), mSessionsStateCallback, mBackgroundHandler);
                mCameraDevice.createCaptureSession(Arrays.asList(mSurface), mSessionsStateCallback, mBackgroundHandler);
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

    public void setPrevSizeCallback(IPrevSizeCallback cb) {
        mPrevSizeCallback = cb;
    }

    public void setDataCallback(IDataCallback cb) {
        mDataCallback = cb;
    }

    public interface IPrevSizeCallback {
        void onPrevSize(int w, int h);
    }

    public interface IDataCallback {
        void onDataCallback(ByteBuffer y, ByteBuffer u, ByteBuffer v);
    }
}
