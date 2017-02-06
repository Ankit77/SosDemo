package app.sosdemo.videorecorder;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.IOException;

import app.sosdemo.R;

/**
 * Created by indianic on 06/02/17.
 */

public class VideoCaptureActivity extends AppCompatActivity {
    private Camera myCamera;
    private MyCameraSurfaceView myCameraSurfaceView;
    private MediaRecorder mediaRecorder;
    private SurfaceHolder surfaceHolder;
    boolean recording;
    private String ticket;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videocapture);
        //Get Camera for preview
        if (getIntent().getExtras() != null) {
            ticket = getIntent().getExtras().getString("TICKET");
        }
        if (!TextUtils.isEmpty(ticket)) {
            myCamera = getCameraInstance();
            if (myCamera == null) {
                Toast.makeText(VideoCaptureActivity.this,
                        "Fail to get Camera",
                        Toast.LENGTH_LONG).show();
            }

            myCameraSurfaceView = new MyCameraSurfaceView(this, myCamera);
            FrameLayout myCameraPreview = (FrameLayout) findViewById(R.id.videoview);
            myCameraPreview.addView(myCameraSurfaceView);
        } else {
            finish();
        }
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startRecording();
//            }
//        }, 3000);
    }

    private int getCamreaId() {
        int cameraId = -1;
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                return i;

            }
        }
        return -1;
    }

    public void setCameraDisplayOrientation(Activity activity,
                                            int cameraId, android.hardware.Camera camera) {

        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();

        android.hardware.Camera.getCameraInfo(cameraId, info);

        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 91;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    private Camera getCameraInstance() {
        // TODO Auto-generated method stub
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    private void startRecording(Camera camera) {
        if (recording) {
            // stop recording and release camera
            mediaRecorder.stop();  // stop the recording
            releaseMediaRecorder(camera); // release the MediaRecorder object

            //Exit after saved
            finish();
        } else {

            //Release Camera before MediaRecorder start
            // releaseCamera(camera);

            if (!prepareMediaRecorder(camera)) {
                Toast.makeText(VideoCaptureActivity.this,
                        "Fail in prepareMediaRecorder()!\n - Ended -",
                        Toast.LENGTH_LONG).show();
                finish();
            }

            mediaRecorder.start();
            recording = true;
        }
    }

    private boolean prepareMediaRecorder(Camera camera) {
        mediaRecorder = new MediaRecorder();
        camera.unlock();
        mediaRecorder.setCamera(camera);

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));

        mediaRecorder.setOutputFile("/sdcard/myvideo.mp4");
//        mediaRecorder.setMaxDuration(10000); // Set max duration 60 sec.
//        mediaRecorder.setMaxFileSize(5000000); // Set max file size 5M

        mediaRecorder.setPreviewDisplay(myCameraSurfaceView.getHolder().getSurface());

        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            releaseMediaRecorder(camera);
            return false;
        } catch (IOException e) {
            releaseMediaRecorder(camera);
            return false;
        }
        return true;

    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaRecorder(myCameraSurfaceView.getmCamera());       // if you are using MediaRecorder, release it first
        releaseCamera(myCameraSurfaceView.getmCamera());              // release the camera immediately on pause event
    }

    private void releaseMediaRecorder(Camera camera) {
        if (mediaRecorder != null) {
            mediaRecorder.reset();   // clear recorder configuration
            mediaRecorder.release(); // release the recorder object
            mediaRecorder = null;
            camera.lock();           // lock camera for later use
        }
    }

    private void releaseCamera(Camera camera) {
        if (camera != null) {
            camera.release();        // release the camera for other applications
        }
    }

    public class MyCameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

        private SurfaceHolder mHolder;
        private Camera mCamera;

        public MyCameraSurfaceView(Context context, Camera camera) {
            super(context);
            mCamera = camera;

            // Install a SurfaceHolder.Callback so we get notified when the
            // underlying surface is created and destroyed.
            mHolder = getHolder();
            mHolder.addCallback(this);
            // deprecated setting, but required on Android versions prior to 3.0
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int weight,
                                   int height) {
            // If your preview can change or rotate, take care of those events here.
            // Make sure to stop the preview before resizing or reformatting it.
            // setCameraDisplayOrientation(VideoCaptureActivity.this, getCamreaId(), mCamera);
            if (mHolder.getSurface() == null) {
                // preview surface does not exist
                return;
            }

            // stop preview before making changes
            try {
                mCamera.stopPreview();
            } catch (Exception e) {
                // ignore: tried to stop a non-existent preview
            }

            // make any resize, rotate or reformatting changes here

            // start preview with new settings
            try {
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();

            } catch (Exception e) {
            }
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // TODO Auto-generated method stub
            // The Surface has been created, now tell the camera where to draw the preview.
            try {
//                setCameraDisplayOrientation(VideoCaptureActivity.this, getCamreaId(), mCamera);
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startRecording(mCamera);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (mediaRecorder != null) {
                                    mediaRecorder.stop();  // stop the recording
                                    releaseMediaRecorder(mCamera);
                                    finish();
                                }
                            }
                        }, 60000);
                    }
                }, 2000);


            } catch (IOException e) {
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // TODO Auto-generated method stub

        }

        public Camera getmCamera() {
            return mCamera;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
