package coffee.app.main.logic.parser;

import coffee.app.main.CoffeeApplication;
import coffee.app.main.tools.CoffeeException;
import coffee.app.model.command.expression.ItemExpression;
import coffee.app.model.command.expression.ItemsExpression;

public class ItemsParser extends OrderParser implements Parser {

    public ItemsParser(CoffeeApplication context) throws CoffeeException {
        this.context = context;
    }

    public ItemsExpression parse(String itemsString) throws CoffeeException {
        return parseItems(itemsString);
    }

    private ItemsExpression parseItems(String itemsString) throws CoffeeException {
        String and = (String) dictionary.get(AND);
        if (itemsString.contains(and)) {
            String[] split = getParts(itemsString, and);
            ItemsExpression items = parseItems(tail(split));
            ItemExpression item = (ItemExpression) new ItemParser(context).parse(head(split));
            items.add(item);
            return items;
        } else {
            return new ItemsExpression((ItemExpression) new ItemParser(context).parse(itemsString));
        }
    }
}
