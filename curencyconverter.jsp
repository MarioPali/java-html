<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.io.*, java.util.*" %>
<%@ page import="javax.servlet.*, javax.servlet.http.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>Currency Converter</title>
    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
</head>
<body>
    <h1>Μετατροπή Ποσών σε Διαφορετικά Νομίσματα</h1>

    <form id="conversionForm">
        Ποσό: <input type="text" name="amount">
        Από:
        <select name="from">
            <option value="EUR">Ευρώ</option>
            <option value="USD">Δολάρια</option>
            <option value="GBP">Λίρες</option>
        </select>
        Σε:
        <select name="to">
            <option value="EUR">Ευρώ</option>
            <option value="USD">Δολάρια</option>
            <option value="GBP">Λίρες</option>
        </select>
        <input type="button" value="Μετατροπή" onclick="convertCurrency();">
    </form>

    <div id="resultContainer"></div>

    <script>
        function convertCurrency() {
            var formData = $('#conversionForm').serialize();
            $.post('currencyconverter', formData, function(response) {
                if (response.includes("Σφάλμα:")) {
                    $('#resultContainer').html(response);
                } else {
                 
                    $('#resultContainer').html(response);
                }
            });
        }
    </script>

    <%!
        private static final double EUR_TO_USD = 1.06;
        private static final double EUR_TO_GBP = 0.84;

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
    %>

   

    <%!
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
    %>
</body>
</html>
