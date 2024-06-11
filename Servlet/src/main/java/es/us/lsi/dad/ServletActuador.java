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


public class ServletActuador extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6201150158950823811L;

	private Map<Integer, ActuadorRele> actuadorMap; //map de id y objeto actuador

	public void init() throws ServletException {
		actuadorMap = new HashMap<>();
		actuadorMap.put(1, new ActuadorRele(1, false, 1));
		super.init();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Integer id = Integer.parseInt(req.getParameter("idActuador"));
		
		ActuadorRele sensor = actuadorMap.get(id);
		
		String json = new Gson().toJson(sensor);
		
		response(resp, json);
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    BufferedReader reader = req.getReader();
	    
	    Gson gson = new Gson();
	    ActuadorRele actuador = gson.fromJson(reader, ActuadorRele.class);
	
		actuadorMap.put(actuador.getIdActuador(), actuador);
		resp.getWriter().println(gson.toJson(actuador));
		resp.setStatus(201);
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    BufferedReader reader = req.getReader();
	    
	    Gson gson = new Gson();
		ActuadorRele actuador = gson.fromJson(reader, ActuadorRele.class);
		
		actuadorMap.remove(actuador.getIdActuador());
		resp.getWriter().println(gson.toJson(actuador));
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