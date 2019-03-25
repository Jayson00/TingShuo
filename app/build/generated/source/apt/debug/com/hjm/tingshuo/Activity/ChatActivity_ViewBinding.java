// Generated code from Butter Knife. Do not modify!
package com.hjm.tingshuo.Activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.hjm.tingshuo.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ChatActivity_ViewBinding implements Unbinder {
  private ChatActivity target;

  private View view2131230890;

  private View view2131230777;

  @UiThread
  public ChatActivity_ViewBinding(ChatActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ChatActivity_ViewBinding(final ChatActivity target, View source) {
    this.target = target;

    View view;
    target.mTextView = Utils.findRequiredViewAsType(source, R.id.tv_title, "field 'mTextView'", TextView.class);
    target.mRecyclerView = Utils.findRequiredViewAsType(source, R.id.recycler_chat, "field 'mRecyclerView'", RecyclerView.class);
    target.mTxtEt = Utils.findRequiredViewAsType(source, R.id.et_txt, "field 'mTxtEt'", EditText.class);
    target.mVoiceBtn = Utils.findRequiredViewAsType(source, R.id.btn_voice, "field 'mVoiceBtn'", Button.class);
    view = Utils.findRequiredView(source, R.id.iv_switch_type, "field 'mSwitchIv' and method 'onClick'");
    target.mSwitchIv = Utils.castView(view, R.id.iv_switch_type, "field 'mSwitchIv'", ImageView.class);
    view2131230890 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_send, "field 'mSendBtn' and method 'onClick'");
    target.mSendBtn = Utils.castView(view, R.id.btn_send, "field 'mSendBtn'", Button.class);
    view2131230777 = view;
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
    ChatActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mTextView = null;
    target.mRecyclerView = null;
    target.mTxtEt = null;
    target.mVoiceBtn = null;
    target.mSwitchIv = null;
    target.mSendBtn = null;

    view2131230890.setOnClickListener(null);
    view2131230890 = null;
    view2131230777.setOnClickListener(null);
    view2131230777 = null;
  }
}
