package in.peerreview.inshortsbengali;

import java.util.List;

/**
 * Created by ddutta on 7/6/2017.
 */
public class Nodes {
    String uid, title, imgUrl,fullstory,author,url,date;
    List<String> images, videos;

    public String getTitle() {
        return title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getFullstory() {
        return fullstory+" ...";
    }

    public String getAuthor() {
        return author;
    }

    public String getUrl() {
        return url;
    }

    public String getDate() {
        return date;
    }

    public List<String> getImages() {
        return images;
    }

    public List<String> getVideos() {
        return videos;
    }

    public Nodes(String uid, String title, String main_image, String fullstory, String author, String url, String date, List<String> images, List<String> videos) {
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
}