package coffee.app.main.logic.parser;

import coffee.app.main.CoffeeApplication;
import coffee.app.main.logic.parser.ItemsParser;
import coffee.app.main.tools.CoffeeException;
import coffee.app.main.tools.ConfigReader;
import coffee.app.model.command.expression.ItemsExpression;
import coffee.app.model.command.Command;
import coffee.app.model.command.basic.EmptyOrderCommand;
import coffee.app.model.command.expression.OrderExpression;

import java.util.Properties;

public class OrderParser {

    public static final String WITH = "with";
    public static final String AND = "and";
    public static final String BONUS = "bonus";
    public static final String ORDER = "order";

    final Properties dictionary = ConfigReader.read("dictionary.properties");
    CoffeeApplication context;

    public OrderParser() throws CoffeeException {
    }

    public Command parse(String orderString, CoffeeApplication context) throws CoffeeException {
        this.context = context;
        return parseOrder(orderString);
    }

    private Command parseOrder(String orderString) throws CoffeeException {

        String bonusCodeFromOrder = "";
        String dateTimeFormOrder = null;

        String orderKeyword = (String) dictionary.get(ORDER);
        orderString = removePrefix(orderString, orderKeyword);

        String bonusKeyword = (String) dictionary.get(BONUS);
        if (orderString.contains(bonusKeyword)) {
            bonusCodeFromOrder = extractSuffix(orderString, bonusKeyword);
            orderString = removeSuffix(orderString, bonusKeyword);
        }

        if (orderString.isEmpty()) {
            return new EmptyOrderCommand();
        }

        ItemsExpression items = new ItemsParser(context).parse(orderString);

        if(!bonusCodeFromOrder.isEmpty()) {
            dateTimeFormOrder = extractSuffix(bonusCodeFromOrder, " ");
            bonusCodeFromOrder = extractPrefix(bonusCodeFromOrder, " ");
        }

        return new OrderExpression(items, bonusCodeFromOrder, dateTimeFormOrder);

    }

    String extractPrefix(String orderString, String prefixDelimiter) throws CoffeeException {
        String[] parts = getParts(orderString, prefixDelimiter);
        if (parts.length > 1) {
            return head(parts);
        } else throw new CoffeeException(String.format("Missing prefix %s!", prefixDelimiter));
    }

    String removePrefix(String orderString, String prefixDelimiter) throws CoffeeException {
        String[] parts = getParts(orderString, prefixDelimiter);
        if (parts.length > 1) {
            return tail(parts);
        } else throw new CoffeeException(String.format("Missing prefix %s!", prefixDelimiter));
    }

    String extractSuffix(String orderString, String suffixDelimiter) throws CoffeeException {
        String[] parts = getParts(orderString, suffixDelimiter);
        if (parts.length > 1) {
            return tail(parts);
        } else throw new CoffeeException(String.format("Missing suffix %s!", suffixDelimiter));
    }

    String removeSuffix(String orderString, String suffixDelimiter) throws CoffeeException {
        String[] parts = getParts(orderString, suffixDelimiter);
        if (parts.length > 1) {
            return head(parts);
        } else throw new CoffeeException(String.format("Missing suffix %s!", suffixDelimiter));
    }

    String head(String[] parts) {
        return parts[0].trim();
    }

    String tail(String[] parts) {
        return parts[1].trim();
    }

    String[] getParts(String orderString, String prefixDelimiter) {
        return orderString.split(prefixDelimiter, 2);
    }

}
