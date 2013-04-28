package net.argius.stew.command;

import static net.argius.stew.text.TextUtilities.join;

import java.io.*;
import java.sql.*;
import java.util.*;

import net.argius.stew.*;
import net.argius.stew.io.*;

/**
 * The Load command used to execute SQL from a file.
 *
 * This command has two mode:
 *   if it gived one argument, it will execute SQL read from a file,
 *   or it it will load data from file.
 *
 * The file type to load will be automatically selected by file's extension:
 * @see Importer
 */
public class Load extends Command {

    private static final Logger log = Logger.getLogger(Load.class);

    @Override
    public void execute(Connection conn, Parameter parameter) throws CommandException {
        if (!parameter.has(1)) {
            throw new UsageException(getUsage());
        }
        try {
            final File file = resolvePath(parameter.at(1));
            if (log.isDebugEnabled()) {
                log.debug("file: " + file.getAbsolutePath());
            }
            if (parameter.has(2)) {
                final String table = parameter.at(2);
                final boolean hasHeader = parameter.at(3).equalsIgnoreCase("HEADER");
                if (log.isDebugEnabled()) {
                    log.debug("table: " + table);
                    log.debug("hasHeader: " + hasHeader);
                }
                loadRecord(conn, file, table, hasHeader);
            } else {
                loadSql(conn, file);
            }
        } catch (IOException ex) {
            throw new CommandException(ex);
        } catch (SQLException ex) {
            throw new CommandException(ex);
        }
    }

    private void loadSql(Connection conn, File file) throws IOException, SQLException {
        final String sql = readFileAsString(file);
        if (log.isDebugEnabled()) {
            log.debug("sql : " + sql);
        }
        Statement stmt = prepareStatement(conn, sql);
        try {
            if (isSelect(sql)) {
                ResultSet rs = executeQuery(stmt, sql);
                try {
                    ResultSetReference ref = new ResultSetReference(rs, sql);
                    output(ref);
                    outputMessage("i.selected", ref.getRecordCount());
                } finally {
                    rs.close();
                }
            } else {
                final int count = stmt.executeUpdate(sql);
                outputMessage("i.proceeded", count);
            }
        } finally {
            stmt.close();
        }
    }

    protected void loadRecord(Connection conn, File file, String tableName, boolean hasHeader) throws IOException, SQLException {
        Importer importer = Importer.getImporter(file);
        try {
            final Object[] header;
            if (hasHeader) {
                header = importer.nextRow();
            } else {
                Importer importer2 = Importer.getImporter(file);
                try {
                    Object[] a = importer2.nextRow();
                    Arrays.fill(a, "");
                    header = a;
                } finally {
                    importer2.close();
                }
            }
            final List<Object> headerList = Arrays.asList(header);
            final String columns = (hasHeader) ? String.format("(%s)", join(",", headerList)) : "";
            final List<Object> valueList = new ArrayList<Object>(headerList);
            Collections.fill(valueList, "?");
            final String sql = String.format("INSERT INTO %s %s VALUES (%s)",
                                             tableName,
                                             columns,
                                             join(",", valueList));
            if (log.isDebugEnabled()) {
                log.debug("SQL : " + sql);
            }
            PreparedStatement stmt = conn.prepareStatement(sql);
            try {
                insertRecords(stmt, importer);
            } finally {
                stmt.close();
            }
        } finally {
            importer.close();
        }
    }

    protected void insertRecords(PreparedStatement stmt, Importer importer) throws IOException, SQLException {
        int recordCount = 0;
        int insertedCount = 0;
        int errorCount = 0;
        while (true) {
            Object[] row = importer.nextRow();
            if (row == null || row.length == 0) {
                break;
            }
            ++recordCount;
            try {
                for (int i = 0; i < row.length; i++) {
                    int index = i + 1;
                    Object o = row[i];
                    stmt.setObject(index, o);
                }
                insertedCount += stmt.executeUpdate();
            } catch (SQLException ex) {
                String message = "error occurred at " + recordCount;
                if (log.isTraceEnabled()) {
                    log.trace(message, ex);
                } else if (log.isDebugEnabled()) {
                    log.debug(message + " : " + ex);
                }
                ++errorCount;
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("record   = " + recordCount);
            log.debug("inserted = " + insertedCount);
            log.debug("error    = " + errorCount);
        }
        outputMessage("i.loaded", insertedCount, recordCount);
    }

}
