package coffee.app.model.command.basic;

import coffee.app.main.CoffeeApplication;
import coffee.app.main.tools.ThingsPrinter;
import coffee.app.model.Item;
import coffee.app.model.command.Command;

import java.util.Comparator;
import java.util.stream.Collectors;

public class PrintSortimentCommand implements Command {

    private final CoffeeApplication context;

    public PrintSortimentCommand(CoffeeApplication context) {
        this.context = context;
    }

    @Override
    public String execute() {
        return ThingsPrinter.printWithDetails(context.getItems().stream()
                .sorted(Comparator.comparing(it -> it.getClass().getSimpleName(), Comparator.reverseOrder())
                        .thenComparing(it -> ((Item) it).isBev(), Comparator.reverseOrder()))
                .collect(Collectors.toList()));
    }
}