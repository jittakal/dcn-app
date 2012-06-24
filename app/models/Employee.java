package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import util.JsonDateSerializer;
import java.util.Map;
import java.util.HashMap;
import javax.persistence.UniqueConstraint;
import play.db.ebean.Model;
import javax.persistence.Table;
import com.avaje.ebean.validation.NotNull;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
@JsonSerialize(include = Inclusion.NON_NULL)
public class Employee extends Model {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	public Long id;
	
	@NotNull
	public String name;
	
	@NotNull
	public String address;
		
	public String mobile_number;
	
	@NotNull
	@Temporal(TemporalType.DATE)
	@JsonSerialize(using=JsonDateSerializer.class)
	public Date joining_date;
	
	@Temporal(TemporalType.DATE)
	@JsonSerialize(using=JsonDateSerializer.class,include = Inclusion.NON_NULL)
	public Date terminate_date;
	
	/**
	 * @OneToMany(fetch=FetchType.LAZY)
	 * public List<SubArea> sub_areas;
	 * */
	
	public static Finder<Long, Employee> find = new Finder(Long.class, Employee.class);
	
	public static Employee get(Long id){
		return find.byId(id);
	}
	
	public static List<Employee> all() {
		return find.all();
	}		

	public static List<Employee> allActive() {
		return find.where().eq("terminate_date",null).findList();
	}

	public static List<Employee> allTerminated() {
		return find.where().ne("terminate_date",null).findList();
	}

	public static void delete(Long id) {
		find.byId(id).delete();
	}

	public static Map<String,String> asMap(){
		List<Employee> employees=find.select("id,name").where().eq("terminate_date",null).findList();
		Map<String,String> empMap=new HashMap<String,String>();
		for(Employee employee: employees){
			empMap.put(employee.id.toString(),employee.name);
		}
		return empMap;
	}	

}
