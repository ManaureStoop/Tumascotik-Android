package com.arawaney.tumascotik.client.activity;

import java.util.ArrayList;
import java.util.Date;

import com.arawaney.tumascotik.client.adapter.BudgetItemListBaseAdapter;
import com.arawaney.tumascotik.client.db.BudgetDB;
import com.arawaney.tumascotik.client.dialog.PresupuestoAlertDialog;
import com.arawaney.tumascotik.client.dialog.PresupuestoDeleteallDialog;
import com.arawaney.tumascotik.client.dialog.PresupuestoDialog;
import com.arawaney.tumascotik.client.dialog.PresupuestoItemDialog;
import com.arawaney.tumascotik.client.model.BudgetItemDetails;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import com.arawaney.tumascotik.client.R;


public class Budget extends  FragmentActivity {
	
	ImageView agregar;
	ImageView hacerp;
	ImageView deleteall;
	ListView listpresup;
	TextView totalprice;
	Boolean flagclick;
	int total;
	String motivos;
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_budget);	
		agregar = (ImageView) findViewById(R.id.bagregpresp);
		hacerp = (ImageView) findViewById(R.id.bhacerpedidopresp);
		deleteall = (ImageView) findViewById(R.id.bdeleteallpresp);
		totalprice = (TextView) findViewById(R.id.txttotal);
		

		
		flagclick = false;
		motivos = "";
		total = 0;
		totalprice.setText("TOTAL : "+String.valueOf(total));
		ArrayList<BudgetItemDetails> image_details = SetupList();
	    listpresup = (ListView) findViewById(R.id.listpresp);
	    listpresup.setAdapter(new BudgetItemListBaseAdapter(this, image_details));
		listpresup.setOnItemClickListener(new OnItemClickListener() {
	        	@Override
	        	public void onItemClick(AdapterView<?> a, View v, int position, long id) { 
	        		if (!flagclick){
	        		Object o = listpresup.getItemAtPosition(position);
	            	BudgetItemDetails obj_itemDetails = (BudgetItemDetails)o;
	            	DialogFragment newFragment = new PresupuestoItemDialog(obj_itemDetails.getTitle(),obj_itemDetails.getPrice());
					newFragment.show(getSupportFragmentManager(), "presupitem");
	        	}  }
	        });
		
		agregar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				BudgetDB db = new BudgetDB(Budget.this);
				DialogFragment newFragment = new PresupuestoDialog(Budget.this);
				newFragment.show(getSupportFragmentManager(), "presup");	
				
			}
		});
		hacerp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DialogFragment newFragment = new PresupuestoAlertDialog(motivos,total);
				newFragment.show(getSupportFragmentManager(), "presualert");
				
			}
		});
		deleteall.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DialogFragment newFragment = new PresupuestoDeleteallDialog();
				newFragment.show(getSupportFragmentManager(), "presudelete");
				
			}
		});
	}
	
	public void Refresh(){
		// TODO Auto-generated method stub
		super.onResume();
		ArrayList<BudgetItemDetails> image_details = SetupList();
	     listpresup = (ListView) findViewById(R.id.listpresp);
	     listpresup.setAdapter(new BudgetItemListBaseAdapter(this, image_details));
	}


private ArrayList<BudgetItemDetails> SetupList(){
	final int TITLE_INDEX = 0;
	final int PRICE_INDEX = 1;
	hacerp.setEnabled(true);
	deleteall.setEnabled(true);

	
	total = 0;
	motivos = "";
	ArrayList<BudgetItemDetails> results = new ArrayList<BudgetItemDetails>();
	Cursor cur;
	cur = null;
	
	BudgetDB db = new BudgetDB(this);
	db.open();
	
 
     	try{
		    cur = db.getAll();
	     	cur.moveToFirst();
	     
	   		do{
	   		
	   		BudgetItemDetails item_details = new BudgetItemDetails();
	     	item_details.setTitle(cur.getString(TITLE_INDEX));
	     	item_details.setPrice(cur.getInt(PRICE_INDEX)); 
	     	
	     	total += cur.getInt(PRICE_INDEX);
	     	motivos = motivos+", "+cur.getString(TITLE_INDEX);
	     	
	     	results.add(item_details);	
	   		}while(cur.moveToNext());
	   		
	   	 db.close();  
		 cur.close();	
		 flagclick = false;	
     	}catch(Exception e){
     		Log.d("ERROR DATE", e.toString());
     		BudgetItemDetails item_details = new BudgetItemDetails();
	     	item_details.setTitle("nohaypedidos");
	     	item_details.setPrice(0);
	     	results.add(item_details);
	     	flagclick = true;
			hacerp.setEnabled(false);
			deleteall.setEnabled(false);
	     	
     	}
 
 totalprice.setText(String.valueOf(total) + "Bs");
 return results;
}

public void MakeAppointment(String des){
	
	Intent pedirIntent = new Intent(Budget.this, SetDate.class);
	pedirIntent.putExtra("motivos", motivos);
	startActivity(pedirIntent);
	BudgetDB db = new BudgetDB(Budget.this);
  	db.open();
  	db.deleteAll();
  	db.close();
  	Refresh();
}
public void DeleteAll(){
	
	BudgetDB db = new BudgetDB(Budget.this);
	db.open();
	db.deleteAll();
	db.close();
	Refresh();
	totalprice.setText("0 Bs");
}

}
