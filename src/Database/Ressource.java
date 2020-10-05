package Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import userInterface.Login;

public class Ressource
{
	public static int UseIdRessource(int IdCaserne, int IdTypeRessource)
	{
		Connection connection = Login.connection;
		try
		{
			String query = "SELECT IdRessource FROM ressource where estDisponible='true' and IdTypeRessource="
					+ IdTypeRessource + " and IdCaserne=" + IdCaserne + ";";
			Statement statement = (Statement) connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			int IdRessource = 0;
			if (resultSet.next())
			{
				IdRessource = Integer.valueOf(resultSet.getString(1));
				setRessourceStatue(IdRessource, false);
			}
			return IdRessource;
		} catch (Exception e)
		{
			System.out.println(e);
		}
		return 0;
	}

	public static int[] getResources(int IdCaserne, boolean All)
	{
		int[] ret = new int[TypeRessource.getAllRessourceTypeCount()];
		Connection connection = Login.connection;
		try
		{
			String query = "SELECT IdTypeRessource,count(IdTypeRessource) FROM ressource where estDisponible='true' and IdCaserne="
					+ IdCaserne + " group by IdTypeRessource;";
			if (All)
			{
				query = "SELECT IdTypeRessource,count(IdTypeRessource) FROM ressource where IdCaserne=" + IdCaserne
						+ " group by IdTypeRessource;";
			}
			Statement statement = (Statement) connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			while (resultSet.next())
			{
				ret[resultSet.getInt(1)] = Integer.valueOf(resultSet.getInt(2));
			}
			return ret;
		} catch (Exception e)
		{
			System.out.println(e);
		}
		;
		return ret;
	}

	public static boolean isRessourceFree(int IdRessource)
	{
		Connection connection = Login.connection;

		try
		{
			String query = "SELECT estDisponible FROM ressource where IdRessource=" + IdRessource + ";";
			Statement statement = (Statement) connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next() && resultSet.getString(1).equals("true"))
			{
				return true;
			}
		} catch (Exception e)
		{
			System.out.println(e);
		}
		return false;
	}

	public static void setRessourceStatue(int IdRessource, boolean status)
	{
		Connection connection = Login.connection;

		try
		{
			String query = "UPDATE ressource SET estDisponible = '" + status + "' WHERE (IdRessource =" + IdRessource
					+ ");";
			Statement statement = (Statement) connection.createStatement();
			statement.executeUpdate(query);
		} catch (Exception e)
		{
			System.out.println(e);
		}
	}

	public static int getAllFreeResources(int IdCaserne)
	{
		Connection connection = Login.connection;

		try
		{
			int sum = 0;
			String query = "SELECT count(*) FROM ressource where estDisponible='true' and IdCaserne=" + IdCaserne + ";";
			Statement statement = (Statement) connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next())
			{
				sum = resultSet.getInt(1);
			}
			return sum;
		}

		catch (Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}

	public static int getAllFreeResources(int IdCaserne, int IdTypeRessource)
	{
		Connection connection = Login.connection;

		try
		{
			int sum = 0;
			String query = "SELECT count(*) FROM ressource where estDisponible='true' and IdCaserne=" + IdCaserne
					+ " and IdTypeRessource=" + IdTypeRessource + ";";
			Statement statement = (Statement) connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next())
			{
				sum = resultSet.getInt(1);
			}
			return sum;
		}

		catch (Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}

	public static void addResource(int IdCaserne, int IdTypeRessource) throws Exception
	{
		Connection connection = Login.connection;

		Random rand = new Random();
		int IdRessource = GetMaxId() + 1;
		int r = rand.nextInt(100);
		int mrand = 64 + (int) (rand.nextFloat() * (90 - 65));
		String mat = (char) mrand + "" + r + "" + (r - r / 10);
		String query = "INSERT INTO ressource Values (" + IdRessource + ",'" + mat + "','" + IdCaserne + "','"
				+ IdTypeRessource + "','true');";
		Statement statement;

		statement = connection.createStatement();
		statement.executeUpdate(query);

	}

	public static int GetResourceCount()
	{
		Connection connection = Login.connection;

		try
		{
			String query = "SELECT * FROM ressource;";
			Statement statement = (Statement) connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			int Count = 0;
			while (resultSet.next())
			{
				Count += 1;
			}
			return Count;
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return 0;
	}

	public static int GetMaxId()
	{
		Connection connection = Login.connection;

		try
		{
			String query = "SELECT max(idressource) FROM ressource;";
			Statement statement = (Statement) connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			int Count = 0;
			if (resultSet.next())
			{
				Count = resultSet.getInt(1);
			}
			return Count;
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return 0;
	}

	public static void DeleteAll(int IdCaserne)
	{
		Connection connection = Login.connection;

		try
		{
			String query = "Delete FROM ressource where IdCaserne=" + IdCaserne + ";";
			Statement statement = (Statement) connection.createStatement();
			statement.executeUpdate(query);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public static void Delete(int IdCaserne, int IdTypeResource)
	{
		Connection connection = Login.connection;

		try
		{
			String query = "Delete FROM ressource where IdRessource= (select max(IdRessource) FROM ressource where estDisponible='true' and IdCaserne="
					+ IdCaserne + " and idTypeRessource=" + IdTypeResource + ");";
			Statement statement = (Statement) connection.createStatement();
			statement.executeUpdate(query);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}
