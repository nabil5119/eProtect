package Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import userInterface.Login;

public class TypeIncident
{

	public static String[] getTypes()
	{
		Connection connection = Login.connection;

		String query = "SELECT Nom FROM typeincident;";
		try
		{
			Statement statement = (Statement) connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			int rowCount = 0;
			while (resultSet.next())
			{
				rowCount += 1;
			}
			String[] ret = new String[rowCount];
			resultSet = statement.executeQuery(query);
			int row = 0;
			while (resultSet.next())
			{
				ret[row] = resultSet.getString(1);
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

	public static int TypeCount()
	{
		Connection connection = Login.connection;

		try
		{
			String query = "SELECT Nom FROM typeincident;";
			Statement statement = (Statement) connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			int TypeCount = 0;
			while (resultSet.next())
			{
				TypeCount += 1;
			}
			return TypeCount;
		} catch (Exception e)
		{
			System.out.println(e);
		}
		return 0;
	}
}
