package forms;

import java.io.Serializable;
import play.data.validation.Constraints.Required;

public class InvoiceGenerateForm implements Serializable{

	@Required
	public String month;

	@Required
	public String year;
	
}