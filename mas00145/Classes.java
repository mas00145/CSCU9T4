import java.time.*;

public abstract class Classes {

	private String className;
	private String classType;
	private String classDay;
	private String classStartTime;
	private String classEndTime;
	private String classWeeklyPattern;
	private String classLocation;
	private String classRoomSize;
	private String classSize;
	private String classDepartment;

	public Classes(String cName, String cType, String cDay, String cStart, String cEnd, String cWeekly,
			String cLocation, String cRoomSize, String cClassSize, String cDepartment) {

		this.className = cName;

		this.classDay = cDay;
		this.classType = cType;
		this.classStartTime = cStart;
		this.classEndTime = cEnd;
		this.classWeeklyPattern = cWeekly;
		this.classLocation = cLocation;
		this.classRoomSize = cRoomSize;
		this.classSize = cClassSize;
		this.classDepartment = cDepartment;

	}

	public abstract String getEntry();

	public abstract String getStaff();

	public abstract String getSequence();

	public String getClassName() {
		return className;
	}

	public String getClassType() {
		return classType;
	}

	public String getClassDay() {
		return classDay;
	}

	public String getClassStartTime() {
		return classStartTime;
	}

	// Used to order array before printing to HTML file
	public int getClassStartTimeHour() {
		return LocalTime.parse(classStartTime).getHour();
	}

	public int getClassStartTimeMinutes() {
		return LocalTime.parse(classStartTime).getMinute();
	}

	public String getClassEndTime() {
		return classEndTime;
	}

	public int getClassEndTimeHour() {
		return LocalTime.parse(classEndTime).getHour();
	}

	public int getClassEndTimeMinutes() {
		return LocalTime.parse(classEndTime).getMinute();
	}

	public String getClassWeeklyPattern() {
		return classWeeklyPattern;
	}

	public String getClassLocation() {
		return classLocation;
	}

	public String getClassRoomSize() {
		return classRoomSize;
	}

	public String getClassSize() {
		return classSize;
	}

	public String getClassDepartment() {
		return classDepartment;
	}

}
