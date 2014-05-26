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
import com.arawaney.tumascotik.client.model.Pet;
import com.arawaney.tumascotik.client.util.*;

public class ItemPetGridAdapter extends BaseAdapter {
	private static ArrayList<Pet> itemsPet;
	Context contxt;
	View footerView;

	
	private LayoutInflater inflater;

	public ItemPetGridAdapter(Context context, ArrayList<Pet> pets) {
		itemsPet = pets;
		inflater = LayoutInflater.from(context);
		contxt = context;
	
	}

	
	
	public int getCount() {
	
	    return (footerView == null) ? itemsPet.size() : itemsPet.size() + 1;
    
	}

	public Object getItem(int position) {
		return itemsPet.get(position);
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
			
			holder.petName = (TextView) convertView.findViewById(R.id.picker_item_textv_name);
			holder.petAvatar = (ImageView) convertView.findViewById(R.id.picker_item_imagev_avatar);
			

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			//If view used to add pet
			
			if (holder == null) {
				convertView = inflater.inflate(R.layout.item_picker_view, null);
				holder =  new ViewHolder();
				holder.petName = (TextView) convertView.findViewById(R.id.picker_item_textv_name);
				holder.petAvatar = (ImageView) convertView.findViewById(R.id.picker_item_imagev_avatar);
				convertView.setTag(holder);
			}
			
		}	
		
		if (footerView != null && position == getCount()-1) {
	        return footerView;
	    }
	
		String path = new String( String.valueOf(itemsPet.get(position).getBreed().getSpecie().getName()));
		holder.petName.setTypeface(FontUtil.getTypeface(contxt, FontUtil.ROBOTO_THIN));
		holder.petName.setText(itemsPet.get(position).getName());
		holder.petAvatar.setImageResource(BitMapUtil.getImageId(contxt, path));
		
		return convertView;
	}
	
	static class ViewHolder {

		TextView petName;
		ImageView petAvatar;
	}
}
