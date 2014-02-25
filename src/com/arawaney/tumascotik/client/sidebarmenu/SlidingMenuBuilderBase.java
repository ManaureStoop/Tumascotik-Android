package com.arawaney.tumascotik.client.sidebarmenu;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.sax.StartElementListener;
import android.util.DisplayMetrics;
import android.widget.Toast;


import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.arawaney.tumascotik.client.R;
import com.arawaney.tumascotik.client.activity.PetPicker;
import com.arawaney.tumascotik.client.activity.UserInfoActivity;

/**
 * @author Andrius Baruckis http://www.baruckis.com
 * 
 *         This is base abstract builder class, which is responsible for
 *         creating sliding menu and implementing it's default list items click
 *         actions.
 * 
 */
public abstract class SlidingMenuBuilderBase {
	protected Activity activity;
	protected SlidingMenu menu = null;

	/**
	 * This method creates sliding out menu from the left screen side. It uses
	 * external "SlidingMenu" library for creation. When menu is attached to the
	 * activity, it places a list fragment inside the menu as it's content.
	 * 
	 * @param activity
	 *            This is Activity to which sliding menu is attached.
	 * 
	 */
	public void createSlidingMenu(Activity activity) {
		this.activity = activity;
		// For actual sliding menu creation we use an external open source
		// Android library called "SlidingMenu". It can be found at
		// "https://github.com/jfeinstein10/SlidingMenu".
		// We configure the SlidingMenu to our needs.
		 DisplayMetrics displaymetrics = new DisplayMetrics();
		   activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		
		menu = new SlidingMenu(activity);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.sliding_menu_shadow_width);
		menu.setShadowDrawable(R.drawable.sliding_menu_shadow);
		menu.setBehindOffsetRes(R.dimen.sliding_menu_offset);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(activity, SlidingMenu.SLIDING_WINDOW);
		menu.setBehindWidth((3*displaymetrics.widthPixels)/5);
		menu.setMenu(R.layout.sliding_menu_frame);

		SlidingMenuListFragment slidingMenuListFragment = new SlidingMenuListFragment();
		slidingMenuListFragment.setMenuBuilder(this);

		// We replace a FrameLayout, which is a content of sliding menu, with
		// created list fragment filled with data from menu builder.
		activity.getFragmentManager().beginTransaction()
				.replace(R.id.sliding_menu_frame, slidingMenuListFragment)
				.commit();
	}

	public SlidingMenu getSlidingMenu() {
		return menu;
	}

	// It is our base builder which can be extended, so we can define default
	// actions, which will be called when we press on separate list items.
	public void onListItemClick(SlidingMenuListItem selectedSlidingMenuListItem) {
		CharSequence text;
		switch (selectedSlidingMenuListItem.id) {
		case 0:
			UserInfoActivity.viewMode = UserInfoActivity.MODE_INFO_LIST;
			this.activity.startActivity(selectedSlidingMenuListItem.intent);
			break;
		case 1:
			PetPicker.functionMode = PetPicker.MODE_EDIT_PET;
			this.activity.startActivity(selectedSlidingMenuListItem.intent);
			break;
		default:
			text = "Clicked item. "
					+ "NOTHING CLICKED";
			Toast.makeText(activity, text, Toast.LENGTH_LONG).show();
			break;
		}
	}

}
