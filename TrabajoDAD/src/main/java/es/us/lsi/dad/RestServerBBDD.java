package es.us.lsi.dad;

import java.util.ArrayList;
//import java.util.Calendar;
import java.util.List;
//import java.util.Random;
//import java.util.stream.IntStream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
//import io.vertx.core.json.JsonArray;
//import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;

public class RestServerBBDD extends AbstractVerticle {

	private Gson gson;
	MySQLPool mySqlClient;
	MqttClient mqttClient;

	public void start(Promise<Void> startFuture) {
		// tengo que cambiar el puerto y el usuario y contraseña
		MySQLConnectOptions connectOptions = new MySQLConnectOptions().setPort(3306).setHost("localhost")
				.setDatabase("proyectodad").setUser("root").setPassword("root");

		PoolOptions poolOptions = new PoolOptions().setMaxSize(5);

		mySqlClient = MySQLPool.pool(vertx, connectOptions, poolOptions);

		// Instantiating a Gson serialize object using specific date format
		Router router = Router.router(vertx);
		gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

		vertx.createHttpServer().requestHandler(router::handle).listen(8081, result -> {
			if (result.succeeded()) {
				startFuture.complete();
			} else {
				startFuture.fail(result.cause());
			}
		});
		mqttClient = MqttClient.create(vertx, new MqttClientOptions().setAutoKeepAlive(true));
		mqttClient.connect(1883, "localhost", s -> {
		});	
		router.route("/api/sensors*").handler(BodyHandler.create());
		router.get("/api/sensors").handler(this::getAllSensors);
		router.get("/api/sensors/all").handler(this::getAllSensorsWithConnection);
		router.get("/api/sensors/:idSensors").handler(this::getSensorById);
		router.get("/api/sensors/:idSensors/last").handler(this::getLastSensorId);
		router.get("/api/sensors/lastByGroup/:idGroup").handler(this::getLastIdGroupSensor);
		router.post("/api/sensors").handler(this::addSensor);
		router.delete("/api/sensors/:idSensors").handler(this::deleteSensor);
		router.put("/api/sensors/:idSensors").handler(this::updateSensor);
		
		router.route("/api/actuators*").handler(BodyHandler.create());
		router.get("/api/actuators/all").handler(this::getAllActuators); 
		router.get("/api/actuators").handler(this::getAllActuatorsWithConnection);
		router.get("/api/actuators/:idActuators").handler(this::getActuadorById);
		router.get("/api/actuators/:idActuators/last").handler(this::getLastActuadorId);
		router.get("/api/actuators/lastByGroup/:idGroup").handler(this::getLastIdGroupActuador);
		router.post("/api/actuators").handler(this::addActuador);
		router.delete("/api/actuators/:idActuators").handler(this::deleteActuador);
		router.put("/api/actuators/:idActuators").handler(this::updateActuador);
		
	}

//    private Date localDateToDate(LocalDate localDate) {
//		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
//	}

	public void stop(Promise<Void> stopPromise) throws Exception {
		try {
			stopPromise.complete();
		} catch (Exception e) {
			stopPromise.fail(e);
		}
		super.stop(stopPromise);
	}

	// Sensor Endpoints
	private void getAllSensors(RoutingContext routingContext) {
		mySqlClient.query("SELECT * FROM sensors;").execute(res -> {
			if (res.succeeded()) {
				// Get the result set
				RowSet<Row> resultSet = res.result();
				List<List<Object>> result = new ArrayList<>();
				for (Row elem : resultSet) {
					List<Object> sensorData = new ArrayList<>();
					sensorData.add(elem.getInteger("idSensors"));
					sensorData.add(elem.getLong("timeStamp"));
					sensorData.add(elem.getBoolean("isBedroom"));
					sensorData.add(elem.getInteger("temperatura"));
					sensorData.add(elem.getInteger("idPlaca"));
					sensorData.add(elem.getInteger("idGroup"));
					result.add(sensorData);
				}
				routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
						.setStatusCode(200).end(result.toString());
			} else {
				routingContext.response().setStatusCode(500)
						.end("Error al obtener los sensores: " + res.cause().getMessage());
			}
		});
	}

	private void getAllSensorsWithConnection(RoutingContext routingContext) {
		mySqlClient.getConnection(connection -> {
			if (connection.succeeded()) {
				connection.result().query("SELECT * FROM sensorss;").execute(res -> {
					if (res.succeeded()) {
						// Get the result set
						RowSet<Row> resultSet = res.result();
						System.out.println(resultSet.size());
						List<SensorImpl> result = new ArrayList<>();
						for (Row elem : resultSet) {
							result.add(new SensorImpl(elem.getInteger("idSensors"), elem.getLong("timeStamp"),
									elem.getBoolean("isBedroom"), elem.getInteger("temperatura"), elem.getInteger("idPlaca"),
									elem.getInteger("idGroup")));
						}
						routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
								.setStatusCode(200).end(result.toString());
					} else {
						System.out.println("Error: " + res.cause().getLocalizedMessage());
						routingContext.response().setStatusCode(500).end("Error al obtener los sensores: " + res.cause().getMessage());
					}
					connection.result().close();
				});
			} else {
				System.out.println(connection.cause().toString());
				routingContext.response().setStatusCode(500).end("Error con la coenxión: " + connection.cause().getMessage());
			}
		});
	}

	private void getSensorById(RoutingContext routingContext) {
		mySqlClient.getConnection(connection -> {
			int idSensor = Integer.parseInt(routingContext.request().getParam("idSensors"));
			if (connection.succeeded()) {
				connection.result().preparedQuery("SELECT * FROM sensors WHERE idSensors = ?").execute(Tuple.of(idSensor),
						res -> {
							if (res.succeeded()) {
								// Get the result set
								RowSet<Row> resultSet = res.result();
								System.out.println(resultSet.size());
								List<SensorImpl> result = new ArrayList<>();
								for (Row elem : resultSet) {
									result.add(new SensorImpl(elem.getInteger("idSensors"), elem.getLong("timeStamp"),
											elem.getBoolean("isBedroom"), elem.getInteger("temperatura"), elem.getInteger("idPlaca"),
											elem.getInteger("idGroup")));
								}
								routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
										.setStatusCode(200).end(result.toString());
							} else {
								System.out.println("Error: " + res.cause().getLocalizedMessage());
								routingContext.response().setStatusCode(500).end("Error al obtener los sensores: " + res.cause().getMessage());
							}
							connection.result().close();
						});
			} else {
				System.out.println(connection.cause().toString());
				routingContext.response().setStatusCode(500).end("Error con la coenxión: " + connection.cause().getMessage());
			}
		});
	}
	private void getLastSensorId(RoutingContext routingContext) {
	    Integer idSensor = Integer.parseInt(routingContext.request().getParam("idSensors"));
	    mySqlClient.getConnection(connection -> {
	        if (connection.succeeded()) {
	            connection.result().preparedQuery("SELECT * FROM sensors WHERE idSensors = ? ORDER BY timeStamp DESC LIMIT 1")
	                    .execute(Tuple.of(idSensor), res -> {
	                        if (res.succeeded()) {
	                            // Get the result set
	                            RowSet<Row> resultSet = res.result();
	                            List<SensorImpl> result = new ArrayList<>();
								for (Row elem : resultSet) {
									result.add(new SensorImpl(elem.getInteger("idSensors"), elem.getLong("timeStamp"),
											elem.getBoolean("isBedroom"), elem.getInteger("temperatura"), elem.getInteger("idPlaca"),
											elem.getInteger("idGroup")));
								}
				                routingContext.response()
		                        .putHeader("content-type", "application/json; charset=utf-8")
		                        .setStatusCode(200)
		                        .end(gson.toJson(result));
	                        } else {
	                            System.out.println("Error: " + res.cause().getLocalizedMessage());
	                            routingContext.response()
	                                    .setStatusCode(404)
	                                    .end("Error al obtener el sensor con idSensor " + idSensor + ": " + res.cause().getMessage());
	                        }
	                        connection.result().close();
	                    });
	        } else {
	            System.out.println(connection.cause().toString());
	            routingContext.response()
	                    .setStatusCode(500)
	                    .end("Error al conectar con la base de datos: " + connection.cause().getMessage());
	        }
	    });
	}

	private void getLastIdGroupSensor(RoutingContext routingContext) {
	    int idGroup = Integer.parseInt(routingContext.request().getParam("idGroup"));
	    mySqlClient.getConnection(connection -> {
	        if (connection.succeeded()) {
	            connection.result().preparedQuery("SELECT * FROM sensors WHERE idGroup = ? ORDER BY timestamp DESC LIMIT 1")
	                    .execute(Tuple.of(idGroup), res -> {
	                        if (res.succeeded()) {
	                            // Get the result set
	                            RowSet<Row> resultSet = res.result();
	                            List<SensorImpl> result = new ArrayList<>();
								for (Row elem : resultSet) {
									result.add(new SensorImpl(elem.getInteger("idSensors"), elem.getLong("timeStamp"),
											elem.getBoolean("isBedroom"), elem.getInteger("temperatura"), elem.getInteger("idPlaca"),
											elem.getInteger("idGroup")));
								}
				                routingContext.response()
		                        .putHeader("content-type", "application/json; charset=utf-8")
		                        .setStatusCode(200)
		                        .end(gson.toJson(result));
	                        } else {
	                            System.out.println("Error: " + res.cause().getLocalizedMessage());
	                            routingContext.response()
	                                    .setStatusCode(404)
	                                    .end("Error al obtener el sensor con idGroup " + idGroup + ": " + res.cause().getMessage());
	                        }
	                        connection.result().close();
	                    });
	        } else {
	            System.out.println(connection.cause().toString());
	            routingContext.response()
	                    .setStatusCode(500)
	                    .end("Error al conectar con la base de datos: " + connection.cause().getMessage());
	        }
	    });
	}
	private void addSensor(RoutingContext routingContext) {

	    // Parseamos el cuerpo de la solicitud HTTP a un objeto Sensor_humedad_Entity
	    final SensorImpl sensor = gson.fromJson(routingContext.getBodyAsString(),
	            SensorImpl.class);

	    // Ejecutamos la inserción en la base de datos MySQL
	    mySqlClient
	            .preparedQuery(
	                    "INSERT INTO sensors (idSensors, timeStamp, temperatura, isBedroom, idPlaca, idGroup) VALUES (?, ?, ?, ?, ?, ?)")
	            .execute((Tuple.of(sensor.getidSensors(), sensor.getTimeStamp(), sensor.gettemperatura(), sensor.getIsBedroom(),
	                    sensor.getidPlaca(), sensor.getIdGroup())), res -> {
	                        if (res.succeeded()) {
	                            // Si la inserción es exitosa, respondemos con el sensor creado
	                            routingContext.response().setStatusCode(201).putHeader("content-type",
	                                    "application/json; charset=utf-8").end("Sensor añadido correctamente");

	                            /*
	                            // Publicar en MQTT después de la inserción exitosa
	                            if (sensor.getTemperatura() > 30) {
	                                mqttClient.publish(sensor.getIdGroup() + "",
	                                        Buffer.buffer("ON"), MqttQoS.AT_LEAST_ONCE, false, false);
	                            } else {
	                                mqttClient.publish(sensor.getIdGroup() + "",
	                                        Buffer.buffer("OFF"), MqttQoS.AT_LEAST_ONCE, false, false);
	                            }
	                            */
	                        } else {
	                            // Si hay un error en la inserción, respondemos con el mensaje de error
	                            System.out.println("Error: " + res.cause().getLocalizedMessage());
	                            routingContext.response().setStatusCode(500).end("Error al añadir el sensor: " + res.cause().getMessage());
	                        }
	                    });

	}


	private void deleteSensor(RoutingContext routingContext) {
		mySqlClient.getConnection(connection -> {
			int idSensor = Integer.parseInt(routingContext.request().getParam("idSensors"));
			if (connection.succeeded()) {
				connection.result().preparedQuery("DELETE FROM sensors WHERE idSensors = ?").execute(Tuple.of(idSensor),
						res -> {
							if (res.succeeded()) {
								// Get the result set
								RowSet<Row> resultSet = res.result();
								System.out.println(resultSet.size());
								List<SensorImpl> result = new ArrayList<>();
								for (Row elem : resultSet) {
									result.add(new SensorImpl(elem.getInteger("idSensors"), elem.getLong("timeStamp"),
											elem.getBoolean("isBedroom"), elem.getInteger("temperatura"), elem.getInteger("idPlaca"),
											elem.getInteger("idGroup")));
								}
								routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
										.setStatusCode(200).end(result.toString());

							} else {
								System.out.println("Error: " + res.cause().getLocalizedMessage());
								routingContext.response().setStatusCode(500).end("Error al eliminar el sensor: " + res.cause().getMessage());
							}
							connection.result().close();
						});
			} else {
				System.out.println(connection.cause().toString());
				routingContext.response().setStatusCode(500).end("Error con la coenxión: " + connection.cause().getMessage());
			}
		});
	}

	private void updateSensor(RoutingContext routingContext) {
		// Obtenemos el ID del sensor de los parámetros de la solicitud HTTP
		int idSensor = Integer.parseInt(routingContext.request().getParam("idSensor"));

		// Obtenemos el sensor actualizado del cuerpo de la solicitud HTTP
		final SensorImpl updatedSensor = gson.fromJson(routingContext.getBodyAsString(),
				SensorImpl.class);

		// Ejecutamos la actualización en la base de datos MySQL
		mySqlClient
				.preparedQuery(
						"UPDATE sensor SET idSensors = ?, timeStamp = ?, isBedroom = ?, idPlaca = ?, idGroup = ? WHERE idSensors = ?")
				.execute((Tuple.of(updatedSensor.getidSensors(), updatedSensor.getTimeStamp(), updatedSensor.gettemperatura(),
						updatedSensor.getIsBedroom(), updatedSensor.getidPlaca(), updatedSensor.getIdGroup(), idSensor)), res -> {
							if (res.succeeded()) {
								// Si la actualización es exitosa, respondemos con el sensor actualizado
								if (res.result().rowCount() > 0) {
									routingContext.response().setStatusCode(200)
											.putHeader("content-type", "application/json; charset=utf-8")
											.end(gson.toJson(updatedSensor));
								}
							} else {
								// Si hay un error en la actualización, respondemos con el código 500 (Error
								// interno del servidor)
								System.out.println("Error: " + res.cause().getLocalizedMessage());
								routingContext.response().setStatusCode(500).end("Error al actualizar el sensor: " + res.cause().getMessage());
							}
						});
	}

	// Actuador Endpoints

	private void getAllActuators(RoutingContext routingContext) {
		// RoutingContext routingContext PARAMETRO DE LA FUNCION
//			routingContext.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(200)
//					.end(gson.toJson(new UserEntityListWrapper(users.values())));
		mySqlClient.query("SELECT * FROM actuators;").execute(res -> {
			if (res.succeeded()) {
				// Get the result set
				RowSet<Row> resultSet = res.result();
				System.out.println(resultSet.size());
				List<ActuatorImpl> result = new ArrayList<>();
				for (Row elem : resultSet) {
					result.add(new ActuatorImpl(elem.getInteger("idActuators"), elem.getLong("timeStamp"),
							elem.getBoolean("estado"), elem.getInteger("idPlaca"), elem.getInteger("idGroup")));
				}
				routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
						.setStatusCode(200).end(result.toString());
			} else {
				System.out.println("Error: " + res.cause().getLocalizedMessage());
				routingContext.response().setStatusCode(500).end("Error al obtener los actuadores: " + res.cause().getMessage());
			}
		});
	}

	private void getAllActuatorsWithConnection(RoutingContext routingContext) {
		mySqlClient.getConnection(connection -> {
			if (connection.succeeded()) {
				connection.result().query("SELECT * FROM actuators;").execute(res -> {
					if (res.succeeded()) {
						// Get the result set
						RowSet<Row> resultSet = res.result();
						System.out.println(resultSet.size());
						List<ActuatorImpl> result = new ArrayList<>();
						for (Row elem : resultSet) {
							result.add(new ActuatorImpl(elem.getInteger("idActuators"), elem.getLong("timeStamp"),
									elem.getBoolean("estado"), elem.getInteger("idPlaca"), elem.getInteger("idGroup")));
						}
						routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
								.setStatusCode(200).end(result.toString());
					} else {
						System.out.println("Error: " + res.cause().getLocalizedMessage());
						routingContext.response().setStatusCode(500).end("Error al obtener los actuadores: " + res.cause().getMessage());
					}
					connection.result().close();
				});
			} else {
				System.out.println(connection.cause().toString());
				routingContext.response().setStatusCode(500).end("Error con la conexión: " + connection.cause().getMessage());
			}
		});
	}

	private void getActuadorById(RoutingContext routingContext) {
		mySqlClient.getConnection(connection -> {
			int idActuador = Integer.parseInt(routingContext.request().getParam("idActuators"));
			if (connection.succeeded()) {
				connection.result().preparedQuery("SELECT * FROM actuators WHERE idActuators = ?")
						.execute(Tuple.of(idActuador), res -> {
							if (res.succeeded()) {
								// Get the result set
								RowSet<Row> resultSet = res.result();
								System.out.println(resultSet.size());
								List<ActuatorImpl> result = new ArrayList<>();
								for (Row elem : resultSet) {
									result.add(new ActuatorImpl(elem.getInteger("idActuators"), elem.getLong("timeStamp"),
											elem.getBoolean("estado"), elem.getInteger("idPlaca"), elem.getInteger("idGroup")));
								}
								routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
										.setStatusCode(200).end(result.toString());
							} else {
								System.out.println("Error: " + res.cause().getLocalizedMessage());
								routingContext.response().setStatusCode(500).end("Error al obtener el actuador: " + res.cause().getMessage());
							}
							connection.result().close();
						});
			} else {
				System.out.println(connection.cause().toString());
				routingContext.response().setStatusCode(500).end("Error con la coenxión: " + connection.cause().getMessage());
			}
		});
	}
	private void getLastActuadorId(RoutingContext routingContext) {
	    Integer idSensor = Integer.parseInt(routingContext.request().getParam("idActuators"));
	    mySqlClient.getConnection(connection -> {
	        if (connection.succeeded()) {
	            connection.result().preparedQuery("SELECT * FROM actuators WHERE idActuators = ? ORDER BY timeStamp DESC LIMIT 1")
	                    .execute(Tuple.of(idSensor), res -> {
	                        if (res.succeeded()) {
	                            // Get the result set
	                            RowSet<Row> resultSet = res.result();
	                            List<ActuatorImpl> result = new ArrayList<>();
								for (Row elem : resultSet) {
									result.add(new ActuatorImpl(elem.getInteger("idActuators"), elem.getLong("timeStamp"),
											elem.getBoolean("estado"), elem.getInteger("idPlaca"), elem.getInteger("idGroup")));
								}
				                routingContext.response()
		                        .putHeader("content-type", "application/json; charset=utf-8")
		                        .setStatusCode(200)
		                        .end(gson.toJson(result));
	                        } else {
	                            System.out.println("Error: " + res.cause().getLocalizedMessage());
	                            routingContext.response()
	                                    .setStatusCode(404)
	                                    .end("Error al obtener el actuador con idActuador " + idSensor + ": " + res.cause().getMessage());
	                        }
	                        connection.result().close();
	                    });
	        } else {
	            System.out.println(connection.cause().toString());
	            routingContext.response()
	                    .setStatusCode(500)
	                    .end("Error al conectar con la base de datos: " + connection.cause().getMessage());
	        }
	    });
	}

	private void getLastIdGroupActuador(RoutingContext routingContext) {
	    int idGroup = Integer.parseInt(routingContext.request().getParam("idGroup"));
	    mySqlClient.getConnection(connection -> {
	        if (connection.succeeded()) {
	            connection.result().preparedQuery("SELECT * FROM actuators WHERE idGroup = ? ORDER BY timeStamp DESC LIMIT 1")
	                    .execute(Tuple.of(idGroup), res -> {
	                        if (res.succeeded()) {
	                            // Get the result set
	                            RowSet<Row> resultSet = res.result();
	                            List<ActuatorImpl> result = new ArrayList<>();
								for (Row elem : resultSet) {
									result.add(new ActuatorImpl(elem.getInteger("idActuators"), elem.getLong("timeStamp"),
											elem.getBoolean("estado"), elem.getInteger("idPlaca"), elem.getInteger("idGroup")));
								}
				                routingContext.response()
		                        .putHeader("content-type", "application/json; charset=utf-8")
		                        .setStatusCode(200)
		                        .end(gson.toJson(result));
	                        } else {
	                            System.out.println("Error: " + res.cause().getLocalizedMessage());
	                            routingContext.response()
	                                    .setStatusCode(404)
	                                    .end("Error al obtener el actuador con idGroup " + idGroup + ": " + res.cause().getMessage());
	                        }
	                        connection.result().close();
	                    });
	        } else {
	            System.out.println(connection.cause().toString());
	            routingContext.response()
	                    .setStatusCode(500)
	                    .end("Error al conectar con la base de datos: " + connection.cause().getMessage());
	        }
	    });
	}
	private void addActuador(RoutingContext routingContext) {

		// Parseamos el cuerpo de la solicitud HTTP a un objeto Sensor_humedad_Entity
		final ActuatorImpl actuador = gson.fromJson(routingContext.getBodyAsString(), ActuatorImpl.class);

		// Ejecutamos la inserción en la base de datos MySQL
		mySqlClient.preparedQuery(
				"INSERT INTO actuators (idActuators, timeStamp, estado, idPlaca, idGroup) VALUES (?, ?, ?, ?, ?, ?)")
				.execute((Tuple.of(actuador.getidActuators(), actuador.getTimeStamp(), actuador.getEstado(),
						actuador.getIdPlaca(), actuador.getIdGroup())), res -> {
							if (res.succeeded()) {
								// Si la inserción es exitosa, respondemos con el sensor creado
								routingContext.response().setStatusCode(201).putHeader("content-type",
										"application/json; charset=utf-8").end("Acutador añadido correctamente");
							} else {
								// Si hay un error en la inserción, respondemos con el mensaje de error
								System.out.println("Error: " + res.cause().getLocalizedMessage());
								routingContext.response().setStatusCode(500).end("Error al añadir el actuador: " + res.cause().getMessage());
							}
						});
	}

	private void deleteActuador(RoutingContext routingContext) {
		mySqlClient.getConnection(connection -> {
			int idActuador = Integer.parseInt(routingContext.request().getParam("idActuators"));
			if (connection.succeeded()) {
				connection.result().preparedQuery("DELETE FROM actuators WHERE idActuators = ?")
						.execute(Tuple.of(idActuador), res -> {
							if (res.succeeded()) {
								// Get the result set
								RowSet<Row> resultSet = res.result();
								System.out.println(resultSet.size());
								List<ActuatorImpl> result = new ArrayList<>();
								for (Row elem : resultSet) {
									result.add(new ActuatorImpl(elem.getInteger("idActuators"), elem.getLong("timeStamp"),
											elem.getBoolean("estado"), elem.getInteger("idPlaca"), elem.getInteger("idGroup")));
								}
								routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
										.setStatusCode(200).end(result.toString());

							} else {
								System.out.println("Error: " + res.cause().getLocalizedMessage());
								routingContext.response().setStatusCode(500).end("Error al eliminar el actuador: " + res.cause().getMessage());
							}
							connection.result().close();
						});
			} else {
				System.out.println(connection.cause().toString());
				routingContext.response().setStatusCode(500).end("Error con la coenxión: " + connection.cause().getMessage());
			}
		});
	}

	private void updateActuador(RoutingContext routingContext) {
		// Obtenemos el ID del sensor de los parámetros de la solicitud HTTP
		int idActuador = Integer.parseInt(routingContext.request().getParam("idActuators"));

		// Obtenemos el sensor actualizado del cuerpo de la solicitud HTTP
		final ActuatorImpl updatedActuador = gson.fromJson(routingContext.getBodyAsString(), ActuatorImpl.class);

		// Ejecutamos la actualización en la base de datos MySQL
		mySqlClient
				.preparedQuery(
						"UPDATE actuador SET idActuators = ?, timeStamp = ?, estado = ?, idPlaca = ?, idGroup = ? WHERE idActuators = ?")
				.execute((Tuple.of(updatedActuador.getidActuators(), updatedActuador.getTimeStamp(),
						updatedActuador.getEstado(), updatedActuador.getIdPlaca(), updatedActuador.getIdGroup(),  idActuador)), res -> {
							if (res.succeeded()) {
								// Si la actualización es exitosa, respondemos con el sensor actualizado
								if (res.result().rowCount() > 0) {
									routingContext.response().setStatusCode(200)
											.putHeader("content-type", "application/json; charset=utf-8")
											.end(gson.toJson(updatedActuador));
								}
							} else {
								// Si hay un error en la actualización, respondemos con el código 500 (Error
								// interno del servidor)
								System.out.println("Error: " + res.cause().getLocalizedMessage());
								routingContext.response().setStatusCode(500).end("Error al actualizar el actuador: " + res.cause().getMessage());

							}
						});
	}

}