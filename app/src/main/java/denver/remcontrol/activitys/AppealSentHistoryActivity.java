package denver.remcontrol.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.AdapterView.OnItemClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import container.Appeal;
import denver.remcontrol.R;


/**
 * @author yurabuhaenko
 * Activity to show list of user appeals from server
 * extends
 * @see NavigationDrawerActivity
 */

public class AppealSentHistoryActivity extends NavigationDrawerActivity {


    ListView listViewAppealSents;
    AppealListAdapter appealListAdapter;
    ArrayList<Appeal> appealSents = new ArrayList<>();


    /**
     * default on create method
     * initialize view components, execute method to set listView
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appeal_sent_history);

        postCreate(savedInstanceState, R.layout.activity_appeal_sent_history);

        listViewAppealSents = (ListView)findViewById(R.id.listViewAppealSent);


        if(remControlApplication.getAppealSents()!= null && remControlApplication.getAppealSents().size() > 0) {
            appealSents = remControlApplication.getAppealSents();
        }else{
            appealSents = new ArrayList<Appeal>();
        }

        listViewAppealSents.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AppealSentHistoryActivity.this, ShowSentAppealActivity.class);

                intent.putExtra("position", position);


                startActivity(intent);
            }
        });
        setAppealListOnView();
        getSupportActionBar().setTitle("Історія звернень");
    }


    /**
     * create new adapter
     * set adapter to list view
     */
    private void setAppealListOnView(){

        if(appealSents.size() > 0){
            appealListAdapter = new AppealListAdapter(appealSents);
            listViewAppealSents.setAdapter(appealListAdapter);
        }else{
            listViewAppealSents.setAdapter(null);
        }
    }


    /**
     * Adapter class. Create list of item layouts to display it on view
     */
    private class AppealListAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        List<Appeal> appealSents;

        /**
         *
         * @param appealSents list of appeals to display
         */
        public AppealListAdapter(List<Appeal> appealSents) {
            mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            this.appealSents = appealSents;
            //notifyDataSetChanged();
        }


        public int getCount() {
            return appealSents.size();
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
         * @param position number of appeal for which it was executed
         * @param convertView current item layout
         * @param parent parent view
         * @return item layout for display one appeal
         */
        public View getView(final int position, View convertView, ViewGroup parent) {
            final TextView textViewDistrict;
            final TextView textViewAddress;
            final TextView textViewStatus;
            final LinearLayout linearLayoutStatus;

            if (convertView == null) {

                convertView = mInflater.inflate(R.layout.item_appeal_sent, null);

                textViewDistrict = (TextView) convertView.findViewById(R.id.textViewDistrict);
                textViewAddress = (TextView) convertView.findViewById(R.id.textViewAddress);
                textViewStatus = (TextView) convertView.findViewById(R.id.textViewStatus);
                linearLayoutStatus = (LinearLayout) convertView.findViewById(R.id.linearLayoutStatus);

                convertView.setTag(textViewAddress);

                textViewDistrict.setText(appealSents.get(position).getDistrict());
                textViewAddress.setText(appealSents.get(position).getAddress());
                textViewStatus.setText(appealSents.get(position).getStatus());
                switch (appealSents.get(position).getStatusCode()){
                    case 3:
                        linearLayoutStatus.setBackgroundColor(getResources().getColor(R.color.md_deep_orange_300));
                        break;
                    case 1:
                        linearLayoutStatus.setBackgroundColor(getResources().getColor(R.color.md_yellow_400));
                        break;
                    case 2:
                        linearLayoutStatus.setBackgroundColor(getResources().getColor(R.color.md_light_green_A200));
                        break;
                }


            }
            return convertView;
        }



    }


}
