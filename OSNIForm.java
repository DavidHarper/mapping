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

import java.lang.Math;
import java.text.*;

import java.awt.*;
import java.awt.event.*;

import com.obliquity.mapping.*;

public class OSNIForm extends Panel implements ActionListener {
	OSNI osni;
	Separator sep;
	TextField eastings, northings;
	List prefix;
	Label longitude, latitude, gridinput;
	Button calculate;
	NumberFormat myFormat = NumberFormat.getInstance();

	public OSNIForm() {
		osni = new OSNI();

		myFormat.setMaximumFractionDigits(2);
		myFormat.setMinimumFractionDigits(2);

		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();

		setLayout(gbl);

		Label label = new Label("OSNI to Latitude/Longitude");

		label.setFont(new Font("SansSerif", Font.BOLD + Font.ITALIC, 18));
		label.setForeground(Color.blue);

		gbc.anchor = GridBagConstraints.NORTH;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(label, gbc);

		sep = new Separator();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 0, 5, 0);
		add(sep, gbc);

		label = new Label("Eastings (metres):");
		gbc.gridwidth = 1;
		gbc.insets = new Insets(0, 0, 0, 0);
		add(label, gbc);

		eastings = new TextField(10);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(eastings, gbc);

		label = new Label("Northings (metres):");
		gbc.gridwidth = 1;
		gbc.insets = new Insets(0, 0, 0, 0);
		add(label, gbc);

		northings = new TextField(10);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(northings, gbc);

		label = new Label("Grid prefix:");
		gbc.gridwidth = 1;
		gbc.insets = new Insets(0, 0, 0, 0);
		add(label, gbc);

		prefix = new List(5);
		populatePrefixes(prefix);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(prefix, gbc);

		sep = new Separator();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 0, 5, 0);
		add(sep, gbc);

		Panel buttonpanel = new Panel();
		buttonpanel.setLayout(new BorderLayout());
		calculate = new Button("Convert");
		buttonpanel.add(calculate, BorderLayout.CENTER);

		calculate.addActionListener(this);

		gbc.insets = new Insets(0, 0, 0, 0);
		add(buttonpanel, gbc);

		sep = new Separator();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 0, 5, 0);
		add(sep, gbc);

		gridinput = new Label();
		gridinput.setForeground(Color.red);
		gbc.insets = new Insets(0, 0, 5, 0);
		add(gridinput, gbc);

		label = new Label("Latitude:");
		label.setForeground(Color.blue);
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.gridwidth = 1;
		add(label, gbc);

		longitude = new Label();
		longitude.setForeground(Color.blue);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(longitude, gbc);

		label = new Label("Longitude:");
		label.setForeground(Color.blue);
		gbc.gridwidth = 1;
		gbc.insets = new Insets(0, 0, 0, 0);
		add(label, gbc);

		latitude = new Label();
		latitude.setForeground(Color.blue);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(latitude, gbc);

		sep = new Separator();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 0, 2, 0);
		add(sep, gbc);

		Label obliquitylabel = new Label(
				"Copyright \u00a9 2002 by Obliquity.com");

		obliquitylabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
		obliquitylabel.setForeground(Color.blue);

		gbc.anchor = GridBagConstraints.NORTH;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(obliquitylabel, gbc);
	}

	private void populatePrefixes(List list) {
		String[] prefixes = { "B", "C", "D", "G", "H", "J" };

		for (int j = 0; j < prefixes.length; j++)
			list.add(prefixes[j]);

		list.select(0);
	}

	public void actionPerformed(ActionEvent ae) {
		double x = 0.0, y = 0.0;
		Double d;

		try {
			d = new Double(eastings.getText());
			x = d.doubleValue();
		} catch (NumberFormatException nfe) {
			eastings.setText("0");
			x = 0.0;
		}

		try {
			d = new Double(northings.getText());
			y = d.doubleValue();
		} catch (NumberFormatException nfe) {
			northings.setText("0");
			y = 0.0;
		}

		DPoint p0 = new DPoint(x, y);

		String pfxs = prefix.getSelectedItem();

		DPoint dp = osni.GridSquareToOffset(pfxs.charAt(0));
		dp.offsetBy(p0);

		DPoint p = osni.GridToLongitudeAndLatitude(dp);

		double phi, lambda;

		lambda = (180.0 / Math.PI) * p.getX();
		phi = (180.0 / Math.PI) * p.getY();

		double ls = Math.abs(lambda);
		double ps = Math.abs(phi);

		int ld, lm, pd, pm;

		ld = (int) ls;
		ls = 60.0 * (ls - ld);
		lm = (int) ls;
		ls = 60.0 * (ls - lm);

		pd = (int) ps;
		ps = 60.0 * (ps - pd);
		pm = (int) ps;
		ps = 60.0 * (ps - pm);

		String str = ld + "\u00b0 " + lm + "\' " + myFormat.format(ls) + "\""
				+ ((lambda < 0.0) ? " West" : " East");

		latitude.setText(str);

		str = pd + "\u00b0 " + pm + "\' " + myFormat.format(ps) + "\""
				+ ((phi < 0.0) ? " South" : " North");

		longitude.setText(str);

		str = pfxs + " " + myFormat.format(p0.getX()) + " "
				+ myFormat.format(p0.getY());
		gridinput.setText(str);
	}
}
