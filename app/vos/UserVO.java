package vos;

import java.io.Serializable;

import play.data.validation.Constraints.Required;

public class UserVO implements Serializable {
	
	@Required
	public String username;
	
	@Required
	public String password;

}
