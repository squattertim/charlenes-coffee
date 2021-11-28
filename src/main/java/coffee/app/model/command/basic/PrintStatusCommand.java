package coffee.app.model.command.basic;

import coffee.app.main.CoffeeApplication;
import coffee.app.model.command.Command;

public class PrintStatusCommand implements Command {

    public PrintStatusCommand(CoffeeApplication context) {
    }

    @Override
    public String execute() {
        return "Ready";
    }
}
