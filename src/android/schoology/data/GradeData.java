package android.schoology.data;

import java.util.ArrayList;

public class GradeData {
	
	public ArrayList<GradeObject> grades;
	
	public class GradeObject {
		public String student;
		public float grade;
		public String thumbnail;
	}
}
