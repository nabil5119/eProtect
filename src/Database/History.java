package Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import userInterface.Login;

/************************************************************
 * La classe History permet d'enregistrer l'historique et de voir l'historique
 * des actions effectué par l'utilisateur<BR>
 * 
 * @author A.S.E.D.S
 * @version 7.02
 *****************************/
public class History
{
	/**
	 * Méthode.<BR>
	 * Permet d'ajouter l'historique d'une action composé de son identifiant,
	 * l'identifiant de l'utilisateur qui l'a effectué, son nom, l'action executé et
	 * la date d'execution
	 *
	 * @param username le nom de l'utilisateur qui a effectué l'action.
	 *
	 * @param action   l'action effectué.
	 * 
	 * @throws SQLException Si on n’arrive pas à insérer les données.
	 **/
	public static void add(String username, String action) throws SQLException
	{
		Connection connection = Login.connection;

		int idAction = count() + 1;
		int idUser = Utilisateur.getID(username);
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
		String strDate = dateFormat.format(date);
		String query = "INSERT INTO historique Values (" + idAction + "," + idUser + ",'" + username + "',\"" + action
				+ "\",'" + strDate + "');";
		Statement statement;
		statement = connection.createStatement();
		statement.executeUpdate(query);

	}

	/**
	 * Méthode.<BR>
	 * Permet d'avoir l'identifiant de la dernière action enregistré.
	 * 
	 * @return l'identifiant de la dernière action enregistré.
	 **/
	public static int count()
	{
		Connection connection = Login.connection;

		try
		{
			String query = "SELECT idaction FROM historique;";
			Statement statement = (Statement) connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			int Count = 0;
			while (resultSet.next())
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

	/**
	 * Méthode.<BR>
	 * Permet d'extraire l'ensemble d'historique d'actions enregistrés.
	 * 
	 * @return ResultSet des actions enregistrés.
	 **/
	public static ResultSet getHistory()
	{
		Connection connection = Login.connection;

		try
		{
			String query = "SELECT * FROM historique;";
			Statement statement = (Statement) connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			return resultSet;
		} catch (Exception e)
		{
			System.out.println(e);
		}
		return null;
	}
}
