package rem.calendar;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

/**
 * 
 * @author ovae.
 * @version 20150726.
 */
public class GradientPanel extends JPanel{


	/**
	 * 
	 */
	public GradientPanel(){
		super();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		int w = getWidth();
		int h = getHeight();
		Color colour1 = new Color(249,249,249);
		Color colour2 = new Color(238,238,238);
		GradientPaint gp = new GradientPaint(0, 0, colour1, 0, h, colour2);
		g2d.setPaint(gp);
		g2d.fillRect(0, 0, w, h);
	}
}
