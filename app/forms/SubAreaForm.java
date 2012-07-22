package forms;

import java.io.Serializable;
import play.data.validation.Constraints.Required;
import play.data.validation.Constraints.MaxLength;
import models.SubArea;

public class SubAreaForm implements Serializable{

	@Required
	@MaxLength(value=120)
	public String name;

	@Required
	public String areaid;
	
	/*public String validate() {        
		if(SubArea.isNameExists(name)){
			return "Sub Area name ['" + name + "'] already exists";
		}		
        return null;
    }*/
	
}