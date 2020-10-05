package userInterface.admin;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;

import Database.Caserne;
import Database.History;
import Database.Resolue;
import Database.Ressource;
import Database.RessourceHumaine;
import Database.TypeRessource;
import net.proteanit.sql.DbUtils;
import userInterface.Login;
import userInterface.Window;
import scripts.CustomDialog;
import scripts.GUI;
import scripts.Geolocalisation;

public class Casernes
{
	private static JPanel Casernes;
	static JSplitPane splitPane;
	static JTable table;
	static JButton Delete, ChangeName, ChangeTele, ChangeRess;
	static float scalex = Window.scalex;
	static float scaley = Window.scaley;
	static boolean RowSelected;
	static int SelectedIdCaserne;
	static Connection connection;
	static int[] resources;
	static int HumainResources;
	static double lat, lon;
	public static boolean windowopen;
	public static JButton Localosation, Ressources;

	public static JPanel Init()
	{
		Casernes = new JPanel();
		connection = Login.connection;
		RowSelected = false;

		table = new JTable();
		table.setDefaultEditor(Object.class, null);
		table.setForeground(Color.DARK_GRAY);
		table.setFont(new Font("Century Gothic", Font.PLAIN, (int) (scalex * 14)));
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds((int) (scalex * 50), (int) (scaley * 50), (int) (scalex * 1600), (int) (scaley * 750));
		Casernes.add(scrollPane);
		scrollPane.setViewportView(table);
		ResultSet rs = Caserne.getCasernes();
		table.setModel(DbUtils.resultSetToTableModel(rs));

		Delete = GUI.NewButton("Supprimer", Color.gray, 20, 200, 40, 1685, 50);
		Casernes.add(Delete);
		Casernes.add(GUI.NewLabel("Modifier :", 20, 1685, 110));
		ChangeTele = GUI.NewButton("N° de Telephone", Color.gray, 20, 200, 30, 1685, 150);
		Casernes.add(ChangeTele);
		ChangeName = GUI.NewButton("Nom", Color.gray, 20, 200, 30, 1685, 190);
		Casernes.add(ChangeName);
		ChangeRess = GUI.NewButton("Ressources", Color.gray, 20, 200, 30, 1685, 230);
		Casernes.add(ChangeRess);

		ChangeTele.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				if (RowSelected)
				{
					SelectedIdCaserne = Integer.valueOf(table.getValueAt(table.getSelectedRow(), 0).toString());
					SelectedIdCaserne = Integer.valueOf(table.getValueAt(table.getSelectedRow(), 0).toString());
					CustomDialog customDialog = new CustomDialog(null, "N° de Telephone");
					customDialog.pack();
					customDialog.setLocationRelativeTo(null);
					customDialog.setVisible(true);
					if (customDialog.getValidatedText() != null)
					{
						try
						{
							Caserne.ChangeTele(SelectedIdCaserne, customDialog.getValidatedText());
							History.add(Login.username, "Changer le N° de Telephone du caserne " + SelectedIdCaserne);
							connection.commit();
						} catch (SQLException e1)
						{
							JOptionPane.showMessageDialog(null, "Erreur de connexion, veuillez réessayer.", "Erreur",
									JOptionPane.ERROR_MESSAGE);
						}
					}
					resetTable();
					resetTable();
				}
			}
		});

		ChangeName.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				if (RowSelected)
				{
					SelectedIdCaserne = Integer.valueOf(table.getValueAt(table.getSelectedRow(), 0).toString());
					CustomDialog customDialog = new CustomDialog(null, "Nom");
					customDialog.pack();
					customDialog.setLocationRelativeTo(null);
					customDialog.setVisible(true);
					if (customDialog.getValidatedText() != null)
					{
						try
						{
							Caserne.ChangeName(SelectedIdCaserne, customDialog.getValidatedText());
							History.add(Login.username, "Changer le Nom du caserne " + SelectedIdCaserne);
							connection.commit();
						} catch (SQLException e1)
						{
							JOptionPane.showMessageDialog(null, "Erreur de connexion, veuillez réessayer.", "Erreur",
									JOptionPane.ERROR_MESSAGE);
						}
					}
					resetTable();
				}
			}
		});

		ChangeRess.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				if (RowSelected)
				{
					SelectedIdCaserne = Integer.valueOf(table.getValueAt(table.getSelectedRow(), 0).toString());
					int[] OldRes = Ressource.getResources(SelectedIdCaserne, true);
					int[] res = new int[TypeRessource.getAllRessourceTypeCount() + 1];
					int i = 0;
					for (; i < OldRes.length; i++)
					{
						res[i] = OldRes[i];
					}
					res[i] = RessourceHumaine.getAllResources(SelectedIdCaserne);
					ResourceManagement.init(res, false, SelectedIdCaserne, true);
					windowopen = true;
					resetTable();
				}
			}
		});

		Delete.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				if (RowSelected)
				{
					SelectedIdCaserne = Integer.valueOf(table.getValueAt(table.getSelectedRow(), 0).toString());
					if (Resolue.isWorking(SelectedIdCaserne))
					{
						JOptionPane.showMessageDialog(null, "Cette caserne gère encore des incidents", "Erreur",
								JOptionPane.ERROR_MESSAGE);
					} else
					{
						Caserne.delete(SelectedIdCaserne);
						Ressource.DeleteAll(SelectedIdCaserne);
						RessourceHumaine.DeleteAll(SelectedIdCaserne);
						try
						{
							History.add(Login.username, "Supprimer la caserne " + SelectedIdCaserne);
							connection.commit();
						} catch (SQLException e1)
						{
							e1.printStackTrace();
						}
						resetTable();
					}
				}
			}
		});

		table.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				RowSelected = true;
				setButtonColor(new Color(100, 150, 215));
			}
		});
		resources = new int[TypeRessource.getAllRessourceTypeCount()];

		Casernes.add(GUI.NewLabel("Ajouter :", 20, 50, 860));

		Casernes.add(GUI.NewLabel("N° de Telephone :", 20, 220, 810));
		JTextField Tele = GUI.NewTextField(200, 35, 200, 850);
		Casernes.add(Tele);

		Casernes.add(GUI.NewLabel("Nom de Caserne :", 20, 520, 810));
		JTextField Name = GUI.NewTextField(200, 35, 500, 850);
		Casernes.add(Name);

		Casernes.add(GUI.NewLabel("Ressources :", 20, 820, 810));
		Ressources = GUI.NewButton("Ajouter des Ressources", new Color(100, 150, 215), 15, 220, 40, 800, 850);
		Casernes.add(Ressources);
		windowopen = false;
		Ressources.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				if (!windowopen)
				{
					int[] res = new int[TypeRessource.getAllRessourceTypeCount() + 1];
					ResourceManagement.init(res, true, 0, true);
					windowopen = true;
				}
			}
		});

		Casernes.add(GUI.NewLabel("Localisation :", 20, 1120, 810));
		Localosation = GUI.NewButton("Localisation du Caserne", new Color(100, 150, 215), 15, 220, 40, 1100, 850);
		Casernes.add(Localosation);
		Localosation.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				if (!windowopen)
				{
					LocationSetter.init();
					windowopen = true;
				}
			}
		});

		JButton AddCaserne = GUI.NewButton("Ajouter", new Color(100, 150, 215), 20, 150, 40, 1425, 850);
		Casernes.add(AddCaserne);
		AddCaserne.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				int idCaserne = Caserne.CaserneCount() + 1;
				String NTelephone = Tele.getText();
				String Nom = Name.getText();
				double latitude = LocationSetter.getLat();
				;
				double longitude = LocationSetter.getLon();
				double[] coord =
				{ latitude, longitude };
				String rue = Geolocalisation.getLoc(coord)[1];
				String commune = Geolocalisation.getLoc(coord)[2];
				if (isInt(NTelephone) && Nom.length() > 0 && rue.length() > 0 && commune.length() > 0)
				{
					try
					{
						resources = ResourceManagement.getResource();
						HumainResources = ResourceManagement.getHumaineResource();

						Caserne.insert(idCaserne, NTelephone, Nom, rue, commune, 0, latitude, longitude);
						for (int i = 0; i < resources.length; i++)
						{
							// To add multiple resoures of the same type
							for (int j = 0; j < resources[i]; j++)
							{
								Ressource.addResource(idCaserne, i);
							}
						}
						for (int i = 0; i < HumainResources; i++)
						{
							CustomDialog customDialog = new CustomDialog(null, "nom");
							customDialog.pack();
							customDialog.setLocationRelativeTo(null);
							customDialog.setVisible(true);
							if (customDialog.getValidatedText() != null)
							{

								RessourceHumaine.addResource(SelectedIdCaserne,customDialog.getValidatedText());
							}
						}
						ResultSet rs = Caserne.getCasernes();
						table.setModel(DbUtils.resultSetToTableModel(rs));
						History.add(Login.username, "Ajouter la caserne " + idCaserne);
						connection.commit();
						Tele.setText("");
						Name.setText("");
					} catch (Exception ex)
					{
						JOptionPane.showMessageDialog(null, "Veuillez Vérifier vos entrées.", "Erreur",
								JOptionPane.ERROR_MESSAGE);
					}
				} else
				{
					JOptionPane.showMessageDialog(null, "Veuillez Vérifier vos entrées.", "Erreur",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		Casernes.add(GUI.NewImage("res/User/AdminBackground.png", 1920, 1080, 0, 0));

		Casernes.setLayout(null);
		return Casernes;
	}

	public static void setButtonColor(Color color)
	{
		Delete.setBackground(color);
		ChangeName.setBackground(color);
		ChangeTele.setBackground(color);
		ChangeRess.setBackground(color);
	}

	public static void resetTable()
	{
		RowSelected = false;
		ResultSet rs = Caserne.getCasernes();
		table.setModel(DbUtils.resultSetToTableModel(rs));
		table.clearSelection();
		setButtonColor(Color.GRAY);
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
