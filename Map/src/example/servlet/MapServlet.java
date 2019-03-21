package example.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.cj.xdevapi.JsonArray;

import example.entry.Gps;
import example.util.SQLUntil;
import net.sf.json.JSONArray;

/**
 * Servlet implementation class MapServlet
 */
@WebServlet("/MapServlet")
public class MapServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MapServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String swlng = request.getParameter("swlng");
		String swlat = request.getParameter("swlat");
		String nelng = request.getParameter("nelng");
		String nelat = request.getParameter("nelat");
		
		Gps maxGps = new Gps(Double.parseDouble(nelng), Double.parseDouble(nelat));
		Gps minGps = new Gps(Double.parseDouble(swlng), Double.parseDouble(swlat));
		/*
		 * System.out.println("µØÍ¼±ß½ç£º");
		 * System.out.println(maxGps);
		 * System.out.println(minGps);
		 */
		PrintWriter out = response.getWriter();
		List<Gps> list = null;
		try {
			list = SQLUntil.selectData(maxGps, minGps);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONArray json = JSONArray.fromObject(list);
//		System.out.println(json.toString());
		out.print(json.toString());
	}

}
