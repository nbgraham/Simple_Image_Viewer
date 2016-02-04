import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;


import javax.imageio.ImageIO;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SpinnerListModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JMenuItem;


class ImagesModel {
	SpinnerListModel picturesList;
	final JLabel pictureLabel = new JLabel(new ImageIcon());

	ResizeActionListener resizeListener = new ResizeActionListener();
	NextPictureAction nextAction = new NextPictureAction();
	PrevPictureAction prevAction = new PrevPictureAction();
	SpinnerChangeListener spinnerListener = new SpinnerChangeListener();
	NextOnClickListener nextOnClickListener = new NextOnClickListener();
	OpenFileListener openListener = new OpenFileListener();
	
	ImagesModel()
	{
		picturesList = new SpinnerListModel();
	}
	
	class OpenFileListener implements ActionListener {
		public void actionPerformed( ActionEvent e ) {
			//create and show a dialog for allowing user to select
			//a directory
			final JFileChooser jfc = new JFileChooser();
			jfc.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
			//if the user says "OK" (i.e. not "Cancel") then set the
			//chosen directory's path as the label contents
			if( jfc.showOpenDialog( ((JMenuItem)e.getSource()).getParent().getParent() ) == JFileChooser.APPROVE_OPTION ) {
				picturesList.setList(getPicturesInDirectory(jfc.getSelectedFile().getAbsolutePath()));
				updateImage();
			}
		}
	}
	
	class NextOnClickListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent arg0) {
			nextImage();
		}
		//Necessary methods, do nothing
		@Override
		public void mouseEntered(MouseEvent arg0) {			
		}
		@Override
		public void mouseExited(MouseEvent arg0) {	
		}
		@Override
		public void mousePressed(MouseEvent arg0) {			
		}
		@Override
		public void mouseReleased(MouseEvent arg0) {			
		}
	}
	
	class SpinnerChangeListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent arg0) {
			updateImage();
		}

	}
	
	class ResizeActionListener extends ComponentAdapter {
		@Override
		public void componentResized(ComponentEvent e) {
			//Resize the actual frame
			((JFrame)e.getSource()).resize(e.getComponent().getSize());
			
			//Check the difference between the frame size and the image size, 
			double diffH = Math.abs(e.getComponent().getSize().getHeight() - pictureLabel.getSize().getHeight() - 120);
			double diffW = Math.abs(e.getComponent().getSize().getWidth() - pictureLabel.getSize().getWidth() - 120);
			double diff = diffH < diffW ? diffH : diffW;
			
			if (diff>25)
			{
				updateImage();
			}
		}			
	}
	
	class NextPictureAction extends AbstractAction {
	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public NextPictureAction() {
	        super("Next->");
	        putValue(SHORT_DESCRIPTION, "Move to the next picture");
	        putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_RIGHT));
	    }
	    public void actionPerformed(ActionEvent e) {
	        nextImage();
	    }
	}
	
	class PrevPictureAction extends AbstractAction {
	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public PrevPictureAction() {
	        super("<-Previous");
	        putValue(SHORT_DESCRIPTION, "Move to the previous picture");
	        putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_LEFT));
	    }
	    public void actionPerformed(ActionEvent e) {
	        prevImage();
	    }
	}
	
	public void nextImage()
	{
		File next = (File) picturesList.getNextValue();
		if (next != null)
		{
			picturesList.setValue(next);
			updateImage();
		}
	}
	
	public void prevImage()
	{
		File prev = (File) picturesList.getPreviousValue();
		if (prev != null)
		{
			picturesList.setValue(prev);
			updateImage();
		}
	}
	
	public void updateImage()
	{
		String val = picturesList.getValue().toString();
		if ( val != "empty")
		{
			BufferedImage bimg;
			try {
				bimg = ImageIO.read((File) picturesList.getValue());
				int imgWidth = bimg.getWidth();
				int imgHeight = bimg.getHeight();
				
				Rectangle r = pictureLabel.getParent().getBounds();
				int frameWidth = r.width;
				int frameHeight = r.height;
				
				double resizeRatio;
				if (imgHeight > frameHeight)
				{
					resizeRatio = (double) frameHeight/imgHeight;
					imgWidth *= resizeRatio;
					imgHeight *= resizeRatio;
				}
				if (imgWidth > frameWidth)
				{
					resizeRatio = (double) frameWidth/imgWidth;
					imgWidth *= resizeRatio;
					imgHeight *= resizeRatio;
				}
				
				pictureLabel.setIcon(new ImageIcon(((File)picturesList.getValue()).getAbsolutePath()));
				ImageIcon icon = (ImageIcon) pictureLabel.getIcon();
				icon.setImage( icon.getImage().getScaledInstance(
						imgWidth, imgHeight, Image.SCALE_FAST ) );
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else pictureLabel.setIcon(new ImageIcon());
	}
	
	public List<File> getPicturesInDirectory(String directory)
	{
		File folder = new File(directory);
		File[] listOfFiles = folder.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String fileName) {
				Pattern acceptExtensions = Pattern.compile(".*\\.(gif|jpg|png)");
				return acceptExtensions.matcher(fileName).matches();
			}
		});
		if (listOfFiles.length == 0)
		{
			ArrayList<File> files = new ArrayList<File>();
			files.add(new File("empty"));
			return files;
		}
		return Arrays.asList(listOfFiles);
	}
}
