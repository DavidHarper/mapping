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

import java.io.*;
import java.lang.Math;
import java.text.*;

import com.obliquity.mapping.*;

public class TestEllipsoid {
    static NumberFormat angleFormat = NumberFormat.getInstance();
    static NumberFormat distanceFormat = NumberFormat.getInstance();

    static public double DtoR(double d) {
	return d * Math.PI/180.0;
    }

    static public double RtoD(double r) {
	return r * 180.0/Math.PI;
    }

    static void printAngle(double theta, PrintStream ps, String posPrefix, String negPrefix) {
	int pd,pm;

	double phi = RtoD(Math.abs(theta));

	pd = (int)phi;
	phi = 60.0 * (phi - pd);
	pm = (int)phi;
	phi = 60.0 * (phi - pm);

	if (theta < 0.0) {
	    ps.print((negPrefix == null)?"-": negPrefix);
	} else {
	    ps.print((posPrefix == null)?"+": posPrefix);
	}

	ps.print(pd + "\u00b0 " + pm + "\' " + angleFormat.format(phi) + "\"");
    }

    static public void main(String args[]) {
	Ellipsoid earth = new Ellipsoid(6378.137, 1.0/298.257222101);

	double lata, longa, latb, longb, ds;
	Double d;

	if (args.length < 4) {
	    System.err.println("Usage: java TestEllipsoid longa lata longb latb [ds]");
	    System.exit(1);
	}

	d = new Double(args[0]);
	longa = d.doubleValue();

	d = new Double(args[1]);
	lata = d.doubleValue();

	d = new Double(args[2]);
	longb = d.doubleValue();

	d = new Double(args[3]);
	latb = d.doubleValue();

	if (args.length == 4)
	    ds = 100.0;
	else {
	    d = new Double(args[4]);
	    ds = d.doubleValue();
	}

	angleFormat.setMaximumFractionDigits(2);
	angleFormat.setMinimumFractionDigits(2);

	distanceFormat.setMaximumFractionDigits(2);
	distanceFormat.setMinimumFractionDigits(2);

	try {
	    EllipsoidalArc arc = new EllipsoidalArc(earth);

	    arc.setPositionA(DtoR(longa), DtoR(lata));
	    arc.setPositionB(DtoR(longb), DtoR(latb));

	    double s = arc.calculateDistance();

	    System.out.print("Start position: ");
	    printAngle(arc.getLongitudeA(), System.out, "East ", "West ");
	    System.out.print(", ");
	    printAngle(arc.getLatitudeA(), System.out, "North ", "South ");
	    System.out.println();

	    System.out.print("End position: ");
	    printAngle(arc.getLongitudeB(), System.out, "East ", "West ");
	    System.out.print(", ");
	    printAngle(arc.getLatitudeB(), System.out, "North ", "South ");
	    System.out.println();

	    System.out.println("Distance is " + distanceFormat.format(s) + " kilometres");

	    System.out.print("Forward azimuth is ");
	    printAngle(arc.getAzimuthA(), System.out, null, null);
	    System.out.println();

	    System.out.print("Reverse azimuth is ");
	    printAngle(arc.getAzimuthB(), System.out, null, null);
	    System.out.println();

	    for (double ss = 0.0; ss < s; ss += ds) {
		arc.setDistance(ss);
		arc.calculatePosition();
		System.out.print("S = " + distanceFormat.format(ss) + ": ");
		printAngle(arc.getLongitudeB(), System.out, "East ", "West ");
		System.out.print(", ");
		printAngle(arc.getLatitudeB(), System.out, "North ", "South ");
		System.out.print(" (azimuth = ");
		printAngle(arc.getAzimuthB(), System.out, null, null);
		System.out.println(")");
	    }
	}
	catch (IllegalArgumentException iae) {
	    System.err.println("IllegalArgument Exception: " + iae);
	    iae.printStackTrace();
	    System.exit(1);
	}
	catch (ArithmeticException ae) {
	    System.err.println("Arithmetic Exception: " + ae);
	    ae.printStackTrace();
	    System.exit(1);
	}
	catch (IllegalStateException ise) {
	    System.err.println("IllegalState Exception: " + ise);
	    ise.printStackTrace();
	    System.exit(1);
	}
    }
}
