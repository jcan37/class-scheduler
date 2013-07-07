import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/**
 * Adds day header to day panel info
 * 
 * @author Jeffrey Cannon and Alexa Cashetta
 * @date 06/19/13
 */
public class DayPanel extends JPanel {

	// ---------- fields ----------
	private static final long serialVersionUID = 1L;
	protected DayPanelInfo schedule;
	protected JPanel header;
	protected JLabel day;
	protected final int FILLER;
	protected final int WIDTH;
	
	public DayPanel(ArrayList<String[]> classes, int day) {
		// initialize numeric fields
		FILLER = 15;
		WIDTH = 150;
		
		// initialize other fields
		schedule = new DayPanelInfo(classes);
		this.day = new JLabel(convertDay(day), JLabel.CENTER);
		this.day.setAlignmentX(CENTER_ALIGNMENT);
		this.day.setFont(new Font("Tahoma", Font.BOLD, 14));
		this.day.setForeground(Color.white);
		header = new JPanel();
		header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
		header.setBackground(new Color(8, 37, 103));
		header.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(255, 97, 3), 1), BorderFactory.createBevelBorder(BevelBorder.LOWERED)));
		
		// set layout
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// add components
		header.add(Box.createRigidArea(new Dimension(WIDTH, FILLER)));
		header.add(this.day);
		header.add(Box.createRigidArea(new Dimension(WIDTH, FILLER)));
		add(header);
		add(schedule);
	}
	
	public static String convertDay(int d) {
		switch(d) {
			case 1: return "Tue";
			case 2: return "Wed";
			case 3: return "Thu";
			case 4: return "Fri";
			default: return "Mon";
		}
	}
	
}
