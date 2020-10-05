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
 * des actions effectu� par l'utilisateur<BR>
 * 
 * @author A.S.E.D.S
 * @version 7.02
 *****************************/
public class History
{
	/**
	 * M�thode.<BR>
	 * Permet d'ajouter l'historique d'une action compos� de son identifiant,
	 * l'identifiant de l'utilisateur qui l'a effectu�, son nom, l'action execut� et
	 * la date d'execution
	 *
	 * @param username le nom de l'utilisateur qui a effectu� l'action.
	 *
	 * @param action   l'action effectu�.
	 * 
	 * @throws SQLException Si on n�arrive pas � ins�rer les donn�es.
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
	 * M�thode.<BR>
	 * Permet d'avoir l'identifiant de la derni�re action enregistr�.
	 * 
	 * @return l'identifiant de la derni�re action enregistr�.
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
	 * M�thode.<BR>
	 * Permet d'extraire l'ensemble d'historique d'actions enregistr�s.
	 * 
	 * @return ResultSet des actions enregistr�s.
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
