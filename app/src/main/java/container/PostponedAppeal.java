package container;

/**
 * @author SergPohh
 *
 * Class which holds all appeal information before sent it to server
 *
 */
public class PostponedAppeal {

    private String text;
    private String lat;
    private String lon;
    private String datetime;


    /**
     * path to photo on cd card from app directory
     */
    private String photo1;
    private String photo2;
    private String photo3;
    private String photo4;
    private String photo5;

    public PostponedAppeal(){}

    /**
     * Constructor without photo
     * @param text
     * @param lat
     * @param lon
     * @param datetime
     */
    public PostponedAppeal(String text, String lat, String lon, String datetime){
        this.text = text;
        this.lat = lat;
        this.lon = lon;
        this.datetime = datetime;
    }

    /**
     * Constructor with photo
     * @param text
     * @param lat
     * @param lon
     * @param datetime
     * @param photo1
     * @param photo2
     * @param photo3
     * @param photo4
     * @param photo5
     */
    public PostponedAppeal(String text, String lat, String lon, String datetime, String photo1,
                           String photo2, String photo3, String photo4, String photo5){
        this.text = text;
        this.lat = lat;
        this.lon = lon;
        this.datetime = datetime;
        this.photo1 = photo1;
        this.photo2 = photo2;
        this.photo3 = photo3;
        this.photo4 = photo4;
        this.photo5 = photo5;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public void setPhoto1(String photo1) {
        this.photo1 = photo1;
    }

    public void setPhoto2(String photo2) {
        this.photo2 = photo2;
    }

    public void setPhoto3(String photo3) {
        this.photo3 = photo3;
    }

    public void setPhoto4(String photo4) {
        this.photo4 = photo4;
    }

    public void setPhoto5(String photo5) {
        this.photo5 = photo5;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getPhoto1() {
        return photo1;
    }

    public String getPhoto2() {
        return photo2;
    }

    public String getPhoto3() {
        return photo3;
    }

    public String getPhoto4() {
        return photo4;
    }

    public String getPhoto5() {
        return photo5;
    }

    public String getText() {
        return text;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }


    /**
     * get photo by number of photo
     * @param number (1-5 int)
     * @return string path to photo
     */
    public String getPhotoByNumber(int number){
        switch (number){
            case 1:
                return photo1;
            case 2:
                return photo2;
            case 3:
                return photo3;
            case 4:
                return photo4;
            case 5:
                return photo5;

        }
        return "";
    }


    /**
     * Set photo by number
     * @param number (1-5 int)
     * @param photo
     */
    public void addPhotoByNumber(int number, String photo){

        switch (number){
            case 1:
                photo1=photo;
                break;
            case 2:
                photo2=photo;
                break;
            case 3:
                photo3=photo;
                break;
            case 4:
                photo4=photo;
                break;
            case 5:
                photo5 =photo;
                break;
            default:
                break;
        }

    }

    /**
     * Check is eny photos in this object
     * @return return true if exist photos
     */
    public boolean isPhotos(){
        if(photo1 != null || photo2 != null || photo3 != null || photo4 != null || photo5 != null){
           if(photo1 != "" || photo2 != "" || photo3 != "" || photo4 != "" || photo5 != ""){
               return true;
           } else {
               return false;
           }
        }else{
            return false;
        }
    }

    public int getNumberOfFirstFreePhoto(){
        if (photo1 == null || photo1 == ""){return 1;}
        if (photo2 == null || photo2 == ""){return 2;}
        if (photo3 == null || photo3 == ""){return 3;}
        if (photo4 == null || photo4 == ""){return 4;}
        if (photo5 == null || photo5 == ""){return 5;}
        return 0;
    }

}
