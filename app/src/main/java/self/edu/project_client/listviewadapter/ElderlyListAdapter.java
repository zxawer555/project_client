package self.edu.project_client.listviewadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import self.edu.project_client.R;

/**
 * Created by wongfuchuen on 11/1/2017.
 */

public class ElderlyListAdapter extends BaseAdapter {

    private ArrayList<Elderly> elderlyLists;
    private Context mContext;

    public ElderlyListAdapter(ArrayList<Elderly> elderLists, Context context) {
        this.elderlyLists = elderLists;
        this.mContext = context;
    }

    private class ViewHolder  {

        private ImageView imgElderlyIcon;
        private TextView tvElderlyName;

    }

    @Override
    public int getCount() {
        return elderlyLists.size();
    }

    @Override
    public Elderly getItem(int position) {
        return elderlyLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        LayoutInflater inflater = LayoutInflater.from(this.mContext);
        convertView = inflater.inflate(R.layout.listview_elderly_item, parent, false);

        viewHolder = new ViewHolder();
        viewHolder.imgElderlyIcon = (ImageView) convertView.findViewById(R.id.imgElderlyIcon);
        viewHolder.tvElderlyName = (TextView) convertView.findViewById(R.id.tvElderlyName);
        convertView.setTag(viewHolder);

        Elderly elderly = elderlyLists.get(position);

        viewHolder.tvElderlyName.setText(elderly.getIdentifier());

        return convertView;
    }

}
