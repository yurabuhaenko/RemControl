package denver.remcontrol.activitys;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import container.PostponedAppeal;
import denver.remcontrol.R;
import system.ActionHttp;
import system.InternetConnectionChecker;
import system.RemControlApplication;


public class PostponedAppealsListActivity extends NavigationDrawerActivity {



    AppealListAdapter appealListAdapter;
    ArrayList<PostponedAppeal> listPosponedAppeals = new ArrayList<>();
    RemControlApplication remControlApplication;

    private View mProgressView;

    private View mListViewPosponedAppeals;

    private LinearLayout myList;

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

        mListViewPosponedAppeals = findViewById(R.id.scrollViewPostponedAppeals);

////////////////////
        myList = (LinearLayout) findViewById(R.id.listViewPostponedAppeals);
    }



    @Override
    protected void onResume(){
        super.onResume();
        setAppealListOnView();
    }





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


    private class AppealListAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        ArrayList<PostponedAppeal> listPosponedAppeals;

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



        public View getView(final int position, View convertView, ViewGroup parent) {

            PostponedAppeal appeal = listPosponedAppeals.get(position);

            final TextView textText;
            final TextView textDateTime;
            ImageView mImageView1;
            ImageView mImageView2;
            ImageView mImageView3;
            ImageView mImageView4;
            ImageView mImageView5;
            Button sentAppealButton;


            if (convertView == null) {
                Log.d("debugGetView","Pos: "+ Integer.toString(position) +"  text  "+ appeal.getText() +"  convView null");
                convertView = mInflater.inflate(R.layout.item_postponed_appeal, null);

                textText = (TextView) convertView.findViewById(R.id.textOfAppeal);
                textDateTime = (TextView) convertView.findViewById(R.id.textDateTimeOfSave);
                mImageView1 = (ImageView)convertView.findViewById(R.id.imageViewPhotoSaved1);
                mImageView2 = (ImageView)convertView.findViewById(R.id.imageViewPhotoSaved2);
                mImageView3 = (ImageView)convertView.findViewById(R.id.imageViewPhotoSaved3);
                mImageView4 = (ImageView)convertView.findViewById(R.id.imageViewPhotoSaved4);
                mImageView5 = (ImageView)convertView.findViewById(R.id.imageViewPhotoSaved5);
                sentAppealButton = (Button)convertView.findViewById(R.id.onClickSentPostponedAppeal);

                convertView.setTag(textDateTime);

                textText.setText(appeal.getText());
                textDateTime.setText(appeal.getDatetime());

                if(appeal.getPhoto1()!="")setPic(mImageView1, appeal.getPhoto1());
                if(appeal.getPhoto2()!="")setPic(mImageView2, appeal.getPhoto2());
                if(appeal.getPhoto3()!="")setPic(mImageView3, appeal.getPhoto3());
                if(appeal.getPhoto4()!="")setPic(mImageView4, appeal.getPhoto4());
                if(appeal.getPhoto5()!="")setPic(mImageView5, appeal.getPhoto5());

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
            }else{
                Log.d("debugGetView","Pos:  "+ Integer.toString(position) + "   convView existing****");
            }
            return convertView;
        }




        private void setPic(final ImageView mImageView, final String mCurrentPhotoPath) {

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



    }



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


    private class SentAppeal extends AsyncTask<Void, Void, Boolean> {

        int houseId = -1;
        final PostponedAppeal appeal;
        final Context cont;
        final int appealIndex;



        SentAppeal(PostponedAppeal appeal, int appealIndex, Context cont) {
            this.appeal = appeal;
            this.cont = cont;
            this.appealIndex = appealIndex;
        }

        @Override
        protected void onPreExecute(){
            showProgress(true);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            houseId  = ActionHttp.getNearestAddressesByCoordinates(appeal.getLat(), appeal.getLon(),
                    cont);

            if (houseId  == -1 &&  houseId == 0) {
                return false;
            }else{
                return ActionHttp.newAppealSent(houseId, appeal.getLat(), appeal.getLon(), appeal.getText(), cont);
            }

        }


        @Override
        protected void onPostExecute ( final Boolean success){
            // mAuthTask = null;
           // showProgress(false);

            if (success) {
                remControlApplication.getPostponedAppeals().remove(appealIndex);
                remControlApplication.savePostponedAppeals();
                setAppealListOnView();
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