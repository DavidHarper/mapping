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

import java.text.*;

import java.awt.*;
import java.awt.event.*;

import com.obliquity.mapping.*;

public class OSNI2OSGBForm extends Panel implements ActionListener {
    OSNI osni;
    OSGB osgb;
    Separator sep;
    TextField eastings, northings;
    List prefix;
    Label osgbPrefix, osgbEastings, osgbNorthings, gridinput;
    Button calculate;
    NumberFormat myFormat = NumberFormat.getInstance();
   
    public OSNI2OSGBForm() {
	osni = new OSNI();
	osgb = new OSGB();

	myFormat.setMaximumFractionDigits(2);
	myFormat.setMinimumFractionDigits(2);

	GridBagLayout gbl = new GridBagLayout();
	GridBagConstraints gbc = new GridBagConstraints();

	setLayout(gbl);

	Label label = new Label("OSNI to OSGB");

	label.setFont(new Font("SansSerif", Font.BOLD + Font.ITALIC, 18));
	label.setForeground(Color.blue);

	gbc.anchor = GridBagConstraints.NORTH;
	gbc.gridwidth = GridBagConstraints.REMAINDER;
	add(label, gbc);

	sep = new Separator();
	gbc.fill      = GridBagConstraints.HORIZONTAL;
        gbc.insets    = new Insets(5,0,5,0);
        add(sep, gbc);

	label = new Label("Eastings (metres):");
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0,0,0,0);
        add(label, gbc);

	eastings = new TextField(10);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(eastings, gbc);

	label = new Label("Northings (metres):");
	gbc.gridwidth = 1;
        gbc.insets = new Insets(0,0,0,0);
        add(label, gbc);

	northings = new TextField(10);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(northings, gbc);

	label = new Label("Grid prefix:");
	gbc.gridwidth = 1;
        gbc.insets = new Insets(0,0,0,0);
        add(label, gbc);

	prefix = new List(5);
	populatePrefixes(prefix);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(prefix, gbc);

	sep = new Separator();
	gbc.fill      = GridBagConstraints.HORIZONTAL;
        gbc.insets    = new Insets(5,0,5,0);
        add(sep, gbc);

	Panel buttonpanel = new Panel();
	buttonpanel.setLayout(new BorderLayout());
	calculate = new Button("Convert");
	buttonpanel.add(calculate, BorderLayout.CENTER);

	calculate.addActionListener(this);

	gbc.insets = new Insets(0,0,0,0);
	add(buttonpanel, gbc);

	sep = new Separator();
	gbc.fill      = GridBagConstraints.HORIZONTAL;
        gbc.insets    = new Insets(5,0,5,0);
        add(sep, gbc);

	gridinput = new Label();
	gridinput.setForeground(Color.red);
        gbc.insets = new Insets(0,0,5,0);
	add(gridinput, gbc);

	label = new Label("Prefix:");
	label.setForeground(Color.blue);
        gbc.insets = new Insets(0,0,0,0);
	gbc.gridwidth = 1;
        add(label, gbc);

	osgbPrefix = new Label();
	osgbPrefix.setForeground(Color.blue);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(osgbPrefix, gbc);

	label = new Label("Eastings:");
	label.setForeground(Color.blue);
        gbc.insets = new Insets(0,0,0,0);
	gbc.gridwidth = 1;
        add(label, gbc);

	osgbEastings = new Label();
	osgbEastings.setForeground(Color.blue);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(osgbEastings, gbc);

	label = new Label("Northings:");
	label.setForeground(Color.blue);
	gbc.gridwidth = 1;
        gbc.insets = new Insets(0,0,0,0);
        add(label, gbc);

	osgbNorthings = new Label();
	osgbNorthings.setForeground(Color.blue);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(osgbNorthings, gbc);

	sep = new Separator();
	gbc.fill      = GridBagConstraints.HORIZONTAL;
        gbc.insets    = new Insets(5,0,2,0);
        add(sep, gbc);

	Label obliquitylabel = new Label("Copyright \u00a9 2002 by Obliquity.com");

	obliquitylabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
	obliquitylabel.setForeground(Color.blue);

	gbc.anchor = GridBagConstraints.NORTH;
	gbc.gridwidth = GridBagConstraints.REMAINDER;
	add(obliquitylabel, gbc);
    }

    private void populatePrefixes(List list) {
	String [] prefixes = {
	    "B", "C", "D", "G", "H", "J"
	    //"A", "B", "C", "D", "E",
	    //"F", "G", "H", "J", "K",
	    //"L", "M", "N", "O", "P",
	    //"Q", "R", "S", "T", "U",
	    //"V", "W", "X", "Y", "Z"
	};

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
	}
	catch (NumberFormatException nfe) {
	    eastings.setText("0");
	    x = 0.0;
	}

	try {
	    d = new Double(northings.getText());
	    y = d.doubleValue();
	}
	catch (NumberFormatException nfe) {
	    northings.setText("0");
	    y = 0.0;
	}

	DPoint p0 = new DPoint(x, y);

	String pfxs = prefix.getSelectedItem();

	DPoint dp = osni.GridSquareToOffset(pfxs.charAt(0));
	dp.offsetBy(p0);

	DPoint p = osni.GridToLongitudeAndLatitude(dp);

	DPoint osgbPoint = osgb.LatitudeAndLongitudeToGrid(p);

	String str;

	if (osgbPoint.getX() >= 0.0 && osgbPoint.getY() >= 0.0) {
	    str = new String(osgb.GridToGridSquare(osgbPoint));
	    osgbPrefix.setForeground(Color.blue);
	    osgbPrefix.setText(str);
	} else {
	    osgbPrefix.setForeground(Color.red);
	    osgbPrefix.setText("NO GRID SQUARE");
	}

	double osgbX = osgbPoint.getX()%100000.0;
	if (osgbX < 0.0)
	    osgbX += 100000.0;

	str = myFormat.format(osgbX);

	osgbEastings.setText(str);

	double osgbY = osgbPoint.getY()%100000.0;
	if (osgbY < 0.0)
	    osgbY += 100000.0;

	str = myFormat.format(osgbY);

	osgbNorthings.setText(str);

	str = pfxs + " " + myFormat.format(p0.getX()) + " " + myFormat.format(p0.getY());
	gridinput.setText(str);
    }
}

