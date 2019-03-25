// Generated code from Butter Knife. Do not modify!
package com.hjm.tingshuo.Activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.hjm.tingshuo.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class NewActivity_ViewBinding implements Unbinder {
  private NewActivity target;

  @UiThread
  public NewActivity_ViewBinding(NewActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public NewActivity_ViewBinding(NewActivity target, View source) {
    this.target = target;

    target.mTextView = Utils.findRequiredViewAsType(source, R.id.tv_title, "field 'mTextView'", TextView.class);
    target.mRecyclerView = Utils.findRequiredViewAsType(source, R.id.recycler, "field 'mRecyclerView'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    NewActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mTextView = null;
    target.mRecyclerView = null;
  }
}
