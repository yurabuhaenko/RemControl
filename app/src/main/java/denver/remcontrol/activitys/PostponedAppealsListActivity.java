package denver.remcontrol.activitys;

import android.os.Bundle;

import denver.remcontrol.R;

public class PostponedAppealsListActivity extends NavigationDrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postponed_appeals_list);

        postCreate(savedInstanceState, R.layout.activity_postponed_appeals_list);



    }
/*

    private class PostponedAppealListAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        List<PostponedAppeals> postponedAppeals;

        public PostponedAppealListAdapter(List<PostponedAppeals> appealSents) {
            mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            this.postponedAppeals = appealSents;
            //notifyDataSetChanged();
        }

        public List<PostponedAppeals> getUserTasks() {
            return postponedAppeals;
        }

        public int getCount() {
            return postponedAppeals.size();
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



            }
            return convertView;
        }



    }
*/

}
