package models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import play.data.validation.Constraints.Required;

@Entity
public class User implements Serializable {
	
	@Id
	public Long id;
	
	@Required
	public String username;
	
	@Required
	public String password;
	
	@Required
	public String role; //Admin,Employee,Operator
	
	@OneToOne
	@Required
	public Employee employee;

}
