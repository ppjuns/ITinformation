package com.rabbit.application.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.rabbit.application.R;
import com.rabbit.application.adapter.MyFragmentAdapter;
import com.rabbit.application.ui.fragment.InfoListviewFragment;
import com.rabbit.application.util.Urls;

import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity {
    //声明相关变量
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
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
        getWindow().setBackgroundDrawable(null);
        mConext = this;
        findViews(); //获取控件

        sharedpreferences = getSharedPreferences("textSize", Activity.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.putInt("textSize", 14);
        editor.commit();
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
        String[] data = {"字号大少", "夜间模式", "关于"};
        lvLeftMenu.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data));
        lvLeftMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                switch (position) {
                    case 0:
                        View dialogView = LayoutInflater.from(mConext).inflate(R.layout.textsize, null);
                        final Dialog textSizeDiaog = new Dialog(mConext);
                        textSizeDiaog.setContentView(dialogView);
                        textSizeDiaog.setTitle("字体大小");
                        textSizeDiaog.setCancelable(true);
                        textSizeDiaog.setCanceledOnTouchOutside(true);
                        textSizeDiaog.show();


                        WindowManager mWM = (WindowManager) mConext.getSystemService(Context.WINDOW_SERVICE);

                        Display display = mWM.getDefaultDisplay();
                        Window window = textSizeDiaog.getWindow();
                        WindowManager.LayoutParams lp = window.getAttributes();
                        lp.width = (int) (display.getWidth() * 0.8);
                        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                        window.setAttributes(lp);

                        textSizeDiaog.show();
                        TextView tv_small = (TextView) dialogView.findViewById(R.id.tv_smallsize);
                        TextView tv_normal = (TextView) dialogView.findViewById(R.id.tv_normalsize);
                        TextView tv_big = (TextView) dialogView.findViewById(R.id.tv_bigsize);
                        TextView tv_superbig = (TextView) dialogView.findViewById(R.id.tv_superbigsize);
                        TextView tv_cancel = (TextView) dialogView.findViewById(R.id.tv_cancel);
                       sharedpreferences = getSharedPreferences("textSize", Activity.MODE_PRIVATE);
                        editor = sharedpreferences.edit();
                        tv_small.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                editor.putInt("textSize", 13);
                                editor.commit();
                                textSizeDiaog.dismiss();
                            }
                        });
                        tv_normal.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                editor.putInt("textSize", 14);
                                editor.commit();
                                textSizeDiaog.dismiss();
                            }
                        });

                        tv_big.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                editor.putInt("textSize", 16);
                                editor.commit();
                                textSizeDiaog.dismiss();
                            }
                        });
                        tv_superbig.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                editor.putInt("textSize", 20);
                                editor.commit();
                                textSizeDiaog.dismiss();
                            }
                        });
                        tv_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                textSizeDiaog.dismiss();
                            }
                        });


                        break;


                }
            }
        });


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









}