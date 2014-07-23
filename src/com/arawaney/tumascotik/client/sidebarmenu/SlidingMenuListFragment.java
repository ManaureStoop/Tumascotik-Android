package com.arawaney.tumascotik.client.sidebarmenu;

import java.util.ArrayList;
import java.util.List;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.ListView;

import com.arawaney.tumascotik.client.MainActivity;
import com.arawaney.tumascotik.client.R;
import com.arawaney.tumascotik.client.activity.AboutActivity;
import com.arawaney.tumascotik.client.activity.ClientPicker;
import com.arawaney.tumascotik.client.activity.PetPicker;
import com.arawaney.tumascotik.client.activity.UserInfoActivity;
import com.arawaney.tumascotik.client.control.MainController;
import com.arawaney.tumascotik.client.db.provider.UserProvider;
import com.arawaney.tumascotik.client.model.User;
import com.arawaney.tumascotik.client.sidebarmenu.SlidingMenuListAdapter;
import com.arawaney.tumascotik.client.sidebarmenu.SlidingMenuListItem;
import com.parse.GetDataCallback;

/**
 * @author Andrius Baruckis http://www.baruckis.com
 * 
 *         List fragment, which will be used as a content for sliding out menu.
 * 
 */
public class SlidingMenuListFragment extends ListFragment {
	protected List<SlidingMenuListItem> slidingMenuList;
	private SlidingMenuBuilderBase slidingMenuBuilderBase;
	Context context;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// We set here a custom layout which uses holo light theme colors.
		return inflater.inflate(R.layout.sliding_menu_holo_dark_list, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// We get a list from our specially created list data class.
		slidingMenuList = getSlidingMenuList(MainController.USER);
		if (slidingMenuList == null)
			return;

		// We pass our taken list to the adapter.
		SlidingMenuListAdapter adapter = new SlidingMenuListAdapter(
				getActivity(), R.layout.sliding_menu_holo_dark_list_row,
				slidingMenuList);
		setListAdapter(adapter);
	}

	// We could define item click actions here, but instead we want our builder
	// to be responsible for that.
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		l.setSelection(position);
		SlidingMenuListItem item = slidingMenuList.get(position);
		slidingMenuBuilderBase.onListItemClick(item);
	}

	// We can not provide a builder as an argument inside a fragment
	// constructor, so that is why we have separate method for that.
	public void setMenuBuilder(SlidingMenuBuilderBase slidingMenuBuilderBase) {
		this.slidingMenuBuilderBase = slidingMenuBuilderBase;
	}

	private List<SlidingMenuListItem> getSlidingMenuList(User user) {
		List<SlidingMenuListItem> menuList = new ArrayList<SlidingMenuListItem>();
		if (user != null) {
			if (user.getisAdmin() == User.IS_ADMIN) {
				Intent petIntent = new Intent(getActivity(), ClientPicker.class);
				SlidingMenuListItem myPetItem = new SlidingMenuListItem(1,
						getString(R.string.menu_my_clients),
						getResources().getDrawable(
								R.drawable.ic_users),
						petIntent);

				menuList.add(myPetItem);

			} else if (user.getisAdmin() == User.NOT_ADMIN) {
				
				Intent petIntent = new Intent(getActivity(), PetPicker.class);
				SlidingMenuListItem myPetItem = new SlidingMenuListItem(1,
						getString(R.string.menu_my_pets),
						getResources().getDrawable(
								R.drawable.ic_menu_pet),
						petIntent);

				menuList.add(myPetItem);
				
			}
			MainController.setCLIENTUSER(UserProvider.readMainUser(context));
			Intent profileIntent = new Intent(getActivity(),
					UserInfoActivity.class);
			SlidingMenuListItem myProfileItem = new SlidingMenuListItem(0,
					getString(R.string.menu_my_profile),
					getResources().getDrawable(
							R.drawable.ic_menu_user),
					profileIntent);
			menuList.add(myProfileItem);
			
			Intent aboutIntent = new Intent(getActivity(),
					AboutActivity.class);
			SlidingMenuListItem aboutItem = new SlidingMenuListItem(0,
					getString(R.string.about_view_about_menu_title),
					getResources().getDrawable(
							R.drawable.ic_about),
							aboutIntent);
			menuList.add(aboutItem);
			

		}else{
			
		}

		return menuList;
	}
}
