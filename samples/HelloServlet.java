import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HelloServlet extends HttpServlet
{
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		response.setContentType("text/plain");

		String path = request.getPathInfo();

		switch(path)
		{
			case "/500":
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				break;
			case "/400":
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				break;
			default:
				response.setStatus(HttpServletResponse.SC_OK);
		}

		response.getWriter().println(path);
	}
}
