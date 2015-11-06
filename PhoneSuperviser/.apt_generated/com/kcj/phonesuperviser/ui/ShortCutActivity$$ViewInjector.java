// Generated code from Butter Knife. Do not modify!
package com.kcj.phonesuperviser.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class ShortCutActivity$$ViewInjector<T extends com.kcj.phonesuperviser.ui.ShortCutActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165229, "field 'mRelativeLayout'");
    target.mRelativeLayout = finder.castView(view, 2131165229, "field 'mRelativeLayout'");
    view = finder.findRequiredView(source, 2131165232, "field 'cleanLightImg'");
    target.cleanLightImg = finder.castView(view, 2131165232, "field 'cleanLightImg'");
    view = finder.findRequiredView(source, 2131165230, "field 'layoutAnim'");
    target.layoutAnim = finder.castView(view, 2131165230, "field 'layoutAnim'");
  }

  @Override public void reset(T target) {
    target.mRelativeLayout = null;
    target.cleanLightImg = null;
    target.layoutAnim = null;
  }
}
