package com.libertymutual.goforcode.spark.app.controllers;

import java.util.HashMap;
import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;

import com.libertymutual.goforcode.spark.app.models.User;
import com.libertymutual.goforcode.spark.app.utilities.AutoCloseableDb;
import com.libertymutual.goforcode.spark.app.utilities.MustacheRenderer;

import spark.Request;
import spark.Response;
import spark.Route;

public class UserController {
	
	public static final Route newForm = (Request req, Response res) -> {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("currentUser", req.session().attribute("currentUser"));
		model.put("noUser", req.session().attribute("currentUser") == null);
		return MustacheRenderer.getInstance().render("users/newForm.html", null);
	};

	public static Route create = (Request req, Response res) -> {

		String password = BCrypt.hashpw(req.queryParams("password"), BCrypt.gensalt());
		String email = req.queryParams("email");
		String firstName = req.queryParams("first_name");
		String lastName = req.queryParams("last_name");
		
		User user  = new User (email, password, firstName, lastName);
		System.out.println("User: " + user);
		if (user != null) {
			try (AutoCloseableDb db = new AutoCloseableDb()) {
				user.saveIt();
				req.session().attribute("currentUser", user);
				req.session().attribute("pwMessage", " ");
			}
		}
		res.redirect("/");
		return "";
	};
	
	
//	public static final Route create = (Request req, Response res) -> {
//		Map<String, String> map = req.queryMap("user")
//				.toMap()
//				.entrySet()
//				.stream()
//				.map(entry -> new AbstractMap.SimpleEntry<String, String>(entry.getKey(), entry.getValue()[0]))
//				.peek(entry -> entry.setValue(entry.getKey() == "password"
//								? BCrypt.hashpw(entry.getValue(), BCrypt.gensalt())
//								: entry.getValue()))
//				.collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));
//		User user = new User();
//		user.fromMap(map);
//		try (AutoCloseableDb db = new AutoCloseableDb()) {
//			user.saveIt();
//			req.session().attribute("currentUser", user);
//			res.redirect("/");
//			return "";
//		}
//	};

}