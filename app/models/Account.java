package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import play.db.ebean.Model;

import com.avaje.ebean.validation.NotNull;

@Entity
public class Account extends Model {

	@Id
	public long id;
	
	@OneToOne
	@NotNull
	public Customer customer;
	
	@ManyToOne
	@NotNull
	public Price price;
}
