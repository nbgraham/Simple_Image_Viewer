import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSpinner;

/**
 *	Simple Image Viewer File
 * 
 * @author Nicholas Graham 113038384
 */
public class Main {

	final static JFrame frame = new JFrame();
	final static List<File> pictureFiles = new ArrayList<File>();

	public static void main( final String[] args ) {
		//Houses the list of files, the picture image, and the listeners
		ImagesModel model = new ImagesModel();
		
		//When the when is closed, exit the application
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		//initial size of window
		frame.setSize( 600, 500 );
		//give the window a title
		frame.setTitle( "Image Viewer" );
		
		//Add a listener to the frame so that the image can be resized when the frame is resized
		frame.addComponentListener(model.resizeListener);
		
		//Next, Previous Buttons
		final JButton nextButton = new JButton(model.nextAction);
		final JButton prevButton = new JButton(model.prevAction);
		final JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(prevButton);
		buttonPanel.add(nextButton);
		
		//Image panel
		final JPanel imagePanel = new JPanel();
		model.pictureLabel.setHorizontalAlignment(JLabel.CENTER);
		model.pictureLabel.setVerticalAlignment(JLabel.CENTER);
		imagePanel.add(model.pictureLabel);
		model.pictureLabel.addMouseListener(model.nextOnClickListener);
		
		//Image file spinner
		final JSpinner pictureSpinner = new JSpinner(model.picturesList);
		pictureSpinner.addChangeListener(model.spinnerListener);
		
		//add the various components to the window's contents;
		frame.getContentPane().setLayout( new BorderLayout());
		frame.getContentPane().add(imagePanel, BorderLayout.CENTER );
		frame.getContentPane().add(buttonPanel, BorderLayout.PAGE_END);
		frame.getContentPane().add(pictureSpinner, BorderLayout.PAGE_START);

		
		//Create the menu bar
		final JMenuBar menuBar = new JMenuBar();
		//File menu
		final JMenu fileMenu = new JMenu();
		fileMenu.setText( "File" );
		//Close application menu item
		final JMenuItem close =  new JMenuItem();
		close.setText( "Close application" );
		close.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent arg0 ) {
				frame.dispose();
				System.exit( 0 );
			}
		});
		//Open picture menu item
		JMenuItem open = new JMenuItem();
		open.setText( "Open" );
		open.addActionListener(model.openListener);
		
		//Picture menu
		final JMenu pictureMenu = new JMenu("Picture");
		final JMenuItem nextPicture = new JMenuItem(model.nextAction);
		final JMenuItem prevPicture = new JMenuItem(model.prevAction);
		
		//Add items to file menu
		fileMenu.add( open );
		fileMenu.add( close );
	
		//Add items to picture menu
		pictureMenu.add(nextPicture);
		pictureMenu.add(prevPicture);
		
		//Add the menus to the menubar
		menuBar.add( fileMenu );
		menuBar.add(pictureMenu);
		
		frame.setJMenuBar( menuBar );
		
		//and finally, now that the window is populated with widgets
		//and a menu system, show the window!
		frame.setVisible( true );
	}
}


