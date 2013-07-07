import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Handles a single course
 * 
 * @author Jeffrey Cannon and Alexa Cashetta
 * @date 06/03/13
 */
public class ClassContent extends JPanel implements ActionListener {
	
	// ------ fields ------
	private static final long serialVersionUID = 1L;
	protected ArrayList<ClassField> fields;
	protected JPanel fieldPanel;
	protected JPanel addPanel;
	protected JButton add;
	protected ArrayList<JButton> close;
	protected JScrollPane scroll;
	protected final int FIELD_HEIGHT;
	protected int panelSize;
	protected String directory;
	
	public ClassContent() {
		// initialize directory
		directory = System.getProperty("user.home") + File.separator + "JA App" + File.separator;
				
		// initialize numeric fields
		FIELD_HEIGHT = 50;
		panelSize = 650;
		
		// initialize class field array
		fields = new ArrayList<>();
		fields.add(new ClassField());
		
		// initialize panels
		fieldPanel = new JPanel();
		fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
		addPanel = new JPanel();
		addPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		// initialize buttons
		ImageIcon addIcon = new ImageIcon(directory + "add.png");
		add = new JButton("Add Section", addIcon);
		add.addActionListener(this);
		add.setPreferredSize(new Dimension(120, 30));
		add.setFocusable(false);
		close = new ArrayList<>();
		close.add(fields.get(0).getClose());
		close.get(0).addActionListener(this);
		
		// set layout of panel
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(1000, 300));
		setBorder(BorderFactory.createEtchedBorder(Color.black, Color.darkGray));
		setFocusCycleRoot(true);
		
		// set scroll pane
		scroll = new JScrollPane(fieldPanel);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.getVerticalScrollBar().setUnitIncrement(20);
		scroll.getVerticalScrollBar().setBlockIncrement(100);
		
		// add components
		fieldPanel.add(fields.get(0));
		fieldPanel.add(Box.createRigidArea(new Dimension(600, panelSize - fields.size() * FIELD_HEIGHT)));
		add(scroll, BorderLayout.CENTER);
		addPanel.add(add);
		add(addPanel, BorderLayout.SOUTH);
	}
	
	/*
	public int getNumSections() {
		if (fields.size() == 0) {
			return 1;
		} else {
			return fields.size();
		}
	}
	*/
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == add) {
			fields.add(new ClassField());
			for (JButton b : close) {
				b.removeActionListener(this);
			}
			close.clear();
			for (ClassField cf : fields) {
				close.add(cf.getClose());
			}
			for (JButton b : close) {
				b.addActionListener(this);
			}
		}
		for (int i = 0; i < close.size(); i++) {
			if (e.getSource() == close.get(i)) {
				if (fields.size() > 1) { 
					close.remove(i);
					fields.remove(i);
				} else {
					ImageIcon alert = new ImageIcon(directory + "alert.png");
					JOptionPane.showMessageDialog(this.getTopLevelAncestor(), "You must have atleast one class section!", "Alert", JOptionPane.ERROR_MESSAGE, alert);
				}
				break;
			}
		}
		fieldPanel.removeAll();
		if (!fields.isEmpty()) {
			for (ClassField cf : fields) {
				fieldPanel.add(cf);
			}
			if (fields.size() < panelSize / FIELD_HEIGHT) {
				fieldPanel.add(Box.createRigidArea(new Dimension(600, panelSize - fields.size() * FIELD_HEIGHT)));
			}
		} else {
			fieldPanel.add(Box.createRigidArea(new Dimension(600, panelSize - fields.size() * FIELD_HEIGHT)));
		}
		fieldPanel.revalidate();
		fieldPanel.repaint();
	}
	
}
