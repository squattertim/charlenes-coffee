package coffee.app.model.command.expression;

import coffee.app.main.logic.UglyHardcodedBonusProcessor;
import coffee.app.main.tools.CoffeeException;
import coffee.app.main.tools.ThingsPrinter;
import coffee.app.model.BonusProcessorResults;
import coffee.app.model.command.Command;
import coffee.app.model.command.expression.order.OrderItem;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class OrderExpression implements Command, AbstractExpression {

    private final ItemsExpression orderItems;
    private final String bonusCodeFromOrder;
    private final String dateTimeFromOrder;
    private String bonusCodeNew;
    private BigDecimal bonusPrice;
    private Date dateTimeToday;

    public OrderExpression(ItemsExpression items, String bonusCodeFromOrder, String dateTimeFromOrder) {
        this.orderItems = items;
        this.bonusCodeFromOrder = bonusCodeFromOrder;
        this.dateTimeFromOrder = dateTimeFromOrder;
    }

    @Override
    public String execute() throws CoffeeException {
        List<OrderItem> orderItems = this.evaluate();
        return this.bonusCodeNew == null ? ThingsPrinter.printWithSum(orderItems, orderItems.stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)) :
                ThingsPrinter.printWithSumAndBonus(orderItems, orderItems.stream()
                        .map(OrderItem::getPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .subtract(bonusPrice), bonusPrice, bonusCodeNew, dateTimeToday);
    }

    @Override
    public List<OrderItem> evaluate() throws CoffeeException {
        List<OrderItem> orderItems = this.orderItems.evaluate();
        if (dateTimeToday == null) {
            dateTimeToday = new Date();
        }
        BonusProcessorResults results = new UglyHardcodedBonusProcessor(bonusCodeFromOrder, dateTimeFromOrder, dateTimeToday)
                .process(orderItems);
        bonusPrice = results.getBonusPrice();
        bonusCodeNew = results.getBonusCode();
        return orderItems;
    }

    public void setDateTimeToday(Date dateTimeToday) {
        this.dateTimeToday = dateTimeToday;
    }

}
