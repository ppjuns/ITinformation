package com.rabbit.application.bean;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by Rabbit on 2015/3/17.
 */
public class GetListviewData {


    String result;
    private Handler mHandler;

    public GetListviewData(Context context,Handler mHandler) {
        this.mHandler = mHandler;

        MyVolley.init(context);

    }


    public void getnews(String url) {

        StringRequest stringRequest2 = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // TODO Auto-generated method stub
                        //  Log.d("a", response);

                        ArrayList<Information> list = parser(response,true);
                        if (list.size() > 0) {
                            Message msg = Message.obtain(mHandler, 1, list);
                            msg.sendToTarget();
                        }
//                        if (list.size() > 0)
//                            EventBus.getDefault().post(list);
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
        // stringRequest2.setShouldCache(false);
        MyVolley.addRequest(stringRequest2);


    }


    public ArrayList<Information> parser(String response,boolean isCreateList ) {

        ArrayList<Information> informationList = new ArrayList<Information>();


        Document doc = Jsoup.parse(response);
        // Log.e("doc--->", doc.toString());
        // 获取class="article_item"的所有元素
        Elements blogList = doc.getElementsByClass("cate_list").select("li");
        // Log.e("elements--->", blogList.toString());
        // Log.e("size", blogList.size() + "");


        for (Element blogItem : blogList) {
            Information information = new Information();
            String title = blogItem.select("h2").select("a").text();
            String datetime = blogItem.select("h2").select("span").text();
            String url = blogItem.select("h2").select("a").attr("href");
            String img_url = blogItem.select("a").select("img").attr("data-original");
            String content = blogItem.select("p").text();
            String tags = blogItem.getElementsByClass("tags").text();
//            Log.d("title", title);
//            Log.d("datetime", datetime);
//            Log.d("img_url", img_url);
//            Log.d("url", url);
//            Log.d("content", content);
//            Log.d("tags", tags);


            information.setTitle(title);
            information.setDatetime(datetime);
            information.setImg_url(img_url);
            information.setUrl(url);
            information.setContent(content);
            information.setTags(tags);

            if (!"".equals(information.getImg_url()))
                informationList.add(information);
        }


        return informationList;
    }


}
