package forms;

import java.io.Serializable;
import play.data.validation.Constraints.Required;

public class AreaForm implements Serializable{

	@Required
	public String name;
	
}