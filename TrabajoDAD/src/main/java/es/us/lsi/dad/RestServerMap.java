package es.us.lsi.dad;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class RestServerMap extends AbstractVerticle {

	private Map<Integer, SensorEntity> sensors = new HashMap<Integer, SensorEntity>();
	private Map<Integer, ActuadorEntity> actuators = new HashMap<Integer, ActuadorEntity>();
	private Gson gson;

	public void start(Promise<Void> startFuture) {
		// Creating some synthetic data
		createSomeData(6);

		// Instantiating a Gson serialize object using specific date format
		gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

		// Defining the router object
		Router router = Router.router(vertx);

		// Handling any server startup result
		vertx.createHttpServer().requestHandler(router::handle).listen(8081, result -> {
			if (result.succeeded()) {
				startFuture.complete();
			} else {
				startFuture.fail(result.cause());
			}
		});

		// Defining URI paths for each method in RESTful interface, including body
		// handling by /api/sensors* or /api/sensors/*
		router.route("/api/sensors*").handler(BodyHandler.create());
		router.get("/api/sensors").handler(this::getAllSensors);
		router.get("/api/sensors/:sensorid").handler(this::getOneSensor);
		router.post("/api/sensors").handler(this::addOneSensor);
		router.delete("/api/sensors/:sensorid").handler(this::deleteOneSensor);
		router.put("/api/sensors/:sensorid").handler(this::updateSensor);
		router.get("/api/sensors/getLastUpdateSensor").handler(this::getLastUpdateSensor);
		router.get("/api/sensors/getSensorsByGroup/:idGroup").handler(this::getSensorsByGroup);
		router.get("/api/sensors/getLastUpdateSensorByGroup/:idGroup").handler(this::getLastUpdateSensorByGroup);
		
		router.route("/api/actuators*").handler(BodyHandler.create());
		router.get("/api/actuators").handler(this::getAllActuators);
		router.get("/api/actuators/:actuatorid").handler(this::getOneActuator);
		router.post("/api/actuators").handler(this::addOneActuator);
		router.delete("/api/actuators/:actuatorid").handler(this::deleteOneActuator);
		router.put("/api/actuators/:actuatorid").handler(this::putOneActuator);
		router.get("/api/actuators/getLastUpdateActuator").handler(this::getLastUpdateActuator);
		router.get("/api/actuators/getActuatorsByGroup/:idGroup").handler(this::getActuatorsByGroup);
		router.get("/api/actuators/getLastUpdateActuatorByGroup/:idGroup").handler(this::getLastUpdateActuatorByGroup);
	}

	@SuppressWarnings("unused")
	private void getAllSensors(RoutingContext routingContext) {
		routingContext.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(200)
				.end(gson.toJson(sensors.values()));
	}
	
	private void getAllActuators(RoutingContext routingContext) {
		routingContext.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(200)
				.end(gson.toJson(actuators.values()));
	}
	
	private void getOneSensor(RoutingContext routingContext) {
		int id = 0;
		try {
			id= Integer.valueOf(routingContext.request().getParam("sensorid"));

			if (sensors.containsKey(id)) {
				SensorEntity ds = sensors.get(id);
				routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
						.setStatusCode(200).end(ds != null ? gson.toJson(ds) : "");
			} else {
				routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
						.setStatusCode(204).end();
			}
		} catch (Exception e) {
			routingContext.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(204)
					.end();
		}
	}
	
	private void getOneActuator(RoutingContext routingContext) {
		int id = Integer.parseInt(routingContext.request().getParam("actuatorid"));
		if (actuators.containsKey(id)) {
			ActuadorEntity ds = actuators.get(id);
			routingContext.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(200)
					.end(gson.toJson(ds));
		} else {
			routingContext.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(204)
					.end();
		}
	}

	private void addOneSensor(RoutingContext routingContext) {
		final SensorEntity sensor = gson.fromJson(routingContext.getBodyAsString(), SensorEntity.class);
		sensors.put(sensor.getIdSensors(), sensor);
		routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
				.end(gson.toJson(sensor));
	}
	
	private void addOneActuator(RoutingContext routingContext) {
		final ActuadorEntity actuator = gson.fromJson(routingContext.getBodyAsString(), ActuadorEntity.class);
		actuators.put(actuator.getIdActuators(), actuator);
		routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
				.end(gson.toJson(actuator));
	}

	private void deleteOneSensor(RoutingContext routingContext) {
		int id = Integer.parseInt(routingContext.request().getParam("sensorid"));
		if (sensors.containsKey(id)) {
			SensorEntity user = sensors.get(id);
			sensors.remove(id);
			routingContext.response().setStatusCode(200).putHeader("content-type", "application/json; charset=utf-8")
					.end(gson.toJson(user));
		} else {
			routingContext.response().setStatusCode(204).putHeader("content-type", "application/json; charset=utf-8")
					.end();
		}
	}
	
	private void deleteOneActuator(RoutingContext routingContext) {
		int id = Integer.parseInt(routingContext.request().getParam("actuatorid"));
		if (actuators.containsKey(id)) {
			ActuadorEntity user = actuators.get(id);
			actuators.remove(id);
			routingContext.response().setStatusCode(200).putHeader("content-type", "application/json; charset=utf-8")
					.end(gson.toJson(user));
		} else {
			routingContext.response().setStatusCode(204).putHeader("content-type", "application/json; charset=utf-8")
					.end();
		}
	}

	private void updateSensor(RoutingContext routingContext) {
		int id = Integer.parseInt(routingContext.request().getParam("sensorid"));
		SensorEntity ds = sensors.get(id);
		final SensorEntity element = gson.fromJson(routingContext.getBodyAsString(), SensorEntity.class);
		ds.setTimeStamp(element.getTimeStamp());
		ds.setIsBedroom(element.getIsBedroom());
		ds.setTemperatura(element.getTemperatura());
		ds.setIdPlaca(element.getIdPlaca());		
		ds.setIdGroup(element.getIdGroup());
		sensors.put(ds.getIdSensors(), ds);
		routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
				.end(gson.toJson(element));
	}
	
	private void putOneActuator(RoutingContext routingContext) {
		int id = Integer.parseInt(routingContext.request().getParam("actuatorid"));
		ActuadorEntity ds = actuators.get(id);
		final ActuadorEntity element = gson.fromJson(routingContext.getBodyAsString(), ActuadorEntity.class);
		ds.setTimeStamp(element.getTimeStamp());
		ds.setEstado(element.getEstado());		
		actuators.put(ds.getIdActuators(), ds);
		routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
				.end(gson.toJson(element));
	}
	
	private void getLastUpdateSensor(RoutingContext routingContext) {
		Optional<SensorEntity> res = sensors.values().stream().max(Comparator.comparing(SensorEntity::getTimeStamp));
		String json = res.isPresent() ? gson.toJson(res.get()) : "";
		routingContext.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(200)
				.end(json);
	}
	
	private void getLastUpdateActuator(RoutingContext routingContext) {
		Optional<ActuadorEntity> res = actuators.values().stream().max(Comparator.comparing(ActuadorEntity::getTimeStamp));
		String json = res.isPresent() ? gson.toJson(res.get()) : "";
		routingContext.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(200)
				.end(json);
	}
	
	private void getSensorsByGroup(RoutingContext routingContext) {
		int id = Integer.parseInt(routingContext.request().getParam("idGroup"));
		
		routingContext.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(200)
		.end(gson.toJson(new SensorEntityListWrapper(sensors.values()).getsensorList().stream().filter(x->x.getIdGroup()==id).collect(Collectors.toList())));
	}
	
	private void getActuatorsByGroup(RoutingContext routingContext) {
		int id = Integer.parseInt(routingContext.request().getParam("idGroup"));
		
		routingContext.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(200)
		.end(gson.toJson(new ActuadorEntityListWrapper(actuators.values()).getactuatorList().stream().filter(x->x.getIdGroup()==id).collect(Collectors.toList())));
	}
	
	private void getLastUpdateSensorByGroup(RoutingContext routingContext) {
		Integer idgrupo = Integer.parseInt(routingContext.request().getParam("IdGroup"));
		Optional<SensorEntity> res = sensors.values().stream().filter(x->x.getIdGroup()==idgrupo).max(Comparator.comparing(SensorEntity::getTimeStamp));
		String json = res.isPresent() ? gson.toJson(res.get()) : "";
		routingContext.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(200)
		.end(gson.toJson(json));
	}
	
	private void getLastUpdateActuatorByGroup(RoutingContext routingContext) {
		Integer idgrupo = Integer.parseInt(routingContext.request().getParam("IdGroup"));
		Optional<ActuadorEntity> res = actuators.values().stream().filter(x->x.getIdGroup()==idgrupo).max(Comparator.comparing(ActuadorEntity::getTimeStamp));
		String json = res.isPresent() ? gson.toJson(res.get()) : "";
		routingContext.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(200)
		.end(gson.toJson(json));
	}
	
	private void createSomeData(int number) {
		Random rnd = new Random();
		
		IntStream.range(0, number).forEach(elem -> {
			int id = rnd.nextInt(50);
			int idgroup=1;
			if(id%2==0) {
				idgroup=2;
			}
			sensors.put(id, new SensorEntity(id,true,id,id,idgroup));
			actuators.put(id, new ActuadorEntity(id,false,idgroup));
		});
	}
	
	

}
