package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import play.db.ebean.Model;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.validation.NotNull;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "username" }))
public class User extends Model {

	@Id
	public Long id;

	@NotNull
	public String username;

	@NotNull
	public String password;

	@NotNull
	public String role; // Admin,Operator //TODO Employee,

	/*
	 * @OneToOne
	 * 
	 * @Required public Employee employee;
	 */

	public static Finder<Long, User> find = new Finder(Long.class, User.class);

	public static User get(Long id) {
		return find.byId(id);
	}

	public static List<User> all() {
		return find.all();
	}

	public static void delete(Long id) {
		find.byId(id).delete();
	}

	public static User authenticate(String username, String password) {
		User user = Ebean.find(User.class).where().eq("username", username)
				.eq("password", password).findUnique();
		return user;
	}

}
