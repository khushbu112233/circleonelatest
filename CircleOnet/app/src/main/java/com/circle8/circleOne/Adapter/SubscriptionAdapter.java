package com.circle8.circleOne.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.circle8.circleOne.Model.SubscriptionModel;
import com.circle8.circleOne.R;

import java.util.ArrayList;

/**
 * Created by ample-arch on 9/9/2017.
 */

public class SubscriptionAdapter extends BaseAdapter
{
    Context context ;
    private ArrayList<SubscriptionModel> subscriptionModelArrayList ;

    private ArrayList<String> packageName = new ArrayList<>();
    private ArrayList<Integer> contact = new ArrayList<>();
    private ArrayList<Integer> group = new ArrayList<>();
    private ArrayList<Integer> connection = new ArrayList<>();
    private ArrayList<Integer> amount = new ArrayList<>();
    private ArrayList<Integer> left_connection = new ArrayList<>();

    public SubscriptionAdapter(Context applicationContext, ArrayList<String> packageName,
           ArrayList<Integer> contact, ArrayList<Integer> group, ArrayList<Integer> connection,
                               ArrayList<Integer> amount, ArrayList<Integer> left_connection)
    {
        this.context = applicationContext ;
        this.packageName = packageName ;
        this.contact = contact ;
        this.group = group ;
        this.connection = connection ;
        this.amount = amount ;
        this.left_connection = left_connection;
    }

    @Override
    public int getCount() {
        return packageName.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;

        if( row == null)
        {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.subscription_details, null);

            TextView tvPackageName = (TextView)row.findViewById(R.id.tvPackageName);
            TextView tvConnect_Group = (TextView)row.findViewById(R.id.tvContact_Group);
            TextView tvConnection = (TextView)row.findViewById(R.id.tvConnection);
            TextView tvAmount = (TextView)row.findViewById(R.id.tvAmount);
            TextView tvLeftConnection = (TextView)row.findViewById(R.id.tvLeftConnection);
            RelativeLayout rlLeftConnection = (RelativeLayout)row.findViewById(R.id.rlLeftConnection);

            tvPackageName.setText(packageName.get(position));

            int group_no = group.get(position);

            if(group_no > 5 )
            {
                tvConnect_Group.setText(contact.get(position)+" contacts, unlimited groups,");
            }
            else
            {
                tvConnect_Group.setText(contact.get(position)+" contacts, up to "+group.get(position)+" groups,");
            }

            int connection_no = connection.get(position);
            if( connection_no < 1)
            {
                tvConnection.setText(connection.get(position)+" connections per month.");
            }
            else
            {
                tvConnection.setText("up to "+connection.get(position)+" connections per month.");
            }


            tvAmount.setText("S$"+amount.get(position));

            int l_connect = left_connection.get(position);

            if(l_connect <= 0)
            {
                rlLeftConnection.setVisibility(View.GONE);
            }
            else
            {
                tvLeftConnection.setText(l_connect+" connections left, until the end of month");
            }

        }

        return row;
    }
}
