package coffee.app.model.command;

import coffee.app.main.tools.CoffeeException;

public interface Command {
    String execute() throws CoffeeException;
}
