/*
 * Map projections package
 *
 * 2D point
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
 * This class encapsulates a two-dimensional point. The point has an
 * X coordinate and a Y coordinate. Member functions are provided
 * to perform simple operations on a point or points.
 *
 * <p>
 * All numerical values are stored and manipulated as <CODE>double</CODE>
 * numbers.
 *
 * @author David Harper at obliquity.com
 * @version 1.1 2001-10-03
 */

public class DPoint extends java.lang.Object{
  private double myX, myY;

    /**
     * Create a point with the spcfieid X and Y coordinates.
     */
  public DPoint(double x, double y) {
    myX = x;
    myY = y;
  }

    /**
     * Create a default point, which is the origin (0.0, 0.0).
     */
  public DPoint() {
    myX = 0.0;
    myY = 0.0;
  }

    /**
     * Get the X coordinate of the point.
     */
  public double getX() { return myX; }

    /**
     * get the Y coordinate of the point.
     */
  public double getY() { return myY; }

    /**
     * Format the point as a string.
     */
  public String toString() {
    return "DPoint[" + myX + ", " + myY + "]";
  }

    /**
     * Calculate the position of this point relative to an another
     * point.
     *
     * @param p The other point.
     *
     * @return A new <CODE>DPoint</CODE> which represents the vector
     * from the other point to this point.
     */
  public DPoint offsetFrom(DPoint p) {
    return new DPoint(myX - p.getX(),
		      myY - p.getY());
  }

    /**
     * Calculate the scalar distance from this point to another point.
     *
     * @param p The other point.
     *
     * @return The scalar distance between this point and the other.
     */
  public double distanceFrom(DPoint p) {
    double dx = myX - p.getX(),
      dy = myY - p.getY();

    return Math.sqrt(dx * dx + dy * dy);
  }

    /**
     * Scale this point by an amount in both the X and Y coordinates.
     *
     * @param r The scale factor. Both coordinates of this point are
     * multiplied by this factor. <em>The coordinates are changed by
     * this function.</em>
     */
  public void scaleBy(double r) {
    myX *= r;
    myY *= r;
  }

    /**
     * Translate this point by a specified vector.
     *
     * @param v The translation vector. <em>The coordinates are changed by
     * this function.</em>
     */
  public void offsetBy(DPoint v) {
    myX += v.getX();
    myY += v.getY();
  }
}
