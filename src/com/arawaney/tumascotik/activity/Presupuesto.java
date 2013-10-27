package com.arawaney.tumascotik.activity;

import java.util.ArrayList;
import java.util.Date;

import com.arawaney.tumascotik.adapter.BudgetItemListBaseAdapter;
import com.arawaney.tumascotik.db.BudgetDB;
import com.arawaney.tumascotik.dialog.PresupuestoAlertDialog;
import com.arawaney.tumascotik.dialog.PresupuestoDeleteallDialog;
import com.arawaney.tumascotik.dialog.PresupuestoDialog;
import com.arawaney.tumascotik.dialog.PresupuestoItemDialog;
import com.arawaney.tumascotik.model.BudgetItemDetails;

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
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class Presupuesto extends  FragmentActivity {
	Button volver;
	Button agregar;
	Button hacerp;
	Button deleteall;
	ListView listpresup;
	TextView totalprice;
	Boolean flagclick;
	int total;
	String motivos;
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_presupuesto);	
		volver = (Button) findViewById(R.id.bvolpresp);
		agregar = (Button) findViewById(R.id.bagregpresp);
		hacerp = (Button) findViewById(R.id.bhacerpedidopresp);
		deleteall = (Button) findViewById(R.id.bdeleteallpresp);
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
		volver.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			finish();	
			}
		});
		
		agregar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				BudgetDB db = new BudgetDB(Presupuesto.this);
				DialogFragment newFragment = new PresupuestoDialog(Presupuesto.this);
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
	
	void Refresh(){
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

void MakeAppointment(String des){
	
	Intent pedirIntent = new Intent(Presupuesto.this, SetDate.class);
	pedirIntent.putExtra("motivos", motivos);
	startActivity(pedirIntent);
	BudgetDB db = new BudgetDB(Presupuesto.this);
  	db.open();
  	db.deleteAll();
  	db.close();
  	Refresh();
}
void DeleteAll(){
	
	BudgetDB db = new BudgetDB(Presupuesto.this);
	db.open();
	db.deleteAll();
	db.close();
	Refresh();
	totalprice.setText("0 Bs");
}

}
