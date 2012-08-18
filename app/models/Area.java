package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Map;
import java.util.LinkedHashMap;
import play.db.ebean.Model;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.UniqueConstraint;
import com.avaje.ebean.validation.NotNull;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "name" }))
public class Area extends Model {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	public Long id;

	@NotNull
	public String name;

	@ManyToOne
	@NotNull
	public Employee employee;

	public static Finder<Long, Area> find = new Finder(Long.class, Area.class);
	
	public static Area get(Long id){
		return find.byId(id);
	}	
	
	public static List<Area> all() {
		return find.all();
	}		

	public static void delete(Long id) {
		find.byId(id).delete();
	}

	public static Map<String,String> asMap(){
		List<Area> areas=find.select("id,name").orderBy("name").findList();
		Map<String,String> areaMap=new LinkedHashMap<String,String>();
		for(Area area: areas){
			areaMap.put(area.id.toString(),area.name);
		}
		return areaMap;
	}

	public static boolean isBelongsToEmployee(Long employeeid){
		return find.select("id").where().eq("employee_id",employeeid).findList().size()==0?false:true;
	}

	public static boolean isNameExists(Long id,String name){
		if(id==0){
			return find.select("id").where().eq("name",name).findList().size()==0?false:true;			
		}
		return find.select("id").where().ne("id",id).eq("name",name).findList().size()==0?false:true;		
	}
	
}
