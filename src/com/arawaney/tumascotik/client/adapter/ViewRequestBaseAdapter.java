package com.arawaney.tumascotik.client.adapter;

import java.util.ArrayList;

import com.arawaney.tumascotik.client.db.provider.ServiceProvider;
import com.arawaney.tumascotik.client.model.Request;
import com.arawaney.tumascotik.client.model.Service;
import com.arawaney.tumascotik.client.util.BitMapUtil;
import com.arawaney.tumascotik.client.util.CalendarUtil;
import com.arawaney.tumascotik.client.util.FontUtil;

import com.arawaney.tumascotik.client.R;
import android.content.Context;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewRequestBaseAdapter extends BaseAdapter {
	private static ArrayList<Request> requests;
	Context contxt;
	private String[] status;
	private LayoutInflater lInflater;


	public ViewRequestBaseAdapter(Context context, ArrayList<Request> results) {
		requests = results;
		lInflater = LayoutInflater.from(context);
		contxt = context;
		status = contxt.getResources().getStringArray(R.array.Status);
	}

	public int getCount() {
		return requests.size();
	}

	public Object getItem(int position) {
		return requests.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = lInflater.inflate(R.layout.item_request_view, null);
			holder = new ViewHolder();
			
			holder.petName = (TextView) convertView.findViewById(R.id.textView_request_view_petname);
			holder.serviceName = (TextView) convertView.findViewById(R.id.textView_request_view_title_service);
			holder.day = (TextView) convertView.findViewById(R.id.textView_request_view_title_day_2);
			holder.time = (TextView) convertView.findViewById(R.id.textView_request_view_title_time_2);
			holder.status = (TextView) convertView.findViewById(R.id.textView_request_view_status);
			holder.petAvatar = (ImageView) convertView.findViewById(R.id.imageView_request_view_petavatar);
			holder.serviceNameTitle = (TextView) convertView.findViewById(R.id.textView_request_view_title);
			holder.dayTitle = (TextView) convertView.findViewById(R.id.textView_request_view_title_day);
			holder.timeTitle = (TextView) convertView.findViewById(R.id.textView_request_view_title_time);
			
			
			holder.petName.setTypeface(FontUtil.getTypeface(contxt, FontUtil.ROBOTO_LIGHT));
			holder.serviceName.setTypeface(FontUtil.getTypeface(contxt, FontUtil.ROBOTO_LIGHT));
			holder.day.setTypeface(FontUtil.getTypeface(contxt, FontUtil.ROBOTO_LIGHT));
			holder.time.setTypeface(FontUtil.getTypeface(contxt, FontUtil.ROBOTO_LIGHT));
			holder.serviceNameTitle.setTypeface(FontUtil.getTypeface(contxt, FontUtil.ROBOTO_LIGHT));
			holder.dayTitle.setTypeface(FontUtil.getTypeface(contxt, FontUtil.ROBOTO_LIGHT));
			holder.timeTitle.setTypeface(FontUtil.getTypeface(contxt, FontUtil.ROBOTO_LIGHT));
			holder.status.setTypeface(FontUtil.getTypeface(contxt, FontUtil.ROBOTO_REGULAR));
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String path = new String("mini_"+ String.valueOf(requests.get(position).getPet().getBreed().getSpecie().getName()));
		holder.petName.setText(requests.get(position).getPet().getName().toString());
		Service service = requests.get(position).getService();
		holder.serviceName.setText(service.getName());
		Log.d("LOG", "path");
		String dayOfWeek = CalendarUtil.getDateFormated(requests.get(position).getStart_date(), "EEEE");
		String date = CalendarUtil.getDateFormated(requests.get(position).getStart_date(), "dd MMMM yyyy");
		String totalDate = dayOfWeek+""+", "+date;
		holder.day.setText(totalDate);
		
		holder.time.setText(CalendarUtil.getDateFormated(requests.get(position).getStart_date(), "hh:mm a"));
		holder.status.setText(status[requests.get(position).getStatus()-1]);
		if (requests.get(position).getStatus() == Request.STATUS_ACCEPTED) {
			holder.status.setTextColor(contxt.getResources().getColor(R.color.request_status_accepted));
		}else if (requests.get(position).getStatus() == Request.STATUS_PENDING) {
			holder.status.setTextColor(contxt.getResources().getColor(R.color.request_status_pending));
		}else if (requests.get(position).getStatus() == Request.STATUS_CANCELED) {
			holder.status.setTextColor(contxt.getResources().getColor(R.color.request_status_canceled));
		}
		holder.status.setText(status[requests.get(position).getStatus()-1]);
		holder.petAvatar.setImageResource(BitMapUtil.getImageId(contxt, path));
		
		return convertView;
	}


	static class ViewHolder {
		TextView petName;
		TextView serviceName;
		TextView serviceNameTitle;
		TextView day;
		TextView dayTitle;
		TextView time;
		TextView timeTitle;
		TextView status;
		ImageView petAvatar;
	}
}
