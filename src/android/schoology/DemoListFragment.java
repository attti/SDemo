package android.schoology;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class DemoListFragment extends ListFragment {

	private SelectionListener listener;
	private BaseAdapter adapter;

	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setListShown(true);
		if (adapter != null) {
			setListAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
	}

	public void setAdapter(BaseAdapter adapter) {
		this.adapter = adapter;
		if (getView() != null) {
			getListView().setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
	}

	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		if (listener != null)
			listener.onSelected(position, v, l);
	}

	public void setSelectionListener(SelectionListener selectionListener) {
		listener = selectionListener;
	}

	public interface SelectionListener {
		public void onSelected(int pos, View v, ListView list);
	}

}
