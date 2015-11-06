// Generated code from Butter Knife. Do not modify!
package com.kcj.phonesuperviser.ui.fragment;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class HomeFragment$$ViewInjector<T extends com.kcj.phonesuperviser.ui.fragment.HomeFragment> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165239, "field 'arcProcess' and method 'arcProgressMemorydUp'");
    target.arcProcess = finder.castView(view, 2131165239, "field 'arcProcess'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.arcProgressMemorydUp();
        }
      });
    view = finder.findRequiredView(source, 2131165238, "field 'capacity'");
    target.capacity = finder.castView(view, 2131165238, "field 'capacity'");
    view = finder.findRequiredView(source, 2131165237, "field 'arcStore' and method 'arcProgressRubbishClean'");
    target.arcStore = finder.castView(view, 2131165237, "field 'arcStore'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.arcProgressRubbishClean();
        }
      });
    view = finder.findRequiredView(source, 2131165248, "method 'SoftwareManage'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.SoftwareManage();
        }
      });
    view = finder.findRequiredView(source, 2131165246, "method 'rubbishClean'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.rubbishClean();
        }
      });
    view = finder.findRequiredView(source, 2131165247, "method 'AutoStartManage'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.AutoStartManage();
        }
      });
    view = finder.findRequiredView(source, 2131165245, "method 'memorydUp'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.memorydUp();
        }
      });
  }

  @Override public void reset(T target) {
    target.arcProcess = null;
    target.capacity = null;
    target.arcStore = null;
  }
}
