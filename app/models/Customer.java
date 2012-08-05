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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.Map;
import java.util.LinkedHashMap;

@Entity
@JsonSerialize(include = Inclusion.NON_NULL)
public class Customer extends Model {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	public Long id;
	
	@NotNull
	public String name;
	
	@ManyToOne
	@NotNull
	public Area area;

	@ManyToOne
	@NotNull
	public SubArea sub_area;

	@ManyToOne
	@NotNull
	public Node node;

	@ManyToOne
	@NotNull
	public Amply amply;
	
	@NotNull
	public String address;
	
	public String mobile_number;
	
	@NotNull
	public String id_number;
			
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
	public Integer deposite;
	
	@NotNull
	public Integer balance;
	
	
	public static Finder<Long, Customer> find = new Finder(Long.class, Customer.class);

	public static Customer get(Long id) {
		return find.byId(id);
	}

	public static List<Customer> all() {
		return find.all();
	}

	public static List<Customer> allActive() {
		return find.where().eq("terminate_date",null).findList();
	}

	public static List<Customer> allTerminated() {
		return find.where().ne("terminate_date",null).findList();
	}	

	public static void delete(Long id) {
		find.byId(id).delete();
	}

	public static boolean isBelongsToSubArea(Long sub_areaid){
		return find.select("id").where().eq("sub_area_id",sub_areaid).findList().size()==0?false:true;
	}

	public static boolean isBelongsToNode(Long nodeid){
		return find.select("id").where().eq("node_id",nodeid).findList().size()==0?false:true;
	}

	public static boolean isBelongsToAmply(Long amplyid){
		return find.select("id").where().eq("amply_id",amplyid).findList().size()==0?false:true;
	}

	public static boolean isBelongsToPrice(Long priceid){
		return find.select("id").where().eq("price_id",priceid).findList().size()==0?false:true;
	}

	public static boolean isCustomerIdExists(Long id,String customerId, Long areaId){
		return find.select("id").where().ne("id",id).eq("id_number",customerId).eq("area_id",areaId).findList().size()==0?false:true;
	}

	public static Map<String,String> asMapBySubAreaId(Long sub_area_id){
		List<Customer> customers=find.select("id,name").where().eq("terminate_date",null).eq("sub_area_id",sub_area_id).orderBy("name").findList();
		Map<String,String> customerMap=new LinkedHashMap<String,String>();
		for(Customer customer:customers){
			customerMap.put(customer.id.toString(),customer.name);
		}
		return customerMap;
	}

}
