package es.us.lsi.dad;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ServletSensor extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6201150158950823811L;

	private Map<Integer, SensorTemperatura> sensorPass; //map de id y temperatura

	public void init() throws ServletException {
		sensorPass = new HashMap<>();
		sensorPass.put(1, new SensorTemperatura(1, true, 30, 1, 1));
		super.init();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Integer id = Integer.valueOf(req.getParameter("idSensor"));
		
		SensorTemperatura sensor = sensorPass.get(id);
		
		String json = new Gson().toJson(sensor);
		
		response(resp, json);
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    BufferedReader reader = req.getReader();
	    
	    Gson gson = new Gson();
		SensorTemperatura sensor = gson.fromJson(reader, SensorTemperatura.class);
	
		sensorPass.put(sensor.getIdSensor(), sensor);
		resp.getWriter().println(gson.toJson(sensor));
		resp.setStatus(201);
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    BufferedReader reader = req.getReader();
	    
	    Gson gson = new Gson();
		SensorTemperatura sensor = gson.fromJson(reader, SensorTemperatura.class);
		
		sensorPass.remove(sensor.getIdSensor());
		resp.getWriter().println(gson.toJson(sensor));
		resp.setStatus(201);
	   
	}

	private void response(HttpServletResponse resp, String msg) throws IOException {
		PrintWriter out = resp.getWriter();
		out.println("<html>");
		out.println("<body>");
		out.println("<t1>" + msg + "</t1>");
		out.println("</body>");
		out.println("</html>");
	}
	
}