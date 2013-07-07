import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ProgressMonitor;

/**
 * Container for all elements in application
 * 
 * @author Jeffrey Cannon and Alexa Cashetta
 * @date 06/06/13
 */
public class ClassSchedulerContainer extends JPanel implements ActionListener {

	// -------- fields --------
	private static final long serialVersionUID = 1L;
	protected ClassTabs classes;
	protected JScrollPane scroll;
	protected static JButton schedule, narrow, showMore;
	// private JLabel possibleLabel;
	// private JLabel possible;
	protected JLabel generated;
	protected JPanel bottom, btnPanel, main;
	protected ArrayList<CalendarPanel> calendars = new ArrayList<>();
	protected ProgressMonitor monitor;
	protected String directory;
	protected int shown = 1;
	protected final int SHOW;
	
	public ClassSchedulerContainer() {
		// initialize directory and constants
		directory = System.getProperty("user.home") + File.separator + "JA App" + File.separator;
		SHOW = 50;
		
		// initialize components
		classes = new ClassTabs();
		classes.demo.addActionListener(this);
		classes.demo.addMouseListener(new CursorChanger());
		ImageIcon gen = new ImageIcon(directory + "generate.png");
		schedule = new JButton("Generate Schedules", gen);
		schedule.setEnabled(false);
		schedule.setFocusable(false);
		Dimension d = new Dimension(180, 40);
		schedule.setPreferredSize(d);
		schedule.addActionListener(this);
		schedule.addMouseListener(new CursorChanger());
		ImageIcon nar = new ImageIcon(directory + "narrow.png");
		narrow = new JButton("Narrow Search", nar);
		narrow.setEnabled(false);
		narrow.setFocusable(false);
		narrow.setPreferredSize(d);
		narrow.addActionListener(this);
		narrow.addMouseListener(new CursorChanger());
		// possibleLabel = new JLabel("Number of possible schedules:", JLabel.CENTER);
		// possible = new JLabel("0", JLabel.CENTER);
		ImageIcon show = new ImageIcon(directory + "show.png");
		showMore = new JButton("Show More", show);
		showMore.setFocusable(false);
		showMore.addActionListener(this);
		showMore.addMouseListener(new CursorChanger());
		showMore.setAlignmentX(CENTER_ALIGNMENT);
		showMore.setMultiClickThreshhold(1000);
		generated = new JLabel("", JLabel.CENTER);
		generated.setForeground(Color.white);
		btnPanel = new JPanel();
		bottom = new JPanel();
		main = new JPanel();
		scroll = new JScrollPane(main);
		main.add(classes);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.getVerticalScrollBar().setUnitIncrement(20);
		scroll.getVerticalScrollBar().setBlockIncrement(100);
		
		// set layout
		setLayout(new BorderLayout());
		btnPanel.setLayout(new FlowLayout());
		btnPanel.setBackground(Color.black);
		bottom.setLayout(new BorderLayout());
		bottom.setBackground(Color.black);
		main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
		
		// add components
		btnPanel.add(schedule);
		btnPanel.add(narrow);
		bottom.add(btnPanel, BorderLayout.CENTER);
		bottom.add(generated, BorderLayout.SOUTH);
		// bottom.add(possibleLabel);
		// bottom.add(possible);
		add(scroll, BorderLayout.CENTER);
		add(bottom, BorderLayout.SOUTH);
	}
	
	public void displayGenerated(int n, boolean schedule) {
		String txt = "";
		if (schedule) {
			txt = "Number of Schedules Generated: " + n;
		} else {
			txt = "Narrowed Search to: " + n;
		}
		generated.setText(txt);
	}
	
	public int numSchedules(ArrayList<ArrayList<String[]>> s) {
		if (s.size() == 0) return 0;
		int total = 1;
		for (int i = 0; i < s.size(); i++) {
			total *= s.get(i).size();
		}
		return total;
	}
	
	public ArrayList<ArrayList<String[]>> schedule(ArrayList<ArrayList<String[]>> s) {
		int n = numSchedules(s);
		ArrayList<ArrayList<String[]>> possibleSchedules = new ArrayList<ArrayList<String[]>>();
		for (int i = 0; i < n; i++) {
			possibleSchedules.add(new ArrayList<String[]>());
		}
		
		int[] sigBits = new int[s.size()];
		// sigBits array holds how many combinations precede each column of
		// possibleSchedules
		for (int i = 0; i < s.size(); i++) {
			sigBits[i] = 1;
			for (int j = i - 1; j >= 0; j--) {
				sigBits[i] *= s.get(j).size();
			}
		}
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < s.size(); j++) {
				possibleSchedules.get(i).add(s.get(j).get((i / sigBits[j]) % s.get(j).size()));
			}
		}
		
		return possibleSchedules;
	}
	
	private static void checkAllForConflicts(ArrayList<ArrayList<String[]>> scheduleOptions) {
		// Fill in the code here!
		int[] conflicts = new int[scheduleOptions.size()];
		int count = 0;
		for (int i = 0; i < scheduleOptions.size(); i++) {
			if (checkForConflicts(scheduleOptions.get(i))) {
				conflicts[count] = i;
				count++;
			}
		}

		for (int i = 0; i < count; i++) {
			scheduleOptions.remove(conflicts[i]);
			for (int j = 0; j < count; j++) {
				conflicts[j]--;
			}
		}
	}
	
	private static boolean checkForConflicts(ArrayList<String[]> singleSchedule) {
		// Fill in the code here!
		for (int i = 0; i < singleSchedule.size(); i++) {
			for (int j = 0; j < singleSchedule.size(); j++) {
				if (conflicts(singleSchedule.get(i), singleSchedule.get(j)) && (i != j)) {
					return true;
				}
			}
		}
		return false;
	}
	
	private static boolean conflicts(String[] first, String[] second) {
		int firstStart = ClassPanel.convertTime(first[4]);
		int firstEnd = ClassPanel.convertTime(first[5]);
		int secondStart = ClassPanel.convertTime(second[4]);
		int secondEnd = ClassPanel.convertTime(second[5]);
		boolean[] days1 = new boolean[5], days2 = new boolean[5];
		for (int i = 0; i < days1.length; i++) {
			days1[i] = CalendarPanel.convertBoolean(first[i + 6]);
			days2[i] = CalendarPanel.convertBoolean(second[i + 6]);
 		}
		for (int index = 0; index < days1.length; index++) {
			if (((secondStart <= firstEnd && secondStart >= firstStart) || (firstStart <= secondEnd && firstStart >= secondStart)) && (days1[index] == true && days2[index] == true)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean checkForSelection(ArrayList<CalendarPanel> cal) {
		for (CalendarPanel cp : cal) {
			if (cp.narrow.isSelected()) {
				return true;
			}
		}
		return false;
	}
	
	public static void narrowSelection(ArrayList<CalendarPanel> cal) {
		int count = 0;
		while (count < cal.size()) {
			if (!cal.get(count).narrow.isSelected()) {
				cal.remove(count);
			} else {
				count++;
			}
		}
	}
	
	public void updateDisplay(boolean restartShow) {
		if (restartShow) {
			shown = 1;
		}
		main.removeAll();
		main.add(classes);
		for (int i = 0; i < calendars.size() && i < SHOW * shown; i++) {
			main.add(Box.createVerticalStrut(30));
			main.add(calendars.get(i));
		}
		main.add(Box.createVerticalStrut(30));
		if (calendars.size() > SHOW * shown) {
			main.add(showMore);
			main.add(Box.createVerticalStrut(30));
		}
		main.revalidate();
		main.repaint();
	}
	
	@Override
	public Insets getInsets() {
		int x = 10;
		return new Insets(x, x, x, x);
	}
	
	public void demonstrate() {
		Thread demo = new Thread(new Runnable() {
			final int DELAY = 200;
			// final ImageIcon demoIcon = new ImageIcon(directory + "professor.png");
			@Override
			public void run() {
				/*
				Runnable ckPt1 = new Runnable() {
					@Override
					public void run() {
						JOptionPane.showMessageDialog(classes.getTopLevelAncestor(), "Click on the new tab button to add classes that you will be taking this semester.", "Checkpoint", JOptionPane.INFORMATION_MESSAGE, demoIcon);
					}
				};
				EventQueue.invokeLater(ckPt1);
				*/
				for (int i = 0; i < 6; i++) {
					try {
						Thread.sleep(DELAY);
					} catch (InterruptedException ex) {}
					Runnable r = new Runnable() {
						@Override
						public void run() {
							classes.add.doClick();
						}
					};
					EventQueue.invokeLater(r);
				}
				Runnable disableGen = new Runnable() {
					@Override
					public void run() {
						if (schedule.isEnabled()) {
							schedule.setEnabled(false);
						}
					}
				};
				EventQueue.invokeLater(disableGen);
				/*
				Runnable ckPt2 = new Runnable() {
					@Override
					public void run() {
						JOptionPane.showMessageDialog(classes.getTopLevelAncestor(), "<html>Write the name of the class in the text field in the specified area of each tab.<br>Within each tab, click on the \"Add Section\" button to indicate a different section of a class you're considering.", "Checkpoint", JOptionPane.INFORMATION_MESSAGE, demoIcon);
					}
				};
				EventQueue.invokeLater(ckPt2);
				*/
				final String[] classHeaders = {"CS 2110", "CS 2102", "CS 2330", "APMA 3080", "PHYS 2415", "PHYS 2419"};
				for (int i = 0; i < classes.classes.size(); i++) {
					final int tmp = i;
					try {
						Thread.sleep(DELAY);
					} catch (InterruptedException ex) {};
					Runnable head = new Runnable() {
						@Override
						public void run() {
							classes.setSelectedIndex(tmp);
							classes.classNames.get(tmp).setText(classHeaders[tmp]);
						}
					};
					EventQueue.invokeLater(head);
					for (int j = 0; j < 2; j++) {
						try {
							Thread.sleep(DELAY);
						} catch (InterruptedException ex) {}
						Runnable r = new Runnable() {
							@Override
							public void run() {
								classes.classes.get(tmp).add.doClick();
							}
						};
						EventQueue.invokeLater(r);
					}
				}
				final ArrayList<String[]> info = new ArrayList<>();
				final ArrayList<boolean[]> days = new ArrayList<>();
				final int numTimes = 18;
				final String[] start = new String[numTimes];
				final String[] end = new String[numTimes];
				new Thread(new Runnable() {
					@Override
					public void run() {
						// create start and end string arrays
						int odd = 0;
						for (int i = 0; i < numTimes; i++) {
							if (i - odd + 8 <= 11) {
								start[i] = (i - odd + 8) + ":00a";
								end[i] = (i - odd + 8) + ":50a";
								if ((i - odd + 8) % 3 == 0) {
									odd++;
									i++;
									start[i] = (i - odd + 8) + ":30a";
									end[i] = (i - odd + 9) + ":45a";
								}
							} else if (i - odd + 8 == 12) {
								start[i] = "12:00p";
								end[i] = "12:50p";
								odd++;
								i++;
								start[i] = "12:30p";
								end[i] = "1:45p";
							} else {
								start[i] = (i - odd - 4) + ":00p";
								end[i] = (i - odd - 4) + ":50p";
								if ((i - odd - 4) % 3 == 0 && i < numTimes - 1) {
									odd++;
									i++;
									start[i] = (i - odd - 4) + ":30p";
									end[i] = (i - odd - 3) + ":45p";
								}
							}
						}
						/*
						System.out.println("DIAGNOSTIC: time list");
						for (int i = 0; i < numTimes; i++) {
							System.out.println(start[i] + " to " + end[i]);
						}
						*/
						Random rand = new Random();
						for (int i = 0; i < 18; i++) {
							// create random days for classes
							days.add(new boolean[5]);
							for (int j = 0; j < 5; j++) {
								days.get(i)[j] = rand.nextBoolean();
							}
							// create random times for classes
							info.add(new String[4]);
							info.get(i)[0] = "Staff";
							info.get(i)[1] = "TBD, 001";
							int time = rand.nextInt(numTimes);
							info.get(i)[2] = start[time];
							info.get(i)[3] = end[time];
						}
						/*
						System.out.println("\nDIAGNOSTIC: random generated values");
						for (int i = 0; i < 18; i++) {
							System.out.print("<");
							for (int j = 0; j < info.get(i).length; j++) {
								System.out.print(info.get(i)[j] + ";");
							}
							for (int j = 0; j < days.get(i).length; j++) {
								System.out.print(days.get(i)[j] + ";");
							}
							System.out.println(">");
						}
						System.out.println();
						*/
					}
				}).start();
				// sleep then add random classes to application
				try {
					Thread.sleep(2 * DELAY);
				} catch (InterruptedException ex) {}
				for (int i = 0; i < classes.classes.size(); i++) {
					final int tmpClass = i;
					Runnable head = new Runnable() {
						@Override
						public void run() {
							classes.setSelectedIndex(tmpClass);
						}
					};
					EventQueue.invokeLater(head);
					for (int j = 0; j < classes.classes.get(tmpClass).fields.size(); j++) {
						final int tmpSection = j;
						try {
							Thread.sleep(DELAY);
						} catch (InterruptedException ex) {}
						Runnable r = new Runnable() {
							@Override
							public void run() {
								int count = tmpClass * 3 + tmpSection;
								classes.classes.get(tmpClass).fields.get(tmpSection).teacher.setText(info.get(count)[0]);
								classes.classes.get(tmpClass).fields.get(tmpSection).loc.setText(info.get(count)[1]);
								classes.classes.get(tmpClass).fields.get(tmpSection).start.setText(info.get(count)[2]);
								classes.classes.get(tmpClass).fields.get(tmpSection).end.setText(info.get(count)[3]);
								for (int k = 0; k < days.get(count).length; k++) {
									classes.classes.get(tmpClass).fields.get(tmpSection).select.get(k).setSelected(days.get(count)[k]);
								}
							}
						};
						EventQueue.invokeLater(r);
					}
				}
				try {
					Thread.sleep(DELAY);
				} catch (InterruptedException ex) {}
				Runnable gen = new Runnable() {
					@Override
					public void run() {
						if (!schedule.isEnabled()) {
							schedule.setEnabled(true);
						}
						schedule.doClick();
					}
				};
				EventQueue.invokeLater(gen);
			}
		});
		demo.start();
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		Object src = ev.getSource();
		if (src == schedule) {
			/*
			boolean cancel = false;
			monitor = new ProgressMonitor(this.getTopLevelAncestor(), "Generating Schedules", "Creating all possible schedules", 0, 100);
			monitor.setMillisToDecideToPopup(250);
			monitor.setMillisToPopup(500);
			long start = System.nanoTime();
			*/
			ArrayList<ArrayList<String[]>> possibilities = schedule(classes.toStringArray());
			/*
			if (monitor.isCanceled()) {
				cancel = true;
			} else {
				monitor.setProgress(1);
				monitor.setNote("Removing conflicting schedules");
			}
			long ckPt1 = System.nanoTime();
			System.out.println("Creating all possiblities: " + ((double)(ckPt1 - start) / 1e9) + " s");
			*/
			checkAllForConflicts(possibilities);
			/*
			if (monitor.isCanceled()) {
				cancel = true;
			} else {
				monitor.setProgress(20);
				monitor.setNote("Creating calendar views");
			}
			long ckPt2 = System.nanoTime();
			System.out.println("Removing conflicting schedules: " + ((double)(ckPt2 - ckPt1) / 1e9) + " s");
			*/
			displayGenerated(possibilities.size(), true);
			/*
			long ckPt3 = System.nanoTime();
			System.out.println("Determining total number of non-conflicting schedules: " + ((double)(ckPt3 - ckPt2) / 1e9) + " s");
			*/
			if (calendars.size() > 0) {
				/*
				for (int i = 0; i < calendars.size(); i++) {
					main.remove(calendars.get(i));
				}
				*/
				calendars.clear();
			}
			for (ArrayList<String[]> cal : possibilities) {
				calendars.add(new CalendarPanel(cal));
			}
			/*
			if (monitor.isCanceled()) {
				cancel = true;
			} else {
				monitor.setProgress(90);
				monitor.setNote("Displaying calendars");
			}
			long ckPt4 = System.nanoTime();
			System.out.println("Creating calendar views: " + ((double)(ckPt4 - ckPt3) / 1e9) + " s");
			*/
			if (calendars.size() > 0) {
				if (!narrow.isEnabled()) {
					narrow.setEnabled(true);
				}
			} else if (narrow.isEnabled()) {
				narrow.setEnabled(false);
			}
			for (CalendarPanel cal : calendars) {
				cal.remove.addActionListener(this);
			}
			/*
			long ckPt5 = System.nanoTime();
			System.out.println("Adding action listeners to the calendars: " + ((double)(ckPt5 - ckPt4) / 1e9) + " s");
			*/
			updateDisplay(true);
			/*
			monitor.setProgress(100);
			long end = System.nanoTime();
			System.out.println("Displaying calendars: " + ((double)(end - ckPt5) / 1e9) + " s");
			System.out.println("Total time: " + ((double)(end - start) / 1e9) + " s");
			*/
		} else if (src == narrow) {
			// first make sure at least one schedule was selected to narrow
			if (checkForSelection(calendars)) {
				// System.out.println("Narrow search");
				narrowSelection(calendars);
				// System.out.println(calendars.size());
				displayGenerated(calendars.size(), false);
				updateDisplay(true);
			} else {
				ImageIcon alert = new ImageIcon(directory + "alert.png");
				JOptionPane.showMessageDialog(null, "No calendars were selected for further review!", "Alert", JOptionPane.ERROR_MESSAGE, alert);
			}
		} else if (src == showMore) {
			shown++;
			updateDisplay(false);
		} else if (src == classes.demo) {
			demonstrate();
		} else {
			// check close button listeners
			for (int i = 0; i < calendars.size(); i++) {
				if (src == calendars.get(i).remove) {
					calendars.remove(i);
					displayGenerated(calendars.size(), false);
					updateDisplay(false);
					if (calendars.size() == 0) {
						narrow.setEnabled(false);
					}
					break;
				}
			}
		}
	}
	
	
	protected class CursorChanger extends MouseAdapter {
		@Override
		public void mouseEntered(MouseEvent ev) {
			setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
		
		@Override
		public void mouseExited(MouseEvent ev) {
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}

}
