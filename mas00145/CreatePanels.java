import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;


@SuppressWarnings("serial")
public class CreatePanels extends JPanel {

	/*******************************************************
	 * START Classes panel attributes *
	 *******************************************************/

	private JLabel labClassName = new JLabel("ClassName: ");
	private JTextField txtClassName = new JTextField(10);

	private JLabel labClassType = new JLabel("ClassType:");
	private JComboBox<String> combClassType = new JComboBox<String>();

	private JLabel labSequence = new JLabel("Sequence:");
	private JTextField txtSequence = new JTextField(3);

	private JLabel labDay = new JLabel("Day: ");
	private JComboBox<String> combDay = new JComboBox<String>();

	private JLabel labClassStart = new JLabel("ClassStart:");
	private JSpinner spinStartHour = new JSpinner();
	private JSpinner spinStartMin = new JSpinner();

	private JLabel labClassEnd = new JLabel("ClassEnd:");
	private JSpinner spinEndHour = new JSpinner();
	private JSpinner spinEndMin = new JSpinner();

	private JLabel labLocation = new JLabel("Location:");
	private JTextField txtLocation = new JTextField(25);

	private JLabel labWeeklyPattern = new JLabel("WeeklyPattern:");
	private JTextField txtWeeklyPattern = new JTextField(20);

	private JLabel labRoomSize = new JLabel("RoomSize:");
	private JTextField txtRoomSize = new JTextField();

	private JLabel labClassSize = new JLabel("ClassSize:");
	private JTextField txtClassSize = new JTextField();

	private JLabel labStaff = new JLabel("Staff:");
	private JTextField txtStaff = new JTextField(80);

	JLabel labDepartment = new JLabel("Department:");
	private JTextField txtDepartment = new JTextField(15);

	private JTextArea txtOutput = new JTextArea(5, 10);
	private JScrollPane txtOutputScroll = new JScrollPane(txtOutput);

	private JButton btnAdd = new JButton("Add");
	private JButton btnClear = new JButton("Clear");

	/*******************************************************
	 * END Classes panel attributes *
	 *******************************************************/

	/*******************************************************
	 * START Search panel attributes *
	 *******************************************************/

	
	private String[] columns = new String[] { "Alert", "ClassName", "Type", "Sequ", "Day", "StartTime", "EndTime",
			"WeeklyPattern", "Location", "RoomSize", "ClassSize", "Staff", "Dept" };



	/**
	 * create a DefaultTableModel model to use with the JTable
	 */
	private DefaultTableModel model = new DefaultTableModel(new Object[][] {}, columns) {
		public boolean isCellEditable(int rowIndex, int mColIndex) {
			return false;
		}
	};

	/**
	 * create JTable and attach the DefaultTableModel model to it
	 * make sure the table columns are auto adjusted to fit the content
	 */
	private JTable tableSearchResults = new JTable(model) {
		@Override
		public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
			Component component = super.prepareRenderer(renderer, row, column);
			int rendererWidth = component.getPreferredSize().width;
			TableColumn tableColumn = getColumnModel().getColumn(column);
			tableColumn.setPreferredWidth(
					Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
			return component;
		}
	};

	private JLabel labModuleNames = new JLabel("ModuleName(s):");
	private JTextField txtModuleNames = new JTextField(10);
	private JTextArea txtSearchOutput = new JTextArea(5, 30);
	private JButton btnSearch = new JButton("Search");
	private JButton btnSaveAll = new JButton("Save All");
	private JButton btnSaveAsHTML = new JButton("Save As HTML");
	private JButton btnClearSearch = new JButton("Clear");
	

	/*******************************************************
	 * END Search panel attributes *
	 *******************************************************/

	/**
	 * new types can be added as and when they are needed in the future
	 * @param typeOfPanel - this can be "classes" or "search"
	 * n
	 */
	public CreatePanels(String typeOfPanel) {

		setPanelComponents(typeOfPanel);

	}

	/**
	 * types - this can be extended easily if we decide to add more types of panels in the future
	 * @param typeOfPanel can be "classes" or "search"  
	 */
	public void setPanelComponents(String typeOfPanel) {

		typeOfPanel = typeOfPanel.toLowerCase();

		switch (typeOfPanel) {
		case "classes":
			setClassPanelComponents();
			break;
		case "search":
			setSearchPanelComponents();
			break;
		default:
			System.out.println("Unknow panel type!!");
			System.exit(0);
			break;
		}

	}

	/**
	 * create the classes panel - and set all the required components
	 */
	private void setClassPanelComponents() {

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 20, 30, 0, 30, 0, 30, 0, 30, 0, 100, 0, 20, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0,
				0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		setLayout(gridBagLayout);

		GridBagConstraints gbc_labClassName = new GridBagConstraints();
		gbc_labClassName.anchor = GridBagConstraints.EAST;
		gbc_labClassName.insets = new Insets(0, 0, 5, 6);
		gbc_labClassName.gridx = 1;
		gbc_labClassName.gridy = 1;
		add(labClassName, gbc_labClassName);

		GridBagConstraints gbc_txtClassName = new GridBagConstraints();
		gbc_txtClassName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtClassName.insets = new Insets(0, 0, 5, 5);
		gbc_txtClassName.gridx = 2;
		gbc_txtClassName.gridy = 1;
		add(txtClassName, gbc_txtClassName);
		txtClassName.setColumns(10);

		GridBagConstraints gbc_labClassType = new GridBagConstraints();
		gbc_labClassType.insets = new Insets(0, 0, 5, 6);
		gbc_labClassType.anchor = GridBagConstraints.EAST;
		gbc_labClassType.gridx = 4;
		gbc_labClassType.gridy = 1;
		add(labClassType, gbc_labClassType);

		combClassType.setModel(new DefaultComboBoxModel<String>(new String[] { "-", "L", "CL", "S" }));
		combClassType.setSelectedIndex(0);
		combClassType.setBackground(Color.WHITE);
		GridBagConstraints gbc_combClassType = new GridBagConstraints();
		gbc_combClassType.insets = new Insets(0, 0, 5, 5);
		gbc_combClassType.anchor = GridBagConstraints.EAST;
		gbc_combClassType.gridx = 5;
		gbc_combClassType.gridy = 1;
		add(combClassType, gbc_combClassType);

		GridBagConstraints gbc_labSequence = new GridBagConstraints();
		gbc_labSequence.insets = new Insets(0, 6, 5, 6);
		gbc_labSequence.anchor = GridBagConstraints.EAST;
		gbc_labSequence.gridx = 7;
		gbc_labSequence.gridy = 1;
		add(labSequence, gbc_labSequence);

		GridBagConstraints gbc_txtSequence = new GridBagConstraints();
		gbc_txtSequence.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtSequence.insets = new Insets(0, 0, 5, 5);
		gbc_txtSequence.gridx = 8;
		gbc_txtSequence.gridy = 1;
		add(txtSequence, gbc_txtSequence);
		txtSequence.setColumns(3);

		GridBagConstraints gbc_labDay = new GridBagConstraints();
		gbc_labDay.anchor = GridBagConstraints.EAST;
		gbc_labDay.insets = new Insets(0, 0, 5, 6);
		gbc_labDay.gridx = 1;
		gbc_labDay.gridy = 3;
		add(labDay, gbc_labDay);

		combDay.setModel(new DefaultComboBoxModel<String>(
				new String[] { "-", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" }));
		combDay.setSelectedIndex(0);
		combDay.setBackground(Color.WHITE);
		GridBagConstraints gbc_combDay = new GridBagConstraints();
		gbc_combDay.fill = GridBagConstraints.HORIZONTAL;
		gbc_combDay.insets = new Insets(0, 0, 5, 5);
		gbc_combDay.gridx = 2;
		gbc_combDay.gridy = 3;
		add(combDay, gbc_combDay);

		GridBagConstraints gbc_labClassStart = new GridBagConstraints();
		gbc_labClassStart.anchor = GridBagConstraints.EAST;
		gbc_labClassStart.insets = new Insets(0, 0, 5, 6);
		gbc_labClassStart.gridx = 4;
		gbc_labClassStart.gridy = 3;
		add(labClassStart, gbc_labClassStart);

		spinStartHour.setModel(new SpinnerListModel(new String[] { "09", "10", "11", "12", "13", "14", "15", "16", "17" }));
		GridBagConstraints gbc_spinStartHour = new GridBagConstraints();
		gbc_spinStartHour.insets = new Insets(0, 0, 5, 0);
		gbc_spinStartHour.anchor = GridBagConstraints.EAST;
		gbc_spinStartHour.gridx = 5;
		gbc_spinStartHour.gridy = 3;
		add(spinStartHour, gbc_spinStartHour);

		spinStartMin.setModel(new SpinnerListModel(
				new String[] { "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55" }));
		GridBagConstraints gbc_spinStartMin = new GridBagConstraints();
		gbc_spinStartMin.insets = new Insets(0, 0, 5, 5);
		gbc_spinStartMin.anchor = GridBagConstraints.WEST;
		gbc_spinStartMin.gridx = 6;
		gbc_spinStartMin.gridy = 3;
		add(spinStartMin, gbc_spinStartMin);

		labClassEnd.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_labClassEnd = new GridBagConstraints();
		gbc_labClassEnd.anchor = GridBagConstraints.EAST;
		gbc_labClassEnd.insets = new Insets(0, 5, 5, 6);
		gbc_labClassEnd.gridx = 7;
		gbc_labClassEnd.gridy = 3;
		add(labClassEnd, gbc_labClassEnd);

		spinEndHour.setModel(new SpinnerListModel(new String[] { "09", "10", "11", "12", "13", "14", "15", "16", "17"}));
		GridBagConstraints gbc_spinEndHour = new GridBagConstraints();
		gbc_spinEndHour.anchor = GridBagConstraints.EAST;
		gbc_spinEndHour.insets = new Insets(0, 0, 5, 0);
		gbc_spinEndHour.gridx = 8;
		gbc_spinEndHour.gridy = 3;
		add(spinEndHour, gbc_spinEndHour);

		spinEndMin.setModel(new SpinnerListModel(
				new String[] { "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55" }));
		GridBagConstraints gbc_spinEndMin = new GridBagConstraints();
		gbc_spinEndMin.anchor = GridBagConstraints.WEST;
		gbc_spinEndMin.insets = new Insets(0, 0, 5, 5);
		gbc_spinEndMin.gridx = 9;
		gbc_spinEndMin.gridy = 3;
		add(spinEndMin, gbc_spinEndMin);

		GridBagConstraints gbc_labWeeklyPattern = new GridBagConstraints();
		gbc_labWeeklyPattern.anchor = GridBagConstraints.WEST;
		gbc_labWeeklyPattern.insets = new Insets(0, 0, 5, 6);
		gbc_labWeeklyPattern.gridx = 11;
		gbc_labWeeklyPattern.gridy = 3;
		add(labWeeklyPattern, gbc_labWeeklyPattern);

		GridBagConstraints gbc_txtWeeklyPattern = new GridBagConstraints();
		gbc_txtWeeklyPattern.gridwidth = 2;
		gbc_txtWeeklyPattern.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtWeeklyPattern.insets = new Insets(0, 0, 5, 0);
		gbc_txtWeeklyPattern.gridx = 12;
		gbc_txtWeeklyPattern.gridy = 3;
		add(txtWeeklyPattern, gbc_txtWeeklyPattern);
		txtWeeklyPattern.setColumns(10);

		GridBagConstraints gbc_labLocation = new GridBagConstraints();
		gbc_labLocation.anchor = GridBagConstraints.EAST;
		gbc_labLocation.insets = new Insets(0, 0, 5, 6);
		gbc_labLocation.gridx = 1;
		gbc_labLocation.gridy = 5;
		add(labLocation, gbc_labLocation);

		GridBagConstraints gbc_txtLocation = new GridBagConstraints();
		gbc_txtLocation.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtLocation.insets = new Insets(0, 0, 5, 6);
		gbc_txtLocation.gridx = 2;
		gbc_txtLocation.gridy = 5;
		add(txtLocation, gbc_txtLocation);
		txtLocation.setColumns(10);

		GridBagConstraints gbc_labRoomSize = new GridBagConstraints();
		gbc_labRoomSize.anchor = GridBagConstraints.EAST;
		gbc_labRoomSize.insets = new Insets(0, 0, 5, 6);
		gbc_labRoomSize.gridx = 4;
		gbc_labRoomSize.gridy = 5;
		add(labRoomSize, gbc_labRoomSize);

		GridBagConstraints gbc_txtRoomSize = new GridBagConstraints();
		gbc_txtRoomSize.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtRoomSize.gridwidth = 2;
		gbc_txtRoomSize.insets = new Insets(0, 0, 5, 5);
		gbc_txtRoomSize.gridx = 5;
		gbc_txtRoomSize.gridy = 5;
		add(txtRoomSize, gbc_txtRoomSize);
		txtRoomSize.setColumns(10);

		GridBagConstraints gbc_labClassSize = new GridBagConstraints();
		gbc_labClassSize.anchor = GridBagConstraints.EAST;
		gbc_labClassSize.insets = new Insets(0, 0, 5, 5);
		gbc_labClassSize.gridx = 7;
		gbc_labClassSize.gridy = 5;
		add(labClassSize, gbc_labClassSize);

		GridBagConstraints gbc_txtClassSize = new GridBagConstraints();
		gbc_txtClassSize.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtClassSize.gridwidth = 2;
		gbc_txtClassSize.insets = new Insets(0, 0, 5, 5);
		gbc_txtClassSize.gridx = 8;
		gbc_txtClassSize.gridy = 5;
		add(txtClassSize, gbc_txtClassSize);
		txtClassSize.setColumns(10);

		GridBagConstraints gbc_labStaff = new GridBagConstraints();
		gbc_labStaff.anchor = GridBagConstraints.EAST;
		gbc_labStaff.insets = new Insets(0, 0, 5, 6);
		gbc_labStaff.gridx = 1;
		gbc_labStaff.gridy = 7;
		add(labStaff, gbc_labStaff);

		GridBagConstraints gbc_txtStaff = new GridBagConstraints();
		gbc_txtStaff.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtStaff.gridwidth = 8;
		gbc_txtStaff.insets = new Insets(0, 0, 5, 5);
		gbc_txtStaff.gridx = 2;
		gbc_txtStaff.gridy = 7;
		add(txtStaff, gbc_txtStaff);
		txtStaff.setColumns(10);

		GridBagConstraints gbc_labDepartment = new GridBagConstraints();
		gbc_labDepartment.anchor = GridBagConstraints.EAST;
		gbc_labDepartment.insets = new Insets(0, 0, 5, 5);
		gbc_labDepartment.gridx = 11;
		gbc_labDepartment.gridy = 7;
		add(labDepartment, gbc_labDepartment);

		GridBagConstraints gbc_txtDepartment = new GridBagConstraints();
		gbc_txtDepartment.gridwidth = 2;
		gbc_txtDepartment.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtDepartment.insets = new Insets(0, 0, 5, 0);
		gbc_txtDepartment.gridx = 12;
		gbc_txtDepartment.gridy = 7;
		add(txtDepartment, gbc_txtDepartment);
		txtDepartment.setColumns(10);

		GridBagConstraints gbc_txtOutput = new GridBagConstraints();
		gbc_txtOutput.insets = new Insets(0, 0, 5, 0);
		gbc_txtOutput.gridwidth = 14;
		gbc_txtOutput.fill = GridBagConstraints.BOTH;
		gbc_txtOutput.gridx = 0;
		gbc_txtOutput.gridy = 9;
		add(txtOutputScroll, gbc_txtOutput);
		txtOutput.setColumns(10);

		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.anchor = GridBagConstraints.WEST;
		gbc_btnAdd.insets = new Insets(0, 0, 0, 5);
		gbc_btnAdd.gridx = 1;
		gbc_btnAdd.gridy = 11;
		add(btnAdd, gbc_btnAdd);

		GridBagConstraints gbc_btnClear = new GridBagConstraints();
		gbc_btnClear.anchor = GridBagConstraints.WEST;
		gbc_btnClear.insets = new Insets(0, 0, 0, 5);
		gbc_btnClear.gridx = 2;
		gbc_btnClear.gridy = 11;
		add(btnClear, gbc_btnClear);

	}

	/**
	 * create the searches panel and add all the required components
	 */
	private void setSearchPanelComponents() {

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 30, 170, 170, 170, 0, 0, 0, 30, 0, 0, 30, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 50, 0, 0, 0, 0, 100, 50, 0, 0, 0, 10 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		setLayout(gridBagLayout);

		GridBagConstraints gbc_labModuleNames = new GridBagConstraints();
		gbc_labModuleNames.anchor = GridBagConstraints.WEST;
		gbc_labModuleNames.insets = new Insets(0, 0, 5, 5);
		gbc_labModuleNames.gridx = 1;
		gbc_labModuleNames.gridy = 1;
		add(labModuleNames, gbc_labModuleNames);

		GridBagConstraints gbc_txtModuleNames = new GridBagConstraints();
		gbc_txtModuleNames.gridwidth = 8;
		gbc_txtModuleNames.insets = new Insets(0, 0, 5, 5);
		gbc_txtModuleNames.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtModuleNames.gridx = 2;
		gbc_txtModuleNames.gridy = 1;
		add(txtModuleNames, gbc_txtModuleNames);
		txtModuleNames.setColumns(10);

		txtSearchOutput.setEditable(false);
		GridBagConstraints gbc_txtSearchOutput = new GridBagConstraints();
		gbc_txtSearchOutput.gridheight = 2;
		gbc_txtSearchOutput.gridwidth = 12;
		gbc_txtSearchOutput.insets = new Insets(0, 0, 5, 5);
		gbc_txtSearchOutput.fill = GridBagConstraints.BOTH;
		gbc_txtSearchOutput.gridx = 1;
		gbc_txtSearchOutput.gridy = 2;
		add(txtSearchOutput, gbc_txtSearchOutput);

		tableSearchResults.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		JScrollPane paneSearch = new JScrollPane(tableSearchResults);

		GridBagConstraints gbc_tableSearchResults = new GridBagConstraints();
		gbc_tableSearchResults.gridheight = 5;
		gbc_tableSearchResults.gridwidth = 12;
		gbc_tableSearchResults.insets = new Insets(0, 0, 5, 5);
		gbc_tableSearchResults.fill = GridBagConstraints.BOTH;
		gbc_tableSearchResults.gridx = 1;
		gbc_tableSearchResults.gridy = 4;
		add(paneSearch, gbc_tableSearchResults);

		GridBagConstraints gbc_btnSearch = new GridBagConstraints();
		gbc_btnSearch.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSearch.insets = new Insets(0, 0, 5, 5);
		gbc_btnSearch.gridx = 1;
		gbc_btnSearch.gridy = 9;
		add(btnSearch, gbc_btnSearch);

		GridBagConstraints gbc_btnSaveAll = new GridBagConstraints();
		gbc_btnSaveAll.anchor = GridBagConstraints.WEST;
		gbc_btnSaveAll.insets = new Insets(0, 0, 5, 5);
		gbc_btnSaveAll.gridx = 2;
		gbc_btnSaveAll.gridy = 9;
		add(btnSaveAll, gbc_btnSaveAll);

		GridBagConstraints gbc_btnSaveAsHtml = new GridBagConstraints();
		gbc_btnSaveAsHtml.anchor = GridBagConstraints.WEST;
		gbc_btnSaveAsHtml.insets = new Insets(0, 0, 5, 5);
		gbc_btnSaveAsHtml.gridx = 3;
		gbc_btnSaveAsHtml.gridy = 9;
		add(btnSaveAsHTML, gbc_btnSaveAsHtml);

		GridBagConstraints gbc_btnClearSearch = new GridBagConstraints();
		gbc_btnClearSearch.anchor = GridBagConstraints.WEST;
		gbc_btnClearSearch.insets = new Insets(0, 0, 5, 5);
		gbc_btnClearSearch.gridx = 4;
		gbc_btnClearSearch.gridy = 9;
		add(btnClearSearch, gbc_btnClearSearch);

	}

	// attach action listeners to btnAdd button
	public void addRecord(ActionListener al) {
		btnAdd.addActionListener(al);
	}

	// attach action listeners to btnClear button
	public void clearRecord(ActionListener al) {
		btnClear.addActionListener(al);
	}

	// attach action listeners to btnSearch button
	public void searchRecord(ActionListener al) {
		btnSearch.addActionListener(al);
	}

	// attach action listeners to btnSaveAll button
	public void saveAllRecord(ActionListener al) {
		btnSaveAll.addActionListener(al);
	}

	// attach action listeners to btnSaveAsHTML button
	public void saveAsHTMLRecord(ActionListener al) {
		btnSaveAsHTML.addActionListener(al);
	}

	// attach action listeners to btnClearSearch button
	public void clearSearchRecord(ActionListener al) {
		btnClearSearch.addActionListener(al);
	}

	

	/*
	 * START: Add modules components
	 */
	public void setOutputArea(String outputArea) {
		txtOutput.setForeground(Color.RED);
		this.txtOutput.setText(outputArea);
	}

	public String getClassName() {
		return txtClassName.getText();
	}

	public String getClassType() {
		return (String) combClassType.getSelectedItem();
	}

	public String getClassSequence() {
		return txtSequence.getText();
	}

	public String getDay() {
		return (String) combDay.getSelectedItem();
	}

	public String getStartTime() {

		return spinStartHour.getValue() + ":" + spinStartMin.getValue();
	}

	public String getEndTime() {
		return spinEndHour.getValue() + ":" + spinEndMin.getValue();
	}

	public String getWeeklyPattern() {
		return txtWeeklyPattern.getText();
	}

	public String getClassLocation() {
		return txtLocation.getText();
	}

	public String getRoomSize() {
		return txtRoomSize.getText();
	}

	public String getClassSize() {
		return txtClassSize.getText();
	}

	public String getStaff() {
		return txtStaff.getText();
	}

	public String getDepartment() {
		return txtDepartment.getText();
	}

	

	public void clearDisplay() {
		txtClassName.setText("");
		combClassType.setSelectedIndex(0);
		txtSequence.setText("");
		combDay.setSelectedIndex(0);
		txtWeeklyPattern.setText("");
		txtLocation.setText("");
		txtRoomSize.setText("");
		txtClassSize.setText("");
		txtStaff.setText("");
		txtDepartment.setText("");
		txtOutput.setText("");

	}
	
	public void setTxtOutput(String classesOutput) {
		this.txtOutput.setText(classesOutput);
	}
	
	/*
	 * END: Add modules components
	 */

	/*
	 * START: Search and Save components
	 */
	public void clearSearch() {
		txtModuleNames.setText("");
		txtSearchOutput.setText("");
		model.setRowCount(0);
	}

	public void clearSearchTable() {
		model.setRowCount(0);
	}

	public void clearSearchTerm() {
		txtModuleNames.setText("");
	}

	public JTable getsearchTable() {
		return this.tableSearchResults;
	}

	public DefaultTableModel getModel() {
		return this.model;
	}

	public String getTxtOutput() {
		 return this.txtOutput.getText();
	}

//	public String getTxtSequence() {
//		return txtSequence.getText();
//	}


	public String getTxtModuleNames() {
		return txtModuleNames.getText();
	}
	
	public void setTxtSearchOutput(String txtSearchOutput) {
		this.txtSearchOutput.setText(txtSearchOutput);
	}
	/*
	 * END: Search and Save components
	 */

	

}
