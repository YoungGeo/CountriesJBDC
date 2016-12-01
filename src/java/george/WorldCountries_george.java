/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package george;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author gy674
 */
public class WorldCountries_george extends HttpServlet {

    String sqlcount = "SELECT COUNT(*) AS count FROM country";
    String sql = "SELECT country.*, city.Name FROM country INNER JOIN city ON country.Capital = city.ID ORDER by country.Name asc";
    String sqlconti = "SELECT * FROM country WHERE continent=? FROM country INNER JOIN city ON country.Capital = city.ID ORDER by Name asc";
    
    //String sql= "SELECT * FROM country ORDER by Name asc";
     int count = 0;
        
    String errmsg = "";
    JdbcHelper jdbc = new JdbcHelper();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            
            Boolean a = jdbc.connect("jdbc:mysql://localhost:3306/ejd", "root", "674600");

            if (a) {

                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>World Countries</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>World Countries</h1>");

                out.println("<form name='form' action='WorldCountries_george' method='get'>");
                out.println("Continent: <select name='continent'>");
                out.println("<option value='All'>All</option>");
                out.println("<option value='Africa'>Africa</option>");
                out.println("<option value='Antarctica'>Antarctica</option>");
                out.println("<option value='Asia'>Asia</option>");
                out.println("<option value='Europe'>Europe</option>");
                out.println("<option value='North America'>North America</option>");
                out.println("<option value='Oceania'>Oceania</option>");
                out.println("<option value='South America'>South America</option>");
                out.println("</select>\n"
                        + "<input type='submit' value='Submit'>\n"
                        + "</form>");
                
                
                ResultSet result = jdbc.query(sqlcount);

                if (result != null) {

                    result.next();

                    out.println("<p> Number of Countries: " + result.getInt("count") + "</p>");
                    result.close();

                } else {
                    out.println("<p> did not work</p>");
                }
                
                String selected_continent = request.getParameter("continent") ;
                
                
                out.println("<table>\n<tr>\n"
                        + "<th>Code</th><th>Name</th><th>Continent</th><th>Region</th><th>Surface Area</th><th>Population</th><th>Capital</th>\n"
                        + "</tr>\n");
                
                if(selected_continent == null || selected_continent.equalsIgnoreCase("all")){
                result = jdbc.query(sql);
                }else{
                    result = jdbc.query(sqlconti, selected_continent);
                }
                

                while (result.next()) {

                    out.println("<tr>\n"
                            + "<th>" + result.getString("country.Code") + "</th><th>" + result.getString("Country.Name") + "</th><th>" + result.getString("Continent") + "</th>"
                            + "<th>" + result.getString("Region") + "</th><th>" + result.getDouble("SurfaceArea") + "</th><th>" + result.getInt("Population") + "</th>\n"
                            +"<th>" + result.getString("city.Name") + "</th>\n"+ "</tr>\n");
                }
                out.println("</table>\n");

                out.println("</body>");
                out.println("</html>");
            }
        } catch (SQLException e) {
            errmsg = e.getSQLState() + ": " + e.getMessage();
            System.err.println(errmsg);
        } catch (Exception e) {
            errmsg = e.getMessage();
            System.err.println(errmsg);
        }

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
        processRequest(request, response);
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
