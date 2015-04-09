package com.rabbit.application.ui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.rabbit.application.R;
import com.rabbit.application.adapter.MyFragmentAdapter;
import com.rabbit.application.ui.fragment.InfoListviewFragment;
import com.rabbit.application.util.Urls;

import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity implements ObservableScrollViewCallbacks {
    //声明相关变量

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView lvLeftMenu;

    private ArrayAdapter arrayAdapter;
    private Context mConext;
    //声明viewpage tab
    String result;
    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private MyFragmentAdapter mMyPagerAdapter;
    private ViewGroup viewGroup;
    Document doc;
    Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            Log.d("titile", msg.obj.toString());

        }

    };
    private final int CURRENT_VERSION = Build.VERSION.SDK_INT;

    private final int VERSION_KITKAT = Build.VERSION_CODES.KITKAT;

    //exit
    private boolean isExit = false;
    private Handler mHandle = new Handler() {
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mConext = this;
        findViews(); //获取控件


        toolbar.setTitle("柚子");//设置Toolbar标题
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF")); //设置标题颜色

        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //创建返回键，并实现打开关/闭监听
        mDrawerToggle = new

                ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.hello_world, R.string.hello_world) {
                    @Override
                    public void onDrawerOpened(View drawerView) {
                        super.onDrawerOpened(drawerView);

                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        super.onDrawerClosed(drawerView);

                    }
                }

        ;
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        //设置菜单列表


        initViewPager();


        tabs.setViewPager(pager);

        tabs.setTextColor(Color.parseColor("#747474"));

        final int pagerMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        pager.setPageMargin(pagerMargin);
        pager.setOffscreenPageLimit(5);
        pager.setCurrentItem(0);
        String[] data = {"字号大少", "关于"};
        lvLeftMenu.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data));

    }

    private void findViews() {
        viewGroup = (ViewGroup) findViewById(R.id.content);
        toolbar = (Toolbar) findViewById(R.id.tl_custom);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_left);

        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        lvLeftMenu = (ListView) findViewById(R.id.lv_menu);

    }

    public void initViewPager() {
        List<Fragment> list = new ArrayList<Fragment>();
        InfoListviewFragment androidFragment = new InfoListviewFragment().getInstance(Urls.AndroidURL);
        InfoListviewFragment iphoneFragment = new InfoListviewFragment().getInstance(Urls.IPHONEURL);
        InfoListviewFragment wpFragment = new InfoListviewFragment().getInstance(Urls.WpURL);
        InfoListviewFragment digitalFragment = new InfoListviewFragment().getInstance(Urls.DIGIURL);
        InfoListviewFragment nextFragment = new InfoListviewFragment().getInstance(Urls.NEXTURL);
        InfoListviewFragment discoveryFragment = new InfoListviewFragment().getInstance(Urls.DISCOVERYURL);


        list.add(androidFragment);
        list.add(iphoneFragment);
        list.add(wpFragment);
        list.add(digitalFragment);
        list.add(nextFragment);
        list.add(discoveryFragment);

        mMyPagerAdapter = new MyFragmentAdapter(getSupportFragmentManager(), list);
        pager.setAdapter(mMyPagerAdapter);
    }


    /**
     * 实现点击两次退出程序
     */
    private void exit() {
        if (isExit) {
            finish();
            System.exit(0);
        } else {
            isExit = true;
            Toast.makeText(getApplicationContext(), R.string.hello_world, Toast.LENGTH_SHORT).show();
            //两秒内不点击back则重置mIsExit
            mHandle.sendEmptyMessageDelayed(0, 2000);
        }
    }


    @Override
    public void onScrollChanged(int i, boolean b, boolean b2) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        if (scrollState == ScrollState.UP) {
            if (toolbarIsShown()) {
                hideToolbar();
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (toolbarIsHidden()) {
                showToolbar();
            }
        }
    }

    private boolean toolbarIsShown() {
        return toolbar.getTranslationY() == 0;
    }

    private boolean toolbarIsHidden() {
        return toolbar.getTranslationY() == -toolbar.getHeight();
    }

    private void showToolbar() {
        moveToolbar(0);
    }


    private void hideToolbar() {
        moveToolbar(-toolbar.getHeight());
    }

    /**
     * 将toolbar移动到某个位置
     *
     * @param toTranslationY 移动到的Y轴位置
     */
    private void moveToolbar(float toTranslationY) {
        if (toolbar.getTranslationY() == toTranslationY) {
            return;
        }
        //利用动画过渡移动的过程
        final ValueAnimator animator = ValueAnimator.ofFloat(toolbar.getTranslationY(), toTranslationY).
                setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float translationY = (Float) animator.getAnimatedValue();
                toolbar.setTranslationY(translationY);
                toolbar.setTranslationY(translationY);
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) viewGroup.getLayoutParams();
                lp.height = (int) (getScreenHeight() - translationY - getStatusBarHeight()
                        - lp.topMargin);
                if (CURRENT_VERSION >= VERSION_KITKAT) {
                    lp.height -= getNavigationBarHeight();
                }
                Log.i("TEST", "after" + Float.toString(toolbar.getHeight()));
                viewGroup.requestLayout();
            }
        });
        animator.start();
    }

    /**
     * 获取navigation bar的高度
     *
     * @return
     */
    protected int getNavigationBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    protected int getScreenHeight() {
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 获取状态栏的高度
     *
     * @return
     */
    protected int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


}