/*
 * Map projections package
 *
 * Transverse Mercator Projection for the Ordnance Survey of Great
 * Britain
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
 * for the Ordnance Survey of Great Britain. The parameters
 * of the projection are those specified in Ordnance Survey
 * Geodetic Information Paper No 1, <em>The Ellipsoid and the 
 * Transverse Mercator Projection</em>.
 *
 * <p>
 * The constructor passes the major and minor axes of the ellipsoid
 * to the superclass constructor in <em>metres</em>. All lengths
 * used by this class are therefore also in <em>metres</em>.
 *
 * <p>
 * In addition to inheriting the conversion functions from
 * its superclass, this class includes public member functions
 * which convert between Ordnance Survey 100km grid square prefixes
 * and the offsets from the origin of the British National grid.
 *
 * @author David Harper at obliquity.com
 * @version 1.1 2001-10-03
 */

public class OSGB extends TransverseMercatorProjection {
  static final private double OSGB_E0 = 400000.0;
  static final private double OSGB_N0 = -100000.0;
  static final private double OSGB_F0 = 0.9996012717;
  static final private double OSGB_a = 6377563.396;
  static final private double OSGB_b = 6356256.910;
  static final private double OSGB_phi0 = Math.PI * 49.0/180.0;
  static final private double OSGB_lambda0 = Math.PI * (-2.0)/180.0;

    /**
     * The constructor for this class simply calls the constructor
     * of its superclass, passing the parameters which specify the
     * ellipsoid and grid system which are the basis of the
     * Ordnance Survey of Great Britain.
     */
  public OSGB() {
    super(OSGB_a, OSGB_b, OSGB_F0, OSGB_lambda0, OSGB_phi0,
	  OSGB_E0, OSGB_N0);
  }

  static final private String gridletters[] = {
    "VWXYZ", "QRSTU", "LMNOP", "FGHJK", "ABCDE"
  };

    /**
     * Determine the 100km grid square in which the specified point
     * lies.
     *
     * @param p The grid coordinates of the point, in <em>metres</em>,
     * relative to the origin of the British National Grid.
     *
     * @return A two-character array representing the two-letter prefix
     * for the 100km square in which this point lies.
     */
  public char [] GridToGridSquare(DPoint p) {
    double E = p.getX();
    double N = p.getY();

    int x = (int)(E/100000.0);
    int y = (int)(N/100000.0);

    int jx = (2 + (x/5))%5;
    int jy = (1 + (y/5))%5;

    char [] prefix = new char[2];

    prefix[0] = gridletters[jy].charAt(jx);

    int kx = x%5;
    int ky = y%5;

    prefix[1] = gridletters[ky].charAt(kx);

    return prefix;
  }

    /**
     * Calculate the offset from the origin of the British National
     * Grid of the 100km square identified by the specified two-letter
     * prefix.
     *
     * @param a The first letter of the two-letter prefix.
     * @param b The second letter of the two-letter prefix.
     *
     * @return A <CODE>DPoint</CODE> which represents the offset, in
     * <em>metres</em> of the origin of the specified 100km square
     * with respect to the origin of the British National Grid.
     */
  public DPoint GridSquareToOffset(char a, char b) {
    char A, B;

    A = Character.toUpperCase(a);
    B = Character.toUpperCase(b);

    double E = 0.0;
    double N = 0.0;

    switch (A) {
    case 'N':
      N += 500000.0;
      break;

    case 'H':
      N += 1000000.0;
      break;

    case 'T':
      E += 500000.0;
      break;
    }

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
