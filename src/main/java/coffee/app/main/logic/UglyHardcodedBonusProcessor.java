package coffee.app.main.logic;

import coffee.app.main.tools.CoffeeException;
import coffee.app.main.tools.ConfigReader;
import coffee.app.main.tools.CryptoUtil;
import coffee.app.model.BonusProcessorResults;
import coffee.app.model.command.expression.order.OrderItem;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class UglyHardcodedBonusProcessor implements BonusProcessor {

    private final Properties properties;
    private final String bonusCodeFromOrder;
    private final String dateTimeFromOrder;
    private final Date dateTimeToday;
    private BigDecimal bonusPrice;
    private String bonusCode;

    public UglyHardcodedBonusProcessor(String bonusCodeFromOrder, String dateTimeFromOrder, Date dateTimeToday) throws CoffeeException {
        this.bonusCodeFromOrder = bonusCodeFromOrder;
        this.dateTimeFromOrder = dateTimeFromOrder;
        this.dateTimeToday = dateTimeToday;
        this.properties = ConfigReader.read("config.properties");
        bonusPrice = new BigDecimal(0);
        bonusCode = "";
    }

    public BonusProcessorResults process(List<OrderItem> orderItems) throws CoffeeException {
        processforBonusPrice(orderItems);
        processForBonusCode(orderItems, bonusCodeFromOrder, dateTimeFromOrder, dateTimeToday);
        return new BonusProcessorResults(bonusPrice, bonusCode);
    }

    public void processForBonusCode(List<OrderItem> orderItems, String bonusCodeFromOrder, String dateTimeFromOrder, Date todaysDate) throws CoffeeException {
        int countBeverages = countBeverages(orderItems);
        int beveragesForBonus = Integer.parseInt((String) properties.get("bevsBonus"));
        int previousOrderBeverages = 0;
        int forNextOrder = 0;

        CryptoUtil cryptoUtil = new CryptoUtil();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        String encryptedBonusCode = "";
        try {
            if (!bonusCodeFromOrder.isEmpty()) {
                previousOrderBeverages = getPreviousOrderBeverages(bonusCodeFromOrder, dateTimeFromOrder, cryptoUtil);
            }

            String dateString = formatter.format(todaysDate);
            String key = properties.get("secret") + " " + dateString;
            int sumBeverages = countBeverages + previousOrderBeverages;
            if (sumBeverages >= beveragesForBonus) {
                orderItems.stream()
                        .filter(OrderItem::isBev)
                        .limit(countBeverages - (sumBeverages - beveragesForBonus))
                        .findFirst().ifPresent(item ->
                                bonusPrice = bonusPrice.add(item.getItemPrice()));
                forNextOrder = (sumBeverages - beveragesForBonus);
            } else {
                forNextOrder += countBeverages;
            }

            if (forNextOrder > 0) {
                String codePlain = "b:" + forNextOrder;
                encryptedBonusCode = cryptoUtil.encrypt(key, codePlain);
            }

        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException |
                InvalidKeyException | InvalidAlgorithmParameterException | UnsupportedEncodingException |
                IllegalBlockSizeException | BadPaddingException e) {
            throw new CoffeeException(String.format("Some problem when encoding the bonus: %s", e.getMessage()));
        } catch (IOException e) {
            throw new CoffeeException(String.format("Some problem when decoding the bonus: %s", e.getMessage()));
        }

        bonusCode = encryptedBonusCode;
    }

    private int getPreviousOrderBeverages(String bonusCodeFromOrder, String dateTimeFromOrder, CryptoUtil cryptoUtil) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IOException {
        int previousOrderBeverages;
        String userKey = properties.get("secret") + " " + dateTimeFromOrder;
        String lastBonus = cryptoUtil.decrypt(userKey, bonusCodeFromOrder);
        String[] parts = lastBonus.split(":");
        previousOrderBeverages = Integer.parseInt(parts[1]);
        return previousOrderBeverages;
    }

    private int countBeverages(List<OrderItem> orderItems) {
        return orderItems.stream()
                .filter(OrderItem::isBev)
                .mapToInt(OrderItem::getCount)
                .sum();
    }

    public void processforBonusPrice(List<OrderItem> orderItems) {
        Optional<OrderItem> anyBev = orderItems.stream().filter(OrderItem::isBev).findAny();
        Optional<OrderItem> anyNonBev = orderItems.stream().filter(item -> !item.isBev()).findAny();
        if (anyBev.isPresent() && anyNonBev.isPresent()) {
            orderItems.stream().filter(OrderItem::isExtra).findFirst()
                    .ifPresent(item -> bonusPrice = bonusPrice.add(item.getExtraPrice())
                    );
        }

    }

}
