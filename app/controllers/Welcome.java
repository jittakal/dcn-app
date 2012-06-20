package controllers;

import play.*;
import play.mvc.*;
import play.mvc.Security.Authenticated;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.welcome.*;
import util.DcnAuthenticator;

@Authenticated(value = DcnAuthenticator.class)
public class Welcome extends Controller {
	
	public static Result index() {
		return ok(index.render());
	}

}