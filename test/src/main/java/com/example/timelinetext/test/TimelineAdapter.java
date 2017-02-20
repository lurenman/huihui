package com.example.timelinetext.test;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TimelineAdapter extends BaseAdapter {

	private Context context;
	private List<Map<String, Object>> list;
	private LayoutInflater inflater;

	public TimelineAdapter(Context context, List<Map<String, Object>> list) {
		super();
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {

		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			inflater = LayoutInflater.from(parent.getContext());
			convertView = inflater.inflate(R.layout.listview_item, null);
			viewHolder = new ViewHolder();

			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
			viewHolder.edge = (RelativeLayout) convertView.findViewById(R.id.relative);
			viewHolder.subtitle = (RelativeLayout) convertView.findViewById(R.id.subtitle);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		String titleStr = list.get(position).get("title").toString();
		final String a = list.get(position).get("title").toString();
		Log.e("Time", a);
		viewHolder.title.setText(titleStr);
		viewHolder.edge .setOnClickListener(new TextView.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.w("Time-test", a);
				Toast.makeText(context, a, Toast.LENGTH_SHORT).show();
			}
		});
		if(a.equals("这是第1行测试数据")==true){
			viewHolder.subtitle.setVisibility(View.VISIBLE);
		}else{
			viewHolder.subtitle.setVisibility(View.GONE);
		}
		return convertView;
	}



	static class ViewHolder {
		public TextView year;
		public TextView month;
		public TextView title;
		public RelativeLayout edge;
		public RelativeLayout subtitle;
	}




}
