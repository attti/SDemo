package android.schoology;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Fragment;
import android.opengl.Visibility;
import android.os.Bundle;
import android.schoology.DemoListFragment.SelectionListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class DemoTimeDisplayFragment extends Fragment implements
		OnClickListener, SelectionListener {

	private static final String TIME_FORMAT = "dd MMM yyyy HH':'mm':'ss z";
	private static final int INTERVAL_TO_UPDATE = 1000;

	private Timer timer;
	private String[] zones;
	private DemoListFragment countryList;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_time_detail, null);
	}

	public void onViewCreated(final View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		updateTime(null);
		Button btn = (Button) view.findViewById(R.id.timezone);
		btn.setOnClickListener(this);
	}

	private void updateTime(final TimeZone timeZone) {
		if (timer != null)
			timer.cancel();
		timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				displayTime(timeZone);
			}
		}, 0, INTERVAL_TO_UPDATE);
	}

	synchronized private void displayTime(final TimeZone timeZone) {
		if (getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {
				public void run() {
					TextView time = (TextView) getView()
							.findViewById(R.id.time);
					time.setText("");
					Date now = new Date();
					SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT);
					if (timeZone != null)
						format.setTimeZone(timeZone);
					time.setText(format.format(now));
					Log.d("DTDF", format.format(now));
				}
			});
		}
	}

	public void onDetach() {
		if (timer != null)
			timer.cancel();
		countryList = null;
		super.onDetach();
	}
	
	private void showCountryList() {

		if (countryList == null) {
			zones = TimeZone.getAvailableIDs();
			countryList = new DemoListFragment();
			countryList.setAdapter(new ArrayAdapter<>(getActivity(),
					android.R.layout.simple_list_item_1, zones));
			countryList.setSelectionListener(this);
			getFragmentManager().beginTransaction()
					.add(R.id.container, countryList, "List").commit();

		}
	}
	
	public void onClick(View v) {
		if (v.getId() == R.id.timezone) {
			showCountryList();
			getView().findViewById(R.id.container).setVisibility(View.VISIBLE);
			getView().findViewById(R.id.container).bringToFront();
			getView().invalidate();
		}
	}
	
	@Override
	public void onSelected(int pos, View v, ListView list) {
		String zoneId = zones[pos];
		TimeZone timeZone = TimeZone.getTimeZone(zoneId);
		updateTime(timeZone);
		((TextView)getView().findViewById(R.id.loc)).setText(zoneId);
		getView().findViewById(R.id.container).setVisibility(View.GONE);
	}
	
	boolean onBackPressed() {
		View list = getView() == null ? null : getView().findViewById(R.id.container);
		if (list != null && list.getVisibility() == View.VISIBLE) {
			getView().findViewById(R.id.container).setVisibility(View.GONE);
			return true;
		}
		return false;
	}
}