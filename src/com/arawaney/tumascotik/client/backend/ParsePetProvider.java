package com.arawaney.tumascotik.client.backend;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.arawaney.tumascotik.client.R;
import com.arawaney.tumascotik.client.control.MainController;
import com.arawaney.tumascotik.client.db.provider.BreedProvider;
import com.arawaney.tumascotik.client.db.provider.PetPropertieProvider;
import com.arawaney.tumascotik.client.db.provider.PetProvider;
import com.arawaney.tumascotik.client.db.provider.RequestProvider;
import com.arawaney.tumascotik.client.db.provider.SpecieProvider;
import com.arawaney.tumascotik.client.db.provider.UserProvider;
import com.arawaney.tumascotik.client.listener.ParsePetListener;
import com.arawaney.tumascotik.client.listener.ParseRequestListener;
import com.arawaney.tumascotik.client.listener.ParseServiceListener;
import com.arawaney.tumascotik.client.listener.ParseUserListener;
import com.arawaney.tumascotik.client.model.Breed;
import com.arawaney.tumascotik.client.model.Pet;
import com.arawaney.tumascotik.client.model.PetPropertie;
import com.arawaney.tumascotik.client.model.Request;
import com.arawaney.tumascotik.client.model.Specie;
import com.arawaney.tumascotik.client.model.User;
import com.arawaney.tumascotik.client.util.CalendarUtil;
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

public class ParsePetProvider {
	private static final String LOG_TAG = "Tumascotik-Client-ParsePetProvider";

	private static final String PUPPY_TAG = "puppy";
	private static final String AGRESSIVE_TAG = "aggressive";
	private static final String COMMENT_TAG = "comment";
	private static final String UPDATED_AT_TAG = "updatedAt";
	private static final String USER_ID_TAG = "userId";
	private static final String GENDER_TAG = "gender";
	private static final String NAME_TAG = "name";
	private static final String BREED_ID_TAG = "breedId";
	private static final String SPECIE_ID_TAG = "specieId";
	private static final String PET_PROPERTIE_ID_TAG = "petPropertiesId";
	private static final String ACTIVE_TAG = "active";

	private static final String PET_TABLE = "Pet";
	private static final String BREED_TABLE = "Breed";
	private static final String SPECIE_TABLE = "Specie";
	private static final String PET_PROPERTIES_TABLE = "Pet_Properties";

	private static final String SEX_MALE = "M";
	private static final String SEX_FEMALE = "F";
	private static final int BREED_QUERY_LIMIT = 250;


	public static void getAllSpecies(final ParsePetListener listener) {

		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
				SPECIE_TABLE);
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> cList, ParseException e) {
				final ArrayList<Specie> species;
				if (e == null) {
					species = new ArrayList<Specie>();
					for (ParseObject object : cList) {
						Specie specie = new Specie();
						specie.setName(object.getString(NAME_TAG));
						specie.setSystem_id(object.getObjectId());
						Calendar Updated_At = Calendar.getInstance();
						Updated_At.setTimeInMillis(object.getUpdatedAt()
								.getTime());
						specie.setUpdated_at(Updated_At);
						species.add(specie);
					}

					listener.onAllSpeciesQueryFinished(true, species);

				} else {
					species = null;
					Log.d(LOG_TAG, " Query Error: " + e.getMessage());
					listener.onAllSpeciesQueryFinished(false, species);
				}

			}

		});
	}

	public static void getAllBreeds(final ParsePetListener listener) {

		
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(BREED_TABLE);
		query.setLimit(BREED_QUERY_LIMIT);
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> cList, ParseException e) {
				final ArrayList<Breed> breeds;
				if (e == null) {
					breeds = new ArrayList<Breed>();
					for (ParseObject object : cList) {
						Breed breed = new Breed();
						breed.setName(object.getString(NAME_TAG));
						breed.setSystem_id(object.getObjectId());

						PetPropertie petPropertie = new PetPropertie();
						petPropertie.setSystem_id(object
								.getString(PET_PROPERTIE_ID_TAG));
						breed.setPetPropertie(petPropertie);
						Calendar Updated_At = Calendar.getInstance();
						Updated_At.setTimeInMillis(object.getUpdatedAt()
								.getTime());
						breed.setUpdated_at(Updated_At);

						Specie specie = new Specie();
						specie.setSystem_id(object.getString(SPECIE_ID_TAG));
						breed.setSpecie(specie);

						breeds.add(breed);
					}

					listener.onAllBreedsQueryFinished(true, breeds);

				} else {
					breeds = null;
					Log.d(LOG_TAG, " Query Error: " + e.getMessage());
					listener.onAllBreedsQueryFinished(false, breeds);
				}

			}

		});
	}

	public static void getAllPetProperties(final ParsePetListener listener) {

		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
				PET_PROPERTIES_TABLE);
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> cList, ParseException e) {
				final ArrayList<PetPropertie> petProperties;
				if (e == null) {
					petProperties = new ArrayList<PetPropertie>();
					for (ParseObject object : cList) {
						PetPropertie petPropertie = new PetPropertie();
						petPropertie.setName(object.getString(NAME_TAG));
						petPropertie.setSystem_id(object.getObjectId());
						Calendar Updated_At = Calendar.getInstance();
						Updated_At.setTimeInMillis(object.getUpdatedAt()
								.getTime());
						petPropertie.setUpdated_at(Updated_At);
						petProperties.add(petPropertie);
					}

					listener.onAllPetPropertiesFinished(true, petProperties);

				} else {
					petProperties = null;
					Log.d(LOG_TAG, " Query Error: " + e.getMessage());
					listener.onAllPetPropertiesFinished(false, petProperties);
				}

			}

		});
	}

	public static void getBreed(final ParsePetListener listener,
			String specieName) {
		ParseQuery<ParseObject> innerQuery = new ParseQuery<ParseObject>(
				SPECIE_TABLE);
		innerQuery.whereEqualTo(NAME_TAG, specieName);
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(BREED_TABLE);
		query.whereMatchesQuery(SPECIE_ID_TAG, innerQuery);

		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> cList, ParseException e) {
				final ArrayList<String> breed;
				if (e == null) {
					breed = new ArrayList<String>();
					for (ParseObject object : cList) {
						breed.add(object.getString(NAME_TAG));
					}

				} else {
					breed = null;
					Log.d(LOG_TAG, " Query Error: " + e.getMessage());
				}

				listener.onBreedQueryFinished(breed);
			}

		});
	}

	public static void getPet(final ParsePetListener listener, String systemId,
			Calendar updatedAt) {

		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(PET_TABLE);
		query.whereGreaterThan(UPDATED_AT_TAG, updatedAt);
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

		pet.setName(parsedPet.getString(NAME_TAG));

		if (parsedPet.getBoolean(PUPPY_TAG)) {
			pet.setPuppy(Pet.AGE_PUPPY);
		} else {
			pet.setPuppy(Pet.AGE_ADULT);
		}

		if (parsedPet.getBoolean(AGRESSIVE_TAG)) {
			pet.setAgressive(Pet.AGRESSIVE);
		} else {
			pet.setAgressive(Pet.NOT_AGRESSIVE);
		}

		pet.setComment(parsedPet.getString(COMMENT_TAG));
		String gender = parsedPet.getString(GENDER_TAG);
		if (gender.equals(SEX_MALE)) {
			pet.setGender(Pet.GENDER_MALE);
		} else if (gender.equals(SEX_FEMALE)) {
			pet.setGender(Pet.GENDER_FEMALE);
		}
		pet.setSystem_id(parsedPet.getObjectId());
		Calendar Updated_At = Calendar.getInstance();
		Updated_At.setTimeInMillis(parsedPet.getUpdatedAt().getTime());
		pet.setUpdated_at(Updated_At);
		Breed breed = new Breed();
		breed.setSystem_id(parsedPet.getString(BREED_ID_TAG));
		pet.setBreed(breed);
		
		pet.setActive(parsedPet.getBoolean(ACTIVE_TAG));

		return pet;

	}

	public static void updatePet(final ParsePetListener listener, final Pet pet) {

		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(PET_TABLE);

		// Retrieve the object by id
		query.getInBackground(pet.getSystem_id(),
				new GetCallback<ParseObject>() {
					public void done(ParseObject parsedPet, ParseException e) {
						if (e == null) {

							parsedPet.put(NAME_TAG, pet.getName());
							int gender = pet.getGender();
							if (gender == Pet.GENDER_MALE) {
								parsedPet.put(GENDER_TAG, SEX_MALE);
							} else if (gender == Pet.GENDER_FEMALE) {
								parsedPet.put(GENDER_TAG, SEX_FEMALE);
							}
							parsedPet.put(COMMENT_TAG, pet.getComment());
							parsedPet.put(BREED_ID_TAG, pet.getBreed()
									.getSystem_id());
							if (pet.getPuppy() == Pet.AGE_PUPPY) {
								parsedPet.put(PUPPY_TAG, true);
							} else {
								parsedPet.put(PUPPY_TAG, false);
							}

							if (pet.getAgressive() == Pet.AGRESSIVE) {
								parsedPet.put(AGRESSIVE_TAG, true);
							} else {
								parsedPet.put(AGRESSIVE_TAG, false);
							}

							parsedPet.saveInBackground();

							listener.OnPetUpdateFinished(true);

						} else
							listener.OnPetUpdateFinished(false);

					}
				});

	}

	private void cleanBreeds(String breedName, final ParseObject parsedPet) {
		final ParseRelation<ParseObject> breedrelation = parsedPet
				.getRelation(BREED_ID_TAG);
		ParseQuery<ParseObject> query = breedrelation.getQuery();
		query.whereNotEqualTo(NAME_TAG, breedName);
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
							"Breed Relations not found: " + e.getMessage());
				}

			}
		});

	}

	public static void updateClientPets(final ParsePetListener listener,
			final User user, Context context) {

		
		Date lastUpdate = PetProvider.getLastUpdate(context);

		String systemId = user.getSystemId();
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(PET_TABLE);

		if (lastUpdate == null) {
			lastUpdate = new Date(0);
		}

		query.whereEqualTo(USER_ID_TAG, systemId);
		query.whereGreaterThan(UPDATED_AT_TAG, lastUpdate);
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

		final ParseObject parsePet = new ParseObject(PET_TABLE);
		parsePet.put(NAME_TAG, auxPet.getName());
		parsePet.put(USER_ID_TAG, auxPet.getOwner().getSystemId());
		parsePet.put(BREED_ID_TAG, auxPet.getBreed().getSystem_id());

		int gender = auxPet.getGender();
		if (gender == Pet.GENDER_MALE) {
			parsePet.put(GENDER_TAG, SEX_MALE);
		} else if (gender == Pet.GENDER_FEMALE) {
			parsePet.put(GENDER_TAG, SEX_FEMALE);
		}
		int agressive = auxPet.getAgressive();
		if(agressive == Pet.AGRESSIVE) {
			parsePet.put(AGRESSIVE_TAG, true);
		} else if (agressive == Pet.NOT_AGRESSIVE) {
			parsePet.put(AGRESSIVE_TAG, false);
		}

		if (auxPet.getPuppy() == Pet.AGE_PUPPY) {
			parsePet.put(PUPPY_TAG, true);
		} else {
			parsePet.put(PUPPY_TAG, false);
		}
		parsePet.put(COMMENT_TAG, auxPet.getComment());
		parsePet.put(ACTIVE_TAG, true);

		parsePet.saveInBackground(new SaveCallback() {

			@Override
			public void done(ParseException e) {
				if (e == null) {
					listener.onPetInserted(parsePet.getObjectId(), true);
				}else{
					Log.d(LOG_TAG, "error inserting pet: "+e.getMessage());
					listener.onPetInserted(parsePet.getObjectId(), false);
					
				}
				
			}

		});

	}

	public static void updateAllBreeds(Context context,
			final ParsePetListener listener) {

		Date lastUpdate = BreedProvider.getLastUpdate(context);
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(BREED_TABLE);
		if (lastUpdate != null) {
			query.whereGreaterThan(UPDATED_AT_TAG, lastUpdate);
		}
		query.setLimit(BREED_QUERY_LIMIT);
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> cList, ParseException e) {

				if (e == null) {
					ArrayList<Breed> breeds = new ArrayList<Breed>();
					for (ParseObject object : cList) {
						Breed breed = new Breed();
						breed.setName(object.getString(NAME_TAG));
						breed.setSystem_id(object.getObjectId());

						PetPropertie petPropertie = new PetPropertie();
						petPropertie.setSystem_id(object
								.getString(PET_PROPERTIE_ID_TAG));
						breed.setPetPropertie(petPropertie);
						Calendar Updated_At = Calendar.getInstance();
						Updated_At.setTimeInMillis(object.getUpdatedAt()
								.getTime());
						breed.setUpdated_at(Updated_At);

						Specie specie = new Specie();
						specie.setSystem_id(object.getString(SPECIE_ID_TAG));
						breed.setSpecie(specie);

						breeds.add(breed);
					}

					listener.onAllBreedsQueryFinished(true, breeds);

				} else {
					listener.onAllBreedsQueryFinished(true, null);
					Log.d(LOG_TAG,
							" Query error updating PetProperties: "
									+ e.getMessage());
				}

			}

		});

	}

	public static void updateAllSpecies(Context context,
			final ParsePetListener listener) {
		Date lastUpdate = SpecieProvider.getLastUpdate(context);

		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
				SPECIE_TABLE);

		if (lastUpdate != null) {
			query.whereGreaterThan(UPDATED_AT_TAG, lastUpdate);
		}

		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> cList, ParseException e) {

				if (e == null) {
					ArrayList<Specie> species = new ArrayList<Specie>();

					for (ParseObject object : cList) {

						Specie specie = new Specie();
						specie.setName(object.getString(NAME_TAG));
						specie.setSystem_id(object.getObjectId());
						Calendar Updated_At = Calendar.getInstance();
						Updated_At.setTimeInMillis(object.getUpdatedAt()
								.getTime());
						specie.setUpdated_at(Updated_At);
						species.add(specie);

					}

					listener.onAllSpeciesQueryFinished(true, species);

				} else {
					listener.onAllSpeciesQueryFinished(true, null);
					Log.d(LOG_TAG,
							" Query error updating Services: " + e.getMessage());
				}

			}

		});
	}

	public static void updateAllPetProperties(Context context,
			final ParsePetListener listener) {
		Date lastUpdate = PetPropertieProvider.getLastUpdate(context);
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
				PET_PROPERTIES_TABLE);

		if (lastUpdate != null) {
			query.whereGreaterThan(UPDATED_AT_TAG, lastUpdate);
		}

		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> cList, ParseException e) {

				if (e == null) {
					ArrayList<PetPropertie> petProperties = new ArrayList<PetPropertie>();

					for (ParseObject object : cList) {
						PetPropertie petPropertie = new PetPropertie();
						petPropertie.setName(object.getString(NAME_TAG));
						petPropertie.setSystem_id(object.getObjectId());
						Calendar Updated_At = Calendar.getInstance();
						Updated_At.setTimeInMillis(object.getUpdatedAt()
								.getTime());
						petPropertie.setUpdated_at(Updated_At);
						petProperties.add(petPropertie);
					}

					listener.onAllPetPropertiesFinished(true, petProperties);

				} else {
					listener.onAllPetPropertiesFinished(true, null);
					Log.d(LOG_TAG,
							" Query error updating PetProperties: "
									+ e.getMessage());
				}

			}

		});

	}

	public static void deletePet(Context context,
			final ParsePetListener listener, final Pet pet) {

		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
				PET_TABLE);
		query.whereEqualTo("objectId", pet.getSystem_id());
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> parsedPet, ParseException e) {
				if (e == null) {
					if (parsedPet.size() != 0) {
						parsedPet.get(0).put(ACTIVE_TAG, false);
						parsedPet.get(0).saveInBackground();
					}
					ParseRequestProvider.deleteRequestsByPet(pet);
					listener.onPetRemoveFinished(true);

				} else {
					Log.d(LOG_TAG,
							"Error getting pet to delete : "
									+ e.getMessage());
					listener.onPetRemoveFinished(false);

				}
			}
		});

	}

	public static void updateAllPets(final ParsePetListener listener, Context context) {

		Date lastUpdate = PetProvider.getLastUpdate(context);

		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(PET_TABLE);

		if (lastUpdate == null) {
			lastUpdate = new Date(0);
		}

		query.whereGreaterThan(UPDATED_AT_TAG, lastUpdate);
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> parsedPets, ParseException e) {
				ArrayList<Pet> pets = new ArrayList<Pet>();
				if (e == null) {
					for (ParseObject parseObject : parsedPets) {

						Pet pet = new Pet();
						pet = readPetfromParsedObject(parseObject, listener);
						
						User user = new User();
						user.setSystemId(parseObject.getString(USER_ID_TAG));
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
}
