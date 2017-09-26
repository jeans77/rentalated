package com.libertymutual.goforcode.spark.app.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.libertymutual.goforcode.spark.app.models.Apartment;
import com.libertymutual.goforcode.spark.app.models.ApartmentsUsers;
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
		Boolean currentIsCreated = false;
		System.out.println("AptDetails User : " + (req.session().attribute("currentUser")));
		System.out.println("AptDetails noUser  : " + (req.session().attribute("currentUser") == null));
		try (AutoCloseableDb db = new AutoCloseableDb()){
//		Apartment apartment = Apartment.findById(Integer.parseInt(req.params("id")));
		Apartment apartment = Apartment.findById(id);
		User currentUser = req.session().attribute("currentUser");
		List<User> usersThatLikedApartment = apartment.getAll(User.class);
		int numberOfLikes = usersThatLikedApartment.size();
		int numberOfCurrentUserLikes = 0;
		Map<String, Object> model = new HashMap<String, Object>();
		Object createdBy = apartment.get("user_id");
		Object isActive = apartment.get("is_active");
		List<User> likedBy = apartment.getAll(User.class);

		if (currentUser != null) {
			List<User> currentUserLikes = ApartmentsUsers.where("user_id = ? and apartment_id = ?", currentUser.getId(), apartment.getId());
			numberOfCurrentUserLikes = currentUserLikes.size();
			Object currentUserId = currentUser.getId();
			if (currentUserId.equals(createdBy)) {
				currentIsCreated = true;
			}
		}
		model.put("apartment", apartment);
		model.put("numberOfLikes", numberOfLikes);
		model.put("usersThatLikedApartment", usersThatLikedApartment);
		model.put("id", id);
		model.put("createdBy", createdBy);
		model.put("likedBy", likedBy);
		model.put("currentIsCreated", currentIsCreated);
		model.put("numberOfCurrentUserLikes", numberOfCurrentUserLikes);
		model.put("currentUser", req.session().attribute("currentUser"));
		model.put("isActive", isActive);
		model.put("noUser", req.session().attribute("currentUser") == null);
		
		return MustacheRenderer.getInstance().render("apartment/details.html", model);

		}
	};
	
//	public static final Route details = (Request req, Response res) -> {
//		String idAsString = req.params("id");
//		int id = Integer.parseInt(idAsString);
//		
//		try (AutoCloseableDb db = new AutoCloseableDb()){
////		Apartment apartment = Apartment.findById(Integer.parseInt(req.params("id")));
//		Apartment apartment = Apartment.findById(id);
//		User currentUser = req.session().attribute("currentUser");
//		List<User> usersThatLikedApartment = apartment.getAll(User.class);
//		int numberOfLikes = usersThatLikedApartment.size();
//		int numberOfCurrentUserLikes = 0;
//		Map<String, Object> model = new HashMap<String, Object>();
//		System.out.println("userC :" + req.session().attribute("currentUser"));
//		if (currentUser != null) {
//			List<User> currentUserLikes = ApartmentsUsers.where("user_id = ? and apartment_id = ?", currentUser.getId(), apartment.getId());
//			numberOfCurrentUserLikes = currentUserLikes.size();
//		}
//		model.put("apartment", apartment);
//		model.put("numberOfLikes", numberOfLikes);
//		model.put("usersThatLikedApartment", usersThatLikedApartment);
//		model.put("numberOfCurrentUserLikes", numberOfCurrentUserLikes);
//		model.put("currentUser", req.session().attribute("currentUser"));
//		model.put("noUser", req.session().attribute("currentUser") == null);
//		
//		return MustacheRenderer.getInstance().render("apartment/details.html", model);
//
//		}
//	};

	public static final Route newForm = (Request req, Response res) -> {
		System.out.println("userB :" + req.session().attribute("currentUser"));
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("currentUser", req.session().attribute("currentUser"));
		model.put("noUser", req.session().attribute("currentUser") == null);
		
		System.out.println("AptNewForm User : " + (req.session().attribute("currentUser")));
		System.out.println("AptNewForm noUser  : " + (req.session().attribute("currentUser") == null));
		
		return MustacheRenderer.getInstance().render("apartment/newForm.html", model);
	};

	
	public static final Route create =(Request req,Response res) -> {
		try (AutoCloseableDb db = new AutoCloseableDb()) {
			Apartment apartment=new Apartment(
					Integer.parseInt(req.queryParams("rent")),
					Integer.parseInt(req.queryParams("number_of_bedrooms")),
					Double.parseDouble(req.queryParams("number_of_bathrooms")),
					Integer.parseInt(req.queryParams("square_footage")),
					req.queryParams("address"),
					req.queryParams("city"),
					req.queryParams("state"),
					Integer.parseInt(req.queryParams("zip_code")), 
					Boolean.parseBoolean(req.queryParams("is_active"))
				);
					
			apartment.set("is_active", true);
			User user=req.session().attribute("currentUser");
			user.add(apartment);apartment.saveIt();
			res.redirect("/");
			return"";}};
	
//	public static final Route create = (Request req, Response res) -> {
//		System.out.println("userD :" + req.session().attribute("currentuser"));
//		Apartment apartment = new Apartment(
//				Integer.parseInt(req.queryParams("rent")),
//				Integer.parseInt(req.queryParams("number_of_bedrooms")),
//				Double.parseDouble(req.queryParams("number_of_bathrooms")),
//				Integer.parseInt(req.queryParams("square_footage")),
//				req.queryParams("address"),
//				req.queryParams("city"),
//				req.queryParams("state"),
//				Integer.parseInt(req.queryParams("zip_code")),
//				Boolean.parseBoolean(req.queryParams("is_active")));
//				
//		try (AutoCloseableDb db = new AutoCloseableDb()) {
//			User user = req.session().attribute("currentuser");
//			System.out.println("userA :" + user);
//			user.add(apartment);
//			apartment.saveIt();
//		}
//			res.redirect("/");
//			return "";
//	};

//			public static final Route details = (Request req, Response res) -> {
//			try (AutoCloseableDb db = new AutoCloseableDb()){
//				LazyList<Apartment> apartments = 
//			}
			
	public static final Route index = (Request req, Response res) -> {
		System.out.println("AptIndex User : " + (req.session().attribute("currentUser")));
		System.out.println("AptIndex noUser  : " + (req.session().attribute("currentUser") == null));


		long id = -1;
		
		User user = req.session().attribute("currentUser");
		if (user != null) {
			id = (long) user.getId();
		}

		try (AutoCloseableDb db = new AutoCloseableDb()) {
			List<Apartment> myListings = Apartment.where("user_id = ?", id);
			List<Apartment> myActiveListings = Apartment.where("user_id = ? and is_active = ?", id, true);
			List<Apartment> myInactiveListings = Apartment.where("user_id = ? and is_active = ?", id, false);
// or			List<Apartment> apartments = currentUser.getAll(Apartment.class);
// or			Apartment.where("user_id = + id);
			Map<String, Object> model = new HashMap<String, Object>();
//			model.put("apartments", apartments);

//			System.out.println("MyListings: " + myListings);
//			System.out.println("MyActiveListings: " + myActiveListings);
//			System.out.println("MyInactiveListings: " + myInactiveListings);
			
			model.put("myActiveListings", myActiveListings);
			model.put("myInactiveListings", myInactiveListings);
			model.put("currentUser",req.session().attribute("currentUser"));
			model.put("noUser",req.session().attribute("currentUser")==null);
			return MustacheRenderer.getInstance().render("apartment/usersListings.html", model);

		}
		
	};
	
	public static final Route like = (Request req,Response res)->{
		System.out.println("AptLike User : " + (req.session().attribute("currentUser")));
		System.out.println("AptLike noUser  : " + (req.session().attribute("currentUser") == null));

		String idAsString=req.params("id");
		int id=Integer.parseInt(idAsString);
		try (AutoCloseableDb db = new AutoCloseableDb()) {
			Apartment apartment=Apartment.findById(id);
			User user=req.session().attribute("currentUser");

			// // should get a list of all users who have liked this apartment
			apartment.add(user);
			apartment.saveIt();
			res.redirect("/apartments/"+id);
			return"";
		}
	};

	public static Route deactivate = (Request req, Response res) -> {
		System.out.println("AptDeact User : " + (req.session().attribute("currentUser")));
		System.out.println("AptDeact noUser  : " + (req.session().attribute("currentUser") == null));

		try (AutoCloseableDb db = new AutoCloseableDb()) {
		String idAsString=req.params("id");
		Map<String,Object> model=new HashMap<String,Object>();
		int id = Integer.parseInt(idAsString);
		Apartment apartment = Apartment.findById(id);
		apartment.set("is_active", false);
		apartment.saveIt();
		res.redirect("/apartments/"+id);
		return"";
		}
		
	};
	
	public static Route activate = (Request req, Response res) -> {
		
		try (AutoCloseableDb db = new AutoCloseableDb()) {
		String idAsString=req.params("id");
		Map<String,Object> model=new HashMap<String,Object>();
		int id = Integer.parseInt(idAsString);
		Apartment apartment = Apartment.findById(id);
		apartment.set("is_active", true);
		apartment.saveIt();
		res.redirect("/apartments/"+id);
		return"";
		}
	};
	
}
