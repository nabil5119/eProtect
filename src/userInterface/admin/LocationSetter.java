package userInterface.admin;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.event.MouseInputListener;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.cache.FileBasedLocalCache;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;

import scripts.Geolocalisation;

public class LocationSetter
{
	private static double lat = 33.9716, lon = -6.8498;

	public static void init()
	{
		TileFactoryInfo info = new OSMTileFactoryInfo();
		DefaultTileFactory tileFactory = new DefaultTileFactory(info);

		File cacheDir = new File(System.getProperty("user.home") + File.separator + ".jxmapviewer2");
		tileFactory.setLocalCache(new FileBasedLocalCache(cacheDir, false));

		final JXMapViewer mapViewer = new JXMapViewer();
		mapViewer.setTileFactory(tileFactory);

		GeoPosition rabat = new GeoPosition(lat, lon);

		mapViewer.setZoom(7);
		mapViewer.setAddressLocation(rabat);

		MouseInputListener mia = new PanMouseInputListener(mapViewer);
		mapViewer.addMouseListener(mia);
		mapViewer.addMouseMotionListener(mia);

		mapViewer.addMouseListener(new CenterMapListener(mapViewer));
		mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapViewer));

		final JFrame frame = new JFrame();
		frame.setLayout(new BorderLayout());
		frame.add(mapViewer);
		frame.setSize(800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setUndecorated(false);
		frame.setVisible(true);

		mapViewer.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON3)
				{
					java.awt.Point p = e.getPoint();
					GeoPosition geo = mapViewer.convertPointToGeoPosition(p);
					lat = geo.getLatitude();
					lon = geo.getLongitude();
					double[] coord =
					{ lat, lon };
					if (Geolocalisation.getLoc(coord)[1].length() > 0)
						Casernes.Localosation.setText(Geolocalisation.getLoc(coord)[1]);
					else
						Casernes.Localosation.setText("Erreur, Veuillez reessayer");
					Casernes.windowopen = false;
					frame.dispose();
				}
			}
		});
	}

	public static double getLat()
	{
		return lat;
	}

	public static double getLon()
	{
		return lon;
	}
}
