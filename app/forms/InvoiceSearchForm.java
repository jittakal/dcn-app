package forms;

import java.io.Serializable;
import play.data.validation.Constraints.Required;

public class InvoiceSearchForm implements Serializable{

	@Required	
	public String areaid;	

	public String subareaid;
	
	@Required
	public String month;

	@Required
	public String year;
	
}