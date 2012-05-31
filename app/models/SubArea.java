package models;

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
}
