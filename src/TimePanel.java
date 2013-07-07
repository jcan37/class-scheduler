import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/**
 * time panel of calendar
 * 
 * @author Jeffrey Cannon and Alexa Cashetta
 * @date 06/19/13
 */
public class TimePanel extends JPanel {

	// -------- fields ---------
	private static final long serialVersionUID = 1L;
	protected ArrayList<JLabel> timeLabels = new ArrayList<>();
	protected final int SUBDIVISIONS;
	protected final int SUBDIVISION_SIZE;
	protected final int FILLER;
	protected final int WIDTH;
	
	public TimePanel() {
		// initialize numeric fields
		SUBDIVISIONS = 14;
		SUBDIVISION_SIZE = 45;
		FILLER = 15;
		WIDTH = 75;
		
		// initialize time labels
		String txt = null;
		for (int i = 8; i <= 21; i++) {
			if (i < 12) {
				txt = i + ":00a";
			} else if (i == 12) {
				txt = i + ":00p";
			} else {
				txt = (i - 12) + ":00p";
			}
			timeLabels.add(new JLabel(txt, JLabel.CENTER));
		}
		
		// set layout
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(new Color(8, 37, 103));
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(255, 97, 3), 1), BorderFactory.createBevelBorder(BevelBorder.LOWERED)));
		
		// add components
		for (JLabel l : timeLabels) {
			l.setFont(new Font("Tahoma", Font.BOLD, 12));
			l.setForeground(Color.white);
			l.setAlignmentX(CENTER_ALIGNMENT);
			Dimension d = new Dimension(WIDTH, FILLER);
			add(Box.createRigidArea(d));
			add(l);
			add(Box.createRigidArea(d));
		}
	}
	
	@Override
	public Dimension getMaximumSize() {
		return new Dimension(100, 635);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(new Color(255, 97, 3));
        for (int i = 1; i < SUBDIVISIONS; i++) {
           int y = i * SUBDIVISION_SIZE;
           g2.drawLine(0, y, getSize().width, y);
        }
	}

}
