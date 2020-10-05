package userInterface.user;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import map.Map;
import scripts.DisplayImage;
import scripts.GUI;
import scripts.Geolocalisation;
import scripts.WritePDF;
import userInterface.Login;

import org.jxmapviewer.viewer.GeoPosition;

import Database.Caserne;
import Database.History;
import Database.Incident;
import Database.PersonnelUtiliser;
import Database.Resolue;
import Database.Ressource;
import Database.RessourceHumaine;
import Database.TypeRessource;
import Database.Utiliser;

public class Validation
{

	public static JPanel validation = new JPanel();
	public static int IdCaserne = 0;
	public static int IdIncident = 0;
	public static boolean currentPanel = false;
	static boolean ScreenShotTaken;
	static BufferedImage screenshot;

	static String date, sousType, Emplacement, commune, Rue;

	public static JPanel Init(int IdCaserne, String date, String sousType, String Emplacement, String commune,
			String Rue)
	{
		validation = new JPanel();
		currentPanel = true;
		ScreenShotTaken = false;

		Validation.IdCaserne = IdCaserne;
		Validation.date = date;
		Validation.sousType = sousType;
		Validation.Emplacement = Emplacement;
		Validation.commune = commune;
		Validation.Rue = Rue;

		IdIncident = Manage.SelectedIdIncident;
		Connection connection = Login.connection;

		GeoPosition CasernePos = Caserne.getCasernePosition(IdCaserne);
		String[][] CasernesInfo = Caserne.getInfo();
		GeoPosition IncidentPos = Incident.getIncidentPosition(IdIncident);
		int[] itinerary = Geolocalisation.getDistance(CasernePos, IncidentPos);
		int[] maxRes = Ressource.getResources(IdCaserne, false);
		int AllRessourceTypeCount = maxRes.length;
		String[][] RessourcesNames = TypeRessource.getRessourceTypeName();

		validation.add(
				GUI.NewLabel("La caserne à agir : " + Caserne.getCaserneName(IdCaserne), Color.white, 25, 220, 250));
		validation.add(
				GUI.NewLabel("Distance du caserne a L'incident : " + itinerary[0] + "m", Color.white, 20, 220, 290));

		String[] CasernesNames = new String[CasernesInfo.length];

		int row = 0;
		for (String[] str : CasernesInfo)
		{
			CasernesNames[row] = str[0];
			row += 1;
		}

		int j = 0;
		@SuppressWarnings("unchecked")
		JComboBox<String>[] resourcesUsed = (JComboBox<String>[]) new JComboBox[AllRessourceTypeCount];

		int Xoffset = 0;
		int Yoffset = 0;
		for (String RessourceType[] : RessourcesNames)
		{
			String FullName = RessourcesNames[j][1];
			if (j == Math.ceil(AllRessourceTypeCount / 3.0))
			{
				Xoffset = 200;
				Yoffset = 0;
			}
			if (j == Math.ceil(AllRessourceTypeCount * 2 / 3.0))
			{
				Xoffset = 400;
				Yoffset = 0;
			}
			JLabel label = GUI.NewLabel(RessourceType[0] + " :", Color.white, 20, 120, 40, 210 + Xoffset,
					340 + Yoffset);
			label.setToolTipText(FullName);
			validation.add(label);

			String[] ResourceCount = new String[maxRes[j] + 1];
			for (int i = 0; i < maxRes[j] + 1; i++)
				ResourceCount[i] = Integer.toString(i);

			resourcesUsed[j] = GUI.NewComboBox(ResourceCount, 50, 25, 320 + Xoffset, 350 + Yoffset);
			validation.add(resourcesUsed[j]);
			Yoffset += 50;
			j += 1;
		}

		validation.add(GUI.NewLabel("Resource humaine à utiliser :", Color.white, 25, 220, 750));

		int ResourceHumaineCount = RessourceHumaine.getAllResources(IdCaserne)-RessourceHumaine.getAllWorkingResources(IdCaserne);
		String[] ResourceHumaineBox = new String[ResourceHumaineCount];
		for (int i = 1; i < ResourceHumaineCount; i++)
			ResourceHumaineBox[i - 1] = Integer.toString(i);
		JComboBox<String> HumainResourcesUsed = GUI.NewComboBox(ResourceHumaineBox, 50, 25, 650, 750);
		validation.add(HumainResourcesUsed);

		JButton BScreenShot = GUI.NewButton("Capturer la carte", 17, 200, 40, 250, 800);
		validation.add(BScreenShot);
		JButton BConfirm = GUI.NewButton("Confirmer", Color.GRAY, 20, 200, 40, 550, 800);
		validation.add(BConfirm);

		BScreenShot.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent arg0)
			{
				screenshot = GUI.screenShot(10, 180, 920, 720);
				File outputfile = new File("map.jpg");
				try
				{
					ImageIO.write(screenshot, "jpg", outputfile);
					new DisplayImage(screenshot);
				} catch (IOException e)
				{
					e.printStackTrace();
				}
				ScreenShotTaken = true;
				BConfirm.setBackground(new Color(224, 116, 60));
			}

		});

		BConfirm.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent arg0)
			{
				if (ScreenShotTaken)
				{
					Resolue.Insert(IdIncident, IdCaserne);

					int[] ResourcesUsed = new int[AllRessourceTypeCount];

					for (int i = 0; i < AllRessourceTypeCount; i++)
					{
						ResourcesUsed[i] = Integer.valueOf((String) resourcesUsed[i].getSelectedItem());
						// for if a ressource get used more than once
						for (int j = 0; j < ResourcesUsed[i]; j++)
						{
							int IdRessource = Ressource.UseIdRessource(IdCaserne, i);
							Utiliser.Insert(IdIncident, IdRessource);
						}
					}
					int HumainResourceUsed = Integer.valueOf((String) HumainResourcesUsed.getSelectedItem());

					for (int i = 0; i < HumainResourceUsed; i++)
					{
						int IdRessourceHumaine = RessourceHumaine.UseRessourceHumaine(IdCaserne);
						PersonnelUtiliser.Insert(IdIncident, IdRessourceHumaine);
					}
					Incident.SetIncidentState(IdIncident, "En Cours");
					try
					{
						History.add(Login.username, "Mettre l'incident " + IdIncident + " 'en Cours'");
						connection.commit();
					} catch (SQLException e)
					{
						e.printStackTrace();
					}

					try
					{
						Desktop.getDesktop().open(new File("fiche.pdf"));
					} catch (IOException e)
					{
						e.printStackTrace();
					}
					WritePDF.InstancePDF(IdIncident, date, sousType, Emplacement, commune, Rue, ResourcesUsed,
							HumainResourceUsed);

					Map.clearIncident();
					Map.track1 = null;
					Map.resetPainter();

					Insertion.Insertion.removeAll();
					Manage.Manage.removeAll();
					Home.Home.removeAll();
					validation.removeAll();
					User.panel.add(Home.Init(), "0");
					User.panel.add(Insertion.Init(), "1");
					User.cl.show(User.panel, "0");
					ScreenShotTaken = false;
				}
			}
		});

		JButton Back = GUI.NewButton("Retour", Color.black, 15, 150, 40, 425, 850);
		validation.add(Back);

		Back.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				currentPanel = false;
				Map.track1 = null;
				Map.resetPainter();
				Insertion.Insertion.removeAll();
				Home.Home.removeAll();
				validation.removeAll();
				Manage.Manage.removeAll();
				// User.panel.add(Home.Init(),"0");
				// User.panel.add(Insertion.Init(),"1");
				User.panel.add(Manage.Init(), "2");
				User.cl.show(User.panel, "2");
			}
		});

		validation.add(GUI.NewImage("res/User/Backgroundd.png", 960, 1080, 0, 0));
		validation.setLayout(null);
		return validation;
	}

	public static JPanel reset(int id)
	{
		validation.removeAll();
		return Init(id, date, sousType, Emplacement, commune, Rue);

	}
}
