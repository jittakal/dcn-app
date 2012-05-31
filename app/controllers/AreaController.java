package controllers;

import models.Area;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import play.mvc.BodyParser;
import play.libs.Json;
import org.codehaus.jackson.*;

import views.html.area.*;

public class AreaController extends Controller {
	
	//@Authenticated
	public static Result index(){
		return ok(index.render(Area.all()));
	}


	@BodyParser.Of(json.class)
	public static Result get_area(){
		ObjectNode result = Json.newObject();
		Area area=new Area();
		area.id="1";
		area.name="Patali pada";
		result.put("area",area);
		return ok(result);
	}

}
