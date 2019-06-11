import java.util.*;

public class ClassRecord implements Comparator<Classes>{

	private List<Classes> cl;
	

	public ClassRecord() {

		cl = new ArrayList<Classes>();

	}

	// add a record to the Classes list
	public void addEntry(Classes e) {
		cl.add(e);
	} // Classes to list

	public List<Classes> getClassesArray() {

		return returnAllClassesArrayList();
	}

	private List<Classes> returnAllClassesArrayList() {
		return this.cl;
	}

	public ArrayList<String> lookupEntry(String className) {
		ListIterator<Classes> iter = cl.listIterator();
		ArrayList<String> searchResults = new ArrayList<String>();
		String alertMessage = "";

		while (iter.hasNext()) {
			Classes current = iter.next();

			if (current.getClassName().toLowerCase().trim().contains(className.toLowerCase().trim())) {

				try {

					if (current.getClassType().toLowerCase().equals("cl")
							|| current.getClassType().toLowerCase().equals("p")) {

						if (classSizeExceedsRoomSize(Integer.parseInt(current.getClassRoomSize()),
								Integer.parseInt(current.getClassSize()))) {

							alertMessage = "ClassSize > RoomSize";
							searchResults.add(alertMessage + "\t" + current.getEntry().toLowerCase());

							continue;
						} else {
							alertMessage = "";
							searchResults.add(alertMessage + "\t" + current.getEntry().toLowerCase());
							continue;
						}
					} else if (current.getClassType().toLowerCase().equals("s")) {

						if (classSizeLessThanHalfOfRoomSize(Integer.parseInt(current.getClassRoomSize()),
								Integer.parseInt(current.getClassSize()))) {
							alertMessage = "ClassSize < Half of RoomSize";
							searchResults.add(alertMessage + "\t" + current.getEntry().toLowerCase());
							continue;
						}

						if (classSizeMoreThanTenPercentOfRoomSize(Integer.parseInt(current.getClassRoomSize()),
								Integer.parseInt(current.getClassSize()))) {

							alertMessage = "ClassSize > 10% of RoomSize";
							searchResults.add(alertMessage + "\t" + current.getEntry().toLowerCase());
							continue;
						} else {
							alertMessage = "";
							searchResults.add(alertMessage + "\t" + current.getEntry().toLowerCase());
							continue;
						}
					}

				} catch (Exception e) {

					continue;
				}

				if (current.getClassType().toLowerCase().equals("l")) {
					alertMessage = "";
					searchResults.add(alertMessage + "\t" + current.getEntry().toUpperCase());
				}

			}

		}

		return searchResults;

	} // lookupEntry

	/**
	 * @param className - the list of modules to search for 
	 * @return ArrayList<Classes> - the array of classes found, returned as an ArrayList of Class objects
	 */
	public ArrayList<Classes> findExactMatches(String className) {
		ListIterator<Classes> iter = cl.listIterator();
		ArrayList<Classes> startTimeOrder = new ArrayList<Classes>();
		startTimeOrder.clear();
		
		
		String[] s = className.split(",");

		for (int i = 0; i < s.length; i++) {

			while (iter.hasNext()) {
				Classes current = iter.next();

				if (current.getClassName().toLowerCase().trim().equals(s[i].toLowerCase().trim())) {

					startTimeOrder.add(current);
				}			
			}
			iter = cl.listIterator();
		}
		
		// Sort on start time, Hour - to make it easier to uild the timetable for HTML file
		Collections.sort(startTimeOrder, new ClassRecord());
		
		return startTimeOrder;

	} // lookupEntry

	/**
	 * @param name
	 * @param day
	 * @param start
	 * @param end
	 * @param weekly
	 * @return boolean - if class entry already exists return true
	 * A unique entry is defined by: Name, Day, Start Time, End Time and Weekly Pattern
	 */
	public boolean doesEntryExist(String name, String day, String start, String end, String weekly) {
		ListIterator<Classes> iter = cl.listIterator();

		while (iter.hasNext()) {
			Classes current = iter.next();
			// TODO error with current.getName()

			if (current.getClassName().trim().toLowerCase().equals(name.toLowerCase().trim())
					&& current.getClassDay().toLowerCase().trim().equals(day.toLowerCase().trim())
					&& current.getClassStartTime().trim().equals(start.trim())
					&& current.getClassEndTime().trim().equals(end.trim())
					&& current.getClassWeeklyPattern().trim().equals(weekly.trim()))

				return true;
		}

		return false;

	} // doesEntryExist including class name

	/**
	 * @param roomSize
	 * @param classSize
	 * @return boolean - if class size exceeds room size return true
	 */
	private boolean classSizeExceedsRoomSize(int roomSize, int classSize) {

		if (classSize > roomSize) {
			return true;
		}

		return false;

	}

	/**
	 * @param roomSize
	 * @param classSize
	 * @return boolean - true if class size is less than half of the room size
	 */
	private boolean classSizeLessThanHalfOfRoomSize(int roomSize, int classSize) {

		if (classSize < Math.ceil((float) roomSize * 0.5)) {
			return true;
		}

		return false;

	}

	/**
	 * @param roomSize
	 * @param classSize
	 * @return boolean - true is class size is greater than 10% of room size
	 */
	private boolean classSizeMoreThanTenPercentOfRoomSize(int roomSize, int classSize) {

		if (classSize > Math.ceil((float) roomSize * 1.1)) {
			return true;
		}

		return false;

	}

	
	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 * Custom compare method to sort ArrayList of Class objects by start Hour, ascending order
	 */
	@Override
	public int compare(Classes a, Classes b) {
		
		return a.getClassStartTimeHour() - b.getClassStartTimeHour();
		
	}

}
