package controllers;

import play.mvc.Result;
import play.mvc.Http.Context;
import play.mvc.Security.Authenticator;

public class AppAuthenticator extends Authenticator {
	
	@Override
	public Result onUnauthorized(Context arg0) {
		return LoginController.index();//ok(views.html.login.index.render("Darpan Cable Networl"));
	}

}
