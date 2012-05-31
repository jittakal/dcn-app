package controllers;

import models.User;
import play.data.Form;
import play.mvc.Call;
import play.mvc.Controller;
import play.mvc.Result;

public class LoginController extends Controller {
	
	final static Form<User> loginForm = form(User.class);
	
	public static Result index() {
		return ok(views.html.login.index.render("Darpan Cable Network",loginForm));
	}
	
	public static Result submit(){
		Form<User> filledForm = loginForm.bindFromRequest();
		
		User user=filledForm.get();
		System.out.println(user.username);
		return  ok(views.html.index.render("Jitendra Takalkar"));		
	}

}
