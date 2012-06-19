																											package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import util.JsonDateSerializer;
import play.db.ebean.Model;

import com.avaje.ebean.validation.NotNull;

@Entity
@JsonSerialize(include = Inclusion.NON_NULL)
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
	
	public String mobile_number;
	
	public String home_number;
	
	public String email_address;
	
	@NotNull
	@Temporal(value=TemporalType.DATE)
	@JsonSerialize(using=JsonDateSerializer.class)
	public Date joining_date;
	
	@Temporal(value=TemporalType.DATE)
	@JsonSerialize(using=JsonDateSerializer.class,include = Inclusion.NON_NULL)
	public Date terminate_date;
	
	@ManyToOne
	@NotNull
	public Price price;
	
	@NotNull
	public Integer balance;
	
	
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
