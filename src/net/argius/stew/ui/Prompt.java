package net.argius.stew.ui;

import net.argius.stew.*;

public final class Prompt {

    private final Environment env;

    public Prompt(Environment env) {
        this.env = env;
    }

    @Override
    public String toString() {
        Connector connector = env.getCurrentConnector();
        final String currentConnectorName = (connector == null) ? "" : connector.getName();
        return currentConnectorName + " > ";
    }

}
