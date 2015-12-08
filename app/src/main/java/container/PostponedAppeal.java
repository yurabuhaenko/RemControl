package container;

/**
 * Created by root on 07.12.15.
 */
public class PostponedAppeal {

    private String text;
    private String lat;
    private String lon;
    private String datetime;
    private String photo1;
    private String photo2;
    private String photo3;
    private String photo4;
    private String photo5;

    public PostponedAppeal(){}

    public PostponedAppeal(String text, String lat, String lon, String datetime){
        this.text = text;
        this.lat = lat;
        this.lon = lon;
        this.datetime = datetime;
    }

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


}
