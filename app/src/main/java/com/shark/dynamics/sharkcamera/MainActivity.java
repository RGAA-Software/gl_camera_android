package com.shark.dynamics.sharkcamera;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.widget.TextView;

import com.shark.dynamics.sharkcamera.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final int kRequestPermCode = 10;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private Camera camera;

    private ActivityMainBinding binding;

    private CameraPreview mCameraPreview;
    private CameraHelper mCameraHelper;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mCameraPreview = binding.idCameraPreview;
        mCameraHelper = new CameraHelper();
        mCameraPreview.setCameraHelper(mCameraHelper);

        requestPermissions(new String[]{Manifest.permission.CAMERA}, kRequestPermCode);

        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == kRequestPermCode) {
            startCamera();
        }
    }

    private void startCamera() {

    }
}