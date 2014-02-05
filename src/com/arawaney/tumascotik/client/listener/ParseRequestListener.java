package com.arawaney.tumascotik.client.listener;

import java.util.ArrayList;
import java.util.Date;

import com.arawaney.tumascotik.client.model.Request;




public interface ParseRequestListener {
	
	public void OnRequestInserted(boolean inserted, String systemId);

	public void OnAllRequestsQueryFinished(ArrayList<Request> requests);

	public void onRequestQueryFInished(Request request);
	
	public void onCanceledQueryFinished(boolean canceled);

	public void onRequestRemoveFinished(Request request);
	
	public void onDayRequestsQueryFinished(Date[] initialScheduledDates, Date[] finalScheduledDates);
	

}


