package app.sosdemo.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v13.app.FragmentCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.lkland.util.FileUtils;
import com.lkland.util.Logger;
import com.lkland.videocompressor.compressor.QueuedFFmpegCompressor;
import com.lkland.videocompressor.fragments.IQueueList;
import com.lkland.videocompressor.parser.ProgressPaser;
import com.lkland.videocompressor.responsehandler.OnProgressHandler;
import com.lkland.videocompressor.responsehandler.OnQueueHandler;
import com.lkland.videocompressor.responsemanager.ActivityResponseManager;
import com.lkland.videocompressor.responsemanager.IResponseManager;
import com.lkland.videocompressor.services.AbstractCompressionService;
import com.lkland.videocompressor.services.CompressionService;
import com.lkland.videocompressor.validations.AbstractCompressionOptionsValidator;
import com.lkland.videocompressor.validations.ValidationFactory;
import com.lkland.videocompressor.video.IVideo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import app.sosdemo.KavachApp;
import app.sosdemo.MainActivity;
import app.sosdemo.R;
import app.sosdemo.adapter.DashboardAdapter;
import app.sosdemo.audio.AudioRecorder;
import app.sosdemo.model.ActionModel;
import app.sosdemo.util.Constant;
import app.sosdemo.util.GetFilePath;
import app.sosdemo.util.Utils;
import app.sosdemo.webservice.WSConstants;
import app.sosdemo.webservice.WSGetAlertList;
import app.sosdemo.webservice.WSSOS;
import app.sosdemo.webservice.WSUploadPhoto;
import id.zelory.compressor.Compressor;

/**
 * Created by indianic on 28/01/17.
 */

public class DashboardFragment extends Fragment implements View.OnClickListener, IQueueList, DashboardAdapter.onActionListner, LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private View view;
    //files
    private final int REQUEST_CAPTURE_IMAGE = 111;
    private final int REQUEST_CAPTURE_VIDEO = 222;
    private final int REQUEST_PICK_IMAGE = 333;
    private final int REQUEST_PICK_VIDEO = 444;
    private final int REQUEST_PICK_FILE = 555;
    int PERMISSION_ALL = 1;


    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    protected String filePath = null;
    protected String cameraFilePath;
    private QueuedFFmpegCompressor mCompressor;
    private RecyclerView rvActionList;
    private DashboardAdapter dashboardAdapter;
    private ArrayList<ActionModel> actionList;
    private ProgressDialog progressDialog;
    private AudioRecorder audioRecorder;

    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private AsyncSendSos asyncSendSos;
    private String mCode;
    private String mAction;
    private String TimeStamp;
    private String ticketNumber;
    private AyncLoadActionList ayncLoadActionList;
    private int year;
    private int month;
    private int day;
    ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            Logger.log("");
            AbstractCompressionService service = ((AbstractCompressionService.LocalBinder) binder).getService();
            mCompressor = (QueuedFFmpegCompressor) service.getCompressor();
            IResponseManager arm = new ActivityResponseManager(DashboardFragment.this);
            OnProgressHandler prh = (OnProgressHandler) mCompressor.getOnProgressListener();
            OnQueueHandler qrh = (OnQueueHandler) mCompressor.getOnQueueListener();
            prh.addResponseManager(arm);
            qrh.addResponseManager(arm);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
//			prh.removeResponseManager(ActivityResponseManager.class);
//			qrh.removeResponseManager(ActivityResponseManager.class);
            Logger.log("");

        }

    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard, null);
        init();
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null && !mGoogleApiClient.isConnected())
            mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent intent = new Intent(this.getActivity(), CompressionService.class);
        this.getActivity().bindService(intent, conn, Activity.BIND_AUTO_CREATE);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mCompressor != null) {
            ((OnProgressHandler) mCompressor.getOnProgressListener()).removeResponseManager(ActivityResponseManager.class);
            ((OnQueueHandler) mCompressor.getOnQueueListener()).removeResponseManager(ActivityResponseManager.class);
        }
        if (conn != null)
            this.getActivity().unbindService(conn);
    }


    @Override
    public void queueFinished() {
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });

    }

    @Override
    public void progress(final IVideo video, final String str) {
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ProgressPaser pp = new ProgressPaser();
                pp.parse(str);

            }

        });
    }

    @Override
    public void add(final IVideo video) {
    }

    @Override
    public void remove(final IVideo video) {
    }

    @Override
    public void pop(final IVideo video) {
    }


    private void init() {
        ((MainActivity) getActivity()).setTitle(getString(R.string.lbl_title_dashboard));
        ((MainActivity) getActivity()).isshowBackButton(false);
        ((MainActivity) getActivity()).isMenuButton(true);
        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        rvActionList = (RecyclerView) view.findViewById(R.id.fragment_dashboard_rv_actionlist);
        if (hasPermissions(getActivity(), PERMISSIONS)) {
//            loadDashboardAdapter();
            if (Utils.isNetworkAvailable(getActivity())) {
                ayncLoadActionList = new AyncLoadActionList();
                ayncLoadActionList.execute();
            } else {
                Utils.displayDialog(getActivity(), getString(R.string.app_name), getString(R.string.alret_internet));
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            ((MainActivity) getActivity()).setTitle(getString(R.string.lbl_title_dashboard));
            ((MainActivity) getActivity()).isshowBackButton(false);
            ((MainActivity) getActivity()).isMenuButton(true);
        }
    }

    private void loadDashboardAdapter() {
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
        dashboardAdapter = new DashboardAdapter(getActivity(), actionList, false);
        dashboardAdapter.setOnactionListner(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvActionList.setLayoutManager(mLayoutManager);
        rvActionList.setAdapter(dashboardAdapter);
    }


    private void chooseImageFileFromStorage() {
        Intent intent = Utils.getFileChooserIntent("image/*");
        if (intent != null) {
            startActivityForResult(intent, REQUEST_PICK_IMAGE);
        }

    }

    private void chooseVideoFileFromStorage() {
        Intent intent = Utils.getFileChooserIntent("video/*");
        if (intent != null) {
            startActivityForResult(intent, REQUEST_PICK_VIDEO);
        }
    }

    /**
     * Opens camera activity
     */

    private void captureImage() {
        Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            String timestamp = Utils.getCurrentTimeStamp();
            timestamp = timestamp.replace("-", "");
            timestamp = timestamp.replace(":", "");
            timestamp = timestamp.replace(" ", "");
            Uri outputFileUri = getPostImageUri(true, ticketNumber, timestamp);
            intent1.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            intent1.putExtra("return-data", true);
            startActivityForResult(intent1, REQUEST_CAPTURE_IMAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void captureVideo() {
        Intent intent1 = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        try {
            String timestamp = Utils.getCurrentTimeStamp();
            timestamp = timestamp.replace("-", "");
            timestamp = timestamp.replace(":", "");
            timestamp = timestamp.replace(" ", "");
            Uri outputFileUri = getPostVideoUri(true, ticketNumber, timestamp);
            intent1.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            intent1.putExtra("return-data", true);
            startActivityForResult(intent1, REQUEST_CAPTURE_VIDEO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            if (resultCode == Activity.RESULT_OK) {
                // Callback from Gallery
                if (requestCode == REQUEST_CAPTURE_IMAGE) {
                    try {
                        if (!TextUtils.isEmpty(cameraFilePath)) {
                            final Uri uri = Uri.fromFile(new File(cameraFilePath));
                            filePath = GetFilePath.getPath(getActivity(), uri);
                            if (!TextUtils.isEmpty(filePath)) {

                                File compressedImage = null;
                                //compress image
                                if (Utils.getFileSizeInKB(filePath) > 500) {
                                    compressedImage = new Compressor.Builder(getActivity())
                                            .setMaxWidth(640)
                                            .setMaxHeight(480)
                                            .setQuality(75)
                                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
                                            .setDestinationDirectoryPath(FileUtils.createFolderInExternalStorageDirectory(getString(R.string.app_name) + "/" + Constant.IMAGE_FOLDER_NAME))
                                            .build()
                                            .compressToFile(new File(filePath));
                                } else {
                                    compressedImage = new File(filePath);
                                }

                                if (Utils.isNetworkAvailable(getActivity())) {
                                    new AynsUploadPhoto().execute(compressedImage.getPath(), "http://kawach.ilabindia.com/" + WSConstants.METHOD_FILEUPLOAD, ticketNumber, TimeStamp);

                                    //new AynsUploadPhoto().execute(Environment.getExternalStorageDirectory()+"/test.jpeg", "http://kawach.ilabindia.com/" + WSConstants.METHOD_FILEUPLOAD, ticketNumber, TimeStamp);
                                } else {
                                    Utils.displayDialog(getActivity(), getString(R.string.app_name), getString(R.string.alret_internet));
                                }

                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (requestCode == REQUEST_CAPTURE_VIDEO) {
                    try {

                        if (!TextUtils.isEmpty(cameraFilePath)) {
                            progressDialog = Utils.displayProgressDialog(getActivity());
                            final Uri uri = Uri.fromFile(new File(cameraFilePath));
                            filePath = GetFilePath.getPath(getActivity(), uri);
                            String outPath = FileUtils.createFolderInExternalStorageDirectory(getString(R.string.app_name) + "/" + Constant.VIDEO_FOLDER_NAME);
                            String outName = ticketNumber + "_" + TimeStamp;
                            String outSize = Constant.VIDEO_SIZE;
                            ValidationFactory validationFactory = new ValidationFactory();
                            int ret = validationFactory.getValidator(filePath, outPath, outName, outSize).validate();
                            if (ret != AbstractCompressionOptionsValidator.PASS) {
                                //tvErrorMsg.setText(validationFactory.getErrorMsgPresenter().present(ret));
                                return;
                            }

                            Intent intent = new Intent(DashboardFragment.this.getActivity(), CompressionService.class);
                            intent.putExtra(CompressionService.TAG_ACTION, CompressionService.FLAG_ACTION_ADD_VIDEO);
                            intent.putExtra(CompressionService.TAG_DATA_INPUT_FILE_PATH, filePath);
                            intent.putExtra(CompressionService.TAG_DATA_OUTPUT_FILE_PATH, outPath);
                            intent.putExtra(CompressionService.TAG_DATA_OUTPUT_FILE_NAME, outName);
                            intent.putExtra(CompressionService.TAG_DATA_OUTPUT_FILE_SIZE, outSize);
                            DashboardFragment.this.getActivity().startService(intent);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    final Uri selectedImageUri = data.getData();
                    try {
                        filePath = GetFilePath.getPath(getActivity(), selectedImageUri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (requestCode == REQUEST_PICK_FILE) {
                        if (!TextUtils.isEmpty(filePath)) {
                        }
                    } else if (requestCode == REQUEST_PICK_IMAGE) {
                        if (!TextUtils.isEmpty(filePath)) {
                            File compressedImage = null;
                            //compress image
                            if (Utils.getFileSizeInKB(filePath) > 500) {
                                compressedImage = new Compressor.Builder(getActivity())
                                        .setMaxWidth(640)
                                        .setMaxHeight(480)
                                        .setQuality(75)
                                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                                        .setDestinationDirectoryPath(FileUtils.createFolderInExternalStorageDirectory(getString(R.string.app_name) + "/" + Constant.IMAGE_FOLDER_NAME))
                                        .build()
                                        .compressToFile(new File(filePath));
                            } else {
                                compressedImage = new File(filePath);
                            }
                        }
                    } else if (requestCode == REQUEST_PICK_VIDEO) {
                        if (!TextUtils.isEmpty(filePath)) {
                            progressDialog = Utils.displayProgressDialog(getActivity());
                            final Uri uri = Uri.fromFile(new File(filePath));
                            filePath = GetFilePath.getPath(getActivity(), uri);
                            String outPath = FileUtils.createFolderInExternalStorageDirectory(getString(R.string.app_name) + "/" + Constant.VIDEO_FOLDER_NAME);
                            String outName = "video_" + System.currentTimeMillis();
                            String outSize = Constant.VIDEO_SIZE;
                            ValidationFactory validationFactory = new ValidationFactory();
                            int ret = validationFactory.getValidator(filePath, outPath, outName, outSize).validate();
                            if (ret != AbstractCompressionOptionsValidator.PASS) {
                                //tvErrorMsg.setText(validationFactory.getErrorMsgPresenter().present(ret));
                                return;
                            }

                            Intent intent = new Intent(DashboardFragment.this.getActivity(), CompressionService.class);
                            intent.putExtra(CompressionService.TAG_ACTION, CompressionService.FLAG_ACTION_ADD_VIDEO);
                            intent.putExtra(CompressionService.TAG_DATA_INPUT_FILE_PATH, filePath);
                            intent.putExtra(CompressionService.TAG_DATA_OUTPUT_FILE_PATH, outPath);
                            intent.putExtra(CompressionService.TAG_DATA_OUTPUT_FILE_NAME, outName);
                            intent.putExtra(CompressionService.TAG_DATA_OUTPUT_FILE_SIZE, outSize);
                            DashboardFragment.this.getActivity().startService(intent);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets Uri of image to capture. An External Cache Directory is created and
     * a file for current_post is created in it, this method Uri for it.
     *
     * @param canCleanup : Can delete the file if already exists
     * @return : Generated Uri.
     */
    private Uri getPostImageUri(boolean canCleanup, String ticket, String timeStamp) {
        final File file = new File(getActivity().getExternalCacheDir() + File.separator + ticket + "_" + timeStamp + Constant.IMAGE_EXTENSION);
        if (canCleanup) {
            if (file.exists()) {
                file.delete();
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        cameraFilePath = file.getAbsolutePath();
        return Uri.fromFile(file);
    }

    /**
     * Gets Uri of video to capture. An External Cache Directory is created and
     * a file for current_post is created in it, this method Uri for it.
     *
     * @param canCleanup : Can delete the file if already exists
     * @return : Generated Uri.
     */
    private Uri getPostVideoUri(boolean canCleanup, String ticket, String timeStamp) {
        final File file = new File(getActivity().getExternalCacheDir() + File.separator + ticket + "_" + timeStamp + Constant.VIDEO_EXTENSION);
        if (canCleanup) {
            if (file.exists()) {
                file.delete();
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        cameraFilePath = file.getAbsolutePath();
        return Uri.fromFile(file);
    }

    //Permission check


    private boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    requestPermission();
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Requests the write permission.
     * If the permission has been denied previously, a alert will prompt the user to grant the
     * permission, otherwise it is requested directly.
     */
    private void requestPermission() {
        // permission has not been granted yet. Request it directly.
        FragmentCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_ALL) {
            // BEGIN_INCLUDE(permission_result)
            // Received permission result for write permission.

            // Check if the only required permission has been granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED && grantResults[3] == PackageManager.PERMISSION_GRANTED) {
                // write permission has been granted
                if (Utils.isNetworkAvailable(getActivity())) {
                    ayncLoadActionList = new AyncLoadActionList();
                    ayncLoadActionList.execute();
                } else {
                    Utils.displayDialog(getActivity(), getString(R.string.app_name), getString(R.string.alret_internet));
                }
            } else {
                Toast.makeText(getActivity(), R.string.alret_permision_need, Toast.LENGTH_LONG).show();
            }
            // END_INCLUDE(permission_result)

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onClick(View view) {

    }


    @Override
    public void onClick(String action, String code) {
        mAction = action;
        mCode = code;
        if (Utils.isNetworkAvailable(getActivity())) {

            asyncSendSos = new AsyncSendSos();
            asyncSendSos.execute("0", KavachApp.getInstance().getDeviceID(), KavachApp.getInstance().getIMEI(), Utils.getCurrentTimeStamp(), "" + KavachApp.getInstance().getCurrentLocation().getLatitude(), "" + KavachApp.getInstance().getCurrentLocation().getLongitude(), mCode, "I");
        } else {
            Utils.displayDialog(getActivity(), getString(R.string.app_name), getString(R.string.alret_internet));
        }
//        if (action.equalsIgnoreCase(Constant.TYPE_VIDEO)) {
//            selectVideoOption();
//        } else if (action.equalsIgnoreCase(Constant.TYPE_IMAGE)) {
//            selectImageOption();
//        } else {
//            audioRecorder = new AudioRecorder(FileUtils.createFolderInExternalStorageDirectory(getString(R.string.app_name) + "/" + Constant.AUDIO_FOLDER_NAME) + "/Audio_" + System.currentTimeMillis());
//            try {
//                audioRecorder.start();
//                CountDownTimer countDowntimer = new CountDownTimer(Constant.AUDIO_RECORD_TIMELIMIT, 1000) {
//                    public void onTick(long millisUntilFinished) {
//                    }
//
//                    public void onFinish() {
//                        try {
//                            audioRecorder.stop();
//                        } catch (IOException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//
//
//                    }
//                };
//                countDowntimer.start();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }


    private void selectImageOption() {
        captureImage();
//        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle("Add Photo!");
//        builder.setItems(options, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//                if (item == 0) {
//                    captureImage();
//                } else if (item == 1) {
//                    chooseImageFileFromStorage();
//
//                } else if (options[item].equals("Cancel")) {
//                    dialog.dismiss();
//                }
//            }
//        });
//        builder.show();
    }


    private void selectVideoOption() {
        captureVideo();
//        final CharSequence[] options = {"Take Video", "Choose from Gallery", "Cancel"};
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle("Add Photo!");
//        builder.setItems(options, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//                if (item == 0) {
//                    captureVideo();
//                } else if (item == 1) {
//                    chooseVideoFileFromStorage();
//
//                } else if (options[item].equals("Cancel")) {
//                    dialog.dismiss();
//                }
//            }
//        });
//        builder.show();
    }


    //--------------------LOCATION CODE-----------------------------------
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, DashboardFragment.this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        KavachApp.getInstance().setCurrentLocation(location);

    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private class AsyncSendSos extends AsyncTask<String, Void, Boolean> {
        ProgressDialog progressDialog;
        WSSOS wssos;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = Utils.displayProgressDialog(getActivity());
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String TripID = params[0];
            String MacID = params[1];
            String SimID = params[2];
            TimeStamp = params[3];
            String Lat = params[4];
            String Long = params[5];
            String AlertType = params[6];
            String LogStatus = params[7];

            wssos = new WSSOS(getActivity());
            return wssos.executeService(TripID, MacID, SimID, TimeStamp, Lat, Long, AlertType, LogStatus);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (!isCancelled()) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (aBoolean) {

                    ticketNumber = "" + wssos.getTicket();
                    if (mAction.equalsIgnoreCase(Constant.TYPE_VIDEO)) {
                        selectVideoOption();
                    } else if (mAction.equalsIgnoreCase(Constant.TYPE_IMAGE)) {
                        selectImageOption();
                    } else {
                        audioRecorder = new AudioRecorder(FileUtils.createFolderInExternalStorageDirectory(getString(R.string.app_name) + "/" + Constant.AUDIO_FOLDER_NAME) + "/Audio_" + System.currentTimeMillis());
                        try {
                            audioRecorder.start();
                            CountDownTimer countDowntimer = new CountDownTimer(Constant.AUDIO_RECORD_TIMELIMIT, 1000) {
                                public void onTick(long millisUntilFinished) {
                                }

                                public void onFinish() {
                                    try {
                                        audioRecorder.stop();
                                    } catch (IOException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }


                                }
                            };
                            countDowntimer.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
//                    Utils.displayDialog(getActivity(), getString(R.string.app_name), getString(R.string.alert_sos_success));
                } else {
                    Utils.displayDialog(getActivity(), getString(R.string.app_name), getString(R.string.alert_sos_failed));
                }
            }
        }
    }

//    private class AynsUploadFile extends AsyncTask<String, Void, Void> {
//        private ProgressDialog progressDialog;
//        private WSUploadPhoto wsUploadPhoto;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            progressDialog = Utils.displayProgressDialog(getActivity());
//        }
//
//        @Override
//        protected Void doInBackground(String... voids) {
//            try {
//                wsUploadPhoto = new WSUploadPhoto(voids[1], voids[2], voids[3]);
//                final FileInputStream fstrm = new FileInputStream(selectedPath);
//                String finalDay = "" + day;
//                if (finalDay.length() <= 1) {
//                    finalDay = "0" + finalDay;
//                }
//                String finalMonth = "" + (month + 1);
//                if (finalMonth.length() <= 1) {
//                    finalMonth = "0" + finalMonth;
//                }
//                final String fileName = "DA_" + year + finalMonth + finalDay + "_" + System.currentTimeMillis() + ".jpg";
//                wsUploadPhoto.Send_Now(fstrm, fileName);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            super.onPostExecute(result);
//            Utils.dismissDialog(progressDialog);
//            if (!isCancelled()) {
//                if (getActivity() != null && !getActivity().isFinishing() && isAdded()) {
//                    Utils.displayAlertDialog(false, "", getString(R.string.msg_successfully_uploaded_photo), getActivity());
//                }
//            }
//        }
//    }

    private class AyncLoadActionList extends AsyncTask<Void, Void, ArrayList<ActionModel>> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = Utils.displayProgressDialog(getActivity());
        }

        @Override
        protected ArrayList<ActionModel> doInBackground(Void... params) {
            WSGetAlertList wsGetAlertList = new WSGetAlertList(getActivity());
            return wsGetAlertList.executeService();
        }

        @Override
        protected void onPostExecute(ArrayList<ActionModel> actionModels) {
            super.onPostExecute(actionModels);
            if (!isCancelled()) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (actionModels != null && actionModels.size() > 0) {
                    actionList = new ArrayList<>();
                    actionList.addAll(actionModels);
                    loadDashboardAdapter();
                }

            }
        }
    }

    private class AynsUploadPhoto extends AsyncTask<String, Void, Void> {
        private ProgressDialog progressDialog;
        private WSUploadPhoto wsUploadPhoto;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = Utils.displayProgressDialog(getActivity());
        }

        @Override
        protected Void doInBackground(String... voids) {
            try {
                wsUploadPhoto = new WSUploadPhoto(voids[1], voids[2], voids[3]);
                final FileInputStream fstrm = new FileInputStream(voids[0]);
                String finalDay = "" + day;
                if (finalDay.length() <= 1) {
                    finalDay = "0" + finalDay;
                }
                String finalMonth = "" + (month + 1);
                if (finalMonth.length() <= 1) {
                    finalMonth = "0" + finalMonth;
                }
//                String name=new File(voids[0]).getName();
//                name=name.replace(":","");
//                name=name.replace(" ","");
//                name=na

                wsUploadPhoto.Send_Now(fstrm, new File(voids[0]).getName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (!isCancelled()) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        }

    }
}
