package net.argius.stew.command;

import java.sql.*;

import net.argius.stew.*;

/**
 * The Wait command used to suspend a specified period of time(seconds).
 */
public final class Wait extends Command {

    @Override
    public void execute(Connection conn, Parameter parameter) throws CommandException {
        try {
            final double seconds = Double.parseDouble(parameter.at(1));
            outputMessage("i.wait-time", seconds);
            try {
                Thread.sleep((long)(seconds * 1000L));
            } catch (InterruptedException ex) {
                throw new CommandException(ex);
            }
        } catch (NumberFormatException ex) {
            throw new UsageException(getUsage());
        }
    }

}
