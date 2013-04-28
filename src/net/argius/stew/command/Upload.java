package net.argius.stew.command;

import java.io.*;
import java.sql.*;

import net.argius.stew.*;

/**
 * The Upload command used to upload a file into specified column.
 */
public final class Upload extends Command {

    private static final Logger log = Logger.getLogger(Upload.class);

    @Override
    public void execute(Connection conn, Parameter parameter) throws CommandException {
        if (!parameter.has(2)) {
            throw new UsageException(getUsage());
        }
        final File file = resolvePath(parameter.at(1));
        final String sql = parameter.after(2);
        if (log.isDebugEnabled()) {
            log.debug("file: " + file.getAbsolutePath());
            log.debug("SQL: " + sql);
        }
        if (file.length() > Integer.MAX_VALUE) {
            throw new CommandException("file too large: " + file);
        }
        final int length = (int)file.length();
        try {
            InputStream is = new FileInputStream(file);
            try {
                PreparedStatement stmt = conn.prepareStatement(sql);
                try {
                    setTimeout(stmt);
                    stmt.setBinaryStream(1, is, length);
                    final int updatedCount = stmt.executeUpdate();
                    outputMessage("i.updated", updatedCount);
                } finally {
                    stmt.close();
                }
            } finally {
                is.close();
            }
        } catch (IOException ex) {
            throw new CommandException(ex);
        } catch (SQLException ex) {
            throw new CommandException(ex);
        }
    }

}
