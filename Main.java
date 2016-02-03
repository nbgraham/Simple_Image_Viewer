import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**
 * It's bad form to create an entire program in a single class
 * with only a main method, but this is just a demo, written
 * in-class on Jan 28, 2016 and is for demonstration of Java
 * Swing usage only.
 * 
 * @author Nicholas Graham 113038384
 */
public class Main {
	//Create a new JFrame (i.e. an application window
	final static JFrame frame = new JFrame();
	final static JLabel pictureLabel = new JLabel();
	final static List<File> pictureFiles = new ArrayList<File>();
	final static List<Integer> currentFileIndex = new ArrayList<Integer>();

	public static void main( final String[] args ) {

		//When the when is closed, exit the application
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		//initial size of window
		frame.setSize( 600, 500 );
		//give the window a title
		frame.setTitle( "Image Viewer" );
		
		/*
		final ImageIcon icon = new ImageIcon( "/Users/Nicholas/Pictures/Picture1.png" );
		icon.setImage( icon.getImage().getScaledInstance(
				30, 30, Image.SCALE_FAST ) );
		pictureLabel.setIcon( icon );
		*/
		
		currentFileIndex.add(new Integer(0));
		
		final JButton nextButton = new JButton("Next->");
		final JButton prevButton = new JButton("<-Previous");
		
		nextButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				nextImage();
			}
			
		});
		
		prevButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				prevImage();
			}
			
		});
		
		final JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(prevButton);
		buttonPanel.add(nextButton);
		
		frame.getContentPane().setLayout( new BorderLayout());
		
		//add the various components to the window's contents;
		frame.getContentPane().add( pictureLabel, BorderLayout.CENTER );
		frame.getContentPane().add(buttonPanel, BorderLayout.PAGE_END);
		
		//create a menu bar, menu, and two menu items:
		//one which opens an "Open Directory" dialog, and one which
		//closes the window and program
		final JMenuBar menuBar = new JMenuBar();
		final JMenu fileMenu = new JMenu();
		fileMenu.setText( "File" );
		final JMenuItem close =  new JMenuItem();
		close.setText( "Close" );
		close.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent arg0 ) {
				frame.dispose();
				System.exit( 0 );
			}
		});
		
		JMenuItem open = new JMenuItem();
		open.setText( "Open" );
		open.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				//create and show a dialog for allowing user to select
				//a directory
				final JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
				//if the user says "OK" (i.e. not "Cancel") then set the
				//chosen directory's path as the label contents
				if( jfc.showOpenDialog( frame ) == JFileChooser.APPROVE_OPTION ) {
					pictureFiles.clear();
					pictureFiles.addAll(getPicturesInDirectory(jfc.getSelectedFile().getAbsolutePath()));
					currentFileIndex.set(0, new Integer(0));
					updateImage();
				}
			}
		});
		
		fileMenu.add( open );
		fileMenu.add( close );
		menuBar.add( fileMenu );
		
		frame.setJMenuBar( menuBar );
		
		//and finally, now that the window is populated with widgets
		//and a menu system, show the window!
		frame.setVisible( true );
	}
	
	public static List<File> getPicturesInDirectory(String directory)
	{
		File folder = new File(directory);
		File[] listOfFiles = folder.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String fileName) {
				Pattern acceptExtensions = Pattern.compile(".*\\.(gif|jpg|png)");
				return acceptExtensions.matcher(fileName).matches();
			}
		});
		return Arrays.asList(listOfFiles);
	}
	
	public static void nextImage()
	{
		Integer nextValue = currentFileIndex.get(0) + 1;
		if (nextValue.compareTo(new Integer(pictureFiles.size())) >= 0)
		{
			nextValue = new Integer(0);
		}
		currentFileIndex.set(0, nextValue);
		updateImage();
	}
	
	public static void prevImage()
	{
		Integer prevValue = currentFileIndex.get(0) - 1;
		if (prevValue.compareTo(new Integer(0)) < 0)
		{
			prevValue = new Integer(pictureFiles.size()-1);
		}
		currentFileIndex.set(0, prevValue);
		updateImage();
	}
	
	public static void updateImage()
	{
		pictureLabel.setIcon(new ImageIcon(pictureFiles.get(currentFileIndex.get(0)).getAbsolutePath()));
	}
}


