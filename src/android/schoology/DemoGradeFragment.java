package android.schoology;

import android.app.Fragment;
import android.os.Bundle;
import android.schoology.DemoListFragment.SelectionListener;
import android.schoology.data.GradeData;
import android.schoology.network.NetworkManager;
import android.schoology.network.NetworkManager.RequestCallback;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

public class DemoGradeFragment extends Fragment implements SelectionListener {
	private DemoListFragment gradeOperationlist;
	private DemoListFragment gradeDetailList;
	
	private static final String LOAD_GRADES = "Load Grades";
	private static final String SHOW_GRADES = "Show Grades";
	
	private static final String[] title = {LOAD_GRADES};
	private static final String[] title_ = {LOAD_GRADES, SHOW_GRADES};

	private GradeData gradeData;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_grade, null);
	}
	
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		gradeOperationlist = new DemoListFragment();
		gradeOperationlist.setAdapter(new ArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, title));
		gradeOperationlist.setSelectionListener(this);
		getFragmentManager().beginTransaction().add(R.id.local_list, gradeOperationlist, "local list").commit();

		gradeDetailList = new DemoListFragment();

	}
	
	boolean onBackPressed() {
		if (gradeDetailList.isVisible()) {
			getFragmentManager().beginTransaction().replace(R.id.local_list, gradeOperationlist).commit();
			return true;
		}
		return false;
		
	}
	private void fetchJSON() {
		(new NetworkManager()).request(GradeData.class, new RequestCallback() {
			
			public void onRequest(String raw) {
				if (raw != null) {
					Log.d("DGF", raw);
					Gson gson = new Gson();
					DemoGradeFragment.this.gradeData = gson.fromJson(raw, GradeData.class);
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							gradeOperationlist.setAdapter(new ArrayAdapter(getActivity(),
									 android.R.layout.simple_list_item_1, title_));							
						}
					});
				}
			}
		});
	}
	
	public void onSelected(int pos, View v, ListView list) {
		switch (pos) {
		case 0:
			fetchJSON();
			((TextView) v).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.spinner_black_48, 0);
			break;
		case 1:
			gradeDetailList.setAdapter(new MyAdapter());
			getFragmentManager().beginTransaction().replace(R.id.local_list, gradeDetailList).commit();
			break;
		default:
			break;
		}
	}
	
	class MyAdapter extends BaseAdapter {

		public int getCount() {
			return (gradeData.grades != null) ? gradeData.grades.size() : 0;
		}

		public Object getItem(int pos) {
			return pos;
		}

		public long getItemId(int pos) {
			return pos;
		}

		public View getView(int pos, View v, ViewGroup parent) {
			if (v == null)
				v = new DemoItemView(getActivity());
			((DemoItemView)v).setData(gradeData.grades.get(pos));
			return v;
		}
		
	}
}
