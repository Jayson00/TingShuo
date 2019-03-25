// Generated code from Butter Knife. Do not modify!
package com.hjm.tingshuo.Activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.hjm.tingshuo.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LoginActivity_ViewBinding implements Unbinder {
  private LoginActivity target;

  private View view2131230769;

  private View view2131230772;

  private View view2131230868;

  private View view2131231050;

  @UiThread
  public LoginActivity_ViewBinding(LoginActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public LoginActivity_ViewBinding(final LoginActivity target, View source) {
    this.target = target;

    View view;
    target.mTextView = Utils.findRequiredViewAsType(source, R.id.tv_title, "field 'mTextView'", TextView.class);
    target.mImageView = Utils.findRequiredViewAsType(source, R.id.iv_upload, "field 'mImageView'", ImageView.class);
    target.mUserEt = Utils.findRequiredViewAsType(source, R.id.et_user, "field 'mUserEt'", EditText.class);
    target.mPwdEt = Utils.findRequiredViewAsType(source, R.id.et_pwd, "field 'mPwdEt'", EditText.class);
    view = Utils.findRequiredView(source, R.id.btn_commit, "field 'mCommitBtn' and method 'onClick'");
    target.mCommitBtn = Utils.castView(view, R.id.btn_commit, "field 'mCommitBtn'", Button.class);
    view2131230769 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_login_out, "field 'mLoginOutBtn' and method 'onClick'");
    target.mLoginOutBtn = Utils.castView(view, R.id.btn_login_out, "field 'mLoginOutBtn'", Button.class);
    view2131230772 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.mCheckBox = Utils.findRequiredViewAsType(source, R.id.cb_remember, "field 'mCheckBox'", CheckBox.class);
    view = Utils.findRequiredView(source, R.id.iv_exit, "method 'onClick'");
    view2131230868 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.tv_regist, "method 'onClick'");
    view2131231050 = view;
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
    LoginActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mTextView = null;
    target.mImageView = null;
    target.mUserEt = null;
    target.mPwdEt = null;
    target.mCommitBtn = null;
    target.mLoginOutBtn = null;
    target.mCheckBox = null;

    view2131230769.setOnClickListener(null);
    view2131230769 = null;
    view2131230772.setOnClickListener(null);
    view2131230772 = null;
    view2131230868.setOnClickListener(null);
    view2131230868 = null;
    view2131231050.setOnClickListener(null);
    view2131231050 = null;
  }
}
