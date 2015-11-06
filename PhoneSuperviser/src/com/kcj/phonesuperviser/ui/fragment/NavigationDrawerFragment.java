package com.kcj.phonesuperviser.ui.fragment;

import butterknife.ButterKnife;

import com.kcj.phonesuperviser.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;


/**
 * @ClassName: NavigationDrawerFragment
 * @Description: 
 * @author: 
 * @date: 
 */
public class NavigationDrawerFragment extends BaseFragment{

//	private DrawerLayout mDrawerLayout;
//    private View mFragmentContainerView;
//    private boolean mFromSavedInstanceState;
    
    private NavigationDrawerCallbacks mCallbacks;
    
    final int radioIds[] = {
            R.id.radio0,
            R.id.radio1,
            R.id.radio2
    };
    
    RadioButton radios[] = new RadioButton[radioIds.length];
	
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException( "Activity must implement NavigationDrawerCallbacks.");
        }
    }
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_navigation,container, false);
        ButterKnife.inject(this, view);
		return view;
	}
	
	@Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initViews();
		initEvents();
		initDatas();
	}
	
	@Override
	protected void initViews() {
		for (int i = 0; i < radioIds.length; ++i) {
            radios[i] = (RadioButton) getView().findViewById(radioIds[i]);
            radios[i].setOnClickListener(clickItem);
        }
	}

	@Override
	protected void initEvents() {}

	@Override
	protected void initDatas() {}

	@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
	
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
    
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
    
    View.OnClickListener clickItem = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < radios.length; ++i) {
                if (v.equals(radios[i])) {
                    selectItem(i);
                } else {
                    radios[i].setChecked(false);
                }
            }
        }
    };
    
    private void selectItem(int position) {
      if (mCallbacks != null) {
          mCallbacks.onNavigationDrawerItemSelected(position);
      }
  }

	public static interface NavigationDrawerCallbacks {
        void onNavigationDrawerItemSelected(int position);
    }
	
}
