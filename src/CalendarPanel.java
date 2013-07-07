import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.GrayFilter;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

/**
 * Creates a single calendar
 * 
 * @author Jeffrey Cannon and Alexa Cashetta
 * @date 06/07/13
 */
public class CalendarPanel extends JPanel {

	// ------ fields ------
	private static final long serialVersionUID = 1L;
	protected ArrayList<DayPanel> dayPanels = new ArrayList<>();
	protected ArrayList<boolean[]> days = new ArrayList<>();
	protected ArrayList<String[]> classes = new ArrayList<>();
	protected TimePanel time;
	protected JPanel left, corner;
	protected JCheckBox narrow;
	protected JButton remove, info;
	protected JPopupMenu infoMenu;
	protected ArrayList<JMenuItem> classInfo = new ArrayList<>();
	protected String directory;

	// example constructor input:
	// {{"APMA 2130", "Gianluca Guadagni", "Thornton Hall, E303", "Lecture", "11:00a", "11:50a", "true", "false", "true", "false", "true"},
	// {"CS 1110", "Mark Sherriff", "Olsson Hall, 120", "Lecture", "2:00p", "3:15p", "true", "false", "true", "false", "false"},...}
	public CalendarPanel(ArrayList<String[]> classes) {
		// set directory for icons
		directory = System.getProperty("user.home") + File.separator + "JA App" + File.separator;
		
		// initialize fields
		for (int i = 0; i < classes.size(); i++) {
			days.add(new boolean[5]);
			this.classes.add(new String[6]);
			for (int j = 0; j < classes.get(i).length; j++) {
				if (j < 6) {
					this.classes.get(i)[j] = classes.get(i)[j];
				} else {
					days.get(i)[j - 6] = convertBoolean(classes.get(i)[j]);
				}
			}			
		}
		
		// initialize day panels
		ArrayList<String[]> tmp = null;
		for (int i = 0; i < 5; i++) {
			tmp = new ArrayList<>();
			for (int j = 0; j < this.classes.size(); j++) {
				if (days.get(j)[i]) {
					tmp.add(this.classes.get(j));
				}
			}
			dayPanels.add(new DayPanel(tmp, i));
		}
		
		// initialize menu
		infoMenu = new JPopupMenu();
		JMenuItem detail = null;
		String[] days = new String[5];
		for (String[] s : classes) {
			detail = new JMenuItem(s[0] + " " + s[3]);
			classInfo.add(detail);
			for (int i = 0; i < 5; i++) {
				days[i] = s[i + 6];
			}
			detail.setToolTipText("<html>Instructor: " + s[1] + "<br>Location: " + s[2] + "<br>Time: " + s[4] + " - " + s[5] + "<br>Days: " + convertDays(days));
		}
		for (JMenuItem item : classInfo) {
			infoMenu.add(item);
		}
		
		// initialize left panel
		left = new JPanel();
		corner = new JPanel() {
			private static final long serialVersionUID = 1L;
			@Override
			public Insets getInsets() {
				int x = 8;
				return new Insets(x, x, x, x);
			}
		};
		left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
		corner.setLayout(new BoxLayout(corner, BoxLayout.X_AXIS));
		ButtonListener btnAction = new ButtonListener();
		remove = new JButton(new ImageIcon(directory + "x.png")) {
			private static final long serialVersionUID = 1L;
			Dimension d = new Dimension(30, 30);
			@Override
			public Dimension getPreferredSize() {
				return d;
			}
			@Override
			public Dimension getMaximumSize() {
				return d;
			}
		};
		remove.setToolTipText("Fuck this schedule");
		remove.addMouseListener(btnAction);
		remove.setAlignmentX(CENTER_ALIGNMENT);
		remove.setBorderPainted(false);
		remove.setContentAreaFilled(false);
		info = new JButton(new ImageIcon(directory + "info-icon.png")) {
			private static final long serialVersionUID = 1L;
			Dimension d = new Dimension(30, 30);
			@Override
			public Dimension getPreferredSize() {
				return d;
			}
			@Override
			public Dimension getMaximumSize() {
				return d;
			}
		};
		info.setAlignmentX(CENTER_ALIGNMENT);
		info.setBorderPainted(false);
		info.setContentAreaFilled(false);
		info.setToolTipText("Schedule details");
		info.addMouseListener(btnAction);
		ImageIcon check = new ImageIcon(directory + "magnify.png");
		ImageIcon uncheck = new ImageIcon(GrayFilter.createDisabledImage(check.getImage()));
		narrow = new JCheckBox(uncheck);
		narrow.setSelectedIcon(check);
		narrow.setToolTipText("Select for further review");
		narrow.addMouseListener(btnAction);
		narrow.setAlignmentX(CENTER_ALIGNMENT);
		corner.add(remove);
		corner.add(info);
		corner.add(narrow);
		left.add(corner);
		time = new TimePanel();
		left.add(time);
		
		// set layout and add listener to panel
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		addMouseMotionListener(btnAction);
		
		// add components
		add(left);
		for (DayPanel d : dayPanels) {
			add(d);
		}
	}
	
	public static boolean convertBoolean(String s) {
		switch(s) {
		case "true":
		case "TRUE":
		case "t":
		case "T":
			return true;
		default:
			return false;
		}
	}
	
	public static String convertDays(String[] days) {
		String dayString = "";
		for (int i = 0; i < days.length; i++) {
			if (convertBoolean(days[i])) {
				dayString += DayPanel.convertDay(i);
			}
		}
		return dayString;
	}
	
	protected class ButtonListener implements MouseListener, MouseMotionListener {

		@Override
		public void mouseClicked(MouseEvent ev) {
			// nothing
		}

		@Override
		public void mouseEntered(MouseEvent ev) {
			setCursor(new Cursor(Cursor.HAND_CURSOR));
		}

		@Override
		public void mouseExited(MouseEvent ev) {
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}

		@Override
		public void mousePressed(MouseEvent ev) {
			// nothing
		}

		@Override
		public void mouseReleased(MouseEvent ev) {
			if (ev.getSource() == info) {
				infoMenu.show(ev.getComponent(), ev.getX(), ev.getY());
			}
		}
		
		// Mouse motion events
		
		@Override
		public void mouseDragged(MouseEvent ev) {
			// System.out.println(ev.getX() + ", " + ev.getY());
		}

		@Override
		public void mouseMoved(MouseEvent ev) {
			int x1 = ev.getX();
			int y1 = ev.getY();
			int x2 = infoMenu.getX();
			int y2 = infoMenu.getY();
			int h = infoMenu.getSize().height;
			int w = infoMenu.getSize().width;
			if ((x1 < x2 && y1 < y2 || x1 > w + x2 && y1 > h + y2 || x1 < x2 && y1 > h + y2 || x1 > w + x2 && y1 < y2) && infoMenu.isShowing()) {
				infoMenu.setVisible(false);
				// System.out.println("Info menu set invisible.");
			}
		}
		
	}
	/*
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(new NimbusLookAndFeel());
			// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {}
		ToolTipManager.sharedInstance().setInitialDelay(250);
		ToolTipManager.sharedInstance().setDismissDelay(10000);
		ToolTipManager.sharedInstance().setReshowDelay(250);
		JFrame w = new JFrame();
		w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		String[] details1 = {"APMA 3080", "Monika Abramenko", "Olsson Hall, 011", "Lecture", "12:00p", "12:50p", "true", "false", "true", "false", "true"};
		String[] details2 = {"CS 2102", "Hamed Soroush", "Rice Hall, 130", "Lecture", "2:00p", "3:15p", "false", "true", "false", "true", "false"};
		String[] details3 = {"CS 2110", "Luther Tychonievich", "Rice Hall, 130", "Lecture", "10:00a", "10:50a", "true", "false", "true", "false", "true"};
		String[] details4 = {"CS 2110", "Staff", "Rice Hall, 130", "Laboratory", "5:00p", "6:45p", "true", "false", "false", "false", "false"};
		String[] details5 = {"CS 2330", "Joanne Dugan", "Rice Hall, 130", "Lecture", "9:00a", "9:50a", "true", "false", "true", "false", "true"};
		String[] details6 = {"CS 2330", "Joanne Dugan", "Rice Hall, 130", "Discussion", "5:30p", "7:00p", "false", "false", "false", "true", "false"};
		String[] details7 = {"CS 2330", "Joanne Dugan", "Rice Hall, 240", "Laboratory", "4:00p", "6:00p", "false", "true", "false", "false", "false"};
		String[] details8 = {"PHYS 2415", "John Pribram", "Physics Bldg, 203", "Lecture", "11:00a", "11:50a", "true", "false", "true", "false", "true"};
		String[] details9 = {"PHYS 2419", "Maxim Bychkov", "Physics Bldg, 215", "Laboratory", "4:00p", "5:50p", "false", "false", "true", "false", "false"};
		ArrayList<String[]> classes = new ArrayList<>();
		classes.add(details1);
		classes.add(details2);
		classes.add(details3);
		classes.add(details4);
		classes.add(details5);
		classes.add(details6);
		classes.add(details7);
		classes.add(details8);
		classes.add(details9);
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(Box.createRigidArea(new Dimension(800, 20)));
		for (int i = 0; i < 10; i++) {
			panel.add(new CalendarPanel(classes));
			panel.add(Box.createRigidArea(new Dimension(800, 20)));
		}
		JScrollPane scroll = new JScrollPane(panel);
		scroll.getVerticalScrollBar().setUnitIncrement(50);
		w.add(scroll);
		w.setSize(1000, 700);
		w.setLocationRelativeTo(null);
		w.setVisible(true);
	}
	*/
}
