import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * It's bad form to create an entire program in a single class
 * with only a main method, but this is just a demo, written
 * in-class on Jan 28, 2016 and is for demonstration of Java
 * Swing usage only.
 * 
 * @author Nicholas Graham 113038384
 */
public class Main {
	
	static class NextPictureAction extends AbstractAction {
	    public NextPictureAction() {
	        super("Next->");
	        putValue(SHORT_DESCRIPTION, "Move to the next picture");
	        putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_RIGHT));
	    }
	    public void actionPerformed(ActionEvent e) {
	        nextImage();
	    }
	}
	
	static class PrevPictureAction extends AbstractAction {
	    public PrevPictureAction() {
	        super("<-Previous");
	        putValue(SHORT_DESCRIPTION, "Move to the previous picture");
	        putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_LEFT));
	    }
	    public void actionPerformed(ActionEvent e) {
	        prevImage();
	    }
	}
	
	final static JFrame frame = new JFrame();
	final static JLabel pictureLabel = new JLabel();
	final static List<File> pictureFiles = new ArrayList<File>();
	final static SpinnerListModel picturesList = new SpinnerListModel();

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
				
		final JButton nextButton = new JButton(new NextPictureAction());
		final JButton prevButton = new JButton(new PrevPictureAction());
		
		final JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(prevButton);
		buttonPanel.add(nextButton);
		
		final JSpinner pictureSpinner = new JSpinner(picturesList);
		pictureSpinner.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				updateImage();
			}
			
		});
		
		pictureLabel.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				nextImage();
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		frame.getContentPane().setLayout( new BorderLayout());
		//add the various components to the window's contents;
		frame.getContentPane().add(pictureLabel, BorderLayout.CENTER );
		frame.getContentPane().add(buttonPanel, BorderLayout.PAGE_END);
		frame.getContentPane().add(pictureSpinner, BorderLayout.PAGE_START);

		
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
		
		final JMenu pictureMenu = new JMenu("Picture");
		final JMenuItem nextPicture = new JMenuItem(new NextPictureAction());
		final JMenuItem prevPicture = new JMenuItem(new PrevPictureAction());
		
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
					picturesList.setList(pictureFiles);
					updateImage();
				}
			}
		});
		
		fileMenu.add( open );
		fileMenu.add( close );
		menuBar.add( fileMenu );
		
		pictureMenu.add(nextPicture);
		pictureMenu.add(prevPicture);
		menuBar.add(pictureMenu);
		
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
		if (pictureFiles.size() > 0)
		{
			picturesList.setValue(picturesList.getNextValue());
			updateImage();
		}
	}
	
	public static void prevImage()
	{
		if (pictureFiles.size() > 0)
		{
			picturesList.setValue(picturesList.getPreviousValue());
			updateImage();
		}
	}
	
	public static void updateImage()
	{
		if (pictureFiles.size() > 0)
		{
			pictureLabel.setIcon(new ImageIcon(((File)picturesList.getValue()).getAbsolutePath()));
		}
	}
}


