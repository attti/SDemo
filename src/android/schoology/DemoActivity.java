package android.schoology;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.schoology.DemoListFragment.SelectionListener;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DemoActivity extends Activity implements SelectionListener {

	final static String TIME_DISPLAY = "Demo 1: Time display";
	final static String JSON_FETCH = "Demo 2: JSON fetch";
	final static String[] demos = {TIME_DISPLAY, JSON_FETCH};
	
	private DemoListFragment mainListFragment;
	private DemoTimeDisplayFragment timeFragment;
	private DemoGradeFragment gradeFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_twopane);
	}

	@Override
	protected void onResume() {
		super.onResume();
		ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, demos);
		
		mainListFragment = new DemoListFragment();
		mainListFragment.setSelectionListener(this);
		mainListFragment.setAdapter(adapter);
		getFragmentManager().beginTransaction().add(R.id.item_list, mainListFragment, TIME_DISPLAY).commit();

		timeFragment = new DemoTimeDisplayFragment();
		gradeFragment = new DemoGradeFragment();

		onSelected(0, null, null);
	}
	@Override
	protected void onPause() {
		super.onPause();
		if (mainListFragment != null) getFragmentManager().beginTransaction().remove(mainListFragment).commit();
		if (gradeFragment != null) getFragmentManager().beginTransaction().remove(gradeFragment).commit();
		if (timeFragment != null) getFragmentManager().beginTransaction().remove(timeFragment).commit();

	}

	@Override
	public void onBackPressed() {
		if (timeFragment.isVisible() && !timeFragment.onBackPressed() || gradeFragment.isVisible() && !gradeFragment.onBackPressed())
			super.onBackPressed();
	}
	@Override
	public void onSelected(int pos, View v, ListView list) {
		Log.d("DA", "Position selected at " + pos);
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		int parentId = R.id.item_detail_container;
		switch (pos) {
		case 0:
			if (gradeFragment != null && gradeFragment.isVisible())
				transaction.replace(parentId, timeFragment, TIME_DISPLAY).commit();
			else if (!timeFragment.isVisible()) 
				transaction.add(parentId, timeFragment, TIME_DISPLAY).commit();
			break;
		case 1:
			if (timeFragment != null && timeFragment.isVisible())
				transaction.replace(parentId, gradeFragment, JSON_FETCH).commit();
			else if (!gradeFragment.isVisible())
				transaction.add(parentId, gradeFragment, JSON_FETCH).commit();
			break;
		default:
			break;
		}
	}
	
}
