package coffee.app.model.command.expression;

import coffee.app.model.command.expression.order.OrderItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemsExpression implements AbstractExpression {

    private final List<ItemExpression> items = new ArrayList<>();

    public ItemsExpression(ItemExpression item) {
        this.add(item);
    }

    public void add(ItemExpression item) {
        this.items.add(item);
    }

    @Override
    public List<OrderItem> evaluate() {
        return items.stream().flatMap(itemExpression ->
                itemExpression.evaluate().stream()).collect(Collectors.toList());
    }
}
