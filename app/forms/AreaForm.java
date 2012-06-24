package forms;

import java.io.Serializable;
import play.data.validation.Constraints.Required;
import models.Area;

public class AreaForm implements Serializable{

	@Required
	public String name;	
	
}