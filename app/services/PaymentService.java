package services;

import models.*;
import java.util.*;
import sos.*;
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
			dailyCollectionReportMap.put(area.id.toString(),
				new DailyCollectionReportSO(area.name,getAreaInvoiceAmount(month,year,area.id)));
		}

		String sql="select a.id as aid,a.name as name, extract(day from p.payment_date) as day,sum(p.amount) as total, sum(i.amount) as itotal "
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

	public static Integer getAreaInvoiceAmount(Integer month, Integer year, Long area){

		String sql="select sum(i.amount) as itotal "
		+ " from invoice i, customer c, area a "
		+ " where i.customer_id=c.id and c.area_id=a.id "
		+ " and i.year = :year " 
		+ " and i.month = :month "
		+ " and a.id = :area ";

		SqlQuery sqlQuery = Ebean.createSqlQuery(sql);
 		sqlQuery.setParameter("year", year);
 		sqlQuery.setParameter("month", month);
 		sqlQuery.setParameter("area", area);

 		SqlRow sqlRow=sqlQuery.findUnique();		

 		if(sqlRow==null)
 			return 0;

 		return sqlRow.getInteger("itotal");
	}

	public static Integer getSubAreaInvoiceAmount(Integer month, Integer year, Long subarea){

		String sql="select sum(i.amount) as itotal "
		+ " from invoice i, customer c, sub_area s "
		+ " where i.customer_id=c.id and c.sub_area_id=s.id "
		+ " and i.year = :year " 
		+ " and i.month = :month "
		+ " and s.id = :subarea ";

		SqlQuery sqlQuery = Ebean.createSqlQuery(sql);
 		sqlQuery.setParameter("year", year);
 		sqlQuery.setParameter("month", month);
 		sqlQuery.setParameter("subarea", subarea);

 		SqlRow sqlRow=sqlQuery.findUnique();		

 		if(sqlRow==null)
 			return 0;

 		return sqlRow.getInteger("itotal");
	}

	public static Map dailyCollectionReport(Integer month, Integer year, Long area){
		Map dailyCollectionReportMap=new LinkedHashMap();
		List<SubArea> subareas=SubArea.all();
		for(SubArea subarea:subareas){
			dailyCollectionReportMap.put(subarea.id.toString(),
				new DailyCollectionReportSO(subarea.name,getSubAreaInvoiceAmount(month,year,subarea.id)));
		}

		String sql="select s.id as sid,s.name as name, extract(day from p.payment_date) as day,sum(p.amount) as total "
		+ " from payment p, invoice i, customer c, area a, sub_area s "
		+ " where p.invoice_id=i.id and i.customer_id=c.id and c.area_id=a.id and c.sub_area_id=s.id"
		+ " and extract(year from payment_date)= :year " 
		+ " and extract(month from payment_date)= :month "
		+ " and a.id= :area"
		+ " group by s.id,s.name,day order by day,s.id";
		

		SqlQuery sqlQuery = Ebean.createSqlQuery(sql);
 		sqlQuery.setParameter("year", year);
 		sqlQuery.setParameter("month", month);
 		sqlQuery.setParameter("area", area);

		List<SqlRow> sqlRows=sqlQuery.findList();		
		Set<Integer> days=new TreeSet<Integer>();
		for(SqlRow sqlRow: sqlRows){
			String sid=sqlRow.getInteger("sid").toString();
			days.add(sqlRow.getInteger("day"));
			((DailyCollectionReportSO)dailyCollectionReportMap.get(sid)).addDayTotal(sqlRow.getInteger("day"),
					sqlRow.getInteger("total"));						
		}
		dailyCollectionReportMap.put("days",days);

		return dailyCollectionReportMap;
	}

	public static Map<String,InvoiceReportSO> invoiceReport(Integer year){
		Map<String,InvoiceReportSO> invoiceReportMap=new LinkedHashMap<String,InvoiceReportSO>();
		List<Area> areas=Area.all();
		for(Area area:areas){			
			invoiceReportMap.put(area.name,new InvoiceReportSO());
		}
		
		String invoiceSql="select a.name as name,i.month,sum(i.amount) as itotal " 
		+ " from invoice i, customer c, area a "
		+ " where i.year= :year "
		+ " and i.customer_id=c.id "
		+ " and c.area_id=a.id "
		+ " group by i.month, a.name "
		+ " order by a.id,i.month";

		SqlQuery invoiceSqlQuery = Ebean.createSqlQuery(invoiceSql);
 		invoiceSqlQuery.setParameter("year", year); 		
		List<SqlRow> isqlRows=invoiceSqlQuery.findList();		
		
		for(SqlRow sqlRow: isqlRows){			
			invoiceReportMap.get(sqlRow.getString("name")).addInvoice(sqlRow.getInteger("month"),
				sqlRow.getInteger("itotal"));						
		}

		String paymentSql="select a.name as name,i.month,sum(i.amount) as ptotal "
		+ " from invoice i, customer c, area a "
		+ " where i.year= :year "
		+ " and paid=true "
		+ " and i.customer_id=c.id "
		+ " and c.area_id=a.id "
		+ " group by i.month,a.name "
		+ " order by i.month,a.id";

		SqlQuery paymentSqlQuery = Ebean.createSqlQuery(paymentSql);
 		paymentSqlQuery.setParameter("year", year); 		
		List<SqlRow> psqlRows=paymentSqlQuery.findList();		
		
		for(SqlRow sqlRow: psqlRows){			
			invoiceReportMap.get(sqlRow.getString("name")).addInvoice(sqlRow.getInteger("month"),
				sqlRow.getInteger("ptotal"));						
		}		

		return invoiceReportMap;
	}

}