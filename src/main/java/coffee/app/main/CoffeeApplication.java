package coffee.app.main;

import coffee.app.main.logic.CommandReader;
import coffee.app.main.tools.CoffeeException;
import coffee.app.main.tools.ConfigReader;
import coffee.app.model.Item;
import coffee.app.model.command.Command;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.TreeMap;

public class CoffeeApplication implements Runnable {

    private final Map<String, Item> items = new TreeMap<>();

    private Scanner scanner;
    private CommandReader commandReader;
    private Properties properties;

    public void bootstrap(String itemsFileLocation) throws CoffeeException {

        CoffeeAppBootstraper bootstraper;

        if (itemsFileLocation != null) {
            bootstraper = new CoffeeAppFileBootstraper(itemsFileLocation);
        } else {
            bootstraper = new CoffeeAppDefaultBootstraper();
        }

        bootstraper.loadItems().forEach(item ->
                items.put(item.getName().toLowerCase(Locale.ROOT), item));
        bootstraper.loadExtras().forEach(extra ->
                items.put(extra.getName().toLowerCase(Locale.ROOT), extra));

        scanner = new Scanner(System.in);
        commandReader = new CommandReader();

        properties = ConfigReader.read("config.properties");

    }

    public String process(String commandString) throws CoffeeException {
        Command command = commandReader.getCommand(commandString, this);
        return command.execute();
    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        while (true) {
            System.out.print("> ");
            String command = scanner.nextLine();
            try {
                System.out.println(this.process(command));
            } catch (CoffeeException e) {
                System.out.printf("There was a problem while processing the command %s - %s%n", command, e.getMessage());
            }
        }
    }

    public String getProperty(String key) {
        return ((String) properties.getOrDefault(key, "Not found config property: " + key));
    }

    public Item getItem(String name) {
        return items.get(name);
    }

    public ArrayList<Item> getItems() {
        return new ArrayList<>(items.values());
    }

}
