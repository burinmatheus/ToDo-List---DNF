package ServletDNF;

import ejbconnection.EJBConnection;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Matheus M. Burin
 */
@WebServlet(name = "ServletDNF", urlPatterns = {"/ServletDNF"})
public class ServletDNF extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter saida = response.getWriter()) {

            try {
                saida.print(EJBConnection.execMethod("EJBdonotforget/Funcoes", request.getParameter("funcao"), request, response));
            } catch (Exception ex) {
                try (PrintWriter out = response.getWriter()) {
                    response.setStatus(500);
                    out.print("{\"erro\":\"Falha interna\"}");
                }
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
// </editor-fold>

}
