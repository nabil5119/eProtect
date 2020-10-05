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
import Database.TypeRessource;
import net.proteanit.sql.DbUtils;
import userInterface.Login;
import userInterface.Window;
import scripts.CustomDialog;
import scripts.GUI;

public class TypeResources
{
	private static JPanel TypeResources;
	static JSplitPane splitPane;
	static JTable table;
	static JButton Delete, ChangeABV, ChangeName;
	static float scalex = Window.scalex;
	static float scaley = Window.scaley;
	static boolean RowSelected;
	static int SelectedId;
	static Connection connection = Login.connection;

	public static JPanel Init()
	{
		TypeResources = new JPanel();
		RowSelected = false;

		table = new JTable();
		table.setDefaultEditor(Object.class, null);
		table.setForeground(Color.DARK_GRAY);
		table.setFont(new Font("Century Gothic", Font.PLAIN, (int) (scalex * 14)));
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds((int) (scalex * 50), (int) (scaley * 50), (int) (scalex * 1600), (int) (scaley * 750));
		TypeResources.add(scrollPane);
		scrollPane.setViewportView(table);
		ResultSet rs = TypeRessource.getTypes();
		table.setModel(DbUtils.resultSetToTableModel(rs));

		Delete = GUI.NewButton("Supprimer", Color.gray, 20, 200, 40, 1685, 50);
		TypeResources.add(Delete);
		TypeResources.add(GUI.NewLabel("Modifier :", 20, 1685, 110));
		ChangeABV = GUI.NewButton("Abbreviation", Color.gray, 20, 200, 30, 1685, 150);
		TypeResources.add(ChangeABV);
		ChangeName = GUI.NewButton("Nom", Color.gray, 20, 200, 30, 1685, 190);
		TypeResources.add(ChangeName);

		ChangeABV.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				if (RowSelected)
				{
					SelectedId = Integer.valueOf(table.getValueAt(table.getSelectedRow(), 0).toString());
					CustomDialog customDialog = new CustomDialog(null, "Abbreviation");
					customDialog.pack();
					customDialog.setLocationRelativeTo(null);
					customDialog.setVisible(true);
					if (customDialog.getValidatedText() != null)
					{
						try
						{
							TypeRessource.ChangeABV(SelectedId, customDialog.getValidatedText());
							History.add(Login.username, "Changer l'Abbreviation du type de ressource " + SelectedId);
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

		ChangeName.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				if (RowSelected)
				{
					SelectedId = Integer.valueOf(table.getValueAt(table.getSelectedRow(), 0).toString());
					CustomDialog customDialog = new CustomDialog(null, "nom");
					customDialog.pack();
					customDialog.setLocationRelativeTo(null);
					customDialog.setVisible(true);
					if (customDialog.getValidatedText() != null)
					{
						try
						{
							TypeRessource.ChangeName(SelectedId, customDialog.getValidatedText());
							History.add(Login.username, "Changer le nom du type de ressource " + SelectedId);
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

		Delete.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				if (RowSelected)
				{
					SelectedId = Integer.valueOf(table.getValueAt(table.getSelectedRow(), 0).toString());
					if (SelectedId <= 17)
					{
						JOptionPane.showMessageDialog(null,
								"Vous ne pouvez pas supprimer les types définis par défaut des engines", "Erreur",
								JOptionPane.ERROR_MESSAGE);
					} else
					{
						if (TypeRessource.isWorking(SelectedId))
						{
							JOptionPane.showMessageDialog(null, "Ce type gère encore des incidents", "Erreur",
									JOptionPane.ERROR_MESSAGE);
						} else
						{
							TypeRessource.delete(SelectedId);
							try
							{
								History.add(Login.username, "Suppimer le type de ressource " + SelectedId);
								connection.commit();
							} catch (SQLException e1)
							{
								JOptionPane.showMessageDialog(null, "Erreur de connexion, veuillez réessayer.",
										"Erreur", JOptionPane.ERROR_MESSAGE);
							}
							resetTable();
						}

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

		TypeResources.add(GUI.NewLabel("Ajouter :", 20, 50, 860));

		TypeResources.add(GUI.NewLabel("Abbreviation :", 20, 220, 810));
		JTextField Abv = GUI.NewTextField(200, 35, 200, 850);
		TypeResources.add(Abv);

		TypeResources.add(GUI.NewLabel("Nom complet :", 20, 520, 810));
		JTextField Name = GUI.NewTextField(200, 35, 500, 850);
		TypeResources.add(Name);

		JButton Add = GUI.NewButton("Ajouter", new Color(100, 150, 215), 20, 150, 40, 800, 850);
		TypeResources.add(Add);
		Add.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				if(table.getRowCount()>=24)
				{
					JOptionPane.showMessageDialog(null, "Vous ne pouvez pas ajouter plus que 24 types.", "Erreur",
							JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					int Id = TypeRessource.getMaxId() + 1;
					String abv = Abv.getText();
					String nom = Name.getText();
					if (nom.length() > 0 && nom.length() > 0)
					{
						try
						{
							TypeRessource.add(abv, nom);
							History.add(Login.username, "Ajouter le type de ressource " + Id);
							ResultSet rs = TypeRessource.getTypes();
							table.setModel(DbUtils.resultSetToTableModel(rs));
							connection.commit();
							Abv.setText("");
							Name.setText("");
						} catch (Exception ex)
						{
							ex.printStackTrace();
							JOptionPane.showMessageDialog(null, "Vérifiez votre connection puis réessayez.", "Erreur",
									JOptionPane.ERROR_MESSAGE);
						}
					} else
					{
						JOptionPane.showMessageDialog(null, "Veuillez Vérifier vos entrées.", "Erreur",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		TypeResources.add(GUI.NewImage("res/User/AdminBackground.png", 1920, 1080, 0, 0));

		TypeResources.setLayout(null);
		return TypeResources;
	}

	public static void setButtonColor(Color color)
	{
		Delete.setBackground(color);
		ChangeABV.setBackground(color);
		ChangeName.setBackground(color);
	}

	public static void resetTable()
	{
		ResultSet rs = TypeRessource.getTypes();
		table.setModel(DbUtils.resultSetToTableModel(rs));
		table.clearSelection();
		setButtonColor(Color.GRAY);
		RowSelected = false;
	}
}
