package app.sosdemo.video.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import app.sosdemo.R;
import app.sosdemo.util.Constant;
import app.sosdemo.video.CameraHelper;
import app.sosdemo.video.CameraWrapper;
import app.sosdemo.video.Util;
import app.sosdemo.video.core.BaseRecorder;
import app.sosdemo.video.core.DefaultVideoRecorder;


public class VideoRecordingActivity extends Activity implements SurfaceHolder.Callback {
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private CameraWrapper mCameraPair;
    private DefaultVideoRecorder mRecorder;

    private TextView tvDuration;
    private long seconds = 0;
    private String ticket;

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_recording_video);

        if (getIntent().getExtras() != null) {
            ticket = getIntent().getExtras().getString("TICKET");

        }
        if (!getCameraObject(true, CameraInfo.CAMERA_FACING_BACK)) {
            Toast.makeText(getBaseContext(),
                    getString(R.string.alert_camera_not_support), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        mSurfaceView = (SurfaceView) findViewById(R.id.surface_camera);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


        tvDuration = (TextView) findViewById(R.id.duration);

        initRecorder();
    }

    private void initRecorder() {
        if (mRecorder != null) {
            mRecorder.release();
        }
        mRecorder = new DefaultVideoRecorder();
        mRecorder.setOutputFile(getVideoFilePath(VideoRecordingActivity.this, ticket));
        mRecorder.bindCamera(mCameraPair);
        mRecorder.setOnRecordingListener(new BaseRecorder.OnRecordingListener() {

            @Override
            public void onPrepared() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        setUIState(false);
                    }
                });
            }

            @Override
            public void onStart() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        setUIState(true);
                    }
                });
            }

            @Override
            public void onError(final String error) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            int errorCode = Integer.valueOf(error);
                            if (errorCode == -1007) {
                                Toast.makeText(getApplicationContext(),
                                        R.string.alert_video_problem, Toast.LENGTH_SHORT).show();
                            }
                            setUIState(false);
                            return;
                        } catch (NumberFormatException e) {
                        }
                    }
                });
            }

            @Override
            public void onFailure() {
                onError(null);
            }

            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        setUIState(false);
                        Intent intent = new Intent();
                        intent.putExtra("FILEPATH", mRecorder.getOutputFile().getAbsolutePath());
                        setResult(2, intent);
                        finish();//finishing activity
                    }
                });
            }

            @Override
            public void onCancel() {
                onFailure();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecorder.startRecording();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mRecorder.isRecording()) {
                            mRecorder.stopRecording(false);

                        }
                    }
                }, Constant.VIDEO_RECORD_TIMELIMIT);
            }
        }, 2000);
    }

    private void prepareRecorder() {

        new Thread() {
            public void run() {
                mRecorder.prepare();
            }

            ;
        }.start();
    }

    private File getVideoFilePath(Context context, String ticket) {
        File f = new File(context.getExternalCacheDir() + File.separator + ticket + Constant.VIDEO_EXTENSION);
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f;
    }

    private void setUIState(boolean recording) {
        if (!recording) {
            seconds = 0;
            mUIHandler.removeCallbacks(mUIRunnable);
            tvDuration.setVisibility(View.GONE);
        } else {
            seconds = 0;
            mUIHandler.postDelayed(mUIRunnable, 0);
            tvDuration.setVisibility(View.VISIBLE);
        }
    }


    private Handler mUIHandler = new Handler();
    private Runnable mUIRunnable = new Runnable() {

        @Override
        public void run() {
            tvDuration.setText(Util.formatDuration(seconds++));
            if (mRecorder != null && seconds >= mRecorder.getMaxDurationInSeconds() &&
                    mRecorder.reachRecorderMaxDuration()) {
                mRecorder.stopRecording(false);

            } else mUIHandler.postDelayed(this, 1000);
        }
    };

    private void switchCamera() {
        //Close current camera firstly
        Camera mCamera = mCameraPair.getCamera();
        mCamera.lock();
        mCamera.stopPreview();
        mCamera.release();

        int camId = mCameraPair.getCameraInfo().facing;
        int toggleCamId = (camId == CameraInfo.CAMERA_FACING_BACK ?
                CameraInfo.CAMERA_FACING_FRONT : CameraInfo.CAMERA_FACING_BACK);
        if (!getCameraObject(false, toggleCamId)) {
            return;
        }

        initRecorder();
        startCameraPreview();
    }

    public View.OnClickListener onClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
//            if (v.getId() == R.id.bt_start_stop) {
//               final String tag = (String) v.getTag();
//                if (mRecorder.isRecording() && tag.toString().equals("stop")) {
//                    mRecorder.stopRecording(false);
//                } else if (tag.toString().equals("start")) {
//
//
//
//                }
//            }
        }
    };


    protected void onStart() {
        super.onStart();

        mSurfaceHolder.addCallback(this);
    }

    ;

    @Override
    protected void onRestart() {
        super.onRestart();
        int cameraId;
        if (mCameraPair != null) {
            cameraId = mCameraPair.getCameraInfo().facing;
        } else cameraId = CameraInfo.CAMERA_FACING_BACK;

        if (!getCameraObject(false, cameraId)) {
            Toast.makeText(getBaseContext(),
                    R.string.alert_camera_not_support, Toast.LENGTH_LONG).show();
            finish();
            return;
        }


        initRecorder();

        if (mSurfaceView.isShown()) {
            //After screen OFF >  screen ON
            startCameraPreview();
        } else {
            //After press HOME
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(recordedVideoPath)) {
            recordedVideoPath = null;
        }
    }

    private static String recordedVideoPath;

    protected void onPause() {
        if (mRecorder.isRecording()) {
            recordedVideoPath = mRecorder.getOutputFile().getAbsolutePath();
            mRecorder.stopRecording(true);
        }
        if (mRecorder != null) {
            mRecorder.release();
        }
        super.onPause();
    }

    ;

    protected void onStop() {
        if (mCameraPair != null) {
            mCameraPair.getCamera().lock();
            mCameraPair.release();
        }
        super.onStop();
    }

    ;

    @Override
    public void onBackPressed() {
        if (!mRecorder.isRecording()) {
            super.onBackPressed();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        holder.setKeepScreenOn(true);
        startCameraPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    private boolean getCameraObject(boolean first, int cameraId) {
        if (first && !CameraHelper.isCameraSupported(this)) return false;

        mCameraPair = CameraHelper.getCamera(cameraId);
        if (mCameraPair == null && Camera.getNumberOfCameras() > 1) {
            int toggleCamId = (cameraId == CameraInfo.CAMERA_FACING_BACK ?
                    CameraInfo.CAMERA_FACING_FRONT : CameraInfo.CAMERA_FACING_BACK);
            mCameraPair = CameraHelper.getCamera(toggleCamId);
        }
        if (mCameraPair != null) {
            CameraHelper.addCameraAttributes(this, mCameraPair);
            return true;
        }
        return false;
    }

    private void startCameraPreview() {
        Camera mCamera = mCameraPair.getCamera();
        if (mCamera != null) {
            mCamera.lock();
            try {
                mCamera.setPreviewDisplay(mSurfaceHolder);
                mCamera.startPreview();
//				CameraHelper.addCameraAutoFocusFeature(mCamera);
            } catch (IOException e) {
                e.printStackTrace();
            }

            prepareRecorder();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
