package util;

import play.mvc.Result;
import play.mvc.Http.Context;
import controllers.Application;
import play.mvc.Security.Authenticator;

public class DcnAuthenticator extends Authenticator {
	
	@Override
	public Result onUnauthorized(Context context) {
		return Application.index();
	}

}
