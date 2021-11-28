package coffee.app.main.logic;

import coffee.app.main.tools.CoffeeException;
import coffee.app.main.tools.ConfigReader;
import coffee.app.main.tools.CryptoUtil;
import coffee.app.model.BonusProcessorResults;
import coffee.app.model.Extra;
import coffee.app.model.Item;
import coffee.app.model.command.expression.order.OrderItem;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

public class BonusProcessorTest {

    private static Properties properties;

    @BeforeClass
    public static void setUp() throws CoffeeException {
        properties = ConfigReader.read("config.properties");
    }

    @Test
    public void process() throws CoffeeException, ParseException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, IOException, InvalidKeyException {

        List<OrderItem> items = Arrays.asList(
                new OrderItem(1, new Item("Roll", new BigDecimal("5.0"), false), null),
                new OrderItem(2, new Item("Coffee", new BigDecimal("2.0"), true),
                        new Extra("Extra milk", new BigDecimal("1.0"), true)));

        String codeFromOrder = "";
        String dateFromOrder = "";

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        UglyHardcodedBonusProcessor bonusProcessor = new UglyHardcodedBonusProcessor(codeFromOrder, dateFromOrder,
                formatter.parse("2021-01-27 09:29:21"));

        BonusProcessorResults result = bonusProcessor.process(items);

        assertEquals("Ru7RRH9OoFI=", result.getBonusCode());

        String userKey = properties.get("secret") + " " + "2021-01-27 09:29:21";
        CryptoUtil cryptoUtil = new CryptoUtil();
        String orderBreveryCount = cryptoUtil.decrypt(userKey, result.getBonusCode());

        assertEquals("b:2", orderBreveryCount);
        assertEquals(new BigDecimal("1.0"), result.getBonusPrice());
    }

    @Test
    public void process_withEmptyBonus() throws CoffeeException, ParseException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, IOException, InvalidKeyException {

        List<OrderItem> items = Arrays.asList(
                new OrderItem(1, new Item("Roll", new BigDecimal("5.0"), false), null),
                new OrderItem(2, new Item("Coffee", new BigDecimal("2.0"), true),
                        new Extra("Extra milk", new BigDecimal("1.0"), true)));

        String codeFromOrder = "";
        String dateFromOrder = "";

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        UglyHardcodedBonusProcessor bonusProcessor = new UglyHardcodedBonusProcessor(codeFromOrder, dateFromOrder,
                formatter.parse("2021-01-29 09:29:21"));

        BonusProcessorResults result = bonusProcessor.process(items);

        assertEquals("KWMWsGS1OAs=", result.getBonusCode());

        String userKey = properties.get("secret") + " " + "2021-01-29 09:29:21";
        CryptoUtil cryptoUtil = new CryptoUtil();
        String bonusBreveryCount = cryptoUtil.decrypt(userKey, result.getBonusCode());

        assertEquals("b:2", bonusBreveryCount);
        assertEquals(new BigDecimal("1.0"), result.getBonusPrice());

    }

    @Test
    public void process_withBonus() throws CoffeeException, ParseException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, IOException, InvalidKeyException {

        List<OrderItem> items = List.of(
                new OrderItem(3, new Item("Coffee", new BigDecimal("2.0"), true),
                        new Extra("Extra milk", new BigDecimal("1.0"), true)));

        String codeFromOrder = "/F/VsCZn2qY=";
        String dateFromOrder = "2021-01-28 09:29:21";

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        UglyHardcodedBonusProcessor bonusProcessor = new UglyHardcodedBonusProcessor(codeFromOrder, dateFromOrder,
                formatter.parse("2021-01-29 09:29:21"));

        BonusProcessorResults result = bonusProcessor.process(items);

        assertEquals("5nY05g3G1Og=", result.getBonusCode());

        String userKey = properties.get("secret") + " " + "2021-01-29 09:29:21";
        CryptoUtil cryptoUtil = new CryptoUtil();
        String bonusBreveryCount = cryptoUtil.decrypt(userKey, result.getBonusCode());

        assertEquals("b:1", bonusBreveryCount);
        assertEquals(new BigDecimal("2.0"), result.getBonusPrice());
    }

    @Test
    public void process_withCombinedBonus() throws CoffeeException, ParseException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, IOException, InvalidKeyException {

        List<OrderItem> items = Arrays.asList(
                new OrderItem(1, new Item("Roll", new BigDecimal("5.0"), false), null),
                new OrderItem(3, new Item("Coffee", new BigDecimal("2.0"), true),
                        new Extra("Extra milk", new BigDecimal("1.0"), true)));

        String codeFromOrder = "/F/VsCZn2qY=";
        String dateFromOrder = "2021-01-28 09:29:21";

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        UglyHardcodedBonusProcessor bonusProcessor = new UglyHardcodedBonusProcessor(codeFromOrder, dateFromOrder,
                formatter.parse("2021-01-29 09:29:21"));

        BonusProcessorResults result = bonusProcessor.process(items);

        assertEquals("5nY05g3G1Og=", result.getBonusCode());

        String userKey = properties.get("secret") + " " + "2021-01-29 09:29:21";
        CryptoUtil cryptoUtil = new CryptoUtil();
        String bonusBreveryCount = cryptoUtil.decrypt(userKey, result.getBonusCode());

        assertEquals("b:1", bonusBreveryCount);
        assertEquals(new BigDecimal("3.0"), result.getBonusPrice());
    }


}