package Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import userInterface.Login;

public class Resolue
{
	public static void Insert(int IdIncident, int IdCaserne)
	{
		Connection connection = Login.connection;

		String query = "INSERT INTO Resolue Values (" + IdIncident + "," + IdCaserne + ");";
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

	public static int getCaserne(int IdIncident)
	{
		Connection connection = Login.connection;

		String query = "select idCaserne from Resolue where IdIncident=" + IdIncident + ";";
		Statement statement;
		try
		{
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);
			if (rs.next())
			{
				return rs.getInt(1);
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return 0;
	}

	public static boolean isWorking(int idCaserne)
	{
		Connection connection = Login.connection;

		String query = "select count(Resolue.idIncident) from Resolue,incident where Resolue.IdIncident=incident.IdIncident and etat='En Cours' and idCaserne="
				+ idCaserne + ";";
		Statement statement;
		try
		{
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);
			if (rs.next() && rs.getInt(1) == 0)
			{
				return false;
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return true;
	}
}
