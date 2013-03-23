package controllers;

import play.*;
import play.mvc.*;
import play.mvc.Security.Authenticated;
import models.User;
import play.data.*;
import static play.data.Form.*;
import play.mvc.Controller;
import play.mvc.Result;
import forms.UserForm;
import play.mvc.Security.Authenticated;
import util.DcnAuthenticator;
import views.html.*;


public class Application extends Controller {

	final static Form<UserForm> loginForm = form(UserForm.class);
	
	public static Result index() {
		return ok(index.render(loginForm));
	}
	
	public static Result login(){
		Form<UserForm> filledForm = loginForm.bindFromRequest();

		if(filledForm.hasErrors()){
			return badRequest(index.render(filledForm));
		}
					
		UserForm userForm=filledForm.get();
		if(userForm==null){			
			return badRequest(index.render(filledForm));
		}
		
		User auser=userForm.getUser();
		if(auser==null){
			return badRequest(index.render(filledForm));
		}
		
		session().put("username", auser.username);
		session().put("role", auser.role);
		return  redirect(controllers.routes.Welcome.index());
	}		

	@Authenticated(value = DcnAuthenticator.class)
	public static Result logout(){
		session().clear();
		return  redirect(controllers.routes.Application.index());
	}

}