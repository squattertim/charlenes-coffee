package coffee.app.main;

import coffee.app.model.Extra;
import coffee.app.model.Item;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CoffeeAppDefaultBootstraper implements CoffeeAppBootstraper {
    @Override
    public List<Item> loadItems() {
        return Arrays.asList(
                new Item("Sample item 1", new BigDecimal("2.5"), true),
                new Item("Sample item 2", new BigDecimal("3.0"), false)
        );
    }

    @Override
    public List<Extra> loadExtras() {
        return Collections.singletonList(new Extra("Extra sample", new BigDecimal("1.5"), false));
    }
}
