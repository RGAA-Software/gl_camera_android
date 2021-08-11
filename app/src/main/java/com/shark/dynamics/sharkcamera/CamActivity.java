package com.shark.dynamics.sharkcamera;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shark.dynamics.basic.rv.RView;
import com.shark.dynamics.sharkcamera.effect.LogoEffect;

import java.util.ArrayList;
import java.util.List;


public class CamActivity extends Activity {
    private static final String TAG = "Camera";

    private static final int kPermCode = 10;

    private GLSurfaceView mGLSurfaceView;
    private CamPreviewRenderer mCamPreviewRenderer;

    private LogoEffect mLogoEffect;

    private CameraHelper2 mCameraHelper2;
//    private CameraHelper mCameraHelper;

    // prev-effect
    private RecyclerView mPrevEffectRV;
    private EffectAdapter mPrevEffectAdapter;
    private List<EffectItem> mPrevEffects = new ArrayList<>();

    // post-effect
    private RecyclerView mPostEffectRV;
    private EffectAdapter mPostEffectAdapter;
    private List<EffectItem> mPostEffects = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mCameraHelper = new CameraHelper();
        mCameraHelper2 = new CameraHelper2(this);
        setupViews();
        mCameraHelper2.setGLSurfaceView(mGLSurfaceView);
        mCameraHelper2.setCamPreviewRenderer(mCamPreviewRenderer);
        requestPermission();

    }

    private void requestPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, kPermCode);
        } else {
            mCameraHelper2.initCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == kPermCode && grantResults != null && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mCameraHelper2.initCamera();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCameraHelper2.openCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCameraHelper2.closeCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mCameraHelper.releaseCamera();
    }

    private void setupViews() {
        setContentView(R.layout.activity_preview);
        mGLSurfaceView = findViewById(R.id.id_cam_preview);
//        mGLSurfaceView = findViewById(R.id.id_cam_preview_custom);
        mGLSurfaceView.setEGLContextClientVersion(3);

        mCamPreviewRenderer = new CamPreviewRenderer(this);
        mCamPreviewRenderer.setCameraHelper(mCameraHelper2);
        mCamPreviewRenderer.setSurfaceView(mGLSurfaceView);

        mLogoEffect = new LogoEffect();
        mCamPreviewRenderer.addEffect(mLogoEffect);

        mGLSurfaceView.setRenderer(mCamPreviewRenderer);
        mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
//        mGLSurfaceView.setCameraHelper(mCameraHelper);

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
                for (EffectItem it : mPrevEffects) {
                    it.selected = false;
                }
                item.selected = true;
                mPrevEffectAdapter.notifyDataSetChanged();

                mCamPreviewRenderer.clearEffects();
                mCamPreviewRenderer.addEffect(mLogoEffect);
                mCamPreviewRenderer.addEffect(item.e);
            }
        });

        mPostEffectRV = findViewById(R.id.id_post_effect);
        mPostEffectRV.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        mPostEffects.addAll(EffectLoader.loadPostEffects());
        mPostEffectAdapter = new EffectAdapter(this, mPostEffects);
        mPostEffectRV.setAdapter(mPostEffectAdapter);
        mPostEffectAdapter.setOnItemClickListener(new RView.OnItemClickListener() {
            @Override
            public void onItemClick(View rootView, int position) {
                EffectItem item = mPostEffects.get(position);
                for (EffectItem it : mPostEffects) {
                    it.selected = false;
                }
                item.selected = true;
                mPostEffectAdapter.notifyDataSetChanged();

                mCamPreviewRenderer.setPostEffect(item.pe);
            }
        });

    }



}
