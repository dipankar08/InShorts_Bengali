package in.peerreview.inshortsbengali;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
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

public class MainActivity extends AppCompatActivity {
    private VerticalViewPager verticalViewPager;
    private VerticlePagerAdapter verticlePagerAdapter;
    private OkHttpClient mHttpclient;

    static int page = -1; // Note that at the time of fetching we are trying to get the next one and once we get it incremnet the page
    final static int limit = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verticalViewPager = (VerticalViewPager) findViewById(R.id.verticleViewPager);
        verticlePagerAdapter = new VerticlePagerAdapter(this);
        verticalViewPager.setAdapter(verticlePagerAdapter);
        verticalViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                Log.i("Dipankar", "position: " + position);
                LoadRemoteData(oldquery);
            }
        });

        mHttpclient =  new OkHttpClient();
        LoadRemoteData(null);
    }

    public void showToast(final String msg){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this,  msg, Toast.LENGTH_SHORT).show();
            }
        });
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
        Log.d("Dipankar","Requesting page:"+(page+1));
        Request request = new Request.Builder().url("http://52.89.112.230/api/inshortsbengali1?limit="+limit+"&page="+(page+1)+"&"+query).build();
        mHttpclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                showToast("Not able to retrive data ...");
                e.printStackTrace();
                isProgress = false;
            }
            @Override
            public void onResponse(Response response) throws IOException {
                final List<Nodes> nodesList = new ArrayList<Nodes>();
                try {
                    String jsonData = response.body().string();
                    JSONObject Jobject = new JSONObject(jsonData);
                    JSONArray Jarray = null;
                    Jarray = Jobject.getJSONArray("out");
                    for (int i = 0; i < Jarray.length(); i++) {
                        JSONObject object = Jarray.getJSONObject(i);
                        if(object.has("title") && object.has("fullstory")) { //TODO
                            nodesList.add(new Nodes(object.optString("uid",null),
                                                    object.optString("title",null),
                                                    object.optString("imgurl",null),
                                                    object.optString("fullstory",null),
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
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();

                }
                isProgress = false;
            }

        });
    }


}
