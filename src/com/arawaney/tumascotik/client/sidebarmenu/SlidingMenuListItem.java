package com.arawaney.tumascotik.client.sidebarmenu;

import android.content.Intent;
import android.graphics.drawable.Drawable;

/**
 * @author Andrius Baruckis http://www.baruckis.com
 * 
 */
public class SlidingMenuListItem {
	public int id;
	public String name;
	public Drawable iconResource;
	public Intent intent ;

	public SlidingMenuListItem() {
	}

	public SlidingMenuListItem(int id, String name, Drawable iconResourceId, Intent intent) {
		this.id = id;
		this.name = name;
		this.iconResource = iconResourceId;
		this.intent = intent;
	}
}
