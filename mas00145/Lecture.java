public class Lecture extends Classes {

	private String classStaff; 
	private String lectureSequence;

	public Lecture(String cName, String cType, String cSequence, String cDay, String cStart, String cEnd,
			String cWeekly, String cLocation, String cRoomSize, String cClassSize, String cStaff, String cDepartment) {

		super(cName, cType, cDay, cStart, cEnd, cWeekly, cLocation, cRoomSize, cClassSize, cDepartment);

		this.classStaff = cStaff;
		this.lectureSequence = cSequence;

	}

	@Override
	public String getEntry() {
		// TODO
		return getClassName() + "\t" + getClassType() + "\t" + getSequence() + "\t" + getClassDay() + "\t" + getClassStartTime() + 
				"\t" + getClassEndTime() + "\t" + getClassWeeklyPattern() + "\t" + getClassLocation() + "\t" + getClassRoomSize() + 
				"\t" + getClassSize() + "\t"+ getStaff() + "\t" + getClassDepartment();
	}

	public String getStaff() {
		return classStaff;
	}

	public void setClassStaff(String classStaff) {
		this.classStaff = classStaff;
	}

	public String getSequence() {
		return lectureSequence;
	}

	

}
