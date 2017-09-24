package com.libertymutual.goforcode.spark.app.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;

import com.libertymutual.goforcode.spark.app.filters.SecurityFilters;
import com.libertymutual.goforcode.spark.app.models.User;
import com.libertymutual.goforcode.spark.app.utilities.AutoCloseableDb;
import com.libertymutual.goforcode.spark.app.utilities.MustacheRenderer;

import spark.Request;
import spark.Response;
import spark.Route;

public class SessionController {

	public static final Route newForm = (Request req, Response res) -> {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("returnPath", req.queryParams("returnPath"));
		model.put("currentUser", req.session().attribute("currentUser"));
		model.put("noUser", req.session().attribute("currentUser") == null);
		UUID thisCSRF = SecurityFilters.getNewCSRF();
		model.put("thisCSRF", thisCSRF);
		String StringCSRF = thisCSRF.toString();
		res.cookie("thisCSRF", StringCSRF);
		MustacheRenderer.getInstance();
		System.out.println("SessionNewForm Current : " + (req.session().attribute("currentUser")));
		System.out.println("SessionNewForm noUser  : " + (req.session().attribute("currentUser") == null));
		return MustacheRenderer.getInstance().render("session/newForm.html", model);
	};

	public static final Route create = (Request req, Response res) -> {
		String email = req.queryParams("email");
		String password = req.queryParams("password");
		System.out.println("SessionCreate Current : " + (req.session().attribute("currentUser")));
		System.out.println("SessionCreate noUser  : " + (req.session().attribute("currentUser") == null));

		try (AutoCloseableDb db = new AutoCloseableDb()) {

			User user = User.findFirst("email = ?", email);
			String testCSRF = req.queryParams("thisCSRF"); 
			String receivedCSRF = req.cookie("thisCSRF");
			
			if (user != null && BCrypt.checkpw(password, user.getPassword()) && testCSRF.equals(receivedCSRF)) {
				req.session().attribute("currentUser", user);
			} else if (user != null) {
				req.session().attribute("message", "Wrong Password");
			} else {
				req.session().attribute("message", "Unknown Username");
			}
		}
		
		res.redirect(req.queryParamOrDefault("returnPath", "/"));
		return "";
	};
	
	public static final Route destroy = (Request req, Response res) -> {
		req.session().attribute("currentUser", null);
		res.redirect("/");
		return "";	
	};
}
