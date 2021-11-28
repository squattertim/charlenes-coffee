package coffee.app.main.logic;

import coffee.app.main.tools.CoffeeException;
import coffee.app.model.BonusProcessorResults;
import coffee.app.model.command.expression.order.OrderItem;

import java.util.List;

public interface BonusProcessor {

    BonusProcessorResults process(List<OrderItem> orderItems) throws CoffeeException;

}
