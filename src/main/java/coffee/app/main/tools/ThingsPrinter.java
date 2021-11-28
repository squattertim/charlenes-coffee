package coffee.app.main.tools;

import coffee.app.model.Printable;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

public class ThingsPrinter {

    private static Properties properties;
    private static Properties dictionary;

    static {
        try {
            properties = ConfigReader.read("config.properties");
            dictionary = ConfigReader.read("dictionary.properties");
        } catch (CoffeeException e) {
            // nothing to do
        }
    }

    public static String print(List<? extends Printable> things) {
        StringBuilder sb = new StringBuilder();
        int len = getMax(things);
        for (Printable item : things) {
            sb.append(String.format(getFormat(), leftPad(item.getName(), len, " "), item.getPrice()));
        }
        return sb.toString();
    }

    public static String printWithSumAndBonus(List<? extends Printable> things, BigDecimal sum,
                                              BigDecimal bonusPrice, String bonusCode, Date date) {
        StringBuilder sb = new StringBuilder();
        int len = getMax(things);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        sb.append(String.format("%s %n", leftPad("Order (" + formatter.format(date) + ")", len, " ")));
        sb.append(String.format("%s %n", leftPad("", len + 11, "-")));
        sb.append(print(things));
        sb.append(String.format("%s %n", leftPad("", len + 11, "-")));
        if (bonusPrice.compareTo(new BigDecimal(0)) > 0) {
            sb.append(String.format(getBonusPriceFormat(), leftPad("Bonus price", len, " "), bonusPrice));
        }
        sb.append(String.format(getFormat(), leftPad("SUM", len, " "), sum));
        if (!bonusCode.isEmpty()) {
            sb.append(String.format(getBonusCodeFormat(), leftPad("Bonus code", len, " "), bonusCode));
        }
        return sb.toString();
    }

    public static String printWithSum(List<? extends Printable> things, BigDecimal sum) {
        StringBuilder sb = new StringBuilder();
        int len = getMax(things);
        sb.append(print(things));
        sb.append(String.format("%s %n", leftPad("", len + 11, "-")));
        sb.append(String.format(getFormat(), leftPad("SUM", len, " "), sum));
        return sb.toString();
    }

    public static String printWithDetails(List<? extends Printable> items) {
        StringBuilder sb = new StringBuilder();
        int len = getMax(items);
        for (Printable item : items) {
            sb.append(String.format(getFormat(), leftPad(getDetails(item), len, " "), item.getPrice()));
        }
        return sb.toString();
    }

    private static String getDetails(Printable item) {
        return item.getName() + " ("
                + item.getClass().getSimpleName().toLowerCase(Locale.ROOT)
                + (item.isBev() ? ", " + dictionary.get("beverage") : "")
                + ")";
    }

    private static String getFormat() {
        return "%s %.2f " + properties.get("currency") + "%n";
    }

    private static String getBonusPriceFormat() {
        return "%s -%.2f " + properties.get("currency") + "%n";
    }

    private static String getBonusCodeFormat() {
        return "%s %s%n";
    }

    private static int getMax(List<? extends Printable> things) {
        return things.stream()
                .map(Printable::getName)
                .map(String::length)
                .max(Comparator.comparing(Integer::valueOf))
                .orElse(0) + 5;
    }

    private static String genString(int len, String content) {
        return content.repeat(Math.max(0, len));
    }

    private static String leftPad(String in, int len, String content) {
        return in + genString(len - in.length() - 1, content);
    }

}
