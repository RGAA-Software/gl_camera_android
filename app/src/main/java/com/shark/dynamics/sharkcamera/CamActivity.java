package com.shark.dynamics.sharkcamera;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Camera;
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
import android.os.Handler;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shark.dynamics.basic.rv.RView;
import com.shark.dynamics.sharkcamera.effect.AnimEffect1;
import com.shark.dynamics.sharkcamera.effect.AnimEffect2;
import com.shark.dynamics.sharkcamera.effect.AnimEffect3;
import com.shark.dynamics.sharkcamera.effect.BigImageTestEffect;
import com.shark.dynamics.sharkcamera.effect.LogoEffect;
import com.shark.dynamics.sharkcamera.effect.SnowEffect;
import com.shark.dynamics.sharkcamera.posteffect.BlurEffect;
import com.shark.dynamics.sharkcamera.posteffect.GrayEffect;
import com.shark.dynamics.sharkcamera.posteffect.NoneEffect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class CamActivity extends Activity {
    private static final String TAG = "Camera";

    private static final int kPermCode = 10;

    private GLSurfaceView mGLSurfaceView;
    private CamPreviewRenderer mCamPreviewRenderer;

    private LogoEffect mLogoEffect;

    private CameraHelper mCameraHelper;

    // prev-effect
    private RecyclerView mPrevEffectRV;
    private EffectAdapter mPrevEffectAdapter;
    private List<EffectItem> mPrevEffects = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCameraHelper = new CameraHelper(this);
        setupViews();
        mCameraHelper.setGLSurfaceView(mGLSurfaceView);
        mCameraHelper.setCamPreviewRenderer(mCamPreviewRenderer);
        requestPermission();

    }

    private void requestPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, kPermCode);
        } else {
            mCameraHelper.initCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == kPermCode && grantResults != null && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mCameraHelper.initCamera();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCameraHelper.openCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCameraHelper.closeCamera();
    }

    private void setupViews() {
        setContentView(R.layout.activity_preview);
        mGLSurfaceView = findViewById(R.id.id_cam_preview);
        mGLSurfaceView.setEGLContextClientVersion(3);

        mCamPreviewRenderer = new CamPreviewRenderer(this);
        mCamPreviewRenderer.setCameraHelper(mCameraHelper);

        mLogoEffect = new LogoEffect();
        mCamPreviewRenderer.addEffect(mLogoEffect);

        mGLSurfaceView.setRenderer(mCamPreviewRenderer);
        mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        // prev effect
        mPrevEffectRV = findViewById(R.id.id_prev_effect);
        mPrevEffectRV.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        mPrevEffects.addAll(EffectLoader.loadPrevEffects());
        mPrevEffectAdapter = new EffectAdapter(this, mPrevEffects);
        mPrevEffectRV.setAdapter(mPrevEffectAdapter);
        mPrevEffectAdapter.setOnItemClickListener(new RView.OnItemClickListener() {
            @Override
            public void onItemClick(View rootView, int position) {
                EffectItem item = mPrevEffects.get(position);
                mCamPreviewRenderer.clearEffects();
                mCamPreviewRenderer.addEffect(mLogoEffect);
                mCamPreviewRenderer.addEffect(item.e);
            }
        });

    }



}
