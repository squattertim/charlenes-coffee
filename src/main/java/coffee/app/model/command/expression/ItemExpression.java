package coffee.app.model.command.expression;

import coffee.app.model.command.expression.order.OrderItem;

import java.util.Collections;
import java.util.List;

public class ItemExpression implements AbstractExpression {

    private final OrderItem orderItem;

    public ItemExpression(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    @Override
    public List<OrderItem> evaluate() {
        return Collections.singletonList(orderItem);
    }
}
