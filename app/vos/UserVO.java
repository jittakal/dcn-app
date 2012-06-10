package vos;

import java.io.Serializable;

import models.User;

import play.data.validation.Constraints.Required;

public class UserVO implements Serializable {
	
	@Required
	public String username;
	
	@Required
	public String password;

	private User user;
	
	public String validate() {
        User auser=User.authenticate(username,password);
		if(auser==null){
			return "Invalid username or password, Please try again";
		}

		user=auser;
        return null;
    }

    public User getUser(){
    	return user;
    }

}
