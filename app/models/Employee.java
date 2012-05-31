package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import play.db.ebean.Model;

import com.avaje.ebean.validation.NotNull;

@Entity
public class Employee extends Model {
	@Id
	public long id;
	
	@NotNull
	public String name;
	
	@NotNull
	public String address;
		
	public int mobileNumber;
	
	@NotNull
	@Temporal(TemporalType.DATE)
	public Date joiningDate;
	
	@Temporal(TemporalType.DATE)
	public Date terminateDate;
	
	@OneToMany(fetch=FetchType.LAZY)
	public List<SubArea> subAreas;

}
