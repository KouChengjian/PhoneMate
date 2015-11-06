// Generated code from Butter Knife. Do not modify!
package com.kcj.phonesuperviser.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class SoftwareManageActivity$$ViewInjector<T extends com.kcj.phonesuperviser.ui.SoftwareManageActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165210, "field 'tabs'");
    target.tabs = finder.castView(view, 2131165210, "field 'tabs'");
    view = finder.findRequiredView(source, 2131165211, "field 'pager'");
    target.pager = finder.castView(view, 2131165211, "field 'pager'");
  }

  @Override public void reset(T target) {
    target.tabs = null;
    target.pager = null;
  }
}
