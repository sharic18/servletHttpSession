import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

// Lleva la cuenta de todas las sesiones activas del servidor
@WebListener
public class SessionRegistry implements HttpSessionListener {

	private static final Map<String, HttpSession> SESIONES = new ConcurrentHashMap<>();

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		SESIONES.put(se.getSession().getId(), se.getSession());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		SESIONES.remove(se.getSession().getId());
	}

	// Invalida todas las sesiones registradas y devuelve cuántas cerró
	public static int invalidarTodas() {
		int total = 0;
		for (HttpSession s : SESIONES.values()) {
			try {
				s.invalidate();
				total++;
			} catch (IllegalStateException e) {
				// la sesión ya estaba invalidada
			}
		}
		return total;
	}
}