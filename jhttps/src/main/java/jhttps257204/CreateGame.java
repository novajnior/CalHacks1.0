package jhttps257204;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;


import java.io.IOException;

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
  public void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws IOException {
	  Game newGame = new Game("INSERT_GAME_ID");
    response.setContentType("text/plain");
    response.setCharacterEncoding("UTF-8");

    response.getWriter().print("{“success”: “true”/”false”}");
    
  }
}