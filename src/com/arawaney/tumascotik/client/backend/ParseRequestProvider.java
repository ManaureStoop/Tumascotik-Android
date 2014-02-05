package com.arawaney.tumascotik.client.backend;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.arawaney.tumascotik.client.control.MainController;
import com.arawaney.tumascotik.client.db.provider.PetProvider;
import com.arawaney.tumascotik.client.db.provider.RequestProvider;
import com.arawaney.tumascotik.client.listener.ParseRequestListener;
import com.arawaney.tumascotik.client.model.Pet;
import com.arawaney.tumascotik.client.model.Request;
import com.arawaney.tumascotik.client.model.Service;
import com.arawaney.tumascotik.client.util.CalendarUtil;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.SaveCallback;

public class ParseRequestProvider {
	private static final String LOG_TAG = "Tumascotik-Client-ParseProvider";

	private static final String INITIAL_DATE_TAG = "initialDate";
	private static final String END_DATE_TAG = "endDate";
	private static final String COMMENT_TAG = "comment";
	private static final String DELIVERY_TAG = "delivery";
	private static final String UPDATED_AT_TAG = "updatedAt";
	private static final String IS_APPOINTMENT_TAG = "isAppointment";
	private static final String ACTIVE_TAG = "active";
	private static final String PET_ID_TAG = "petId";
	private static final String STATUS_ID_TAG = "statusId";
	private static final String SERVICE_ID_TAG = "serviceId";
	private static final String REQUEST_ID_TAG = "requestId";
	private static final String PET_PROPERTIE_ID_TAG = "petPropertiesId";
	private static final String PRICE_TAG = "price";
	private static final String STATUS_NUMBER_TAG = "statusNumber";

	private static final String REQUEST_TABLE = "Request";
	private static final String STATUS_TABLE = "Status";
	private static final String REQUEST_SERVICE_TABLE = "Service_Request";
	private static final String PRICE_TABLE = "Price";		
	
	static Date[] initialScheduledDates = null;
	 static Date[] finalScheduledDates = null; 
	
	

	public static void sendRequest(Context context,
			final ParseRequestListener listener, final Request request) {
		final ParseObject parseRequest = new ParseObject(REQUEST_TABLE);

		Date startDate = request.getStart_date().getTime();
		Date finishDate = request.getFinish_date().getTime();

		if (request.getComment() != null) {
			parseRequest.put(COMMENT_TAG, request.getComment());
		}
		parseRequest.put(PET_ID_TAG, request.getPet().getSystem_id());

		parseRequest.put(SERVICE_ID_TAG, request.getService().getSystem_id());

		parseRequest.put(INITIAL_DATE_TAG, startDate);
		parseRequest.put(END_DATE_TAG, finishDate);
		if (request.isDelivery() == Request.IS_DELIVERY) {
			parseRequest.put(DELIVERY_TAG, true);
		} else
			parseRequest.put(DELIVERY_TAG, false);
		if (request.isActive() == Request.ACTIVE) {
			parseRequest.put(ACTIVE_TAG, true);
		} else
			parseRequest.put(ACTIVE_TAG, false);


		parseRequest.saveInBackground(new SaveCallback() {

			@Override
			public void done(ParseException e) {
				if (e == null) {
					listener.OnRequestInserted(true, parseRequest.getObjectId());
					saveNewRequestStatus();
					saveRequestService();

				} else {
					Log.d(LOG_TAG, "Error saving request: " + e.getMessage());
					listener.OnRequestInserted(false,
							parseRequest.getObjectId());
				}
			}

			private void saveNewRequestStatus() {

				int statusNUm = request.getStatus();
				ParseQuery<ParseObject> innerQuery = new ParseQuery<ParseObject>(
						STATUS_TABLE);
				innerQuery.whereEqualTo(STATUS_NUMBER_TAG, statusNUm);
				innerQuery.findInBackground(new FindCallback<ParseObject>() {

					@Override
					public void done(List<ParseObject> status, ParseException e) {
						if (e == null) {
							parseRequest.put(STATUS_ID_TAG, status.get(0)
									.getObjectId());
							parseRequest.saveInBackground();

						} else {
							Log.e(LOG_TAG,
									"No status matches inserting new pet "
											+ e.getMessage());

						}

					}
				});

			}

			private void saveRequestService() {
				final ParseObject parseRequestService = new ParseObject(
						REQUEST_SERVICE_TABLE);
				parseRequestService.put(SERVICE_ID_TAG, request.getService().getSystem_id());
				parseRequestService.put(REQUEST_ID_TAG,
						parseRequest.getObjectId());

				ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
						PRICE_TABLE);
				query.whereEqualTo(SERVICE_ID_TAG, request.getService().getSystem_id());
				query.whereEqualTo(PET_PROPERTIE_ID_TAG, request.getPet()
						.getBreed().getPetPropertie().getSystem_id());

				// Log.d(LOG_TAG, "serviceid: "+ request.getServiceID() +
				// " petprotid: "+request.getPet().getBreed().getPetPropertie().getSystem_id());

				query.findInBackground(new FindCallback<ParseObject>() {

					@Override
					public void done(List<ParseObject> prices, ParseException e) {
						if (e == null) {
							if (!prices.isEmpty()) {
								parseRequestService.put(PRICE_TAG, prices
										.get(0).get(PRICE_TAG));
							}

						} else {
							e.printStackTrace();
						}

						parseRequestService.saveInBackground();

					}
				});

			}

		});
	}

	public static void updateRequests(Context context,
			final ParseRequestListener listener) {
		Date updateAt = RequestProvider.getLastUpdate(context);

		ArrayList<Pet> pets = PetProvider.readPets(context);

		if (pets != null) {
			for (final Pet pet : pets) {

				ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
						REQUEST_TABLE);
				query.whereEqualTo(PET_ID_TAG, pet.getSystem_id());
				if (updateAt != null) {
					query.whereGreaterThan(UPDATED_AT_TAG, updateAt);
				}
				query.findInBackground(new FindCallback<ParseObject>() {

					@Override
					public void done(List<ParseObject> cList, ParseException e) {

						if (e == null) {
							ArrayList<Request> requests = new ArrayList<Request>();

							for (ParseObject object : cList) {
								Request request = readRequestFromCursor(object,
										pet, listener);
								request.setSystem_id(object.getObjectId());
								requests.add(request);
							}

							listener.OnAllRequestsQueryFinished(requests);

						} else {
							listener.OnAllRequestsQueryFinished(null);
							Log.d(LOG_TAG,
									" Query error getting Requests from "
											+ pet.getName() + ": "
											+ e.getMessage());
						}

					}

					private Request readRequestFromCursor(
							ParseObject parsedRequest, Pet pet,
							final ParseRequestListener listener) {
						final Request request = new Request();

						if (parsedRequest.getBoolean(ACTIVE_TAG)) {
							request.setActive(Request.ACTIVE);
						} else
							request.setActive(Request.INACTIVE);

						if (parsedRequest.getBoolean(DELIVERY_TAG)) {
							request.setDelivery(Request.IS_DELIVERY);
						} else
							request.setDelivery(Request.IS__NOT_DELIVERY);

						if (parsedRequest.getString(COMMENT_TAG) != null) {
							request.setComment(parsedRequest
									.getString(COMMENT_TAG));
						}

						Calendar startDate = Calendar.getInstance();
						startDate.setTimeInMillis(parsedRequest.getDate(
								INITIAL_DATE_TAG).getTime());
						request.setStart_date(startDate);

						Calendar finishDate = Calendar.getInstance();
						finishDate.setTimeInMillis(parsedRequest.getDate(
								END_DATE_TAG).getTime());
						request.setFinish_date(finishDate);

						Calendar updateCalendar = Calendar.getInstance();

						updateCalendar.setTimeInMillis(parsedRequest
								.getUpdatedAt().getTime());

						request.setUpdated_at(updateCalendar);

						request.setPet(pet);

						request.setSystem_id(parsedRequest.getObjectId());
						Service service = new Service();
						service.setSystem_id(parsedRequest
								.getString(SERVICE_ID_TAG));
						request.setService(service);

						String statusId = parsedRequest
								.getString(STATUS_ID_TAG);

						ParseQuery<ParseObject> statusQuery = new ParseQuery<ParseObject>(
								STATUS_TABLE);
						statusQuery.whereEqualTo("objectId", statusId);

						statusQuery
								.findInBackground(new FindCallback<ParseObject>() {

									@Override
									public void done(List<ParseObject> status,
											ParseException e) {
										if (e == null) {
											request.setStatus(status.get(0)
													.getInt(STATUS_NUMBER_TAG));
											listener.onRequestQueryFInished(request);
										} else {
											Log.d(LOG_TAG,
													"Error getting status from request: "
															+ request
																	.getSystem_id()
															+ " "
															+ e.getMessage());
										}
									}
								});
						ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
								REQUEST_SERVICE_TABLE);
						query.whereEqualTo(REQUEST_ID_TAG,
								parsedRequest.getObjectId());
						query.findInBackground(new FindCallback<ParseObject>() {

							@Override
							public void done(List<ParseObject> serviceRequests,
									ParseException e) {
								if (e == null) {
									if (serviceRequests.size() > 0) {
										request.setPrice(serviceRequests.get(0)
												.getInt(PRICE_TAG));
									} else {
										request.setPrice(0);
									}

									listener.onRequestQueryFInished(request);

								} else
									Log.d(LOG_TAG,
											"Error getting Service_Requests from request: "
													+ request.getSystem_id()
													+ " " + e.getMessage());

							}
						});

						return request;
					}

				});

			}

		}

	}

	public static void cancelRequest(Context context,
			final ParseRequestListener listener, Request request) {

		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
				REQUEST_TABLE);
		query.whereEqualTo("objectId", request.getSystem_id());
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> parsedRequest, ParseException e) {
				if (e == null) {
					parsedRequest.get(0).put(STATUS_ID_TAG,
							Request.STATUS_CANCELED);
					parsedRequest.get(0).saveInBackground();
					listener.onCanceledQueryFinished(true);
				} else {
					Log.d(LOG_TAG,
							"Error getting request to cancel : "
									+ e.getMessage());
					listener.onCanceledQueryFinished(false);

				}
			}
		});

	}

	public static void removeRequest(final Request request,
			final ParseRequestListener listener) {

		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
				REQUEST_TABLE);
		query.whereEqualTo("objectId", request.getSystem_id());
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> parsedRequest, ParseException e) {
				if (e == null) {
					try {

						parsedRequest.get(0).delete();
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
							REQUEST_SERVICE_TABLE);
					query.whereMatches(REQUEST_ID_TAG, request.getSystem_id());
					query.findInBackground(new FindCallback<ParseObject>() {

						@Override
						public void done(List<ParseObject> list,
								ParseException arg1) {
							try {
								list.get(0).delete();
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					});
					Log.d(LOG_TAG, "HOOLA");
					listener.onRequestRemoveFinished(request);

				} else {
					Log.d(LOG_TAG,
							"Error getting request to delete : "
									+ e.getMessage());

				}
			}
		});

	}

	public static void geRequestByDay(Date dayInitialDate, Date dayFinalDate,
			final ParseRequestListener listener) {


		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Request");
		query.whereGreaterThan(INITIAL_DATE_TAG, dayInitialDate);
		query.whereLessThan(END_DATE_TAG, dayFinalDate);
		query.whereEqualTo(IS_APPOINTMENT_TAG, true);
		query.whereEqualTo(ACTIVE_TAG, true);
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> cList, ParseException e) {

				if (e == null) {
					int i;
					ParseObject parseobject;

					int numberOfScheduledAppointments = cList.size();

					if (numberOfScheduledAppointments > 0) {
						initialScheduledDates = new Date[numberOfScheduledAppointments];
						finalScheduledDates = new Date[numberOfScheduledAppointments];
						for (i = 0; i < numberOfScheduledAppointments; i++) {
							parseobject = cList.get(i);
							initialScheduledDates[i] = parseobject
									.getDate(INITIAL_DATE_TAG);
							finalScheduledDates[i] = parseobject
									.getDate(END_DATE_TAG);

						}
					}
					 for (i = 0; i < numberOfScheduledAppointments; i++) {
					 Log.d("TEST", String.valueOf(initialScheduledDates[i]
					 .toString()));
					 Log.d("TEST", String.valueOf(finalScheduledDates[i]
					 .toString()));

					 }
					
					listener.onDayRequestsQueryFinished(initialScheduledDates,
							finalScheduledDates);
				
				} else {
					Log.d(LOG_TAG,
							"Error loading scheduled time blocks: "
									+ e.getMessage());
				}
			}
		});

	}

}
