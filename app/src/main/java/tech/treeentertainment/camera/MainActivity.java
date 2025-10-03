package tech.treeentertainment.camera;

import java.io.File;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import androidx.appcompat.widget.Toolbar;

import tech.treeentertainment.camera.activities.AppSettingsActivity;
import tech.treeentertainment.camera.ptp.Camera;
import tech.treeentertainment.camera.ptp.Camera.CameraListener;
import tech.treeentertainment.camera.ptp.PtpService;
import tech.treeentertainment.camera.ptp.model.LiveViewData;
import tech.treeentertainment.camera.util.PackageUtil;
import tech.treeentertainment.camera.view.SessionActivity;
import tech.treeentertainment.camera.view.SessionView;
import tech.treeentertainment.camera.view.WebViewDialogFragment;
import tech.treeentertainment.camera.BuildConfig;

public class MainActivity extends SessionActivity implements CameraListener {

    private static final int DIALOG_PROGRESS = 1;
    private static final int DIALOG_NO_CAMERA = 2;
    private final String TAG = MainActivity.class.getSimpleName();

    private final Handler handler = new Handler();

    private PtpService ptp;
    private Camera camera;

    private boolean isInStart;
    private boolean isInResume;
    private SessionView sessionFrag;
    private boolean isLarge;
    private AppSettings settings;

    @Override
    public Camera getCamera() {
        return camera;
    }

    @Override
    public void setSessionView(SessionView view) {
        sessionFrag = view;
    }

    @Override
    public AppSettings getSettings() {
        return settings;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppConfig.LOG) {
            Log.i(TAG, "onCreate");
        }

        setContentView(R.layout.session);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            final WindowInsetsController controller = getWindow().getInsetsController();
            if (controller != null) {
                controller.hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
                controller.setSystemBarsBehavior(
                        WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                );
            }
        } else {
            // 구버전 호환
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        }

        settings = new AppSettings(this);


        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager = findViewById(R.id.viewPager);

        // 1. 어댑터 먼저 세팅
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // 2. TabLayoutMediator로 Tab과 ViewPager 연결
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Session");
                            break;
                        case 1:
                            tab.setText("Gallery");
                            break;
                    }
                }).attach();

        int appVersionCode = -1;
        try {
            appVersionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            // nop
        }

        if (settings.showChangelog(appVersionCode)) {
            showChangelog();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false); // 앱 이름 숨김
        }


        ptp = PtpService.Singleton.getInstance(this);
    }

    private void showChangelog() {
        FragmentTransaction changelogTx = getFragmentManager().beginTransaction();
        WebViewDialogFragment changelogFragment = WebViewDialogFragment.newInstance(R.string.whats_new,
                "file:///android_asset/changelog/changelog.html");
        changelogTx.add(changelogFragment, "changelog");
        changelogTx.commit();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (AppConfig.LOG) {
            Log.i(TAG, "onNewIntent " + intent.getAction());
        }
        this.setIntent(intent);
        if (isInStart) {
            ptp.initialize(this, intent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (AppConfig.LOG) {
            Log.i(TAG, "onStart");
        }
        isInStart = true;
        ptp.setCameraListener(this);
        ptp.initialize(this, getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        isInResume = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isInResume = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (AppConfig.LOG) {
            Log.i(TAG, "onStop");
        }
        isInStart = false;
        ptp.setCameraListener(null);
        if (isFinishing()) {
            ptp.shutdown();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (AppConfig.LOG) {
            Log.i(TAG, "onDestroy");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    private void showNoCameraDialog() {
        new MaterialAlertDialogBuilder(this, R.style.CustomAlertDialogTheme)
                .setTitle(R.string.dialog_no_camera_title)
                .setMessage(R.string.dialog_no_camera_message)
                .setNeutralButton(R.string.ok, (dialog, which) -> dialog.dismiss())
                .show();
    }

    // 2. "Progress" 다이얼로그 (구버전 ProgressDialog 유지 or Material 로 교체)
    private void showProgressDialog() {
        // 권장: Material 로 Progress 표시
        new MaterialAlertDialogBuilder(this, R.style.CustomAlertDialogTheme)
                .setTitle(R.string.app_name)
                .setMessage(R.string.generating_information_please_wait)
                .setCancelable(false)
                .show();
        // 만약 기존 ProgressDialog 계속 쓰고 싶다면:
        // ProgressDialog.show(this, "", "Generating information. Please wait...", true);
    }


    public void onMenuFeedbackClicked(MenuItem item) {
        new MaterialAlertDialogBuilder(this, R.style.CustomAlertDialogTheme)
                .setTitle(R.string.feedback_dialog_title)
                .setMessage(R.string.feedback_dialog_message)
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                    sendDeviceInformation();
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    private void sendDeviceInformation() {
        new Thread(() -> {
            File dir = getExternalCacheDir();
            File out = dir != null ? new File(dir, "deviceinfo.txt") : null;
    
            try {
                if (camera != null && out != null) {
                    camera.writeDebugInfo(out);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
    
            runOnUiThread(() -> {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("message/rfc822"); // 이메일 앱만 필터됨
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"treeentertainment@naver.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "RYC USB Feedback");
    
                if (out != null && out.exists()) {
                    Uri uri = FileProvider.getUriForFile(
                            this,
                            BuildConfig.APPLICATION_ID + ".provider",
                            out
                    );
                    emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "문제가 있나요? 로그 파일을 확인해 주세요.");
                } else {
                    String deviceInfo = camera != null ? camera.getDeviceInfo() : "unknown";
                    emailIntent.putExtra(Intent.EXTRA_TEXT,
                            "문제가 있나요? 로그 파일이 첨부되지 않았습니다.\n\nDevice Info:\n" + deviceInfo);
                }
    
                try {
                    startActivity(Intent.createChooser(emailIntent, "Send email..."));
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "이메일 앱을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }



    public void onMenuChangelogClicked(MenuItem item) {
        showChangelog();
    }

    public void onMenuSettingsClicked(MenuItem item) {
        startActivity(new Intent(this, AppSettingsActivity.class));
    }

    public void onMenuAboutClicked(MenuItem item) {
        View view = getLayoutInflater().inflate(R.layout.about_dialog, null);
        ((TextView) view.findViewById(R.id.about_dialog_version))
                .setText(getString(R.string.about_dialog_version, PackageUtil.getVersionName(this)));

        new MaterialAlertDialogBuilder(this, R.style.CustomAlertDialogTheme)
                .setView(view)
                .setNeutralButton(R.string.ok, (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    @Override
    public void onCameraStarted(Camera camera) {
        this.camera = camera;
        if (AppConfig.LOG) {
            Log.i(TAG, "camera started");
        }
        try {
            dismissDialog(DIALOG_NO_CAMERA);
        } catch (IllegalArgumentException e) {
        }
        TextView cameraNameView = findViewById(R.id.cameraName);
        if (cameraNameView != null) {
            cameraNameView.setText(camera.getDeviceName());
        }
        camera.setCapturedPictureSampleSize(settings.getCapturedPictureSampleSize());
        sessionFrag.cameraStarted(camera);
    }

    @Override
    public void onCameraStopped(Camera camera) {
        if (AppConfig.LOG) {
            Log.i(TAG, "camera stopped");
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        this.camera = null;
        sessionFrag.cameraStopped(camera);
    }

    @Override
    public void onNoCameraFound() {
        showNoCameraDialog();
    }

    @Override
    public void onError(String message) {
        sessionFrag.enableUi(false);
        sessionFrag.cameraStopped(null);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPropertyChanged(int property, int value) {
        sessionFrag.propertyChanged(property, value);
    }

    @Override
    public void onPropertyStateChanged(int property, boolean enabled) {
        // TODO
    }

    @Override
    public void onPropertyDescChanged(int property, int[] values) {
        sessionFrag.propertyDescChanged(property, values);
    }

    @Override
    public void onLiveViewStarted() {
        sessionFrag.liveViewStarted();
    }

    @Override
    public void onLiveViewStopped() {
        sessionFrag.liveViewStopped();
    }

    @Override
    public void onLiveViewData(LiveViewData data) {
        if (!isInResume) {
            return;
        }
        sessionFrag.liveViewData(data);
    }

    @Override
    public void onCapturedPictureReceived(int objectHandle, String filename, Bitmap thumbnail, Bitmap bitmap) {
        if (thumbnail != null) {
            sessionFrag.capturedPictureReceived(objectHandle, filename, thumbnail, bitmap);
        } else {
            Toast.makeText(this, "No thumbnail available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBulbStarted() {
        sessionFrag.setCaptureBtnText("0");
    }

    @Override
    public void onBulbExposureTime(int seconds) {
        sessionFrag.setCaptureBtnText("" + seconds);
    }

    @Override
    public void onBulbStopped() {
        sessionFrag.setCaptureBtnText("Fire");
    }

    @Override
    public void onFocusStarted() {
        sessionFrag.focusStarted();
    }

    @Override
    public void onFocusEnded(boolean hasFocused) {
        sessionFrag.focusEnded(hasFocused);
    }

    @Override
    public void onFocusPointsChanged() {
        // TODO onFocusPointsToggleClicked(null);
    }

    @Override
    public void onObjectAdded(int handle, int format) {
        sessionFrag.objectAdded(handle, format);
    }
}
