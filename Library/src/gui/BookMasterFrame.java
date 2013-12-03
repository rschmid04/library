package gui;


import javax.swing.*;
import javax.swing.border.*;

import java.awt.Color;
import java.awt.Font;

import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import domain.Library;

public class BookMasterFrame extends JFrame implements Observer {

	private JPanel contentPane;
	private JPanel booksPanel;
	private JPanel loanPanel;

	/**
	 * Create the frame.
	 */
	public BookMasterFrame(Library library) {
		this.booksPanel = new BookPanel(library);
		this.loanPanel = new LoanPanel(library);
		//List<Book> books, int nbrOfExemplars, List<Loan> loans
		//TODO AUFTEILEN der Panels Books und Loans
		//TODO Aufbau der Panels mit init Funktionen 
		//sonst ist es zu unübersichtlich
		
		
		this.setIconImage(new ImageIcon(getClass().getResource("/library.png")).getImage());
		setTitle("Library");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension dimension = new Dimension();
		dimension.height = 600;
		dimension.width = 800;
		this.setMinimumSize(dimension);
		setBounds(100, 100, 600, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(10, 4, 4, 4));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font("Tahoma", Font.PLAIN, 12));
		TitledBorder titledBorder = new TitledBorder(new CompoundBorder(null, UIManager.getBorder("CheckBoxMenuItem.border")), "Library", TitledBorder.RIGHT, TitledBorder.TOP, new Font("Tahoma", Font.BOLD, 16), new Color(0, 0, 0));
		//TitledBorder titledBorder = new TitledBorder(new CompoundBorder(null, UIManager.getBorder("CheckBoxMenuItem.border")), "Bibliothek", TitledBorder.RIGHT, TitledBorder.TOP, new Font("Tahoma", Font.BOLD, 16), new Color(0, 0, 0));
		tabbedPane.setBorder(titledBorder);
		
		tabbedPane.addTab("<html><body marginwidth=10 marginheight=10>Books</body></html>", new ImageIcon(getClass().getResource("/book.png")), this.booksPanel, null);
		
		tabbedPane.addTab("<html><body marginwidth=10 marginheight=10>Loans</body></html>", new ImageIcon(getClass().getResource("/switch.png")), this.loanPanel, null);
		
		contentPane.add(tabbedPane);
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}
}
