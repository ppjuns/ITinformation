package com.rabbit.application.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.rabbit.application.R;
import com.rabbit.application.bean.MyVolley;
import com.rabbit.application.bean.TouchImageView;
import com.rabbit.application.util.HttpUtils;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;
import in.srain.cube.views.ptr.util.PtrLocalDisplay;


/**
 * Created by Rabbit on 2015/3/23.
 */
public class InfoActivity extends BaseActivity {


    private Context context;
    private LinearLayout linearLayout;
    private TextView tv_title;
    private TextView tv_datetime;
    private Dialog dialog;
    private StringRequest htmlinfo;
    private PtrFrameLayout frame;
    private MaterialHeader header;
    private boolean mIsFirstLoad = true;
    private ScrollView scrollView;

    private String title;
    private Intent intent;
    private UMSocialService mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        context = this;
        initView();

        PtrFrameLayout();
        intent = getIntent();


        if (HttpUtils.checkNetWork(context)) {

            toolbar.setTitle("加载中...");
        } else {
            toolbar.setTitle("加载失败，请检查网络设置");
        }

        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF")); //设置标题颜色
        setSupportActionBar(toolbar);


        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_arrow_back_white_18dp));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.action_share:


                        mController = UMServiceFactory.getUMSocialService("com.umeng.share");

                        mController.getConfig().removePlatform(SHARE_MEDIA.SINA, SHARE_MEDIA.TENCENT);
                        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(InfoActivity.this, "1104708607",
                                "DLknGU9N1dvzKBuN");
                        qqSsoHandler.addToSocialSDK();


                        QQShareContent qqShareContent = new QQShareContent();

                        qqShareContent.setShareContent(title);
                        qqShareContent.setShareImage(new UMImage(InfoActivity.this, R.mipmap.ic_launcher));

                        qqShareContent.setTargetUrl(intent.getStringExtra("url").toString());
                        mController.setShareMedia(qqShareContent);


                        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(InfoActivity.this, "1104708607", "DLknGU9N1dvzKBuN");
                        qZoneSsoHandler.addToSocialSDK();


                        QZoneShareContent qZoneShareContent = new QZoneShareContent();
                        qZoneShareContent.setShareContent(title);
                        qZoneShareContent.setTargetUrl(intent.getStringExtra("url").toString());
                        qZoneShareContent.setShareImage(new UMImage(InfoActivity.this, R.mipmap.ic_launcher));
                        mController.setShareMedia(qZoneShareContent);

                        String appID = "wx8ac7d6b8c9f59a39";
                        String appSecret = "172d1fb3212d9f02ab4e08cfe317479a";
                        UMWXHandler wxHandler = new UMWXHandler(context, appID, appSecret);
                        wxHandler.addToSocialSDK();
                        WeiXinShareContent weiXinShareContent = new WeiXinShareContent();
                        weiXinShareContent.setShareImage(new UMImage(InfoActivity.this, R.mipmap.ic_launcher));
                        weiXinShareContent.setTitle(title);
                        weiXinShareContent.setTargetUrl(intent.getStringExtra("url"));
                        mController.setShareMedia(weiXinShareContent);


                        UMWXHandler wxCircleHandler = new UMWXHandler(context, appID, appSecret);
                        wxCircleHandler.setToCircle(true);
                        wxCircleHandler.addToSocialSDK();

                        CircleShareContent circleMedia = new CircleShareContent();
                        circleMedia.setShareContent(title);
//设置朋友圈title

                        circleMedia.setShareImage(new UMImage(InfoActivity.this, R.mipmap.ic_launcher));
                        circleMedia.setTargetUrl(title);
                        mController.setShareMedia(circleMedia);


                        mController.openShare(InfoActivity.this, false);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });


        MyVolley.getInstance(context);
        htmlinfo = new

                StringRequest(Request.Method.GET, intent.getStringExtra("url").toString(),

                        new Response.Listener<String>()

                        {
                            @Override
                            public void onResponse(String response) {


                                Document doc = Jsoup.parse(response);

                                title = doc.getElementsByClass("post_title").select("h1").text();
                                String datetime1 = doc.getElementsByClass("post_title").select("span").get(1).text();

                                tv_title.setText("\t\t" + title);

                                tv_datetime.setText(datetime1);

                                Elements contents = doc.getElementsByClass("post_content").select("p");

                                for (final Element content : contents) {

                                    String cc = content.text();

                                    String img1 = content.select("img").attr("src");
                                    String img2 = content.select("img").attr("data-original");

                                    if (!"".equals(cc)) {

                                        SharedPreferences sharedPreferences = getSharedPreferences("textSize", Activity.MODE_PRIVATE);

                                        int textsize = sharedPreferences.getInt("textSize", 0);

                                        TextView textView = new TextView(linearLayout.getContext());


                                        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textsize);

                                        textView.setText("\t\t" + cc);

                                        textView.setLineSpacing(0, 1.5f);
                                        textView.setTextColor(Color.parseColor("#2c2c2c"));
                                        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
                                        linearLayout.addView(textView);

                                    }

                                    if (!"".equals(img1)) {

                                        final NetworkImageView networkImageView1 = new NetworkImageView(linearLayout.getContext());
                                        networkImageView1.setDefaultImageResId(R.mipmap.ic_launcher);
                                        networkImageView1.setErrorImageResId(R.mipmap.ic_launcher);
                                        networkImageView1.setImageUrl(img1, MyVolley.getImageLoader());

                                        linearLayout.addView(networkImageView1);

                                        networkImageView1.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                getDialog(networkImageView1.getDrawable());
                                            }
                                        });

                                    }
                                    if (!"".equals(img2)) {

                                        final NetworkImageView networkImageView2 = new NetworkImageView(linearLayout.getContext());
                                        networkImageView2.setDefaultImageResId(R.mipmap.ic_launcher);
                                        networkImageView2.setErrorImageResId(R.mipmap.ic_launcher);
                                        networkImageView2.setImageUrl(img2, MyVolley.getImageLoader());

                                        linearLayout.addView(networkImageView2);

                                        networkImageView2.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                getDialog(networkImageView2.getDrawable());

                                            }
                                        });


                                    }

                                }

                                toolbar.setTitle("资讯");

                            }
                        }

                        , new Response.ErrorListener()

                {
                    @Override
                    public void onErrorResponse(VolleyError e) {

                    }
                }

                )

                {
                    @Override
                    protected Response<String> parseNetworkResponse(NetworkResponse response) {

                        String str = null;

                        try {
                            str = new String(response.data, "GBK");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        return Response.success(str, HttpHeaderParser.parseCacheHeaders(response));
                    }
                };


    }


    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.tl_custom);
        tv_title = (TextView) findViewById(R.id.tv_title);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        tv_datetime = (TextView) findViewById(R.id.tv_datetime);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public void PtrFrameLayout() {

        frame = (PtrFrameLayout) findViewById(R.id.material_style_ptr_frame);
        header = new MaterialHeader(context);
        int[] colors = getResources().getIntArray(R.array.google_colors);
        header.setColorSchemeColors(colors);
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, PtrLocalDisplay.dp2px(15), 0, PtrLocalDisplay.dp2px(10));
        header.setPtrFrameLayout(frame);

        frame.setLoadingMinTime(1000);
        frame.setDurationToCloseHeader(300);
        frame.setHeaderView(header);
        frame.addPtrUIHandler(header);
        frame.postDelayed(new Runnable() {
            @Override
            public void run() {
                frame.autoRefresh(false);
            }
        }, 100);

        frame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout ptrFrameLayout, View view, View view2) {
                return scrollView.getScrollY() == 0;
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout ptrFrameLayout) {
                MyVolley.addRequest(htmlinfo);
                frame.refreshComplete();
            }
        });
    }

    private void getDialog(Drawable drawable) {
        if (null != dialog) {
            dialog.dismiss();
            return;
        } else {
            initDialog(drawable);
        }
    }

    protected void initDialog(Drawable drawable) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_infopic, null);
        final Dialog dialog = new Dialog(context, R.style.MyDialog);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Window window = dialog.getWindow();

        WindowManager.LayoutParams lp = window.getAttributes();

        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);


        TouchImageView touchImageView = (TouchImageView) view.findViewById(R.id.img_touchImageView);
        touchImageView.setImageDrawable(drawable);
    }


}
