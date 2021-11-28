package coffee.app.main;

import coffee.app.main.tools.CoffeeException;
import coffee.app.main.tools.ConfigReader;
import coffee.app.model.Extra;
import coffee.app.model.Item;


import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.stream.Collectors;

public class CoffeeAppFileBootstraper implements CoffeeAppBootstraper {

    private static final String DELIMITER = ",";
    private final String itemsFileLocation;
    private final Properties dictionary;

    private final int namePosition;
    private final int pricePosition;

    public CoffeeAppFileBootstraper(String itemsFileLocation) throws CoffeeException {
        this.itemsFileLocation = itemsFileLocation;
        this.dictionary = ConfigReader.read("dictionary.properties");
        Properties properties = ConfigReader.read("config.properties");
        List<String> dbStructure = Arrays.asList(((String) properties.get("dbStructure")).split(";"));
        namePosition = dbStructure.indexOf((String) dictionary.get("name"));
        pricePosition = dbStructure.indexOf((String) dictionary.get("price"));
    }

    @Override
    public List<Item> loadItems() throws CoffeeException {
        return getRecords(itemsFileLocation).stream().
                filter(row -> !isExtra(row))
                .map(row -> new Item(row.get(namePosition), new BigDecimal(row.get(pricePosition)), isBev(row)))
                .collect(Collectors.toList());
    }

    @Override
    public List<Extra> loadExtras() throws CoffeeException {
        return getRecords(itemsFileLocation).stream().
                filter(this::isExtra)
                .map(row -> new Extra(row.get(namePosition), new BigDecimal(row.get(pricePosition)), isBev(row)))
                .collect(Collectors.toList());
    }

    private boolean isExtra(List<String> row) {
        return row.contains((String) dictionary.get("extra"));
    }

    private boolean isBev(List<String> row) {
        return row.contains((String) dictionary.get("beverage"));
    }

    private List<List<String>> getRecords(String fileLocation) throws CoffeeException {
        List<List<String>> records = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(fileLocation))) {
            while (scanner.hasNextLine()) {
                records.add(getRecordFromLine(scanner.nextLine()));
            }
        } catch (FileNotFoundException e) {
            throw new CoffeeException(e.getMessage());
        }
        return records;
    }

    private List<String> getRecordFromLine(String line) {
        List<String> values = new ArrayList<>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(DELIMITER);
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next().trim());
            }
        }
        return values;
    }

}
