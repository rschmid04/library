package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import domain.Book;

public class BookPanel extends JPanel {

	private JList<String> listBooks;
	private JLabel lblSelected;;
	
	public BookPanel(List<Book> books, int nbrOfExemplars)
	{
		
		initPanelLayout();
		
		initBookList(books);
		
		initInventoryStatisticPanel(books.size(), nbrOfExemplars);
		
		initInventoryBooksPanel();
	}
	
	private void initPanelLayout()
	{
		GridBagLayout gbl_booksPanel = new GridBagLayout();
		gbl_booksPanel.columnWidths = new int[]{0, 0};
		gbl_booksPanel.rowHeights = new int[]{70, 0, 0};
		gbl_booksPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_booksPanel.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		this.setLayout(gbl_booksPanel);
	}
	
	private void initInventoryBooksPanel() {
		JPanel inventoryBooksPanel = new JPanel();
		inventoryBooksPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), "Book Inventory", TitledBorder.LEADING, TitledBorder.TOP, null, SystemColor.textText));
		GridBagConstraints gbc_inventoryBooksPanel = new GridBagConstraints();
		gbc_inventoryBooksPanel.insets = new Insets(5, 5, 5, 5);
		gbc_inventoryBooksPanel.fill = GridBagConstraints.BOTH;
		gbc_inventoryBooksPanel.gridx = 0;
		gbc_inventoryBooksPanel.gridy = 1;
		this.add(inventoryBooksPanel, gbc_inventoryBooksPanel);
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
		this.add(inventoryStatisticPanel, gbc_inventoryStatisticPanel);
		
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
}
