package com.linxl.colibacillus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.linxl.colibacillus.model.ConfigItem;

import java.util.ArrayList;

public class SpinnerAdapter extends BaseAdapter {

    private ArrayList<ConfigItem> data;
    private LayoutInflater layout;
    private Context context;

    public SpinnerAdapter(Context context, ArrayList<ConfigItem> data) {
        this.context = context;
        this.data = data;
        this.layout = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        item it = null;
        if (convertView == null) {
            it = new item();
            convertView = layout.inflate(R.layout.spanner_text, null);
            it.name = convertView.findViewById(R.id.tv_name);
        } else {
            it = (item) convertView.getTag();
        }
		/*if(data.get(arg0).get("result").equalsIgnoreCase("+"))
		{
			it.name.setTextColor(Color.RED);
			it.result.setTextColor(Color.RED);
		}*/
        it.name.setText(data.get(position).configName);
        convertView.setTag(it);
        return convertView;
    }

    public final class item {
        public TextView name;
    }
}
