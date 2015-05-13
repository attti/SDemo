package android.schoology;

import android.content.Context;
import android.schoology.data.GradeData.GradeObject;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DemoItemView extends LinearLayout {

	TextView name;
	TextView score;
	ImageView thumbnail;
	
	public DemoItemView(Context context) {
		super(context);
		String inflatorservice = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(inflatorservice);
		layoutInflater.inflate(R.layout.item_view, this);

		name = (TextView) findViewById(R.id.name);
		score = (TextView) findViewById(R.id.score);
		thumbnail = (ImageView) findViewById(R.id.image);
	}
	
	

	public void setData(GradeObject grade) {
		name.setText(grade.student);
		score.setText(""+ grade.grade);
		Picasso.with(getContext()).load(grade.thumbnail).into(thumbnail);
	}
	
}
