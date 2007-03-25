/*
 * Map projections package
 *
 * Demonstration applet.
 *
 * Copyright (C) 2002 David Harper at obliquity.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 * 
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 *
 * See the COPYING file located in the top-level-directory of
 * the archive of this library for complete text of license.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.text.*;

import com.obliquity.mapping.*;

public class OSGBReverseForm extends JPanel implements ActionListener {
	OSGB osgb;
	JSeparator sep;
	JTextField latd, latm, lats, lngd, lngm, lngs;
	JCheckBox east;
	JLabel prefix, eastings, northings;
	JButton calculate;
	NumberFormat myFormat = NumberFormat.getInstance();

	public OSGBReverseForm() {
		super();
		
		osgb = new OSGB();

		myFormat.setMaximumFractionDigits(0);
		myFormat.setMinimumFractionDigits(0);

		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();

		setLayout(gbl);

		JLabel label = new JLabel("Latitude/Longitude to OSGB");

		label.setFont(new Font("SansSerif", Font.BOLD + Font.ITALIC, 18));
		label.setForeground(Color.blue);

		gbc.anchor = GridBagConstraints.NORTH;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(label, gbc);

		sep = new JSeparator();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 0, 5, 0);
		add(sep, gbc);

		label = new JLabel("Longitude:");
		gbc.gridwidth = 1;
		gbc.insets = new Insets(0, 0, 0, 0);
		add(label, gbc);

		lngd = new JTextField(2);
		add(lngd, gbc);

		add(new Label("d"), gbc);

		lngm = new JTextField(2);
		add(lngm, gbc);

		add(new Label("m"), gbc);

		lngs = new JTextField(5);
		add(lngs, gbc);

		add(new Label("s"), gbc);

		ButtonGroup cbg = new ButtonGroup();
		east = new JCheckBox("East", true);
		JCheckBox west = new JCheckBox("West", false);
		cbg.add(east);
		cbg.add(west);

		add(east, gbc);

		gbc.gridwidth = GridBagConstraints.REMAINDER;

		add(west, gbc);

		label = new JLabel("Latitude:");
		gbc.gridwidth = 1;
		gbc.insets = new Insets(0, 0, 0, 0);
		add(label, gbc);

		latd = new JTextField(2);
		add(latd, gbc);

		add(new Label("d"), gbc);

		latm = new JTextField(2);
		add(latm, gbc);

		add(new Label("m"), gbc);

		lats = new JTextField(5);
		add(lats, gbc);

		gbc.gridwidth = GridBagConstraints.REMAINDER;

		add(new Label("s"), gbc);

		sep = new JSeparator();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 0, 5, 0);
		add(sep, gbc);

		Panel buttonpanel = new Panel();
		buttonpanel.setLayout(new BorderLayout());
		calculate = new JButton("Convert");
		buttonpanel.add(calculate, BorderLayout.CENTER);

		calculate.addActionListener(this);

		gbc.insets = new Insets(0, 0, 0, 0);
		add(buttonpanel, gbc);

		sep = new JSeparator();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 0, 5, 0);
		add(sep, gbc);

		label = new JLabel("Prefix:");
		label.setForeground(Color.blue);
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.gridwidth = 1;
		add(label, gbc);

		prefix = new JLabel();
		prefix.setForeground(Color.blue);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(prefix, gbc);

		label = new JLabel("Eastings:");
		label.setForeground(Color.blue);
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.gridwidth = 1;
		add(label, gbc);

		eastings = new JLabel();
		eastings.setForeground(Color.blue);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(eastings, gbc);

		label = new JLabel("Northings:");
		label.setForeground(Color.blue);
		gbc.gridwidth = 1;
		gbc.insets = new Insets(0, 0, 0, 0);
		add(label, gbc);

		northings = new JLabel();
		northings.setForeground(Color.blue);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(northings, gbc);

		sep = new JSeparator();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 0, 2, 0);
		add(sep, gbc);

		Label obliquitylabel = new Label(
				"Copyright \u00a9 2004 by Obliquity.com");

		obliquitylabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
		obliquitylabel.setForeground(Color.blue);

		gbc.anchor = GridBagConstraints.NORTH;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(obliquitylabel, gbc);
	}

	public void actionPerformed(ActionEvent ae) {
		double x = 0.0, y = 0.0;

		x = parseAngleFields(lngd, lngm, lngs) * Math.PI / 180.0;

		if (!east.isSelected())
			x = -x;

		y = parseAngleFields(latd, latm, lats) * Math.PI / 180.0;

		DPoint p0 = new DPoint(x, y);

		DPoint p = osgb.LatitudeAndLongitudeToGrid(p0);

		char[] pfx = osgb.GridToGridSquare(p);

		prefix.setText(new String(pfx));
		eastings.setText(myFormat.format(p.getX() % 100000.0));
		northings.setText(myFormat.format(p.getY() % 100000.0));
	}

	private double parseAngleFields(JTextField degrees, JTextField minutes,
			JTextField seconds) {
		double d, m, s;

		try {
			d = Double.parseDouble(degrees.getText());
		} catch (NumberFormatException nfe) {
			d = 0.0;
		}

		try {
			m = Double.parseDouble(minutes.getText());
		} catch (NumberFormatException nfe) {
			m = 0.0;
		}

		try {
			s = Double.parseDouble(seconds.getText());
		} catch (NumberFormatException nfe) {
			s = 0.0;
		}

		return d + m / 60.0 + s / 3600.0;
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame("Lat/long to OSGB");
				frame.getContentPane().add(new OSGBReverseForm());
				frame.pack();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}
}
