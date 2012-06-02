package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

import com.avaje.ebean.validation.NotNull;

@Entity
public class Price extends Model {
	
	@Id
	public Long id;
	
	@NotNull
	public int amount;
	
	/*@Temporal(value=TemporalType.DATE)
	@NotNull
	public Date start_date;
	
	@Temporal(value=TemporalType.DATE)
	public Date end_date;*/
	
	public static Finder<Long, Price> find = new Finder(Long.class, Price.class);

	public static Price get(Long id) {
		return find.byId(id);
	}

	public static List<Price> all() {
		return find.all();
	}

	public static void delete(Long id) {
		find.byId(id).delete();
	}

}
