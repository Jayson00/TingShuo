// Generated code from Butter Knife. Do not modify!
package com.hjm.tingshuo.Activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.hjm.tingshuo.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ClassificationActivity_ViewBinding implements Unbinder {
  private ClassificationActivity target;

  private View view2131230779;

  private View view2131230783;

  private View view2131230784;

  private View view2131230782;

  private View view2131230786;

  private View view2131230788;

  private View view2131230785;

  private View view2131230781;

  private View view2131230787;

  private View view2131230780;

  private View view2131230868;

  @UiThread
  public ClassificationActivity_ViewBinding(ClassificationActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ClassificationActivity_ViewBinding(final ClassificationActivity target, View source) {
    this.target = target;

    View view;
    target.mTextView = Utils.findRequiredViewAsType(source, R.id.tv_title, "field 'mTextView'", TextView.class);
    view = Utils.findRequiredView(source, R.id.btn_type_1, "method 'onClick'");
    view2131230779 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_type_2, "method 'onClick'");
    view2131230783 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_type_20, "method 'onClick'");
    view2131230784 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_type_15, "method 'onClick'");
    view2131230782 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_type_22, "method 'onClick'");
    view2131230786 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_type_25, "method 'onClick'");
    view2131230788 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_type_21, "method 'onClick'");
    view2131230785 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_type_14, "method 'onClick'");
    view2131230781 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_type_23, "method 'onClick'");
    view2131230787 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_type_10, "method 'onClick'");
    view2131230780 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.iv_exit, "method 'onClick'");
    view2131230868 = view;
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
    ClassificationActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mTextView = null;

    view2131230779.setOnClickListener(null);
    view2131230779 = null;
    view2131230783.setOnClickListener(null);
    view2131230783 = null;
    view2131230784.setOnClickListener(null);
    view2131230784 = null;
    view2131230782.setOnClickListener(null);
    view2131230782 = null;
    view2131230786.setOnClickListener(null);
    view2131230786 = null;
    view2131230788.setOnClickListener(null);
    view2131230788 = null;
    view2131230785.setOnClickListener(null);
    view2131230785 = null;
    view2131230781.setOnClickListener(null);
    view2131230781 = null;
    view2131230787.setOnClickListener(null);
    view2131230787 = null;
    view2131230780.setOnClickListener(null);
    view2131230780 = null;
    view2131230868.setOnClickListener(null);
    view2131230868 = null;
  }
}
