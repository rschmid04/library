package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.RowFilter.ComparisonType;
import javax.swing.RowFilter.Entry;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import domain.Book;
import domain.Copy;
import domain.Library;
import domain.Loan;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JCheckBox;

public class BookPanel extends JPanel {;
	private JButton btnShowSelected;
	private HashMap<String, BookDetailFrame> openBooks;
	private Library library;
	private List<Book> listOfBooks;
	private JTable tblBooks;
	private JTextField txtSearch;
	private TableRowSorter<DefaultTableModel> sorter;
	private JCheckBox chckbxOnlyAvailable;
	
	public BookPanel(Library library)
	{
		this.library = library;
		this.listOfBooks = library.getBooks();
		this.openBooks = new HashMap<String, BookDetailFrame>();
		
		initPanelLayout();
		
		initInventoryStatisticPanel();
		
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
		gbl_panelInventoryNavigation.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
		gbl_panelInventoryNavigation.rowHeights = new int[]{0, 0, 0};
		gbl_panelInventoryNavigation.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panelInventoryNavigation.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panelInventoryNavigation.setLayout(gbl_panelInventoryNavigation);
		
		btnShowSelected = new JButton("<html><body style=\"margin:4 0 4 0\">Show selected</body></html>");
		btnShowSelected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Iterate through selected books and open detail view for each
				//Check if a book detail is already opened
				
				int[] selectedRows = tblBooks.getSelectedRows();
				for(int i = 0; i < selectedRows.length; i++)
				{
					int modelIndex = tblBooks.convertRowIndexToModel(selectedRows[i]);
					Book book = listOfBooks.get(modelIndex);
					if (openBooks.containsKey(book.getName())){
						BookDetailFrame detail = openBooks.get(book.getName());
						if(detail.isVisible() == false)
						{
							detail.setVisible(true);
						}
					}
					else
					{
						BookDetailFrame newDetail = new BookDetailFrame(library, book);
						openBooks.put(book.getName(), newDetail);
						newDetail.setVisible(true);
					}
				}
				
			}
		});
		
		JLabel lblAllBooksOf = new JLabel("All books of the library is shown in table below");
		GridBagConstraints gbc_lblAllBooksOf = new GridBagConstraints();
		gbc_lblAllBooksOf.anchor = GridBagConstraints.WEST;
		gbc_lblAllBooksOf.gridwidth = 5;
		gbc_lblAllBooksOf.insets = new Insets(5, 5, 5, 5);
		gbc_lblAllBooksOf.gridx = 0;
		gbc_lblAllBooksOf.gridy = 0;
		panelInventoryNavigation.add(lblAllBooksOf, gbc_lblAllBooksOf);
		
		txtSearch = new JTextField();
		txtSearch.setBorder(new RoundedBorder());
		GridBagConstraints gbc_txtSearch = new GridBagConstraints();
		gbc_txtSearch.insets = new Insets(0, 5, 5, 5);
		gbc_txtSearch.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtSearch.gridx = 0;
		gbc_txtSearch.gridy = 1;
		panelInventoryNavigation.add(txtSearch, gbc_txtSearch);
		txtSearch.setColumns(10);
		
		txtSearch.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				newFilter();
			}
			
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				newFilter();
			}
			
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				newFilter();
			}
		});
		
		chckbxOnlyAvailable = new JCheckBox("Only available");
		GridBagConstraints gbc_chckbxOnlyAvailable = new GridBagConstraints();
		gbc_chckbxOnlyAvailable.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxOnlyAvailable.gridx = 1;
		gbc_chckbxOnlyAvailable.gridy = 1;
		chckbxOnlyAvailable.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				newFilter();				
			}
		});
		panelInventoryNavigation.add(chckbxOnlyAvailable, gbc_chckbxOnlyAvailable);
		
		
		btnShowSelected.setEnabled(false);
		btnShowSelected.setBackground(UIManager.getColor("Button.background"));
		GridBagConstraints gbc_btnShowSelected = new GridBagConstraints();
		gbc_btnShowSelected.anchor = GridBagConstraints.EAST;
		gbc_btnShowSelected.insets = new Insets(0, 5, 5, 5);
		gbc_btnShowSelected.gridx = 3;
		gbc_btnShowSelected.gridy = 1;
		panelInventoryNavigation.add(btnShowSelected, gbc_btnShowSelected);
		
		JButton btnAddNewBook = new JButton("<html><body style=\"margin:4 0 4 0\">Add new book</body></html>");
		GridBagConstraints gbc_btnAddNewBook = new GridBagConstraints();
		gbc_btnAddNewBook.insets = new Insets(0, 5, 5, 0);
		gbc_btnAddNewBook.anchor = GridBagConstraints.EAST;
		gbc_btnAddNewBook.gridx = 4;
		gbc_btnAddNewBook.gridy = 1;
		panelInventoryNavigation.add(btnAddNewBook, gbc_btnAddNewBook);
		
		JScrollPane scrollPane = new JScrollPane();
		inventoryBooksPanel.add(scrollPane, BorderLayout.CENTER);
		
		tblBooks = new JTable();
		initBookTable();
		scrollPane.setViewportView(tblBooks);
		
	}

	private void initInventoryStatisticPanel() {
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
		
		JLabel lblNbrBooks = new JLabel(String.valueOf(listOfBooks.size()));
		inventoryStatisticPanel.add(lblNbrBooks);
		
		JLabel lbltextNbrExemplars = new JLabel("<html><body style=\"margin-left:20px\">Number of exemplars:</body></html>");
		inventoryStatisticPanel.add(lbltextNbrExemplars);
		
		JLabel lblNbrExemplars = new JLabel(String.valueOf(library.getCopies().size()));
		inventoryStatisticPanel.add(lblNbrExemplars);
	}

	private void initBookTable() {
		tblBooks.setShowVerticalLines(false);
		tblBooks.setAutoCreateRowSorter(true);
		tblBooks.setDragEnabled(false);
		tblBooks.setShowHorizontalLines(false);
		tblBooks.setShowGrid(false);
		tblBooks.setFillsViewportHeight(true);
		tblBooks.getTableHeader().setReorderingAllowed(false);
		
		tblBooks.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				
				if(tblBooks.getSelectedRowCount() > 0)
					btnShowSelected.setEnabled(true);
				else
					btnShowSelected.setEnabled(false);
			}
		});
		
		DefaultTableModel m = new DefaultTableModel(){
			boolean[] canEdit = new boolean[]{
	                false, false, false, false
	        };
			
			@Override
	        public boolean isCellEditable(int rowIndex, int columnIndex) {
	            return canEdit[columnIndex];
	        }
		};
		
		m.addColumn("Available");
		m.addColumn("Title");
		m.addColumn("Author");
		m.addColumn("Publisher");
		
		String available;
		String title;
		String author;
		String publisher;
		
		for(Book b : listOfBooks)
		{
				
				available = library.getAvailabilityOfBook(b);
				title = b.getName();
				author = b.getAuthor();
				publisher = b.getPublisher();
			
				m.addRow(new Object[] {available, title, author, publisher});
		}
		tblBooks.setModel(m);
		sorter = new TableRowSorter<DefaultTableModel>(m);
		
		tblBooks.setRowSorter(sorter);
		tblBooks.getColumnModel().getColumn(0).setMaxWidth(80);
		tblBooks.getColumnModel().getColumn(1).setPreferredWidth(400);
		tblBooks.getColumnModel().getColumn(2).setPreferredWidth(100);
		tblBooks.getColumnModel().getColumn(3).setPreferredWidth(100);
		tblBooks.doLayout();
		tblBooks.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
	}
	
	private void newFilter()
	{
		RowFilter<Object, Object> rf = null;
		
		
	    try {
	    	//check if we have to display only available
	    	if(chckbxOnlyAvailable.isSelected()){
	    		List<RowFilter<Object, Object>> rfs = new ArrayList<RowFilter<Object,Object>>();
	    		
	    		rfs.add(RowFilter.regexFilter(txtSearch.getText(), 0, 1, 2, 3));
	    		
	    		RowFilter<Object, Object> greaterThanZeroFilter = new RowFilter<Object,Object>() {
	    			   public boolean include(Entry<? extends Object, ? extends Object> entry) {

	    			      for (int i = entry.getValueCount() - 1; i >= 0; i--) {
	    			          if(entry.getValue(i) != null)
	    			          {
	    			        	  String valueOfCell = entry.getStringValue(i);
	    			              if(isNumeric(valueOfCell))
	    			            	  if(Integer.parseInt(valueOfCell) > 0)
	    			            		  return true;
	    			          }
	    			      }
	    			      // None of the columns start with "a"; return true so that this
	    			      // entry is shown
	    			      return false;
	    			   }
	    			};
	    		
	    		rfs.add(greaterThanZeroFilter);
	    		rf = RowFilter.andFilter(rfs);
	    				
	    	}
	    	else
	    		rf = RowFilter.regexFilter(txtSearch.getText(), 0, 1, 2, 3);
	    } catch (java.util.regex.PatternSyntaxException e) {
	        return;
	    }

	    sorter.setRowFilter(rf);
	}
	
	public static boolean isNumeric(String str)  
	{  
	  try  
	  {  
	    int d = Integer.parseInt(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
}
