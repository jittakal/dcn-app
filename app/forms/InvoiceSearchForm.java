package forms;

import java.io.Serializable;
import play.data.validation.Constraints.Required;

public class InvoiceSearchForm implements Serializable{

	@Required	
	public String areaid;	

	@Required
	public String subareaid;

	public String customerid;

	public String month;

	public String year;
	
}