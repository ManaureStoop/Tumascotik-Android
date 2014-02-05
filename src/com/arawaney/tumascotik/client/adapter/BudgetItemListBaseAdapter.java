package com.arawaney.tumascotik.client.adapter;

import java.util.ArrayList;

import com.arawaney.tumascotik.client.model.BudgetItemDetails;
import com.arawaney.tumascotik.client.model.BudgetService;
import com.arawaney.tumascotik.client.model.Request;
import com.arawaney.tumascotik.client.model.Service;
import com.arawaney.tumascotik.client.util.FontUtil;
import com.arawaney.tumascotik.client.R;


import android.R.integer;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import android.widget.TextView;


public class BudgetItemListBaseAdapter extends BaseAdapter {
	private static ArrayList<BudgetService> services;
	Context contxt;
	private LayoutInflater l_Inflater;
	

	

	public BudgetItemListBaseAdapter(Context context, ArrayList<BudgetService> services) {
		this.services = services;
		l_Inflater = LayoutInflater.from(context);
		contxt = context;
	}

	public int getCount() {
		return services.size();
	}

	public Object getItem(int position) {
		return services.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder2 holder;
		if (convertView == null) {
			convertView = l_Inflater.inflate(R.layout.item_budget_view, null);
			holder = new ViewHolder2();
			
			holder.itemServiceName = (TextView) convertView.findViewById(R.id.budget_item_service);
			holder.txt_itemPrice = (TextView) convertView.findViewById(R.id.budget_item_price);
			

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder2) convertView.getTag();
		}
		holder.txt_itemPrice.setTypeface(FontUtil.getTypeface(contxt, FontUtil.ROBOTO_LIGHT));
		holder.itemServiceName.setText(services.get(position).getService().getName());
		
		String price = new String(" Bs."+String.valueOf(services.get(position).getPrice()));
			holder.txt_itemPrice.setText(price);
	    
		return convertView;
	}
	
	
	static class ViewHolder2 {
		TextView itemServiceName;
		TextView txt_itemPrice;

	}
}
