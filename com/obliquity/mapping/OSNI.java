/*
 * Map projections package
 *
 * Transverse Mercator Projection for the Ordnance Survey of
 * Northenr Ireland
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
 * This class encapsulates the Transverse Mercator Projection
 * for the Ordnance Survey of Northen Ireland.
 *
 * <p>
 * The constructor passes the major and minor axes of the ellipsoid
 * to the superclass constructor in <em>metres</em>. All lengths
 * used by this class are therefore also in <em>metres</em>.
 *
 * <p>
 * In addition to inheriting the conversion functions from
 * its superclass, this class includes public member functions
 * which convert between 100km grid square prefixes
 * and the offsets from the origin of the Irish Grid.
 *
 * @author David Harper at obliquity.com
 * @version 1.1 2001-10-03
 */

public class OSNI extends TransverseMercatorProjection {
    static final private double OSNI_E0 = 200000.0;
    static final private double OSNI_N0 = 250000.0;
    static final private double OSNI_F0 = 1.000035;
    static final private double OSNI_a = 6377340.189;
    static final private double OSNI_eSquared = 0.006670540;
    static final private double OSNI_b = OSNI_a * Math.sqrt(1.0 - OSNI_eSquared);
    static final private double OSNI_phi0 = Math.PI * 53.5/180.0;
    static final private double OSNI_lambda0 = Math.PI * (-8.0)/180.0;

    /**
     * The constructor for this class simply calls the constructor
     * of its superclass, passing the parameters which specify the
     * ellipsoid and grid system which are the basis of the
     * Irish Grid.
     */
    public OSNI() {
	super(OSNI_a, OSNI_b, OSNI_F0, OSNI_lambda0, OSNI_phi0,
	      OSNI_E0, OSNI_N0);
    }

    static final private String gridletters[] = {
	"VWXYZ", "QRSTU", "LMNOP", "FGHJK", "ABCDE"
    };

    /**
     * Determine the 100km grid square in which the specified point
     * lies.
     *
     * @param p The grid coordinates of the point, in <em>metres</em>,
     * relative to the origin of the Irish Grid.
     *
     * @return A character representing the two-letter prefix
     * for the 100km square in which this point lies.
     */

    public char GridToGridSquare(DPoint p) {
	double E = p.getX();
	double N = p.getY();

	int x = (int)(E/100000.0);
	int y = (int)(N/100000.0);

	int kx = x%5;
	int ky = y%5;

	return gridletters[ky].charAt(kx);
    }

    /**
     * Calculate the offset from the origin of the Irish
     * Grid of the 100km square identified by the specified letter
     * prefix.
     *
     * @param a The letter which identifies the 100km square..
     *
     * @return A <CODE>DPoint</CODE> which represents the offset, in
     * <em>metres</em> of the origin of the specified 100km square
     * with respect to the origin of the Irish Grid.
     */
  public DPoint GridSquareToOffset(char b) {
    char B;

    B = Character.toUpperCase(b);

    double E = 0.0;
    double N = 0.0;

    /* Adjust easting for 100km offsets */

    switch (B) {
    case 'A': case 'F': case 'L': case 'Q': case 'V':
      /* Do nothing */
      break;

    case 'B': case 'G': case 'M': case 'R': case 'W':
      E += 100000.0;
      break;

    case 'C':  case 'H': case 'N': case 'S': case 'X':
      E += 200000.0;
      break;

    case 'D': case 'J': case 'O': case 'T': case 'Y':
      E += 300000.0;
      break;

    case 'E': case 'K': case 'P': case 'U': case 'Z':
      E += 400000.0;
      break;
    }

    /* Adjust northing for 100km offsets */
    switch (B) {
    case 'A': case 'B': case 'C': case 'D': case 'E':
      N += 400000.0;
      break;

    case 'F': case 'G': case 'H': case 'J': case 'K':
      N += 300000.0;
      break;

    case 'L': case 'M': case 'N': case 'O': case 'P':
      N += 200000.0;
      break;

    case 'Q': case 'R': case 'S': case 'T': case 'U':
      N += 100000.0;
      break;

    case 'V': case 'W': case 'X': case 'Y': case 'Z':
      /* Do nothing */
      break;
    }

    return new DPoint(E, N);
  }
}
