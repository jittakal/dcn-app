package controllers;

import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import forms.UserForm;

public class LoginController extends Controller {
	
	final static Form<UserForm> loginForm = form(UserForm.class);
	
	public static Result index() {
		return ok(views.html.login.index.render(loginForm));
	}
	
	public static Result submit(){
		Form<UserForm> filledForm = loginForm.bindFromRequest();

		if(filledForm.hasErrors()){
			return badRequest(views.html.login.index.render(filledForm));
		}
					
		UserForm userForm=filledForm.get();
		if(userForm==null){			
			return badRequest(views.html.login.index.render(filledForm));
		}
		
		User auser=userForm.getUser();
		if(auser==null){
			return badRequest(views.html.login.index.render(filledForm));
		}
		
		session().put("username", auser.username);
		session().put("role", auser.role);
		return  ok(views.html.welcome.render());		
	}

	public static Result welcome(){
		session().put("username", "jittakal");
		session().put("role", "admin");
		return ok(views.html.welcome.render());
	}

}
