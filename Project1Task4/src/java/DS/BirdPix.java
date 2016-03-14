/*
* Author: Enzhe Lu
* Last Modified: Feb 05 2016.
*
* This program is for screen capturating bird picture with author information
* and provide to the user.
* The interface is including two different jsp webpages to demonstrate the 
* result.
* User could select from dropdown list for either type of bird name and a random
* bird picture will generate for them
 */
package DS;

// imports required for capturing Picture

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Enzhe Lu
 */

@WebServlet(name = "BirdPix",
        urlPatterns = {"/BirdPix"})
public class BirdPix extends HttpServlet {

    /**
     * Processes requests for both HTTP <code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet BirdPix</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet BirdPix at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
     BirdPixModel bpm = null;  // The "business model" for this app
    // Initiate this servlet by instantiating the model that it will use.
    @Override
    public void init() {
        bpm = new BirdPixModel();
    }
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         // get the search parameter if it exists
        String search = request.getParameter("pix");
        System.out.println(search);

        // determine what type of device our user is
        String ua = request.getHeader("User-Agent");

        boolean mobile;
        // prepare the appropriate DOCTYPE for the view pages
        if (ua != null && ((ua.indexOf("Android") != -1) || (ua.indexOf("iPhone") != -1))) {
            mobile = true;
            /*
             * This is the latest XHTML Mobile doctype. To see the difference it
             * makes, comment it out so that a default desktop doctype is used
             * and view on an Android or iPhone.
             */
            request.setAttribute("doctype", "<!DOCTYPE html PUBLIC \"-//WAPFORUM//DTD XHTML Mobile 1.2//EN\" \"http://www.openmobilealliance.org/tech/DTD/xhtml-mobile12.dtd\">");
        } else {
            mobile = false;
            request.setAttribute("doctype", "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
        }

        String nextView;
        
        if (search != null) {
            // use model to do the search and choose the result view
            bpm.doPictureSearch(search);
            /*
             * Attributes on the request object can be used to pass data to
             * the view.  These attributes are name/value pairs, where the name
             * is a String object.  Here the pictureURL is passed to the view
             * after it is returned from the model interestingPictureSize method.
             */
            if(bpm.getPictureTag() == null){
                nextView = "index.jsp"; // return to origional view when not find.
            } else {
                request.setAttribute("pictureURL",
                    bpm.interestingPictureSize(
                            (mobile) ? "mobile" : "desktop"));
                // Pass the user search string (pictureTag, authorURL
                // authorName to the view.
                request.setAttribute("pictureTag", bpm.getPictureTag());
                request.setAttribute("authorURL", bpm.getAuthorURL());
                request.setAttribute("authorName", bpm.getAuthorName());
            
                //assign the view
                nextView = "result.jsp";
            }
            
        } else {
            // no search parameter so choose the prompt view
            nextView = "index.jsp";
        }
        
        // Transfer control over the the correct "view"
        RequestDispatcher view = request.getRequestDispatcher(nextView);
        view.forward(request, response);
        
        
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
