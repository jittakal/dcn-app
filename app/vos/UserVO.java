package vos;

import java.io.Serializable;

import play.data.validation.Constraints.Required;

public class UserVO implements Serializable {
	
	@Required
	public String username;
	
	@Required
	public String password;
	
	public String validate() {
        if(username ==null){
            return "Invalid username";	
        }
        if(password ==null){
            return "Invalid password";
        }
        return null;
    }

}
