import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Panel;
/*
 * Created on 27-Aug-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author adh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class ThreeDPanel extends Panel {
    public void paint(Graphics g) {
    	Dimension sz = getSize();
    	g.setColor(Color.lightGray);
    	g.draw3DRect(0, 0, sz.width-1, sz.height-1, true);
        }

}
