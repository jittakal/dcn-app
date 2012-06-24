package controllers;

import java.util.List;

import models.Price;
import models.Customer;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;
import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import forms.PriceForm;
import views.html.price.*;
import play.mvc.Security.Authenticated;
import util.DcnAuthenticator;

@Authenticated(value = DcnAuthenticator.class)
public class PriceController extends Controller {

	final static Form<PriceForm> priceForm =  form (PriceForm.class);
	
	/**
	 * Return Price by id.
	 * @return 
	 * 
	 * @return
	 */
	
	public static Result index(){
		return ok(index.render());
	}
	
	public static Result create_new(){
		return ok(create.render(priceForm));
	}
	
	public static Result create_save(){
		Form<PriceForm> filledForm = priceForm.bindFromRequest();
			
		if(filledForm.hasErrors()){
			return badRequest(create.render(filledForm));
		}
		
		PriceForm priceForm=filledForm.get();
		if(priceForm==null){
			return badRequest(create.render(filledForm));
		}

		if(Price.isAmountExists(0L,priceForm.amount)){
			filledForm.reject("Price amount already exists.");
			return badRequest(create.render(filledForm));	
		}
		
		Price price=new Price();
		price.amount=priceForm.amount;
		price.save();
		
		return  redirect(controllers.routes.PriceController.index());
	}

	public static Result update_get(Long id){
		Price price=Price.get(id);		
		PriceForm pForm=new PriceForm();
		pForm.amount=price.amount;
		return ok(update.render(priceForm.fill(pForm),id));
	}

	public static Result update_save(Long id){
		Form<PriceForm> filledForm = priceForm.bindFromRequest();

		if(filledForm.hasErrors()){
			return badRequest(update.render(filledForm,id));
		}
					
		PriceForm priceForm=filledForm.get();
		if(priceForm==null){			
			return badRequest(update.render(filledForm,id));
		}

		if(Price.isAmountExists(id,priceForm.amount)){
			filledForm.reject("Price amount already exists.");
			return badRequest(update.render(filledForm,id));	
		}

		Price price=new Price();
		price.id=id;
		price.amount=priceForm.amount;
		price.update();
								
		return  redirect(controllers.routes.PriceController.index());
	}

	public static Result delete(Long id){		
		Price price=Price.get(id);
		if(price==null){
			return notFound("Price having id [" + id + "] does not exists.");
		}
		if(!Customer.isBelongsToPrice(id)){
			Price.delete(id);			
			return ok("Selected Price has been deleted successfully");
		}
		return notFound("Price ['" + price.amount.toString() + "'] is associated with Customer(s)");			
	}

	/**
	 * Return List of all Prices in JSON format.
	 * 
	 * @return
	 */
	public static Result all() {
		List<Price> prices = Price.all();
		return ok(Json.toJson(prices));
	}

	/**
	 * Return Price by id.
	 * 
	 * @return
	 */
	public static Result get(Long id) {
		if (id == null) {
			return badRequest("Expecting Price Id");
		}

		Price price = Price.get(id);

		if (price == null) {
			return notFound("Price with id [" + id + "] does not exists.");
		}
		JsonNode result = Json.toJson(price);
		return ok(result);
	}

	/**
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public static Result create() {
		JsonNode jsonNode = request().body().asJson();
		if (jsonNode == null) {
			return badRequest("Expecting Json data");
		}
		Price price = Json.fromJson(jsonNode, Price.class);
		if (price == null) {
			return badRequest("Expecting Price Json data");
		}
		price.save();
		ObjectNode result = Json.newObject();
		result.put("id", price.id);
		return created(result);
	}

	/**
	 * @param id
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public static Result update(Long id) {
		if (id == null) {
			return badRequest("Expecting Price Id");
		}

		JsonNode jsonNode = request().body().asJson();
		if (jsonNode == null) {
			return badRequest("Expecting Json data");
		}
		Price price = Json.fromJson(jsonNode, Price.class);
		if (price == null) {
			return badRequest("Expecting Price Json data");
		}
		price.id = id;
		price.update();
		return ok();
	}

}
