
package map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;

import org.jxmapviewer.painter.Painter;

/**************************************************************************
 * La classe CirclePainter permet de dessiner des cercles sur la carte.<BR>
 * 
 * @author A.S.E.D.S
 * @version 7.02
 **************************************************************************/
public class CirclePainter implements Painter<JXMapViewer>, ActionListener
{
	private Color color;
	private boolean relative;
	private boolean fading;
	private List<GeoPosition> track;
	private final float radius;

	Timer timer = new Timer(20, this);
	private float alpha = 1f;
	private boolean dec = true;

	public void actionPerformed(ActionEvent e)
	{
		if (dec)
		{
			alpha -= 0.025f;
		}
		if ((alpha < 0.025f) || alpha > 0.975f)
		{
			timer = new Timer(20, this);
			dec = !dec;
		}
		if (!dec)
		{
			alpha += 0.025f;
		}
		Map.mapViewer.repaint();
	}

	/**
	 * Constructeur paramétré.
	 * 
	 * @param track    la liste des géolocalisations des centres des cercles.
	 * @param radius   Le rayon des cercles.
	 * @param color    la couleur de l'intérieur des cercles.
	 * @param relative Prendre en considération le zoom de la carte dans le dessin
	 *                 du cercle.
	 * @param fading   Avoir l'effect de décoloration du cercle.
	 */

	public CirclePainter(List<GeoPosition> track, float radius, Color color, boolean relative, boolean fading)
	{
		timer.start();
		this.relative = relative;
		this.fading = fading;
		this.track = new ArrayList<GeoPosition>(track);
		this.radius = radius;
		this.color = color;
	}

	/**
	 * Permets d'interpréter le graphique en deux dimensions donné.
	 * 
	 * @param g   le graphique en deux dimensions à interpréter.
	 * @param map la carte à interpréter.
	 * @param w   largeur de la zone à dessiner.
	 * @param h   hauteur de la zone à dessiner.
	 */

	public void paint(Graphics2D g, JXMapViewer map, int w, int h)
	{
		g = (Graphics2D) g.create();

		Rectangle rect = map.getViewportBounds();
		g.translate(-rect.x, -rect.y);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(2));
		drawOval(g, map, false);

		if (fading)
			color = new Color(color.getRed() * 1 / 255f, color.getGreen() * 1 / 255f, color.getBlue() * 1 / 255f,
					alpha);
		g.setColor(color);
		g.setStroke(new BasicStroke(2));
		drawOval(g, map, true);

		g.dispose();
	}

	/**
	 * Permets de dessiner les cercles sur la carte.
	 *
	 * @param g    le graphique en deux dimensions à interpréter.
	 * @param map  la carte à interpréter.
	 * @param fill Remplir ou non les cercles.
	 */
	private void drawOval(Graphics2D g, JXMapViewer map, boolean fill)
	{
		for (GeoPosition gp : track)
		{
			Point2D pt = map.getTileFactory().geoToPixel(gp, map.getZoom());
			double p = 100;
			if (relative == true)
				p = Math.cos(gp.getLongitude()) / Math.pow(2, map.getZoom() + 8) * 100000;
			if (fill)
				g.fillOval((int) (pt.getX() - p * radius / 2), (int) (pt.getY() - p * radius / 2), (int) (p * radius),
						(int) (p * radius));
			else
				g.drawOval((int) (pt.getX() - p * radius / 2), (int) (pt.getY() - p * radius / 2), (int) (p * radius),
						(int) (p * radius));
		}
	}
}
