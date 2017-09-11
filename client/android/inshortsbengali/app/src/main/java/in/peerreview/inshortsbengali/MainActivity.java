package in.peerreview.inshortsbengali;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.peerreview.External.CacheControl;
import in.peerreview.External.IPermissionCallbacks;
import in.peerreview.External.IResponse;
import in.peerreview.External.MyOkHttp;
import in.peerreview.External.RunTimePermission;
import in.peerreview.External.SettingsActivity;
import in.peerreview.External.ShareScreen;
import in.peerreview.External.Telemetry;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener{

    private static final String TAG ="MainActivity";
    private String mCategories = "";
    private String mSource = "";
    private String mData = "";

    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    FrameLayout progressBarHolder,toolbar;
    ProgressBar m_pb;

    public static VerticalViewPager verticalViewPager;
    public VerticlePagerAdapter verticlePagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sMainActivity = this;

        ///Setups..
        Telemetry.setup("http://52.89.112.230/api/inshortsbengalistat", true);
        MyOkHttp.setup(this);
        ShareScreen.setup(this);
        RunTimePermission.setup(this);



        //getting Views
        verticalViewPager = (VerticalViewPager)findViewById(R.id.pager);
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
        toolbar = (FrameLayout) findViewById(R.id.toolbar);
        m_pb = (ProgressBar)findViewById(R.id.progressbar_Horizontal);
        m_pb.setVisibility(View.GONE);


        // Setting up veritical view pager
        verticlePagerAdapter = new VerticlePagerAdapter(this);
        verticalViewPager.setAdapter(verticlePagerAdapter);
        verticalViewPager.setCurrentItem(1);
        verticalViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            public void onPageSelected(int position) {
                Log.i("Dipankar", "position: " + position);
                LoadRemoteData(oldquery);
            }
        });

        //Setting up run time permission.
        RunTimePermission.askPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE, new IPermissionCallbacks() {
            @Override
            public void success() {
                Log.d(TAG,"Success callback executed!");
            }

            @Override
            public void failure() {
                Log.d(TAG,"error callback executed!");
            }
        });
        //toolbar
        toolbar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                hideToolBar();
            }
        });
        //filling up data
        verticlePagerAdapter.appendNodes(getIntroNodes());
        //LoadRemoteData(null);
    }

    private List<Nodes> getIntroNodes() {
        List<Nodes> nodes = new ArrayList<>();
        nodes.add( new Nodes(Nodes.TYPE.INTRO,"Really short!","With just few words, you can grab more news in less time!",R.drawable.icons8paste48));
        nodes.add( new Nodes(Nodes.TYPE.INTRO,"Really Easy","A better experinec just sweep over news",R.drawable.icons8paste48));
        nodes.add( new Nodes(Nodes.TYPE.INTRO,"Really Fast","Just all news are in simple mode",R.drawable.icons8paste48));
        return nodes;
    }
    private List<Nodes> getLoading() {
        List<Nodes> nodes = new ArrayList<>();
        nodes.add( new Nodes(Nodes.TYPE.INTRO,"Wait for a while...","Ohhhh.. please sit back and relax while we are fetching news for you!",R.drawable.icons8_cound));
        return nodes;
    }
    /********  External : Setting activity *****************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            // launch settings activity
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showLoading() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                m_pb.setVisibility(View.VISIBLE);
                /*
                inAnimation = new AlphaAnimation(0f, 1f);
                inAnimation.setDuration(200);
                progressBarHolder.setAnimation(inAnimation);
                progressBarHolder.setVisibility(View.VISIBLE);*/
            }
        });
    }

    public void hideLoading() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                m_pb.setVisibility(View.GONE);
                /*
                outAnimation = new AlphaAnimation(1f, 0f);
                outAnimation.setDuration(200);
                progressBarHolder.setAnimation(outAnimation);
                progressBarHolder.setVisibility(View.GONE);*/
            }
        });

    }
    public void showToolBar() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {

                inAnimation = new AlphaAnimation(0f, 1f);
                inAnimation.setDuration(200);
                toolbar.setAnimation(inAnimation);
                toolbar.setVisibility(View.VISIBLE);
                /*
                Animation animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.pop_in);
                toolbar.setVisibility(View.VISIBLE);
                toolbar.startAnimation(animFadeIn);
                */
            }
        });
    }

    public void hideToolBar() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                outAnimation = new AlphaAnimation(1f, 0f);
                outAnimation.setDuration(200);
                toolbar.setAnimation(outAnimation);
                toolbar.setVisibility(View.GONE);
            }
        });
    }

    private static MainActivity sMainActivity;
    public static MainActivity Get() {
        return sMainActivity;
    }
    public void showToast(final String msg) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void buttonPressed(final View v) {
        switch (v.getId()) {
            case R.id.all:
                mCategories = "";
                mSource = "";
                refetch();
                break;
            //toolbar
            case R.id.top:
                moveToTop();
                break;
            case R.id.refresh:
                refetch();
                break;
            case R.id.like:
                break;
            case R.id.share:
                break;
            case R.id.setting:
                showSetting();
                break;
            case R.id.bookmark:
                break;
        }
        Telemetry.sendTelemetry("menu_btn_click", new HashMap<String, String>() {{
            put("btn_id", getResources().getResourceName(v.getId()));
        }});

    }

    private void showSetting() {

    }

    public void refetch() {
        String res = "";
        if (mCategories.length() != 0) {
            res += "tag=" + mCategories + "&";
        }
        if (mSource.length() != 0) {
            res += "tag=" + mSource + "&";
        }
        if (mData.length() != 0) {
            res += "tag=" + mData + "&";
        }
        LoadRemoteData(res);
    }

    public void moveToTop() {
        verticalViewPager.setCurrentItem(0);
    }

    @Override
    public boolean onLongClick(View view) {
        showToolBar();
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        RunTimePermission.processResult(requestCode,permissions,grantResults);
    }

    //#######################   News Cache controls #########################
    private String oldquery ="";
    private boolean isProgress = false;
    private static int page =0;
    private static int limit  =10;
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
        CacheControl c = CacheControl.GET_LIVE_ELSE_CACHE;
        MyOkHttp.getData(url, c, new IResponse() {
            @Override
            public void success(JSONObject Jobject) {
                if(Jobject != null){
                    final List<Nodes> nodesList = new ArrayList<Nodes>();
                    try {
                        JSONArray Jarray = null;
                        if (Jobject.has("out")){
                            Jarray = Jobject.getJSONArray("out");
                            for (int i = 0; i < Jarray.length(); i++) {
                                JSONObject object = Jarray.getJSONObject(i);
                                if(object.has("title") && object.has("preview")) { //TODO
                                    nodesList.add( new Nodes(Nodes.TYPE.NEWS,
                                            object.optString("uid",null),
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
                }
                //cleanup.
                isProgress = false;
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.Get().hideLoading();
                    }
                });
            }
            @Override
            public void error(String msg) {
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
