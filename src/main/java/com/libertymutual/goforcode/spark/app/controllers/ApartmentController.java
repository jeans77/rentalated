package com.libertymutual.goforcode.spark.app.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.libertymutual.goforcode.spark.app.models.Apartment;
import com.libertymutual.goforcode.spark.app.models.User;
import com.libertymutual.goforcode.spark.app.utilities.AutoCloseableDb;
import com.libertymutual.goforcode.spark.app.utilities.JsonHelper;
import com.libertymutual.goforcode.spark.app.utilities.MustacheRenderer;

import spark.Request;
import spark.Response;
import spark.Route;

public class ApartmentController {

	public static final Route details = (Request req, Response res) -> {
		String idAsString = req.params("id");
		int id = Integer.parseInt(idAsString);
		
		try (AutoCloseableDb db = new AutoCloseableDb()){
//		Apartment apartment = Apartment.findById(Integer.parseInt(req.params("id")));
		Apartment apartment = Apartment.findById(id);
		User currentUser = req.session().attribute("currentUser");
//		List<User> usersThatLikedApartment = apartment.getAll(User.class);
//		int numberOfLikes = usersThatLikedApartment.size();
//		int numberOfCurrentUserLikes = 0;
		Map<String, Object> model = new HashMap<String, Object>();
		
//		if (currentUser != null) {
//			List<User> currentUserLikes = ApartmentsUsers.where("user_id = ? and apartment_id = ?", currentUser.getId(), apartment.getId());
//			numberOfCurrentUserLikes = currentUserLikes.size();
//		}
		model.put("apartment", apartment);
//		model.put("numberOfLikes", numberOfLikes);
//		model.put("usersThatLikedApartment", usersThatLikedApartment);
//		model.put("numberOfCurrentUserLikes", numberOfCurrentUserLikes);
		model.put("currentUser", req.session().attribute("currentUser"));
		model.put("noUser", req.session().attribute("currentUser") == null);	
		return MustacheRenderer.getInstance().render("apartment/details.html", model);

		}
	};

	public static final Route newForm = (Request req, Response res) -> {
		return MustacheRenderer.getInstance().render("apartment/newForm.html", null);
	};

	public static final Route create = (Request req, Response res) -> {
		Apartment apartment = new Apartment(
				Integer.parseInt(req.queryParams("rent")),
				Integer.parseInt(req.queryParams("number_of_bedrooms")),
				Double.parseDouble(req.queryParams("number_of_bathrooms")),
				Integer.parseInt(req.queryParams("square_footage")),
				req.queryParams("address"),
				req.queryParams("city"),
				req.queryParams("state"),
				Integer.parseInt(req.queryParams("zip_code")),
				Boolean.parseBoolean(req.queryParams("is_active")));
				
		try (AutoCloseableDb db = new AutoCloseableDb()) {
			User user = req.session().attribute("currentuser");
			user.add(apartment);
			apartment.saveIt();
		}
			res.redirect("/");
			return "";
	};

	public static final Route index = (Request req, Response res) -> {
		User currentUser = req.session().attribute("currentUser");
		long id = (int) currentUser.getId();
		
		try (AutoCloseableDb db = new AutoCloseableDb()) {
//			List<Apartment> apartments = Apartment.where("user_id = ?", id);
			List<Apartment> activeApartments = Apartment.where("user_id = ? and is_active = ?", id, true);
			List<Apartment> inactiveApartments = Apartment.where("user_id = ? and is_active = ?", id, false);
// or			List<Apartment> apartments = currentUser.getAll(Apartment.class);
// or			Apartment.where("user_id = + id);
			Map<String, Object> model = new HashMap<String, Object>();
//			model.put("apartments", apartments);
			model.put("activeApartments", activeApartments);
			model.put("inactiveApartments", inactiveApartments);
			return MustacheRenderer.getInstance().render("apartment/index.html", model);

		}
		
	};
}
