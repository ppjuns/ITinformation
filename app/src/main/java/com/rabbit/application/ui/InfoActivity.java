package com.rabbit.application.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.rabbit.application.R;
import com.rabbit.application.bean.MyVolley;
import com.rabbit.application.util.HttpUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;


/**
 * Created by Rabbit on 2015/3/23.
 */
public class InfoActivity extends BaseActivity {


    Context context;
    LinearLayout linearLayout;
    TextView tv_title;
    TextView tv_datetime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        context = this;
        initView();


        Intent intent = getIntent();



        if(HttpUtils.checkNetWork(context)){

            toolbar.setTitle("加载中...");
        }else{
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
                        Toast.makeText(InfoActivity.this, "action_share", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        MyVolley.init(context);
        StringRequest htmlinfo = new StringRequest(Request.Method.GET, intent.getStringExtra("url").toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Document doc = Jsoup.parse(response);
                Elements t = doc.getElementsByClass("post_title");
                String title = doc.getElementsByClass("post_title").select("h1").text();
                String datetime1 = doc.getElementsByClass("post_title").select("span").get(1).text();

                tv_title.setText("\t\t" + title);

                tv_datetime.setText(datetime1);

                Elements contents = doc.getElementsByClass("post_content").select("p");

                for (Element content : contents) {

                    String cc = content.text();

                    String img1 = content.select("img").attr("src");
                    String img2 = content.select("img").attr("data-original");
                    Log.d("cc", cc);
                    Log.d("img", img1);
                    Log.d("img", img2);
                    if (!"".equals(cc)) {
                        TextView textView = new TextView(linearLayout.getContext());
                        textView.setText("\t\t" + cc);
                        textView.setLineSpacing(0, 1.5f);
                        textView.setTextColor(Color.parseColor("#2c2c2c"));
                        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
                        linearLayout.addView(textView);

                    }

                    if (!"".equals(img1)) {

                        NetworkImageView networkImageView1 = new NetworkImageView(linearLayout.getContext());
                        networkImageView1.setDefaultImageResId(R.mipmap.ic_launcher);
                        networkImageView1.setErrorImageResId(R.mipmap.ic_launcher);
                        networkImageView1.setImageUrl(img1, MyVolley.getImageLoader());

                        linearLayout.addView(networkImageView1);
                    }
                    if (!"".equals(img2)) {

                        NetworkImageView networkImageView2 = new NetworkImageView(linearLayout.getContext());
                        networkImageView2.setDefaultImageResId(R.mipmap.ic_launcher);
                        networkImageView2.setErrorImageResId(R.mipmap.ic_launcher);
                        networkImageView2.setImageUrl(img2, MyVolley.getImageLoader());

                        linearLayout.addView(networkImageView2);


                    }

                }

                toolbar.setTitle("资讯");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {

            }
        }
        ) {
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

        MyVolley.addRequest(htmlinfo);
    }


    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.tl_custom);
        tv_title = (TextView) findViewById(R.id.tv_title);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        tv_datetime = (TextView) findViewById(R.id.tv_datetime);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
