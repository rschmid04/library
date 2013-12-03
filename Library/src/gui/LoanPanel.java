package gui;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.JScrollBar;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.border.CompoundBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import domain.Library;
import domain.Loan;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LoanPanel extends JPanel {
	private JTextField txtSearch;
	private JTable tblLoans;
	private JPanel panelLoans;
	
	private TableRowSorter<DefaultTableModel> sorter;
	private JButton btnShowSelectedLoans;
	private JCheckBox chckbxOnlyOverdue;
	private List<Loan> loans;
	private HashMap<Loan, LoanDetailFrame> openLoans;
	private Library library;

	/**
	 * Create the panel.
	 */
	public LoanPanel(Library library) {
		this.loans = library.getLoans();
		this.library = library;
		
		openLoans = new HashMap<Loan, LoanDetailFrame>();
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {1, 0};
		gridBagLayout.rowHeights = new int[]{70, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		initLoanStatistic();
		
		initLoans();
		initLoanTable();
		

	}
	private void initLoans()
	{
		panelLoans = new JPanel();
		panelLoans.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), "Collected loans", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLACK));
		GridBagConstraints gbc_panelLoans = new GridBagConstraints();
		gbc_panelLoans.insets = new Insets(5, 5, 5, 5);
		gbc_panelLoans.fill = GridBagConstraints.BOTH;
		gbc_panelLoans.gridx = 0;
		gbc_panelLoans.gridy = 1;
		add(panelLoans, gbc_panelLoans);
		panelLoans.setLayout(new BorderLayout(0, 0));
		
		JPanel panelLoanNavigation = new JPanel();
		panelLoans.add(panelLoanNavigation, BorderLayout.NORTH);
		GridBagLayout gbl_panelLoanNavigation = new GridBagLayout();
		gbl_panelLoanNavigation.columnWidths = new int[] {0, 40, 40, 40, 0};
		gbl_panelLoanNavigation.rowHeights = new int[] {0, 0, 0};
		gbl_panelLoanNavigation.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panelLoanNavigation.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panelLoanNavigation.setLayout(gbl_panelLoanNavigation);
		
		JLabel lblTextLoans = new JLabel("All loans for every customer are in the following table");
		GridBagConstraints gbc_lblTextLoans = new GridBagConstraints();
		gbc_lblTextLoans.gridwidth = 4;
		gbc_lblTextLoans.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblTextLoans.insets = new Insets(5, 5, 5, 5);
		gbc_lblTextLoans.gridx = 0;
		gbc_lblTextLoans.gridy = 0;
		panelLoanNavigation.add(lblTextLoans, gbc_lblTextLoans);
		
		txtSearch = new JTextField();
		txtSearch.setBorder(new RoundedBorder());
		txtSearch.setText("");
		GridBagConstraints gbc_txtSearch = new GridBagConstraints();
		gbc_txtSearch.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtSearch.insets = new Insets(5, 5, 5, 5);
		gbc_txtSearch.anchor = GridBagConstraints.NORTHWEST;
		gbc_txtSearch.gridx = 0;
		gbc_txtSearch.gridy = 1;
		panelLoanNavigation.add(txtSearch, gbc_txtSearch);
		txtSearch.setColumns(14);
		
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
		
		
		chckbxOnlyOverdue = new JCheckBox("Only overdue");
		chckbxOnlyOverdue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				newFilter();
			}
		});
		GridBagConstraints gbc_chckbxOnlyOverdue = new GridBagConstraints();
		gbc_chckbxOnlyOverdue.anchor = GridBagConstraints.NORTHWEST;
		gbc_chckbxOnlyOverdue.insets = new Insets(5, 5, 5, 5);
		gbc_chckbxOnlyOverdue.gridx = 1;
		gbc_chckbxOnlyOverdue.gridy = 1;
		panelLoanNavigation.add(chckbxOnlyOverdue, gbc_chckbxOnlyOverdue);
		
		btnShowSelectedLoans = new JButton("<html><body style=\"margin:4 0 4 0\">Show selected loans</body></html>");
		btnShowSelectedLoans.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Iterate through selected books and open detail view for each
				//Check if a book detail is already opened
				
				int[] selectedRows = tblLoans.getSelectedRows();
				for(int i = 0; i < selectedRows.length; i++)
				{
					int modelIndex = tblLoans.convertRowIndexToModel(selectedRows[i]);
					Loan loan = loans.get(modelIndex);
					if (openLoans.containsKey(loan)){
						LoanDetailFrame detail = openLoans.get(loan);
						if(detail.isVisible() == false)
						{
							detail.setVisible(true);
						}
					}
					else
					{
						LoanDetailFrame newDetail = new LoanDetailFrame(library, loan);
						openLoans.put(loan, newDetail);
						newDetail.setVisible(true);
					}
				}
			}
		});
		GridBagConstraints gbc_btnShowSelectedLoans = new GridBagConstraints();
		gbc_btnShowSelectedLoans.insets = new Insets(0, 0, 5, 0);
		gbc_btnShowSelectedLoans.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnShowSelectedLoans.gridx = 2;
		gbc_btnShowSelectedLoans.gridy = 1;
		btnShowSelectedLoans.setEnabled(false);
		panelLoanNavigation.add(btnShowSelectedLoans, gbc_btnShowSelectedLoans);
		
		JButton btnAddNewLoan = new JButton("<html><body style=\"margin:4 0 4 0\">Add new loan</body></html>");
		GridBagConstraints gbc_btnAddNewLoan = new GridBagConstraints();
		gbc_btnAddNewLoan.insets = new Insets(0, 5, 5, 0);
		gbc_btnAddNewLoan.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnAddNewLoan.gridx = 3;
		gbc_btnAddNewLoan.gridy = 1;
		panelLoanNavigation.add(btnAddNewLoan, gbc_btnAddNewLoan);
	}
	
	private void initLoanStatistic()
	{
		JPanel panelLoanStatistic = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panelLoanStatistic.getLayout();
		flowLayout.setVgap(10);
		flowLayout.setHgap(10);
		flowLayout.setAlignment(FlowLayout.LEFT);
		panelLoanStatistic.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), "Loan statistic", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLACK));
		GridBagConstraints gbc_panelLoanStatistic = new GridBagConstraints();
		gbc_panelLoanStatistic.insets = new Insets(10, 5, 5, 5);
		gbc_panelLoanStatistic.fill = GridBagConstraints.BOTH;
		gbc_panelLoanStatistic.gridx = 0;
		gbc_panelLoanStatistic.gridy = 0;
		add(panelLoanStatistic, gbc_panelLoanStatistic);
		
		JLabel lblTextNbrLoans = new JLabel("Number of loans:");
		panelLoanStatistic.add(lblTextNbrLoans);
		
		JLabel lblNbrLoans = new JLabel(String.valueOf(library.getLoans().size()));
		panelLoanStatistic.add(lblNbrLoans);
		
		JLabel lblTextLoanOut = new JLabel("<html><body style=\"margin-left:20px\">Loan out:</body></html>");
		panelLoanStatistic.add(lblTextLoanOut);
		
		JLabel lblLoanOut = new JLabel(String.valueOf(library.getLentOutLoans().size()));
		panelLoanStatistic.add(lblLoanOut);
		
		JLabel lblTextLoansOverdue = new JLabel("<html><body style=\"margin-left:20px\">Loans overdue:</body></html>");
		panelLoanStatistic.add(lblTextLoansOverdue);
		
		JLabel lblLoansOverdue = new JLabel(String.valueOf(library.getOverdueLoans().size()));
		panelLoanStatistic.add(lblLoansOverdue);
	}

	private void initLoanTable() {
		JScrollPane scrollPane = new JScrollPane();
		panelLoans.add(scrollPane, BorderLayout.CENTER);
		
		tblLoans = new JTable();
		tblLoans.setShowVerticalLines(false);
		tblLoans.setAutoCreateRowSorter(true);
		tblLoans.setDragEnabled(false);
		tblLoans.setShowHorizontalLines(false);
		tblLoans.setShowGrid(false);
		tblLoans.setFillsViewportHeight(true);
		tblLoans.getTableHeader().setReorderingAllowed(false);
		tblLoans.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				
				if(tblLoans.getSelectedRowCount() > 0)
					btnShowSelectedLoans.setEnabled(true);
				else
					btnShowSelectedLoans.setEnabled(false);
			}
		});
		
		DefaultTableModel m = new DefaultTableModel(){
			boolean[] canEdit = new boolean[]{
	                false, false, false, false, false
	        };
			
			@Override
	        public boolean isCellEditable(int rowIndex, int columnIndex) {
	            return canEdit[columnIndex];
	        }
		};
		
		m.addColumn("Status");
		m.addColumn("Exemplar-ID");
		m.addColumn("Title");
		m.addColumn("Loan out date");
		m.addColumn("Loan out to");
		
		String status;
		int exemplarID;
		String title;
		String loanOutDate;
		String loanOutTo;
		for(Loan l : loans)
		{
			if(l.isLent())
			{
				//prepare table vars
				if(l.isOverdue())
					status = "overdue";
				else
					status = "ok";
				
				exemplarID = (int) l.getCopy().getInventoryNumber();
				title = l.getCopy().getTitle().getName();
				
				GregorianCalendar returnDate = (GregorianCalendar) l.getPickupDate().clone();
				returnDate.add(Calendar.DAY_OF_YEAR, 30);
				
				DateFormat f = SimpleDateFormat.getDateInstance();
				
				loanOutDate = f.format(returnDate.getTime());
				
				int i1 = l.getDaysOfLoanDuration();
				int i2 = l.getDaysOverdue();
				
				String te = String.valueOf(i1);
				String tee = String.valueOf(i2);
				if(l.isOverdue())
					loanOutDate+=" (since "+tee+" days)";
				else
					loanOutDate+=" (still "+te+" days)";
				
				loanOutTo = l.getCustomer().getSurname()+" "+l.getCustomer().getName();
				
				m.addRow(new Object[] {status, exemplarID, title, loanOutDate, loanOutTo});
			}
		}
		tblLoans.setModel(m);
		
		
		
		TableColumnModel tcm = tblLoans.getColumnModel();
		tcm.getColumn(0).setCellRenderer(new ColorRenderer());
		sorter = new TableRowSorter<DefaultTableModel>(m);
		
		tblLoans.setRowSorter(sorter);
		
		tblLoans.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		scrollPane.setViewportView(tblLoans);
	}
	
	private void newFilter()
	{
		RowFilter<Object, Object> rf = null;
		
	    try {
	    	if(chckbxOnlyOverdue.isSelected()){
	    		List<RowFilter<Object, Object>> rfs = new ArrayList<RowFilter<Object,Object>>();
	    		rfs.add(RowFilter.regexFilter(txtSearch.getText(), 0, 1, 2, 3, 4));
	    		rfs.add(RowFilter.regexFilter("overdue", 0));
	    		rf = RowFilter.andFilter(rfs);
	    	}
	    	else{
	    		rf = RowFilter.regexFilter(txtSearch.getText(), 0, 1, 2, 3, 4);
	    	}
	    	
	    } catch (java.util.regex.PatternSyntaxException e) {
	        return;
	    }

	    sorter.setRowFilter(rf);
	}
	
	public class ColorRenderer extends JLabel implements TableCellRenderer {

		public ColorRenderer() {
			setOpaque(true);
		}

		public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus, int row, int column) {
			if (isSelected)  
                setBackground( table.getSelectionBackground() );  
            else  
                setBackground( table.getBackground() );  
			
			if(value.equals("ok")){
				setIcon(new ImageIcon(getClass().getResource("/library.png")));
				setText("Ok");
			}
			else{
				setIcon(new ImageIcon(getClass().getResource("/book.png")));
				setText("Overdue");
			}
				
			return this;
		}
	}

}
