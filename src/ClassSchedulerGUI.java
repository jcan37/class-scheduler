import java.awt.EventQueue;
import java.awt.Font;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

/**
 * window of application
 * 
 * @author Jeffrey Cannon and Alexa Cashetta
 * @date 06/06/13
 */
public class ClassSchedulerGUI extends JFrame {

	// ------ fields ------
	private static final long serialVersionUID = 1L;
	private ClassSchedulerContainer container;
	private String directory;
	// private JScrollPane scroll;
	
	public ClassSchedulerGUI() {
		// call super class's constructor
		super("Class Scheduler");
		
		// set directory
		directory = System.getProperty("user.home") + File.separator + "JA App" + File.separator;
		
		// set default close operation
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ImageIcon icon = new ImageIcon(directory + "frame.png");
		setIconImage(icon.getImage());
		setDefaultLookAndFeelDecorated(true);
		
		// build window
		container = new ClassSchedulerContainer();
		/*
		scroll = new JScrollPane(container);
		scroll.getHorizontalScrollBar().setUnitIncrement(20);
		scroll.getHorizontalScrollBar().setBlockIncrement(100);
		scroll.getVerticalScrollBar().setUnitIncrement(20);
		scroll.getVerticalScrollBar().setBlockIncrement(100);
		*/
		add(container);
		
		// set size, location and display window
		setSize(1100, 500);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	/*
	protected class AcceleratorKey extends KeyAdapter {
		protected KeyStroke key;
		protected JButton action;
		
		protected AcceleratorKey(KeyStroke key, JButton action) {
			this.key = key;
			this.action = action;
		}

		@Override
		public void keyTyped(KeyEvent ev) {
			if (key.getKeyCode() == ev.getKeyCode()) {
				action.doClick();
			}
		}
		
	}
	*/
	/**
	 * starts application
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		Runnable runner = new Runnable() {
			@Override
			public void run() {
				NimbusLookAndFeel laf = new NimbusLookAndFeel();
				
				try {
					UIManager.setLookAndFeel(laf);
					// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception e) {
					// default look and feel
				}
				
				// set default font
				if (laf != null) {
					laf.getDefaults().put("defaultFont", new Font("Tahoma", Font.PLAIN, 12));
				}
				
				// specify tool tip info
				ToolTipManager.sharedInstance().setInitialDelay(250);
				ToolTipManager.sharedInstance().setDismissDelay(10000);
				ToolTipManager.sharedInstance().setReshowDelay(250);
				
				// initialize application
				new ClassSchedulerGUI();
			}
		};
		EventQueue.invokeLater(runner);
	}
	
	/**
	 * main function for testing purposes
	 * 
	 * @param args
	 */
	/*
	public static void main(String[] args) {
		Runnable runner = new Runnable() {
			@Override
			public void run() {
				NimbusLookAndFeel laf = new NimbusLookAndFeel();
				
				try {
					UIManager.setLookAndFeel(laf);
					// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception e) {
					// default look and feel
				}
				
				// set default font
				if (laf != null) {
					laf.getDefaults().put("defaultFont", new Font("Tahoma", Font.PLAIN, 12));
				}
				
				// specify tool tip info
				ToolTipManager.sharedInstance().setInitialDelay(250);
				ToolTipManager.sharedInstance().setDismissDelay(10000);
				ToolTipManager.sharedInstance().setReshowDelay(250);
				
				// initialize application
				final ClassSchedulerGUI gui = new ClassSchedulerGUI();
				new Thread(new Runnable() {
					final int DELAY = 200;
					@Override
					public void run() {
						for (int i = 0; i < 6; i++) {
							try {
								Thread.sleep(DELAY);
							} catch (InterruptedException ex) {}
							Runnable r = new Runnable() {
								@Override
								public void run() {
									gui.container.classes.add.doClick();
								}
							};
							EventQueue.invokeLater(r);
						}
						final String[] classes = {"CS 2110", "CS 2102", "CS 2330", "APMA 3080", "PHYS 2415", "PHYS 2419"};
						for (int i = 0; i < gui.container.classes.classes.size(); i++) {
							final int tmp = i;
							try {
								Thread.sleep(DELAY);
							} catch (InterruptedException ex) {};
							Runnable head = new Runnable() {
								@Override
								public void run() {
									gui.container.classes.setSelectedIndex(tmp);
									gui.container.classes.classNames.get(tmp).setText(classes[tmp]);
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
										gui.container.classes.classes.get(tmp).add.doClick();
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
								System.out.println("DIAGNOSTIC: time list");
								for (int i = 0; i < numTimes; i++) {
									System.out.println(start[i] + " to " + end[i]);
								}
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
							}
						}).start();
						// sleep then add random classes to application
						try {
							Thread.sleep(2 * DELAY);
						} catch (InterruptedException ex) {}
						for (int i = 0; i < gui.container.classes.classes.size(); i++) {
							final int tmpClass = i;
							Runnable head = new Runnable() {
								@Override
								public void run() {
									gui.container.classes.setSelectedIndex(tmpClass);
								}
							};
							EventQueue.invokeLater(head);
							for (int j = 0; j < gui.container.classes.classes.get(tmpClass).fields.size(); j++) {
								final int tmpSection = j;
								try {
									Thread.sleep(DELAY);
								} catch (InterruptedException ex) {}
								Runnable r = new Runnable() {
									@Override
									public void run() {
										int count = tmpClass * 3 + tmpSection;
										gui.container.classes.classes.get(tmpClass).fields.get(tmpSection).teacher.setText(info.get(count)[0]);
										gui.container.classes.classes.get(tmpClass).fields.get(tmpSection).loc.setText(info.get(count)[1]);
										gui.container.classes.classes.get(tmpClass).fields.get(tmpSection).start.setText(info.get(count)[2]);
										gui.container.classes.classes.get(tmpClass).fields.get(tmpSection).end.setText(info.get(count)[3]);
										for (int k = 0; k < days.get(count).length; k++) {
											gui.container.classes.classes.get(tmpClass).fields.get(tmpSection).select.get(k).setSelected(days.get(count)[k]);
										}
									}
								};
								EventQueue.invokeLater(r);
							}
						}
					}
				}).start();
			}
		};
		EventQueue.invokeLater(runner);
	}
	*/
}
