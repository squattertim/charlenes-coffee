package coffee.app.model;

import java.math.BigDecimal;

public class BonusProcessorResults {

    BigDecimal bonusPrice;
    String bonusCode;

    public BonusProcessorResults(BigDecimal bonusPrice, String bonusCode) {
        this.bonusPrice = bonusPrice;
        this.bonusCode = bonusCode;
    }

    public BigDecimal getBonusPrice() {
        return this.bonusPrice;
    }

    public String getBonusCode() {
        return this.bonusCode;
    }
}
