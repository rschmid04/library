package domain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class Library {

	private List<Copy> copies;
	private List<Customer> customers;
	private List<Loan> loans;
	private List<Book> books;

	public Library() {
		copies = new ArrayList<Copy>();
		customers = new ArrayList<Customer>();
		loans = new ArrayList<Loan>();
		books = new ArrayList<Book>();
	}

	public Loan createAndAddLoan(Customer customer, Copy copy) {
		if (!isCopyLent(copy)) {
			Loan l = new Loan(customer, copy);
			loans.add(l);
			return l;
		} else {
			return null;
		}
	}

	public Customer createAndAddCustomer(String name, String surname) {
		Customer c = new Customer(name, surname);
		customers.add(c);
		return c;
	}

	public Book createAndAddBook(String name) {
		Book b = new Book(name);
		books.add(b);
		return b;
	}

	public Copy createAndAddCopy(Book title) {
		Copy c = new Copy(title);
		copies.add(c);
		return c;
	}

	public Book findByBookTitle(String title) {
		for (Book b : books) {
			if (b.getName().equals(title)) {
				return b;
			}
		}
		return null;
	}
	
	public Loan getLoanOfCopy(Copy copy)
	{
		for (Loan l : loans) {
			if (l.getCopy().equals(copy) && l.isLent()) {
				return l;
			}
		}
		
		return null;
	}

	public boolean isCopyLent(Copy copy) {
		for (Loan l : loans) {
			if (l.getCopy().equals(copy) && l.isLent()) {
				return true;
			}
		}
		return false;
	}

	public List<Copy> getCopiesOfBook(Book book) {
		List<Copy> res = new ArrayList<Copy>();
		for (Copy c : copies) {
			if (c.getTitle().equals(book)) {
				res.add(c);
			}
		}

		return res;
	}

	public List<Loan> getLentCopiesOfBook(Book book) {
		List<Loan> lentCopies = new ArrayList<Loan>();
		for (Loan l : loans) {
			if (l.getCopy().getTitle().equals(book) && l.isLent()) {
				lentCopies.add(l);
			}
		}
		return lentCopies;
	}

	public List<Loan> getCustomerLoans(Customer customer) {
		List<Loan> lentCopies = new ArrayList<Loan>();
		for (Loan l : loans) {
			if (l.getCustomer().equals(customer)) {
				lentCopies.add(l);
			}
		}
		return lentCopies;
	}
	
	public String getAvailabilityOfBook(Book b)
	{
		List<Copy> copies = this.getCopiesOfBook(b);
		List<Loan> loans = this.getLentCopiesOfBook(b);
		if(copies.size() == loans.size()){
			//all copies are lent out, return nearest date
			GregorianCalendar nearestDate = null;
			for(Loan l : loans){
				GregorianCalendar estDate = (GregorianCalendar) l.getPickupDate().clone();
				estDate.add(GregorianCalendar.DAY_OF_YEAR, 30);
				if(nearestDate == null){
					nearestDate = (GregorianCalendar) estDate.clone();
				}
				else{
					if(nearestDate.after(estDate)){
						nearestDate = (GregorianCalendar) estDate.clone();
					}
				}
			}
			DateFormat f = SimpleDateFormat.getDateInstance();
			
			String returnString = f.format(nearestDate.getTime());
			return returnString;
		}
		else{
			
			return String.valueOf(copies.size() - loans.size());
		}
		
	}
	
	public List<Loan> getOverdueLoans() {
		List<Loan> overdueLoans = new ArrayList<Loan>();
		for ( Loan l : getLoans() ) {
			if (l.isOverdue())
				overdueLoans.add(l);
		}
		return overdueLoans;
	}
	
	public List<Copy> getAvailableCopies(){
		return getCopies(false);
	}
	
	public List<Loan> getLentOutLoans(){
		List<Loan> retLoans = new ArrayList<Loan>();
		for (Loan l : loans) {
			if (l.isLent()) {
				retLoans.add(l);
			}
		}
		return retLoans;
	}
	
	public List<Copy> getLentOutBooks(){
		return getCopies(true);
	}

	private List<Copy> getCopies(boolean isLent) {
		List<Copy> retCopies = new ArrayList<Copy>();
		for (Copy c : copies) {
			if (isLent == isCopyLent(c)) {
				retCopies.add(c);
			}
		}
		return retCopies;
	}

	public List<Copy> getCopies() {
		return copies;
	}

	public List<Loan> getLoans() {
		return loans;
	}

	public List<Book> getBooks() {
		return books;
	}

	public List<Customer> getCustomers() {
		return customers;
	}

}
