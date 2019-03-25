// Generated code from Butter Knife. Do not modify!
package com.hjm.tingshuo.Fragment;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.hjm.tingshuo.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class Mine_testFragment_ViewBinding implements Unbinder {
  private Mine_testFragment target;

  private View view2131230914;

  private View view2131230916;

  private View view2131230911;

  private View view2131230915;

  @UiThread
  public Mine_testFragment_ViewBinding(final Mine_testFragment target, View source) {
    this.target = target;

    View view;
    target.mRecyclerview = Utils.findRequiredViewAsType(source, R.id.recyclerview, "field 'mRecyclerview'", RecyclerView.class);
    target.mTvSelectNum = Utils.findRequiredViewAsType(source, R.id.tv_select_num, "field 'mTvSelectNum'", TextView.class);
    target.mBtnDelete = Utils.findRequiredViewAsType(source, R.id.btn_delete, "field 'mBtnDelete'", Button.class);
    target.mSelectAll = Utils.findRequiredViewAsType(source, R.id.select_all, "field 'mSelectAll'", TextView.class);
    target.mLlMycollectionBottomDialog = Utils.findRequiredViewAsType(source, R.id.ll_mycollection_bottom_dialog, "field 'mLlMycollectionBottomDialog'", LinearLayout.class);
    target.mLvEditor = Utils.findRequiredViewAsType(source, R.id.lv_editor, "field 'mLvEditor'", LinearLayout.class);
    target.mLvAdd = Utils.findRequiredViewAsType(source, R.id.lv_add, "field 'mLvAdd'", LinearLayout.class);
    target.mTvEditor = Utils.findRequiredViewAsType(source, R.id.btn_editor, "field 'mTvEditor'", Button.class);
    view = Utils.findRequiredView(source, R.id.lv_local, "method 'onClick'");
    view2131230914 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.lv_love, "method 'onClick'");
    view2131230916 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.lv_friend, "method 'onClick'");
    view2131230911 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.lv_login, "method 'onClick'");
    view2131230915 = view;
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
    Mine_testFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mRecyclerview = null;
    target.mTvSelectNum = null;
    target.mBtnDelete = null;
    target.mSelectAll = null;
    target.mLlMycollectionBottomDialog = null;
    target.mLvEditor = null;
    target.mLvAdd = null;
    target.mTvEditor = null;

    view2131230914.setOnClickListener(null);
    view2131230914 = null;
    view2131230916.setOnClickListener(null);
    view2131230916 = null;
    view2131230911.setOnClickListener(null);
    view2131230911 = null;
    view2131230915.setOnClickListener(null);
    view2131230915 = null;
  }
}
