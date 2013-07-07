import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

/**
 * Handles all classes in tabular form
 * 
 * @author Jeffrey Cannon and Alexa Cashetta
 * @date 06/04/13
 */
public class ClassTabs extends JTabbedPane implements ActionListener, FocusListener {

	// ------ fields ------
	private static final long serialVersionUID = 1L;
	protected JPanel welcome;
	protected JLabel greeting;
	protected JLabel instructions;
	protected JButton demo;
	protected JButton add;
	protected ArrayList<ClassContent> classes;
	protected ArrayList<JTextField> classNames;
	protected ArrayList<JButton> closeTab;
	protected String directory;
	
	public ClassTabs() {
		// call super class's constructor first
		super(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
		
		// initialize directory
		directory = System.getProperty("user.home") + File.separator + "JA App" + File.separator;
		
		// initialize fields
		welcome = new JPanel();
		welcome.setLayout(new BoxLayout(welcome, BoxLayout.Y_AXIS));
		welcome.setPreferredSize(new Dimension(650, 300));
		welcome.setBorder(BorderFactory.createEtchedBorder(Color.black, Color.darkGray));
		greeting = new JLabel("Welcome, bitch!", JLabel.CENTER);
		greeting.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		Font bold = new Font("Tahoma", Font.BOLD, 20);
		Font plain = new Font("Tahoma", Font.PLAIN, 20);
		greeting.setFont(bold);
		instructions = new JLabel("Click on the new tab button to add a class.", JLabel.CENTER);
		instructions.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		instructions.setFont(plain);
		ImageIcon demoIcon = new ImageIcon(directory + "demo.png");
		demo = new JButton("Watch a Demonstration", demoIcon);
		demo.setAlignmentX(CENTER_ALIGNMENT);
		ImageIcon plus = new ImageIcon(directory + "add.png");
		add = new JButton(plus);
		add.setPreferredSize(new Dimension(20, 20));
		add.setOpaque(false);
		add.setContentAreaFilled(false);
		add.setBorderPainted(false);
		add.setFocusable(false);
		add.setToolTipText("Add a class");
		add.addActionListener(this);
		add.addMouseListener(new CursorChanger());
		classes = new ArrayList<>();
		classNames = new ArrayList<>();
		closeTab = new ArrayList<>();
		
		// add components to welcome panel
		welcome.add(Box.createRigidArea(new Dimension(100, 100)));
		welcome.add(greeting);
		welcome.add(Box.createRigidArea(new Dimension(10, 25)));
		welcome.add(instructions);
		welcome.add(Box.createRigidArea(new Dimension(10, 25)));
		welcome.add(demo);
		
		// add welcome tab
		addTab("Welcome!", welcome);
		addTab(null, new JPanel());
		setTabComponentAt(1, add);
		setEnabledAt(1, false);
	}
	/*
	public int getPossSchedules() {
		int n = 1;
		if (classes.size() == 0) {
			return 0;
		}
		for (ClassContent cc : classes) {
			n *= cc.getNumSections();
		}
		return n;
	}
	*/	
	@Override
	public void actionPerformed(ActionEvent e) {
		removeAll();
		if (e.getSource() == add) {
			ClassContent c = new ClassContent();
			c.add.addMouseListener(new CursorChanger());
			classes.add(c);
			JTextField text = new JTextField("Class Name", 8);
			text.addFocusListener(this);
			classNames.add(text);
			ImageIcon close = new ImageIcon(directory + "close.png");
			JButton x = new JButton(close);
			x.setPreferredSize(new Dimension(20, 20));
			x.setOpaque(false);
			x.setContentAreaFilled(false);
			x.setBorderPainted(false);
			x.setFocusable(false);
			x.setToolTipText("Remove this class");
			x.addActionListener(this);
			x.addMouseListener(new CursorChanger());
			closeTab.add(x);
			if (!ClassSchedulerContainer.schedule.isEnabled()) {
				ClassSchedulerContainer.schedule.setEnabled(true);
			}
		}
		for (int i = 0; i < closeTab.size(); i++) {
			if (e.getSource() == closeTab.get(i)) {
				if (classes.size() > 1) {
					classes.remove(i);
					classNames.remove(i);
					closeTab.get(i).removeActionListener(this);
					closeTab.remove(i);
				} else {
					ImageIcon alert = new ImageIcon(directory + "alert.png");
					JOptionPane.showMessageDialog(this.getTopLevelAncestor(), "You must have atleast one class!", "Alert", JOptionPane.ERROR_MESSAGE, alert);
				}
				break;
			}
		}
		for (int i = 0; i < classes.size(); i++) {
			addTab(null, classes.get(i));
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
			panel.setOpaque(false);
			panel.add(classNames.get(i));
			panel.add(closeTab.get(i));
			if (classes.size() <= 1) {
				closeTab.get(i).setEnabled(false);
				if (closeTab.get(i).getToolTipText() != null) {
					closeTab.get(i).setToolTipText(null);
				}
			} else {
				closeTab.get(i).setEnabled(true);
				if (closeTab.get(i).getToolTipText() == null) {
					closeTab.get(i).setToolTipText("Remove this class");
				}
			}
			setTabComponentAt(i, panel);
		}
		addTab(null, new JPanel());
		setTabComponentAt(classes.size(), add);
		setEnabledAt(classes.size(), false);
		if (classes.size() > 0) {
			classNames.get(classes.size() - 1).requestFocus();
		}
		revalidate();
		repaint();
	}
	
	public ArrayList<ArrayList<String[]>> toStringArray() {
		ImageIcon fish = new ImageIcon(directory + "fish.png");
		ArrayList<ArrayList<String[]>> os = new ArrayList<>();
		String className = "";
		String[] field = null;
		String[] origField = null;
		ArrayList<String> exceptions = new ArrayList<>();
		for (int i = 0; i < classes.size(); i++) {
			className = classNames.get(i).getText().trim();
			ArrayList<String[]> is = new ArrayList<>();
			for (int j = 0; j < classes.get(i).fields.size(); j++) {
				if (classes.get(i).fields.get(j).enabled() && classes.get(i).fields.get(j).checkValidity()) {
					field = new String[11];
					field[0] = className;
					origField = classes.get(i).fields.get(j).toStringArray();
					for (int k = 0; k < origField.length; k++) {
						field[k + 1] = origField[k];
					}
					is.add(field);
				} else if (classes.get(i).fields.get(j).enabled() && !classes.get(i).fields.get(j).checkValidity()) {
					// System.err.println("Something was filled wrong");
					exceptions.add(className + " section " + (j + 1));
					// JOptionPane.showMessageDialog(null, "Something's fishy about the time format for " + className + "\nsection " + (j + 1) + "... I'll just skip that section for now...", "Alert", JOptionPane.ERROR_MESSAGE, fish);
				}
			}
			ArrayList<ArrayList<String[]>> type = new ArrayList<>();
			int count = 0;
			while (count < ClassField.items.length) {
				type.add(new ArrayList<String[]>());
				count++;
			}
			for (int j = 0; j < type.size(); j++) {
				for (int k = 0; k < is.size(); k++) {
					if (is.get(k)[3].equals(ClassField.items[j])) {
						type.get(j).add(is.get(k));
					}
				}
			}
			for (ArrayList<String[]> iswt : type) {
				if (iswt.size() > 0) {
					os.add(iswt);
				}
			}
		}
		// show error message if there are exceptions
		if (exceptions.size() > 0) {
			final int errSize;
			if (exceptions.size() > 10) {
				errSize = 10;
			} else {
				errSize = exceptions.size();
			}
			JPanel errPanel = new JPanel() {
				private static final long serialVersionUID = 1L;
				@Override
				public Insets getInsets() {
					int x = 10;
					return new Insets(x, x, x, x);
				}
			};
			errPanel.setLayout(new BoxLayout(errPanel, BoxLayout.Y_AXIS));
			errPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
			errPanel.setBackground(Color.white);
			JLabel errHead = new JLabel("", JLabel.CENTER), errList = new JLabel("", JLabel.CENTER), errFoot = new JLabel("", JLabel.CENTER);
			errHead.setText("Something's fishy about the time format for the following sections:");
			errHead.setAlignmentX(CENTER_ALIGNMENT);
			String errBody = "<html>";
			for (String s : exceptions) {
				errBody = errBody + s + "<br>";
			}
			errList.setText(errBody);
			errList.setAlignmentX(CENTER_ALIGNMENT);
			errFoot.setText("I'll just skip those sections for now...");
			errFoot.setAlignmentX(CENTER_ALIGNMENT);
			errPanel.add(errHead);
			errPanel.add(Box.createVerticalStrut(10));
			errPanel.add(errList);
			errPanel.add(Box.createVerticalStrut(10));
			errPanel.add(errFoot);
			JScrollPane errScroll = new JScrollPane(errPanel) {
				private static final long serialVersionUID = 1L;
				private Dimension d = new Dimension(425, 80 + errSize * 15);
				@Override
				public Dimension getPreferredSize() {
					return d;
				}
				@Override
				public Dimension getMaximumSize() {
					return d;
				}
			};
			JOptionPane.showMessageDialog(this.getTopLevelAncestor(), errScroll, "Alert", JOptionPane.ERROR_MESSAGE, fish);
		}
		return os;
	}
	
	@Override
	public Insets getInsets() {
		return new Insets(0, 0, 0, 0);
	}

	@Override
	public void focusGained(FocusEvent ev) {
		for (int i = 0; i < classNames.size(); i++) {
			if (ev.getSource() == classNames.get(i)) {
				classNames.get(i).selectAll();
				setSelectedIndex(i);
				break;
			}
		}
	}

	@Override
	public void focusLost(FocusEvent ev) {
		// do nothing
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
