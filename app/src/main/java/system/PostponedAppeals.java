package system;

/**
 * Created by Denver on 06.09.2015.
 */
public class PostponedAppeals {

    private final Integer ID;
    private String COORDINATE_X;
    private String COORDINATE_Y;
    private String COMENT;
    private String PHOTO1;
    private String PHOTO2;
    private String PHOTO3;
    private String PHOTO4;
    private String PHOTO5;

    public PostponedAppeals(Integer id, String coordinateX, String coordinateY, String coment, String photo1, String photo2, String photo3, String photo4, String photo5){
        ID = id;
        COORDINATE_X = coordinateX;
        COORDINATE_Y = coordinateY;
        COMENT = coment;
        PHOTO1 = photo1;
        PHOTO2 = photo2;
        PHOTO3 = photo3;
        PHOTO4 = photo4;
        PHOTO5 = photo5;
    }

    public Integer getId(){
        return ID;
    }

    public String getCoordinateX(){
        return COORDINATE_X;
    }
    public void setCoordinateX(String x){COORDINATE_X = x;}

    public String getCoordinateY(){
        return COORDINATE_Y;
    }
    public void setCoordinateY(String y){COORDINATE_Y = y;}

    public String getComent(){
        return COMENT;
    }
    public void setCOMENT(String str){COMENT = str;}

    public String getPhoto1(){
        return PHOTO1;
    }
    public void setPhoto1(String photo){PHOTO1 = photo;}

    public String getPhoto2(){
        return PHOTO2;
    }
    public void setPhoto2(String photo){PHOTO2 = photo;}

    public String getPhoto3(){
        return PHOTO3;
    }
    public void setPhoto3(String photo){PHOTO3 = photo;}

    public String getPhoto4(){
        return PHOTO4;
    }
    public void setPhoto4(String photo){PHOTO4 = photo;}

    public String getPhoto5(){
        return PHOTO5;
    }
    public void setPhoto5(String photo){PHOTO5 = photo;}
}
