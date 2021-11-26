
# 1. 	I have implemented all of the functionality required in this assignment. 

	The assumptions I have made are as follows:

	a)	A “unique” record is defined by the following 5 fields only:
			i)	Name, Day, Start Time, End Time and Weekly Pattern
		
	b)	The “*” character is NOT allowed in the Location field, and also NOT allowed in the RoomSize field, if these 2 fields contain this 
		illegal character they are marked as bad input and displayed after the loading of the source file.

	c)	To load the program, via the command line prompt, the source file MUST be in the top level directory.
		E.g. it must be in the following directory: C:\mas00145\complex.txt and only the file name is accepted, NOT a full path to the file.

	d) 	To load the source file via Eclipse or BlueJ, the file again MUST be in the top level directory as stated above. (C:\mas00145\complex.txt)

	e) 	There must be ONLY 10 fields in the source file, each separated by a tab one field each for the following types:
		Name, Day, Start Time, EndTime, WeeklyPattern, Location, RoomSize, ClassSize, StaffName, Department

		Name can consist of multiple names, each separated by a “/” after the Name there must be a “.” After which Type of class is specified 
		(E.g. L for Lectuer, CL for Computer Lab and S for Seminar) after this an optional sequence number can be specified: E.g.  ACCU9A5.S/01

		The only optional fields is StaffName, and SequenceNumer or Letter – if no StaffName is specified there must still be a tab between it and the field before 
		and after it. All other fields are required fields, in order for the record to be a valid one.

	The program provides the following functionality:

	a)	A user, after loading the source file, can add a new record. All mandatory fields are checked for correct formatting, and an error message 
				is given until the correct input is supplied.

	b)	The user can search for a record by entering the module code, partial and non-initial matches are returned and displayed in a table. 

	c)	The “Alert” field in the table will give a message for Computer Labs (CL)
			i)	If ClassSize > RoomSize

	d)	The “Alert” field in the table will display a message I for Seminars (S)
			i)	If ClassSize is less then half of the RoomSize
			ii)	If ClassSize is more than 10% above RoomSize

	e)	Lectures will be displayed in ALL CAPS

	f)	Seminars and Computer Labs will be displayed in all lowercase

	g)	The user is able to SaveAll records in the original format, by clicking the SaveAll button on the “Search and Save” tab. 
		The file created is date and time stamped, so as not to overwrite the original file.
			
	h)	The user is able to search for and create a timetable for up to 3 module codes. It is up to the user to make sure the names exist, 
		if none of the names are found in the database, then a blank timetable is presented. Any class names that are found are added to the timetable 
		and the file is saved using the original source files name and only a “html” extension is added to this filename. There is a limit of a maximum 
		of 3 modules only. In order to produce the timetable the user must enter the modules in the search area and click the SaveHTML button. The names must 
		be a comma separated list only.
			
# 2. 	The Object Model:

	2.1	Classes.java:

	The superclass Classes.java, is an abstract class, which contains attributes and operations that are common to all the sublasses. 
	The superclass (Classes) has 3 abstract methods defined:

		public abstract String getEntry();

		public abstract String getStaff();

		public abstract String getSequence();
		
	These are required methods in the child classes, and are implemented in each child class, to take account of the unique requirements for display, and search purposes.


	2.2	Lecture.java, Seminar.java, ComputerLab.java:  (extends Classes):

	From the superclass (Classes.java), I have defined 3 child classes: A Lecture.java, Seminar.java and ComputerLab.java class, to take define each of the required objects. 
	
	2.3	FileMethods.java

	I have also created a FileMethods.java class, to hold all the common file and record processing methods required. This is to ensure that the main object classes are as independant as 
	possible, and to achieve low coupling, so that any future requirements changes to record searching, or field definitions can be changed in one place and have no impact on the main object classes.
	By defining the classes in this manner also achieves high cohesion, in other words each class only has methods related to its intention. All methods not required or relevant are not placed in 
	their own class.

	2.4	ClassRecord.java (implements Comparator<Classes>)
	
	This class contains all the methods for searching and adding records to the database. Again these methods are placed in a seperate class to achieve high cohesion and low coupling for each class in the program.
	These methods can be changed if future requirements change, or if the definition for a "unique" record changes, only one method needs to be changed without impacting any other part of the program.
	This class also implements the Comparator interface, this is so that I could implement a custom compare() method, to Override the standard one, I needed this to enable me to sort the Classes objects on a specific field
	so that I could order them in StartTime, to able to produce the HTML time table in the most efficient and logical manner.


	2.5 	CreatePanels.java (extends JPanel)

	This class is used to create the GUI components. Each display area is of type JPanel, the panels are created and the CreatePanels object is then used in the ClassRecordGUI.java class, and added to a JTabbedPane
	component, and displayed. I have a seperated this into a seperate class so that if in the future more tabs, GUI functionality is required it can be added with very little difficulty, and without impacting the rest of the program.

	2.6	ClassRecordGUI	(contains the main method)
		
		This class contains the main method, and is used to display and start execution of the program.


	I have used an ArrayList to store the Classes objects, however when parsing the file, I also used a TreeSet, to check for uniqueness of each record as it is loaded. 
	By attempting to add each record to the TreeSet, a Boolean is returned – true is the record is added and false if it is not. Any false returns are added to a duplicates ArrayList, 
	so that a count of duplicate records can be shown to the user after starting the application. Any errors detected in the source file are also displayed after parsing and loading 
	the database. 
	
	The ClassRecord class implements the Comparator interface, this is because I have overridden the “compare” method, in order to write my own compare method, to be able to sort 
	on a specific field from the Classes objects. I wanted to sort search results on start hour, so that the records are in the correct order, to enable me to print the HTML file 
	more efficiently. The object model can be seen in the attached mas00145_UML_DIAGRAM.png file.
	
	
	I have also created a FileMethods class, to hold all of the functionality for source file and field checking methods. Again if the requirements for the records and what is
	a legal record changes in the future, these methods can be changed to reflect the changes. The majority of methods in the FileMethods class return a boolean, this is to simplify any 
	changes that might come up in the future. The boolean is retuned to indicate a valid (true) of non-valid(false) field in the record.

	
# 3	UML Diagram

	3.1	mas00145_UML_DIAGRAM.png (the UML diagram detailing all classes, methods and attriibutes)



