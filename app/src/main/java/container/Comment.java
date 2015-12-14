package container;

/**
 * @author yurabuhaenko
 *
 * Class-container Comment used for holding coments from server
 */
public class Comment {

    private String type;
    private String date;
    private String text;

    /**
     * Default empty constructor
     */
    public Comment(){
        this.type = null;
        this.text = null;
        this.date = null;
    }

    /**
     * Second constructor wih parametrs
     * @param type
     * @param date
     * @param text
     */
    public Comment(String type, String date, String  text){
        this.type = type;
        this.text = text;
        this.date = date;
    }

    public String getType(){return type;}
    public String getDate(){return date;}
    public String getText(){return text;}


}
