package app.sosdemo.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v13.app.FragmentCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import java.io.IOException;
import java.util.ArrayList;

import app.sosdemo.R;
import app.sosdemo.adapter.DashboardAdapter;
import app.sosdemo.model.ActionModel;
import app.sosdemo.util.Constant;
import app.sosdemo.util.GetFilePath;
import app.sosdemo.util.Utils;

/**
 * Created by indianic on 28/01/17.
 */

public class MediaFragment extends Fragment implements View.OnClickListener, IQueueList, DashboardAdapter.onActionListner {
    private View view;
    //files
    private final String WRITE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final int REQUEST_WRITE_ACCESS = 1000;

    private final int REQUEST_CAPTURE_IMAGE = 111;
    private final int REQUEST_CAPTURE_VIDEO = 222;
    private final int REQUEST_PICK_IMAGE = 333;
    private final int REQUEST_PICK_VIDEO = 444;
    private final int REQUEST_PICK_FILE = 555;

    protected String filePath = null;
    protected String cameraFilePath;
    private QueuedFFmpegCompressor mCompressor;
    private RecyclerView rvActionList;
    private DashboardAdapter dashboardAdapter;
    private ArrayList<ActionModel> actionList;
    private ProgressDialog progressDialog;
    ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            Logger.log("");
            AbstractCompressionService service = ((AbstractCompressionService.LocalBinder) binder).getService();
            mCompressor = (QueuedFFmpegCompressor) service.getCompressor();
            IResponseManager arm = new ActivityResponseManager(MediaFragment.this);
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
                Log.d("FINISH", "FINISH");
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
                Log.e("Progress", "Per - " + pp.getTime());
                Log.e("Progress", "Per - " + pp.getTotalTime());

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
        rvActionList = (RecyclerView) view.findViewById(R.id.fragment_dashboard_rv_actionlist);
        if (isWritePermission()) {
            loadDashboardAdapter();
        }
    }


    private void loadDashboardAdapter() {
        loadActiondata();
        dashboardAdapter = new DashboardAdapter(getActivity(), actionList, false);
        dashboardAdapter.setOnactionListner(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvActionList.setLayoutManager(mLayoutManager);
        rvActionList.setAdapter(dashboardAdapter);
    }

    private void loadActiondata() {
        actionList = new ArrayList<>();
        actionList.add(new ActionModel("SOS/EMergency", "VIDEO"));
        actionList.add(new ActionModel("Domestic Violence", "VIDEO"));
        actionList.add(new ActionModel("Trafic Update", "IMAGE"));
        actionList.add(new ActionModel("Recording", "AUDIO"));
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

            Uri outputFileUri = getPostImageUri(true);
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

            Uri outputFileUri = getPostVideoUri(true);
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
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (requestCode == REQUEST_CAPTURE_VIDEO) {
                    try {

                        if (!TextUtils.isEmpty(filePath)) {
                            progressDialog = Utils.displayProgressDialog(getActivity());
                            final Uri uri = Uri.fromFile(new File(cameraFilePath));
                            filePath = GetFilePath.getPath(getActivity(), uri);
                            String outPath = FileUtils.createFolderInExternalStorageDirectory("VideoCompressor");
                            String outName = "video_" + System.currentTimeMillis();
                            String outSize = Constant.VIDEO_SIZE;
                            ValidationFactory validationFactory = new ValidationFactory();
                            int ret = validationFactory.getValidator(filePath, outPath, outName, outSize).validate();
                            if (ret != AbstractCompressionOptionsValidator.PASS) {
                                //tvErrorMsg.setText(validationFactory.getErrorMsgPresenter().present(ret));
                                return;
                            }

                            Intent intent = new Intent(MediaFragment.this.getActivity(), CompressionService.class);
                            intent.putExtra(CompressionService.TAG_ACTION, CompressionService.FLAG_ACTION_ADD_VIDEO);
                            intent.putExtra(CompressionService.TAG_DATA_INPUT_FILE_PATH, filePath);
                            intent.putExtra(CompressionService.TAG_DATA_OUTPUT_FILE_PATH, outPath);
                            intent.putExtra(CompressionService.TAG_DATA_OUTPUT_FILE_NAME, outName);
                            intent.putExtra(CompressionService.TAG_DATA_OUTPUT_FILE_SIZE, outSize);
                            MediaFragment.this.getActivity().startService(intent);

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
                        }
                    } else if (requestCode == REQUEST_PICK_VIDEO) {
                        if (!TextUtils.isEmpty(filePath)) {
                            progressDialog = Utils.displayProgressDialog(getActivity());
                            final Uri uri = Uri.fromFile(new File(filePath));
                            filePath = GetFilePath.getPath(getActivity(), uri);
                            String outPath = FileUtils.createFolderInExternalStorageDirectory("VideoCompressor");
                            String outName = "video_" + System.currentTimeMillis();
                            String outSize = Constant.VIDEO_SIZE;
                            ValidationFactory validationFactory = new ValidationFactory();
                            int ret = validationFactory.getValidator(filePath, outPath, outName, outSize).validate();
                            if (ret != AbstractCompressionOptionsValidator.PASS) {
                                //tvErrorMsg.setText(validationFactory.getErrorMsgPresenter().present(ret));
                                return;
                            }

                            Intent intent = new Intent(MediaFragment.this.getActivity(), CompressionService.class);
                            intent.putExtra(CompressionService.TAG_ACTION, CompressionService.FLAG_ACTION_ADD_VIDEO);
                            intent.putExtra(CompressionService.TAG_DATA_INPUT_FILE_PATH, filePath);
                            intent.putExtra(CompressionService.TAG_DATA_OUTPUT_FILE_PATH, outPath);
                            intent.putExtra(CompressionService.TAG_DATA_OUTPUT_FILE_NAME, outName);
                            intent.putExtra(CompressionService.TAG_DATA_OUTPUT_FILE_SIZE, outSize);
                            MediaFragment.this.getActivity().startService(intent);
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
    private Uri getPostImageUri(boolean canCleanup) {
        final File file = new File(getActivity().getExternalCacheDir() + File.separator + System.currentTimeMillis() + Constant.IMAGE_EXTENSION);
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
    private Uri getPostVideoUri(boolean canCleanup) {
        final File file = new File(getActivity().getExternalCacheDir() + File.separator + System.currentTimeMillis() + Constant.VIDEO_EXTENSION);
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

    /**
     * check write access
     */
    public boolean isWritePermission() {
        // Check if the write permission is already available.
        if (ContextCompat.checkSelfPermission(getActivity(), WRITE_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
            // Camera permission has not been granted.
            requestWritePermission();
            return false;
        }

        return true;
    }

    /**
     * Requests the write permission.
     * If the permission has been denied previously, a alert will prompt the user to grant the
     * permission, otherwise it is requested directly.
     */
    private void requestWritePermission() {
        // permission has not been granted yet. Request it directly.
        FragmentCompat.requestPermissions(this, new String[]{WRITE_PERMISSION}, REQUEST_WRITE_ACCESS);
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == REQUEST_WRITE_ACCESS) {
            // BEGIN_INCLUDE(permission_result)
            // Received permission result for write permission.
            Log.i("Permission", "Received response for write permission request.");

            // Check if the only required permission has been granted
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // write permission has been granted
                Log.i("Permission", "permission has now been granted. Showing preview.");
                loadDashboardAdapter();
            } else {
                Log.i("Permission", "permission was NOT granted.");
                Toast.makeText(getActivity(), "msg_write_permission_needed", Toast.LENGTH_LONG).show();
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
    public void onClick(String action) {
        if (action.equalsIgnoreCase(Constant.TYPE_VIDEO)) {
            selectVideoOption();
        } else if (action.equalsIgnoreCase(Constant.TYPE_AUDIO)) {
            selectImageOption();
        } else {

        }
    }


    private void selectImageOption() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    captureImage();
                } else if (item == 1) {
                    chooseImageFileFromStorage();

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void selectVideoOption() {

        final CharSequence[] options = {"Take Video", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    captureVideo();
                } else if (item == 1) {
                    chooseVideoFileFromStorage();

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
}
