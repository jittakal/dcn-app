package forms;

import java.io.Serializable;
import play.data.validation.Constraints.Required;
import play.data.validation.Constraints.MaxLength;

public class ChangePasswdForm implements Serializable{	
	
	@Required	
	public String current_passwd;

	@Required	
	public String new_passwd;

	@Required	
	public String confirm_passwd;

	public String validate() {        
		if(new_passwd!=null && !new_passwd.equals(confirm_passwd)) {
			return "New and confirm password should be same, please try again";
		}		
        return null;
    }

}