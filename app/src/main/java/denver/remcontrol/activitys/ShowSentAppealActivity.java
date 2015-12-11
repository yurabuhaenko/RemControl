package denver.remcontrol.activitys;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.apache.http.HttpEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import container.Appeal;
import container.ContainerSignInResponse;
import denver.remcontrol.R;
import system.ActionHttp;
import system.ServiceServerHandler;

public class ShowSentAppealActivity extends NavigationDrawerActivity {

    Appeal appealSent;

    TextView textViewDistrict;
    TextView textViewAddress;
    TextView textViewText;
    TextView textViewStatus;
    TextView textViewCommentDate;
    TextView textViewComment;
    TextView textViewCommentText;
    LinearLayout linearLayoutStatusInActivityShow;
    LinearLayout linearLayoutPhotos;

    ImageView mImageView1;
    ImageView mImageView2;
    ImageView mImageView3;
    ImageView mImageView4;
    ImageView mImageView5;


    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_sent_appeal);
        postCreate(savedInstanceState, R.layout.activity_show_sent_appeal);


        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);

        Bundle bn = new Bundle();
        Intent intent = getIntent();
        int pos = intent.getIntExtra("position", 0);

        appealSent = remControlApplication.getAppealSents().get(pos);

        textViewDistrict = (TextView) findViewById(R.id.textViewDistrictInActivityShow);
        textViewAddress = (TextView) findViewById(R.id.textViewAddressInActivityShow);
        textViewText = (TextView) findViewById(R.id.textViewTextInActivityShow);
        textViewStatus = (TextView) findViewById(R.id.textViewStatusInActivityShow);
        linearLayoutStatusInActivityShow = (LinearLayout) findViewById(R.id.linearLayoutStatusInActivityShow);

        linearLayoutPhotos = (LinearLayout) findViewById(R.id.linearLayoutPhotos);

        textViewDistrict.setText(appealSent.getDistrict());
        textViewAddress.setText(appealSent.getAddress());
        textViewText.setText(appealSent.getAppealText());
        textViewStatus.setText(appealSent.getStatus());

        textViewCommentDate = (TextView) findViewById(R.id.commentDate);
        textViewComment = (TextView) findViewById(R.id.comment);
        textViewCommentText = (TextView) findViewById(R.id.textOfComment);

        mImageView1 = (ImageView)findViewById(R.id.imageViewPhotoSent1);
        mImageView2 = (ImageView)findViewById(R.id.imageViewPhotoSent2);
        mImageView3 = (ImageView)findViewById(R.id.imageViewPhotoSent3);
        mImageView4 = (ImageView)findViewById(R.id.imageViewPhotoSent4);
        mImageView5 = (ImageView)findViewById(R.id.imageViewPhotoSent5);

        mProgressView = findViewById(R.id.photos_download_progress);

        if(appealSent.getComments().size() > 0){
            textViewCommentDate.setText( appealSent.getComments().get(0).getDate().toString());
            textViewCommentText.setText(appealSent.getComments().get(0).getText());
        }else {
            textViewComment.setVisibility(View.GONE);
        }


        switch (appealSent.getStatusCode()){
            case 3:
                linearLayoutStatusInActivityShow.setBackgroundColor(getResources().getColor(R.color.md_deep_orange_300));
                break;
            case 1:
                linearLayoutStatusInActivityShow.setBackgroundColor(getResources().getColor(R.color.md_yellow_400));
                break;
            case 2:
                linearLayoutStatusInActivityShow.setBackgroundColor(getResources().getColor(R.color.md_light_green_A200));
                break;
        }
        getSupportActionBar().setTitle("Перегляд звернення");
    }



    private boolean isVisiblePhoros = false;
    private boolean isDownloadedPhotos = false;



    public void onClickButtonAddPhoto(View view){
        if(isVisiblePhoros){
            isVisiblePhoros = false;
            linearLayoutPhotos.setVisibility(View.INVISIBLE);



        }else{
            isVisiblePhoros = true;
            linearLayoutPhotos.setVisibility(View.VISIBLE);

            if (!isDownloadedPhotos){
                ImageDownloadingTask task = new ImageDownloadingTask(appealSent.getPhotosUrl());
                task.execute();
            }

        }
    }


    private ImageView getViewByCurrentNumber(int curPhotoNumb){
        switch (curPhotoNumb){
            case 1:
                return mImageView1;
            // break;
            case 2:
                return mImageView2;
            //break;
            case 3:
                return mImageView3;
            //break;
            case 4:
                return mImageView4;
            //break;
            case 5:
                return mImageView5;
            // break;
        }
        return mImageView1;
    }





    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);



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

        }
    }


    public class ImageDownloadingTask extends AsyncTask<Void, Void, Boolean> {

        private ArrayList<String> urls;

        private ArrayList<Drawable> d;
        private ArrayList<Bitmap> bitm;

        String error;

        ImageDownloadingTask(ArrayList<String> urls) {
            this.urls = urls;
            bitm = new ArrayList<>();
        }

        @Override
        protected void onPreExecute(){
            showProgress(true);
        }


        /*
        @Override
        protected Boolean doInBackground(Void... params) {
            for(int i = 0; i < urls.size() && i < 5; ++i){

                Drawable drawable = loadImageFromWebOperations(urls.get(i));
                if (drawable == null){
                    return false;
                }else {
                    d.add(drawable);
                }
            }
            return true;
        }

        private Drawable loadImageFromWebOperations(String url) {
            try {
                //url = "https://1551.gov.ua" + url;
url = "http://social.gseosem.com/wp-content/plugins/wp-o-matic/cache201508/92f1ba5e96_b2e2t.png";

                //urlsrc = "http://social.gseosem.com/wp-content/plugins/wp-o-matic/cache201508/92f1ba5e96_b2e2t.png";
                Bitmap bmp = null;
                HttpEntity entity = ServiceServerHandler.makeServiceCallForEntity(url);
                Drawable draw = null;
                if (entity != null) {
                    InputStream instream = entity.getContent();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    int bufferSize = 1024;
                    byte[] buffer = new byte[bufferSize];
                    int len = 0;

                    // instream is content got from httpentity.getContent()
                    while ((len = instream.read(buffer)) != -1) {
                        baos.write(buffer, 0, len);
                    }
                    baos.close();
                    draw = Drawable.createFromStream(instream, "src name");
                }


                return draw;
            } catch (Exception e) {
                error = e.getMessage();
                return null;
            }
        }

            @Override
            protected void onPostExecute ( final Boolean success){

                showProgress(false);

                if (success) {
                    for (int i = 0; i < d.size() && i < 5; ++i) {
                        ImageView image = getViewByCurrentNumber(i + 1);
                        if (d.get(i).equals(null)) {
                            Toast t = Toast.makeText(ShowSentAppealActivity.this, "Error. Unnable to download!", Toast.LENGTH_LONG);
                        } else {
                            image.setImageDrawable(d.get(i));
                        }
                    }
                    isDownloadedPhotos = true;
                } else {
                    Toast t = Toast.makeText(ShowSentAppealActivity.this, "Error. Unnable to download!", Toast.LENGTH_LONG);
                }
            }



        */

        @Override
        protected Boolean doInBackground(Void... params) {
            ImageLoader imageLoader = ImageLoader.getInstance();
            for(int i = 0; i < urls.size() && i < 5; ++i){

                ///Bitmap bitmap = loadImageFromWebOperations(urls.get(i));
                String str ="https://1551.gov.ua"+urls.get(i);
                Bitmap bitmap = imageLoader.loadImageSync(str);
                if (bitmap == null){
                    return false;
                }else {
                    bitm.add(bitmap);
                }
            }
            return true;
        }

        private Bitmap loadImageFromWebOperations(String urlsrc) {
            try {
                urlsrc = "https://1551.gov.ua"+urlsrc;
                //urlsrc = "http://social.gseosem.com/wp-content/plugins/wp-o-matic/cache201508/92f1ba5e96_b2e2t.png";
                Bitmap bmp = null;
                HttpEntity entity = ServiceServerHandler.makeServiceCallForEntity(urlsrc);
                if (entity != null){
                    InputStream instream = entity.getContent();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    int bufferSize = 1024;
                    byte[] buffer = new byte[bufferSize];
                    int len = 0;
                    try {
                        // instream is content got from httpentity.getContent()
                        while ((len = instream.read(buffer)) != -1) {
                            baos.write(buffer, 0, len);
                        }
                        baos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    byte[] byteArr = baos.toByteArray();
                    //bmp = BitmapFactory.decodeByteArray(byteArr, 0, byteArr.length);

                    //bmp = BitmapFactory.decodeByteArray(byteArr, 0, byteArr.length);
                }

                //InputStream input = connection.getInputStream();
                //Bitmap b = BitmapFactory.decodeStream(input);
                //Bitmap b = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                return bmp;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }




            @Override
            protected void onPostExecute ( final Boolean success){

                showProgress(false);

                if (success) {
                    for(int i = 0; i < bitm.size() && i < 5; ++i){
                        ImageView image = getViewByCurrentNumber(i + 1);
                        if (bitm.get(i).equals(null)){
                            Toast t = Toast.makeText(ShowSentAppealActivity.this, "Error. Unnable to download!", Toast.LENGTH_LONG);
                        }else {
                            image.setImageBitmap(bitm.get(i));
                        }
                    }
                    isDownloadedPhotos = true;
                } else {
                    Toast t = Toast.makeText(ShowSentAppealActivity.this, "Error. Unnable to download!", Toast.LENGTH_LONG);
                }
            }





    }



}
