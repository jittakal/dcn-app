package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import play.db.ebean.Model;

import com.avaje.ebean.validation.NotNull;

@Entity
public class Customer extends Model {
	
	@Id
	public long id;
	
	@NotNull
	public String name;
	
	@ManyToOne
	@NotNull
	public SubArea subArea;
	
	@NotNull
	public String address;
	
	public int mobileNumber;
	
	public int homeNumber;
	
	public String emailAddress;
	
	@NotNull
	@Temporal(value=TemporalType.DATE)
	public Date joiningDate;
	
	@Temporal(value=TemporalType.DATE)
	public Date terminateDate;

}
