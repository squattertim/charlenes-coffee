package coffee.app.model.command.basic;

import coffee.app.model.command.Command;

public class EmptyOrderCommand implements Command {
    @Override
    public String execute() {
        return "Empty order! = )";
    }
}
