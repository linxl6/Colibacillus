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

public class ConfigAdapter extends BaseAdapter implements View.OnClickListener {

    private ArrayList<ConfigItem> data;
    private LayoutInflater layout;
    private Context context;
    private InnerItemOnclickListener mListener;

    public ConfigAdapter(Context context, ArrayList<ConfigItem> data) {
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
            convertView = layout.inflate(R.layout.config_item, null);
            it.name = convertView.findViewById(R.id.name);
            it.edit = convertView.findViewById(R.id.config_edit);
            it.edit.setOnClickListener(this);
            it.delete = convertView.findViewById(R.id.config_delete);
            it.delete.setOnClickListener(this);
            convertView.setTag(it);
        } else {
            it = (item) convertView.getTag();
        }
		/*if(data.get(arg0).get("result").equalsIgnoreCase("+"))
		{
			it.name.setTextColor(Color.RED);
			it.result.setTextColor(Color.RED);
		}*/
        it.edit.setTag(position);
        it.delete.setTag(position);
        it.name.setText(data.get(position).configName);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.itemClick(v);
        }
    }

    interface InnerItemOnclickListener {
        void itemClick(View v);
    }

    public void setOnInnerItemOnClickListener(InnerItemOnclickListener listener) {
        this.mListener = listener;
    }

    public final class item {
        public TextView name;
        public Button edit;
        public Button delete;
    }

}
