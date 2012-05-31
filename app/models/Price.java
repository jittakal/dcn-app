package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import play.db.ebean.Model;

import com.avaje.ebean.validation.NotNull;

@Entity
public class Price extends Model {
	
	@Id
	public long id;
	
	@NotNull
	public int amount;
	
	@Temporal(value=TemporalType.DATE)
	public Date startDate;
	
	@Temporal(value=TemporalType.DATE)
	public Date endDate;		

}
