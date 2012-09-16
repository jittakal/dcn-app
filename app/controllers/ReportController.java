package controllers;

import java.util.*;
import models.*;
import services.*;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

//import views.html.report.*;

public class ReportController extends Controller {

	public static Result customer_counts(){		
		return ok(Json.toJson(ReportService.customersByArea()));
	}

}