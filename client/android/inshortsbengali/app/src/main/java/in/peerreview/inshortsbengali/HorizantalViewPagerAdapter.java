package in.peerreview.inshortsbengali;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by ddutta on 7/11/2017.
 */
public class HorizantalViewPagerAdapter  extends PagerAdapter {

    private Context context;

    public static VerticalViewPager verticalViewPager;
    public VerticlePagerAdapter verticlePagerAdapter;
    private static OkHttpClient mHttpclient = new OkHttpClient();

    static int page = -1; // Note that at the time of fetching we are trying to get the next one and once we get it incremnet the page
    final static int limit = 10;

    public HorizantalViewPagerAdapter(Context context) {
        super();
        this.context = context;
        mHttpclient.setConnectTimeout(30, TimeUnit.SECONDS);
        mHttpclient.setReadTimeout(30, TimeUnit.SECONDS);
        mHttpclient.setWriteTimeout(30, TimeUnit.SECONDS);
    }


    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        //LayoutInflater inflater = LayoutInflater.from(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);

        View view = null;
        switch (position){
            case 0:
                view = inflater.inflate(R.layout.left_pane, collection, false);//MemoryView.getView(context, collection);

                break;
            case 1:
                view = inflater.inflate(R.layout.middle_pane, collection, false);;
                verticalViewPager = (VerticalViewPager) view.findViewById(R.id.verticleViewPager);
                verticlePagerAdapter = new VerticlePagerAdapter(context);
                verticalViewPager.setAdapter(verticlePagerAdapter);
                verticalViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    public void onPageScrollStateChanged(int state) {}
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
                    public void onPageSelected(int position) {
                        Log.i("Dipankar", "position: " + position);
                        LoadRemoteData(oldquery);
                    }
                });
/*
                view.setOnTouchListener(new OnSwipeTouchListener(context) {
                    public void onSwipeTop() {
                        Toast.makeText(context, "top", Toast.LENGTH_SHORT).show();
                    }
                    public void onSwipeRight() {
                        Toast.makeText(context, "right", Toast.LENGTH_SHORT).show();
                    }
                    public void onSwipeLeft() {
                        Toast.makeText(context, "left", Toast.LENGTH_SHORT).show();
                    }
                    public void onSwipeBottom() {
                        Toast.makeText(context, "bottom", Toast.LENGTH_SHORT).show();
                    }

                });
*/
                LoadRemoteData(null);
                break;
            case 2:
                view = inflater.inflate(R.layout.right_pane, collection, false);
                break;
        }

        collection.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        //collection.removeView((View) view);
    }

    private String oldquery ="";
    private boolean isProgress = false;
    public void LoadRemoteData(String query){
        if(isProgress) return;
        if(query != oldquery){
            oldquery = query;
            page = -1;
            verticlePagerAdapter.setNodes(new ArrayList<Nodes>());
        } else{
            if((limit *(page)) - verticalViewPager.getCurrentItem() < limit/2){
                // No need to incremnet here as page is already hot incremneted while success
                //Just fall through
            }
            else{
                return;
            }
        }
        isProgress = true;
        String  cquery = query;
        if(query == null){
            query = "";
        }
        MainActivity.Get().showLoading();
        Log.d("Dipankar","Requesting page:"+(page+1));
        String url = "http://52.89.112.230/api/inshortsbengali?_project=uid,title,preview,url,imgurl,date&limit="+limit+"&page="+(page+1)+"&"+query;
        Log.d("Dipankar"," Calling the server by "+url);
        Request request = new Request.Builder().url(url).build();
        mHttpclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                //showToast("Not able to retrive data ...");
                e.printStackTrace();
                isProgress = false;
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.Get().hideLoading();
                    }
                });
            }
            @Override
            public void onResponse(Response response) throws IOException {
                final List<Nodes> nodesList = new ArrayList<Nodes>();
                try {
                    String jsonData = response.body().string();
                    JSONObject Jobject = new JSONObject(jsonData);
                    JSONArray Jarray = null;
                    if (Jobject.has("out")){
                        Jarray = Jobject.getJSONArray("out");
                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject object = Jarray.getJSONObject(i);
                            if(object.has("title") && object.has("preview")) { //TODO
                                nodesList.add(new Nodes(object.optString("uid",null),
                                        object.optString("title",null),
                                        object.optString("imgurl",null),
                                        object.optString("preview",null),
                                        object.optString("author",null),
                                        object.optString("url",null),
                                        object.optString("date",null),
                                        null,
                                        null));

                            }
                        }
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                if(nodesList.size() == 0){

                                } else{
                                    verticlePagerAdapter.appendNodes(nodesList);
                                    page++;
                                    if(page == 0){
                                        MainActivity.Get().moveToTop();
                                    }
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isProgress = false;
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.Get().hideLoading();
                    }
                });
            }

        });
    }
}