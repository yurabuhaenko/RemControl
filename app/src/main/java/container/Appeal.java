package container;

import android.content.Context;
import android.content.res.Resources;

import java.util.ArrayList;

import denver.remcontrol.R;
import system.DistrictsHolder;

/**
 * @author yurabuhaenko
 *
 * Class-container Appeal used for holding appeals from server
 *
 *
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


    /**
     * Constructor without photos & comments
     * @param id
     * @param districtId
     * @param street
     * @param house
     * @param appealText
     * @param status
     * @param statusCode
     */
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


    /**
     * Constructor with photos & comments
     * @param id
     * @param districtId
     * @param street
     * @param house
     * @param appealText
     * @param status
     * @param statusCode
     * @param photosUrl
     * @param comments
     */
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


    /**
     *
     * @param comments
     */
    public void setComments(ArrayList<Comment> comments){this.comments = comments;}

    /**
     *
     * @param comment
     */
    public void addComment(Comment comment){comments.add(comment);}

    /**
     *
     * @return comments
     */
    public ArrayList<Comment> getComments(){return comments;}

    /**
     *
     * @param photos
     */
    public void setPhotosUrl(ArrayList<String> photos){this.photosUrl = photos;}

    /**
     *
     * @param photoUrl
     */
    public void addPhotoUrl(String photoUrl){photosUrl.add(photoUrl);}

    /**
     *
     * @return photosUrl
     */
    public ArrayList<String> getPhotosUrl(){return photosUrl;}

    /**
     *
     * @return appealId
     */
    public int getAppealId(){return appealId;}
    /**
     *
     * @return district
     */
    public String getDistrict(){return district;}
    /**
     *
     * @return address
     */
    public String getAddress(){return address;}
    /**
     *
     * @return appealText
     */
    public String getAppealText(){return appealText;}
    /**
     *
     * @return status
     */
    public String getStatus(){return status;}
    /**
     *
     * @return statusCode
     */
    public int getStatusCode(){return statusCode;}

}
