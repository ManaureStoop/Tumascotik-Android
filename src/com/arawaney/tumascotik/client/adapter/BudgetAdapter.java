package com.arawaney.tumascotik.client.adapter;

import java.util.ArrayList;

import com.arawaney.tumascotik.client.control.MainController;
import com.arawaney.tumascotik.client.db.provider.UserProvider;
import com.arawaney.tumascotik.client.model.Budget;
import com.arawaney.tumascotik.client.model.BudgetItemDetails;
import com.arawaney.tumascotik.client.model.BudgetService;
import com.arawaney.tumascotik.client.model.Request;
import com.arawaney.tumascotik.client.model.Service;
import com.arawaney.tumascotik.client.model.User;
import com.arawaney.tumascotik.client.util.CalendarUtil;
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


public class BudgetAdapter extends BaseAdapter {
	private static ArrayList<Budget> budgets;
	Context contxt;
	private LayoutInflater l_Inflater;
	private String[] status;

	

	public BudgetAdapter(Context context, ArrayList<Budget> budgets) {
		this.budgets = budgets;
		l_Inflater = LayoutInflater.from(context);
		contxt = context;
		status = contxt.getResources().getStringArray(R.array.Status);

	}

	public int getCount() {
		return budgets.size();
	}

	public Object getItem(int position) {
		return budgets.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder2 holder;
		if (convertView == null) {
			convertView = l_Inflater.inflate(R.layout.item_send_budget_view, null);
			holder = new ViewHolder2();
			
			holder.txt_itemUserName = (TextView) convertView.findViewById(R.id.textView_budget_username);
			holder.txt_itemPrice = (TextView) convertView.findViewById(R.id.textView_budget_total_price);
			holder.txt_itemDate = (TextView) convertView.findViewById(R.id.textView_budget_view_date);
			holder.txt_itemStatus = (TextView) convertView.findViewById(R.id.textView_budget_view_status);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder2) convertView.getTag();
		}
		
		if (MainController.USER.getisAdmin() == User.IS_ADMIN ) {
			String userId = budgets.get(position).getUserId();
			User user = UserProvider.readUser(contxt);
			holder.txt_itemUserName.setText(user.getName()+" "+user.getLastname());
		}
		else{
			holder.txt_itemUserName.setVisibility(View.GONE);
		}
		setFonts(holder);
		
		
		
		String price = new String(" Bs."+String.valueOf(budgets.get(position).getTotal()));
		holder.txt_itemPrice.setText(price);
	    
	    holder.txt_itemDate.setText(CalendarUtil.getDateFormated(budgets.get(position).getCreatedAt(), "dd/MM/yyyy"));
		
	    holder.txt_itemStatus.setText(status[budgets.get(position).getStatus()-1]);
		
	    if (budgets.get(position).getStatus() == Budget.STATUS_DELIVERED) {
			holder.txt_itemStatus.setTextColor(contxt.getResources().getColor(R.color.request_status_accepted));
		}else if (budgets.get(position).getStatus() == Budget.STATUS_IN_PROCESS) {
			holder.txt_itemStatus.setTextColor(contxt.getResources().getColor(R.color.request_status_pending));
		}else if (budgets.get(position).getStatus() == Budget.STATUS_READY) {
			holder.txt_itemStatus.setTextColor(contxt.getResources().getColor(R.color.request_status_canceled));
		}
		
		return convertView;
	}

	private void setFonts(ViewHolder2 holder) {
		holder.txt_itemPrice.setTypeface(FontUtil.getTypeface(contxt, FontUtil.ROBOTO_LIGHT));
		holder.txt_itemUserName.setTypeface(FontUtil.getTypeface(contxt, FontUtil.ROBOTO_LIGHT));
		holder.txt_itemDate.setTypeface(FontUtil.getTypeface(contxt, FontUtil.ROBOTO_LIGHT));
		holder.txt_itemStatus.setTypeface(FontUtil.getTypeface(contxt, FontUtil.ROBOTO_LIGHT));
	}
	
	
	static class ViewHolder2 {
		TextView txt_itemUserName;
		TextView txt_itemPrice;
		TextView txt_itemDate;
		TextView txt_itemStatus;
	}
}
