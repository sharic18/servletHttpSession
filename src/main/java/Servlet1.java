import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/Servlet1")
public class Servlet1 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Servlet1() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		noCache(response);
		PrintWriter out = response.getWriter();

		String n = request.getParameter("userName");

		// Validaciones del nombre
		if (n == null || n.trim().isEmpty()) {
			error(out, "El nombre no puede estar vacío.");
			return;
		}
		n = n.trim();
		if (n.length() < 2 || n.length() > 30) {
			error(out, "El nombre debe tener entre 2 y 30 caracteres.");
			return;
		}

		// Si ya había una sesión, se cierra y se crea una nueva (más seguro)
		HttpSession vieja = request.getSession(false);
		if (vieja != null) {
			vieja.invalidate();
		}
		HttpSession session = request.getSession(true);
		session.setAttribute("uname", n);

		out.print("<h3>Welcome " + escapar(n) + "</h3>");
		out.print("<a href='Servlet2'>visit</a> | ");
		out.print("<a href='Logout'>Cerrar sesión</a> | ");
		out.print("<a href='Logout?todas=true'>Cerrar todas las sesiones</a>");
		out.close();
	}

	// Si alguien abre Servlet1 escribiendo la dirección, se manda al login
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendRedirect("index.html");
	}

	private void error(PrintWriter out, String mensaje) {
		out.print("<h3 style='color:red'>" + mensaje + "</h3>");
		out.print("<a href='index.html'>Volver al login</a>");
		out.close();
	}

	private void noCache(HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
	}

	private String escapar(String s) {
		return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
	}
}