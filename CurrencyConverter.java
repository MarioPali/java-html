package xe2169;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

@WebServlet("/currencyconverter")
public class CurrencyConverter extends HttpServlet {

    private static final double EUR_TO_USD = 1.06;
    private static final double EUR_TO_GBP = 0.84;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.println

        ("<!DOCTYPE html>\n" +
        "<html><head><title>Currency Converter</title>" +
        "<script src=\"https://code.jquery.com/jquery-3.6.4.min.js\"></script>" +
        "</head><body>\n" +
        "<h1>Μετατροπή Ποσών σε Διαδορετικά Νομίσματα</h1>\n" + 
        "<form id=\"conversionForm\">\n" +
        "Ποσό: <input type=\"text\" name=\"amount\">\n" +
        "Απο: \n" +
        "<select name=\"from\">\n" +
        "<option value=\"EUR\">Ευρώ</option>\n" +
        "<option value=\"USD\">Δολάρια</option>\n" +
        "<option value=\"GBP\">Λίρες</option>\n"+
        "</select>\n"+ 
        "Σε: \n"+
        "<select name=\"to\">\n"+
        "<option value=\"EUR\">Ευρώ</option>\n"+
        "<option value=\"USD\">Δολάρια</option>\n"+
        "<option value=\"GBP\">Λίρες</option>\n"+
        "</select>\n"+
        "<input type=\"button\" value=\"Μετατροπή\" onclick=\"convertCurrency();\">\n"+
        "</form>"+
        "<div id=\"resultContainer\"></div>"+
        "<script>\n"+	
        "function convertCurrency() {\n"+
        "   var formData = $('#conversionForm').serialize();\n"+
        "   $.post('currencyconverter', formData, function(response) {\n"+
        "       $('#resultContainer').html(response);\n"+
        "   });\n"+
        "}\n"+
        "</script>"+
        "</body>"+ 
        "</html>");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            double amount = Double.parseDouble(request.getParameter("amount"));
            String fromCurrency = request.getParameter("from");
            String toCurrency = request.getParameter("to");
            double result = convertCurrency(amount, fromCurrency, toCurrency);

            out.println("<h1>Αποτέλεσμα:</h1>");
            out.println("<p>" + amount + " " + fromCurrency + " = " + result + " " + toCurrency + "</p>");
        } catch (NumberFormatException e) {
          
            out.println("<h1>Σφάλμα:</h1>");
            out.println("<p style=\"color:red;\">Παρακαλώ εισάγετε έγκυρο αριθμητικό ποσό.</p>");
        }
    } 

    private double convertCurrency(double amount, String fromCurrency, String toCurrency) {
        double result;

        if (fromCurrency.equals("EUR")) {
            if (toCurrency.equals("USD")) {
                result = amount * EUR_TO_USD;
            } else if (toCurrency.equals("GBP")) {
                result = amount * EUR_TO_GBP;
            } else {
                result = amount;
            }
        } else if (fromCurrency.equals("USD")) {
            if (toCurrency.equals("EUR")) {
                result = amount / EUR_TO_USD;
            } else if (toCurrency.equals("GBP")) {
                result = (amount / EUR_TO_USD) * EUR_TO_GBP;
            } else {
                result = amount;
            }
        } else if (fromCurrency.equals("GBP")) {
            if (toCurrency.equals("EUR")) {
                result = amount / EUR_TO_GBP;
            } else if (toCurrency.equals("USD")) {
                result = (amount / EUR_TO_GBP) * EUR_TO_USD;
            } else {
                result = amount;
            }
        } else {
            result = amount;
        }

        return result;
    }
}
