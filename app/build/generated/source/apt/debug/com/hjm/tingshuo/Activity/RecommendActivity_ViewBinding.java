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

public class RecommendActivity_ViewBinding implements Unbinder {
  private RecommendActivity target;

  private View view2131230868;

  @UiThread
  public RecommendActivity_ViewBinding(RecommendActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public RecommendActivity_ViewBinding(final RecommendActivity target, View source) {
    this.target = target;

    View view;
    target.textView = Utils.findRequiredViewAsType(source, R.id.tv_title, "field 'textView'", TextView.class);
    target.mRecyclerView = Utils.findRequiredViewAsType(source, R.id.recycler, "field 'mRecyclerView'", RecyclerView.class);
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
    RecommendActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.textView = null;
    target.mRecyclerView = null;

    view2131230868.setOnClickListener(null);
    view2131230868 = null;
  }
}
