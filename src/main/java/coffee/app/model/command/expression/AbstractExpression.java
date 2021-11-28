package coffee.app.model.command.expression;

import coffee.app.main.tools.CoffeeException;
import coffee.app.model.command.expression.order.OrderItem;

import java.util.List;

public interface AbstractExpression {
    List<OrderItem> evaluate() throws CoffeeException;
}
