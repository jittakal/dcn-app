package services;

import models.*;
import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import sos.CollectionReportSO;

public class PaymentService {

	public static List<CollectionReportSO> collectionReport(Long areaId, Integer year){
		// Get List of Active Customers by Area
		List<Customer> customers=Customer.allActiveByArea(areaId);

		List<CollectionReportSO> collectionReports=new ArrayList<CollectionReportSO>();

		// For each customer get the payment details.
		List<Payment> payments;
		CollectionReportSO collectionReport;
		for(Customer customer: customers){
			collectionReport=new CollectionReportSO();
			collectionReport.id_number=customer.id_number;
			collectionReport.area_name=customer.area.name;
			collectionReport.sub_area_name=customer.sub_area.name;
			collectionReport.customer_name=customer.name;

			payments=Payment.search(customer.id,year);
			for(Payment payment:payments){
				collectionReport.month_collection.put(payment.invoice.month,payment.amount);
			}

			collectionReports.add(collectionReport);			
		}

		return collectionReports;			 
	}

}