package jhttps257204;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;


import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
    name = "GetStatus",
    urlPatterns = {"/getStatus?game=*****&playerID=*****"}
)
public class GetStatus extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws IOException {

    response.setContentType("text/plain");
    response.setCharacterEncoding("UTF-8");

    response.getWriter().print("{“alive”: “false”, “winner”: “false”, "
    		+ "“playersLeft”: x, “killfeed”: “X was killed by x, x players remain”"
    		+ "[“” if no kills]}");
    
  }
}