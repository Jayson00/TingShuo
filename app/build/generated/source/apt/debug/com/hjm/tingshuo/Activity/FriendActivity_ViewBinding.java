// Generated code from Butter Knife. Do not modify!
package com.hjm.tingshuo.Activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.hjm.tingshuo.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class FriendActivity_ViewBinding implements Unbinder {
  private FriendActivity target;

  private View view2131230867;

  private View view2131230910;

  @UiThread
  public FriendActivity_ViewBinding(FriendActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public FriendActivity_ViewBinding(final FriendActivity target, View source) {
    this.target = target;

    View view;
    target.textView = Utils.findRequiredViewAsType(source, R.id.tv_show_title, "field 'textView'", TextView.class);
    target.mRecyclerView = Utils.findRequiredViewAsType(source, R.id.friend_recycler, "field 'mRecyclerView'", RecyclerView.class);
    target.mNoFritv = Utils.findRequiredViewAsType(source, R.id.tv_no_friuser, "field 'mNoFritv'", TextView.class);
    view = Utils.findRequiredView(source, R.id.iv_add_friend, "method 'onClick'");
    view2131230867 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.lv_exit, "method 'onClick'");
    view2131230910 = view;
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
    FriendActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.textView = null;
    target.mRecyclerView = null;
    target.mNoFritv = null;

    view2131230867.setOnClickListener(null);
    view2131230867 = null;
    view2131230910.setOnClickListener(null);
    view2131230910 = null;
  }
}
