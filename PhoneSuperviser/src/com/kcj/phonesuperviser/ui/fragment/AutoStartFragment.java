package com.kcj.phonesuperviser.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.kcj.phonesuperviser.R;
import com.kcj.phonesuperviser.adapter.AutoStartAdapter;
import com.kcj.phonesuperviser.bean.AutoStartInfo;
import com.kcj.phonesuperviser.util.BootStartUtils;
import com.kcj.phonesuperviser.util.RootUtil;
import com.kcj.phonesuperviser.util.ShellUtils;


/**
 * @ClassName: AutoStartFragment
 * @Description: 自动启动
 * @author: 
 * @date: 
 */
public class AutoStartFragment extends BaseFragment {

//	public Context mContext;
    public static final int REFRESH_BT = 111;
    private static final String ARG_POSITION = "position";
    private int position; // 0:普通软件，2 系统软件
    private AutoStartAdapter mAutoStartAdapter;
    private List<AutoStartInfo> isSystemAuto = null;
    private List<AutoStartInfo> noSystemAuto = null;
    private int canDisableCom;
    
    @InjectView(R.id.listview)
    protected ListView listview;
    @InjectView(R.id.bottom_lin)
    protected LinearLayout bottom_lin;
    @InjectView(R.id.disable_button)
    protected Button disableButton;
    @InjectView(R.id.topText)
    protected TextView topText;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		 View view = inflater.inflate(R.layout.fragment_autostart, container, false);
		 ButterKnife.inject(this, view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		position = getArguments().getInt(ARG_POSITION);
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
		List<AutoStartInfo> mAutoStartInfo = BootStartUtils.fetchAutoApps(mContext);
		noSystemAuto = new ArrayList<>();
        isSystemAuto = new ArrayList<>();
        for (AutoStartInfo a : mAutoStartInfo) {
            if (a.isSystem()) {
                isSystemAuto.add(a);
            } else {
                noSystemAuto.add(a);
            }
        }
        if (position == 0) {
        	topText.setText("禁止下列软件自启,可提升运行速度");
            mAutoStartAdapter = new AutoStartAdapter(mContext, noSystemAuto, mHandler);
            listview.setAdapter(mAutoStartAdapter);
            refeshButoom();
        } else {
        	topText.setText("禁止系统核心软件自启,将会影响手机的正常使用,请谨慎操作");
            mAutoStartAdapter = new AutoStartAdapter(mContext, isSystemAuto, null);
            listview.setAdapter(mAutoStartAdapter);
        }
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy() {
		ButterKnife.reset(this);
		super.onDestroy();
	}
	
	@OnClick(R.id.disable_button)
    public void onClickDisable() {
        RootUtil.preparezlsu(mContext);
        disableAPP();
    }
	
	private void refeshButoom() {
        if (position == 0) {
            canDisableCom = 0;
            for (AutoStartInfo autoS : noSystemAuto) {
                if (autoS.isEnable()) {
                    canDisableCom++;
                }
            }
            if (canDisableCom > 0) {
                bottom_lin.setVisibility(View.VISIBLE);
                disableButton.setText("可优化" + canDisableCom + "款");
            } else {
                bottom_lin.setVisibility(View.GONE);
            }
        }
    }
	
	private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH_BT:
                    refeshButoom();
                    break;
            }
        }
    };
	
    private void disableAPP() {
        List<String> mSring = new ArrayList<>();
        for (AutoStartInfo auto : noSystemAuto) {
            if (auto.isEnable()) {
                String packageReceiverList[] = auto.getPackageReceiver().toString().split(";");
                for (int j = 0; j < packageReceiverList.length; j++) {
                    String cmd = "pm disable " + packageReceiverList[j];
                    //部分receiver包含$符号，需要做进一步处理，用"$"替换掉$
                    cmd = cmd.replace("$", "\"" + "$" + "\"");
                    //执行命令
                    mSring.add(cmd);
                }
            }
        }

        ShellUtils.CommandResult mCommandResult = ShellUtils.execCommand(mSring, true, true);
        if (mCommandResult.result == 0) {
            ShowToast( "应用已经全部禁止");
            for (AutoStartInfo auto : noSystemAuto) {
                if (auto.isEnable()) {
                    auto.setEnable(false);
                }
            }
            mAutoStartAdapter.notifyDataSetChanged();
            refeshButoom();
        } else {
        	ShowToast( "该功能需要获取系统root权限，请允许获取root权限");
        }
    }
}
