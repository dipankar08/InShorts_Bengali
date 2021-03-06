package in.peerreview.inshortsbengali;

import org.w3c.dom.Node;

import java.util.List;

/**
 * Created by ddutta on 7/6/2017.
 */
public class Nodes {
    private int drawableid;
    private String subtitle;

    enum TYPE{
        NEWS,
        LOADING,
        ADDS,
        INTRO,
    }
    String uid, title, imgUrl,fullstory,author,url,date;
    Nodes.TYPE type;
    List<String> images, videos;

    public String getTitle() {
        return title;
    }
    public String getSubtitle() {
        return subtitle;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getFullstory() {
        return fullstory+" ...";
    }

    public String getAuthor() {
        return author;
    } public int getDrawableid() {
        return drawableid;
    }

    public String getUrl() {
        return url;
    }

    public String getDate() {
        return date;
    }

    public TYPE getType() {
        return type;
    }

    public List<String> getImages() {
        return images;
    }

    public List<String> getVideos() {
        return videos;
    }

    public Nodes(Nodes.TYPE type, String uid, String title, String main_image, String fullstory, String author, String url, String date, List<String> images, List<String> videos) {
        this.type = type;
        this.uid = uid;
        this.title = title;
        this.imgUrl = main_image;
        this.fullstory = fullstory;
        this.author = author;
        this.url = url;
        this.date = date;
        this.images = images;
        this.videos = videos;

    }
    public Nodes(Nodes.TYPE type,String title, String subtitle, int drawableid){
        this.drawableid= drawableid;
        this.type = type;
        this.title = title;
        this.subtitle = subtitle;
    }
}
