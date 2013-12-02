package com.arawaney.tumascotik.client.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.arawaney.tumascotik.client.R;
import com.arawaney.tumascotik.client.model.Pet;

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
		  if (footerView != null && position == getCount()-1) {
		        return footerView;
		    }
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_pet_view, null);
			holder = new ViewHolder();
			
			holder.petName = (TextView) convertView.findViewById(R.id.pet_item_textv_pet_name);
			holder.petAvatar = (ImageView) convertView.findViewById(R.id.pet_item_imagev_pet_avatar);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String path = new String( String.valueOf(itemsPet.get(position).getSpecie()));
		holder.petName.setText(itemsPet.get(position).getName());
		holder.petAvatar.setImageResource(getImageId(contxt, path));
		return convertView;
	}
	public static int getImageId(Context context, String imageName) {
	    return context.getResources().getIdentifier("drawable/" + imageName.toLowerCase(), null, context.getPackageName());
	}
	static class ViewHolder {

		TextView petName;
		ImageView petAvatar;
	}
}
