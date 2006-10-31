/*
 * Map projections package
 *
 * Vincenty algorithms for distance on an ellipsoid
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

public class EllipsoidalArc {
	private Ellipsoid ellipsoid;
	private double longitudeA, latitudeA, longitudeB, latitudeB;
	private double azimuthA, azimuthB, geodesicDistance;
	private boolean havePositionA, havePositionB, haveAzimuthA, haveAzimuthB,
			haveDistance;

	private static double TwoPi = 2.0 * Math.PI;
	private final static double EPSILON = 1e-8;

	public EllipsoidalArc(Ellipsoid e) throws IllegalArgumentException {
		if (e == null)
			throw new IllegalArgumentException("Ellipsoid is null");

		ellipsoid = e;
		havePositionA = havePositionB = haveAzimuthA = haveAzimuthB = haveDistance = false;
	}

	public void setPositionA(double longitude, double latitude) {
		longitudeA = longitude;
		latitudeA = latitude;
		havePositionA = true;
	}

	public void setPositionB(double longitude, double latitude) {
		longitudeB = longitude;
		latitudeB = latitude;
		havePositionB = true;
	}

	public void setAzimuthA(double azimuth) {
		azimuthA = azimuth;
		haveAzimuthA = true;
	}

	public void setAzimuthB(double azimuth) {
		azimuthB = azimuth;
		haveAzimuthB = true;
	}

	public void setDistance(double distance) {
		geodesicDistance = distance;
		haveDistance = true;
	}

	public double getLatitudeA() throws IllegalStateException {
		if (havePositionA)
			return latitudeA;
		else
			throw new IllegalStateException("latitudeA is not defined");
	}

	public double getLongitudeA() throws IllegalStateException {
		if (havePositionA)
			return longitudeA;
		else
			throw new IllegalStateException("longitudeA is not defined");
	}

	public double getLatitudeB() throws IllegalStateException {
		if (havePositionB)
			return latitudeB;
		else
			throw new IllegalStateException("latitudeB is not defined");
	}

	public double getLongitudeB() throws IllegalStateException {
		if (havePositionB)
			return longitudeB;
		else
			throw new IllegalStateException("longitudeB is not defined");
	}

	public double getAzimuthA() throws IllegalStateException {
		if (haveAzimuthA)
			return azimuthA;
		else
			throw new IllegalStateException("azimuthA is not defined");
	}

	public double getAzimuthB() throws IllegalStateException {
		if (haveAzimuthB)
			return azimuthB;
		else
			throw new IllegalStateException("azimuthB is not defined");
	}

	public double getDistance() throws IllegalStateException {
		if (haveDistance)
			return geodesicDistance;
		else
			throw new IllegalStateException("distance is not defined");
	}

	public double calculateDistance() throws IllegalStateException,
			ArithmeticException {
		double U1, U2, w, sigma, ssig, csig, salp, c2alp, c2sigm;
		double u, A, B, C, dsig, b;
		double lambda, dlam = 0.0, lastdlam = 0.0;
		double p, q;
		int nIters = 0, nMaxIters = 10;

		double a = ellipsoid.getSemiMajorAxis();
		double f = ellipsoid.getFlattening();

		if (!havePositionA)
			throw new IllegalStateException("positionA is not defined");

		if (!havePositionB)
			throw new IllegalStateException("positionB is not defined");

		b = a * (1.0 - f);

		U1 = Math.atan((1.0 - f) * Math.tan(latitudeA));
		U2 = Math.atan((1.0 - f) * Math.tan(latitudeB));

		w = longitudeB - longitudeA;
		if (w < -Math.PI)
			w += TwoPi;
		if (w > Math.PI)
			w -= TwoPi;

		lambda = w;

		do {
			lastdlam = dlam;

			p = Math.cos(U2) * Math.sin(lambda);
			q = Math.cos(U1) * Math.sin(U2) - Math.sin(U1) * Math.cos(U2)
					* Math.cos(lambda);

			ssig = Math.sqrt(p * p + q * q);

			csig = Math.sin(U1) * Math.sin(U2) + Math.cos(U1) * Math.cos(U2)
					* Math.cos(lambda);

			sigma = Math.atan2(ssig, csig);

			salp = Math.cos(U1) * Math.cos(U2) * Math.sin(lambda) / ssig;

			c2alp = 1.0 - salp * salp;

			c2sigm = csig - 2.0 * Math.sin(U1) * Math.sin(U2) / c2alp;

			C = (f / 16.0) * c2alp * (4.0 + f * (4.0 - 3.0 * c2alp));

			dlam = (1.0 - C)
					* f
					* salp
					* (sigma + C
							* ssig
							* (c2sigm + C * csig
									* (2.0 * c2sigm * c2sigm - 1.0)));

			lambda = w + dlam;

			nIters += 1;
		} while ((Math.abs(dlam - lastdlam) > EPSILON) && (nIters <= nMaxIters));

		if (nIters > nMaxIters)
			throw new ArithmeticException("Exceeded iteration limit");

		u = (1.0 - salp * salp) * (a * a - b * b) / (b * b);

		A = 1.0 + (u / 16384.0)
				* (4096.0 + u * (-768.0 + u * (320.0 - 175.0 * u)));

		B = (u / 1024.0) * (256.0 + u * (-128.0 + u * (74.0 - 47.0 * u)));

		dsig = B
				* ssig
				* (c2sigm + (B / 4.0)
						* (csig * (2.0 * c2sigm * c2sigm - 1.0) - (B / 6.0)
								* c2sigm * (4.0 * ssig * ssig - 3.0)
								* (4.0 * c2sigm * c2sigm - 3.0)));

		p = Math.cos(U2) * Math.sin(lambda);
		q = Math.cos(U1) * Math.sin(U2) - Math.sin(U1) * Math.cos(U2)
				* Math.cos(lambda);

		azimuthA = Math.atan2(p, q);
		haveAzimuthA = true;

		p = -Math.cos(U1) * Math.sin(lambda);
		q = Math.cos(U2) * Math.sin(U1) - Math.sin(U2) * Math.cos(U1)
				* Math.cos(lambda);

		azimuthB = Math.atan2(p, q);
		haveAzimuthB = true;

		geodesicDistance = b * A * (sigma - dsig);
		haveDistance = true;

		return geodesicDistance;
	}

	public void calculatePosition() {
		double sigma1, tsig1, tu1, sa, ca, sa1, ca1, A, B, twosm;
		double cu1, su1, uu;
		double sigma;
		int nIters = 0, nMaxIters = 10;
		double c2sm, ssig, csig, dsig = 0.0, lastdsig;
		double lambda, C;
		double x, y;

		double a = ellipsoid.getSemiMajorAxis();
		double f = ellipsoid.getFlattening();

		double b = a * (1.0 - f);

		if (!havePositionA)
			throw new IllegalStateException("positionA is not defined");

		if (!haveAzimuthA)
			throw new IllegalStateException("azimuthA is not defined");

		if (!haveDistance)
			throw new IllegalStateException("distance is not defined");

		tu1 = (1.0 - f) * Math.tan(latitudeA);
		cu1 = Math.cos(Math.atan(tu1));
		su1 = Math.sin(Math.atan(tu1));

		ca1 = Math.cos(azimuthA);
		sa1 = Math.sin(azimuthB);

		tsig1 = tu1 / ca1;
		sa = cu1 * sa1;
		ca = 1.0 - sa * sa;

		uu = (1.0 - sa * sa) * (a * a - b * b) / (b * b);

		A = 1.0 + (uu / 16384.0)
				* (4096.0 + uu * (-768.0 + uu * (320.0 - 175.0 * uu)));

		B = (uu / 1024.0) * (256.0 + uu * (-128.0 + uu * (74.0 - 47.0 * uu)));

		sigma1 = Math.atan(tsig1);
		if (tsig1 < 0.0)
			sigma1 += Math.PI;

		sigma = geodesicDistance / (b * A);

		lastdsig = 0.0;

		twosm = 2.0 * sigma1 + sigma;

		do {
			lastdsig = dsig;

			c2sm = Math.cos(twosm);
			ssig = Math.sin(sigma);
			csig = Math.cos(sigma);

			dsig = B
					* ssig
					* (c2sm + (B / 4.0)
							* (csig * (-1.0 + 2.0 * c2sm * c2sm) - (B / 6.0)
									* c2sm * (-3.0 + 4.0 * ssig * ssig)
									* (-3.0 + 4.0 * c2sm * c2sm)));

			sigma = geodesicDistance / (b * A) + dsig;

			twosm = 2 * sigma1 + sigma;

			nIters += 1;
		} while ((Math.abs(dsig - lastdsig) > EPSILON) && (nIters <= nMaxIters));

		if (nIters > nMaxIters)
			throw new ArithmeticException("Exceeded iteration limit");

		c2sm = Math.cos(twosm);
		ssig = Math.sin(sigma);
		csig = Math.cos(sigma);

		y = su1 * csig + cu1 * ssig * ca1;

		x = su1 * ssig - cu1 * csig * ca1;
		x *= x;
		x += sa * sa;
		x = (1.0 - f) * Math.sqrt(x);

		latitudeB = Math.atan2(y, x);

		y = ssig * sa1;
		x = cu1 * csig - su1 * ssig * ca1;

		lambda = Math.atan2(y, x);

		C = (f / 16.0) * ca * (4.0 + f * (4.0 - 3.0 * ca));

		x = (1.0 - C)
				* f
				* sa
				* (sigma + C * ssig
						* (c2sm + (C * csig * (-1.0 + 2.0 * c2sm * c2sm))));

		lambda -= x;

		longitudeB = longitudeA + lambda;

		havePositionB = true;

		y = sa;

		x = -su1 * ssig + cu1 * csig * ca1;

		azimuthB = Math.atan2(-y, -x);
		haveAzimuthB = true;
	}
}
