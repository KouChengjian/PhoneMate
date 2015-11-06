package com.kcj.phonesuperviser.ui;

import java.lang.reflect.Field;
import java.util.List;

import butterknife.InjectView;

import com.kcj.phonesuperviser.CoreService;
import com.kcj.phonesuperviser.R;
import com.kcj.phonesuperviser.bean.AppProcessInfo;
import com.kcj.phonesuperviser.util.StorageUtil;
import com.kcj.phonesuperviser.util.SystemBarTintManager;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;


/**
 * @ClassName: ShortCutActivity
 * @Description:
 * @author:
 * @date:
 */
public class ShortCutActivity extends BaseActivity implements CoreService.OnPeocessActionListener{

	@InjectView(R.id.layout_anim)
	protected RelativeLayout layoutAnim;

	@InjectView(R.id.mRelativeLayout)
	protected RelativeLayout mRelativeLayout;

	@InjectView(R.id.clean_light_img)
	protected ImageView cleanLightImg;

	private Rect rect;

	private CoreService mCoreService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showcut);
		rect = getIntent().getSourceBounds();
		if (rect == null) {
			finish();
			return;
		}
		initViews();
		initEvents();
		initDatas();
	}

	@Override
	protected void initViews() {}

	@Override
	protected void initEvents() {}

	@Override
	protected void initDatas() {
		if (rect != null) {
			Class<?> c = null;
			Object obj = null;
			Field field = null;
			int x = 0, statusBarHeight = 0;
            try {
                c = Class.forName("com.android.internal.R$dimen");
                obj = c.newInstance();
                field = c.getField("status_bar_height");
                x = Integer.parseInt(field.get(obj).toString());
                statusBarHeight = getResources().getDimensionPixelSize(x);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            layoutAnim.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            int height = layoutAnim.getMeasuredHeight();
            int width = layoutAnim.getMeasuredWidth();
            RelativeLayout.LayoutParams layoutparams = (RelativeLayout.LayoutParams) layoutAnim .getLayoutParams();
            layoutparams.leftMargin = rect.left + rect.width() / 2 - width / 2;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            	setTranslucentStatus(true);
                SystemBarTintManager tintManager = new SystemBarTintManager( this);
                tintManager.setStatusBarTintEnabled(true);
                tintManager.setStatusBarTintResource(R.color.transparent);
                layoutparams.topMargin = rect.top + rect.height() / 2 - height / 2;
            }else {
                layoutparams.topMargin = rect.top + rect.height() / 2 - height/ 2 - statusBarHeight;
            }
            mRelativeLayout.updateViewLayout(layoutAnim, layoutparams);
		}
		cleanLightImg.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate_anim));
        bindService(new Intent(mContext, CoreService.class),mServiceConnection, Context.BIND_AUTO_CREATE);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		unbindService(mServiceConnection);
		super.onDestroy();
	}

	@TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        LayoutParams winParams = win.getAttributes();
        final int bits = LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
	
	
	private ServiceConnection mServiceConnection = new ServiceConnection() {
		
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mCoreService = ((CoreService.ProcessServiceBinder) service).getService();
            mCoreService.setOnActionListener(ShortCutActivity.this);
            mCoreService.cleanAllProcess();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mCoreService.setOnActionListener(null);
            mCoreService = null;
        }
    };

	@Override
	public void onScanStarted(Context context) {}

	@Override
	public void onScanProgressUpdated(Context context, int current, int max) {}

	@Override
	public void onScanCompleted(Context context, List<AppProcessInfo> apps) {}

	@Override
	public void onCleanStarted(Context context) {}

	@Override
	public void onCleanCompleted(Context context, long cacheSize) {
		if (cacheSize > 0) {
            ShowToast("一键清理 开源版,为您释放" + StorageUtil.convertStorage(cacheSize) + "内存");
        } else {
        	ShowToast("您刚刚清理过内存,请稍后再来~");
        }
        finish();
	}
	
	private void killProcess() {
        ActivityManager am = (ActivityManager) getBaseContext().getApplicationContext().getSystemService(  Context.ACTIVITY_SERVICE);
        // 获得正在运行的所有进程
        List<ActivityManager.RunningAppProcessInfo> processes = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : processes) {
            if (info != null && info.processName != null&& info.processName.length() > 0) {
                String pkgName = info.processName;
                if (!("system".equals(pkgName) || "launcher".equals(pkgName)
                        || "android.process.media".equals(pkgName)
                        || "android.process.acore".equals(pkgName)
                        || "com.android.phone".equals(pkgName)
                        || "com.fb.FileBrower".equals(pkgName)// 浏览器
                        || "com.ott_pro.launcher".equals(pkgName)// 桌面
                        || "com.ott_pro.upgrade".equals(pkgName)// 升级
                        || "com.example.airplay".equals(pkgName)// 媒体分享
                        || "com.amlogic.mediacenter".equals(pkgName)// 媒体分享
                        || "com.android.dreams.phototable".equals(pkgName)// 屏保
                        || "com.amlogic.inputmethod.remote".equals(pkgName)// 输入法
                        || pkgName.startsWith("com.lefter"))) {
                    am.killBackgroundProcesses(pkgName);// 杀进程
                }
            }
        }
    }
}
