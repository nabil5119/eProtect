package userInterface.admin;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;

import Database.History;
import Database.Incident;
import Database.PersonnelUtiliser;
import Database.Resolue;
import Database.Utiliser;
import net.proteanit.sql.DbUtils;
import scripts.GUI;
import userInterface.Login;
import userInterface.Window;

public class Incidents
{
	private static JPanel Incidents;
	static JSplitPane splitPane;
	static JTable table;
	static JButton Delete;

	static float scalex = Window.scalex;
	static float scaley = Window.scaley;
	static int SelectedIdIncident;
	static Connection connection;
	static boolean RowSelected;

	public static JPanel Init()
	{
		connection = Login.connection;
		Incidents = new JPanel();
		table = new JTable();
		table.setDefaultEditor(Object.class, null);
		table.setForeground(Color.DARK_GRAY);
		table.setFont(new Font("Century Gothic", Font.PLAIN, (int) (scalex * 14)));
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds((int) (scalex * 50), (int) (scaley * 20), (int) (scalex * 1820), (int) (scaley * 850));
		Incidents.add(scrollPane);
		scrollPane.setViewportView(table);
		ResultSet rs = Incident.GetAllIncident();
		table.setModel(DbUtils.resultSetToTableModel(rs));

		Delete = GUI.NewButton("Supprimer", Color.gray, 20, 200, 30, 860, 900);
		Incidents.add(Delete);
		Delete.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				if (RowSelected)
				{
					SelectedIdIncident = Integer.valueOf(table.getValueAt(table.getSelectedRow(), 0).toString());
					if (table.getValueAt(table.getSelectedRow(), 8).toString().equals("En Cours"))
					{
						JOptionPane.showMessageDialog(null,
								"Vous ne pouvez pas supprimer une incident dans l'état \"En cours\"", "Erreur",
								JOptionPane.ERROR_MESSAGE);
					} 
					else
					{
						try
						{
							Incident.delete(SelectedIdIncident);
							History.add(Login.username, "Supprimer l'incident " + SelectedIdIncident);
							connection.commit();
						} catch (SQLException e1)
						{
							e1.printStackTrace();
							JOptionPane.showMessageDialog(null, "Erreur de connexion, veuillez réessayer.", "Erreur",
									JOptionPane.ERROR_MESSAGE);
						}
						RowSelected = false;
						ResultSet rs = Incident.GetAllIncident();
						table.setModel(DbUtils.resultSetToTableModel(rs));
						table.clearSelection();
						Delete.setBackground(Color.gray);
					}

				}
			}
		});

		table.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				RowSelected = true;
				Delete.setBackground(new Color(100, 150, 215));
				if (e.getClickCount() == 2)
				{
					int SelectedIdIncident = Integer.valueOf(table.getValueAt(table.getSelectedRow(), 0).toString());
					int[] MRes = Utiliser.getRessourceUsed(SelectedIdIncident);
					int[] res = new int[MRes.length + 1];
					for (int i = 0; i < MRes.length; i++)
					{
						res[i] = MRes[i];
					}
					res[MRes.length] = PersonnelUtiliser.getPersonnelUsed(SelectedIdIncident).length;
					int IdCaserne = Resolue.getCaserne(SelectedIdIncident);
					ResourceManagement.init(res, false, IdCaserne, false);
				}
			}
		});

		Incidents.add(GUI.NewImage("res/User/AdminBackground.png", 1920, 1080, 0, 0));

		Incidents.setLayout(null);
		return Incidents;
	}

	public static void update()
	{
		ResultSet rs = Incident.GetAllIncident();
		table.setModel(DbUtils.resultSetToTableModel(rs));
	}

}
