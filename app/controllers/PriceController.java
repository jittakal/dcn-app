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
import play.i18n.Messages;

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
			filledForm.reject(Messages.get("amount.already.exists"));
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
			filledForm.reject(Messages.get("amount.already.exists"));
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
			return notFound(Messages.get("id.not.exists",id));
		}
		if(!Customer.isBelongsToPrice(id)){
			Price.delete(id);			
			return ok(Messages.get("record.deleted.succ"));
		}
		return notFound(Messages.get("price.assoc.customer",price.amount.toString()));			
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
			return badRequest(Messages.get("expecting.id"));
		}

		Price price = Price.get(id);

		if (price == null) {
			return notFound(Messages.get("id.not.exists",id));
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
			return badRequest(Messages.get("expecting.json"));
		}
		Price price = Json.fromJson(jsonNode, Price.class);
		if (price == null) {
			return badRequest(Messages.get("expecting.json"));
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
			return badRequest(Messages.get("expecting.id"));
		}

		JsonNode jsonNode = request().body().asJson();
		if (jsonNode == null) {
			return badRequest(Messages.get("expecting.json"));
		}
		Price price = Json.fromJson(jsonNode, Price.class);
		if (price == null) {
			return badRequest(Messages.get("expecting.json"));
		}
		price.id = id;
		price.update();
		return ok();
	}

}
