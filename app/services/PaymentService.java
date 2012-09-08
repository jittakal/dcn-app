package services;

import models.*;
import java.util.*;
import sos.CollectionReportSO;
import sos.DailyCollectionReportSO;
import com.avaje.ebean.*;

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

	public static Map dailyCollectionReport(Integer month, Integer year){
		Map dailyCollectionReportMap=new LinkedHashMap();
		List<Area> areas=Area.all();
		for(Area area:areas){
			dailyCollectionReportMap.put(area.id.toString(),new DailyCollectionReportSO(area.name));
		}

		String sql="select a.id as aid,a.name as name, extract(day from p.payment_date) as day,sum(p.amount) as total "
		+ " from payment p, invoice i, customer c, area a "
		+ " where p.invoice_id=i.id and i.customer_id=c.id and c.area_id=a.id "
		+ " and extract(year from payment_date)= :year " 
		+ " and extract(month from payment_date)= :month "
		+ " group by a.id,a.name,day order by day,a.id";

		SqlQuery sqlQuery = Ebean.createSqlQuery(sql);
 		sqlQuery.setParameter("year", year);
 		sqlQuery.setParameter("month", month);

		List<SqlRow> sqlRows=sqlQuery.findList();		
		Set<Integer> days=new TreeSet<Integer>();
		for(SqlRow sqlRow: sqlRows){
			String aid=sqlRow.getInteger("aid").toString();
			days.add(sqlRow.getInteger("day"));
			((DailyCollectionReportSO)dailyCollectionReportMap.get(aid)).addDayTotal(sqlRow.getInteger("day"),
					sqlRow.getInteger("total"));						
		}
		dailyCollectionReportMap.put("days",days);

		return dailyCollectionReportMap;
	}

}