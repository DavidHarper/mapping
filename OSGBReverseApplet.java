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

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import com.obliquity.mapping.DPoint;
import com.obliquity.mapping.OSGB;

public class OSGBReverseApplet extends Applet {
    public void init() {
    	ThreeDPanel p = new ThreeDPanel();
    	p.add(new OSGBReverseForm());
    	add(p);
        }

        public String getAppletInfo() {
    	return "OSGBReverseApplet\n\n" +
    	    "Demonstration applet for the TransverseMercatorProjection\n" + 
    	    "and OSGB classes\n\n" +
    	    "Copyright \u00a9 2004 David Harper at Obliquity Consulting\n" +
    	    "www.obliquity.com";
        }
}

class OSGBReverseForm extends Panel implements ActionListener {
    OSGB osgb;
    Separator sep;
    TextField latd, latm, lats, lngd, lngm, lngs;
    Checkbox east;
    Label prefix, eastings, northings;
    Button calculate;
    NumberFormat myFormat = NumberFormat.getInstance();
   
    public OSGBReverseForm() {
	osgb = new OSGB();

	myFormat.setMaximumFractionDigits(0);
	myFormat.setMinimumFractionDigits(0);

	GridBagLayout gbl = new GridBagLayout();
	GridBagConstraints gbc = new GridBagConstraints();

	setLayout(gbl);

	Label label = new Label("Latitude/Longitude to OSGB");

	label.setFont(new Font("SansSerif", Font.BOLD + Font.ITALIC, 18));
	label.setForeground(Color.blue);

	gbc.anchor = GridBagConstraints.NORTH;
	gbc.gridwidth = GridBagConstraints.REMAINDER;
	add(label, gbc);

	sep = new Separator();
	gbc.fill      = GridBagConstraints.HORIZONTAL;
    gbc.insets    = new Insets(5,0,5,0);
    add(sep, gbc);

	label = new Label("Longitude:");
    gbc.gridwidth = 1;
    gbc.insets = new Insets(0,0,0,0);
    add(label, gbc);

	lngd = new TextField(2);
    add(lngd, gbc);
        
    add(new Label("d"), gbc);
        
    lngm = new TextField(2);
    add(lngm, gbc);
    
    add(new Label("m"), gbc);

	lngs = new TextField(5);
    add(lngs, gbc);
    
    add(new Label("s"), gbc);

    CheckboxGroup cbg = new CheckboxGroup();
    east = new Checkbox("East", cbg, true);
    Checkbox west = new Checkbox("West", cbg, false);

    add(east, gbc);
    
    gbc.gridwidth = GridBagConstraints.REMAINDER;
          
    add(west, gbc);
    
	label = new Label("Latitude:");
	gbc.gridwidth = 1;
        gbc.insets = new Insets(0,0,0,0);
        add(label, gbc);


    	latd = new TextField(2);
        add(latd, gbc);
            
        add(new Label("d"), gbc);
            
        latm = new TextField(2);
        add(latm, gbc);
        
        add(new Label("m"), gbc);

    	lats = new TextField(5);
        add(lats, gbc);

        gbc.gridwidth = GridBagConstraints.REMAINDER;
              
        add(new Label("s"), gbc);

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

    label = new Label("Prefix:");
	label.setForeground(Color.blue);
    gbc.insets = new Insets(0,0,0,0);
    gbc.gridwidth = 1;
    add(label, gbc);

    prefix = new Label();
    prefix.setForeground(Color.blue);
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    add(prefix, gbc);
    
	label = new Label("Eastings:");
	label.setForeground(Color.blue);
        gbc.insets = new Insets(0,0,0,0);
	gbc.gridwidth = 1;
        add(label, gbc);

	eastings = new Label();
	eastings.setForeground(Color.blue);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(eastings, gbc);

	label = new Label("Northings:");
	label.setForeground(Color.blue);
	gbc.gridwidth = 1;
        gbc.insets = new Insets(0,0,0,0);
        add(label, gbc);

	northings = new Label();
	northings.setForeground(Color.blue);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(northings, gbc);

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

    public void actionPerformed(ActionEvent ae) {
	double x = 0.0, y = 0.0;
	Double d;

	try {
	    x = Double.parseDouble(lngd.getText()) +
			Double.parseDouble(lngm.getText())/60.0 +
			Double.parseDouble(lngs.getText())/3600.0;
	    
	    if (!east.getState())
	    	x = -x;
	}
	catch (NumberFormatException nfe) {
	    x = 0.0;
	}

	try {
	    y = Double.parseDouble(latd.getText()) +
			Double.parseDouble(latm.getText())/60.0 +
			Double.parseDouble(lats.getText())/3600.0;
	}
	catch (NumberFormatException nfe) {
	    y = 0.0;
	}
	
	x *= Math.PI/180.0;
	y *= Math.PI/180.0;

	DPoint p0 = new DPoint(x, y);

	DPoint p = osgb.LatitudeAndLongitudeToGrid(p0);
	
	char[] pfx = osgb.GridToGridSquare(p);

	prefix.setText(new String(pfx));
	eastings.setText(myFormat.format(p.getX()%100000.0));
	northings.setText(myFormat.format(p.getY()%100000.0));
    }
}
