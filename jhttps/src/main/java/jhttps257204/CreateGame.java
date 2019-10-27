package jhttps257204;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;

import org.json.simple.*;
import org.json.simple.parser.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
    name = "CreateGame",
    urlPatterns = {"/createNewGame"}
)
public class CreateGame extends HttpServlet {
	
 
  @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws IOException {
		//String user = request.getParameter("game");
	  String test = "badCase";
	  if ("POST".equalsIgnoreCase(request.getMethod())) 
	  {
	     test = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
	  }
		
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print("I Hacked the mainframe, " + test + ".\r\n");
		// create HTML response
		/*PrintWriter writer = response.getWriter();
		writer.append("<!DOCTYPE html>\r\n")
			  .append("<html>\r\n")
			  .append("		<head>\r\n")
			  .append("			<title>Welcome message</title>\r\n")
			  .append("		</head>\r\n")
			  .append("		<body>\r\n");
		if (user != null && !user.trim().isEmpty()) {
			writer.append("	Welcome " + user + ".\r\n");
			writer.append("	You successfully completed this javatutorial.net example.\r\n");
		} else {
			writer.append("	You did not entered a name!\r\n");
		}
		*///writer.append("		</body>\r\n");

}
  
   @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws IOException {
	  Game newGame = new Game("INSERT_GAME_ID");
    response.setContentType("text/plain");
    response.setCharacterEncoding("UTF-8");

    response.getWriter().print("{“success”: “true”/”false”}");
    
  }
  
}