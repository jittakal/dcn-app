package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import play.db.ebean.Model;

import com.avaje.ebean.validation.NotNull;

@Entity
public class Customer extends Model {
	
	@Id
	public Long id;
	
	@NotNull
	public String name;
	
	@ManyToOne
	@NotNull
	public SubArea sub_area;
	
	@NotNull
	public String address;
	
	public int mobile_number;
	
	public int home_number;
	
	public String email_address;
	
	@NotNull
	@Temporal(value=TemporalType.DATE)
	public Date joining_date;
	
	@Temporal(value=TemporalType.DATE)
	public Date terminate_date;
	
	@ManyToOne
	@NotNull
	public Price price;
	
	@NotNull
	public int balance;
	
	
	public static Finder<Long, Customer> find = new Finder(Long.class, Customer.class);

	public static Customer get(Long id) {
		return find.byId(id);
	}

	public static List<Customer> all() {
		return find.all();
	}

	public static void delete(Long id) {
		find.byId(id).delete();
	}

}
