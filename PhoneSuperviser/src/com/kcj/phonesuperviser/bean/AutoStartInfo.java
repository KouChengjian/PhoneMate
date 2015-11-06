package com.kcj.phonesuperviser.bean;

import android.graphics.drawable.Drawable;


/**
 * @ClassName: AutoStartInfo
 * @Description: 自动启动数据
 * @author: 
 * @date: 
 */
public class AutoStartInfo {
    private String label;
    private String packageName;
    private Drawable icon;
    private String name;

    private String packageReceiver;
    private String desc;
    /**  是否是系统进程. */
    public boolean isSystem;
    public boolean isenable;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean isSystem) {
        this.isSystem = isSystem;
    }

    public boolean isEnable() {
        return isenable;
    }

    public void setEnable(boolean enable) {
        this.isenable = enable;
    }

    public String getPackageReceiver() {
        return packageReceiver;
    }

    public void setPackageReceiver(String packageReceiver) {
        this.packageReceiver = packageReceiver;
    }
}
