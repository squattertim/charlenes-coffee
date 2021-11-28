package coffee.app.main.logic;

import coffee.app.main.CoffeeApplication;
import coffee.app.main.logic.parser.OrderParser;
import coffee.app.main.tools.CoffeeException;
import coffee.app.model.command.Command;
import coffee.app.model.command.expression.OrderExpression;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OrderParserTest {

    @Test
    public void testParseOrder() throws CoffeeException, ParseException {

        CoffeeApplication coffeeApplication = new CoffeeApplication();
        coffeeApplication.bootstrap("src/test/resources/itemsTestDb.csv");

        OrderParser orderParser = new OrderParser();

        Command result = orderParser.parse(
                "order 1 coffee (small) with extra milk " +
                        "and 2 bacon roll with extra milk " +
                        "and freshly squeezed orange juice " +
                        "and coffee (large) " +
                        "and 5 coffee (medium) with foamed milk",
                coffeeApplication);

        assertNotNull(result);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        ((OrderExpression) result).setDateTimeToday(formatter.parse("2021-01-27 09:29:21"));

        assertEquals("" +
                        "Order (2021-01-27 09:29:21)         \n" +
                        "---------------------------------------------- \n" +
                        "5 Coffee (medium), foamed milk      20.56 CHF\n" +
                        "1 Coffee (large)                    3.50 CHF\n" +
                        "1 Freshly squeezed orange juice     3.95 CHF\n" +
                        "2 Bacon Roll, extra milk            689.30 CHF\n" +
                        "1 Coffee (small), extra milk        1232.80 CHF\n" +
                        "---------------------------------------------- \n" +
                        "Bonus price                         -8.56 CHF\n" +
                        "SUM                                 1941.55 CHF\n" +
                        "Bonus code                          xsSUXk18PVg=\n",
                result.execute());
    }

    @Test
    public void testParseOrder_withBonusCode() throws CoffeeException, ParseException {

        CoffeeApplication coffeeApplication = new CoffeeApplication();
        coffeeApplication.bootstrap("src/test/resources/itemsTestDb.csv");

        OrderParser orderParser = new OrderParser();

        Command result = orderParser.parse(
                "order 1 coffee (small) with extra milk " +
                        "and 2 bacon roll " +
                        "and freshly squeezed orange juice " +
                        "and coffee (large) " +
                        "and 5 coffee (medium) with foamed milk" +
                        "bonus code tVyeh2O5Kzk= 2021-01-27 09:29:21",
                coffeeApplication);

        assertNotNull(result);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        ((OrderExpression) result).setDateTimeToday(formatter.parse("2021-01-28 09:29:21"));

        assertEquals("" +
                        "Order (2021-01-28 09:29:21)         \n" +
                        "---------------------------------------------- \n" +
                        "5 Coffee (medium), foamed milk      20.56 CHF\n" +
                        "1 Coffee (large)                    3.50 CHF\n" +
                        "1 Freshly squeezed orange juice     3.95 CHF\n" +
                        "2 Bacon Roll                        689.00 CHF\n" +
                        "1 Coffee (small), extra milk        1232.80 CHF\n" +
                        "---------------------------------------------- \n" +
                        "Bonus price                         -8.56 CHF\n" +
                        "SUM                                 1941.25 CHF\n" +
                        "Bonus code                          O6LexER14oQ=\n",
                result.execute());
    }

    @Test
    public void testParseOrder_smallOrder() throws CoffeeException, ParseException {

        CoffeeApplication coffeeApplication = new CoffeeApplication();
        coffeeApplication.bootstrap("src/test/resources/itemsTestDb.csv");

        OrderParser orderParser = new OrderParser();

        Command result = orderParser.parse(
                "order 3 coffee (small) with extra milk ",
                coffeeApplication);

        assertNotNull(result);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        ((OrderExpression) result).setDateTimeToday(formatter.parse("2021-01-28 09:29:21"));

        assertEquals("" +
                        "Order (2021-01-28 09:29:21)      \n" +
                        "------------------------------------------- \n" +
                        "3 Coffee (small), extra milk     3697.80 CHF\n" +
                        "------------------------------------------- \n" +
                        "SUM                              3697.80 CHF\n" +
                        "Bonus code                       /F/VsCZn2qY=\n",
                result.execute());
    }

    @Test
    public void testParseOrder_smallOrder_usedBonus() throws CoffeeException, ParseException {

        CoffeeApplication coffeeApplication = new CoffeeApplication();
        coffeeApplication.bootstrap("src/test/resources/itemsTestDb.csv");

        OrderParser orderParser = new OrderParser();

        Command result = orderParser.parse(
                "order 2 coffee (small) with extra milk " +
                        "bonus code /F/VsCZn2qY= 2021-01-28 09:29:21",
                coffeeApplication);

        assertNotNull(result);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        ((OrderExpression) result).setDateTimeToday(formatter.parse("2021-01-28 09:29:21"));

        assertEquals("" +
                        "Order (2021-01-28 09:29:21)      \n" +
                        "------------------------------------------- \n" +
                        "2 Coffee (small), extra milk     2465.30 CHF\n" +
                        "------------------------------------------- \n" +
                        "Bonus price                      -1232.50 CHF\n" +
                        "SUM                              1232.80 CHF\n",
                result.execute());
    }

}