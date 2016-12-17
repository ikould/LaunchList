package com.ikould.launchlist.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.ikould.launchlist.R;
import com.ikould.launchlist.adapter.PackagesAdapter;
import com.ikould.launchlist.data.LaunchData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    List<LaunchData> packageLaunch;
    RecyclerView mRvMain;
    PackagesAdapter.RecyclerLister recyclerLister;
    List<String> packageFilters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        packageLaunch = new ArrayList<>();
        initDatas();
        initView();
    }

    /**
     * 初始化数据
     */
    private void initDatas() {
        //获取文本数据
        packageFilters = new ArrayList<>();
        String packagesNames = getFilterPackageName("package_filter.txt");
        String regex = "(\\b\\w+\\b\\.)+\\w+\\b";
        findPackagesByRegex(regex, packagesNames);
        findAppInfo();
    }

    /**
     * 获取符合应用的APP信息
     */
    private void findAppInfo() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> infoList = getPackageManager().queryIntentActivities(intent, 0);
        if (infoList != null && infoList.size() > 0) {
            for (ResolveInfo info : infoList) {
                if (info != null) {
                    String pkgName = info.activityInfo.packageName;
                    if (!isFilterApp(pkgName)) {
                        String name = info.activityInfo.applicationInfo.loadLabel(getPackageManager()).toString();
                        ActivityInfo activityInfo = info.activityInfo;
                        Drawable drawable = getPackageManager().getDrawable(pkgName, activityInfo.getIconResource(), activityInfo.applicationInfo);
                        LaunchData launchData = new LaunchData();
                        launchData.setAppName(name);
                        launchData.setLaunchIcon(drawable);
                        launchData.setPackageName(pkgName);
                        packageLaunch.add(launchData);
                        Log.d("ResolveInfo", launchData.toString() + "\n");
                    }
                }
            }
        }
    }

    /**
     * 判断是否是过滤APP
     *
     * @param packageName
     * @return
     */
    private boolean isFilterApp(String packageName) {
        for (int i = 0; i < packageFilters.size(); i++) {
            if (packageName.equals(packageFilters.get(i))) {
                return true;
            }
        }
        return false;
    }


    /**
     * 获取过滤包名
     *
     * @param regex
     */
    private void findPackagesByRegex(String regex, String packagesNames) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(packagesNames);
        while (matcher.find()) {
            packageFilters.add(matcher.group());
            Log.d("initDatas", matcher.group());
        }
        Log.d("initDatas", packageFilters.toString());
    }

    /**
     * 获取过滤的包名文件
     */
    private String getFilterPackageName(String fileName) {
        try {
            InputStream is = getAssets().open(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
            int i;
            StringBuffer stringBuffer = new StringBuffer();
            while ((i = br.read()) != -1) {
                stringBuffer.append((char) i);
            }
            return stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 初始化控件
     */
    private void initView() {
        //添加RecyclerView item 点击事件
        recyclerLister = new PackagesAdapter.RecyclerLister() {
            @Override
            public void recycleOnClickListener(View v, int position) {
                LaunchData launchData = packageLaunch.get(position);
                Intent intent = getPackageManager().getLaunchIntentForPackage(launchData.getPackageName());
                if (intent == null) {
                    //如果intent为空，则启动系统桌面
                    intent = new Intent();
                    intent.setAction(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                }
                startActivity(intent);
            }
        };
        //设置RecyclerView
        mRvMain = (RecyclerView) findViewById(R.id.rv_main);
        mRvMain.setLayoutManager(new LinearLayoutManager(this));
        PackagesAdapter packagesAdapter = new PackagesAdapter(packageLaunch, this, recyclerLister);
        mRvMain.setAdapter(packagesAdapter);
    }
}
