package controllers;

import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import vos.UserVO;

public class LoginController extends Controller {
	
	final static Form<UserVO> loginForm = form(UserVO.class);
	
	public static Result index() {
		return ok(views.html.login.index.render(loginForm));
	}
	
	public static Result submit(){
		Form<UserVO> filledForm = loginForm.bindFromRequest();

		if(filledForm.hasErrors()){
			return badRequest(views.html.login.index.render(filledForm));
		}
					
		UserVO uservo=filledForm.get();
		if(uservo==null){			
			return badRequest(views.html.login.index.render(filledForm));
		}
		
		User auser=uservo.getUser();//User.authenticate(uservo.username,uservo.password);
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
