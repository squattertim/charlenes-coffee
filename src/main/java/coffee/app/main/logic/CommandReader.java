package coffee.app.main.logic;

import coffee.app.main.CoffeeApplication;
import coffee.app.main.logic.parser.OrderParser;
import coffee.app.main.tools.CoffeeException;
import coffee.app.main.tools.ConfigReader;
import coffee.app.model.command.Command;
import coffee.app.model.command.basic.HelpCommand;
import coffee.app.model.command.basic.PrintSortimentCommand;
import coffee.app.model.command.basic.PrintStatusCommand;

import java.util.Properties;

public class CommandReader {

    private final OrderParser orderParser = new OrderParser();

    public CommandReader() throws CoffeeException {
    }

    public Command getCommand(String commandString, CoffeeApplication context) throws CoffeeException {

        Properties dictionary = ConfigReader.read("dictionary.properties");

        if (!commandString.startsWith((String) dictionary.get("order"))) {

            switch (commandString) {
                case "exit":
                    System.exit(1);
                case "status":
                    return new PrintStatusCommand(context);
                case "sortiment":
                    return new PrintSortimentCommand(context);
                default:
                    return new HelpCommand(context);
            }

        } else {
            return processOrder(commandString, context);
        }

    }

    private Command processOrder(String orderString, CoffeeApplication context) throws CoffeeException {
        return orderParser.parse(orderString, context);
    }

}
