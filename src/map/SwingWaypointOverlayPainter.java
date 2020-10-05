package map;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.WaypointPainter;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

/************************************************************************************************
 * La classe SwingWaypointOverlayPainter permet de dessiner points de
 * cheminement sur JXMapViewer.
 * 
 * @author A.S.E.D.S
 * @version 7.02
 ************************************************************************************************/
public class SwingWaypointOverlayPainter extends WaypointPainter<SwingWaypoint>
{

	/**
	 * Les sous-classes doivent impl�menter cette m�thode et effectuer des
	 * op�rations de peinture personnalis�es ici.
	 * 
	 * @param g           le graphique en deux dimensions � interpr�ter.
	 * @param jxMapViewer la carte � interpr�ter.
	 * @param width       largeur de la zone � dessiner.
	 * @param height      largeur de la zone � dessiner.
	 */
	@Override
	protected void doPaint(Graphics2D g, JXMapViewer jxMapViewer, int width, int height)
	{
		for (SwingWaypoint swingWaypoint : getWaypoints())
		{
			Point2D point = jxMapViewer.getTileFactory().geoToPixel(swingWaypoint.getPosition(), jxMapViewer.getZoom());
			Rectangle rectangle = jxMapViewer.getViewportBounds();
			int buttonX = (int) (point.getX() - rectangle.getX());
			int buttonY = (int) (point.getY() - rectangle.getY());
			JButton button = swingWaypoint.getButton();
			button.setLocation(buttonX - button.getWidth() / 2, buttonY - button.getHeight() / 2);
		}
	}
}
