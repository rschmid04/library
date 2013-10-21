package gui;


import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.Color;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.SystemColor;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.BorderLayout;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import domain.Book;

public class BookMasterFrame extends JFrame implements Observer {

	private JPanel contentPane;
	JList<String> listBooks;
	JLabel lblSelected;
	JPanel booksPanel;

	/**
	 * Create the frame.
	 */
	public BookMasterFrame(List<Book> books, int nbrOfExemplars) {
		
		
		//TODO AUFTEILEN der Panels Books und Loans
		//TODO Aufbau der Panels mit init Funktionen 
		//sonst ist es zu unübersichtlich
		
		initBookList(books);
		
		
		this.setIconImage(new ImageIcon(getClass().getResource("/library.png")).getImage());
		setTitle("Library");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension dimension = new Dimension();
		dimension.height = 400;
		dimension.width = 600;
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
		
		booksPanel = new JPanel();
		
		tabbedPane.addTab("<html><body marginwidth=10 marginheight=10>Books</body></html>", new ImageIcon(getClass().getResource("/book.png")), booksPanel, null);
		GridBagLayout gbl_booksPanel = new GridBagLayout();
		gbl_booksPanel.columnWidths = new int[]{0, 0};
		gbl_booksPanel.rowHeights = new int[]{70, 0, 0};
		gbl_booksPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_booksPanel.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		booksPanel.setLayout(gbl_booksPanel);
		
		initInventoryStatisticPanel(books.size(), nbrOfExemplars);
		
		initInventoryBooksPanel();
		
		
		//tabbedPane.setBackgroundAt(0, SystemColor.menu);
		
		
		
		JPanel loanPanel = new JPanel();
		tabbedPane.addTab("<html><body marginwidth=10 marginheight=10>Loans</body></html>", new ImageIcon(getClass().getResource("/switch.png")), loanPanel, null);
		
		
		contentPane.add(tabbedPane);
		
		
	}

	private void initInventoryBooksPanel() {
		JPanel inventoryBooksPanel = new JPanel();
		inventoryBooksPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), "Book Inventory", TitledBorder.LEADING, TitledBorder.TOP, null, SystemColor.textText));
		GridBagConstraints gbc_inventoryBooksPanel = new GridBagConstraints();
		gbc_inventoryBooksPanel.insets = new Insets(5, 5, 5, 5);
		gbc_inventoryBooksPanel.fill = GridBagConstraints.BOTH;
		gbc_inventoryBooksPanel.gridx = 0;
		gbc_inventoryBooksPanel.gridy = 1;
		booksPanel.add(inventoryBooksPanel, gbc_inventoryBooksPanel);
		inventoryBooksPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel panelInventoryNavigation = new JPanel();
		panelInventoryNavigation.setBorder(null);
		inventoryBooksPanel.add(panelInventoryNavigation, BorderLayout.NORTH);
		GridBagLayout gbl_panelInventoryNavigation = new GridBagLayout();
		gbl_panelInventoryNavigation.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panelInventoryNavigation.rowHeights = new int[]{0, 0};
		gbl_panelInventoryNavigation.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panelInventoryNavigation.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panelInventoryNavigation.setLayout(gbl_panelInventoryNavigation);
		
		JLabel lblTextSelected = new JLabel("Selected:");
		GridBagConstraints gbc_lblTextSelected = new GridBagConstraints();
		gbc_lblTextSelected.insets = new Insets(5, 5, 5, 5);
		gbc_lblTextSelected.gridx = 0;
		gbc_lblTextSelected.gridy = 0;
		panelInventoryNavigation.add(lblTextSelected, gbc_lblTextSelected);
		
		lblSelected = new JLabel("0");
		GridBagConstraints gbc_lblSelected = new GridBagConstraints();
		gbc_lblSelected.insets = new Insets(5, 5, 5, 5);
		gbc_lblSelected.gridx = 1;
		gbc_lblSelected.gridy = 0;
		panelInventoryNavigation.add(lblSelected, gbc_lblSelected);
		
		JButton btnShowSelected = new JButton("<html><body style=\"margin:4 0 4 0\">Show selected</body></html>");
		btnShowSelected.setBackground(UIManager.getColor("Button.background"));
		GridBagConstraints gbc_btnShowSelected = new GridBagConstraints();
		gbc_btnShowSelected.anchor = GridBagConstraints.EAST;
		gbc_btnShowSelected.insets = new Insets(5, 5, 5, 5);
		gbc_btnShowSelected.gridx = 3;
		gbc_btnShowSelected.gridy = 0;
		panelInventoryNavigation.add(btnShowSelected, gbc_btnShowSelected);
		
		JButton btnAddNewBook = new JButton("<html><body style=\"margin:4 0 4 0\">Add new book</body></html>");
		GridBagConstraints gbc_btnAddNewBook = new GridBagConstraints();
		gbc_btnAddNewBook.insets = new Insets(0, 0, 0, 5);
		gbc_btnAddNewBook.anchor = GridBagConstraints.EAST;
		gbc_btnAddNewBook.gridx = 4;
		gbc_btnAddNewBook.gridy = 0;
		panelInventoryNavigation.add(btnAddNewBook, gbc_btnAddNewBook);
		
		JScrollPane scrollPane = new JScrollPane();
		inventoryBooksPanel.add(scrollPane, BorderLayout.CENTER);
		
		scrollPane.setViewportView(listBooks);
		
	}

	private void initInventoryStatisticPanel(int nbrOfBooks, int nbrOfExemplars) {
		JPanel inventoryStatisticPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) inventoryStatisticPanel.getLayout();
		flowLayout.setVgap(10);
		flowLayout.setHgap(10);
		flowLayout.setAlignment(FlowLayout.LEFT);
		inventoryStatisticPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), "Inventory Statistic", TitledBorder.LEADING, TitledBorder.TOP, null, SystemColor.textText));
		GridBagConstraints gbc_inventoryStatisticPanel = new GridBagConstraints();
		
		gbc_inventoryStatisticPanel.insets = new Insets(10, 5, 5, 5);
		gbc_inventoryStatisticPanel.fill = GridBagConstraints.HORIZONTAL;
		booksPanel.add(inventoryStatisticPanel, gbc_inventoryStatisticPanel);
		
		JLabel lblTextNbrBooks = new JLabel("Number of books:");
		inventoryStatisticPanel.add(lblTextNbrBooks);
		
		JLabel lblNbrBooks = new JLabel(String.valueOf(nbrOfBooks));
		inventoryStatisticPanel.add(lblNbrBooks);
		
		JLabel lbltextNbrExemplars = new JLabel("<html><body style=\"margin-left:20px\">Number of exemplars:</body></html>");
		inventoryStatisticPanel.add(lbltextNbrExemplars);
		
		JLabel lblNbrExemplars = new JLabel(String.valueOf(nbrOfExemplars));
		inventoryStatisticPanel.add(lblNbrExemplars);
	}

	private void initBookList(List<Book> books) {
		listBooks = new JList<String>();
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		int i = 0;
		for(Book book : books)
		{
			listModel.add(i, book.getName());
			i++;
		}
		
		listBooks.setModel(listModel);
		
		listBooks.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e)
			{
				int[] indices = listBooks.getSelectedIndices();
				lblSelected.setText(String.valueOf(indices.length));
			}
		});
		
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}
}
