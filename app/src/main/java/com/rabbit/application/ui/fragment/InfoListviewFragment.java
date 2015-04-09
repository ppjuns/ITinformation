package com.rabbit.application.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.rabbit.application.R;
import com.rabbit.application.adapter.InformationAdapter;
import com.rabbit.application.bean.DVApplication;
import com.rabbit.application.bean.GetListviewData;
import com.rabbit.application.bean.Information;
import com.rabbit.application.bean.MyVolley;
import com.rabbit.application.ui.BaseActivity;
import com.rabbit.application.ui.MainActivity;
import com.rabbit.application.util.Urls;
import com.rabbit.application.widget.XListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Rabbit on 2015/3/16.
 */
public class InfoListviewFragment extends Fragment implements XListView.IXListViewListener {
    ProgressDialog dialog;
    GetListviewData getListviewData;
    XListView lv_android;
    InformationAdapter adapter;
    Context context;
    List<String> list;
    ArrayList<Information> informationList;
    Handler lvHandler;

    ImageButton bt_up;
    boolean isFirstCreate = true;
    String androidResult;
    DVApplication application;
    int currentpage = 1;


    public static final String ARG_INFO_TYPE = "newsTypes";

    public InfoListviewFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public InfoListviewFragment getInstance(String newType) {
        InfoListviewFragment fragment = new InfoListviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_INFO_TYPE, newType);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_information_listview, container, false);
        context = getActivity();
        bt_up = (ImageButton) view.findViewById(R.id.bt_up);
        lv_android = (XListView) view.findViewById(R.id.lv_android);


        lv_android.setPullLoadEnable(true);
        lv_android.setXListViewListener(this);

      getListviewData = new GetListviewData(context, mHandler);

     getListviewData.getnews(getArguments().getString(ARG_INFO_TYPE) + 1 + Urls.URL_END);
        BaseActivity.toolbar.setTitle("柚子加载中...");
        lvHandler = new Handler();


        return view;


    }

    @Override
    public void onResume() {
        super.onResume();


    }


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 1) {


                informationList = (ArrayList<Information>) msg.obj;
                adapter = new InformationAdapter(context, informationList);
                lv_android.setAdapter(adapter);
                MainActivity.toolbar.setTitle("柚子");
                listview();
            }
        }
    };


    private void listview() {

        lv_android.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                BaseActivity.itemonclick(getActivity(), informationList.get(position - 1).getUrl());


            }
        });

    }

    //更新信息
    private void onLoad() {
        lv_android.stopRefresh();
        lv_android.stopLoadMore();

        Calendar c = Calendar.getInstance();
        Date date = c.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        lv_android.setRefreshTime(sdf.format(date));
    }

    //下拉刷新
    @Override
    public void onRefresh() {
        getListviewData.getnews(getArguments().getString(ARG_INFO_TYPE) + 1 + Urls.URL_END);
        onLoad();
    }

    //上提加载
    @Override
    public void onLoadMore() {
        lvHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                loadMore();
                bt_up.setVisibility(View.VISIBLE);
                bt_up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lv_android.setSelection(0);
                        bt_up.setVisibility(View.GONE);
                    }
                });
                onLoad();
            }
        }, 0);
    }


    public void loadMore() {
        currentpage++;
        StringRequest stringRequest2 = new StringRequest(Request.Method.GET,
                getArguments().getString(ARG_INFO_TYPE) + currentpage + Urls.URL_END,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // TODO Auto-generated method stub

                        Document doc = Jsoup.parse(response);

                        Elements blogList = doc.getElementsByClass("cate_list").select("li");


                        for (Element blogItem : blogList) {
                            Information information = new Information();
                            String title = blogItem.select("h2").select("a").text();
                            String datetime = blogItem.select("h2").select("span").text();
                            String url = blogItem.select("h2").select("a").attr("href");
                            String img_url = blogItem.select("a").select("img").attr("data-original");
                            String content = blogItem.select("p").text();
                            String tags = blogItem.getElementsByClass("tags").text();


                            information.setTitle(title);
                            information.setDatetime(datetime);
                            information.setImg_url(img_url);
                            information.setUrl(url);
                            information.setContent(content);
                            information.setTags(tags);

                            if (!"".equals(information.getImg_url()))
                                informationList.add(information);
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub

            }
        }) {

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                // TODO Auto-generated method stub
                String str = null;
                try {
                    str = new String(response.data, "GBK");
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return Response.success(str, HttpHeaderParser.parseCacheHeaders(response));
            }

        };
        MyVolley.addRequest(stringRequest2);

    }


}