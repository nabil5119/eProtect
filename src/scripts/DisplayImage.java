package scripts;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.*;
import javax.swing.*;

public class DisplayImage
{

	public DisplayImage(BufferedImage image)
	{
		JFrame editorFrame = new JFrame("Image");
		editorFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		ImageIcon imageIcon = new ImageIcon(image);
		JLabel jLabel = new JLabel();
		jLabel.setIcon(imageIcon);

		JButton Ok = GUI.NewButton("Ok", Color.GRAY, 17, 100, 40, 410, 680);
		editorFrame.add(Ok);
		Ok.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent arg0)
			{
				editorFrame.dispose();
			}
		});

		editorFrame.getContentPane().add(jLabel, BorderLayout.CENTER);
		editorFrame.pack();
		editorFrame.setLocationRelativeTo(null);
		editorFrame.setVisible(true);
	}
}