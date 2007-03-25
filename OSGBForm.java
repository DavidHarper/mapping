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
import javax.swing.*;

import com.obliquity.mapping.*;

public class OSGBForm extends JPanel implements ActionListener {
	OSGB osgb;
	JSeparator sep;
	JTextField eastings, northings;
	JList prefix;
	JLabel longitude, latitude, gridinput;
	JButton calculate;
	NumberFormat myFormat = NumberFormat.getInstance();

	public OSGBForm() {
		super();
		
		osgb = new OSGB();

		myFormat.setMaximumFractionDigits(2);
		myFormat.setMinimumFractionDigits(2);

		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();

		setLayout(gbl);

		JLabel label = new JLabel("OSGB to Latitude/Longitude");

		label.setFont(new Font("SansSerif", Font.BOLD + Font.ITALIC, 18));
		label.setForeground(Color.blue);

		gbc.anchor = GridBagConstraints.NORTH;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(label, gbc);

		sep = new JSeparator();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 0, 5, 0);
		add(sep, gbc);

		label = new JLabel("Eastings (metres):");
		gbc.gridwidth = 1;
		gbc.insets = new Insets(0, 0, 0, 0);
		add(label, gbc);

		eastings = new JTextField(10);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(eastings, gbc);

		label = new JLabel("Northings (metres):");
		gbc.gridwidth = 1;
		gbc.insets = new Insets(0, 0, 0, 0);
		add(label, gbc);

		northings = new JTextField(10);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(northings, gbc);

		label = new JLabel("Grid prefix:");
		gbc.gridwidth = 1;
		gbc.insets = new Insets(0, 0, 0, 0);
		add(label, gbc);

		prefix = prefixList();
		prefix.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(new JScrollPane(prefix), gbc);

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

		gridinput = new JLabel("        ");
		gridinput.setForeground(Color.red);
		gbc.insets = new Insets(0, 0, 5, 0);
		add(gridinput, gbc);

		label = new JLabel("Latitude:");
		label.setForeground(Color.blue);
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.gridwidth = 1;
		add(label, gbc);

		longitude = new JLabel();
		longitude.setForeground(Color.blue);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(longitude, gbc);

		label = new JLabel("Longitude:");
		label.setForeground(Color.blue);
		gbc.gridwidth = 1;
		gbc.insets = new Insets(0, 0, 0, 0);
		add(label, gbc);

		latitude = new JLabel();
		latitude.setForeground(Color.blue);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(latitude, gbc);

		sep = new JSeparator();
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
		
		Dimension d = getPreferredSize();
		d.width += 40;
		d.height += 40;
		setPreferredSize(d);
	}

	private JList prefixList() {
		String[] prefixes = { "HP", "HT", "HU", "HY", "HZ", "NA", "NB", "NC",
				"ND", "NF", "NG", "NH", "NJ", "NK", "NL", "NM", "NN", "NO",
				"NR", "NS", "NT", "NU", "NV", "NW", "NX", "NY", "NZ", "SA",
				"SB", "SC", "SD", "SE", "SH", "SJ", "SK", "SM", "SN", "SO",
				"SP", "SR", "SS", "ST", "SU", "SV", "SW", "SX", "SY", "SZ",
				"TA", "TF", "TG", "TL", "TM", "TQ", "TR", "TV" };

		JList list = new JList(prefixes);
		
		list.setVisibleRowCount(5);
		
		list.setSelectedIndex(0);
		
		return list;
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

		String pfxs = (String)prefix.getSelectedValue();

		DPoint dp = osgb.GridSquareToOffset(pfxs.charAt(0), pfxs.charAt(1));
		dp.offsetBy(p0);

		DPoint p = osgb.GridToLongitudeAndLatitude(dp);

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
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame("OSGB to lat/long");
				frame.getContentPane().add(new OSGBForm());
				frame.pack();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}
}
