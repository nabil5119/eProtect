package userInterface.user;

import java.awt.Color;
import java.awt.event.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;

import Database.Appel;
import Database.History;
import Database.Incident;
import Database.SousTypeIncident;
import Database.TypeIncident;

import java.sql.Connection;

import scripts.*;
import userInterface.Login;
import map.Map;

public class Insertion
{
	public static JTextField Nom, Telephone, Emplacement, Rue, Commune;
	public static JComboBox<String> Ville;
	public static JComboBox<String> Type, SubType;
	public static double longitude = 0, latitude = 0;
	public static boolean currentFrame = false;

	public static JPanel Insertion = new JPanel();

	public static JPanel Init()
	{
		currentFrame = true;
		Connection connection = Login.connection;
		Insertion.add(GUI.NewLabel("Nom d'appelant", Color.white, 25, 190, 250));
		Insertion.add(GUI.NewLabel("N° d'appelant", Color.white, 25, 190, 300));
		Insertion.add(GUI.NewLabel("Emplacement", Color.white, 25, 190, 350));
		Insertion.add(GUI.NewLabel("Rue", Color.white, 25, 190, 400));
		Insertion.add(GUI.NewLabel("Commune", Color.white, 25, 190, 450));
		Insertion.add(GUI.NewLabel("Ville", Color.white, 25, 190, 500));
		Insertion.add(GUI.NewLabel("Type", Color.white, 25, 190, 550));
		Insertion.add(GUI.NewLabel("Sous-type", Color.white, 25, 190, 600));
		Insertion.add(GUI.NewLabel("Priorité", Color.white, 25, 190, 650));

		Nom = GUI.NewTextField(350, 35, 400, 250);
		Telephone = GUI.NewTextField(350, 35, 400, 300);
		Emplacement = GUI.NewTextField(350, 35, 400, 350);
		Rue = GUI.NewTextField(350, 35, 400, 400);
		Commune = GUI.NewTextField(350, 35, 400, 450);
		Ville = GUI.NewComboBox(Database.Ville.getAllCities(), 400, 500);

		String[] typeList = TypeIncident.getTypes();
		String[][] subTypeList = SousTypeIncident.getSubType();
		Type = GUI.NewComboBox(typeList, 400, 550);
		SubType = GUI.NewComboBox(subTypeList[0], 400, 600);
		String[] priorities =
		{ "1", "2", "3", "4", "5", "6", "7", "8", "9" };
		JComboBox<String> priority = GUI.NewComboBox(priorities, 400, 650);
		Insertion.add(priority);

		ItemListener itemListener = new ItemListener()
		{
			public void itemStateChanged(ItemEvent evt)
			{
				String item = (String) evt.getItem();
				for (int i = 0; i < typeList.length; i++)
				{
					if (item.equals(typeList[i]))
					{
						SubType.removeAllItems();
						for (String str : subTypeList[i])
						{

							if (str != null)
								SubType.addItem(str);
						}
					}
				}
			}
		};
		Type.addItemListener(itemListener);

		Insertion.add(Nom);
		Insertion.add(Telephone);
		Insertion.add(Emplacement);
		Insertion.add(Rue);
		Insertion.add(Commune);
		Insertion.add(Ville);
		Insertion.add(Type);
		Insertion.add(SubType);
		// Insertion.add(Longitude);
		// Insertion.add(Latitude);

		JButton BInsert = GUI.NewButton("Insérer","res/User/Insert/InsertIcon.png", 20, 200, 40, 180, 800);
		Insertion.add(BInsert);
		JButton BMap = GUI.NewButton("Voir sur Carte","res/User/Insert/MapIcon.png", Color.GRAY, 15, 200, 40, 380, 800);
		Insertion.add(BMap);
		JButton Clear = GUI.NewButton("Effacer","res/User/Insert/ClearIcon.png", Color.gray, 15, 200, 40, 580, 800);
		Insertion.add(Clear);

		BInsert.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent arg0)
			{
				String emplacement = Emplacement.getText();
				String rue = Rue.getText();
				String commune = Commune.getText();

				int IdVille = Ville.getSelectedIndex();
				int IdTypeIncident = Type.getSelectedIndex();
				int IdSousTypeIncident = SubType.getSelectedIndex();

				if (isInt(Telephone.getText()) && Nom.getText().length() > 0 && emplacement.length() > 0
						&& rue.length() > 0 && commune.length() > 0)
				{
					try
					{
						Date date = new Date();
						DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
						String strDate = dateFormat.format(date);

						History.add(Login.username, "Insérer l'appel " + Appel.AppelCount());
						Appel.insert(Telephone.getText(), Nom.getText());
						History.add(Login.username, "Insérer l'incident " + (Incident.IncidentCount()));
						Incident.insert(strDate, priority.getSelectedIndex() + 1, emplacement, rue, commune, IdVille,
								latitude, longitude, IdTypeIncident, IdSousTypeIncident);
						connection.commit();

						Map.clearIncident();
						Clear();
						JOptionPane.showMessageDialog(null, "Incident inséré avec succès");
					} catch (Exception e)
					{
						e.printStackTrace();
						JOptionPane.showMessageDialog(null, "Veuillez vérifier vos entrées.", "Erreur",
								JOptionPane.ERROR_MESSAGE);
					}
				} else
				{
					JOptionPane.showMessageDialog(null, "Veuillez vérifier vos entrées.", "Erreur",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		BMap.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent arg0)
			{
				double[] Info = Geolocalisation.getCoor(
						Emplacement.getText() + Rue.getText() + Commune.getText() + (String) Ville.getSelectedItem());
				Map.goTo(Info);
				latitude = Info[0];
				longitude = Info[1];
			}
		});
		Clear.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent arg0)
			{
				Clear();
			}
		});

		JButton Back = GUI.NewButton("Retour","res/User/Insert/ReturnIcon.png", Color.black, 15, 150, 40, 425, 850);
		Insertion.add(Back);

		Back.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				currentFrame = false;
				Map.clearIncident();
				Insertion.removeAll();
				Home.Home.removeAll();
				User.panel.add(Home.Init(), "0");
				User.cl.show(User.panel, "0");
			}
		});

		Insertion.add(GUI.NewImage("res/User/Backgroundd.png", 960, 1080, 0, 0));
		Insertion.setLayout(null);
		return Insertion;
	}

	public static void Clear()
	{
		Nom.setText("");
		Telephone.setText("");
		Emplacement.setText("");
		Rue.setText("");
		Commune.setText("");
	}

	public static boolean isInt(String str)
	{
		if (str.length() > 0)
		{
			try
			{
				Integer.valueOf(str);
				return true;
			} catch (Exception e)
			{
			}
		}
		return false;
	}
}
