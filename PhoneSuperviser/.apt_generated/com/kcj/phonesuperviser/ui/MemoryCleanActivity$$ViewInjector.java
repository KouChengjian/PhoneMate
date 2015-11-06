// Generated code from Butter Knife. Do not modify!
package com.kcj.phonesuperviser.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class MemoryCleanActivity$$ViewInjector<T extends com.kcj.phonesuperviser.ui.MemoryCleanActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165219, "field 'mwaveView'");
    target.mwaveView = finder.castView(view, 2131165219, "field 'mwaveView'");
    view = finder.findRequiredView(source, 2131165222, "field 'mListView'");
    target.mListView = finder.castView(view, 2131165222, "field 'mListView'");
    view = finder.findRequiredView(source, 2131165224, "field 'clearButton' and method 'onClickClear'");
    target.clearButton = finder.castView(view, 2131165224, "field 'clearButton'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClickClear();
        }
      });
    view = finder.findRequiredView(source, 2131165218, "field 'header'");
    target.header = finder.castView(view, 2131165218, "field 'header'");
    view = finder.findRequiredView(source, 2131165223, "field 'bottom_lin'");
    target.bottom_lin = finder.castView(view, 2131165223, "field 'bottom_lin'");
    view = finder.findRequiredView(source, 2131165227, "field 'mProgressBarText'");
    target.mProgressBarText = finder.castView(view, 2131165227, "field 'mProgressBarText'");
    view = finder.findRequiredView(source, 2131165225, "field 'mProgressBar'");
    target.mProgressBar = view;
    view = finder.findRequiredView(source, 2131165221, "field 'sufix'");
    target.sufix = finder.castView(view, 2131165221, "field 'sufix'");
    view = finder.findRequiredView(source, 2131165220, "field 'textCounter'");
    target.textCounter = finder.castView(view, 2131165220, "field 'textCounter'");
  }

  @Override public void reset(T target) {
    target.mwaveView = null;
    target.mListView = null;
    target.clearButton = null;
    target.header = null;
    target.bottom_lin = null;
    target.mProgressBarText = null;
    target.mProgressBar = null;
    target.sufix = null;
    target.textCounter = null;
  }
}
