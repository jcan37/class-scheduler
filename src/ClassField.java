import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.Box;
import javax.swing.GrayFilter;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Handles a single class
 * 
 * @author Jeffrey Cannon and Alexa Cashetta
 * @date 06/03/13
 */
public class ClassField extends JPanel implements FocusListener {

	// ------ fields ------
	private static final long serialVersionUID = 1L;
	protected JLabel teacherLabel, locLabel, typeLabel, startLabel, endLabel;
	protected JTextField teacher, loc, start, end;
	protected JComboBox<String> type;
	protected static String[] items = {"Lecture", "Lab", "Discussion", "Seminar"};
	protected JPanel week;
	protected ArrayList<JLabel> days;
	protected ArrayList<JCheckBox> select;
	protected JCheckBox enable;
	protected JButton close;
	protected String directory;
	
	public ClassField() {
		// initialize directory
		directory = System.getProperty("user.home") + File.separator + "JA App" + File.separator;
		
		// initialize labels first
		teacherLabel = new JLabel("Instructor:", JLabel.CENTER);
		locLabel = new JLabel("Location:", JLabel.CENTER);
		typeLabel = new JLabel("Type:", JLabel.CENTER);
		startLabel = new JLabel("Start time:", JLabel.CENTER);
		endLabel = new JLabel("End Time:", JLabel.CENTER);
		
		// initialize text fields
		teacher = new JTextField("Name", 10);
		teacher.addFocusListener(this);
		loc = new JTextField("Building, Room", 10);
		loc.addFocusListener(this);
		start = new JTextField(5);
		start.setDocument(new JTextFieldLimit(6));
		start.setText("12:00p");
		start.addFocusListener(this);
		end = new JTextField(5);
		end.setDocument(new JTextFieldLimit(6));
		end.setText("12:00p");
		end.addFocusListener(this);
		
		// intialize combo box
		type = new JComboBox<String>(items);
		
		// initialize week panel
		week = new JPanel();
		week.setLayout(new GridLayout(2, 5));
		
		// initialize days
		days = new ArrayList<>();
		for (int i = 2; i <= 6; i++) {
			Calendar today = Calendar.getInstance();
			today.set(Calendar.DAY_OF_WEEK, i);
			SimpleDateFormat df = new SimpleDateFormat("EE");
			days.add(new JLabel(df.format(today.getTime()), JLabel.CENTER));
		}
		
		// initialize select
		select = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			select.add(new JCheckBox());
			select.get(i).setFocusable(false);
		}
		
		// initialize enable checkbox
		ImageIcon enableIcon = new ImageIcon(directory + "enabled.png");
		ImageIcon disableIcon = new ImageIcon(GrayFilter.createDisabledImage(enableIcon.getImage()));
		enable = new JCheckBox(disableIcon);
		enable.setSelectedIcon(enableIcon);
		enable.setSelected(true);
		enable.setFocusable(false);
		enable.setToolTipText("Disable this section");
		EnableListener enabler = new EnableListener();
		enable.addItemListener(enabler);
		enable.addMouseListener(enabler);
		
		// initialize close button
		ImageIcon x = new ImageIcon(directory + "close.png");
		close = new JButton(x);
		close.setToolTipText("Remove this section");
		close.addMouseListener(enabler);
		close.setPreferredSize(new Dimension(20, 20));
		close.setFocusable(false);
		close.setOpaque(false);
		close.setContentAreaFilled(false);
		close.setBorderPainted(false);
		
		// set layout of panel
		setLayout(new FlowLayout());
		
		// add days and select to week
		for (JLabel day : days) {
			week.add(day);
		}
		for (JCheckBox ck : select) {
			week.add(ck);
		}
		
		// add components to panel
		add(enable);
		add(Box.createHorizontalStrut(10));
		add(teacherLabel);
		add(teacher);
		add(locLabel);
		add(loc);
		add(typeLabel);
		add(type);
		add(startLabel);
		add(start);
		add(endLabel);
		add(end);
		add(week);
		add(close);
	}
	
	public String[] toStringArray() {
		String[] s = new String[10];
		s[0] = teacher.getText().trim();
		s[1] = loc.getText().trim();
		s[2] = items[type.getSelectedIndex()];
		s[3] = start.getText().trim();
		s[4] = end.getText().trim();
		int size = select.size();
		for (int i = 0; i < size; i++) {
			s[i + 5] = "" + select.get(i).isSelected();
		}
		return s;
	}
	
	public boolean checkValidity() {
		// if times are incorrect format, then invalid
		String start = this.start.getText().trim();
		String end = this.end.getText().trim();
		String[] startArray = start.split(":");
		String[] endArray = end.split(":");
		if (startArray.length <= 1 || endArray.length <= 1) return false;
		try {
			int hrs = Integer.parseInt(startArray[0]);
			if (hrs <= 0 || hrs > 12) return false;
			hrs = Integer.parseInt(endArray[0]);
			if (hrs <= 0 || hrs > 12) return false;
			int mins = Integer.parseInt(startArray[1].substring(0, 2));
			if (mins < 0 || mins >= 60) return false;
			mins = Integer.parseInt(endArray[1].substring(0, 2));
			if (mins < 0 || mins >= 60) return false;
		} catch (NumberFormatException ex) {
			return false;
		}
		char s = startArray[1].charAt(startArray[1].length() - 1);
		char e = endArray[1].charAt(endArray[1].length() - 1);
		if (s != 'a' && s != 'A' && s != 'p' && s != 'P') return false;
		if (e != 'a' && e != 'A' && e != 'p' && e != 'P') return false;
		int startTime = ClassPanel.convertTime(start);
		int endTime = ClassPanel.convertTime(end);
		if (startTime == endTime) return false;
		if (startTime < 480 || startTime > 1320 || endTime < 480 || endTime > 1320) return false;
		
		// if all checks fall through, then valid
		return true;
	}

	public boolean enabled() {
		return enable.isSelected();
	}
	
	public JButton getClose() {
		return close;
	}
	
	public void changeState(boolean val) {
		teacher.setEnabled(val);
		loc.setEnabled(val);
		type.setEnabled(val);
		start.setEnabled(val);
		end.setEnabled(val);
		for (JCheckBox cb : select) {
			cb.setEnabled(val);
		}
		if (val) {
			enable.setToolTipText("Disable this section");
		} else {
			enable.setToolTipText("Enable this section");
		}
	}
	
	@Override
	public void focusGained(FocusEvent e) {
		((JTextField)e.getSource()).selectAll();
	}

	@Override
	public void focusLost(FocusEvent e) {
		// do nothing+
	}
	
	protected class JTextFieldLimit extends PlainDocument {
		private static final long serialVersionUID = 1L;
		private int limit;

		JTextFieldLimit(int limit) {
			super();
			this.limit = limit;
		}

		public void insertString( int offset, String  str, AttributeSet attr ) throws BadLocationException {
			if (str == null) return;

		    if ((getLength() + str.length()) <= limit) {
		    	super.insertString(offset, str, attr);
		    }
		}
	}
	
	protected class EnableListener extends MouseAdapter implements ItemListener {
		@Override
		public void mouseEntered(MouseEvent ev) {
			setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
		
		@Override
		public void mouseExited(MouseEvent ev) {
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
		
		@Override
		public void itemStateChanged(ItemEvent ev) {
			if (ev.getStateChange() == ItemEvent.SELECTED) {
				changeState(true);
			} else if (ev.getStateChange() == ItemEvent.DESELECTED) {
				changeState(false);
			} else {
				System.err.println("Error with class field item listener!");
			}
		}
	}

}