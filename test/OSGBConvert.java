package test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.*;

import com.obliquity.mapping.DPoint;
import com.obliquity.mapping.OSGB;

public class OSGBConvert extends JPanel {
	protected JTextArea txtInput = new JTextArea(25, 80);
	protected JTextArea txtOutput = new JTextArea(25, 80);

	protected OSGB osgb = new OSGB();

	protected NumberFormat format = NumberFormat.getInstance();

	public OSGBConvert() {
		super(null);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JScrollPane sp1 = new JScrollPane(txtInput);
		add(sp1);

		JButton btnConvert = new JButton("Convert");
		add(btnConvert);

		JScrollPane sp2 = new JScrollPane(txtOutput);
		add(sp2);

		btnConvert.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				convertCoordinates();
			}
		});

		format.setMaximumFractionDigits(2);
		format.setMinimumFractionDigits(2);
	}

	protected void convertCoordinates() {
		String[] lines = txtInput.getText().split("\n");

		for (int i = 0; i < lines.length; i++)
			txtOutput.append(lines[i] + "\t\t" + convert(lines[i]) + "\n");
	}

	protected String convert(String line) {
		String[] words = line.split("\\s");

		if (words.length < 3)
			return "*** Error : too few tokens on line ***";

		if (words[0].length() != 2)
			return "*** Error : invalid prefix ***";

		DPoint offset = osgb.GridSquareToOffset(words[0].charAt(0), words[0]
				.charAt(1));

		int eastings = Integer.parseInt(words[1]);

		int northings = Integer.parseInt(words[2]);

		if (words[1].length() == 3 && words[2].length() == 3) {
			eastings *= 100;
			northings *= 100;
		}

		DPoint gridxy = new DPoint((double) eastings, (double) northings);

		gridxy.offsetBy(offset);

		DPoint p = osgb.GridToLongitudeAndLatitude(gridxy);

		double phi, lambda;

		lambda = (180.0 / Math.PI) * p.getX();
		phi = (180.0 / Math.PI) * p.getY();

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

		String lngstr = ld + "\u00b0 " + lm + "\' " + format.format(ls) + "\""
				+ ((lambda < 0.0) ? " West" : " East");

		String latstr = pd + "\u00b0 " + pm + "\' " + format.format(ps) + "\""
				+ ((phi < 0.0) ? " South" : " North");

		return lngstr + "\t" + latstr;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createUI();
			}
		});
	}

	public static void createUI() {
		JFrame frame = new JFrame("OSGB to latitude/longitude");
		frame.getContentPane().add(new OSGBConvert());
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
