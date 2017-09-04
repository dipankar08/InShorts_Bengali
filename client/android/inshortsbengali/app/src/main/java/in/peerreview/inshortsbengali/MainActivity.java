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
import android.widget.Toast;

import java.util.HashMap;

import in.peerreview.External.IPermissionCallbacks;
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
    private String mType = "";
    private HorizantalViewPagerAdapter mHorizantalViewPagerAdapter;
    private ViewPager pager;

    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    FrameLayout progressBarHolder,toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sMainActivity = this;
        //mark it false when the build is ready
        Telemetry.setup("http://52.89.112.230/api/inshortsbengalistat", true);
        MyOkHttp.setup(this);
        ShareScreen.setup(this);
        RunTimePermission.setup(this);
        //setTheme(darkTheme ? R.style.AppThemeDark : R.style.AppThemeLight);
        setContentView(R.layout.activity_main);

        mHorizantalViewPagerAdapter = new HorizantalViewPagerAdapter(this);
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
        toolbar = (FrameLayout) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setCurrentItem(1);
        pager.setAdapter(mHorizantalViewPagerAdapter);
        pager.setCurrentItem(1);

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
                inAnimation = new AlphaAnimation(0f, 1f);
                inAnimation.setDuration(200);
                progressBarHolder.setAnimation(inAnimation);
                progressBarHolder.setVisibility(View.VISIBLE);
            }
        });
    }

    public void hideLoading() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                outAnimation = new AlphaAnimation(1f, 0f);
                outAnimation.setDuration(200);
                progressBarHolder.setAnimation(outAnimation);
                progressBarHolder.setVisibility(View.GONE);
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
            case R.id.trending:
                mCategories = "kolkata";
                break;
            case R.id.saved:
                break;
            case R.id.kolkata:
                mCategories = "kolkata";
                refetch();
                break;
            case R.id.state:
                mCategories = "state";
                refetch();
                break;
            case R.id.india:
                mCategories = "india";
                refetch();
                break;
            case R.id.international:
                mCategories = "international";
                refetch();
                break;
            case R.id.lifestyle:
                mCategories = "lifestyle";
                refetch();
                break;
            case R.id.siteseeing:
                mCategories = "siteseeing";
                refetch();
                break;
            case R.id.game:
                mCategories = "game";
                refetch();
                break;
            case R.id.science:
                mCategories = "science";
                refetch();
                break;
            //all sources
            case R.id.pratidin:
                mSource = "pratidin";
                refetch();
                break;
            case R.id.eisamay:
                mSource = "eisamay";
                refetch();
                break;
            case R.id.anandabazar:
                mSource = "anandabazar";
                refetch();
                break;
            case R.id.bartaman:
                mSource = "bartamanpatrika";
                refetch();
                break;
            case R.id.zeenews:
                mSource = "zeenews";
                refetch();
                break;
            case R.id.ebela:
                mSource = "ebela";
                refetch();
                break;
            case R.id.songbaad:
                mSource = "songbaad";
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
        mHorizantalViewPagerAdapter.LoadRemoteData(res);
        pager.setCurrentItem(1);
    }

    public void moveToTop() {
        mHorizantalViewPagerAdapter.verticalViewPager.setCurrentItem(0);
    }
    public void showSetting() {
        pager.setCurrentItem(0);
        hideToolBar();
        hideLoading();
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
}
