package system;

import android.content.Context;

import denver.remcontrol.R;

/**
 * @author yurabuhaenko
 * class to get Kiev district name by it's id according to 1551 API
 */
public class DistrictsHolder {

    /**
     *
     * @param id id of district according to 1551 API
     * @return district name
     */
    public static String getDistrictById(int id){
        String distr = "";
        switch (id){
            case 1:
                distr = "Голосіївський";
                break;
            case 2:
                distr = "Дарницький";
                break;
            case 3:
                distr = "Деснянський";
                break;
            case 4:
                distr = "Дніпровський";
                break;
            case 5:
                distr = "Оболонський";
                break;
            case 6:
                distr = "Печерський";
                break;
            case 7:
                distr = "Подільський";
                break;
            case 8:
                distr = "Святошинський";
                break;
            case 9:
                distr = "Солом`янський";
                break;
            case 10:
                distr = "Шевченківський";
                break;
        }
        return distr;
    }



}
