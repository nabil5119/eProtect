package map;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import org.jxmapviewer.JXMapKit;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.cache.FileBasedLocalCache;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.WaypointPainter;

import Database.Caserne;
import Database.Incident;
import scripts.GUI;
import scripts.Geolocalisation;
import userInterface.user.Insertion;

/***************************************************************************************************
 * La classe Map permet de créer le panneau de la carte et lui affecter les
 * fonctionnalités appropriées.
 * 
 * @author A.S.E.D.S
 * @version 7.02
 **************************************************************************************************/
public class Map
{
	public static JXMapKit jXMapKit;
	public static JXMapViewer mapViewer;
	public static List<Painter<JXMapViewer>> painters;
	static WaypointPainter<SwingWaypoint> swingWaypointPainter;
	static CirclePainter RadiusPainter;
	static CirclePainter WaitingIncidentsPainter, InProgIncidentsPainter;
	static RoutePainter routePainter;
	public static InfoPainter Info;
	public static List<GeoPosition> track1;

	public static int zoom;
	public static double lat, lon;
	public static boolean drawInfo;
	static boolean drawIncident;
	static GeoPosition incident = new GeoPosition(0, 0);
	private static boolean drawRadius;

	/**
	 * Permets de créer le panneau de la carte.
	 * 
	 * @return le panneau de la carte.
	 */
	public static JPanel newMap()
	{
		jXMapKit = new JXMapKit();
		drawRadius = false;
		mapViewer = new JXMapViewer();
		track1 = new ArrayList<GeoPosition>();
		painters = new ArrayList<Painter<JXMapViewer>>();
		/*mapViewer.add(GUI.NewLabel("       			                                     ", 20, 0, 0));
		JButton ShowHide = GUI.NewButton("Afficher/Cacher la zone de Couverture", Color.GRAY, 0, 500);
		ShowHide.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent arg0)
			{
				drawRadius = !drawRadius;
				resetPainter();
			}
		});

		mapViewer.add(ShowHide);*/

		TileFactoryInfo info = new OSMTileFactoryInfo();
		DefaultTileFactory tileFactory = new DefaultTileFactory(info);
		File cacheDir = new File(System.getProperty("user.home") + File.separator + ".jxmapviewer2");
		tileFactory.setLocalCache(new FileBasedLocalCache(cacheDir, false));
		mapViewer.setTileFactory(tileFactory);

		GeoPosition rabat = new GeoPosition(33.97, -6.84);

		// --------------------------------------------------------------------------------------------------------------------
		Set<SwingWaypoint> Swaypoints = new HashSet<SwingWaypoint>();
		List<GeoPosition> casserneLoc = new ArrayList<GeoPosition>();

		String[][] CaserneInfo = Caserne.getInfo();
		for (int row = 0; row < CaserneInfo.length; row++)
		{
			GeoPosition cas = new GeoPosition(Double.valueOf(CaserneInfo[row][1]), Double.valueOf(CaserneInfo[row][2]));
			casserneLoc.add(cas);
			Swaypoints.add(new SwingWaypoint(CaserneInfo[row][0], row, cas));
		}

		swingWaypointPainter = new SwingWaypointOverlayPainter();
		swingWaypointPainter.setWaypoints(Swaypoints);

		for (SwingWaypoint w : Swaypoints)
		{
			mapViewer.add(w.getButton());
		}
		RadiusPainter = new CirclePainter(casserneLoc, 50, new Color(0.25f, 1f, 0f, 0.1f), true, false);
		// --------------------------------------------------------------------------------------------------------------------

		mapViewer.setZoom(6);
		mapViewer.setAddressLocation(rabat);

		MouseInputListener mia = new PanMouseInputListener(mapViewer);
		mapViewer.addMouseListener(mia);
		mapViewer.addMouseMotionListener(mia);

		mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapViewer));

		mapViewer.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				if (Insertion.currentFrame && e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON3)
				{
					java.awt.Point p = e.getPoint();
					GeoPosition geo = mapViewer.convertPointToGeoPosition(p);
					lat = geo.getLatitude();
					lon = geo.getLongitude();

					Insertion.latitude = lat;
					Insertion.longitude = lon;

					double[] coor =
					{ lat, lon };
					String[] address = Geolocalisation.getLoc(coor);
					Insertion.Emplacement.setText(address[0]);
					Insertion.Rue.setText(address[1]);
					Insertion.Commune.setText(address[2]);
					drawIncident = true;
					resetPainter();
				}
			}
		});

		// ----------Draw-Painters--------------------------------------------------------------------------------------------------------
		painters.add(swingWaypointPainter);
		drawAllIncident();
		if (drawRadius)
			painters.add(RadiusPainter);

		CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
		mapViewer.setOverlayPainter(painter);
		// -------------------------------------------------------------------------------------------------------------------------------
		return mapViewer;
	}

	/**
	 * Permets d'afficher un emplacement sur la carte à partir de ses coordonnées.
	 * 
	 * @param info les coordonnées de l'emplacement plus le zoom.
	 */
	public static void goTo(double[] info)
	{
		drawIncident = true;
		int zoom = 18 - (int) (info[2]);
		mapViewer.setZoom(zoom);
		mapViewer.setAddressLocation(new GeoPosition(info[0], info[1]));
		updateWindowTitle(mapViewer);
		resetPainter();
	}

	/**
	 * Permets de mettre à jour le positionnement et le zoom de la carte.
	 * 
	 * @param mapViewer la carte à interpréter.
	 */
	protected static void updateWindowTitle(JXMapViewer mapViewer)
	{
		lat = mapViewer.getCenterPosition().getLatitude();
		lon = mapViewer.getCenterPosition().getLongitude();
		zoom = mapViewer.getZoom();
	}

	/**
	 * Permets de réinitialiser les dessins sur la carte.
	 */
	public static void resetPainter()
	{
		incident = new GeoPosition(lat, lon);
		CirclePainter incidentCircel = new CirclePainter(Arrays.asList(incident), 0.1f, new Color(1f, 0f, 0f, 1f),
				false, false);
		SwingWaypoint.Incidentcoord = incident;
		painters.clear();
		drawAllIncident();
		painters.add(swingWaypointPainter);
		if (drawIncident)
			painters.add(incidentCircel);
		if (drawRadius)
			painters.add(RadiusPainter);
		if (track1 != null)
		{
			routePainter = new RoutePainter(track1);
			painters.add(routePainter);

		}

		if (drawInfo)
			painters.add(Info);

		CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
		mapViewer.setOverlayPainter(painter);
	}

	/**
	 * Permets d'effacer le cercle dessiné sur l'emplacement de l'incident.
	 */
	public static void clearIncident()
	{
		painters.clear();
		drawAllIncident();
		painters.add(swingWaypointPainter);
		if (drawRadius)
			painters.add(RadiusPainter);
		drawIncident = false;

		if (track1 != null)
		{
			routePainter = new RoutePainter(track1);
			painters.add(routePainter);

		}

		if (drawInfo)
			painters.add(Info);

		CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
		mapViewer.setOverlayPainter(painter);
	}

	/**
	 * Permets de dessiner des cercles sur les emplacements des incidents.
	 */
	static void drawAllIncident()
	{
		List<GeoPosition> WaitingIncidents = Incident.GetIncidentLocByState("En Attente");
		List<GeoPosition> InProgIncidents = Incident.GetIncidentLocByState("En Cours");
		WaitingIncidentsPainter = new CirclePainter(WaitingIncidents, 0.2f, new Color(1f, 0f, 0f, 1f), false, true);
		InProgIncidentsPainter = new CirclePainter(InProgIncidents, 0.2f, new Color(1f, 1f, 0f, 1f), false, true);
		painters.add(WaitingIncidentsPainter);
		painters.add(InProgIncidentsPainter);
	}
}