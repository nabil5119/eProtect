package map;

import userInterface.user.User;

import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;

import Database.Ressource;
import Database.RessourceHumaine;
import scripts.Geolocalisation;
import userInterface.user.Validation;

import javax.swing.*;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SwingWaypoint extends DefaultWaypoint
{
	public final JButton button;
	private final String text;
	private final int IdCaserne;
	private final GeoPosition coord;
	public static GeoPosition Incidentcoord = new GeoPosition(0, 0);
	static InfoPainter infoPainter;

	InfoPainter Info;

	public SwingWaypoint(String Caserne, int IdCaserne, GeoPosition coord)
	{
		super(coord);
		this.coord = coord;
		this.text = Caserne;
		this.IdCaserne = IdCaserne;
		button = new JButton(text);
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.setBackground(new Color(224, 116, 60));
		button.setVisible(true);
		button.addMouseListener(new SwingWaypointMouseListener());

	}

	JButton getButton()
	{
		return button;
	}

	private class SwingWaypointMouseListener implements MouseListener
	{

		@Override
		public void mouseClicked(MouseEvent e)
		{
			if (Validation.currentPanel)
			{
				Map.track1 = Geolocalisation.getRouting(coord, Incidentcoord);
				Map.resetPainter();

				Validation.validation.removeAll();
				User.panel.add(Validation.reset(IdCaserne), "2");
				User.cl.show(User.panel, "2");
			}
		}

		@Override
		public void mousePressed(MouseEvent e)
		{

		}

		@Override
		public void mouseReleased(MouseEvent e)
		{

		}

		@Override
		public void mouseEntered(MouseEvent e)
		{
			Map.drawInfo = true;
			int staff = RessourceHumaine.getAllResources(IdCaserne)-RessourceHumaine.getAllWorkingResources(IdCaserne);
			int vehicule = Ressource.getAllFreeResources(IdCaserne);
			Map.Info = new InfoPainter(coord, new Color(255, 80, 80),
					"Caserne de " + text + "\nStaff: " + staff + "\nVehicule: " + vehicule);
			Map.resetPainter();
		}

		@Override
		public void mouseExited(MouseEvent e)
		{
			Map.drawInfo = false;
			Map.resetPainter();
		}
	}
}