/*
 * Map projections package
 *
 * Transverse Mercator Projection
 *
 * Copyright (C) 2001 David Harper at obliquity.com
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

package com.obliquity.mapping;

/**
 * <code>TransverseMercatorProjection</code> encapsulates a generic Transverse
 * Mercator Projection. It includes public member functions which convert
 * between grid coordinates and latitude/longitude.
 * 
 * <p>
 * Latitude and longitude are <em>always</em> measured in radians. Latitude is
 * positive if north, negative if south. Longitude is positive if east, negative
 * if west.
 * 
 * <p>
 * Grid coordinates and the error tolerance are expressed in the same units as
 * those used to specify the major and minor axes of the ellipsoid in the
 * constructor. In practice, any unit of length may be used.
 * 
 * @author David Harper at obliquity.com
 * @version 1.1 2001-10-03
 */

public class TransverseMercatorProjection extends Object {
	private double E0, N0, F0, phi0, lambda0;
	private double a, b, eSquared, n;
	private double aF0, bF0;
	private double[] tp;
	private double[] pnu;
	private double epsilon;

	/**
	 * Constructs a Transverse Mercator Projection from the specified
	 * parameters.
	 * 
	 * @param majorAxis
	 *            The major axis of the ellipsoid
	 * 
	 * @param minorAxis
	 *            The minor axis of the ellipsoid
	 * 
	 * @param scaleFactor
	 *            The scale factor on the central meridian
	 * 
	 * @param centralLongitude
	 *            The longitude of the central meridian
	 * 
	 * @param centralLatitude
	 *            The latitude of the zero point of the grid
	 * 
	 * @param eastingOffset
	 *            The easting offset of the false origin
	 * 
	 * @param northingOffset
	 *            The northing offset of the false origin
	 * 
	 */
	public TransverseMercatorProjection(double majorAxis, double minorAxis,
			double scaleFactor, double centralLongitude,
			double centralLatitude, double eastingOffset, double northingOffset) {
		a = majorAxis;
		b = minorAxis;
		F0 = scaleFactor;
		phi0 = centralLatitude;
		lambda0 = centralLongitude;
		E0 = eastingOffset;
		N0 = northingOffset;

		n = (a - b) / (a + b);
		eSquared = (a - b) * (a + b) / (a * a);

		aF0 = a * F0;
		bF0 = b * F0;

		tp = new double[7];
		pnu = new double[8];

		epsilon = 0.001;
	}

	private double calculateM(double phi) {
		double M = (1.0 + n + 1.25 * n * n * (1.0 + n)) * (phi - phi0) - 3.0
				* n * (1.0 + n + 1.125 * n * n) * Math.sin(phi - phi0)
				* Math.cos(phi + phi0) + 15.0 * n * n * (1.0 + n) / 8.0
				* Math.sin(2.0 * (phi - phi0)) * Math.cos(2.0 * (phi + phi0))
				- 35.0 * n * n * n / 24.0 * Math.sin(3.0 * (phi - phi0))
				* Math.cos(3.0 * (phi + phi0));

		return M * bF0;
	}

	/**
	 * Set the error tolerance for the conversion routines.
	 * 
	 * @param d
	 *            The error tolerance.
	 */
	public void setErrorTolerance(double d) {
		epsilon = Math.abs(d);
	}

	/**
	 * Retrieve the error tolerance for the conversion routines.
	 * 
	 * @return The error tolerance.
	 */
	public double getErrorTolerance() {
		return epsilon;
	}

	/**
	 * Convert grid coordinates to latitude and longitude.
	 * 
	 * @param easting
	 *            The easting of the point to be converted.
	 * @param northing
	 *            The northing of the point to be converted.
	 * 
	 * @return A <CODE>DPoint</CODE> representing the longitude and latitude
	 *         of the point. The longitude can be obtained using the <CODE>getX()</CODE>
	 *         method of the <CODE>DPoint</CODE>, whilst the latitude is
	 *         obtained using the <CODE>getY()</CODE> method.
	 * 
	 * @see DPoint
	 */
	public DPoint GridToLongitudeAndLatitude(double easting, double northing) {
		DPoint p = new DPoint(easting, northing);
		return GridToLongitudeAndLatitude(p);
	}

	/**
	 * Convert grid coordinates to latitude and longitude.
	 * 
	 * @param gridxy
	 *            A <CODE>DPoint</CODE> containing the grid coordinates of the
	 *            point to be converted.
	 * 
	 * @return A <CODE>DPoint</CODE> representing the longitude and latitude
	 *         of the point. The longitude can be obtained using the <CODE>getX()</CODE>
	 *         method of the <CODE>DPoint</CODE>, whilst the latitude is
	 *         obtained using the <CODE>getY()</CODE> method.
	 * 
	 * @see DPoint
	 */
	public DPoint GridToLongitudeAndLatitude(DPoint gridxy) {
		int i;
		double phi, rho, nu, etasq, sp, M;
		double E, N;
		double x, y, VII, VIII, IX, X, XI, XII, XIIA;

		E = gridxy.getX();
		N = gridxy.getY();

		/*
		 * Calculate an initial estimate of the latitude.
		 */

		phi = (N - N0) / aF0 + phi0;

		/*
		 * Now enter the iterative phase.
		 */

		do {
			M = calculateM(phi);

			x = N - N0 - M;

			phi += x / aF0;
		} while (Math.abs(x) > epsilon);

		/*
		 * Calculate auxiliary quantities.
		 */

		y = Math.sin(phi);
		y *= y * eSquared;

		x = 1.0 - y;

		nu = aF0 / Math.sqrt(x);
		rho = nu * (1.0 - eSquared) / x;

		etasq = nu / rho - 1.0;

		y = E - E0;

		x = Math.tan(phi);

		tp[0] = 1.0;
		for (i = 1; i < 7; i++)
			tp[i] = tp[i - 1] * x;

		pnu[1] = nu;
		x = nu * nu;
		for (i = 3; i < 8; i += 2)
			pnu[i] = pnu[i - 2] * x;

		sp = 1.0 / Math.cos(phi);

		VII = tp[1] / (2.0 * rho * nu);

		VIII = tp[1] * (5.0 + 3.0 * tp[2] + etasq * (1.0 - 9.0 * tp[2]))
				/ (24.0 * rho * pnu[3]);

		IX = tp[1] * (61.0 + 90.0 * tp[2] + 45.0 * tp[4])
				/ (720.0 * rho * pnu[5]);

		X = sp / nu;

		XI = sp * (nu / rho + 2.0 * tp[2]) / (6.0 * pnu[3]);

		XII = sp * (5.0 + 28.0 * tp[2] + 24 * tp[4]) / (120.0 * pnu[5]);

		XIIA = sp * (61.0 + 662.0 * tp[2] + 1320.0 * tp[4] + 720.0 * tp[6])
				/ (5040.0 * pnu[7]);

		x = y * y;

		return new DPoint(lambda0 + y * (X + x * (-XI + x * (XII - x * XIIA))),
				phi + x * (-VII + x * (VIII - x * IX)));
	}

	/**
	 * Convert latitude and longitude to grid coordinates.
	 * 
	 * @param longitude
	 *            The longitude of the point to be converted.
	 * @param latitude
	 *            The latitude of the point to be converted.
	 * 
	 * @return A <CODE>DPoint</CODE> representing the grid coordinates of the
	 *         point.
	 * 
	 * @see DPoint
	 */
	public DPoint LatitudeAndLongitudeToGrid(double longitude, double latitude) {
		DPoint latlong = new DPoint(longitude, latitude);
		return LatitudeAndLongitudeToGrid(latlong);
	}

	/**
	 * Convert latitude and longitude to grid coordinates.
	 * 
	 * @param latlong
	 *            A <CODE>DPoint</CODE> containing the longitude as the X
	 *            coordinate and the latitude as the Y coordinate.
	 * 
	 * @return A <CODE>DPoint</CODE> representing the grid coordinates of the
	 *         point.
	 * 
	 * @see DPoint
	 */
	public DPoint LatitudeAndLongitudeToGrid(DPoint latlong) {
		double phi, lambda, rho, nu, etasq, M;
		double x, y;
		double I, II, III, IIIA, IV, V, VI;
		double sp, cp, tpsq;
		double cp3, cp5;
		double P, P3, P5;

		lambda = latlong.getX();
		phi = latlong.getY();

		sp = Math.sin(phi);
		cp = Math.cos(phi);

		cp3 = cp * cp * cp;
		cp5 = cp3 * cp * cp;

		tpsq = Math.tan(phi);
		tpsq *= tpsq;

		P = lambda - lambda0;
		P3 = P * P * P;
		P5 = P3 * P * P;

		y = sp * sp * eSquared;

		x = 1.0 - y;

		nu = aF0 / Math.sqrt(x);
		rho = nu * (1.0 - eSquared) / x;

		etasq = nu / rho - 1.0;

		M = calculateM(phi);

		I = M + N0;

		II = 0.5 * nu * sp * cp;

		III = (nu / 24.0) * sp * cp3 * (5.0 - tpsq + 9.0 * etasq);

		IIIA = (nu / 720.0) * sp * cp5 * (61.0 - 58.0 * tpsq + tpsq * tpsq);

		IV = nu * cp;

		V = (nu / 6.0) * cp3 * (nu / rho - tpsq);

		VI = (nu / 120.0)
				* cp5
				* (5.0 - 18.0 * tpsq + tpsq * tpsq + 14.0 * etasq - 58.0
						* etasq * tpsq);

		return new DPoint(E0 + P * IV + P3 * V + P5 * VI, I + P
				* (P * II + P3 * III + P5 * IIIA));
	}
}
