// Generated code from Butter Knife. Do not modify!
package com.hjm.tingshuo.Activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.hjm.tingshuo.MyView.LyricView;
import com.hjm.tingshuo.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LocalPlayActivity_ViewBinding implements Unbinder {
  private LocalPlayActivity target;

  private View view2131230877;

  private View view2131230880;

  private View view2131230878;

  private View view2131230879;

  private View view2131230874;

  private View view2131230876;

  @UiThread
  public LocalPlayActivity_ViewBinding(LocalPlayActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public LocalPlayActivity_ViewBinding(final LocalPlayActivity target, View source) {
    this.target = target;

    View view;
    target.nameTv = Utils.findRequiredViewAsType(source, R.id.tv_play_name, "field 'nameTv'", TextView.class);
    target.authorTv = Utils.findRequiredViewAsType(source, R.id.tv_play_author, "field 'authorTv'", TextView.class);
    target.currentTv = Utils.findRequiredViewAsType(source, R.id.tv_play_current_duration, "field 'currentTv'", TextView.class);
    target.durationTv = Utils.findRequiredViewAsType(source, R.id.tv_play_duration, "field 'durationTv'", TextView.class);
    target.seekBar = Utils.findRequiredViewAsType(source, R.id.sb_play_position, "field 'seekBar'", SeekBar.class);
    view = Utils.findRequiredView(source, R.id.iv_play_mode_all, "field 'modeIv' and method 'onClick'");
    target.modeIv = Utils.castView(view, R.id.iv_play_mode_all, "field 'modeIv'", ImageView.class);
    view2131230877 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.iv_play_pre, "field 'preIv' and method 'onClick'");
    target.preIv = Utils.castView(view, R.id.iv_play_pre, "field 'preIv'", ImageView.class);
    view2131230880 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.iv_play_next, "field 'nextIv' and method 'onClick'");
    target.nextIv = Utils.castView(view, R.id.iv_play_next, "field 'nextIv'", ImageView.class);
    view2131230878 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.iv_play_playing, "field 'playIv' and method 'onClick'");
    target.playIv = Utils.castView(view, R.id.iv_play_playing, "field 'playIv'", ImageView.class);
    view2131230879 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.iv_play_exit, "field 'exitIv' and method 'onClick'");
    target.exitIv = Utils.castView(view, R.id.iv_play_exit, "field 'exitIv'", ImageView.class);
    view2131230874 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.mLyricView = Utils.findRequiredViewAsType(source, R.id.slv_play_show_lrc, "field 'mLyricView'", LyricView.class);
    view = Utils.findRequiredView(source, R.id.iv_play_lrc, "method 'onClick'");
    view2131230876 = view;
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
    LocalPlayActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.nameTv = null;
    target.authorTv = null;
    target.currentTv = null;
    target.durationTv = null;
    target.seekBar = null;
    target.modeIv = null;
    target.preIv = null;
    target.nextIv = null;
    target.playIv = null;
    target.exitIv = null;
    target.mLyricView = null;

    view2131230877.setOnClickListener(null);
    view2131230877 = null;
    view2131230880.setOnClickListener(null);
    view2131230880 = null;
    view2131230878.setOnClickListener(null);
    view2131230878 = null;
    view2131230879.setOnClickListener(null);
    view2131230879 = null;
    view2131230874.setOnClickListener(null);
    view2131230874 = null;
    view2131230876.setOnClickListener(null);
    view2131230876 = null;
  }
}
