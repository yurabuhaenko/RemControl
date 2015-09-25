package container;

import android.content.Context;
import android.content.res.Resources;

import denver.remcontrol.R;

/**
 * Created by Denver on 06.09.2015.
 */
public class ContainerAppealSent {

    private String district;
    private String address;
    private String appealText;
    private String status;
    private int statusCode;

    private Context context;

    public ContainerAppealSent(Context context, int districtId, String street, String house, String appealText, String status, int statusCode){
        this.context = context;
        this.address = street + " " + house;
        this.district = getDistrictById(districtId);
        this.status = status;
        this.statusCode = statusCode;
    }

    private String getDistrictById(int id){
        String distr = "";
        switch (id){
            case 1:
                distr = context.getResources().getString(R.string.district1);
                break;
            case 2:
                distr = context.getResources().getString(R.string.district2);
                break;
            case 3:
                distr = context.getResources().getString(R.string.district3);
                break;
            case 4:
                distr = context.getResources().getString(R.string.district4);
                break;
            case 5:
                distr = context.getResources().getString(R.string.district5);
                break;
            case 6:
                distr = context.getResources().getString(R.string.district6);
                break;
            case 7:
                distr = context.getResources().getString(R.string.district7);
                break;
            case 8:
                distr = context.getResources().getString(R.string.district8);
                break;
            case 9:
                distr = context.getResources().getString(R.string.district9);
                break;
            case 10:
                distr = context.getResources().getString(R.string.district10);
                break;
        }
        return distr;
    }

    public String getDistrict(){return district;}
    public String getAddress(){return address;}
    public String getAppealText(){return appealText;}
    public String getStatus(){return status;}
    public int getStatusCode(){return statusCode;}

}
