package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import com.avaje.ebean.validation.NotNull;

@Entity
public class Invoice extends Model {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	public Long id;
	
	@ManyToOne
	@NotNull
	public Customer customer;
	
	@NotNull
	public int month;
	
	@NotNull
	public int year;
	
	@NotNull
	public int amount;
	
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

}
