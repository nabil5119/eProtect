
package map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.painter.Painter;

/********************************************************************************************************
 * La classe RoutePainter permet de dessiner l'itinéraire entre une caserne et
 * l'emplacement de l'incident.
 * 
 * @author A.S.E.D.S
 * @version 7.02
 ********************************************************************************************************/
public class RoutePainter implements Painter<JXMapViewer>
{
	private Color color = Color.RED;
	private boolean antiAlias = true;

	private List<GeoPosition> track;

	/**
	 * Constructeur paramétré.
	 * 
	 * @param track la piste à dessiner (une liste des coordonnées).
	 * 
	 */
	public RoutePainter(List<GeoPosition> track)
	{
		// copy the list so that changes in the
		// original list do not have an effect here
		this.track = new ArrayList<GeoPosition>(track);
	}

	/**
	 * Permets de dessiner sur la carte.
	 *
	 * @param g   le graphique en deux dimensions à interpréter.
	 * @param map la carte à interpréter.
	 * @param w   largeur de la zone à dessiner.
	 * @param h   hauteur de la zone à dessiner.
	 */
	@Override
	public void paint(Graphics2D g, JXMapViewer map, int w, int h)
	{
		g = (Graphics2D) g.create();

		// convert from viewport to world bitmap
		Rectangle rect = map.getViewportBounds();
		g.translate(-rect.x, -rect.y);

		if (antiAlias)
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// do the drawing
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(4));

		drawRoute(g, map);

		// do the drawing again
		g.setColor(color);
		g.setStroke(new BasicStroke(2));

		drawRoute(g, map);

		g.dispose();
	}

	/**
	 * Permets de dessiner l'itinéraire sur la carte.
	 * 
	 * @param g   le graphique en deux dimensions à interpréter.
	 * @param map la carte à interpréter.
	 */
	private void drawRoute(Graphics2D g, JXMapViewer map)
	{
		int lastX = 0;
		int lastY = 0;

		boolean first = true;

		for (GeoPosition gp : track)
		{
			// convert geo-coordinate to world bitmap pixel
			Point2D pt = map.getTileFactory().geoToPixel(gp, map.getZoom());

			if (first)
			{
				first = false;
			} else
			{
				g.drawLine(lastX, lastY, (int) pt.getX(), (int) pt.getY());
			}

			lastX = (int) pt.getX();
			lastY = (int) pt.getY();
		}
	}
}
