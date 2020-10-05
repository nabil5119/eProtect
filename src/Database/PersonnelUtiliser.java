package Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import userInterface.Login;

/************************************************************
 * La classe PersonnelUtiliser contient les methodes qui permettent d'associer
 * des ressources humains à un incident ainsi que liberer ces ressources.
 * 
 * @author A.S.E.D.S
 * @version 7.02
 *****************************/
public class PersonnelUtiliser
{
	/**
	 * Méthode.<BR>
	 * Permet d'associer des ressources humaines à un incident par le biais des
	 * identifiants.
	 * 
	 * @param IdIncident         l'identifiant de l'incident.
	 * @param IdRessourceHumaine l'identifiant du ressource humain à deployer.
	 *
	 **/
	public static void Insert(int IdIncident, int IdRessourceHumaine)
	{
		Connection connection = Login.connection;

		String query = "INSERT INTO personnelutiliser Values (" + IdIncident + "," + IdRessourceHumaine + ");";
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
	 * Permet de liberer tous les ressources humains utilisés pour un incident.
	 * 
	 * @param IdIncident l'identifiant de l'incident dont on veux liberer les
	 *                   ressources humains.
	 **/
	public static void SetAllResourceFree(int IdIncident)
	{
		Connection connection = Login.connection;

		try
		{
			// String query="UPDATE ressourcehumaine,PersonnelUtiliser SET
			// ressourcehumaine.estDisponible = 'true'
			// WHERE(ressourcehumaine.IdRessourceHumaine=PersonnelUtiliser.IdRessourceHumaine
			// and IdIncident ="+IdIncident+");";
			String query = "UPDATE ressourcehumaine SET estDisponible = 'true' WHERE (select IdRessourceHumaine from PersonnelUtiliser where IdRessourceHumaine=ressourcehumaine.IdRessourceHumaine and IdIncident ="
					+ IdIncident + ")";

			Statement statement = (Statement) connection.createStatement();
			statement.executeUpdate(query);

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static String[] getPersonnelUsed(int IdIncident)
	{
		Connection connection = Login.connection;
		try
		{
			int count = 0;
			String query = "Select count(*) from PersonnelUtiliser where IdIncident=" + IdIncident + ";";
			Statement statement = (Statement) connection.createStatement();
			ResultSet rs = statement.executeQuery(query);
			if (rs.next())
			{
				count = rs.getInt(1);
			}
			String[] ret = new String[count];
			query = "Select nom from PersonnelUtiliser,ressourcehumaine where  PersonnelUtiliser.IdRessourceHumaine=ressourcehumaine.IdRessourceHumaine and IdIncident="
					+ IdIncident + ";";
			statement = (Statement) connection.createStatement();
			rs = statement.executeQuery(query);
			int i = 0;
			while (rs.next())
			{
				ret[i] = rs.getString(1);
				i++;
			}
			return ret;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
