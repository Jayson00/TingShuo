// Generated code from Butter Knife. Do not modify!
package com.hjm.tingshuo.Activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.hjm.tingshuo.MyView.ViewPagerFix;
import com.hjm.tingshuo.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class HomeActivity_ViewBinding implements Unbinder {
  private HomeActivity target;

  private View view2131230886;

  private View view2131230887;

  private View view2131230888;

  @UiThread
  public HomeActivity_ViewBinding(HomeActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public HomeActivity_ViewBinding(final HomeActivity target, View source) {
    this.target = target;

    View view;
    target.mNavigationBar = Utils.findRequiredViewAsType(source, R.id.home_bottemnavigation, "field 'mNavigationBar'", BottomNavigationBar.class);
    target.mViewPager = Utils.findRequiredViewAsType(source, R.id.home_viewpager, "field 'mViewPager'", ViewPagerFix.class);
    view = Utils.findRequiredView(source, R.id.iv_skip_classification, "method 'onClick'");
    view2131230886 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.iv_skip_friend, "method 'onClick'");
    view2131230887 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.iv_skip_search, "method 'onClick'");
    view2131230888 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    HomeActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mNavigationBar = null;
    target.mViewPager = null;

    view2131230886.setOnClickListener(null);
    view2131230886 = null;
    view2131230887.setOnClickListener(null);
    view2131230887 = null;
    view2131230888.setOnClickListener(null);
    view2131230888 = null;
  }
}
