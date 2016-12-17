package com.ikould.launchlist.data;

import android.graphics.drawable.Drawable;

/**
 * Created by liudong on 2016/6/21.
 */
public class LaunchData {
    private String appName;
    private String packageName;
    private Drawable launchIcon;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Drawable getLaunchIcon() {
        return launchIcon;
    }

    public void setLaunchIcon(Drawable launchIcon) {
        this.launchIcon = launchIcon;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public String toString() {
        return "LaunchData{" +
                "appName='" + appName + '\'' +
                ", packageName='" + packageName + '\'' +
                ", launchIcon=" + launchIcon +
                '}';
    }
}
