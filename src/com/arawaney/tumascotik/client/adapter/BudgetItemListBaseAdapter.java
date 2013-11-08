package com.arawaney.tumascotik.client.adapter;

import java.util.ArrayList;

import com.arawaney.tumascotik.client.model.BudgetItemDetails;
import com.arawaney.tumascotik.client.R;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import android.widget.TextView;


public class BudgetItemListBaseAdapter extends BaseAdapter {
	private static ArrayList<BudgetItemDetails> BudgetitemDetailsrrayList;
	Context contxt;
	private LayoutInflater l_Inflater;
	

	

	public BudgetItemListBaseAdapter(Context context, ArrayList<BudgetItemDetails> results) {
		BudgetitemDetailsrrayList = results;
		l_Inflater = LayoutInflater.from(context);
		contxt = context;
	}

	public int getCount() {
		return BudgetitemDetailsrrayList.size();
	}

	public Object getItem(int position) {
		return BudgetitemDetailsrrayList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder2 holder;
		if (convertView == null) {
			convertView = l_Inflater.inflate(R.layout.budget_items_details, null);
			holder = new ViewHolder2();
			
			holder.itemImage = (ImageView) convertView.findViewById(R.id.ivmotivebudget);
			holder.txt_itemPrice = (TextView) convertView.findViewById(R.id.price);
			

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder2) convertView.getTag();
		}
		String path = new String( String.valueOf(BudgetitemDetailsrrayList.get(position).getTitle()));
		Log.d("path",path.toLowerCase());
		holder.itemImage.setImageResource(getImageId(contxt, path));
		
		String price = new String(String.valueOf(BudgetitemDetailsrrayList.get(position).getPrice())+" Bs");
		if(price.equals("0 Bs"))
			holder.txt_itemPrice.setText("");
		else
			holder.txt_itemPrice.setText(price);
	    
		return convertView;
	}
	
	public static int getImageId(Context context, String imageName) {
	    return context.getResources().getIdentifier("drawable/" + imageName.toLowerCase(), null, context.getPackageName());
	}
	static class ViewHolder2 {
		ImageView itemImage;
		TextView txt_itemPrice;

	}
}
