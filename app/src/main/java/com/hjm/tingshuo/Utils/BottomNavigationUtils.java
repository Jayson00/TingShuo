package com.hjm.tingshuo.Utils;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;

import java.lang.reflect.Field;

/**
 * Created by linghao on 2018/9/30.
 * space 图片与文字之间的间距
 * imgLen 单位：dp，图片大小，应 <= 36dp
 * textSize 单位：dp，文字大小，应 <= 20dp
 * 使用方法：直接调用setBottomNavigationItem(bottomNavigationBar, 6, 26, 10);
 代表将bottomNavigationBar的文字大小设置为10dp，图片大小为26dp，二者间间距为6dp
 */



public class BottomNavigationUtils {

    private BottomNavigationBar mNavigationBar;
    private Context mContext;

    public BottomNavigationUtils(BottomNavigationBar navigationBar, Context mContext) {
        this.mNavigationBar = navigationBar;
        this.mContext = mContext;
    }

    public void setBottomNavigationItem(int space, int imgLen) {
        float contentLen = 36;
        Class barClass = mNavigationBar.getClass();
        Field[] fields = barClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            if (field.getName().equals("mTabContainer")) {
                try { //反射得到 mTabContainer
                    LinearLayout mTabContainer = (LinearLayout) field.get(mNavigationBar);
                    for (int j = 0; j < mTabContainer.getChildCount(); j++) {                        //获取到容器内的各个 Tab
                        View view = mTabContainer.getChildAt(j);
                        // 获取到Tab内的各个显示控件
                        // 获取到Tab内的文字控件
                        TextView labelView = (TextView) view.findViewById(com.ashokvarma.bottomnavigation.R.id.fixed_bottom_navigation_title);
                        // 计算文字的高度DP值并设置，setTextSize为设置文字正方形的对角线长度，所以：文字高度（总内容高度减去间距和图片高度）*根号2即为对角线长度，此处用DP值，设置该值即可。
                        labelView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (float) (Math.sqrt(2) * (contentLen - imgLen - space)));
                        // 获取到Tab内的图像控件
                        ImageView iconView = (ImageView) view.findViewById(com.ashokvarma.bottomnavigation.R.id.fixed_bottom_navigation_icon);
                        // 设置图片参数，其中，MethodUtils.dip2px()：换算dp值
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(dip2px(imgLen), dip2px(imgLen));
                        params.gravity = Gravity.CENTER;
                        iconView.setLayoutParams(params);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public int dip2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.8f);
    }


}
