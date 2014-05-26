package com.arawaney.tumascotik.client.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.arawaney.tumascotik.client.R;
import com.arawaney.tumascotik.client.model.User;
import com.arawaney.tumascotik.client.util.*;

public class ItemUserGridAdapter extends BaseAdapter {
	private static ArrayList<User> itemsUser;
	Context contxt;
	View footerView;

	
	private LayoutInflater inflater;

	public ItemUserGridAdapter(Context context, ArrayList<User> users) {
		itemsUser = users;
		inflater = LayoutInflater.from(context);
		contxt = context;
	}

	
	
	public int getCount() {
	
	    return (footerView == null) ? itemsUser.size() : itemsUser.size() + 1;
    
	}

	public Object getItem(int position) {
		return itemsUser.get(position);
	}

	public long getItemId(int position) {
		return position;
	}
	
	public void setFooterView(View v) {
		footerView = v;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_picker_view, null);
			holder = new ViewHolder();
			
			holder.userName = (TextView) convertView.findViewById(R.id.picker_item_textv_name);
			holder.userAvatar = (ImageView) convertView.findViewById(R.id.picker_item_imagev_avatar);
			

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			//If view used to add user
			
			if (holder == null) {
				convertView = inflater.inflate(R.layout.item_picker_view, null);
				holder =  new ViewHolder();
				holder.userName = (TextView) convertView.findViewById(R.id.picker_item_textv_name);
				holder.userAvatar = (ImageView) convertView.findViewById(R.id.picker_item_imagev_avatar);
				convertView.setTag(holder);
			}
			
		}	
		
		if (footerView != null && position == getCount()-1) {
	        return footerView;
	    }
	String path;
		if (itemsUser.get(position).getGender()== User.GENDER_WOMAN) {
		path = "user_female";
		}else {
			path = "user_male";
		}
		
		holder.userName.setTypeface(FontUtil.getTypeface(contxt, FontUtil.ROBOTO_THIN));
		holder.userName.setText(itemsUser.get(position).getName());
		holder.userAvatar.setImageResource(BitMapUtil.getImageId(contxt, path));
		
		return convertView;
	}
	
	static class ViewHolder {

		TextView userName;
		ImageView userAvatar;
	}
}
