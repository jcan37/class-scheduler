import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Represents a single panel for a class on the calendar
 * 
 * @author Jeffrey Cannon and Alexa Cashetta
 * @date 06/10/13
 */
public class ClassPanel extends JPanel {

	// ------ fields ------
	private static final long serialVersionUID = 1L;
	protected String[] details;
	protected JLabel className;
	protected JLabel time;
	protected JLabel location;
	protected JPanel fields;
	protected int fieldHeight;
	protected final String TOOL_TIP;
	protected final int HEIGHT;
	protected final int WIDTH;
	protected final int START;
	protected final int END;
	
	public ClassPanel(String[] details) {
		// set details field
		this.details = details;
		TOOL_TIP = "<html>Class: " + details[0] + " " + details[3] + "<br>Instructor: " + details[1] + "<br>Location: " + details[2] + "<br>Time: " + details[4] + " - " + details[5];
		
		// define numeric fields
		WIDTH = 152;
		HEIGHT = calcHeight(details[4], details[5]);
		START = convertTime(details[4]) - 480; // 8:00 A.M. is 0 minutes
		END = convertTime(details[5]) - 480;
		
		// details items: class, teacher, location, type, start, end
		className = new JLabel(details[0] + " " + details[3], JLabel.CENTER);
		className.setFont(new Font("Tahoma", Font.PLAIN, 10));
		className.setForeground(Color.white);
		className.setAlignmentX(CENTER_ALIGNMENT);
		className.setToolTipText(TOOL_TIP);
		time = new JLabel(details[4] + " - " + details[5], JLabel.CENTER);
		time.setFont(new Font("Tahoma", Font.PLAIN, 10));
		time.setForeground(Color.white);
		time.setAlignmentX(CENTER_ALIGNMENT);
		time.setToolTipText(TOOL_TIP);
		location = new JLabel(details[2], JLabel.CENTER);
		location.setFont(new Font("Tahoma", Font.PLAIN, 10));
		location.setForeground(Color.white);
		location.setAlignmentX(CENTER_ALIGNMENT);
		location.setToolTipText(TOOL_TIP);
		
		// set layout and dimensions
		fields =  new JPanel();
		fields.setBackground(new Color(8, 37, 103));
		fields.setLayout(new BoxLayout(fields, BoxLayout.Y_AXIS));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		// setSize(new Dimension(150, calcHeight(details[4], details[5])));
		// setBackground(new Color(8, 37, 103));
		// setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
		
		// add components
		if (calcHeight(details[4], details[5]) < 30) {
			fields.add(className);
			fieldHeight = 12;
			// fieldHeight = 13;
		} else if (calcHeight(details[4], details[5]) < 45) {
			fields.add(className);
			fields.add(time);
			fieldHeight = 26;
			// fieldHeight = 25;
		} else {
			fields.add(className);
			fields.add(location);
			fields.add(time);
			fieldHeight = 40;
			// fieldHeight = 38;
		}
		add(Box.createRigidArea(new Dimension(WIDTH, (HEIGHT - fieldHeight) / 2)));
		add(fields);
		add(Box.createRigidArea(new Dimension(WIDTH, (HEIGHT - fieldHeight) / 2)));
	}
	
	protected static int calcHeight(String start, String end) {
		int hourSize = 45;
		int mins = convertTime(end) - convertTime(start);
		return (int)(((double)mins/60) * hourSize);
	}
	
	protected static int convertTime(String time) {
		// format of time: 12:00p
		String military = "";
		if ((time.substring(0, 2).equals("12") && (time.charAt(time.length() - 1) == 'a' || time.charAt(time.length() - 1) == 'A'))) {
			military = "0" + time.substring(time.indexOf(':'), time.length() - 1);
		} else if ((time.charAt(time.length() - 1) == 'a' || time.charAt(time.length() - 1) == 'A') || (time.substring(0, 2).equals("12") && (time.charAt(time.length() - 1) == 'p' || time.charAt(time.length() - 1) == 'P'))) {
			military = time.substring(0, time.length() - 1);
		} else {
			int hrs = 12;
			try {
				hrs += Integer.parseInt(time.substring(0, time.indexOf(':')));
			} catch (NumberFormatException ex) {}
			military = hrs + time.substring(time.indexOf(':'), time.length() - 1);
		}
		String[] clock = military.split(":");
		int mins = 0;
		try {
			mins += (60 * Integer.parseInt(clock[0]));
			mins += Integer.parseInt(clock[1]);
		} catch (NumberFormatException ex) {}
		return mins;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		final int R = 4;
		Color midnightBlue = new Color(8, 37, 103);
		Color shadow = Color.black;
		Color highlight = new Color(0, 102, 204);
		g2.setColor(midnightBlue);
		RoundRectangle2D background = new RoundRectangle2D.Float(0, 0, getSize().width, getSize().height, 2 * R, 2 * R);
		g2.draw(background);
		g2.fill(background);
		g2.setColor(highlight);
		Rectangle2D top = new Rectangle(R / 2, 0, getSize().width - R, R / 2);
		g2.draw(top);
		g2.fill(top);
		Rectangle2D left = new Rectangle(0, R, R / 2, getSize().height - R);
		g2.draw(left);
		g2.fill(left);
		Arc2D corner = new Arc2D.Float(0, 0, R, R, 90, 180, Arc2D.Float.PIE);
		g2.draw(corner);
		g2.fill(corner);
		g2.setColor(shadow);
		Rectangle2D bottom = new Rectangle(R, getSize().height - 3 * R / 4, getSize().width, 3 * R / 4);
		g2.draw(bottom);
		g2.fill(bottom);
		Rectangle2D right = new Rectangle(getSize().width - 3 * R / 4, R, 3 * R / 4, getSize().height - R);
		g2.draw(right);
		g2.fill(right);
	}
	/*
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		final int R = 10;
		Color midnightBlue = new Color(8, 37, 103);
		Color shadow = Color.black;
		Color highlight = new Color(0, 102, 204);
		BasicStroke stroke = new BasicStroke(R / 3);
		g2.setColor(midnightBlue);
		RoundRectangle2D background = new RoundRectangle2D.Float(0, 0, getSize().width, getSize().height, 2 * R, 2 * R);
		g2.draw(background);
		g2.fill(background);
		g2.setStroke(stroke);
		g2.setColor(shadow);
		Line2D bottom = new Line2D.Float(R, getSize().height - 1, getSize().width - R, getSize().height - 1);
		g2.draw(bottom);
		Arc2D.Float botLeftDark = new Arc2D.Float(0, getSize().height - 2 * R, 2 * R, 2 * R, 225, 45, Arc2D.OPEN);
		g2.draw(botLeftDark);
		Arc2D.Float botRightDark = new Arc2D.Float(getSize().width - 2 * R, getSize().height - 2 * R, 2 * R, 2 * R, 270, 45, Arc2D.OPEN);
		g2.draw(botRightDark);
		g2.setColor(highlight);
		Line2D top = new Line2D.Float(R, 0, getSize().width - R, 0);
		g2.draw(top);
		Line2D left = new Line2D.Float(0, R, 0, getSize().height - R);
		g2.draw(left);
		Line2D right = new Line2D.Float(getSize().width - 1, R, getSize().width - 1, getSize().height - R);
		g2.draw(right);
		Arc2D.Float topLeft = new Arc2D.Float(0, 0, 2 * R, 2 * R, 90, 90, Arc2D.OPEN);
		g2.draw(topLeft);
		Arc2D.Float topRight = new Arc2D.Float(getSize().width - 2 * R, 0, 2 * R, 2 * R, 0, 90, Arc2D.OPEN);
		g2.draw(topRight);
		Arc2D.Float botLeftLight = new Arc2D.Float(0, getSize().height - 2 * R, 2 * R, 2 * R, 180, 45, Arc2D.OPEN);
		g2.draw(botLeftLight);
		Arc2D.Float botRightLight = new Arc2D.Float(getSize().width - 2 * R, getSize().height - 2 * R, 2 * R, 2 * R, 315, 45, Arc2D.OPEN);
		g2.draw(botRightLight);
	}
	*/
}
