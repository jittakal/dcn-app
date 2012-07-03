package services;

import models.Customer;
import models.Invoice;
import java.util.Date;
import java.util.Calendar;
import java.util.List;

public class InvoiceService{

	public static void generateInvoice(Integer month, Integer year){
		// Get List of Active Customers
		List<Customer> activeCustomers=Customer.allActive();

		// For Each Customer
		for(Customer customer: activeCustomers){
			// Check if customers joining date is before 15 of current month and year
			Calendar rightNow=Calendar.getInstance();
			Date curDate=rightNow.getTime();
			rightNow.set(year,month,16);
			Date cutOffDate=rightNow.getTime();

			if(customer.joining_date.before(cutOffDate) && !Invoice.isInvoiceExists(customer.id,month,year)){
				Invoice invoice=new Invoice();
				invoice.customer=customer;
				invoice.month=month;
				invoice.year=year;
				invoice.invoice_date=curDate;
				invoice.amount=customer.price.amount;
				invoice.paid=false;
				invoice.save();

				customer.balance = customer.balance + invoice.amount;
				customer.update();  
			}

		}		
		// check if invoice entry is already exixts for the customer
		// If so do not duplicate the entry again, otherwise generate invoice entry for that customer

	}
	
}