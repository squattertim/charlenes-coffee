package coffee.app.main.logic.parser;


import coffee.app.main.tools.CoffeeException;
import coffee.app.model.command.expression.AbstractExpression;

public interface Parser {

    AbstractExpression parse(String expressionString) throws CoffeeException;

}
