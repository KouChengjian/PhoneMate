package com.kcj.phonesuperviser.ui.fragment;


import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;



/**
 * @ClassName: BaseFragment
 * @Description: 基类
 * @author: 
 * @date: 
 */
abstract public class BaseFragment extends Fragment{

	protected ActionBar actionBar;
	protected Toast mToast;
	protected Context mContext;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		actionBar = getActivity().getActionBar(); 

	}
	
    protected abstract void initViews();
	
	protected abstract void initEvents();
	
	protected abstract void initDatas();
	
	public View findViewById(int paramInt) {
		return getView().findViewById(paramInt);
	}
	
	public void startAnimActivity(Intent intent) {
		this.startActivity(intent);
	}
	
	public void startAnimActivity(Class<?> cla) {
		getActivity().startActivity(new Intent(getActivity(), cla));
	}
	
	protected void startAnimActivity(Class<?> cls, Bundle bundle) {
		Intent intent = new Intent();
		intent.setClass(getActivity(), cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
	}
	
	public void ShowToast(String text) {
		if (mToast == null) {
			mToast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(text);
		}
		mToast.show();
	}

	public void ShowToast(int text) {
		if (mToast == null) {
			mToast = Toast.makeText(getActivity(), text, Toast.LENGTH_LONG);
		} else {
			mToast.setText(text);
		}
		mToast.show();
	}
	
}
