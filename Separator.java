import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.SystemColor;

/*
 * Created on 27-Aug-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author adh
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */

public class Separator extends Component {
	int thickness = 2;

	public void paint(Graphics g) {
		Dimension size = getSize();

		g.setColor(SystemColor.controlShadow);
		int y = (size.height / 2) - (thickness / 2);
		while (y < (size.height / 2)) {
			g.drawLine(0, y, size.width, y);
			++y;
		}
		g.setColor(SystemColor.controlLtHighlight);
		y = size.height / 2;
		while (y < ((size.height / 2) + (thickness / 2))) {
			g.drawLine(0, y, size.width, y);
			++y;
		}
	}

	public Dimension getPreferredSize() {
		Dimension prefsz = getSize();

		prefsz.height = thickness;
		return prefsz;
	}

}
