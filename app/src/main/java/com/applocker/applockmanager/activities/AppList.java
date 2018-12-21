package com.applocker.applockmanager.activities;

import android.app.AppOpsManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

import com.applocker.applockmanager.R;
import com.applocker.applockmanager.adapter.CustomList;
import com.applocker.applockmanager.fragment.ListAppFragment;
import com.applocker.applockmanager.service.AppLockService;
import com.applocker.applockmanager.utils.Constant;
import com.applocker.applockmanager.utils.SharedPreferenceUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AppList extends AppCompatActivity {
    @BindView(R.id.layout_main)
    ConstraintLayout layoutMain;
    @BindView(R.id.constraintLayout2)
    FrameLayout constraintLayout2;
    private ListView applist;
    private ImageView img_main, img_setting;
    private SharedPreferenceUtils utils;
    private boolean check_on_off;
    private ProgressDialog dialog;
    ArrayList<String> packagenameArray;
    ArrayList<String> appnameArray;
    ArrayList<Drawable> iconArray;
    ArrayAdapter<String> arrayAdapter;
    private CustomList adapter;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);
        ButterKnife.bind(this);

        utils = new SharedPreferenceUtils(this);
        applist = (ListView) findViewById(R.id.applist);
        img_main = findViewById(R.id.img_main);
        img_setting = findViewById(R.id.img_setting);

        check_on_off = utils.getBoolanValue(Constant.SWITCH_ON_OFF, true);


        if (!isAccessGranted()) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
//            Toast.makeText(this, "Permission is required", Toast.LENGTH_SHORT).show();
        }
        if (check_on_off == true) {
            Intent intent = new Intent(this, AppLockService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent);
            } else {
                startService(intent);
            }
        } else {
            Intent intent = new Intent(this, AppLockService.class);
            stopService(intent);
        }


        SharedPreferences.Editor editor = getSharedPreferences("Start", MODE_PRIVATE).edit();
        editor.putInt("flag", 1);
        editor.apply();

//        packagenameArray = new ArrayList<String>();
//        appnameArray = new ArrayList<String>();
//        iconArray = new ArrayList<Drawable>();
//
//        PackageManager packageManager = getPackageManager();
//        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
//        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//
//        List<ApplicationInfo> packs = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
//
//        Collections.sort(packs, new ApplicationInfo.DisplayNameComparator(packageManager));
//
//        for (int i = 0; i < packs.size(); i++) {
//            ApplicationInfo p = packs.get(i);
//            if ((packs.get(i).flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
//                packagenameArray.add(p.packageName);
//                appnameArray.add(p.loadLabel(getPackageManager()).toString());
//                iconArray.add(p.loadIcon(getPackageManager()));
//            }
//            if ((packs.get(i).flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
//                packagenameArray.add(p.packageName);
//                appnameArray.add(p.loadLabel(getPackageManager()).toString());
//                iconArray.add(p.loadIcon(getPackageManager()));
//            } else {
////                 packagenameArray.add(p.packageName);
////                 appnameArray.add(p.loadLabel(getPackageManager()).toString());
////                 iconArray.add(p.loadIcon(getPackageManager()));
//            }
//
//        }
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, appnameArray);


        int pos = utils.getIntValue(Constant.CHANGE_THEME, 0);
        if (pos == 0) {
            layoutMain.setBackgroundResource(R.drawable.bg_1);
        }
        if (pos == 1) {
            layoutMain.setBackgroundResource(R.drawable.bg_2);
        }
        if (pos == 2) {
            layoutMain.setBackgroundResource(R.drawable.bg_3);
        }
        if (pos == 3) {
            layoutMain.setBackgroundResource(R.drawable.bg_4);
        }
        if (pos == 4) {
            layoutMain.setBackgroundResource(R.drawable.bg_5);
        }
        if (pos == 5) {
            layoutMain.setBackgroundResource(R.drawable.bg_6);
        }
//        String[] Stringarray = appnameArray.toArray(new String[appnameArray.size()]);
//        String[] Stringarray1 = packagenameArray.toArray(new String[packagenameArray.size()]);
//        Drawable[] Drawablearray = iconArray.toArray(new Drawable[iconArray.size()]);
//        adapter = new CustomList(this, Stringarray, Drawablearray, packagenameArray);
//        applist.setAdapter(adapter);

        Glide.with(this).load(R.drawable.ic_main).into(img_main);
        Glide.with(this).load(R.drawable.ic_setting).into(img_setting);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.constraintLayout2, new ListAppFragment(), "aaa");
        transaction.commit();
    }


    private boolean isAccessGranted() {
        try {
            PackageManager packageManager = this.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(this.getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) this.getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            if (isAccessGranted()) {
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(startMain);
            }

        }

        return false;
    }

    @OnClick(R.id.img_setting)
    public void onViewClicked() {
        startActivity(new Intent(this, SettingActivity.class));
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isAccessGranted()) {
            finishAffinity();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isAccessGranted()) {
            finishAffinity();
        }
    }

}

