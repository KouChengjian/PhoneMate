package com.kcj.phonesuperviser.ui;

import com.kcj.phonesuperviser.R;
import com.kcj.phonesuperviser.util.AppUtil;

import android.os.Bundle;
import android.text.util.Linkify;
import android.view.MenuItem;
import android.widget.TextView;
import butterknife.InjectView;

public class AboutActivity extends BaseSwipeBackActivity{

	@InjectView(R.id.subVersion)
    TextView subVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setTitle("关于");
        TextView tv = (TextView) findViewById(R.id.app_information);
        Linkify.addLinks(tv, Linkify.ALL);

        subVersion.setText("V"+ AppUtil.getVersion(mContext));

    }
	
	@Override
	protected void initViews() {}

	@Override
	protected void initEvents() {}

	@Override
	protected void initDatas() {}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
