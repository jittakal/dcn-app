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
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import com.avaje.ebean.validation.NotNull;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "mobile_number", "home_number", "email_address" }))
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

	public static int countBySubArea(Long sub_areaid){
		return find.select("id").where().eq("sub_area_id",sub_areaid).findList().size();
	}

	public static int countByPrice(Long priceid){
		return find.select("id").where().eq("price_id",priceid).findList().size();
	}

}
