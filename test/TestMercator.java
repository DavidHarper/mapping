/*
 * Map projections package
 *
 * Test program
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

package test;

import java.lang.Math;
import com.obliquity.mapping.*;

public class TestMercator {
	static public void main(String args[]) {
		double x, y;

		OSGB osgb = new OSGB();

		if (args.length < 2) {
			/*
			 * Caister Water Tower
			 */
			x = 651409.903;
			y = 313177.271;
		} else {
			Double d;
			d = new Double(args[0]);
			x = d.doubleValue();
			d = new Double(args[1]);
			y = d.doubleValue();
		}

		DPoint p = osgb.GridToLongitudeAndLatitude(x, y);

		double phi, lambda;

		lambda = (180.0 / Math.PI) * p.getX();
		phi = (180.0 / Math.PI) * p.getY();

		System.out.println("Grid coordinates (" + x + ", " + y
				+ ") map to longitude " + lambda + ", latitude " + phi);

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

		System.out.print((lambda < 0.0) ? "West " : "East ");
		System.out.println(ld + " " + lm + " " + ls);

		System.out.print((phi < 0.0) ? "South " : "North ");
		System.out.println(pd + " " + pm + " " + ps);

		p = osgb.LatitudeAndLongitudeToGrid(p);

		System.out.println("Reverse transform:");
		System.out.println("  Easting  = " + p.getX());
		System.out.println("  Northing = " + p.getY());
	}
}
