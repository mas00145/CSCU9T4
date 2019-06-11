import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author mas00145
 *
 */
public class FileMethods {

	private final String CLASS_TYPES_ALLOWED = "L S CL";
	private final int START_WEEK = 1;
	private final int END_WEEK = 52;

	private ArrayList<String> formatted = new ArrayList<String>();
	private ArrayList<String> invalidInput = new ArrayList<String>();
	private ArrayList<String> duplicateEntries = new ArrayList<String>();
	private ArrayList<String> multipleNamesOrSequences = new ArrayList<String>();
	private ArrayList<String> finalDatabase = new ArrayList<String>();

	private TreeSet<String> checkDuplicates = new TreeSet<String>();

	private String fileHeader = "";
	private boolean multiLineClass = false;
	private boolean multiSequence = false;

	// first letter of possible class types (e.g. L CL S P)
	private Pattern classTypePattern = Pattern.compile("^[LCSP]");

	// pattern to match Weekly Pattern for a single day (e.g. dd-MMM [such as
	// 01-Jan])
	private Pattern singleDayPattern = Pattern.compile("^(([0-9])|([0-2][0-9])|([3][0-1]))\\-[a-zA-Z]{3}$");

	// check string for location - should be A.AN or .A... (i.e. a Letter (A) or "."
	// followed by
	// a '.' and then a string of Alphanumeric chars (AN)
	private Pattern locationPattern = Pattern.compile("[A-Za-z\\.]+[a-zA-Z0-9)&//s]+$");

	// Only allow a single letter
	private Pattern sequenceUserInputPatternLetters = Pattern.compile("^[a-zA-z]{1}$");
	// Only allow a digit between 1 and 99
	private Pattern sequenceUserInputPatternNums = Pattern.compile("^(99|[1-9][0-9]?)$");

	public void printUsageMessage() {
		System.out.println("Enter an input file name!");
		System.exit(0);
	}

	public String getFileHeader() {
		return fileHeader;
	}

	/**
	 * @param fileHeader
	 */
	public void setFileHeader(String fileHeader) {
		this.fileHeader = fileHeader;
	}

	/**
	 * @param outFile
	 * @return
	 */
	public String getOutputFileName(String outFile) {

		int i = 0;
		if (outFile.contains(".")) {
			while (Character.isLetterOrDigit(outFile.charAt(i))) {
				i++;
			}
			return outFile.substring(0, i);
		} else {
			return outFile;
		}

	}

	/**
	 * @param fileName
	 * @return
	 */
	public ArrayList<String> getFormattedLine(String fileName) {

		Scanner in = new Scanner(fileName);
		Scanner inFile = checkInputFile(in, fileName);
		PrintWriter printer = checkOutPutFile(in, fileName);
		in.close();

		while (inFile.hasNextLine()) {

			String s = inFile.nextLine();
			String line = s;
			s = splitAndFormatClassRecord(s);
			if (s.equals("INVALID INPUT")) {
				if (line.trim().equals("")) {
					// blank line so don't add to errorLog
				} else {
					System.out.println("ADDING TO ERROR LOG: " + s);
					invalidInput.add(line);
				}

			} else if (s.equals("")) {
				// empty line so no need to add to arraylist

			} else if (s.equals("DUPLICATE")) {
				duplicateEntries.add(line);
				// duplicateRecord = false;
			} else if (multiLineClass || multiSequence) { // if we've split based on multiple class or sequences
				formatted.addAll(multipleNamesOrSequences);
				multipleNamesOrSequences.clear();
				multiLineClass = false;
				multiSequence = false;
			} else {
				formatted.add(s);
			}

		}

		for (String s : formatted) {

			/*
			 * To check for duplicated we add to a TreeSet if the return value is not true,
			 * then its a duplicate
			 */
			if (!checkDuplicates.add(checkForDuplicateRecords(s))) {
				duplicateEntries.add(s);
			} else {

				if (checkRecordFormat(s)) {
					finalDatabase.add(s); // add to the valid records ArrayList
				} else {
					invalidInput.add(s); // add to the errors ArrayList
				}

			}

		}

		Collections.sort(duplicateEntries);

		printer.close();
		inFile.close();

		return finalDatabase;

	}

	/**
	 * @param list
	 * @return
	 */
	public ArrayList<String> sortAscending(ArrayList<String> list) {
		Collections.sort(list);
		return list;
	}

	/**
	 * @param list
	 * @return
	 */
	public ArrayList<String> sortDescending(ArrayList<String> list) {
		Collections.sort(list, Collections.reverseOrder());
		return list;
	}

	/**
	 * @param in
	 * @param f
	 * @return
	 */
	private Scanner checkInputFile(Scanner in, String f) {

		Scanner inFile = null;

		try {
			File inputFile = new File(f);
			inFile = new Scanner(inputFile);
		} catch (IOException e) {
			System.err.println("\nIOException: " + e.getMessage() + " not found");
			System.out.println("Check input file and try again... ");
			System.exit(0);
		}

		return inFile;
	}

	/**
	 * @return ArrayList - the errorlog 
	 */
	public ArrayList<String> getErrorLog() {

		return invalidInput;
	}

	/**
	 * @param in
	 *            - Scanner object
	 * @param outputFile
	 *            - file to output to
	 * @return PrintWriter object is returned if file is openable
	 */
	private PrintWriter checkOutPutFile(Scanner in, String outputFile) {

		PrintWriter printer = null;

		outputFile = getOutputFileName(outputFile);
		// System.out.println("Output file: " + outputFile);
		outputFile = outputFile + ".html";
		// System.out.println("Output file: " + outputFile);

		try {
			printer = new PrintWriter(outputFile);

		} catch (FileNotFoundException e) {
			System.err.println("FileNotFoundException: " + e.getMessage() + " not openable\n");

		}

		return printer;
	}

	/**
	 * @param s
	 *            - a String record consisting of exactly 10 tab-separated fields
	 * @return String - either the formatted classRec or "INVALID INPUT"
	 */
	private String splitAndFormatClassRecord(String s) {

		String[] splitString = s.split("\t"); // split into tokens on tabs
		String[] splitNamesAndType = splitString[0].split("\\."); // split the field on '.'
																	
		String classRec = "";

		classRec = splitNamesAndType[0].trim().replaceAll("^[^a-zA-Z0-9\\s]+|[^a-zA-Z0-9)\\s]+$", "") + "\t";

		String classType = "";
		String classSequence = "";

		if (splitNamesAndType.length > 1) {

			if (splitNamesAndType[1].contains(".")) { 

				int k = 0;

				char[] chars = splitNamesAndType[1].trim().toCharArray();
				

				for (char c : chars) {
					if (Character.isLetterOrDigit(c) || Character.isWhitespace(c)) {
						k++;
					} else {
						break;
					}

				}

				splitNamesAndType[1] = splitNamesAndType[1].substring(k).trim();

			}

			if (classTypePattern.matcher(splitNamesAndType[1]).find()) {
				int k = 0;

				char[] chars = splitNamesAndType[1].toCharArray();

				for (char c : chars) {
					if (Character.isLetter(c)) {
						k++;
					} else {
						break;
					}
				}

				classType = splitNamesAndType[1].replaceAll("^[^a-zA-Z0-9]+|[^a-zA-Z0-9]+$", "").substring(0, k);

				classSequence = splitNamesAndType[1].replaceAll("^[^a-zA-Z0-9\\s]+|[^a-zA-Z0-9\\s]+$", "").substring(k);
				classSequence = classSequence.replaceAll("^[\\/(\\s]", "");
				classSequence = classSequence.replaceAll("[\\/\\s]", " ");

			}
		} else if (s.contains("Name")) {
			setFileHeader(s);
			return "";
		}

		else {
			
			return "INVALID INPUT";
		}

		String shortDay = "";
		String startOfDayString = "";
		if (!classSequence.equals("")) {
			int k = 0;

			char[] chars = classSequence.toCharArray();

			for (char c : chars) {
				if (Character.isLetter(c)) {
					k++;
				} else {
					break;
				}
			}

			// Remove the not-needed string at start of sequence
			// i.e. remove Th/01 Thursday - remove the Th part and to return 01 Thursday
			shortDay = classSequence.substring(0, k).toLowerCase().trim();
			startOfDayString = splitString[1].substring(0, k).toLowerCase().trim();

			if (shortDay.equals(startOfDayString) && !shortDay.isEmpty()) {

				classSequence = classSequence.substring(k).trim();
			}
		}

		classRec += classType + "\t";
		classRec += classSequence + "\t";

		for (int i = 1; i < splitString.length; i++) {
			if (splitString.length < 10) {
				return "INVALID INPUT"; // for incorrectly formatted input lines add
			} else {

				splitString[i] = splitString[i].replaceAll("^[^a-zA-Z0-9.]+|[^a-zA-Z0-9)]+$", "").trim();
				classRec += splitString[i] + "\t";
			}
		}

		// Remove any trailing tabs from the string
		classRec = classRec.substring(0, classRec.length() - 1);

		// Check for multiple names and split into seperate records
		if (splitNamesAndType[0].contains("/")) {
			splitAndFormatClassNames(classRec);
		}

		// check for multiple sequences, and split into seperate records, one per line
		if (classSequence.contains(" ")) {
			if (isAlpha(classSequence.replaceAll(" ", ""))) {
				splitAndFormatSequence(classRec);
			}

		}

		return classRec;

	}

	/**
	 * @param String
	 *            s - the multiple names of the classes
	 */
	private void splitAndFormatClassNames(String s) {

		int i = 0, j = 0;

		String classRec = "";

		String[] splitRecord = s.split("\t");

		// now split the multi-name into separate ones
		String[] eachName = splitRecord[0].split("/");

		for (i = 0; i < eachName.length; i++) {

			classRec = eachName[i] + "\t";

			for (j = 1; j < splitRecord.length; j++) {

				classRec += splitRecord[j] + "\t";
			}
			classRec = classRec.substring(0, classRec.length() - 1);
			multipleNamesOrSequences.add(classRec);

		}

		multiLineClass = true;

	}

	/**
	 * @param String
	 *            s - the multiple sequences
	 */
	private void splitAndFormatSequence(String s) {

		int i = 0, j = 0;

		String classRec = "";

		String[] splitRecord = s.split("\t");

		// now split the multi-sequence into separate ones
		String[] eachSequence = splitRecord[2].split(" ");

		classRec = splitRecord[0] + "\t" + splitRecord[1] + "\t";

		for (i = 0; i < eachSequence.length; i++) {

			classRec += eachSequence[i] + "\t";

			for (j = 3; j < splitRecord.length; j++) {

				classRec += splitRecord[j] + "\t";
			}
			classRec = classRec.substring(0, classRec.length() - 1);
			multipleNamesOrSequences.add(classRec);

			classRec = splitRecord[0] + "\t" + splitRecord[1] + "\t";
		}

		multiSequence = true;
	}

	/**
	 * @param String
	 *            name
	 * @return true if all are letters false if not all letters
	 */
	private boolean isAlpha(String name) {
		char[] chars = name.toCharArray();

		for (char c : chars) {
			if (!Character.isLetter(c)) {
				return false;
			}
		}

		return true;
	}
	
	/**
	 * @param moduleList
	 * @return
	 */
	public boolean checkModulesForHTMLOutput(String moduleList) {
		
		if (moduleList.isEmpty())
		{
			return false;
		}
		
		char[] chars = moduleList.toCharArray();
		
		for (char c: chars) {
			if (!Character.isLetterOrDigit(c) && c != ',' ) {
				return false;
			}
			
		}
		
		if(moduleList.contains(",")) {
			String[] s = moduleList.split(",");
			
			if (s.length > 3) {
				return false; // only allow a max of 3 modules for timetable
			}
		}
		
		return true;
		
		
	}

	/**************************************************
	 * START: Checker methods for each field of input *
	 **************************************************/

	/**
	 * @param finalRecord
	 * @return
	 */
	private boolean checkRecordFormat(String finalRecord) {

		// System.out.println(finalRecord); TODO

		String[] checkRecord = finalRecord.split("\t");
		boolean names = false, types = false, sequ = false, days = false, classTimes = false, weeklyP = false,
				loc = false, rmSize = false, clSize = false, staff = false, dept = false;

		for (int i = 0; i < checkRecord.length; i++) {

			names = checkClassName(checkRecord[0]);
			types = checkClassType(checkRecord[1]);
			sequ = checkClassSequence(checkRecord[2]);
			days = checkClassDay(checkRecord[3]);
			classTimes = checkClassTimes(checkRecord[4], checkRecord[5]);

			if (singleDayPattern.matcher(checkRecord[6]).find()) {
				weeklyP = checkClassWeeklyPatternForSingleDate(checkRecord[6]);
			} else {
				weeklyP = checkClassWeeklyPattern(checkRecord[6]);
			}

			loc = checkFormatOfLocation(checkRecord[7]);
			rmSize = checkRoomSize(checkRecord[8]);
			clSize = checkClassSize(checkRecord[9]);

			staff = checkStaffField(checkRecord[10]);
			dept = checkDepartmentField(checkRecord[11]);

		}

		// If any of the fields returns "false" then the record read from the file is
		// not valid
		if (!names || !types || !sequ || !days || !classTimes || !weeklyP || !loc || !rmSize || !clSize || !staff
				|| !dept) {

			return false;
		}

		return true;

	}

	/**
	 * @param name
	 * @return
	 */
	public boolean checkClassName(String name) {

		if (name.equals("")) {
			return false;
		}

		char[] chars = name.toCharArray();

		for (char c : chars) {
			if (!Character.isLetterOrDigit(c) && !Character.isWhitespace(c) && c != '_' && c != '-') {
				return false;
			}

		}

		return true;
	}

	/**
	 * @param classType
	 * @return
	 */
	public boolean checkClassType(String classType) {

		if (classType.equals("")) {
			return false;
		}

		if (classType.length() >= 1 && classType.length() <= 2) {
			if (CLASS_TYPES_ALLOWED.toUpperCase().contains(classType.toUpperCase())) {
				return true;
			}

		}

		return false;

	}

	/**
	 * @param seq
	 * @return
	 */
	public boolean checkClassSequence(String seq) {

		seq = seq.trim();

		if (seq.equals("")) {
			return true;
		}

		char[] chars = seq.toCharArray();

		for (char c : chars) {
			if (!Character.isLetterOrDigit(c) && !Character.isWhitespace(c)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * @param classDay
	 * @return
	 */
	public boolean checkClassDay(String classDay) {

		if (classDay.isEmpty()) {
			return false;
		}

		try {
			DayOfWeek.valueOf(classDay.toUpperCase());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * @param seq
	 *            String containing sequence digit or alpha string
	 * @return true if valid sequence and false otherwise this method is for
	 *         checking user input via the GUI
	 */
	public boolean checkClassSequenceUserInput(String seq) {

		seq = seq.trim();

		if (seq.equals("")) {
			return true;
		}

		if (!sequenceUserInputPatternLetters.matcher(seq).find() && !sequenceUserInputPatternNums.matcher(seq).find()) {
			return false;
		}


		return true;
	}

	/**
	 * @param start
	 * @param end
	 * @return
	 */
	public boolean checkClassTimes(String start, String end) {

		if (start.isEmpty() || end.isEmpty()) {
			return false;
		}
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("HH:mm");

		try {
			//
			if (LocalTime.parse(start, dateFormat).isBefore(LocalTime.parse(end, dateFormat))) {
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			return false;
		}

	}

	/**
	 * @param s
	 *            String to check for date patter of NN-AAA
	 * @return true if matches false otherwise
	 */
	public boolean singleDayPattern(String s) {

		if (s.isEmpty()) {
			return false;
		}

		if (singleDayPattern.matcher(s).find()) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * @param weeklyPatternSingleDay
	 * @return
	 */
	public boolean checkClassWeeklyPatternForSingleDate(String weeklyPatternSingleDay) {

		if (weeklyPatternSingleDay.isEmpty()) {
			return false;
		}
		int month = -1;
		int year = -1;

		if (!singleDayPattern.matcher(weeklyPatternSingleDay).find()) {
			return false;
		}

		String[] splitdate = weeklyPatternSingleDay.split("-");

		try {
			Date date = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(splitdate[1].toUpperCase());
			Calendar cal = Calendar.getInstance();
			year = cal.get(Calendar.YEAR);
			cal.setTime(date);
			month = cal.get(Calendar.MONTH);
			LocalDateTime.of(year, (month + 1), Integer.parseInt(splitdate[0]), 0, 0);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	/**
	 * @param weeklyPattern
	 * @return boolean true if weekly patter conformas to requirements false otherwise
	 */
	public boolean checkClassWeeklyPattern(String weeklyPattern) {

		if (weeklyPattern.isEmpty()) {
			return false;
		}
		

		String[] splitCommas = null;
		String[] splitHyphen = null;
		boolean weekRangeEntered = false;
		int previousWeek = 0;

		boolean weeklyBoolean = checkFormatOfweeklyPattern(weeklyPattern);

		if (weeklyBoolean) {
			weeklyPattern = weeklyPattern.replaceAll("\\s+", "");
			if (weeklyPattern.contains(",")) {
				splitCommas = weeklyPattern.split(",");

				for (int i = 0; i < splitCommas.length; i++) {

					if (splitCommas[i].contains("-")) {

						splitHyphen = splitCommas[i].split("-");

						if (Integer.parseInt(splitHyphen[0]) < START_WEEK
								|| Integer.parseInt(splitHyphen[1]) > END_WEEK) {

							return false;
						}

						if (Integer.parseInt(splitHyphen[0]) > Integer.parseInt(splitHyphen[1])) {

							return false;
						}

						weekRangeEntered = true;
						previousWeek = Integer.parseInt(splitHyphen[1]);

					} else {
						if (Integer.parseInt(splitCommas[i]) < START_WEEK
								|| Integer.parseInt(splitCommas[i]) > END_WEEK) {
							return false;
						}

						if (weekRangeEntered) {

							if ((Integer.parseInt(splitCommas[i]) <= previousWeek)) {
								return false;
							}
						}

					}
				}
			}
			return weeklyBoolean;
		}
		return false;

	}

	/**
	 * @param weeklyPattern
	 * @return
	 */
	private boolean checkFormatOfweeklyPattern(String weeklyPattern) {

		if (weeklyPattern.isEmpty()) {
			return false;
		}


		if (!Character.isDigit(weeklyPattern.charAt(0))
				|| !Character.isDigit(weeklyPattern.charAt(weeklyPattern.length() - 1))) {
			return false;

		}

		if (weeklyPattern.contains("\\s")) {
			if (!weeklyPattern.contains(",")) // if we have spaces, we also need commas
												
			{
				return false;
			}
		}

		for (int i = 0; i < weeklyPattern.length(); i++) {

			if (Character.isDigit(weeklyPattern.charAt(i))) {

				;

			} else if (!Character.isDigit(weeklyPattern.charAt(i))) {

				if (weeklyPattern.charAt(i) == '-') {

					if (!Character.isDigit(weeklyPattern.charAt(i + 1))) {
						return false;
					}
				}

				if (weeklyPattern.charAt(i) == ',') {

					if (weeklyPattern.charAt(i + 1) != ' ') {
						return false;
					}
					if (!Character.isDigit(weeklyPattern.charAt(i + 2))) {
						return false;
					}
				}

			}
		}

		return true;
	}

	/**
	 * @param location
	 * @return
	 */
	public boolean checkFormatOfLocation(String location) {

		if (location.isEmpty()) {
			return false;
		}

		// remove unnecessary spaces
		location = location.replaceAll("\\s+", " ").trim();

		// Check that location starts with a letter or a "." and ends
		// with a letter or a digit
		if ((!Character.isLetter(location.charAt(0)) && location.charAt(0) != '.')
				|| (!Character.isLetterOrDigit(location.charAt(location.length() - 1))
						&& location.charAt(location.length() - 1) != ')')) {

			return false;
		}

		// do not allow more than one location, if the location name contains a * - not
		// valid input. add to the errors list
		if (location.contains("*")) {

			return false;

		}

		if (!locationPattern.matcher(location).find()) {

			return false;

		}

		return true;
	}

	/**
	 * @param roomSize
	 * @return
	 */
	public boolean checkRoomSize(String roomSize) {

		if (roomSize.isEmpty()) {
			return false;
		}
		String[] multipleRooms = null;

		if (!Character.isDigit(roomSize.charAt(0)) && !Character.isDigit(roomSize.charAt(roomSize.length() - 1))) {
			return false;
		}

		if (roomSize.contains("*")) {

			multipleRooms = roomSize.split("\\*");

			if (multipleRooms.length != 2) { // only allow 2 roomSizes at most
				return false;
			}

			for (int i = 0; i < multipleRooms.length; i++) {

				try {
					if (Integer.parseInt(multipleRooms[i]) < 0 || !Character.isDigit(multipleRooms[i].charAt(0))
							|| !Character.isDigit(multipleRooms[i].charAt(multipleRooms[i].length() - 1))) {
						return false;
					}

				} catch (Exception e) {
					return false;
				}

			}
			return true;
		} else {

			try {
				if (Integer.parseInt(roomSize) > 0) {
					return true;
				}
				return false;

			} catch (Exception e) {
				return false;
			}
		}
	}

	/**
	 * @param classSize
	 * @return
	 */
	public boolean checkClassSize(String classSize) {

		
		if (classSize.isEmpty()) {
			return false;
		} else {

			try {
				if (Integer.parseInt(classSize) > -1) {
					
					return true;
				} else {
					return false;
				}

			} catch (Exception e) {
				return false;
			}

		}
	}

	/**
	 * @param staff
	 * @return
	 */
	public boolean checkStaffField(String staff) {

		boolean multipleNames = false;
		staff = staff.trim();
		String[] multipleStaff = null;
		String[] firstAndLastName = null;

		if (staff.equals("")) {
			return true;
		}

		if (!Character.isLetter(staff.charAt(0)) || !Character.isLetter(staff.charAt(staff.length() - 1))) {
			return false;
		}

		if (staff.contains("/")) {

			multipleStaff = staff.split("/");
			multipleNames = true;

			for (int i = 0; i < multipleStaff.length; i++) {

				multipleStaff[i] = multipleStaff[i].trim();

				if (multipleStaff[i].equals("")) {
					return false;
				}
				if (!Character.isLetter(multipleStaff[i].charAt(0))
						|| !Character.isLetter(multipleStaff[i].charAt(multipleStaff[i].length() - 1))) {
					return false;
				}
			}
		}

		if (multipleNames) {

			for (int i = 0; i < multipleStaff.length; i++) {

				multipleStaff[i] = multipleStaff[i].trim();

				firstAndLastName = multipleStaff[i].split(" ");

				if (firstAndLastName.length < 2) { // must have at least first
													// and last name
					return false;
				}

				// remove any extra spaces in the names
				List<String> list = new ArrayList<String>(Arrays.asList(firstAndLastName));
				list.removeAll(Arrays.asList("", null));
				firstAndLastName = list.toArray(new String[0]);

				for (int j = 0; j < firstAndLastName.length; j++) {

					firstAndLastName[j] = firstAndLastName[j].trim();

					if (!Character.isLetter(firstAndLastName[j].charAt(0))
							|| !Character.isLetter(firstAndLastName[j].charAt(firstAndLastName[j].length() - 1))) {
						return false;
					} else {
						char[] chars = firstAndLastName[j].toCharArray();

						for (char c : chars) {
							if (!Character.isLetter(c) && c != '-') {
								return false;
							}
						}

					}
				}

			}
		}

		return true;

	}

	/**
	 * @param dept
	 * @return
	 */
	public boolean checkDepartmentField(String dept) {

		if (dept.isEmpty()) {
			return false;
		}

		dept = dept.trim();

		char[] chars = dept.toCharArray();

		int i = 0;
		for (char c : chars) {
			if (Character.isDigit(c)) {
				return false;
			}
			if (Character.isLetter(c) || Character.isWhitespace(c) || dept.charAt(i) == '_' || dept.charAt(i) == '/') {
				; // do nothing
			} else {
				return false; // for any char not a letter or ' ' _ or /
			}

			i++;

		}

		return true;

	}

	/**************************************************
	 * END: Checker methods for each field of input *
	 **************************************************/

	/**************************************************
	 * START: Checker methods for duplicates *
	 **************************************************/
	/**
	 * @param String
	 *            classRec
	 * @return String, containing the fields that are defined as unique for each
	 *         record
	 * 
	 *         This method is separated because if in the future we decide to
	 *         redefine what is unique this can be changed in one place, without
	 *         impacting the rest of the code
	 */
	public String checkForDuplicateRecords(String classRec) {

		String[] duplicateCheck = classRec.split("\t");
		String s = duplicateCheck[0] + "\t" + duplicateCheck[3] + "\t" + duplicateCheck[4] + "\t" + duplicateCheck[5]
				+ "\t" + duplicateCheck[6];

		return s;
	}

	public ArrayList<String> getCheckDuplicates() {
		return this.duplicateEntries;
	}
	/**************************************************
	 * END: Checker methods for duplicates *
	 **************************************************/

}
