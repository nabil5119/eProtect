
package map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.painter.Painter;

/***************************************************************************************************
 * La classe InfoPainter permet d'afficher les informations sur les ressources
 * pour chaque caserne.<BR>
 * lorsque l'utilisateur place la souris sur le nom d'une caserne sur la carte.
 * 
 * @author A.S.E.D.S
 * @version 7.02
 **************************************************************************************************/
public class InfoPainter implements Painter<JXMapViewer>
{
	private Color color;
	private String Info;

	private GeoPosition coord;

	/**
	 * Constructeur paramétré.
	 * 
	 * @param coord les coordonnées de la caserne.
	 * @param color La couleur du fond des informations.
	 * @param Info  les informations à afficher.
	 */
	public InfoPainter(GeoPosition coord, Color color, String Info)
	{
		this.coord = coord;
		this.color = color;
		this.Info = Info;
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

		Point2D pt = map.getTileFactory().geoToPixel(coord, map.getZoom());

		g.setColor(color);
		g.setStroke(new BasicStroke(2));
		drawRect(g, map, pt);

		g.setColor(Color.BLACK);
		g.setFont(new Font("Tahoma", Font.BOLD, 12));
		drawString(g, Info, (int) pt.getX() - 65, (int) pt.getY() - 140);
		g.dispose();

	}

	/**
	 * Permets de dessiner le champ des informations sur la carte.
	 *
	 * @param g   le graphique en deux dimensions à interpréter.
	 * @param map la carte à interpréter.
	 * @param pt  les coordonnées de la caserne.
	 */
	private void drawRect(Graphics2D g, JXMapViewer map, Point2D pt)
	{
		g.fillRect((int) pt.getX() - 75, (int) pt.getY() - 150, 150, 150);
		g.setColor(Color.BLACK);
		g.drawRect((int) pt.getX() - 75, (int) pt.getY() - 150, 150, 150);
	}

	/**
	 * Permets de remplire le champ des informations sur la carte.
	 *
	 * @param g    le graphique en deux dimensions à interpréter.
	 * @param text les informations à afficher.
	 * @param x    l'abscisse de la zone du dessin
	 * @param y    l'ordonnée de la zone du dessin
	 */
	void drawString(Graphics2D g, String text, int x, int y)
	{
		for (String line : text.split("\n"))
			g.drawString(line, x, y += g.getFontMetrics().getHeight());
	}
}
