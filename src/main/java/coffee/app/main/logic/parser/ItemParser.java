package coffee.app.main.logic.parser;

import coffee.app.main.CoffeeApplication;
import coffee.app.main.tools.CoffeeException;
import coffee.app.model.Extra;
import coffee.app.model.Item;
import coffee.app.model.command.expression.ItemExpression;
import coffee.app.model.command.expression.order.OrderItem;
import coffee.app.model.command.expression.AbstractExpression;

import java.util.Locale;

public class ItemParser extends OrderParser implements Parser {

    public ItemParser(CoffeeApplication context) throws CoffeeException {
        this.context = context;
    }

    @Override
    public AbstractExpression parse(String expressionString) throws CoffeeException {
        return parseItem(expressionString);
    }

    ItemExpression parseItem(String itemString) throws CoffeeException {

        String extraString = "";
        String countPrefix;

        String extraKeyword = (String) dictionary.get(WITH);
        if (itemString.contains(extraKeyword)) {
            extraString = extractSuffix(itemString, extraKeyword);
            itemString = removeSuffix(itemString, WITH);
        }

        int count = 1;
        countPrefix = extractPrefix(itemString, " ");
        if (!countPrefix.isEmpty()) {
            try {
                count = Integer.parseInt(countPrefix);
                itemString = removePrefix(itemString, " ");
            } catch (NumberFormatException e) {
                // nothing to do
            }
        }

        Item item = context.getItem(itemString.toLowerCase(Locale.ROOT));

        if (item == null) {
            throw new CoffeeException(String.format("There's no item like this: %s", itemString));
        }

        Extra extra = (Extra) context.getItem(extraString.toLowerCase(Locale.ROOT));

        return new ItemExpression(new OrderItem(count, item, extra));

    }
}
