import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 * Draws a single day panel
 * 
 * @author Jeffrey Cannon and Alexa Cashetta
 * @date 06/07/13
 */
public class DayPanelInfo extends JPanel {
	
	// -------- fields --------
	private static final long serialVersionUID = 1L;
	protected ArrayList<ClassPanel> classes;
	protected final int SUBDIVISIONS;
	protected final int SUBDIVISION_SIZE;
	protected final int WIDTH;
	protected final int START;
	protected final int END;

	public DayPanelInfo(ArrayList<String[]> classes) {		
		// initialize numeric fields
		SUBDIVISIONS = 14;
		SUBDIVISION_SIZE = 45;
		if (classes.size() > 0) {
			WIDTH = 150;
		} else {
			WIDTH = 154;
		}
		START = 0;
		END = 840;
		
		// set layout and size of day panel
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setSize(new Dimension(WIDTH, (SUBDIVISIONS * SUBDIVISION_SIZE)));
		setBackground(Color.white);
		
		// initialize class panels
		this.classes = new ArrayList<>();
		for (String[] s : classes) {
			this.classes.add(new ClassPanel(s));
		}
		sort(this.classes);
		/*
		for (ClassPanel c : this.classes) {
			System.out.println(c.START + " - " + c.END);
		}
		*/
		// set border
		setBorder(BorderFactory.createLineBorder(new Color(255, 97, 3), 1));
		
		// build day panel
		if (this.classes.size() > 0) {
			for (int i = 0; i < this.classes.size(); i++) {
				if (i == 0) {
					add(Box.createRigidArea(new Dimension(WIDTH, (int)(SUBDIVISION_SIZE * ((double)(this.classes.get(i).START - START) / 60)))));
				} else {
					add(Box.createRigidArea(new Dimension(WIDTH, (int)(SUBDIVISION_SIZE * ((double)(this.classes.get(i).START - this.classes.get(i - 1).END) / 60)))));
				}
				add(this.classes.get(i));
				if (i == (this.classes.size() - 1)) {
					add(Box.createRigidArea(new Dimension(WIDTH, (int)(SUBDIVISION_SIZE * ((double)(END - this.classes.get(i).END) / 60)))));
				}
			}
		} else {
			add(Box.createRigidArea(new Dimension(WIDTH, (int)(SUBDIVISION_SIZE * ((double)(END - START) / 60)))));
		}
	}
	
	public static void sort(ArrayList<ClassPanel> c) {
		ClassPanel tmp = null;
		int imax = c.size() - 1;
		for (int i = 0; i < imax; i++) {
			for (int j = 1; j < (imax + 1) - i; j++) {
				if (c.get(j - 1).START > c.get(j).START) {
					tmp = c.get(j - 1);
					c.set(j - 1, c.get(j));
					c.set(j, tmp);
					// System.out.println("SWAP");
				}
			}
		}
	}
	
	@Override 
	public void paintComponent(Graphics g) {
         super.paintComponent(g);
         Graphics2D g2 = (Graphics2D) g;
         g2.setPaint(Color.GRAY);
         for (int i = 1; i < SUBDIVISIONS; i++) {
            int y = i * SUBDIVISION_SIZE;
            g2.drawLine(0, y, getSize().width, y);
         }     
    }
	
}