package coffee.app.model.command.expression.order;

import coffee.app.model.Extra;
import coffee.app.model.Item;
import coffee.app.model.Printable;

import java.math.BigDecimal;
import java.util.Locale;

public class OrderItem implements Printable {

    private final Item item;
    private final Integer count;
    private final Extra extra;

    public OrderItem(Integer count, Item item, Extra extra) {
        this.count = count;
        this.item = item;
        this.extra = extra;
    }

    @Override
    public String getName() {
        return "" + count + " " + item.getName() +
                (extra != null ? ", " + extra.getName().toLowerCase(Locale.ROOT) : "");
    }

    @Override
    public BigDecimal getPrice() {
        return item.getPrice() != null ?
                item.getPrice().multiply(new BigDecimal(count))
                        .add(extra != null ? getExtraPrice() : new BigDecimal(0)) : new BigDecimal(0);
    }

    public BigDecimal getExtraPrice() {
        if (isExtra()) {
            return extra.getPrice();
        } else return new BigDecimal(0);
    }

    @Override
    public boolean isBev() {
        return item.isBev();
    }

    public int getCount() {
        return this.count;
    }

    public boolean isExtra() {
        return this.extra != null;
    }

    public BigDecimal getItemPrice() {
        return this.item.getPrice();
    }
}
