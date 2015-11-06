// Generated code from Butter Knife. Do not modify!
package com.kcj.phonesuperviser.ui.fragment;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class SoftwareManageFragment$$ViewInjector<T extends com.kcj.phonesuperviser.ui.fragment.SoftwareManageFragment> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165222, "field 'listview'");
    target.listview = finder.castView(view, 2131165222, "field 'listview'");
    view = finder.findRequiredView(source, 2131165227, "field 'mProgressBarText'");
    target.mProgressBarText = finder.castView(view, 2131165227, "field 'mProgressBarText'");
    view = finder.findRequiredView(source, 2131165225, "field 'mProgressBar'");
    target.mProgressBar = view;
    view = finder.findRequiredView(source, 2131165234, "field 'topText'");
    target.topText = finder.castView(view, 2131165234, "field 'topText'");
  }

  @Override public void reset(T target) {
    target.listview = null;
    target.mProgressBarText = null;
    target.mProgressBar = null;
    target.topText = null;
  }
}
