package coffee.app.model.command.basic;

import coffee.app.main.CoffeeApplication;
import coffee.app.model.command.Command;

public class HelpCommand implements Command {

    private final CoffeeApplication context;

    public HelpCommand(CoffeeApplication context) {
        this.context = context;
    }

    @Override
    public String execute() {
        return context.getProperty("help");
    }

}
