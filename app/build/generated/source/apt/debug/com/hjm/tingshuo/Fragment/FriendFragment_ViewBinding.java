// Generated code from Butter Knife. Do not modify!
package com.hjm.tingshuo.Fragment;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.hjm.tingshuo.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class FriendFragment_ViewBinding implements Unbinder {
  private FriendFragment target;

  @UiThread
  public FriendFragment_ViewBinding(FriendFragment target, View source) {
    this.target = target;

    target.mGrid1 = Utils.findRequiredViewAsType(source, R.id.grid_1, "field 'mGrid1'", GridView.class);
    target.mGrid2 = Utils.findRequiredViewAsType(source, R.id.grid_2, "field 'mGrid2'", GridView.class);
    target.mRecyclerView = Utils.findRequiredViewAsType(source, R.id.recycler_new, "field 'mRecyclerView'", RecyclerView.class);
    target.mBtnmore1 = Utils.findRequiredViewAsType(source, R.id.btn_more_1, "field 'mBtnmore1'", Button.class);
    target.mBtnmore2 = Utils.findRequiredViewAsType(source, R.id.btn_more_2, "field 'mBtnmore2'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    FriendFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mGrid1 = null;
    target.mGrid2 = null;
    target.mRecyclerView = null;
    target.mBtnmore1 = null;
    target.mBtnmore2 = null;
  }
}
