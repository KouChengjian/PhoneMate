package com.kcj.phonesuperviser.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;


/**
 * @ClassName: ProgressDialog
 * @Description: 
 * @author: 
 * @date: 
 */
public class ProgressDialogView extends DialogFragment{

	int mIndeterminateDrawable;
	String mMessage;
	static View mContentView;

	/**
	 * Create a new instance of AbProgressDialogFragment.
	 */
	public static ProgressDialogView newInstance(int indeterminateDrawable,
			String message) {
		ProgressDialogView f = new ProgressDialogView();
		Bundle args = new Bundle();
		args.putInt("indeterminateDrawable", indeterminateDrawable);
		args.putString("message", message);
		f.setArguments(args);

		return f;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mIndeterminateDrawable = getArguments().getInt("indeterminateDrawable");
		mMessage = getArguments().getString("message");

		ProgressDialog mProgressDialog = new ProgressDialog(getActivity(),AlertDialog.THEME_HOLO_LIGHT);
		if (mIndeterminateDrawable > 0) {
			mProgressDialog.setIndeterminateDrawable(getActivity()
					.getResources().getDrawable(mIndeterminateDrawable));
		}

		if (mMessage != null) {
			mProgressDialog.setMessage(mMessage);
		}

		return mProgressDialog;
	}

	public void setMessage(String mMessage) {
		if (mMessage != null) {
            setMessage(mMessage);
		}

	}
	
}
