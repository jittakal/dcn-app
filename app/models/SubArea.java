package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Map;
import java.util.HashMap;
import play.db.ebean.Model;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import com.avaje.ebean.validation.NotNull;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "name" }))
public class SubArea extends Model {
	
	@Id
	public Long id;
	
	@NotNull
	public String name;
	
	@ManyToOne
	@NotNull
	public Area area;
	
	@ManyToOne
	@NotNull
	public Employee employee;
	
	public static Finder<Long, SubArea> find = new Finder(Long.class, SubArea.class);
	
	public static SubArea get(Long id){
		return find.byId(id);
	}
	
	public static List<SubArea> all() {
		return find.all();
	}		

	public static void delete(Long id) {
		find.byId(id).delete();
	}

	public static Map<String,String> asMap(){
		List<SubArea> subareas=find.select("id,name").findList();
		Map<String,String> subareaMap=new HashMap<String,String>();
		for(SubArea subarea: subareas){
			subareaMap.put(subarea.id.toString(),subarea.name);
		}
		return subareaMap;
	}

	public static int countByArea(Long areaid){
		return find.select("id").where().eq("area_id",areaid).findList().size();
	}

	public static int countByEmployee(Long employeeid){
		return find.select("id").where().eq("employee_id",employeeid).findList().size();
	}
}
