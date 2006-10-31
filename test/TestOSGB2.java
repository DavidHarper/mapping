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
import java.util.Random;

import com.obliquity.mapping.*;

public class TestOSGB2 {
	static public void main(String args[]) {
		double x, y;
		Random rand = new Random();
		String firstLetter = "STNH";
		String secondLetter = "ABCDEFGHJKLMNOPQRSTUVWXYZ";
		double sumx, sumx2;
		int nsum;

		OSGB osgb = new OSGB();

		osgb.setErrorTolerance(0.0001);

		nsum = 0;
		sumx = sumx2 = 0.0;

		for (int i = 0; i < 10000; i++) {
			x = 100000.0 * rand.nextDouble();
			y = 100000.0 * rand.nextDouble();

			DPoint p0 = new DPoint(x, y);

			int ia, ib;
			ia = rand.nextInt() % 4;
			if (ia < 0)
				ia += 4;
			ib = rand.nextInt() % 25;
			if (ib < 0)
				ib += 25;

			char ca = firstLetter.charAt(ia);
			char cb = secondLetter.charAt(ib);

			DPoint dp = osgb.GridSquareToOffset(ca, cb);

			p0.offsetBy(dp);

			char[] pfx = osgb.GridToGridSquare(p0);
			String pfxs = new String(pfx);

			DPoint p1 = osgb.GridToLongitudeAndLatitude(p0);

			double phi, lambda;

			lambda = (180.0 / Math.PI) * p1.getX();
			phi = (180.0 / Math.PI) * p1.getY();

			System.out.println("Grid coordinates (" + ca + cb + " " + p0.getX()
					+ ", " + p0.getY() + ") map to:");

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

			System.out.println("Grid letters are " + pfxs);

			DPoint p2 = osgb.LatitudeAndLongitudeToGrid(p1);

			System.out.println("Reverse transform:");
			System.out.println("  Easting  = " + p2.getX());
			System.out.println("  Northing = " + p2.getY());
			double d = p2.distanceFrom(p0);
			System.out.println("  DISTANCE = " + d);

			if (ca != pfx[0] || cb != pfx[1])
				System.out.println("*** MISMATCH OF PREFIX ***");

			if (Math.abs(d) > 0.01)
				System.out.println("*** POSITION ERROR *** " + pfxs + " " + d);
			else {
				nsum += 1;
				sumx += d;
				sumx2 += d * d;
			}
		}

		sumx /= (double) nsum;

		sumx2 /= (double) nsum;
		sumx2 -= sumx * sumx;
		sumx2 = Math.sqrt(sumx2);

		System.err.println("From " + nsum + " samples, mean = " + sumx
				+ " and sigma = " + sumx2);
	}
}
