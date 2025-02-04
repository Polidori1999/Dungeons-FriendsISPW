package it.uniroma2.marchidori.maininterface.factory;

import it.uniroma2.marchidori.maininterface.exception.FactoryException;

import java.lang.reflect.InvocationTargetException;

public class ControllerFactory {

    private ControllerFactory(){}

    public static <T> T createController(Class<T> controllerClass) {
        try {
            // Utilizza il costruttore no-arg per creare una nuova istanza
            return controllerClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException
                 | NoSuchMethodException | InvocationTargetException e) {
            throw new FactoryException(e.getMessage());
        }
    }
}
