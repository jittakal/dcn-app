package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import play.db.ebean.Model;

import com.avaje.ebean.validation.NotNull;

@Entity
public class Employee extends Model {
	@Id
	public Long id;
	
	@NotNull
	public String name;
	
	@NotNull
	public String address;
		
	public String mobile_number;
	
	@NotNull
	@Temporal(TemporalType.DATE)
	public Date joining_date;
	
	@Temporal(TemporalType.DATE)
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

	public static void delete(Long id) {
		find.byId(id).delete();
	}

}
