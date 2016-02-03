import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * It's bad form to create an entire program in a single class
 * with only a main method, but this is just a demo, written
 * in-class on Jan 28, 2016 and is for demonstration of Java
 * Swing usage only.
 * 
 * @author Nicholas Graham 113038384
 */
public class Main {
	public static void main( final String[] args ) {
		//Create a new JFrame (i.e. an application window
		final JFrame frame = new JFrame();
		//When the when is closed, exit the application
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		//initial size of window
		frame.setSize( 600, 500 );
		//give the window a title
		frame.setTitle( "Image Viewer" );
		
		//another label with a picture/image as its contents, taken
		//from a image on my (Nic Grounds') hard disk, resized to be
		//small like an "icon" should be
		final JLabel pictureLabel = new JLabel();
		final ImageIcon icon = new ImageIcon( "/Users/Nicholas/Pictures/Picture1.png" );
		icon.setImage( icon.getImage().getScaledInstance(
				30, 30, Image.SCALE_FAST ) );
		pictureLabel.setIcon( icon );
		
		//use a FlowLayout to arrange all window contents in a row,
		//left-aligned, spaced apart by 5 pixels
		frame.getContentPane().setLayout( new BorderLayout());
		
		//add the various components to the window's contents;
		frame.getContentPane().add( pictureLabel, BorderLayout.CENTER );
		
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
				//jfc.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
				jfc.setFileSelectionMode( JFileChooser.FILES_ONLY );
				//if the user says "OK" (i.e. not "Cancel") then set the
				//chosen directory's path as the label contents
				if( jfc.showOpenDialog( frame ) == JFileChooser.APPROVE_OPTION ) {
					pictureLabel.setIcon(new ImageIcon(jfc.getSelectedFile().getAbsolutePath()));;
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
}
