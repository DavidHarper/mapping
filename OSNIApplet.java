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

import java.applet.Applet;

public class OSNIApplet extends Applet {
	public void init() {
		ThreeDPanel p = new ThreeDPanel();
		p.add(new OSNIForm());
		add(p);
	}

	public String getAppletInfo() {
		return "OSNIApplet\n\n"
				+ "Demonstration applet for the TransverseMercatorProjection\n"
				+ "and OSNI classes\n\n"
				+ "Copyright \u00a9 2002 David Harper at Obliquity Consulting\n"
				+ "www.obliquity.com";
	}
}
