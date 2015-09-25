package denver.remcontrol.activitys;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.AdapterView.OnItemClickListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import container.ContainerAppealSent;
import denver.remcontrol.R;

import static android.support.v7.internal.widget.AdapterViewCompat.*;

public class AppealSentHistoryActivity extends NavigationDrawerActivity {


    ListView listViewAppealSents;
    AppealListAdapter appealListAdapter;
    List<ContainerAppealSent> appealSents = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appeal_sent_history);

        postCreate(savedInstanceState, R.layout.activity_appeal_sent_history);

        listViewAppealSents = (ListView)findViewById(R.id.listViewAppealSent);
        appealSents = new ArrayList<>();


        remControlApplication.setAppealSents(appealSents);

        listViewAppealSents.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AppealSentHistoryActivity.this, ShowSentAppealActivity.class);

                intent.putExtra("position", position);


                startActivity(intent);
            }
        });
        setAppealListOnView();
    }


    private void setAppealListOnView(){
        if(remControlApplication.getAppealSents()!= null && remControlApplication.getAppealSents().size() > 0)
        {
            listViewAppealSents.setAdapter(appealListAdapter);
        }else
        {
            listViewAppealSents.setAdapter(null);
        }
    }


    private class AppealListAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        List<ContainerAppealSent> appealSents;

        public AppealListAdapter(List<ContainerAppealSent> appealSents) {
            mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            this.appealSents = appealSents;
            //notifyDataSetChanged();
        }

        public List<ContainerAppealSent> getUserTasks() {
            return appealSents;
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
                    case 1:
                        linearLayoutStatus.setBackgroundColor(getResources().getColor(R.color.md_deep_orange_300));
                    case 2:
                        linearLayoutStatus.setBackgroundColor(getResources().getColor(R.color.md_yellow_400));
                    case 3:
                        linearLayoutStatus.setBackgroundColor(getResources().getColor(R.color.md_light_green_A200));
                }


            }
            return convertView;
        }



    }


}
