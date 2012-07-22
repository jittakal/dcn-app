package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Map;
import java.util.LinkedHashMap;
import play.db.ebean.Model;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import com.avaje.ebean.validation.NotNull;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "name" }))
public class SubArea extends Model {
	
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
		return asMap(subareas);
	}

	public static Map<String,String> asMapByAreaId(String areaid){
		List<SubArea> subareas=find.select("id,name").where().eq("area_id",areaid).orderBy("name").findList();
		return asMap(subareas);
	}

	public static Map<String,String> asMap(List<SubArea> subareas){
		Map<String,String> subareaMap=new LinkedHashMap<String,String>();
		for(SubArea subarea: subareas){
			subareaMap.put(subarea.id.toString(),subarea.name);
		}
		return subareaMap;
	}

	public static boolean isBelongsToArea(Long areaid){
		return find.select("id").where().eq("area_id",areaid).findList().size()==0?false:true;
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
