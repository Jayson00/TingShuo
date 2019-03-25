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

public class PlayActivity_ViewBinding implements Unbinder {
  private PlayActivity target;

  private View view2131230877;

  private View view2131230880;

  private View view2131230878;

  private View view2131230879;

  private View view2131230874;

  private View view2131230876;

  private View view2131230875;

  private View view2131230889;

  private View view2131230881;

  @UiThread
  public PlayActivity_ViewBinding(PlayActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public PlayActivity_ViewBinding(final PlayActivity target, View source) {
    this.target = target;

    View view;
    target.nameTv = Utils.findRequiredViewAsType(source, R.id.tv_play_name, "field 'nameTv'", TextView.class);
    target.authorTv = Utils.findRequiredViewAsType(source, R.id.tv_play_author, "field 'authorTv'", TextView.class);
    target.currentTv = Utils.findRequiredViewAsType(source, R.id.tv_play_current_duration, "field 'currentTv'", TextView.class);
    target.durationTv = Utils.findRequiredViewAsType(source, R.id.tv_play_duration, "field 'durationTv'", TextView.class);
    target.LyricView = Utils.findRequiredViewAsType(source, R.id.slv_play_show_lrc, "field 'LyricView'", LyricView.class);
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
    view = Utils.findRequiredView(source, R.id.iv_play_lrc, "field 'mlrcIv' and method 'onClick'");
    target.mlrcIv = Utils.castView(view, R.id.iv_play_lrc, "field 'mlrcIv'", ImageView.class);
    view2131230876 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.iv_play_love, "field 'mLoveIv' and method 'onClick'");
    target.mLoveIv = Utils.castView(view, R.id.iv_play_love, "field 'mLoveIv'", ImageView.class);
    view2131230875 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.iv_song_download, "field 'mDowlloadIv' and method 'onClick'");
    target.mDowlloadIv = Utils.castView(view, R.id.iv_song_download, "field 'mDowlloadIv'", ImageView.class);
    view2131230889 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.iv_recommend, "field 'mRecommendIv' and method 'onClick'");
    target.mRecommendIv = Utils.castView(view, R.id.iv_recommend, "field 'mRecommendIv'", ImageView.class);
    view2131230881 = view;
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
    PlayActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.nameTv = null;
    target.authorTv = null;
    target.currentTv = null;
    target.durationTv = null;
    target.LyricView = null;
    target.seekBar = null;
    target.modeIv = null;
    target.preIv = null;
    target.nextIv = null;
    target.playIv = null;
    target.exitIv = null;
    target.mlrcIv = null;
    target.mLoveIv = null;
    target.mDowlloadIv = null;
    target.mRecommendIv = null;

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
    view2131230875.setOnClickListener(null);
    view2131230875 = null;
    view2131230889.setOnClickListener(null);
    view2131230889 = null;
    view2131230881.setOnClickListener(null);
    view2131230881 = null;
  }
}
