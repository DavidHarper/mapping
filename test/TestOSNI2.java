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

public class TestOSNI2 {
	static public void main(String args[]) {
		double x, y;
		Random rand = new Random();
		String prefixletter = "ABCDEFGHJKLMNOPQRSTUVWXYZ";
		double sumx, sumx2;
		int nsum;

		OSNI osni = new OSNI();

		osni.setErrorTolerance(0.0001);

		nsum = 0;
		sumx = sumx2 = 0.0;

		for (int i = 0; i < 10000; i++) {
			x = 100000.0 * rand.nextDouble();
			y = 100000.0 * rand.nextDouble();

			DPoint p0 = new DPoint(x, y);

			int ib;
			ib = rand.nextInt() % 25;
			if (ib < 0)
				ib += 25;

			char cb = prefixletter.charAt(ib);

			DPoint dp = osni.GridSquareToOffset(cb);

			p0.offsetBy(dp);

			char pfx = osni.GridToGridSquare(p0);

			DPoint p1 = osni.GridToLongitudeAndLatitude(p0);

			double phi, lambda;

			lambda = (180.0 / Math.PI) * p1.getX();
			phi = (180.0 / Math.PI) * p1.getY();

			System.out.println("Grid coordinates (" + cb + " " + p0.getX()
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

			System.out.println("Grid letter is " + pfx);

			DPoint p2 = osni.LatitudeAndLongitudeToGrid(p1);

			System.out.println("Reverse transform:");
			System.out.println("  Easting  = " + p2.getX());
			System.out.println("  Northing = " + p2.getY());
			double d = p2.distanceFrom(p0);
			System.out.println("  DISTANCE = " + d);

			if (cb != pfx)
				System.out.println("*** MISMATCH OF PREFIX ***");

			if (Math.abs(d) > 0.01)
				System.out.println("*** POSITION ERROR *** " + pfx + " " + d);
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
