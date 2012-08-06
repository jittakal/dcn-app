package controllers;

import java.util.List;

import models.Customer;
import models.Invoice;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.data.Form;
import forms.CustomerForm;
import models.*;
import models.Price;
import java.util.Map;

import views.html.customer.*;
import play.mvc.Security.Authenticated;
import util.DcnAuthenticator;

@Authenticated(value = DcnAuthenticator.class)
public class CustomerController extends Controller {

	final static Form<CustomerForm> customerForm = form(CustomerForm.class);

	public static Result index(){
		return ok(index.render());
	}

	public static Result create_new(){		
		return ok(create.render(customerForm));
	}

	public static Result create_save(){
		Form<CustomerForm> filledForm = customerForm.bindFromRequest();

		if(filledForm.hasErrors()){			
			return badRequest(create.render(filledForm));
		}
					
		CustomerForm customerForm=filledForm.get();
		if(customerForm==null){						
			return badRequest(create.render(filledForm));
		}

		if(Customer.isCustomerIdExists(0L,customerForm.id_number,new Long(customerForm.areaid))){
			filledForm.reject("Customer Id already exists.");
			return badRequest(create.render(filledForm));
		}
		if(Customer.isMobileNumberExists(0L,customerForm.mobile_number)){
			filledForm.reject("Customer mobile number already exists.");
			return badRequest(create.render(filledForm));
		}

		Customer customer=new Customer();
		formToModel(customer,customerForm);
		customer.balance=0;
		customer.save();
								
		return  redirect(controllers.routes.CustomerController.index());
	}

	public static Result on_c_area_change(){
		Form<CustomerForm> filledForm = customerForm.bindFromRequest();				

		if(filledForm.hasErrors()){			
			return badRequest(create.render(filledForm));
		}

		CustomerForm csForm=filledForm.get();
		if(csForm==null){
			return badRequest(create.render(filledForm));
		}
				
		return ok(create.render(customerForm.fill(csForm)));	
	}

	public static Result on_u_area_change(Long id){
		Form<CustomerForm> filledForm = customerForm.bindFromRequest();				

		if(filledForm.hasErrors()){			
			return badRequest(update.render(filledForm,id));
		}

		CustomerForm csForm=filledForm.get();
		if(csForm==null){
			return badRequest(update.render(filledForm,id));
		}
				
		return ok(update.render(customerForm.fill(csForm),id));	
	}

	public static Result update_get(Long id){		
		Customer customer=Customer.get(id);

		CustomerForm csForm=new CustomerForm();
		csForm.name=customer.name;
		csForm.areaid=customer.area.id.toString();		
		csForm.subareaid=customer.sub_area.id.toString();
		csForm.nodeid=customer.node.id.toString();
		csForm.amplyid=customer.amply.id.toString();
		csForm.address=customer.address;
		csForm.mobile_number=customer.mobile_number;
		csForm.id_number=customer.id_number;		
		csForm.joining_date=customer.joining_date;
		csForm.terminate_date=customer.terminate_date;
		csForm.priceid=customer.price.id.toString();
		csForm.deposite=customer.deposite;
		
		return ok(update.render(customerForm.fill(csForm),id));
	}

	public static Result update_save(Long id){
		Form<CustomerForm> filledForm = customerForm.bindFromRequest();

		if(filledForm.hasErrors()){			
			return badRequest(update.render(filledForm,id));
		}
					
		CustomerForm customerForm=filledForm.get();
		if(customerForm==null){						
			return badRequest(update.render(filledForm,id));
		}		

		Customer customer=Customer.get(id);

		if(customer==null){			
			filledForm.reject("Customer does not exists");
			return badRequest(update.render(filledForm,id));	
		}

		if(Customer.isCustomerIdExists(id,customerForm.id_number,new Long(customerForm.areaid))){
			filledForm.reject("Customer Id already exists.");
			return badRequest(update.render(filledForm,id));
		}
		if(Customer.isMobileNumberExists(id,customerForm.mobile_number)){
			filledForm.reject("Customer mobile number already exists.");
			return badRequest(update.render(filledForm,id));
		}

		formToModel(customer,customerForm);	
		customer.update();
								
		return  redirect(controllers.routes.CustomerController.index());
	}

	private static void formToModel(Customer customer,CustomerForm customerForm){
		Area area=new Area();
		area.id=new Long(customerForm.areaid);
		SubArea subarea=new SubArea();
		subarea.id=new Long(customerForm.subareaid);		
		Price price=new Price();
		price.id=new Long(customerForm.priceid);		
		Node node=new Node();
		node.id=new Long(customerForm.nodeid);
		Amply amply=new Amply();
		amply.id=new Long(customerForm.amplyid);
		
		customer.name=customerForm.name;
		customer.sub_area=subarea;
		customer.address=customerForm.address;
		customer.mobile_number=customerForm.mobile_number;
		customer.id_number=customerForm.id_number;		
		customer.joining_date=customerForm.joining_date;
		customer.terminate_date=customerForm.terminate_date;
		customer.price=price;
		customer.deposite=customerForm.deposite;		
		customer.id_number=customerForm.id_number;
		customer.type=customerForm.type;
		customer.node=node;
		customer.amply=amply;
		customer.area=area;		
	}

	public static Result delete(Long id){		
		Customer customer=Customer.get(id);
		if(customer==null){
			return notFound("Customer having id [" + id + "] does not exists.");
		}
		if(!Invoice.isBelongsToCustomer(id)){
			Customer.delete(id);			
			return ok("Selected Customer has been deleted successfully");
		}
		return notFound("Customer ['" + customer.name + "'] is associated with Invoice(s)");			
	}	

	/**
	 * Return List of all Customers in JSON format.
	 * 
	 * @return
	 */
	public static Result all() {
		List<Customer> customers = Customer.all();
		return ok(Json.toJson(customers));
	}

	public static Result all_active() {
		List<Customer> customers = Customer.allActive();
		return ok(Json.toJson(customers));
	}

	public static Result all_terminated() {
		List<Customer> customers = Customer.allTerminated();
		return ok(Json.toJson(customers));
	}

	public static Result all_active_by_area(Long areaId) {
		if (areaId == null) {
			return badRequest("Expecting Area Id");
		}

		List<Customer> customers = Customer.allActiveByArea(areaId);
		return ok(Json.toJson(customers));
	}

	public static Result all_terminated_by_area(Long areaId) {
		if (areaId == null) {
			return badRequest("Expecting Area Id");
		}

		List<Customer> customers = Customer.allTerminatedByArea(areaId);
		return ok(Json.toJson(customers));
	}	

	/**
	 * Return Customer by id.
	 * 
	 * @return
	 */
	public static Result get(Long id) {
		if (id == null) {
			return badRequest("Expecting Customer Id");
		}

		Customer customer = Customer.get(id);

		if (customer == null) {
			return notFound("Customer with id [" + id + "] does not exists.");
		}
		JsonNode result = Json.toJson(customer);
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
		Customer customer = Json.fromJson(jsonNode, Customer.class);
		if (customer == null) {
			return badRequest("Expecting Customer Json data");
		}
		customer.save();
		ObjectNode result = Json.newObject();
		result.put("id", customer.id);
		return created(result);
	}

	/**
	 * @param id
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public static Result update(Long id) {
		if (id == null) {
			return badRequest("Expecting Customer Id");
		}

		JsonNode jsonNode = request().body().asJson();
		if (jsonNode == null) {
			return badRequest("Expecting Json data");
		}
		Customer customer = Json.fromJson(jsonNode, Customer.class);
		if (customer == null) {
			return badRequest("Expecting Customer Json data");
		}
		customer.id = id;
		customer.update();
		return ok();
	}

}
