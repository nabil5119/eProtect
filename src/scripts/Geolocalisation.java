package scripts;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jxmapviewer.viewer.GeoPosition;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Geolocalisation
{

	public static double[] getCoor(String Location)
	{
		double[] ret =
		{ 33.9716, -6.8498, 10 };
		try
		{
			URL oracle = new URL("https://www.google.com/maps/search/" + Location.replaceAll(" ", "+"));
			BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));

			String inputLine;

			double x = 0, y = 0;
			int zoom = 0;
			while ((inputLine = in.readLine()) != null)
			{
				if (inputLine.contains("zoom="))
				{
					String X = inputLine.substring(inputLine.indexOf("center=") + 7, inputLine.indexOf("%2C"));
					String Y = inputLine.substring(inputLine.indexOf("%2C") + 3, inputLine.indexOf("&amp"));
					String Zoom = inputLine.substring(inputLine.indexOf("zoom=") + 5, inputLine.indexOf("&amp") + 12);

					x = Double.valueOf(X);
					y = Double.valueOf(Y);
					zoom = Integer.valueOf(Zoom);
				}
			}
			ret[0] = x;
			ret[1] = y;
			ret[2] = zoom;
		} catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "Veuillez Vérifier vos entrées.", "Erreur",
					JOptionPane.ERROR_MESSAGE);
		}
		return ret;
	}

	public static String[] getLoc(double[] coor)
	{
		String[] ret =
		{ "", "", "", "" };
		try
		{
			double lat = (int) (coor[0] * 10000) / 10000.0;
			double lon = (int) (coor[1] * 10000) / 10000.0;

			String url = "https://nominatim.openstreetmap.org/reverse?email=smaraoui2@gmail.com&format=xml&lat=" + lat
					+ "&lon=" + lon + "&addressdetails=1&accept-language=fr";
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new URL(url).openStream());
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("result");
			Node nNode = nList.item(0);
			Element eElement = (Element) nNode;
			ret[0] = Crypting.CharCoding(eElement.getAttribute("ref"));

			nList = doc.getElementsByTagName("addressparts");
			for (int temp = 0; temp < nList.getLength(); temp++)
			{

				nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE)
				{
					eElement = (Element) nNode;
					if (eElement.getElementsByTagName("road").item(0) != null)
					{
						ret[1] = Crypting.CharCoding(eElement.getElementsByTagName("road").item(0).getTextContent());
					}
					if (eElement.getElementsByTagName("city_district").item(0) != null)
					{
						ret[2] = Crypting
								.CharCoding(eElement.getElementsByTagName("city_district").item(0).getTextContent());
					}
					if (eElement.getElementsByTagName("city").item(0) != null)
					{
						ret[3] = Crypting.CharCoding(eElement.getElementsByTagName("city").item(0).getTextContent());
					}
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return ret;
	}

	public static int[] getDistance(GeoPosition Pos1, GeoPosition Pos2)
	{
		int[] ret = new int[2];
		try
		{
			String url = "http://www.yournavigation.org/api/1.0/gosmore.php?format=kml&flat=" + Pos1.getLatitude()
					+ "&flon=" + Pos1.getLongitude() + "&tlat=" + Pos2.getLatitude() + "&tlon=" + Pos2.getLongitude()
					+ "&v=motorcar&fast=1&layer=mapnik";
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new URL(url).openStream());
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("Document");

			for (int temp = 0; temp < nList.getLength(); temp++)
			{

				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE)
				{
					Element eElement = (Element) nNode;
					ret[0] = Math.round(
							Float.valueOf(eElement.getElementsByTagName("distance").item(0).getTextContent()) * 1000);
					ret[1] = Math.round(
							Float.valueOf(eElement.getElementsByTagName("traveltime").item(0).getTextContent()) * 1000);
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return ret;
	}

	public static List<GeoPosition> getRouting(GeoPosition Pos1, GeoPosition Pos2)
	{
		List<GeoPosition> track = new ArrayList<GeoPosition>();
		try
		{
			String url = "http://www.yournavigation.org/api/1.0/gosmore.php?format=kml&flat=" + Pos1.getLatitude()
					+ "&flon=" + Pos1.getLongitude() + "&tlat=" + Pos2.getLatitude() + "&tlon=" + Pos2.getLongitude()
					+ "&v=motorcar&fast=1&layer=mapnik";

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new URL(url).openStream());
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("Document");

			for (int temp = 0; temp < nList.getLength(); temp++)
			{

				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE)
				{
					Element eElement = (Element) nNode;

					String[] coords = eElement.getElementsByTagName("coordinates").item(0).getTextContent().split("\n");
					for (int i = 0; i < coords.length - 1; i++)
					{
						String[] coord = coords[i].split(",");
						GeoPosition pos = new GeoPosition(Double.valueOf(coord[1]), Double.valueOf(coord[0]));
						track.add(pos);
					}
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return track;
	}

}
