package com.arawaney.tumascotik.client.backend;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.arawaney.tumascotik.client.R;
import com.arawaney.tumascotik.client.control.MainController;
import com.arawaney.tumascotik.client.db.provider.UserProvider;
import com.arawaney.tumascotik.client.listener.ParsePetListener;
import com.arawaney.tumascotik.client.listener.ParseUserListener;
import com.arawaney.tumascotik.client.model.Pet;
import com.arawaney.tumascotik.client.model.User;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ParseProvider {
	private static final String LOG_TAG = "Tumascotik-Client-ParseProvider";

	public static void logIn(String username, final String password,
			final Context context, final ParseUserListener listener) {
		final ProgressDialog progressDialog = ProgressDialog.show(context, "",
				"Realizando Login...");
		ParseUser.logInInBackground(username, password, new LogInCallback() {

			public void done(ParseUser user, ParseException e) {

				if (user != null) {
					ParseProvider.setUser(context, user, password);

				} else {
					Log.e(LOG_TAG, "Error by login :" + e.getMessage());
				}
				progressDialog.dismiss();
				listener.OnLoginResponse();
			}

		});
	}

	public static void initializeParse(Context context) {
		Parse.initialize(context, "mLQoFv0KQAi0WYL1JGSSKGRjQ77DgZST0qX47TJe",
				"X0GMTJKHoSDMjA7N9leHfaYNfptL4e8fPhsGMDVN");
	}

	protected static void setUser(Context context, ParseUser user,
			String password) {
		User dbuser = new User();

		dbuser.setSystemId(user.getObjectId().toString());
		dbuser.setUsername(user.getUsername());
		dbuser.setPassword(password);
		dbuser.setName(user.get("Name").toString());
		dbuser.setLastname(user.get("LastName").toString());
		dbuser.setCedula(user.getInt("Cedula"));
		dbuser.setAddress(user.get("Address").toString());
		dbuser.setEmail(user.getEmail());
		dbuser.setGender(user.getInt("Gender"));
		dbuser.setMobile_telephone(user.getLong("Telephone_mobile"));
		dbuser.setHouse_telephone(user.getLong("Telephone_House"));
		dbuser.setAdmin(user.getBoolean("Admin") ? 1 : 0);

		UserProvider.insertUser(context, dbuser);

	}

	public static void getSpecies(final ParsePetListener listener) {

		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Specie");
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> cList, ParseException e) {
				final ArrayList<String> species;
				if (e == null) {
					species = new ArrayList<String>();
					for (ParseObject object : cList) {
						species.add(object.getString("Name"));
					}

				} else {
					species = null;
					Log.d(LOG_TAG, " Query Error: " + e.getMessage());
				}

				listener.onSpecieQueryFinished(species);
			}

		});
	}

	public static void getBreed(final ParsePetListener listener,
			String specieName) {
		ParseQuery<ParseObject> innerQuery = new ParseQuery<ParseObject>(
				"Specie");
		innerQuery.whereEqualTo("Name", specieName);
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Breed");
		query.whereMatchesQuery("Specie_ID", innerQuery);

		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> cList, ParseException e) {
				final ArrayList<String> breed;
				if (e == null) {
					breed = new ArrayList<String>();
					for (ParseObject object : cList) {
						breed.add(object.getString("Name"));
					}

				} else {
					breed = null;
					Log.d(LOG_TAG, " Query Error: " + e.getMessage());
				}

				listener.onBreedQueryFinished(breed);
			}

		});
	}

	public static void getPet(final ParsePetListener listener, String systemId) {

		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Pet");

		// Retrieve the object by id
		query.getInBackground(systemId, new GetCallback<ParseObject>() {

			@Override
			public void done(ParseObject parsedPet, ParseException e) {
				Pet pet;
				if (e == null) {
					pet = new Pet();

					pet = readPetfromParsedObject(parsedPet, listener);

				} else {
					pet = null;
					Log.d(LOG_TAG, " Query Error: " + e.getMessage());
				}

				listener.onPetQueryFinished(pet);

			}

		});
	}

	private static Pet readPetfromParsedObject(ParseObject parsedPet,
			final ParsePetListener listener) {

		final Pet pet = new Pet();

		pet.setName(parsedPet.getString("Name"));

		if (parsedPet.getBoolean("Puppy")) {
			pet.setPuppy(Pet.AGE_PUPPY);
		} else {
			pet.setPuppy(Pet.AGE_ADULT);
		}
		pet.setComment(parsedPet.getString("Comment"));
		pet.setGender(parsedPet.getInt("Gender"));

		pet.setSystem_id(parsedPet.getObjectId());

		ParseRelation<ParseObject> breedRelation = parsedPet
				.getRelation("Breed_ID");
		breedRelation.getQuery().findInBackground(
				new FindCallback<ParseObject>() {

					@Override
					public void done(List<ParseObject> breeds, ParseException e) {
						if (e == null) {
							ParseObject breed = breeds.get(0);
							pet.setBreed(breed.getString("Name"));
							listener.onPetQueryFinished(pet);
							ParseRelation<ParseObject> speciedRelation = breed
									.getRelation("Specie_ID");
							speciedRelation.getQuery().findInBackground(
									new FindCallback<ParseObject>() {

										@Override
										public void done(
												List<ParseObject> species,
												ParseException e) {
											if (e == null) {
												ParseObject specie = species
														.get(0);
												pet.setSpecie(specie
														.getString("Name"));
												listener.onPetQueryFinished(pet);

											} else
												Log.e(LOG_TAG,
														"Getting Breed error"
																+ e.getMessage());

										}
									});

							ParseRelation<ParseObject> propertiesRelation = breed
									.getRelation("PetProperties_ID");
							propertiesRelation.getQuery().findInBackground(
									new FindCallback<ParseObject>() {

										@Override
										public void done(
												List<ParseObject> properties,
												ParseException e) {
											if (e == null) {
												ParseObject propertie = properties
														.get(0);
												pet.setPet_properties(propertie
														.getString("Name"));
												listener.onPetQueryFinished(pet);

											} else
												Log.e(LOG_TAG,
														"Getting propertie error"
																+ e.getMessage());

										}
									});
						} else
							Log.e(LOG_TAG,
									"Getting Breed error" + e.getMessage());

					}
				});

		return pet;

	}

	public static void updatePet(final ParsePetListener listener, final Pet pet) {

		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Pet");

		// Retrieve the object by id
		query.getInBackground(pet.getSystem_id(),
				new GetCallback<ParseObject>() {
					public void done(ParseObject parsedPet, ParseException e) {
						if (e == null) {

							parsedPet.put("Name", pet.getName());
							parsedPet.put("Gender", pet.getGender());
							parsedPet.put("Comment", pet.getComment());
							if (pet.getPuppy() == Pet.AGE_PUPPY) {
								parsedPet.put("Puppy", true);
							} else {
								parsedPet.put("Puppy", false);
							}
							parsedPet.saveInBackground();

							listener.OnPetUpdateFinished(true);

							updateBreed(pet, parsedPet);

						} else
							listener.OnPetUpdateFinished(false);

					}
				});

	}

	private static void updateBreed(final Pet pet, final ParseObject parsedPet) {
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Breed");
		query.whereMatches("Name", pet.getBreed());
		query.getFirstInBackground(new GetCallback<ParseObject>() {
			public void done(ParseObject breed, ParseException e) {
				if (e == null) {
					ParseRelation<ParseObject> relation = parsedPet
							.getRelation("Breed_ID");
					relation.add(breed);
					parsedPet.saveInBackground();
					cleanBreeds(breed.getString("Name"), parsedPet);
				} else {
					Log.d(LOG_TAG, "Breed not found : " + e.getMessage());
				}
			}

			private void cleanBreeds(String breedName,
					final ParseObject parsedPet) {
				final ParseRelation<ParseObject> breedrelation = parsedPet
						.getRelation("Breed_ID");
				ParseQuery<ParseObject> query = breedrelation.getQuery();
				query.whereNotEqualTo("Name", breedName);
				query.findInBackground(new FindCallback<ParseObject>() {

					@Override
					public void done(List<ParseObject> arg0, ParseException e) {
						if (e == null) {
							for (ParseObject parseObject : arg0) {
								breedrelation.remove(parseObject);
							}
							parsedPet.saveInBackground();
						} else {
							Log.d(LOG_TAG,
									"Breed Relations not found: "
											+ e.getMessage());
						}

					}
				});

			}
		});
	}

	public static void getPets(final ParsePetListener listener, final User user) {
		String systemId = user.getSystemId();
		ParseQuery<ParseObject> innerQuery = new ParseQuery<ParseObject>(
				"_User");
		innerQuery.whereEqualTo("objectId", systemId);
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Pet");
		query.whereMatchesQuery("User_ID", innerQuery);
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> parsedPets, ParseException e) {
				ArrayList<Pet> pets = new ArrayList<Pet>();
				if (e == null) {
					for (ParseObject parseObject : parsedPets) {

						Pet pet = new Pet();
						pet = readPetfromParsedObject(parseObject, listener);
						pet.setOwner(user);
						pets.add(pet);
					}
				} else {
					Log.e(LOG_TAG, e.getMessage());
					pets = null;
				}

				listener.onGetAllPets(pets);
			}
		});

	}

	public static void insertPet(final Context context, final Pet auxPet,
			final ParsePetListener listener) {

		final ParseObject parsePet = new ParseObject("Pet");
		parsePet.put("Name", auxPet.getName());
		parsePet.put("Gender", auxPet.getGender());

		if (auxPet.getPuppy() == Pet.AGE_PUPPY) {
			parsePet.put("Puppy", true);
		} else {
			parsePet.put("Puppy", false);
		}
		parsePet.put("Comment", auxPet.getComment());

		parsePet.saveInBackground(new SaveCallback() {

			@Override
			public void done(ParseException arg0) {
				listener.onPetInserted(parsePet.getObjectId());
				saveNewPetUser(parsePet, auxPet, context);
			}

			private void saveNewPetUser(final ParseObject parsePet,
					final Pet auxPet, final Context context) {

				String systemId = auxPet.getOwner().getSystemId();
				ParseQuery<ParseObject> innerQuery = new ParseQuery<ParseObject>(
						"_User");
				innerQuery.whereEqualTo("objectId", systemId);
				innerQuery.findInBackground(new FindCallback<ParseObject>() {

					@Override
					public void done(List<ParseObject> arg0, ParseException e) {
						if (e == null) {
							ParseRelation<ParseObject> userRelation = parsePet
									.getRelation("User_ID");
							userRelation.add(arg0.get(0));
							parsePet.saveInBackground();
							saveNewPetBreed(parsePet, auxPet, context);
						} else {
							Log.e(LOG_TAG, "No uset matches inserting new pet "
									+ e.getMessage());

						}

					}
				});

			}

			private void saveNewPetBreed(final ParseObject parsePet,
					final Pet auxPet, final Context context) {

				ParseQuery<ParseObject> innerQuery = new ParseQuery<ParseObject>(
						"Breed");
				innerQuery.whereEqualTo("Name", auxPet.getBreed());
				innerQuery.findInBackground(new FindCallback<ParseObject>() {

					@Override
					public void done(List<ParseObject> arg0, ParseException e) {
						if (e == null) {
							ParseRelation<ParseObject> breedRelation = parsePet
									.getRelation("Breed_ID");
							breedRelation.add(arg0.get(0));
							parsePet.saveInBackground();
						} else {
							Log.e(LOG_TAG,
									"No breed matches inserting new pet "
											+ e.getMessage());

						}
					}
				});
			}

		});

	}

	public static void getUser(final ParseUserListener parseListener,
			final User user) {

		String systemId = user.getSystemId();
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("_User");
		query.whereEqualTo("objectId", systemId);
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> arg0, ParseException e) {
				if (e == null) {
					ParseObject parseUSer = arg0.get(0);

					User updatedUser = user;

					updatedUser.setName(parseUSer.get("Name").toString());
					updatedUser.setLastname(parseUSer.get("LastName")
							.toString());
					updatedUser.setCedula(parseUSer.getInt("Cedula"));
					updatedUser.setAddress(parseUSer.get("Address").toString());
					updatedUser.setEmail(parseUSer.getString("email"));
					updatedUser.setGender(parseUSer.getInt("Gender"));
					updatedUser.setMobile_telephone(parseUSer
							.getLong("Telephone_mobile"));
					updatedUser.setHouse_telephone(parseUSer
							.getLong("Telephone_House"));
					updatedUser.setAdmin(parseUSer.getBoolean("Admin") ? 1 : 0);

					parseListener.onUserQueryFinish(updatedUser, true);
				} else {
					Log.e(LOG_TAG, "No user " + e.getMessage());
					parseListener.onUserQueryFinish(null, false);

				}

			}

		});
	}

	public static void updateUser(final ParseUserListener listener,
			final User user) {

		ParseUser parsedUser = ParseUser.getCurrentUser();

		if (parsedUser != null) {

			parsedUser.put("Name", user.getName());
			parsedUser.put("LastName", user.getLastname());
			parsedUser.put("email", user.getEmail());
			parsedUser.put("Gender", user.getGender());
			parsedUser.put("Telephone_mobile", user.getMobile_telephone());
			parsedUser.put("Telephone_House", user.getHouse_telephone());
			parsedUser.put("Address", user.getAddress());

			
		    parsedUser.saveInBackground(new SaveCallback() {
				
				@Override
				public void done(ParseException e) {
					if (e != null) {
						listener.onUserUpdateFinish(user, false);						
					}else{
						listener.onUserUpdateFinish(user, true);
					}
					
				}
			});


		} else {
			Log.d(LOG_TAG, "currentuser null");
			listener.onUserUpdateFinish(user, false);
		}

	}

	public static void getCurrentUser(Context context) {

		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser == null) {
			ParseUser.logInInBackground(MainController.USER.getUsername(),
					MainController.USER.getPassword(), new LogInCallback() {

						public void done(ParseUser user, ParseException e) {

							if (user != null) {

							} else {
								Log.e(LOG_TAG,
										"Error by getcurrentuser :"
												+ e.getMessage());
							}
						}

					});
		}

	}
}
