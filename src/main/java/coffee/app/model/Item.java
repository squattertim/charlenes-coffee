package coffee.app.model;

import java.math.BigDecimal;

public class Item implements Printable {

    private final String name;
    private final BigDecimal price;
    private final boolean bev;

    public Item(String name, BigDecimal price, boolean bev) {
        this.name = name;
        this.price = price;
        this.bev = bev;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        return name.equals(item.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public boolean isBev() {
        return bev;
    }
}
