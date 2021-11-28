package coffee.app.main;

import coffee.app.main.tools.CoffeeException;

public class CoffeeAppStarter {

    public static void main(String[] args) {

        CoffeeApplication application = new CoffeeApplication();

        try {
            application.bootstrap(args[0]);

            application.run();

        } catch (CoffeeException e) {
            System.out.printf("There was a problem while running the app - %s%n", e.getMessage());
        }

    }
}
