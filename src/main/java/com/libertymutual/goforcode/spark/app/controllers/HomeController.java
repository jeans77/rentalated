package com.libertymutual.goforcode.spark.app.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.libertymutual.goforcode.spark.app.models.Apartment;
import com.libertymutual.goforcode.spark.app.utilities.AutoCloseableDb;
import com.libertymutual.goforcode.spark.app.utilities.MustacheRenderer;

import spark.Request;
import spark.Response;
import spark.Route;

public class HomeController {

	
	public static final Route index = (Request req, Response res) -> {

		try (AutoCloseableDb db = new AutoCloseableDb()){
	
		List<Apartment> apartments = Apartment.findAll();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("apartments", apartments);
		model.put("message", req.session().attribute("message"));
		model.put("currentUser", req.session().attribute("currentUser"));
		model.put("noUser", req.session().attribute("currentUser") == null);
		System.out.println("HOMEIndex Current : " + (req.session().attribute("currentUser")));
		System.out.println("homeIndex noUser  : " + (req.session().attribute("currentUser") == null));
		return MustacheRenderer.getInstance().render("home/index.html", model);
		
		}
	};

}

