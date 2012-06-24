package forms;

import java.io.Serializable;
import play.data.validation.Constraints.Required;
import models.SubArea;

public class SubAreaForm implements Serializable{

	@Required
	public String name;

	@Required
	public String areaid;

	@Required
	public String employeeid;	

	/*public String validate() {        
		if(SubArea.isNameExists(name)){
			return "Sub Area name ['" + name + "'] already exists";
		}		
        return null;
    }*/
	
}