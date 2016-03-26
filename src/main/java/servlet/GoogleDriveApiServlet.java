package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
    name = "GoogleDriveApiServlet", 
    urlPatterns = {"/drive"}
)
public class GoogleDriveApiServlet extends HttpServlet{
    public void init() throws ServletException
    {
        // Do nothing
    }
    
    @Override
    public void doPost(HttpServletRequest request,
                   HttpServletResponse response)
    throws ServletException, IOException {
        ServletOutputStream out = response.getOutputStream();
        
               
        //Retorna o JSON
        //String r = "{ success : \""+ String.valueOf(sucesso) +"\"}";
        System.out.println("Ola");
        out.print("");
        out.flush();        
    }
    
    @Override
     public void destroy() {
        // Finalization code...
     }
}