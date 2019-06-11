import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.List;
import javax.swing.*;
import java.text.SimpleDateFormat;


public class ClassRecordGUI {

	private static final int HTML_ROWS = 10;
	private static final int HTML_COLS = 6;

	private static FileMethods f = new FileMethods();

	private static String outputFileName = "";
	private static String inputFileName = "";

	private static CreatePanels classes;
	private static CreatePanels searching;

	private static ClassRecord myClasses;

	private static ArrayList<String> formattedFile = new ArrayList<String>();
	private static ArrayList<String> errorLog = new ArrayList<String>();
	private static ArrayList<String> duplicatesList = new ArrayList<String>();

	// htmlTimeTable ia an rray of arraylist with 10 rows.
	// The number of columns is set in the constructor
	// htmlTimeTable[i].size() will give the size of the i'th row
	@SuppressWarnings("unchecked")
	private static ArrayList<String>[] htmlTimeTable = new ArrayList[HTML_ROWS];

	public static void main(String[] args) {

		if (args.length < 1) {
			f.printUsageMessage();
		}

		inputFileName = args[0];

		// get output file name ready - remove the file extension if it exists
		outputFileName = f.getOutputFileName(args[0]);

		initialiseHTMLArrayList();

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowClassesGUI();
			}
		});
	}

	/**
	 * @param fileName
	 */
	private static void createAndShowClassesGUI() {

		JTabbedPane tabs;

		classes = new CreatePanels("CLASSES");
		searching = new CreatePanels("SEARCH");

		myClasses = new ClassRecord();

		JFrame.setDefaultLookAndFeelDecorated(true);

		// Create and set up the window.
		JFrame applic = new JFrame("Class Records");
		applic.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		tabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);

		// Adding panels to JTabbedPane
		tabs.addTab("Module Add", classes);
		tabs.addTab("Search & Save", searching);

		applic.add(tabs);

		/****************************************************
		 * START: classes panel action listeners *
		 ****************************************************/

		classes.addRecord(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				String name = "", type = "", sequ = "", day = "", start = "", end = "", weekly = "", location = "",
						roomSize = "", classSize = "", staff = "", department = "";

				/*
				 * Check class name
				 */
				if (!f.checkClassName(classes.getClassName())) {
					classes.setTxtOutput("ENTER A VALID CLASS NAME!\n"
							+ "\t1/ Name must NOT include any spaces, or special characters.\n"
							+ "\t2/ Name can only include alpha numeric characters, and must start with a letter\n"
							+ "\te.g. ACCP002, AQUU3AE");
					return;
				} else {

					name = classes.getClassName().toUpperCase().trim();
				}

				/*
				 * Check class type
				 */
				if (classes.getClassType().equals("-")) {
					classes.setTxtOutput("SELECT A VALID TYPE FROM DROP DOWN LIST!\n" + "\t1/ (L) for Lectures\n"
							+ "\t2/(CL) for Computer Labs\n" + "\t3/(S) for Seminars");
					return;
				} else {

					type = classes.getClassType().toUpperCase().trim();
				}

				/*
				 * Check class sequence
				 */
				if (!f.checkClassSequenceUserInput(classes.getClassSequence().replaceFirst("^0+(?!$)", ""))) {
					classes.setTxtOutput("ENTER A VALID SEQUENCE!\n" + "\t1/ This can be left blank, or\n"
							+ "\t2/ This can only be a single digit between [1 - 99]\n"
							+ "\t3/ Or a letter between [a-z][A-Z]");
					return;

				} else {

					if (classes.getClassSequence().isEmpty()) {

						sequ = "";
					} else {
						// remove any leading zeros if added
						sequ = classes.getClassSequence().replaceFirst("^0+(?!$)", "");

						char[] chars = sequ.toCharArray();

						// if its a digit and between 1 - 9 then add leading 0
						// to it
						if (Character.isDigit(sequ.charAt(0))) {
							if (chars.length < 2) {
								sequ = "0" + sequ;
							}
						}

					}

				}

				/*
				 * Check Class day
				 */
				if (classes.getDay().equals("-")) {
					classes.setTxtOutput("SELECT A DAY FROM THE DROP DOWN LIST!\n");
					return;
				} else {

					day = classes.getDay();
				}

				/*
				 * Check class times and make sure Start Time if before End Time
				 */
				if (!f.checkClassTimes(classes.getStartTime(), classes.getEndTime())) {
					classes.setTxtOutput("SELECT START & END TIMES!\n" + "\t1/ Start time must be before End time");
					return;
				} else {

					start = classes.getStartTime();
					end = classes.getEndTime();
				}

				/*
				 * Check weekly pattern, to see if it is of format such as for
				 * example 01-Jan etc.. or a weekly patter for weeks
				 */

				if (f.singleDayPattern(classes.getWeeklyPattern())) {

					if (!f.checkClassWeeklyPatternForSingleDate(
							classes.getWeeklyPattern().replaceAll("\\s+", " ").trim())) {
						classes.setTxtOutput("WEEKLY PATTERN NOT CORRECT!\n"
								+ "\t1/ Enter the date in the format, for example \"01-Jan\"\n"
								+ "\t2/ Make sure the number of days is valid for that month.");
						return;
					} else {

						if (classes.getWeeklyPattern().replaceAll("\\s+", " ").trim().length() < 6) {

							weekly = "0" + classes.getWeeklyPattern().replaceAll("\\s+", " ").trim();
							weekly = weekly.substring(0, 3) + weekly.substring(3, 4).toUpperCase()
									+ weekly.substring(4);
						} else {
							weekly = classes.getWeeklyPattern().replaceAll("\\s+", " ").trim();
							weekly = weekly.substring(0, 3) + weekly.substring(3, 4).toUpperCase()
									+ weekly.substring(4);
						}
					}

				} else {
					if (!f.checkClassWeeklyPattern(
							classes.getWeeklyPattern().trim().replaceAll("\\s+", "").replaceAll(",+", ", "))) {
						classes.setTxtOutput("WEEKLY PATTERN NOT CORRECT!\n"
								+ "\t1/ Make sure to enter in the format \"1-4, 5-10\" for range (MUST be a space after each \",\"\n"
								+ "\t Make sure the weeks make sense i.e. this is NOT valid\" 1-3, 2-5\" \n"
								+ "\tbecause week 2 is already included in the range \"1-3\"");
						return;
					} else {

						weekly = classes.getWeeklyPattern().trim().replaceAll("\\s+", "").replaceAll(",+", ", ");
					}

				}

				/*
				 * Check location for valid input
				 */

				if (!f.checkFormatOfLocation(classes.getClassLocation().trim())) {
					classes.setTxtOutput("CLASS LOCATION NOT CORRECT!\n"
							+ "\t1/ Location can start with a \".\" or a letter ONLY (e.g. .Macroberts or C.LT17 etc...)\n"
							+ "\t2/ It must end with a letter or a number, and cannot contain any other characters except spaces\n"
							+ "\t3/ It can contain the following format also: \".Macrobert (MH)\" i.e. can include a description in \"(..)\" at the end.");
					return;
				} else {

					location = classes.getClassLocation().trim();
				}

				/*
				 * Check Room size is valid input
				 */

				if (!f.checkRoomSize(classes.getRoomSize().trim())) {
					classes.setTxtOutput(
							"ROOM SIZE IS NOT CORRECT!\n" + "\t1/ Room size must be an integer and must be > 0 \n");
					return;
				} else {

					roomSize = classes.getRoomSize().trim();
				}

				/*
				 * Check Class size is valid input
				 */
				if (!f.checkClassSize(classes.getClassSize().trim())) {

					classes.setTxtOutput(
							"CLASS SIZE IS NOT CORRECT!\n" + "\t1/ Class size must be an integer and must be >= 0 \n");
					return;
				} else {

					classSize = classes.getClassSize().trim();
				}

				/*
				 * Check staff names is valid input
				 */
				if (!f.checkStaffField(classes.getStaff().trim().replaceAll("\\/\\s+", "\\/"))) {
					classes.setTxtOutput("STAFF IS NOT CORRECT!\n" + "\t1/ Staff names can be empty, or\n"
							+ "\t2/ Contain one name only e.g (\"Jim Smyth\" or \n"
							+ "\t3/ Multiple names seperated by \"/\" e.g \"Alasdair Ross/Alastair Mann/Michael Penman\" \n");
					return;
				} else {

					staff = classes.getStaff().trim().replaceAll("\\/\\s+", "\\/");
				}

				/*
				 * Check department name is valid input
				 */
				if (!f.checkDepartmentField(classes.getDepartment().trim())) {
					classes.setTxtOutput(
							"DEPARTMENT NAME IS NOT CORRECT!\n" + "\t1/ Department name CANNOT be blank, or\n"
									+ "\t2/ Deptartment name can contain only letters, whitespace, underscore, or forwardslash \n"
									+ "\te.g. \"NATCSM M\", \"ARHCMC_PG\", \"US/ADMCDS\", ");
					return;

				} else {
					department = classes.getDepartment().trim();
				}

				/*
				 * Check if this entry already exists in the database
				 */
				if (myClasses.doesEntryExist(name, day, start, end, weekly)) {
					classes.setTxtOutput("RECORD ALREADY EXISTS!\n" + "DUPLICATES NOT ALLOWED!\n\n" + name + "\t" + day
							+ "\t" + start + "\t" + end + "\t" + weekly);
				} else {

					String addRecord = name + "\t" + type + "\t" + sequ + "\t" + day + "\t" + start + "\t" + end + "\t"
							+ weekly + "\t" + location + "\t" + roomSize + "\t" + classSize + "\t" + staff + "\t"
							+ department;

					addToRecords(addRecord);
					classes.setTxtOutput("RECORD ADDED...\n");
				}

			}
		}); // addRecord

		classes.clearRecord(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				classes.clearDisplay();

			}
		}); // clearRecord

		/****************************************************
		 * END: classes panel action listeners *
		 ****************************************************/

		/****************************************************
		 * START: searching panel action listeners *
		 ****************************************************/
		searching.searchRecord(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				searching.clearSearchTable();
				if (searching.getTxtModuleNames().toString().trim().isEmpty()) {
					searching.setTxtSearchOutput("Module name cannot be blank!");

					return;

				} else {
					char[] chars = searching.getTxtModuleNames().toString().trim().toCharArray();

					for (char c : chars) {
						if (!Character.isLetterOrDigit(c)) {
							searching.setTxtSearchOutput("Module name can only contain letters or numbers!");
							return;
						}
					}
				}

				ArrayList<String> results = myClasses.lookupEntry(searching.getTxtModuleNames().toString().trim());

				if (results.isEmpty()) {
					searching.setTxtSearchOutput("No results found!");
					return;
				}

				int count = results.size();

				searching.setTxtSearchOutput("Total number of records returned for search term  \""
						+ searching.getTxtModuleNames().toString().trim() + "\" :" + count);

				Collections.sort(results);

				for (String r : results) {

					String[] rec = r.split("\t");

					searching.getModel().addRow(rec);

				}

				searching.clearSearchTerm();

			}
		}); // searchRecord

		searching.saveAllRecord(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				List<Classes> saveAll = new ArrayList<Classes>();

				String timeTableFileName = "";
				String saveToFile = "";
				saveAll = myClasses.getClassesArray();
				PrintWriter printer = null;

				timeTableFileName = new SimpleDateFormat("'Classes-'dd-MMM-yyyy-HH_mm_ss'.txt'").format(new Date());

				try {
					printer = new PrintWriter(timeTableFileName);

				} catch (FileNotFoundException e) {
					searching.setTxtSearchOutput("FileNotFound: " + e.getMessage());
				} catch (SecurityException e) {
					searching.setTxtSearchOutput("SecurityException: " + e.getMessage());
				} catch (Exception e) {
					searching.setTxtSearchOutput("Exception: " + e.getMessage());
				}

				ListIterator<Classes> iter = saveAll.listIterator();
				while (iter.hasNext()) {
					Classes current = iter.next();

					saveToFile = current.getClassName() + "." + current.getClassType();

					if (!current.getSequence().isEmpty()) {

						try {
							Integer.parseInt(current.getSequence());
							saveToFile += "/" + current.getSequence() + "\t";

						} catch (Exception e) {
							saveToFile += "(" + current.getSequence() + ")" + "\t";
						}
					} else {
						saveToFile += current.getSequence() + "\t";
					}

					saveToFile += current.getClassDay() + "\t" + current.getClassStartTime() + "\t"
							+ current.getClassEndTime() + "\t" + current.getClassWeeklyPattern() + "\t"
							+ current.getClassLocation() + "\t" + current.getClassRoomSize() + "\t"
							+ current.getClassSize() + "\t" + current.getStaff() + "\t" + current.getClassDepartment();

					try {
						printer.println(saveToFile);
					} catch (Exception e) {
						searching.setTxtSearchOutput("Exception writing to file: " + e.getMessage());
					}

				}

				if (printer != null) {
					printer.close();
				}

				searching.setTxtSearchOutput(
						"Timetable has been saved to: \n" + System.getProperty("user.dir") + "\\" + timeTableFileName);

			}
		}); // saveAllRecord

		searching.saveAsHTMLRecord(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				ArrayList<Classes> htmlList = new ArrayList<Classes>();
				String moduleList = "";
				String htmlTableCreated = "";
				PrintWriter printer = null;
				htmlList.clear();
				String htmlFileName = outputFileName;

				if (outputFileName.isEmpty()) {
					htmlFileName = "mas00145.html";
				} else {
					htmlFileName = outputFileName + ".html";
				}

				

				if (searching.getTxtModuleNames().isEmpty()) {
					searching.setTxtSearchOutput(
							"Enter your modules separated by commas only e.g. CSCU9IY,PHIU913,BUSU9EN\n"
									+ "ENTER ONLY 3 MODULES AT MOST");
					return;
				} else {

					moduleList = searching.getTxtModuleNames().trim()
							.replaceAll("^[^a-zA-Z0-9\\s]+|[^a-zA-Z0-9\\s]+$", "").replaceAll("\\s+", "");
					if (!f.checkModulesForHTMLOutput(moduleList)) {
						searching.setTxtSearchOutput(
								"Enter your modules separated by commas only e.g. CSCU9IY,PHIU913,BUSU9EN\n"
										+ "ENTER ONLY 3 MODULES AT MOST");
					} else {

						htmlList = myClasses.findExactMatches(moduleList);

						htmlTableCreated = arrayListToHTML(htmlList, moduleList);
						
						
						try {
							printer = new PrintWriter(htmlFileName);

						} catch (FileNotFoundException e) {
							searching.setTxtSearchOutput("FileNotFound: " + e.getMessage());
							return;
						} catch (SecurityException e) {
							searching.setTxtSearchOutput("SecurityException: " + e.getMessage());
							return;
						} catch (Exception e) {
							searching.setTxtSearchOutput("Exception: " + e.getMessage());
							return;
						}

						try {
							printer.println(htmlTableCreated);
						} catch (Exception e) {
							searching.setTxtSearchOutput("Exception writing to file: " + e.getMessage());
						}

						if (printer != null) {
							printer.close();
						}

						searching.setTxtSearchOutput("Timetable has been saved to: \n" + System.getProperty("user.dir")
								+ "\\" + htmlFileName);

					}

				}

			}
		}); // saveAsHTMLRecord

		searching.clearSearchRecord(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				searching.clearSearch();
			}
		}); // clearSearchRecord
		/****************************************************
		 * END: searching panel action listeners *
		 ****************************************************/

		applic.setSize(890, 400);
		applic.setLocationRelativeTo(null); // center on screen
		applic.setResizable(false);

		applic.setVisible(true);

		createDatabase(myClasses, inputFileName);
	}

	private static void createDatabase(ClassRecord myClasses, String inputFile) {

		formattedFile = f.getFormattedLine(inputFile);
		duplicatesList = f.getCheckDuplicates();
		errorLog = f.getErrorLog();
		if (formattedFile.isEmpty()) {

			classes.setOutputArea("No records found in the file!!");
		} else {

			// add all the formatted and valid records found in the file parsed
			addToRecords(formattedFile);
		}

		String errorOutput = errorLog.size() + " ERRORS FOUND IN INPUT FILE:" + "\n";
		if (!errorLog.isEmpty()) {
			for (String s : errorLog) {
				s = s.replaceAll("\\t+", "   ");
				errorOutput += s + "\n";
			}
		}

		classes.setOutputArea(errorOutput + "\n" + "Number of records added: " + formattedFile.size() + "\n"
				+ "Number of duplicates removed: " + duplicatesList.size());

	}

	/**
	 * @param recList
	 */
	private static void addToRecords(ArrayList<String> recList) {

		for (String s : recList) {
			addToRecords(s);
		}
	}

	/**
	 * @param rec
	 */
	private static void addToRecords(String rec) {

		rec = rec.trim();
		String[] classRecords = rec.split("\\t");

		switch (classRecords[1].toUpperCase()) {
		case "L":
			Lecture l = new Lecture(classRecords[0], classRecords[1], classRecords[2], classRecords[3], classRecords[4],
					classRecords[5], classRecords[6], classRecords[7], classRecords[8], classRecords[9],
					classRecords[10], classRecords[11]);
			myClasses.addEntry(l);

			break;
		case "CL":
			ComputerLab cl = new ComputerLab(classRecords[0], classRecords[1], classRecords[2], classRecords[3],
					classRecords[4], classRecords[5], classRecords[6], classRecords[7], classRecords[8],
					classRecords[9], classRecords[10], classRecords[11]);
			myClasses.addEntry(cl);
			break;
		case "S":
			Seminar s = new Seminar(classRecords[0], classRecords[1], classRecords[2], classRecords[3], classRecords[4],
					classRecords[5], classRecords[6], classRecords[7], classRecords[8], classRecords[9],
					classRecords[10], classRecords[11]);
			myClasses.addEntry(s);
			break;
		default:
			String errorCreatingRedcord = classes.getTxtOutput();
			classes.setTxtOutput(errorCreatingRedcord + "\n" + rec);
			break;
		}

	}

	private static void initialiseHTMLArrayList() {

		// Initialise the arraylist used to print timetable to HTML
		for (int i = 0; i < HTML_ROWS; i++) {
			htmlTimeTable[i] = new ArrayList<String>();
		}

		// 5 columns for row 0
		htmlTimeTable[0].add("Time");
		htmlTimeTable[0].add("Monday");
		htmlTimeTable[0].add("Tuesday");
		htmlTimeTable[0].add("Wednesday");
		htmlTimeTable[0].add("Thursday");
		htmlTimeTable[0].add("Friday");

		// add the first entries for each row
		htmlTimeTable[1].add("09:05");
		htmlTimeTable[2].add("10:05");
		htmlTimeTable[3].add("11:05");
		htmlTimeTable[4].add("12:05");
		htmlTimeTable[5].add("13:05");
		htmlTimeTable[6].add("14:05");
		htmlTimeTable[7].add("15:05");
		htmlTimeTable[8].add("16:05");
		htmlTimeTable[9].add("17:05");

		for (int i = 1; i < HTML_ROWS; i++) {
			for (int j = 1; j < HTML_COLS; j++) {
				htmlTimeTable[i].add(j, " ");
			}
		}

	}

	private static String arrayListToHTML(ArrayList<Classes> htmlList, String modules) {

		ListIterator<Classes> iter = htmlList.listIterator();

		while (iter.hasNext()) {
			Classes current = iter.next();
			for (int i = 1; i < HTML_ROWS; i++) {

				for (int j = 1; j < HTML_COLS; j++) {
					if (current.getClassStartTime().equals(htmlTimeTable[i].get(0))
							&& current.getClassDay().equals(htmlTimeTable[0].get(j))) {
						// System.out.println(htmlTimeTable[0].get(j));
						if (htmlTimeTable[i].get(j).equals(" ")) {
							htmlTimeTable[i].set(j,
									current.getClassName() + "." + current.getClassType() + " "
											+ current.getClassLocation() + "<br>" + "<center>["
											+ current.getClassWeeklyPattern() + "]</center>");
						} else {
							String append = htmlTimeTable[i].get(j);
							htmlTimeTable[i].set(j,
									append + "<br>" + current.getClassName() + "." + current.getClassType() + " "
											+ current.getClassLocation() + "<br>" + "<center>["
											+ current.getClassWeeklyPattern() + "]</center>");
						}

					} else {
						if (htmlTimeTable[i].get(j).equals(" ")) {
							htmlTimeTable[i].set(j, " ");
						}

					}
				}
			}
		}

		StringBuilder html = new StringBuilder("<!doctype html><html lang=\"en\">"
				+ "<head><style>table {border-style: 1px solid #ff0000;}tr {border-style: 1px solid #ff0000;} td {border-style: 1px solid #ff0000;} th {border-style: 1px solid #ff0000;}"
				+ "</style><meta charset=\"utf-8\"><title>" + "Timetable for: " + modules + "</title></head><body>"
				+ "<h2>Timetable for: " + modules + "</h2>");

		html.append("<table border=\"1\">");
		for (String s : htmlTimeTable[0]) {
			html.append("<th>" + s.toString() + "</th>");
		}

		for (int i = 1; i < HTML_ROWS; i++) {
			html.append("<tr>");
			for (int j = 0; j < HTML_COLS; j++) {

				html.append("<td>" + htmlTimeTable[i].get(j) + "</td>");

			}
			html.append("</tr>");
		}

		html.append("</table></body></html>");

		/*
		 * Initialise the array after we're done with it So next time user
		 * clicks on save HTML, we don't append to the original string
		 */
		initialiseHTMLArrayList(); //

		return html.toString();
	}

}
