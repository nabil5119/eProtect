package Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import userInterface.Login;

public class Utiliser
{
	public static void Insert(int IdIncident, int IdRessource)
	{
		Connection connection = Login.connection;

		String query = "INSERT INTO utiliser Values (" + IdIncident + "," + IdRessource + ");";
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

	public static void SetAllResourceFree(int IdIncident)
	{
		Connection connection = Login.connection;

		try
		{
			String query = "UPDATE ressource SET estDisponible = 'true' WHERE (select idressource from utiliser where idressource=ressource.IdRessource and IdIncident ="
					+ IdIncident + ")";
			// String query="UPDATE Ressource, utiliser SET Ressource.estDisponible = 'true'
			// WHERE(Ressource.idRessource=Utiliser.idRessource and IdIncident
			// ="+IdIncident+");";
			Statement statement = (Statement) connection.createStatement();
			statement.executeUpdate(query);

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static int[] getRessourceUsed(int IdIncident)
	{
		Connection connection = Login.connection;
		try
		{
			int[] ret = new int[TypeRessource.getAllRessourceTypeCount()];
			String query = "Select IdTypeRessource,count(IdTypeRessource) from Utiliser,Ressource where Ressource.IdRessource=Utiliser.IdRessource and IdIncident="
					+ IdIncident + " group by IdTypeRessource;";
			Statement statement = (Statement) connection.createStatement();
			ResultSet rs = statement.executeQuery(query);
			while (rs.next())
			{
				ret[rs.getInt(1)] = rs.getInt(2);
			}
			return ret;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
