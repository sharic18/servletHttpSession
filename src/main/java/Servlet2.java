import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/Servlet2")
public class Servlet2 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Servlet2() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// La caché desactivada evita que "atrás" muestre la página vieja
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);

		// false = no crear sesión nueva si no existe
		HttpSession session = request.getSession(false);

		// Página protegida: sin sesión o sin usuario, se obliga a iniciar sesión
		if (session == null || session.getAttribute("uname") == null) {
			response.sendRedirect("index.html");
			return;
		}

		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();

		String n = (String) session.getAttribute("uname");
		out.print("<h3>Hello " + escapar(n) + "</h3>");
		out.print("<a href='Logout'>Cerrar sesión</a> | ");
		out.print("<a href='Logout?todas=true'>Cerrar todas las sesiones</a>");
		out.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	private String escapar(String s) {
		return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
	}
}