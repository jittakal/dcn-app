package forms;

import java.io.Serializable;
import play.data.validation.Constraints.Required;

public class PriceForm implements Serializable{
	
	@Required
	public Integer amount;
}
