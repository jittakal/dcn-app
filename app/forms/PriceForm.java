package forms;

import java.io.Serializable;
import play.data.validation.Constraints.Required;
import models.Price;

public class PriceForm implements Serializable{
	
	@Required
	public Integer amount;

	/*public String validate() {        
		if(Price.isAmountExists(amount)){
			return "Amount is already exists";
		}		
        return null;
    }*/
}
