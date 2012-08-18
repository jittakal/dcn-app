package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;
import play.db.ebean.Model;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import com.avaje.ebean.validation.NotNull;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import util.JsonDateSerializer;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Invoice extends Model {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	public Long id;
	
	@ManyToOne
	@NotNull
	public Customer customer;
	
	@NotNull
	public Integer month;
	
	@NotNull
	public Integer year;
	
	@NotNull
	public Integer amount;

	@NotNull
	public Boolean paid;

	public String getCustomerId(){
		return customer.id_number;
	}

	public String getCustomerName(){
		return customer.name;
	}

	public String getSubAreaName(){
		return customer.sub_area.name;
	}

	@NotNull
	@Temporal(TemporalType.DATE)
	@JsonSerialize(using=JsonDateSerializer.class)
	public Date invoice_date;
	
	public static Finder<Long, Invoice> find = new Finder(Long.class, Invoice.class);

	public static Invoice get(Long id) {
		return find.byId(id);
	}

	public static List<Invoice> all() {
		return find.all();
	}

	public static void delete(Long id) {
		find.byId(id).delete();
	}

	public static boolean isBelongsToCustomer(Long customerid){
		return find.select("id").where().eq("customer_id",customerid).findList().size()==0?false:true;
	}

	public static boolean isInvoiceExists(Long customerid, Integer month, Integer year){
		return find.select("id").where().eq("customer_id",customerid).eq("month",month).eq("year",year).findList().size()==0?false:true;	
	}
	
	public static List<Invoice> search(Integer month,Integer year){
		return find.where().eq("month",month).eq("year",year).findList();	
	}

	public static List<Invoice> search(Integer month,Integer year, Long areaId){
		List<Invoice> invoices=find.where().eq("month",month).eq("year",year).findList();	

		List<Invoice> filteredInvoices =
                find.filter() 
                        .eq("customer.area.id", areaId)                        
                        .filter(invoices);
        return filteredInvoices;
	}	

	public static List<Invoice> search(Integer month,Integer year, Long areaId, Long subAreaId){
		List<Invoice> invoices=find.where().eq("month",month).eq("year",year).findList();	

		List<Invoice> filteredInvoices =
                find.filter() 
                		.eq("customer.area.id", areaId)                        
                        .eq("customer.sub_area.id", subAreaId)                        
                        .filter(invoices);
        return filteredInvoices;
	}	

}
