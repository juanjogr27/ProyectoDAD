#include <HTTPClient.h>
#include "ArduinoJson.h"
#include <WiFiUdp.h>
#include <PubSubClient.h>

// Replace 0 by ID of this current device
const int DEVICE_ID = 124;

int test_delay = 1000; // so we don't spam the API
boolean describe_tests = true;

// Replace 0.0.0.0 by your server local IP (ipconfig [windows] or ifconfig [Linux o MacOS] gets IP assigned to your PC)
String serverName = "http://192.168.178.96:8081/";
HTTPClient http;

// Replace WifiName and WifiPassword by your WiFi credentials
#define STASSID "Note8Juanjo"    //"Your_Wifi_SSID"
#define STAPSK "internetmovil" //"Your_Wifi_PASSWORD"

// MQTT configuration
WiFiClient espClient;
PubSubClient client(espClient);

// Server IP, where de MQTT broker is deployed
const char *MQTT_BROKER_ADRESS = "localhost";
const uint16_t MQTT_PORT = 1883;

// Name for this MQTT client
const char *MQTT_CLIENT_NAME = "ArduinoClient_1";

// callback a ejecutar cuando se recibe un mensaje
// en este ejemplo, muestra por serial el mensaje recibido
void OnMqttReceived(char *topic, byte *payload, unsigned int length)
{
  Serial.print("Received on ");
  Serial.print(topic);
  Serial.print(": ");

  String content = "";
  for (size_t i = 0; i < length; i++)
  {
    content.concat((char)payload[i]);
  }
  Serial.print(content);
  Serial.println();
}

// inicia la comunicacion MQTT
// inicia establece el servidor y el callback al recibir un mensaje
void InitMqtt()
{
  client.setServer(MQTT_BROKER_ADRESS, MQTT_PORT);
  client.setCallback(OnMqttReceived);
}




// Setup
void setup()
{
  Serial.begin(9600);
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(STASSID);

  /* Explicitly set the ESP32 to be a WiFi-client, otherwise, it by default,
     would try to act as both a client and an access-point and could cause
     network-issues with your other WiFi-devices on your WiFi-network. */
  WiFi.mode(WIFI_STA);
  WiFi.begin(STASSID, STAPSK);

  while (WiFi.status() != WL_CONNECTED)
  {
    delay(500);
    Serial.print(".");
  }

  InitMqtt();

  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
  Serial.println("Setup!");
}

// conecta o reconecta al MQTT
// consigue conectar -> suscribe a topic y publica un mensaje
// no -> espera 5 segundos
void ConnectMqtt()
{
  Serial.print("Starting MQTT connection...");
  if (client.connect(MQTT_CLIENT_NAME))
  {
    client.subscribe("hello/world");
    client.publish("hello/world", "connected");
  }
  else
  {
    Serial.print("Failed MQTT connection, rc=");
    Serial.print(client.state());
    Serial.println(" try again in 5 seconds");

    delay(5000);
  }
}

// gestiona la comunicación MQTT
// comprueba que el cliente está conectado
// no -> intenta reconectar
// si -> llama al MQTT loop
void HandleMqtt()
{
  if (!client.connected())
  {
    ConnectMqtt();
  }
  client.loop();
}

String response;

String serializeSensorValueBody(int idSensors, long timeStamp, int temperatura, boolean isBedroom, int idPlaca, int idGroup)
{
  // StaticJsonObject allocates memory on the stack, it can be
  // replaced by DynamicJsonDocument which allocates in the heap.
  //
  DynamicJsonDocument doc(2048);

  // Add values in the document
  //
  doc["idSensors"] = idSensors;
  doc["timeStamp"] = timeStamp;
  doc["temperatura"] = temperatura;
  doc["isBedroom"] = isBedroom;
  doc["idPlaca"] = idPlaca;
  doc["idGroup"] = idGroup;

  // Generate the minified JSON and send it to the Serial port.
  //
  String output;
  serializeJson(doc, output);
  Serial.println(output);

  return output;
}

String serializeActuatorStatusBody(int idActuators,long timeStamp, bool estado, int idPlaca, int idGroup)
{
  DynamicJsonDocument doc(2048);

  doc["idActuators"] = idActuators;
  doc["timeStamp"] = timeStamp;
  doc["estado"] = estado;
  doc["idPlaca"] = idPlaca;
  doc["idGroup"] = idGroup;

  String output;
  serializeJson(doc, output);
  return output;
}

void deserializeActuatorStatusBody(String responseJson)
{
  if (responseJson != "")
  {
    DynamicJsonDocument doc(2048);

    // Deserialize the JSON document
    DeserializationError error = deserializeJson(doc, responseJson);

    // Test if parsing succeeds.
    if (error)
    {
      Serial.print(F("deserializeJson() failed: "));
      Serial.println(error.f_str());
      return;
    }

    // Fetch values.
    int idActuators = doc["idActuators"];
    long timeStamp = doc["timeStamp"];
    bool estado = doc["estado"];
    int idPlaca = doc["idPlaca"];
    int idGroup = doc["idGroup"];

    Serial.println(("Actuator status deserialized: [idActuators: " + String(idActuators) + ", timeStamp: " + String(timeStamp) + ", estado: " + String(estado) + ", idGroup" + String(idGroup) + "]").c_str());
  }
}

void deserializeSensorStatusBody(String responseJson)
{
  if (responseJson != "")
  {
    DynamicJsonDocument doc(2048);

    // Deserialize the JSON document
    DeserializationError error = deserializeJson(doc, responseJson);

    // Test if parsing succeeds.
    if (error)
    {
      Serial.print(F("deserializeJson() failed: "));
      Serial.println(error.f_str());
      return;
    }

    // Fetch values.
    int idSensors = doc["idSensors"];
    long timeStamp = doc["timeStamp"];
    int temperatura = doc["temperatura"];
    bool isBedroom = doc["isBedroom"];
    int idPlaca = doc["idPlaca"];
    int idGroup = doc["idGroup"];

    Serial.println(("Actuator status deserialized: [idSensors: " + String(idSensors) + ", timeStamp: " + String(timeStamp) + ", temperatura: " + String(temperatura) + ", isBedroom" + String(isBedroom) + ", idPlaca: " + String(idPlaca) + ", idGroup: " + String(idGroup) + "]").c_str());
  }
}

void test_response(int httpResponseCode)
{
  delay(test_delay);
  if (httpResponseCode > 0)
  {
    Serial.print("HTTP Response code: ");
    Serial.println(httpResponseCode);
    String payload = http.getString();
    Serial.println(payload);
  }
  else
  {
    Serial.print("Error code: ");
    Serial.println(httpResponseCode);
  }
}

void describe(char *description)
{
  if (describe_tests)
    Serial.println(description);
}

void POST_tests()
{
  String actuator_states_body = serializeActuatorStatusBody(33, millis(), false, 1, 1);
  describe("Test POST with actuator state");
  String serverPath = serverName + "api/actuators";
  http.begin(serverPath.c_str());
  test_response(http.POST(actuator_states_body));

  String sensor_value_body = serializeSensorValueBody(33, millis(), random(20, 40), false, 2, 1);
  describe("Test POST with sensor value");
  serverPath = serverName + "api/sensors";
  http.begin(serverPath.c_str());
  test_response(http.POST(sensor_value_body));

  // String device_body = serializeDeviceBody(String(DEVICE_ID), ("Name_" + String(DEVICE_ID)).c_str(), ("mqtt_" + String(DEVICE_ID)).c_str(), 12);
  // describe("Test POST with path and body and response");
  // serverPath = serverName + "api/device";
  // http.begin(serverPath.c_str());
  // test_response(http.POST(actuator_states_body));
}

// Run the tests!
void loop()
{
  //GET_tests();
  POST_tests();
  HandleMqtt();
}
