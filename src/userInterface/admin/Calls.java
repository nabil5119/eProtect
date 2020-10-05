package userInterface.admin;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;

import Database.Appel;
import Database.History;
import net.proteanit.sql.DbUtils;
import scripts.GUI;
import userInterface.Login;
import userInterface.Window;

public class Calls
{
	private static JPanel Calls;
	static JSplitPane splitPane;
	static JTable table;
	static JButton Delete;

	static float scalex = Window.scalex;
	static float scaley = Window.scaley;
	static int SelectedIdCall;
	static Connection connection;
	static boolean RowSelected;

	public static JPanel Init()
	{

		Calls = new JPanel();
		connection = Login.connection;
		table = new JTable();
		table.setDefaultEditor(Object.class, null);
		table.setForeground(Color.DARK_GRAY);
		table.setFont(new Font("Century Gothic", Font.PLAIN, (int) (scalex * 14)));
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds((int) (scalex * 50), (int) (scaley * 20), (int) (scalex * 1820), (int) (scaley * 850));
		Calls.add(scrollPane);
		scrollPane.setViewportView(table);
		ResultSet rs = Appel.getCalls();
		table.setModel(DbUtils.resultSetToTableModel(rs));

		Delete = GUI.NewButton("Supprimer", Color.gray, 20, 200, 30, 860, 900);
		Calls.add(Delete);
		Delete.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				if (RowSelected)
				{
					SelectedIdCall = Integer.valueOf(table.getValueAt(table.getSelectedRow(), 0).toString());

					try
					{
						Appel.delete(SelectedIdCall);
						History.add(Login.username, "Supprimer l'appel " + SelectedIdCall);
						connection.commit();
					} catch (SQLException e1)
					{
						e1.printStackTrace();
						JOptionPane.showMessageDialog(null, "Erreur de connexion, veuillez réessayer.", "Erreur",
								JOptionPane.ERROR_MESSAGE);
					}
					RowSelected = false;
					ResultSet rs = Appel.getCalls();
					table.setModel(DbUtils.resultSetToTableModel(rs));
					table.clearSelection();
					Delete.setBackground(Color.gray);
				}
			}
		});

		table.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				RowSelected = true;
				Delete.setBackground(new Color(100, 150, 215));
			}
		});

		Calls.add(GUI.NewImage("res/User/AdminBackground.png", 1920, 1080, 0, 0));

		Calls.setLayout(null);
		return Calls;
	}

}
