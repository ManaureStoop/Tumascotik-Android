package com.arawaney.tumascotik.client.adapter;

import java.util.ArrayList;

import com.arawaney.tumascotik.client.model.ItemDetails;

import com.arawaney.tumascotik.client.R;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemListBaseAdapter extends BaseAdapter {
	private static ArrayList<ItemDetails> itemDetailsrrayList;
	Context contxt;
	

	
	private LayoutInflater l_Inflater;

	public ItemListBaseAdapter(Context context, ArrayList<ItemDetails> results) {
		itemDetailsrrayList = results;
		l_Inflater = LayoutInflater.from(context);
		contxt = context;
	}

	public int getCount() {
		return itemDetailsrrayList.size();
	}

	public Object getItem(int position) {
		return itemDetailsrrayList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = l_Inflater.inflate(R.layout.items_details_view, null);
			holder = new ViewHolder();
			
			holder.txt_itemName = (TextView) convertView.findViewById(R.id.title);
			holder.txt_itemDescription = (TextView) convertView.findViewById(R.id.itemDescription);
			holder.txt_itemTime = (TextView) convertView.findViewById(R.id.time);
			holder.itemImage = (ImageView) convertView.findViewById(R.id.photo);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String path = new String( String.valueOf(itemDetailsrrayList.get(position).getRace()));
		holder.txt_itemName.setText(itemDetailsrrayList.get(position).getTitle());
		holder.txt_itemDescription.setText(itemDetailsrrayList.get(position).getItemDescription());
		holder.txt_itemTime.setText(itemDetailsrrayList.get(position).getTime());
		holder.itemImage.setImageResource(getImageId(contxt, path));
		return convertView;
	}
	public static int getImageId(Context context, String imageName) {
	    return context.getResources().getIdentifier("drawable/" + imageName.toLowerCase(), null, context.getPackageName());
	}
	static class ViewHolder {
		TextView txt_itemName;
		TextView txt_itemDescription;
		TextView txt_itemTime;
		ImageView itemImage;
	}
}
