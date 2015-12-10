package container;

import android.content.Context;
import android.content.res.Resources;

import java.util.ArrayList;

import denver.remcontrol.R;
import system.DistrictsHolder;

/**
 * Created by Denver on 06.09.2015.
 */
public class Appeal {

    private int appealId;
    private String district;
    private String address;
    private String appealText;
    private String status;
    private int statusCode;

    private ArrayList<String> photosUrl;
    private ArrayList<Comment> comments;

    //private Context context;

    public Appeal(int id, int districtId, String street, String house, String appealText, String status, int statusCode){
        this.appealId = id;
       // this.context = context;
        this.address = street + " " + house;
        this.district = DistrictsHolder.getDistrictById(districtId);
        this.status = status;
        this.statusCode = statusCode;
        this.appealText = appealText;

        photosUrl = new ArrayList<>();
        comments = new ArrayList<>();
    }

    public Appeal( int id, int districtId, String street, String house, String appealText, String status, int statusCode,
                  ArrayList<String> photosUrl, ArrayList<Comment> comments){

        this.appealId = id;
        //this.context = context;
        this.address = street + " " + house;
        this.district = DistrictsHolder.getDistrictById(districtId);
        this.status = status;
        this.statusCode = statusCode;
        this.appealText = appealText;

        this.photosUrl = photosUrl;
        this.comments = comments;
    }



    public void setComments(ArrayList<Comment> comments){this.comments = comments;}
    public void addComment(Comment comment){comments.add(comment);}
    public ArrayList<Comment> getComments(){return comments;}

    public void setPhotosUrl(ArrayList<String> photos){this.photosUrl = photos;}
    public void addPhotoUrl(String photoUrl){photosUrl.add(photoUrl);}
    public ArrayList<String> getPhotosUrl(){return photosUrl;}


    public int getAppealId(){return appealId;}
    public String getDistrict(){return district;}
    public String getAddress(){return address;}
    public String getAppealText(){return appealText;}
    public String getStatus(){return status;}
    public int getStatusCode(){return statusCode;}

}
