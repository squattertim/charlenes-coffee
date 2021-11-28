package coffee.app.main;

import coffee.app.main.tools.CoffeeException;
import coffee.app.model.Extra;
import coffee.app.model.Item;

import java.util.List;

public interface CoffeeAppBootstraper {

    List<Item> loadItems() throws CoffeeException;

    List<Extra> loadExtras() throws CoffeeException;
}
