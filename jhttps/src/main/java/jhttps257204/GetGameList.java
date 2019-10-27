package jhttps257204;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;


import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
    name = "getGameList",
    urlPatterns = {"/getGameList"}
)
public class GetGameList extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws IOException {

    response.setContentType("text/plain");
    response.setCharacterEncoding("UTF-8");

    response.getWriter().print("{“gameList”: [“yeet”, “beeteeet”, “gamename”, “xdddd”]}");

  }
}