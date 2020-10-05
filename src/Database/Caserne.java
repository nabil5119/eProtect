package Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.jxmapviewer.viewer.GeoPosition;

import userInterface.Login;

/************************************************************
 * La classe Caserne contient les methodes qui permettent la gestion des
 * casernes dans l'application.
 * 
 * @author A.S.E.D.S
 * @version 7.02
 *****************************/
public class Caserne
{
	/**
	 * Méthode.<BR>
	 * Permet d'inserer une nouvelle caserne composé de l'identifiant de
	 * caserne,numéro de telephone de la caserne, son nom, la rue, la commune et
	 * l'identifiant de la ville de la caserne et aussi la longitude et la latitude
	 * de la localisation de la caserne<BR>
	 * 
	 * @param IdCaserne  L'identifiant de la caserne.
	 *
	 * @param NTelephone Le numéro de telephone de la caserne.
	 *
	 * @param Nom        Le nom de la caserne.
	 * 
	 * @param rue        Le rue de la caserne.
	 * 
	 * @param commune    La commune de la caserne.
	 * 
	 * @param IdVille    L'identifiant de la ville de la caserne.
	 * 
	 * @param longitude  La longitude de la localisation de la caserne.
	 * 
	 * @param latitude   La latitude de la localisation de la caserne.
	 * 
	 * @throws SQLException Si on n’arrive pas à insérer les données.
	 *
	 */
	public static void insert(int IdCaserne, String NTelephone, String Nom, String rue, String commune, int IdVille,
			double longitude, double latitude) throws SQLException
	{
		Connection connection = Login.connection;

		String query = "INSERT INTO caserne Values (" + IdCaserne + ",'" + NTelephone + "','" + Nom + "','" + rue
				+ "','" + commune + "'," + IdVille + "," + longitude + "," + latitude + ");";
		Statement statement;
		statement = connection.createStatement();
		statement.executeUpdate(query);
	}

	/**
	 * Méthode.<BR>
	 * Permet de supprimer une caserne identifier par son identifiant caserne. <BR>
	 * 
	 * @param IdCaserne L'identifiant de la caserne à supprimer.
	 *
	 **/
	public static void delete(int IdCaserne)
	{
		Connection connection = Login.connection;

		String query = "DELETE FROM caserne WHERE IdCaserne=" + IdCaserne + ";";
		Statement statement;
		try
		{
			statement = connection.createStatement();
			statement.executeUpdate(query);
		} catch (SQLException e)
		{
			System.out.println(e);
		}

	}

	/**
	 * Méthode.<BR>
	 * Permet d'avoir le nom, la longitude et la latitude de tous les casernes. <BR>
	 * 
	 * @return une matrice ( liste de listes ) qui contient le nom , la latitude et
	 *         la longitude de chaque caserne.
	 *
	 **/
	public static String[][] getInfo()
	{
		Connection connection = Login.connection;

		String query = "SELECT Nom,Latitude,Longitude FROM caserne;";
		try
		{
			Statement statement = (Statement) connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			int rowCount = 0;
			while (resultSet.next())
			{
				rowCount += 1;
			}
			String[][] ret = new String[rowCount][3];
			int row = 0;
			resultSet = statement.executeQuery(query);
			while (resultSet.next())
			{
				for (int i = 1; i <= 3; i++)
				{
					ret[row][i - 1] = resultSet.getString(i);
				}
				row += 1;
			}
			return ret;
		} catch (Exception e)
		{
			System.out.println(e);
		}
		;
		return null;
	}

	/**
	 * Méthode.<BR>
	 * Permet d'avoir le nom d'une caserne par le biais de son identifiant. <BR>
	 * 
	 * @param IdCaserne l'identifiant de la caserne dont on cherche le nom
	 * @return une chaine de caractère qui contient le nom de la caserne.
	 *
	 **/
	public static String getCaserneName(int IdCaserne)
	{
		Connection connection = Login.connection;

		try
		{
			String query = "SELECT Nom FROM caserne where idCaserne=" + IdCaserne + ";";
			Statement statement = (Statement) connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			String Caserne = "";
			if (resultSet.next())
			{
				Caserne = resultSet.getString(1);
			}
			return Caserne;
		} catch (Exception e)
		{
			System.out.println(e);
		}
		return null;
	}

	/**
	 * Méthode.<BR>
	 * Permet d'avoir la latitude et la longitude d'une caserne par le biais de son
	 * identifiant. <BR>
	 * 
	 * @param IdCaserne l'identifiant de la caserne dont on cherche les coordonnées.
	 * @return une GeoPosition qui est une liste constitué de deux élements :
	 *         latitude et longitude qui représente les coordonnées d'une caserne.
	 *
	 **/
	public static GeoPosition getCasernePosition(int IdCaserne)
	{
		Connection connection = Login.connection;

		try
		{
			GeoPosition CasernePos;
			String query = "SELECT Latitude,Longitude FROM caserne where idCaserne=" + IdCaserne + ";";
			Statement statement = (Statement) connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next())
			{
				double Lat = resultSet.getDouble(1);
				double Lon = resultSet.getDouble(2);

				CasernePos = new GeoPosition(Lat, Lon);
				return CasernePos;
			}
		} catch (Exception e)
		{
			System.out.println(e);
		}
		return null;
	}

	/**
	 * Méthode.<BR>
	 * Permet d'avoir la liste des casernes existants avec tous leurs informations.
	 * <BR>
	 *
	 * @return une ResultSet qui contient les données de tous les casernes (
	 *         Identifiant, Numero de telephone, nome de la caserne, rue, commune,
	 *         ville ).
	 *
	 **/
	public static ResultSet getCasernes()
	{
		Connection connection = Login.connection;

		try
		{
			String query = "SELECT IdCaserne,Ntelephone,caserne.Nom,Rue,Commune,ville.nom as Ville FROM caserne, ville where ville.idville=caserne.idville";
			Statement statement = connection.createStatement();
			return statement.executeQuery(query);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Méthode.<BR>
	 * Permet de changer le nom d'une caserne par le biais de son identifiant. <BR>
	 *
	 * @param IdCaserne l'identifiant de la caserne dont on veut changer le nom.
	 * @param Name      le nouveau nom.
	 * @throws SQLException Si on n’arrive pas à changer le nom d'utilisateur.
	 * 
	 **/
	public static void ChangeName(int IdCaserne, String Name) throws SQLException
	{
		Connection connection = Login.connection;

		String query = "UPDATE `Caserne` SET `nom` = '" + Name + "' WHERE (`IdCaserne` =" + IdCaserne + " );";

		Statement statement = connection.createStatement();
		statement.executeUpdate(query);

	}

	/**
	 * Méthode.<BR>
	 * Permet de changer le numéro de telephone d'une caserne par le biais de son
	 * identifiant. <BR>
	 *
	 * @param IdCaserne l'identifiant de la caserne dont on veut changer le numéro
	 *                  de telephone.
	 * @param Phone     le nouveau numéro de telephone.
	 * 
	 **/
	public static void ChangeTele(int IdCaserne, String Phone)
	{
		try
		{
			Connection connection = Login.connection;

			String query = "UPDATE `Caserne` SET `NTelephone` = '" + Phone + "' WHERE (`IdCaserne` =" + IdCaserne
					+ " );";

			Statement statement = connection.createStatement();
			statement.executeUpdate(query);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * Méthode.<BR>
	 * Permet d'avoir le plus grand identifiant pour les casernes qui existent. <BR>
	 * 
	 * @return l'identifiant de la dernière caserne enregistré.
	 **/
	public static int CaserneCount()
	{
		Connection connection = Login.connection;

		try
		{
			String query = "SELECT max(idCaserne) FROM caserne;";
			Statement statement = (Statement) connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			int Count = 0;
			if (resultSet.next())
			{
				Count = resultSet.getInt(1);
			}
			return Count;
		} catch (Exception e)
		{
			System.out.println(e);
		}
		return 0;
	}
}
