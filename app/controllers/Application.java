package controllers;

import play.*;
import play.mvc.*;
import play.mvc.Security.Authenticated;

import views.html.*;

@Authenticated(value = AppAuthenticator.class)
public class Application extends Controller {

	public static Result index() {
		return ok(index.render("Your new application is ready."));
	}

}