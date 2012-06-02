package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;

import com.avaje.ebean.validation.NotNull;

@Entity
public class SubArea extends Model {
	
	@Id
	public long id;
	
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
}
