package scripts;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfWriter;

import Database.Appel;
import Database.PersonnelUtiliser;
import Database.TypeRessource;

public class WritePDF
{

	static public void InstancePDF(int idIncident, String date, String sousType, String Emplacement, String commune,
			String Rue, int[] ResourceUsed, int HumainResourceUsed)
	{
		String fileName = "fiche.pdf";
		Document doc = new Document();

		try
		{

			PdfWriter.getInstance(doc, new FileOutputStream(new File(fileName)));
			doc.open();

			Paragraph para = new Paragraph("                                            ");
			Paragraph para1 = new Paragraph("ROYAUME DU MAROC\r\n" + "MINISTERE DE L’INTERIEUR\r\n"
					+ "DIRECTION GENERALE DE LA PROTECTION CIVILE\r\n" + "COMMANDEMENT REGIONAL RABAT SALE KENITRA\r\n"
					+ "COMMANDEMENT PREFECTORAL RABAT\r\n" + "");

			Image img = Image.getInstance("res/Login/ProtectionCivile.png");
			img.scaleAbsolute(70, 80);
			img.setAbsolutePosition(50, 725);

			Image map = Image.getInstance("map.jpg");
			map.scaleAbsolute(250, 200);
			map.setAbsolutePosition(320, 450);
			doc.add(map);
			PdfPCell cell = new PdfPCell();

			para.setAlignment(Element.ALIGN_RIGHT);
			cell.addElement(para);
			cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
			cell.setBorder(Rectangle.NO_BORDER);
			doc.add(cell);

			para1.setAlignment(Element.ALIGN_CENTER);

			doc.add(img);

			doc.add(para1);
			doc.add(para);
			doc.add(para);
			doc.add(new Paragraph(
					"                                                     " + "LOCALISATION DE L’EVENEMENT SUR CARTE"));
			doc.add(para);
			doc.add(para);

			Paragraph p1 = new Paragraph("DÉPART N° :+ " + idIncident + " /CP RABAT");
			Paragraph p2 = new Paragraph("DATE ET HEURE : " + date);
			Paragraph p3 = new Paragraph("MOTIF DE DÉPART : " + sousType);
			if (Emplacement.length() > 50)
			{
				String[] words = Emplacement.split(" ");
				Emplacement = "";
				for (int i = 0; i < words.length; i++)
				{
					if (i == words.length / 2)
						Emplacement += "\n";
					Emplacement += " " + words[i];
				}
			}
			Paragraph p4 = new Paragraph("ADRESSE : " + Emplacement);
			Paragraph p5 = new Paragraph("\t\tRUE : " + Rue);
			Paragraph p6 = new Paragraph("COMMUNE : " + commune);
			Paragraph p7 = new Paragraph("");
			Paragraph p8 = new Paragraph("N° APPELANT : " + Appel.getCaller(idIncident));

			doc.add(p1);
			doc.add(p2);
			doc.add(p3);
			doc.add(p4);
			doc.add(p5);
			doc.add(p6);
			doc.add(p7);
			doc.add(p8);
			doc.add(para);
			doc.add(para);
			doc.add(para);

			doc.add(new Paragraph(
					"------------------------------------------------------------------------------------------------------------------------------"));
			Paragraph pa1 = new Paragraph("MOYENS ENGAGES DU CENTRE DE SECOURS : ");
			Paragraph pa2 = new Paragraph("ENGINS : ");
			doc.add(pa1);
			doc.add(pa2);

			String[][] RessourceTypeName = TypeRessource.getRessourceTypeName();
			ArrayList<Paragraph> VehiculList = new ArrayList<Paragraph>();
			for (int i = 0; i < ResourceUsed.length; i++)
			{
				for (int j = 0; j < ResourceUsed[i]; j++)
				{
					VehiculList.add(new Paragraph(RessourceTypeName[i][0] + ": " + RessourceTypeName[i][1]));
				}
			}

			for (int i = 0; i < VehiculList.size(); i++)
			{
				doc.add(VehiculList.get(i));
			}
			doc.add(new Paragraph("NOMBRE DE PERSONNEL " + HumainResourceUsed + " :"));
			String[] PersonnelUsed = PersonnelUtiliser.getPersonnelUsed(idIncident);
			for (String name : PersonnelUsed)
			{
				doc.add(new Paragraph("- " + name));
			}

			doc.close();

		} catch (FileNotFoundException a)
		{
			a.printStackTrace();

		} catch (DocumentException b)
		{
			b.printStackTrace();
		} catch (MalformedURLException e1)
		{
			e1.printStackTrace();
		} catch (IOException e1)
		{
			e1.printStackTrace();
		}
	}
}
