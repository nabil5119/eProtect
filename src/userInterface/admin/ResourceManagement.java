package userInterface.admin;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import Database.Caserne;
import Database.Ressource;
import Database.RessourceHumaine;
import Database.TypeRessource;
import scripts.CustomDialog;
import scripts.GUI;
import userInterface.Login;

public class ResourceManagement
{
	static JFrame frame;
	static JTextField[] resources;
	static JTextField HumainResource;
	static Connection connection;

	public static void init(int[] res, boolean isNew, int SelectedIdCaserne, boolean editable)
	{
		frame = new JFrame();
		connection = Login.connection;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		float scaley = (float) screenSize.getHeight() / 1080;
		float scalex = (float) screenSize.getWidth() / 1920;
		frame.setSize((int) (650 * scalex), (int) (650 * scaley));
		frame.setLocationRelativeTo(null);
		frame.setLayout(null);
		frame.setResizable(false);
		frame.setUndecorated(true);
		frame.add(GUI.NewLabel("Caserne : " + Caserne.getCaserneName(SelectedIdCaserne), 30, 8, 5));
		frame.add(GUI.NewLabel("Ressource Matérielle", 30, 8, 55));
		int AllRessourceTypeCount = TypeRessource.getAllRessourceTypeCount();
		resources = new JTextField[AllRessourceTypeCount];

		int j = 0;
		int Xoffset = 0;
		int Yoffset = 0;
		String[][] RessourceTypeNames = TypeRessource.getRessourceTypeName();
		for (String[] RessourceTypeName : RessourceTypeNames)
		{
			if (j == Math.ceil(AllRessourceTypeCount / 3.0))
			{
				Xoffset = 250;
				Yoffset = 0;
			}
			if (j == Math.ceil(AllRessourceTypeCount * 2 / 3.0))
			{
				Xoffset = 450;
				Yoffset = 0;
			}

			JLabel label = GUI.NewLabel(RessourceTypeName[0] + " :", Color.black, 20, 120, 40, 20 + Xoffset,
					100 + Yoffset);
			label.setToolTipText(TypeRessource.getName(j));
			frame.add(label);
			resources[j] = GUI.NewTextField(50, 25, 130 + Xoffset, 110 + Yoffset);
			resources[j].setText(String.valueOf(res[j]));
			resources[j].setEditable(editable);
			frame.add(resources[j]);

			Yoffset += 50;
			j += 1;
		}

		frame.add(GUI.NewLabel("Ressource Humaine", 30, 8, 505));
		frame.add(GUI.NewLabel("Nombre de personnelle :", 22, 8, 555));
		HumainResource = GUI.NewTextField(50, 25, 300, 560);
		HumainResource.setText(String.valueOf(res[j]));
		HumainResource.setEditable(editable);
		frame.add(HumainResource);

		JButton Confirm = GUI.NewButton("Confirmer", Color.black, 20, 150, 35, 250, 605);
		frame.add(Confirm);
		Confirm.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				if (isNew)
				{
					int sum = 0;
					for (int i = 0; i < resources.length; i++)
					{
						String str = resources[i].getText();
						if (isInt(str))
						{
							sum += Integer.valueOf(str);
						}
					}
					int Humaineresources = 0;
					if (isInt(HumainResource.getText()))
					{
						Humaineresources = Integer.valueOf(HumainResource.getText());
					}
					Casernes.Ressources.setText("Véhicules: " + sum + "; Perso. : " + Humaineresources);
				} else
				{
					int j = 0;
					for (; j < res.length - 1; j++)
					{
						int newValue = Integer.valueOf(resources[j].getText());
						if (res[j] < newValue)
						{
							for (int k = 0; k < newValue - res[j]; k++)
							{
								try
								{
									Ressource.addResource(SelectedIdCaserne, j);
									connection.commit();

								} catch (Exception e1)
								{
									e1.printStackTrace();
								}
							}
						} else if (newValue < res[j])
						{
							int freeRes = Ressource.getAllFreeResources(SelectedIdCaserne, j);
							if (newValue >= freeRes)
							{
								for (int k = 0; k < res[j] - newValue; k++)
								{
									Ressource.Delete(SelectedIdCaserne, j);
									try{connection.commit();} 
									catch (SQLException e1){e1.printStackTrace();}
								}

							} else
							{
								JOptionPane
										.showMessageDialog(null,
												freeRes + " véhicule de type " + TypeRessource.getName(j)
														+ " gère encore des incidents",
												"Erreur", JOptionPane.ERROR_MESSAGE);
							}
						}
					}
					int newValue = Integer.valueOf(HumainResource.getText());
					int workingRes = RessourceHumaine.getAllWorkingResources(SelectedIdCaserne);
					if(newValue>=workingRes && newValue!=res[j])
					{
						RessourceHumaine.DeleteAll(SelectedIdCaserne);
						for(int i=0;i<newValue-workingRes;i++)
						{
							CustomDialog customDialog = new CustomDialog(null, "nom");
							customDialog.pack();
							customDialog.setLocationRelativeTo(null);
							customDialog.setVisible(true);
							if (customDialog.getValidatedText() != null)
							{
								try
								{
									RessourceHumaine.addResource(SelectedIdCaserne,customDialog.getValidatedText());
									connection.commit();
								} 
								catch (Exception e1)	{e1.printStackTrace();}
							}
						}
						
					}
					if(newValue<workingRes)
					{
						JOptionPane.showMessageDialog(null, workingRes + " personnelle gère encore des incidents",
								"Erreur", JOptionPane.ERROR_MESSAGE);
					}
				}
				Casernes.windowopen = false;
				frame.dispose();
			}
		});

		frame.add(GUI.NewImage("res/User/ResourceManagement.png", 650, 650, 0, 0));

		frame.setVisible(true);
	}

	public static int[] getResource()
	{
		int[] ret = new int[resources.length];
		for (int i = 0; i < ret.length; i++)
		{
			if (resources[i].getText().equals(""))
				ret[i] = 0;
			else
				ret[i] = Integer.valueOf(resources[i].getText());
		}
		return ret;
	}

	public static int getHumaineResource()
	{
		if (!HumainResource.getText().equals(""))
		{
			return Integer.valueOf(HumainResource.getText());
		}
		return 0;
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
