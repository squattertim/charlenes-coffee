package coffee.app.main;

import coffee.app.main.tools.CoffeeException;

public class CoffeeAppStarter {

    public static void main(String[] args) {

        CoffeeApplication application = new CoffeeApplication();

        try {

            if(args.length == 1) {
                application.bootstrap(args[0]);
            } else {
                application.bootstrap("src/main/resources/itemsDb.csv");
            }

            application.run();

        } catch (CoffeeException e) {
            System.out.printf("There was a problem while running the app - %s%n", e.getMessage());
        }

    }
}
