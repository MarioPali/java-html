package xe2169;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

@WebServlet("/currencyconverter2")
public class CurrencyConverter2 extends HttpServlet {

    private ArrayList<String> currencyCodes;
    
    private HashMap<String, String> codeToFullName;
   
    private HashMap<String, Double> codeToExchangeRate;

    @Override
    public void init() throws ServletException {
        super.init();
        loadCurrencyData();
    }

    private void loadCurrencyData() {
        currencyCodes = new ArrayList<>();
        codeToFullName = new HashMap<>();
        codeToExchangeRate = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(getServletContext().getRealPath("/WEB-INF/euro_rates.txt")), "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String code = parts[0].trim();
                    String fullName = parts[1].trim();
                    double exchangeRate = Double.parseDouble(parts[2].trim());

                    currencyCodes.add(code);
                    codeToFullName.put(code, fullName);
                    codeToExchangeRate.put(code, exchangeRate);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>\n" +
                "<html><head><title>Currency Converter</title>" +
                "<script src=\"https://code.jquery.com/jquery-3.6.4.min.js\"></script>" +
                "</head><body>\n" +
                "<h1>Μετατροπή Ποσών σε Διαφορετικά Νομίσματα</h1>\n" +
                "<form id=\"conversionForm\">\n" +
                "Ποσό: <input type=\"text\" name=\"amount\">\n" +
                "Από: \n" +
                "<select name=\"from\">\n");

        for (String code : currencyCodes) {
            out.println("<option value=\"" + code + "\">" + codeToFullName.get(code) + "</option>\n");
        }

        out.println("</select>\n" +
                "Σε: \n" +
                "<select name=\"to\">\n");

        for (String code : currencyCodes) {
            out.println("<option value=\"" + code + "\">" + codeToFullName.get(code) + "</option>\n");
        }

        out.println("</select>\n" +
                "<input type=\"button\" value=\"Μετατροπή\" onclick=\"convertCurrency();\">\n" +
                "</form>\n" +
                "<div id=\"resultContainer\"></div>\n" +
                "<script>\n" +
                "function convertCurrency() {\n" +
                "   var formData = $('#conversionForm').serialize();\n" +
                "   $.post('currencyconverter2', formData, function(response) {\n" +
                "       $('#resultContainer').html(response);\n" +
                "   });\n" +
                "}\n" +
                "</script>\n" +
                "</body>\n" +
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
            out.println("<p>" + amount + " " + codeToFullName.get(fromCurrency) + " = " +
                    result + " " + codeToFullName.get(toCurrency) + "</p>");
        } catch (NumberFormatException e) {
            out.println("<h1>Σφάλμα:</h1>");
            out.println("<p style=\"color:red;\">Παρακαλώ εισάγετε έγκυρο αριθμητικό ποσό.</p>");
        }
    }

    private double convertCurrency(double amount, String fromCurrency, String toCurrency) {
        double fromRate = codeToExchangeRate.get(fromCurrency);
        double toRate = codeToExchangeRate.get(toCurrency);

        return amount * (toRate / fromRate);
    }
}
