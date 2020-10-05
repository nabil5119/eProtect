package Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import userInterface.Login;

public class Ville
{

	public static String getCityName(int idVille)
	{
		Connection connection = Login.connection;

		try
		{
			String query = "SELECT Nom FROM ville where idVille=" + idVille + ";";
			Statement statement = (Statement) connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			String ville = "";
			if (resultSet.next())
			{
				ville = resultSet.getString(1);
			}
			return ville;
		} catch (Exception e)
		{
			System.out.println(e);
		}
		return null;
	}

	public static String[] getAllCities()
	{

		try
		{
			int CityCount = getCityCount();
			String[] CitiesNames = new String[CityCount];
			for (int i = 0; i < CityCount; i++)
			{
				CitiesNames[i] = getCityName(i);
			}
			return CitiesNames;
		} catch (Exception e)
		{
			System.out.println(e);
		}
		return null;
	}

	public static int getCityCount()
	{
		Connection connection = Login.connection;

		try
		{
			String query = "SELECT * FROM ville;";
			Statement statement = (Statement) connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			int CityCount = 0;
			if (resultSet.next())
			{
				CityCount += 1;
			}
			return CityCount;
		} catch (Exception e)
		{
			System.out.println(e);
		}
		return 0;
	}
}
