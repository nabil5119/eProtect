package Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import userInterface.Login;

/************************************************************
 * La classe Appel contient les methodes qui permettent la gestion des appels
 * dans l'application.
 * 
 * @author A.S.E.D.S
 * @version 7.02
 *****************************/
public class Appel
{
	/**
	 * Méthode.<BR>
	 * Permet d'inserer un appel composé de l'identifiant de l'appel,numéro de
	 * telephone, le nom de l'appelant et l'identifiant de l'incident <BR>
	 * L'ID de l'appel et l'ID de l'incident sont extraitent à l'aide des
	 * methodes @see {@link Appel#AppelCount()} et @see
	 * {@link Incident#IncidentCount()} <BR>
	 * 
	 * @param NTelephone Le numéro de telephone de l'appel qu'on veut enregister.
	 *
	 * @param Nom        Le nom de l'appelant.
	 *
	 * @throws SQLException Si on n’arrive pas à insérer les données.
	 */
	public static void insert(String NTelephone, String Nom) throws SQLException
	{
		Connection connection = Login.connection;

		int IdAppel = AppelCount() + 1;
		int IdIncident = Incident.IncidentCount() + 1;
		String query = "INSERT INTO appel Values ('" + IdAppel + "','" + NTelephone + "','" + Nom + "'," + IdIncident
				+ ");";
		Statement statement;

		statement = connection.createStatement();
		statement.executeUpdate(query);

	}

	/**
	 * Méthode.<BR>
	 * Permet de supprimer un appel composé de l'identifiant de l'appel,numéro de
	 * telephone, le nom de l'appelant et l'identifiant de l'incident et qui est
	 * identifié par son Id d'appel<BR>
	 * 
	 * @param IdAppel L'identifiant de l'appel qu'on veut supprimer.
	 */
	public static void delete(int IdAppel)
	{
		Connection connection = Login.connection;

		String query = "DELETE FROM appel WHERE IdAppel=" + IdAppel + ";";
		Statement statement;
		try
		{
			statement = connection.createStatement();
			statement.executeUpdate(query);

		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Méthode.<BR>
	 * Permet de trouver le plus grand identifiant enregistrés. <BR>
	 *
	 * @return le plus grand ID.
	 */
	public static int AppelCount()
	{
		Connection connection = Login.connection;

		try
		{
			String query = "SELECT max(idAppel) FROM appel;";
			Statement statement = (Statement) connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			int AppelCount = 0;
			if (resultSet.next())
			{
				AppelCount = resultSet.getInt(1);
			}
			return AppelCount;
		} catch (Exception e)
		{
			System.out.println(e);
		}
		return 0;
	}

	/**
	 * Méthode.<BR>
	 * Permet d'extraire tous les appels qui existent dans la table appel.<BR>
	 *
	 * @return Un ResultSet qui contient tous les appels qui existent dans la table
	 *         appel.
	 */
	public static ResultSet getCalls()
	{
		Connection connection = Login.connection;

		try
		{
			String query = "SELECT * FROM appel;";
			Statement statement = (Statement) connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			return resultSet;
		} catch (Exception e)
		{
			System.out.println(e);
		}
		return null;
	}

	/**
	 * Méthode.<BR>
	 * Permet d'extraire le numéro de l'appelant par le biais du numéro de
	 * l'incident.<BR>
	 *
	 * @param idIncident l'identifiant de l'incident dont on cherche le numéro de
	 *                   l'appelant.
	 *
	 * @return Une chaine de caractère qui contient le numéro de l'appelant.
	 */
	public static String getCaller(int idIncident)
	{
		Connection connection = Login.connection;

		try
		{
			String query = "SELECT NTelephone FROM appel where IdIncident=" + idIncident + ";";
			Statement statement = (Statement) connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next())
				return resultSet.getString(1);
		} catch (Exception e)
		{
			System.out.println(e);
		}
		return null;
	}
}
