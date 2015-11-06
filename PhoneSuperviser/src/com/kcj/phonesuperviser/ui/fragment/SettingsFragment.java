package com.kcj.phonesuperviser.ui.fragment;

import com.kcj.phonesuperviser.R;
import com.kcj.phonesuperviser.ui.AboutActivity;
import com.kcj.phonesuperviser.ui.FragmentContainerActivity;
import com.kcj.phonesuperviser.util.AppUtil;
import com.kcj.phonesuperviser.util.Utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;


/**
 * @ClassName: SettingsFragment
 * @Description: 设置
 * @author: KouChengjian
 * @date: 5015-5-16 20:27
 */
public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener{

	private Preference createShortCut;
    private Preference pVersion;
    private Preference pVersionDetail;
    private Preference pGithub;// Github
    private Preference pGrade;// Github
    private Preference pShare;// Github
    private Preference pAbout;// Github
	
    public static void launch(Activity from) {
        FragmentContainerActivity.launch(from, SettingsFragment.class, null);
    }
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addPreferencesFromResource(R.xml.ui_settings);
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        getActivity().getActionBar().setDisplayShowHomeEnabled(false);
        getActivity().getActionBar().setTitle(R.string.title_settings);
        
        createShortCut = findPreference("createShortCut");
        createShortCut.setOnPreferenceClickListener(this);
        pVersion = findPreference("pVersion");
        pVersion.setOnPreferenceClickListener(this);
        pVersionDetail = findPreference("pVersionDetail");
        pVersionDetail.setSummary("当前版本：" + AppUtil.getVersion(getActivity()));
        pVersionDetail.setOnPreferenceClickListener(this);

        pGithub = findPreference("pGithub");
        pGithub.setOnPreferenceClickListener(this);
        pGrade = findPreference("pGrade");
        pGrade.setOnPreferenceClickListener(this);
        pShare = findPreference("pShare");
        pShare.setOnPreferenceClickListener(this);
        pAbout = findPreference("pAbout");
        pAbout.setOnPreferenceClickListener(this);
        initData();
	}
	
	private void initData() {
//        String appID = "wxa263da737a20300e";
//        String appSecret = "381a2fab6466410c674afaa40c77c953";
//// 添加微信平台
//        UMWXHandler wxHandler = new UMWXHandler(getActivity(),appID,appSecret);
//        wxHandler.addToSocialSDK();
//// 添加微信朋友圈
//        UMWXHandler wxCircleHandler = new UMWXHandler(getActivity(),appID,appSecret);
//        wxCircleHandler.setToCircle(true);
//        wxCircleHandler.addToSocialSDK();
    }

	@Override
	public boolean onPreferenceClick(Preference preference) {
        if ("createShortCut".equals(preference.getKey())) {
            createShortCut();
        } else if ("pVersion".equals(preference.getKey())) {
        	ShowToast( "当前版本为最新版本！");
        } else if ("pVersionDetail".equals(preference.getKey())) {
            VersionFragment.launch(getActivity());
        } else if ("pGithub".equals(preference.getKey())) {
            Utils.launchBrowser(getActivity(), "https://github.com/joyoyao/superCleanMaster");
        }else if ("pGrade".equals(preference.getKey())) {
            startMarket();
        }else if ("pShare".equals(preference.getKey())) {
            shareMyApp();
        }
        else if ("pAbout".equals(preference.getKey())) {
            getActivity().startActivity(new Intent(getActivity(), AboutActivity.class));
        }
        return false;
    }
	
	private void createShortCut() {
        Intent intent = new Intent();
        intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "一键加速");
        intent.putExtra("duplicate", false);
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, BitmapFactory.decodeResource(getResources(), R.drawable.short_cut_icon));
        Intent i = new Intent();
        i.setAction("com.kcj.shortcut");
        i.addCategory("android.intent.category.DEFAULT");
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, i);
        getActivity().sendBroadcast(intent);
        ShowToast( "“一键加速”快捷图标已创建");
    }

	public  void startMarket() {
        Uri uri = Uri.parse(String.format("market://details?id=%s", AppUtil.getPackageInfo(getActivity()).packageName));
        if (Utils.isIntentSafe(getActivity(), uri)) {
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(intent);
        }
        // 没有安装市场
        else {
        	ShowToast("无法打开应用市场");

        }
    }
	
	 private void shareMyApp() {
		 
	 }
	
	public void ShowToast(String s){
		Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
	}
}
