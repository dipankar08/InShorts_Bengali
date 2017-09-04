package in.peerreview.inshortsbengali;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import in.peerreview.External.BrowserActivity;
import in.peerreview.External.ShareScreen;

/**
 * Created by ddutta on 7/6/2017.
 */
public class VerticlePagerAdapter extends PagerAdapter {

    List<Nodes> mNodesList;
    Context mContext;
    LayoutInflater mLayoutInflater;

    public VerticlePagerAdapter(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mNodesList  = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mNodesList.size();
    }

    public void setNodes(List<Nodes> list){
        mNodesList = list;
        this.notifyDataSetChanged();
    }
    public void appendNodes(List<Nodes> list){
        mNodesList.addAll(list);
        this.notifyDataSetChanged();
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.card, container, false);

        final Nodes n = mNodesList.get(position);
        ImageView img = (ImageView) itemView.findViewById(R.id.image);
        TextView title = (TextView) itemView.findViewById(R.id.title);
        TextView fullstory = (TextView) itemView.findViewById(R.id.fullstory);
        TextView more = (TextView) itemView.findViewById(R.id.more);
        TextView shareit = (TextView) itemView.findViewById(R.id.shareit);
        TextView rank = (TextView) itemView.findViewById(R.id.rank);
        title.setText(n.getTitle());
        more.setMovementMethod(LinkMovementMethod.getInstance());
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.Get(), BrowserActivity.class);
                intent.putExtra("url", n.getUrl());
                MainActivity.Get().startActivity(intent);
            }
        });
        shareit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareScreen.share();
            }
        });

        rank.setText((position+1)+"/"+mNodesList.size());
        fullstory.setText(n.getFullstory());
        //position1.setText((position+1)+"/"+mNodesList.size());
        Log.d("DIPANKAR","instantiateItem: URL"+n.getUrl());
        Picasso.with(mContext)
                .load(n.getImgUrl())
                .error(R.drawable.noimage)
                .into(img);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
