// Generated code from Butter Knife. Do not modify!
package com.hjm.tingshuo.Activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.hjm.tingshuo.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class RegistActivity_ViewBinding implements Unbinder {
  private RegistActivity target;

  private View view2131230868;

  private View view2131230769;

  @UiThread
  public RegistActivity_ViewBinding(RegistActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public RegistActivity_ViewBinding(final RegistActivity target, View source) {
    this.target = target;

    View view;
    target.mTextView = Utils.findRequiredViewAsType(source, R.id.tv_title, "field 'mTextView'", TextView.class);
    target.mUserEt = Utils.findRequiredViewAsType(source, R.id.et_user, "field 'mUserEt'", EditText.class);
    target.mFirstEt = Utils.findRequiredViewAsType(source, R.id.et_first_pwd, "field 'mFirstEt'", EditText.class);
    target.mSecondEt = Utils.findRequiredViewAsType(source, R.id.et_second_pwd, "field 'mSecondEt'", EditText.class);
    target.warningLv = Utils.findRequiredViewAsType(source, R.id.lv_warning, "field 'warningLv'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.iv_exit, "method 'onClick'");
    view2131230868 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_commit, "method 'onClick'");
    view2131230769 = view;
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
    RegistActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mTextView = null;
    target.mUserEt = null;
    target.mFirstEt = null;
    target.mSecondEt = null;
    target.warningLv = null;

    view2131230868.setOnClickListener(null);
    view2131230868 = null;
    view2131230769.setOnClickListener(null);
    view2131230769 = null;
  }
}
