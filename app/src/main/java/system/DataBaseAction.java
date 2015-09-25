package system;

/**
 * Created by Denver on 06.09.2015.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;
import java.util.ArrayList;
/**
 * Created by user on 05.09.2015.
 */
public class DataBaseAction {

    private final String LOG_TAG = "myLogs";

    private final DBHelper dbHelper;
    private final SQLiteDatabase db;

    public DataBaseAction(Context con){
        dbHelper = new DBHelper(con);
        db = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public long add(PostponedAppeals cont){
        Log.d(LOG_TAG, "--- Insert in Upload: ---");
        ContentValues cv = new ContentValues();
        cv.put("coordinateX", cont.getCoordinateX());
        cv.put("coordinateY", cont.getCoordinateY());
        cv.put("coment", cont.getComent());
        cv.put("foto1", cont.getPhoto1());
        cv.put("foto2", cont.getPhoto2());
        cv.put("foto3", cont.getPhoto3());
        cv.put("foto4", cont.getPhoto4());
        cv.put("foto5", cont.getPhoto5());
        long rowID = db.insert("Upload", null, cv);
        Log.d(LOG_TAG, "row inserted, ID = " + rowID);

        return rowID;
    }

    public long update(PostponedAppeals cont){
        drop(cont.getId());
        return add(cont);
    }

    public List<PostponedAppeals> selectAll(){
        Log.d(LOG_TAG, "--- Rows in Upload: ---");
        Cursor c = db.query("Upload", null, null, null, null, null, null);
        PostponedAppeals dbc;
        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex("id");
            int coordinateXColIndex = c.getColumnIndex("coordinateX");
            int coordinateYColIndex = c.getColumnIndex("coordinateY");
            int comentColIndex = c.getColumnIndex("coment");
            int foto1ColIndex = c.getColumnIndex("foto1");
            int foto2ColIndex = c.getColumnIndex("foto2");
            int foto3ColIndex = c.getColumnIndex("foto3");
            int foto4ColIndex = c.getColumnIndex("foto4");
            int foto5ColIndex = c.getColumnIndex("foto5");
            List<PostponedAppeals> list = new ArrayList<>();
            do {
                dbc = new PostponedAppeals(c.getInt(idColIndex), c.getString(comentColIndex),
                        c.getString(coordinateXColIndex) , c.getString(coordinateYColIndex), c.getString(foto1ColIndex), c.getString(foto2ColIndex),
                        c.getString(foto3ColIndex), c.getString(foto4ColIndex), c.getString(foto5ColIndex));
                list.add(dbc);
                Log.d(LOG_TAG,
                        "ID = " + c.getInt(idColIndex) + ", coment = "
                                + c.getString(comentColIndex) + ", coordinateLongitude = "
                                + c.getString(coordinateXColIndex) + ", coordinateBreadth = "
                                + c.getString(coordinateYColIndex) + " , foto1 = "
                                + c.getString(foto1ColIndex) + " , foto2 = "
                                + c.getString(foto2ColIndex) + " , foto3 = "
                                + c.getString(foto3ColIndex) + " , foto4 = "
                                + c.getString(foto4ColIndex) + " , foto5 = "
                                + c.getString(foto5ColIndex));
            } while (c.moveToNext());
            return list;
        } else
            Log.d(LOG_TAG, "0 rows");
        return null;
    }

    public int drop(int id){
        Log.d(LOG_TAG, "--- Delete from Upload: ---");
        int delCount = db.delete("Upload", "id = " + id, null);
        Log.d(LOG_TAG, "deleted rows count = " + delCount);
        return delCount;
    }

    public PostponedAppeals select(Integer id){
        Log.d(LOG_TAG, "--- Rows in Upload: ---");
        Cursor c = db.query("Upload", null, null, null, null, null, null);
        PostponedAppeals dbc;
        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex("id");
            int coordinateXColIndex = c.getColumnIndex("coordinateX");
            int coordinateYColIndex = c.getColumnIndex("coordinateY");
            int comentColIndex = c.getColumnIndex("coment");
            int foto1ColIndex = c.getColumnIndex("foto1");
            int foto2ColIndex = c.getColumnIndex("foto2");
            int foto3ColIndex = c.getColumnIndex("foto3");
            int foto4ColIndex = c.getColumnIndex("foto4");
            int foto5ColIndex = c.getColumnIndex("foto5");

            do {
                if(id.equals(c.getInt(idColIndex))){
                    dbc = new PostponedAppeals(c.getInt(idColIndex), c.getString(comentColIndex),
                            c.getString(coordinateXColIndex) , c.getString(coordinateYColIndex), c.getString(foto1ColIndex), c.getString(foto2ColIndex),
                            c.getString(foto3ColIndex), c.getString(foto4ColIndex), c.getString(foto5ColIndex));
                    return dbc;
                }
                Log.d(LOG_TAG,
                        "ID = " + c.getInt(idColIndex) + ", coment = "
                                + c.getString(comentColIndex) + ", coordinateLongitude = "
                                + c.getString(coordinateXColIndex) + ", coordinateBreadth = "
                                + c.getString(coordinateYColIndex) + " , foto1 = "
                                + c.getString(foto1ColIndex) + " , foto2 = "
                                + c.getString(foto2ColIndex) + " , foto3 = "
                                + c.getString(foto3ColIndex) + " , foto4 = "
                                + c.getString(foto4ColIndex) + " , foto5 = "
                                + c.getString(foto5ColIndex));
            } while (c.moveToNext());
        } else
            Log.d(LOG_TAG, "0 rows");
        return null;
    }

    private class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context) {
            super(context, "myDB", null, 1);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG, "--- onCreate database ---");
            db.execSQL("create table Upload ("
                    + "id integer primary key autoincrement,"
                    + "coordinateX text,"
                    + "coordinateY text,"
                    + "coment text,"
                    + "foto1 text,"
                    + "foto2 text,"
                    + "foto3 text,"
                    + "foto4 text,"
                    + "foto5 text" + ");");
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

}