package denver.remcontrol.activitys;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Property;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import container.PostponedAppeal;
import denver.remcontrol.R;
import system.ActionHttp;
import system.InternetConnectionChecker;
import system.RemControlApplication;

/**
 * @author  yurabuhaenko
 *
 * Activity to show list of postponed appeals and their photos
 *
 * @see NavigationDrawerActivity
 */
public class PostponedAppealsListActivity extends NavigationDrawerActivity {



    AppealListAdapter appealListAdapter;
    ArrayList<PostponedAppeal> listPosponedAppeals = new ArrayList<>();
    RemControlApplication remControlApplication;

    private View mProgressView;

    private View mListViewPosponedAppeals;

    private LinearLayout myList;
    private int numberOfAppealLastAddedPhoto;
    FloatingActionButton newAppeal;
    FloatingActionButton sentAllPostponedAppeals;


    /**
     * default on create method
     * initialize view elements
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postponed_appeals_list);
        postCreate(savedInstanceState, R.layout.activity_postponed_appeals_list);

        // listViewPosponedAppeals = (ScrollView)findViewById(R.id.listViewPostponedAppeals);
        remControlApplication = (RemControlApplication) getApplicationContext();

        listPosponedAppeals = remControlApplication.getPostponedAppeals();

        mProgressView = findViewById(R.id.sent_postponed_appeal_progress);

        // setAppealListOnView();

        newAppeal = (FloatingActionButton)findViewById(R.id.myFABCreateNewAppeal);
        sentAllPostponedAppeals = (FloatingActionButton)findViewById(R.id.myFABSentAllPostponedAppeals);


        mListViewPosponedAppeals = findViewById(R.id.scrollViewPostponedAppeals);

////////////////////
        myList = (LinearLayout) findViewById(R.id.listViewPostponedAppeals);
        getSupportActionBar().setTitle("Відкладені звернення");
    }


    /**
     * default on resume method
     * perform change fab buttons
     * perform to set appeal list view
     */
    @Override
    protected void onResume(){
        super.onResume();
        revisibleButtons();
        setAppealListOnView();
    }


    /**
     * default on pause method
     * saving all appeals on
     * @see RemControlApplication
     * SharedPreferences
     */
    @Override
    protected void onPause(){
        super.onPause();
        remControlApplication.setPostponedAppeals(listPosponedAppeals);
        remControlApplication.savePostponedAppeals();
    }


    /**
     * Set current displayed FAB
     * if no appeals set FAB redirect to new appeal
     * if are appeals set FAB send al to server
     */
    private void revisibleButtons(){
        if(listPosponedAppeals.size() > 0){
            sentAllPostponedAppeals.setVisibility(View.VISIBLE);
            newAppeal.setVisibility(View.INVISIBLE);
        }else {
            newAppeal.setVisibility(View.VISIBLE);
            sentAllPostponedAppeals.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * redirect to
     * @see AppealActivity
     * activity stack get cleaned
     * @param view
     */
    public void onClickNewAppeal(View view){
        Intent newIntent = new Intent(PostponedAppealsListActivity.this, AppealActivity.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(newIntent);
    }


    /**
     * sent all postponed appeals to server
     * @param view
     */
    public void onClickSentAllAppeals(View view){
        if(InternetConnectionChecker.isNetworkConnected(PostponedAppealsListActivity.this)){
            SentAppeal sentAppeal = new SentAppeal(listPosponedAppeals, PostponedAppealsListActivity.this);
            sentAppeal.execute();
        }else{
            Toast.makeText(PostponedAppealsListActivity.this, "Помилка! \n Немає з’єднання з інтернетом!", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * set Appeal List On View
     * create adapter, execute getView() for each appeal
     * set on view
     */
    private void setAppealListOnView(){

        if(listPosponedAppeals.size() > 0){
            appealListAdapter = new AppealListAdapter(listPosponedAppeals);
        }else{
            appealListAdapter = new AppealListAdapter(new ArrayList<PostponedAppeal>());
        }
        myList.removeAllViews();
        for (int i = 0; i < listPosponedAppeals.size(); i++) {
            View view = appealListAdapter.getView(i, null, myList);
            myList.addView(view);
        }
    }


    /**
     * @author yurabuhaenko
     * adapter class to hold appeals list and set appel in item layout
     */
    private class AppealListAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        ArrayList<PostponedAppeal> listPosponedAppeals;

        /**
         *
         * @param listPosponedAppeals list of appeals to set on view
         */
        public AppealListAdapter(ArrayList<PostponedAppeal> listPosponedAppeals ) {
            mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            this.listPosponedAppeals = listPosponedAppeals;
            //notifyDataSetChanged();
        }

        public ArrayList<PostponedAppeal> getUserTasks() {
            return listPosponedAppeals;
        }

        public int getCount() {
            return listPosponedAppeals.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }


        /**
         * auto executed method
         * All data sets on item layout
         * creates onclick for FABs
         * Sets all parameters of displayed item layout (visibility, width, height)
         * @param position number of appeal for which it was executed
         * @param convertView current item layout
         * @param parent parent view
         * @return item layout for display one appeal
         */
        public View getView(final int position, View convertView, ViewGroup parent) {

            final PostponedAppeal appeal = listPosponedAppeals.get(position);

            final EditText textText;
            final TextView textDateTime;
            ImageView mImageView1;
            ImageView mImageView2;
            ImageView mImageView3;
            ImageView mImageView4;
            ImageView mImageView5;
            final RelativeLayout mRelLay1;
            final RelativeLayout mRelLay2;
            final RelativeLayout mRelLay3;
            final RelativeLayout mRelLay4;
            final RelativeLayout mRelLay5;
            FloatingActionButton fab1;
            FloatingActionButton fab2;
            FloatingActionButton fab3;
            FloatingActionButton fab4;
            FloatingActionButton fab5;
            final FloatingActionButton delAppeal;
            final FloatingActionButton addPhoto;

            Button sentAppealButton;


            if (convertView == null) {
                Log.d("debugGetView","Pos: "+ Integer.toString(position) +"  text  "+ appeal.getText() +"  convView null");
                convertView = mInflater.inflate(R.layout.item_postponed_appeal, null);

                textText = (EditText) convertView.findViewById(R.id.textOfAppeal);
                textDateTime = (TextView) convertView.findViewById(R.id.textDateTimeOfSave);
                mImageView1 = (ImageView)convertView.findViewById(R.id.imageViewPhotoSaved1);
                mImageView2 = (ImageView)convertView.findViewById(R.id.imageViewPhotoSaved2);
                mImageView3 = (ImageView)convertView.findViewById(R.id.imageViewPhotoSaved3);
                mImageView4 = (ImageView)convertView.findViewById(R.id.imageViewPhotoSaved4);
                mImageView5 = (ImageView)convertView.findViewById(R.id.imageViewPhotoSaved5);
                mRelLay1 = (RelativeLayout)convertView.findViewById(R.id.photoAppealLayout1);
                mRelLay2 = (RelativeLayout)convertView.findViewById(R.id.photoAppealLayout2);
                mRelLay3 = (RelativeLayout)convertView.findViewById(R.id.photoAppealLayout3);
                mRelLay4 = (RelativeLayout)convertView.findViewById(R.id.photoAppealLayout4);
                mRelLay5 = (RelativeLayout)convertView.findViewById(R.id.photoAppealLayout5);
                fab1 = (FloatingActionButton)convertView.findViewById(R.id.myFABImageViewPostponedPhoto1);
                fab2 = (FloatingActionButton)convertView.findViewById(R.id.myFABImageViewPostponedPhoto2);
                fab3 = (FloatingActionButton)convertView.findViewById(R.id.myFABImageViewPostponedPhoto3);
                fab4 = (FloatingActionButton)convertView.findViewById(R.id.myFABImageViewPostponedPhoto4);
                fab5 = (FloatingActionButton)convertView.findViewById(R.id.myFABImageViewPostponedPhoto5);
                delAppeal = (FloatingActionButton)convertView.findViewById(R.id.myFABImageViewDeleteAppeal);
                addPhoto = (FloatingActionButton)convertView.findViewById(R.id.myFABImageViewAddPhoto);


                sentAppealButton = (Button)convertView.findViewById(R.id.onClickSentPostponedAppeal);

                convertView.setTag(textDateTime);

                textText.setText(appeal.getText());
                textDateTime.setText(appeal.getDatetime());


                final HorizontalScrollView sr = (HorizontalScrollView) convertView.findViewById(R.id.horizontalScrollViewPhotos);
                LinearLayout linLay = (LinearLayout) convertView.findViewById(R.id.xml_full_img_linear_below_view);

                if(appeal.getPhoto1()!="" && appeal.getPhoto1()!=null){
                    sr.getLayoutParams().height = (int) getResources().getDimension(R.dimen.imageview_height);
                    sr.setVisibility(View.VISIBLE);
                    linLay.getLayoutParams().height = (int) getResources().getDimension(R.dimen.imageview_height);
                    setPic(mImageView1, mRelLay1, appeal.getPhoto1());
                }
                if(appeal.getPhoto2()!="" && appeal.getPhoto2()!=null){
                    sr.getLayoutParams().height = (int) getResources().getDimension(R.dimen.imageview_height);
                    sr.setVisibility(View.VISIBLE);
                    linLay.getLayoutParams().height = (int) getResources().getDimension(R.dimen.imageview_height);
                    setPic(mImageView2, mRelLay2 ,appeal.getPhoto2());
                }
                if(appeal.getPhoto3()!="" && appeal.getPhoto3()!=null){
                    sr.getLayoutParams().height = (int) getResources().getDimension(R.dimen.imageview_height);
                    sr.setVisibility(View.VISIBLE);
                    linLay.getLayoutParams().height = (int) getResources().getDimension(R.dimen.imageview_height);
                    setPic(mImageView3, mRelLay3 ,appeal.getPhoto3());
                }
                if(appeal.getPhoto4()!="" && appeal.getPhoto4()!=null){
                    sr.getLayoutParams().height = (int) getResources().getDimension(R.dimen.imageview_height);
                    sr.setVisibility(View.VISIBLE);
                    linLay.getLayoutParams().height = (int) getResources().getDimension(R.dimen.imageview_height);
                    setPic(mImageView4, mRelLay4 ,appeal.getPhoto4());
                }
                if(appeal.getPhoto5()!="" && appeal.getPhoto5()!=null){
                    sr.getLayoutParams().height = (int) getResources().getDimension(R.dimen.imageview_height);
                    sr.setVisibility(View.VISIBLE);
                    linLay.getLayoutParams().height = (int) getResources().getDimension(R.dimen.imageview_height);
                    setPic(mImageView5, mRelLay5 ,appeal.getPhoto5());
                }


                delAppeal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        delAppeal(position);
                    }
                });

                addPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addPhotoToAppeal(position);
                    }
                });

                textText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        // TODO Auto-generated method stub
                        appeal.setText(textText.getText().toString());
                        listPosponedAppeals.get(position).setText(textText.getText().toString());
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // TODO Auto-generated method stub

                    }});


                sentAppealButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(InternetConnectionChecker.isNetworkConnected(PostponedAppealsListActivity.this)){
                            SentAppeal sentAppeal = new SentAppeal(listPosponedAppeals.get(position), position, PostponedAppealsListActivity.this);
                            sentAppeal.execute();
                        }else{
                            Toast.makeText(PostponedAppealsListActivity.this, "Помилка! \n Неможливо визначити координати", Toast.LENGTH_LONG).show();
                        }
                    }
                });


                fab1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String dir = getFilesDir().getAbsolutePath();
                        File f0 = new File(dir, appeal.getPhotoByNumber(1));
                        boolean d0 = f0.delete();
                        if (d0){Log.d("fileDeleting", "true");}else{Log.d("fileDeleting", "false");}
                        appeal.addPhotoByNumber(1, "");
                        listPosponedAppeals.get(position).addPhotoByNumber(1, "");
                        remControlApplication.setPostponedAppeals(listPosponedAppeals);
                        remControlApplication.savePostponedAppeals();
                        if(!appeal.isPhotos()){
                            sr.setVisibility(View.GONE);
                            sr.getLayoutParams().width = 0;
                            sr.getLayoutParams().height = 0;
                        }
                        mRelLay1.setVisibility(View.GONE);
                        mRelLay1.getLayoutParams().width = 0;
                        mRelLay1.getLayoutParams().height = 0;

                    }
                });

                fab2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String dir = getFilesDir().getAbsolutePath();
                        File f0 = new File(dir, appeal.getPhotoByNumber(2));
                        boolean d0 = f0.delete();

                        appeal.addPhotoByNumber(2, "");
                        listPosponedAppeals.get(position).addPhotoByNumber(2, "");
                        remControlApplication.setPostponedAppeals(listPosponedAppeals);
                        remControlApplication.savePostponedAppeals();
                        if(!appeal.isPhotos()){
                            sr.setVisibility(View.GONE);
                            sr.getLayoutParams().width = 0;
                            sr.getLayoutParams().height = 0;
                        }
                        mRelLay2.setVisibility(View.GONE);
                        mRelLay2.getLayoutParams().width = 0;
                        mRelLay2.getLayoutParams().height = 0;

                    }
                });

                fab3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String dir = getFilesDir().getAbsolutePath();
                        File f0 = new File(dir, appeal.getPhotoByNumber(3));
                        boolean d0 = f0.delete();
                        appeal.addPhotoByNumber(3, "");
                        listPosponedAppeals.get(position).addPhotoByNumber(3, "");
                        remControlApplication.setPostponedAppeals(listPosponedAppeals);
                        remControlApplication.savePostponedAppeals();
                        if(!appeal.isPhotos()){
                            sr.setVisibility(View.GONE);
                            sr.getLayoutParams().width = 0;
                            sr.getLayoutParams().height = 0;
                        }
                        mRelLay3.setVisibility(View.GONE);
                        mRelLay3.getLayoutParams().width = 0;
                        mRelLay3.getLayoutParams().height = 0;

                    }
                });

                fab4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String dir = getFilesDir().getAbsolutePath();
                        File f0 = new File(dir, appeal.getPhotoByNumber(4));
                        boolean d0 = f0.delete();
                        appeal.addPhotoByNumber(4, "");
                        listPosponedAppeals.get(position).addPhotoByNumber(4, "");
                        remControlApplication.setPostponedAppeals(listPosponedAppeals);
                        remControlApplication.savePostponedAppeals();
                        if(!appeal.isPhotos()){
                            sr.setVisibility(View.GONE);
                            sr.getLayoutParams().width = 0;
                            sr.getLayoutParams().height = 0;
                        }
                        mRelLay4.setVisibility(View.GONE);
                        mRelLay4.getLayoutParams().width = 0;
                        mRelLay4.getLayoutParams().height = 0;

                    }
                });

                fab5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String dir = getFilesDir().getAbsolutePath();
                        File f0 = new File(dir, appeal.getPhotoByNumber(5));
                        boolean d0 = f0.delete();

                        appeal.addPhotoByNumber(5, "");
                        listPosponedAppeals.get(position).addPhotoByNumber(5, "");
                        remControlApplication.setPostponedAppeals(listPosponedAppeals);
                        remControlApplication.savePostponedAppeals();
                        if(!appeal.isPhotos()){
                            sr.setVisibility(View.GONE);
                            sr.getLayoutParams().width = 0;
                            sr.getLayoutParams().height = 0;
                        }
                        mRelLay5.setVisibility(View.GONE);
                        mRelLay5.getLayoutParams().width = 0;
                        mRelLay5.getLayoutParams().height = 0;

                    }
                });


            }else{
                Log.d("debugGetView","Pos:  "+ Integer.toString(position) + "   convView existing****");
            }
            return convertView;
        }


        /**
         * set photo by path on view
         * resize photo bitmap to required size
         * @param mImageView view on which photo will be seted
         * @param relLay parent layout
         * @param mCurrentPhotoPath path to photo to display
         */
        private void setPic(final ImageView mImageView, RelativeLayout relLay, final String mCurrentPhotoPath) {
            relLay.setVisibility(View.VISIBLE);
            relLay.getLayoutParams().width = (int) getResources().getDimension(R.dimen.imageview_width);
            relLay.getLayoutParams().height = (int) getResources().getDimension(R.dimen.imageview_height);
            mImageView.getLayoutParams().width = (int) getResources().getDimension(R.dimen.imageview_width);
            mImageView.getLayoutParams().height = (int) getResources().getDimension(R.dimen.imageview_height);
            int targetW = mImageView.getLayoutParams().width;
            int targetH = mImageView.getLayoutParams().height;


            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            //bmOptions.inJustDecodeBounds = true;

            //BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            mImageView.setImageBitmap(bitmap);
            bitmap = null;

        }


        /**
         * Delete appeal from saved in remControlApplication shared preferences.
         * After deleting initing renew listView
         * @see RemControlApplication
         * @param appealIndex index of appeal in list to delete
         */
        private void delAppeal(int appealIndex){

            if(appealIndex != -1) {
                remControlApplication.getPostponedAppeals().remove(appealIndex);
                remControlApplication.savePostponedAppeals();
            }else {
                remControlApplication.setPostponedAppeals(new ArrayList<PostponedAppeal>());
                remControlApplication.deleteAllPostponedAppeals();
            }
            listPosponedAppeals = remControlApplication.getPostponedAppeals();
            setAppealListOnView();
            revisibleButtons();

        }


        /**
         * Make intent to default gallery to take photo
         * @see Intent
         * @param position number of appeal in list in which photo will be added
         */
        private void addPhotoToAppeal(int position){
            if(listPosponedAppeals.get(position).getNumberOfFirstFreePhoto() != 0) {
                numberOfAppealLastAddedPhoto = position;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), 1);
            }else{
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Максимальна доступно 5 фото", Toast.LENGTH_LONG);
                toast.show();
            }
        }

    }



    /**
     * default metod, executes when on result activity closed and send result
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null) {
            Uri selectedImageUri = data.getData();
            String photoPath = getPath(this, selectedImageUri);

            listPosponedAppeals.get(numberOfAppealLastAddedPhoto).addPhotoByNumber(
                    listPosponedAppeals.get(numberOfAppealLastAddedPhoto).getNumberOfFirstFreePhoto(), photoPath);
            remControlApplication.setPostponedAppeals(listPosponedAppeals);
            remControlApplication.savePostponedAppeals();
            setAppealListOnView();
        }
    }




    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @author paulburke
     */
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }



    /**
     * show or hide progress bar
     * @param show if true - progress bar shown, activity layout hides; if false - progress bar hides, activity layout shown;
     */
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mListViewPosponedAppeals.setVisibility(show ? View.GONE : View.VISIBLE);
            mListViewPosponedAppeals.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mListViewPosponedAppeals.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mListViewPosponedAppeals.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }



    /**
     * @author SergPohh
     * asynchronic class to sent single appeal or list to server
     */

    private class SentAppeal extends AsyncTask<Void, Void, Boolean> {

        int houseId = -1;
        // final PostponedAppeal appeal;
        final Context cont;
        int appealIndex = -1;
        ArrayList<PostponedAppeal> appeals;

        /**
         * constructor to list of appeals
         * @param appeals list appeals to sent
         * @param cont activity context
         */
        SentAppeal(ArrayList<PostponedAppeal> appeals,  Context cont) {
            this.appeals = appeals;
            this.cont = cont;
        }

        /**
         * constructor to single appeal
         *
         * @param appeal appeal to sent
         * @param appealIndex index of sended appeal
         * @param cont activity context
         */
        SentAppeal(PostponedAppeal appeal, int appealIndex, Context cont) {
            appeals = new ArrayList<PostponedAppeal>();
            appeals.add(appeal);
            this.cont = cont;
            this.appealIndex = appealIndex;
        }

        /**
         * display progress bar
         */
        @Override
        protected void onPreExecute(){
            showProgress(true);
        }

        /**
         *
         * @param params
         * @return if appeals was successfully sended
         *
         * At first get house id by coordinates
         * At second sent appeals to server
         */
        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            for (int i = 0; i < appeals.size(); ++i) {
                houseId = ActionHttp.getNearestAddressesByCoordinates(appeals.get(i).getLat(), appeals.get(i).getLon(),
                        cont);

                if (houseId == -1 && houseId == 0) {
                    return false;
                } else {
                    ActionHttp.newAppealSent(houseId, appeals.get(i).getLat(), appeals.get(i).getLon(), appeals.get(i).getText(), cont);
                }
            }
            return true;
        }

        /**
         * If sending was successful make toast with success massage,
         * delete all sended appeals from postponed on
         * @see RemControlApplication
         * Shared preferences
         * If sending was fail make toast with error massage
         * @param success result of doInBackground()
         */
        @Override
        protected void onPostExecute ( final Boolean success){
            // mAuthTask = null;
            // showProgress(false);

            if (success) {
                if(appealIndex != -1) {
                    remControlApplication.getPostponedAppeals().remove(appealIndex);
                    remControlApplication.savePostponedAppeals();
                }else {
                    remControlApplication.setPostponedAppeals(new ArrayList<PostponedAppeal>());
                    remControlApplication.deleteAllPostponedAppeals();
                }
                listPosponedAppeals = remControlApplication.getPostponedAppeals();
                setAppealListOnView();
                revisibleButtons();
                showProgress(false);
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Відправлено успішно!", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                showProgress(false);
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Виникла помилка, спробуйте пізніше", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

}