import javax.swing.*;

public class Mapping extends JFrame {
	public Mapping(String title) {
		super(title);
		
		JTabbedPane tabbedpane = new JTabbedPane();
		
		tabbedpane.addTab("OSGB to lat/long", new OSGBForm());
		tabbedpane.addTab("Lat/long to OSGB", new OSGBReverseForm());
		tabbedpane.addTab("OSNI to OSGB", new OSNI2OSGBForm());
		tabbedpane.addTab("OSNI to lat/long", new OSNIForm());
		
		getContentPane().add(tabbedpane);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createUI();
			}
		});
	}
	
	private static void createUI() {
		Mapping mapping = new Mapping("Mapping converter");
		mapping.pack();
		mapping.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mapping.setVisible(true);
	}
}
