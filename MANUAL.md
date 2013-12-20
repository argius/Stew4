% Stew4 User Manual
%
% version 4.1


## What Is Stew ?

Stew is the database (SQL) tool with JDBC.

For details, see "README.md".


## PRECAUTIONS

### Password Management

By defaults, passwords are not encrypted.
Setting "Encryption" in the connector will encrypt the password.

For details, see [How To Use - Configuration Of "Connector"](#configuration-of-connector).


### Rollback Does Not Work When The Connection Is Disconnecting

By defaults, Rollback does not work automatically on disconnect.
You have to care that some of DBMSs commit on disconnect.

Setting "Auto Rollback" in the connector will rollback automatically on disconnect.

For details, see [How To Use - Configuration Of "Connector"](#configuration-of-connector).


### Other

For more information, see the project site.
<http://stew.sourceforge.jp/>  


## Installation

Just extract the package archive file.


## Running App

If you want to use GUI mode, execute the following command:

    > java -jar stew.jar --gui

If you want to use CUI mode, execute the following command:

    > java -jar stew.jar --cui

At first time, a system directory ".stew" created. It is used by some configurations.
By default, ".stew" creates at the current directory.


## Uninstallation

Remove all of installed files and ".stew" directory.


----------------------------------------------------------------------------------------------------

## Usage

To access database with Stew, you need DBMS that supports JDBC and its JDBC driver.
For details, see the manual of DBMS.


### Configuration Of "Connector"

In Stew, an asset of JDBC Connection called it "Connector".

In CUI mode, to start edit tool, specifies --edit option.

    # command line
    $ stew --edit
    # running stew
    > --edit

In GUI mode, executes "Edit Connectors" to start edit dialog.


Connector has the following properties:

Connector ID
:   ID for connector command. Allows only ASCII letters (A-Za-z0-9).

Connector Name
:   The name to display at Prompt.

Classpath
:   Specifies classpath of JDBC driver. This form is same with -CLASSPATH option.

Driver
:   Specifies the driver class that implements java.sql.Driver.
    (In GUI mode, you can use the "search Driver" button.)

URL to connect
:   Specifies JDBC URL (same as url at DriverManager.getConnection(url)).

User
:   Specifies the user ID.

Password
:   Specifies the password of its user ID.

Encryption
:   Specifies the type to save password into file.
    See the next section.

Readonly
:   Is set to READONLY the connection, so that you can not run the commands that update.
 (., Which according to ReadOnly the command side)

Auto Rollback
:   If it was set, the transaction will be rollbacked automatically when disconnect.



### About Password Management

How to save the password, you can select the encryption process.

PlainTextPassword
:   Saves as plain text. (default)

PbePassword
:   You can store your password by using the PBE encryption.


### Anonymous Connector
### Interactive Mode
### Non-interactive Mode
### Alias
## Commands
### connect - Connects to database (built-in)

    > connect <connector-id>
    > -c <connector-id>

This command connects to database with prepared Connector.

In interactive mode, connection is kept until disconnect.
If already connected, disconnect it first, then connect.


### disconnect - Disconnects from database (built-in)

    > disconnect
    > -d

This command disconnects from database.

If auto-rollback was available, try to rollback before disconnecting.


### commit - Commits current transaction (built-in)

This command commits current transaction.

** caution: Commit cannot cancel. Please use carefully. **


### rollback - Rollbacks current transaction (built-in)

This command rollbacks a current transaction.


### -e - Evaluates multiple commands (built-in)

    > -e <command> -e <command> ...

For example, you can execute two export command continuously,
 or can execute as batch in non-interactive mode.


### -f - Executes file content as command (built-in)

    > -f <file>

...


### -s - Executes file content as script (built-in) \<improved feature in version 4.1\>

    > -s . | <SCRIPT FILE> | <SCRIPT-NAME> <SCRIPT-CODE>

 1. If the first argument is ".", the Script Context is reset.
 2. If the first argument is a filename with extension, the script language is identified by the extension, and the file is run as the script language's script.
 3. Otherwise, the first argument is recognized as the name of a script language, and the rest of arguments are executed as the script language's script.

(Only the built-in JavaScript by file is supported in version 4.0.)

To use script languages except the built-in script language (JavaScript), it requires each script engine of script languages such as jruby, jython and groovy.

The Script Context keeps global scope bindings, and following scripts can reuse the bindings.
If you want to reset Script Context, input " -s . " above.


The following variables will be available in the script (global scope):

 * the reference of current connection: connection, conn
 * the reference of Parameter: parameter, p
 * the reference of OutputProcessor: outputProcessor, op


### cd - Changes current directory (built-in)

    > cd <directory>

Changes current directory to specified one.
(This current directory is not OS's, but internal Stew.)


### @ - Displays directories (built-in)

    > @

Displays the paths of current directory and system directory.


### ? - Displays System Properties (built-in)

    > ? [<property-name> [<property-name-2> ... <property-name-n>]]

Display System Properties. (System.getProperty)

By defauts, it displays JRE, OS and Locale.

    > ?
    JRE : ...
    OS : ...
    Locale : ja_JP
    > ? java.version user.timezone zzz
    java.version=[1.6.0_##]
    user.timezone=[Europe/London]
    zzz=undefined


### alias - Registers alias (built-in)

    > alias [<name> [<command>]]

Registers the alias of command.
When called with an argument, display the alias and its command.
When called without arguments, display all aliases.

Before registering or displaying, refreshes cached info in memory.
If modified aliases in another process, includes you directly edit the config file,
run this command.

(implementation details:
 for the infinite loop suppression of circular references,
 the depth of the expansion up to 100.)

    > alias
    No aliases.
    > alias search select * from
    > alias count select count(*) from
    > alias search
    alias search=[select * from]
    > search table1
    >> select * from table1
    (diplays the results of "select * from table1")
    >


### unalias - Removes alias (built-in)

    > unalias <alias>

If the specified alias does not exist, does nothing.
Whether the target exists or not, refreshes cached info in memory.


### exit - Exits application (built-in)

    > exit

This command exits Stew without confirmation.

All connections will be disconnected automatically.
Rollback will do only the connector set auto-rollback option.
For automatic rollback, see [Usage - Configuration Of "Connector"](#configuration-of-connector).


### load - Executes file content as SQL

    > load [<SQL-file> | <data-file> <table-name> [ HEADER ]]

This command executes the SQL read from specified file.

When If a parameter is specified, executes file content as SQL,
otherwise, two or more parameters, imports the file as data file.
The file format is automatically selected by file extension.
 *. .csv : CSV format
 *. .xml : XML format (definition: src/net/argius/stew/io/stew-table.dtd)
 *. otherwise: TSV format

(implementation detail: this is basically same as import command,
                        executes as not batch but executes by record)


### import - Imports from file

    import <data-file> <table-name> [ HEADER ]

This command imports from file as data file.
The file format is automatically selected by file extension.
 *. .csv : CSV format
 *. .xml : XML format (definition: src/net/argius/stew/io/stew-table.dtd)
 *. otherwise: TSV format

(implementation detail: this is basically same as load command,
                        but uses Statement#addBatch.)


### export - Exports to file

This command exports result of command to the specified file.
The file format is automatically selected by file extension.
 *. .htm,html : HTML format
 *. .csv : CSV format
 *. .xml : XML format (definition: src/net/argius/stew/io/stew-table.dtd)
 *. others: TSV format


### time - Measures time to execute SQL

    > time [<count>] <SQL>

This command executes specified SQL and measures its execution time.

If specified count, executes SQL by count continuously
 and summaries "Total", "Ave(average)", "Max" and "Min".
Otherwise, executes once and measure its execution time.

    > time select * from EMPLOYEE
    execution time: 0.093 seconds
    > time 100 select * from EMPLOYEE
    Total: 0.484 seconds
      Ave: 0.005 seconds
      Max: 0.094 seconds
      Min: 0.000 seconds
    >


### find - Finds table names

    > find <table-name-pattern> [<table-type-pattern> [<schema-pattern> [<catalog-pattern> [ FULL ]]]]

This command displays list of tables allowed to show.
The parameter including "pattern" in its name can be specified wildcard (#,?).


### report - Reports database info

    > report - | <table-name> [ FULL | PK | INDEX ]

This command displays the info about current connection and connecting database.

If specifies - (hyphen), displays the name and version of database and JDBC driver,
 user and JDBC address.

If only specifies table name, displays the column info about specified table.

If specifies table name and option,
displays the primary key or index info about specified table.


### download - Downloads a column as file

    > download <root-dir> SELECT <data-column> [, file-path...] FROM ...

This command downloads a column data by rows and saves to file.

For any data type.
It is useful for downloading large text data or large objects (BLOB, CLOB) all at once.

In order to be able to download multiple files,
generates file name using column data.

The file name will be generated by concatenate second column and after as string.
This supporses to specify primary keys and file extension.

    > download emp select FULL_NAME, JOB_COUNTRY, '/', EMP_NO, '.txt' from EMPLOYEE
    The dir[./emp/USA] was created.
    Downloaded. (size=14bytes, file=./emp/USA/2.txt)
    Downloaded. (size=12bytes, file=./emp/USA/4.txt)
     .
     .
     .
    Downloaded. (size=12bytes, file=./emp/USA/24.txt)
    The dir[./emp/England] was created.
    Downloaded. (size=11bytes, file=./emp/England/28.txt)
     .
     .
     .
    Downloaded. (size=18bytes, file=./emp/USA/145.txt)
    Selected 42 records.

When data exist a record, specified <data-column>,
saves file to <root-dir> as file name.

Saving file already exists or no permission to write file,
the process will abort immediately.


### upload - Uploads file data to a column

    > upload <file> <SQL(UPDATE|INSERT)>

This command registers a data file to the column that specified by place-holder.


### wait - Waits for specified interval

    > wait seconds()

This command waits for the interval that specified number of seconds.
It may be useful for executing multiple commands.



## The GUI Mode

### Common Behaviors

Standard commands, "undo", "redo", "cut", "copy", "paste", and "select all", can use in all text components via context menu or shortcut key.


### Main Window

A window has "Input/Output Area", "Result Set Table", "Database Info Tree", "Main Menu", "Status Bar" and "Find Pane".
By default, the database info tree, the status bar and the find pane" are hidden.


### Input/Output Area

The I/O area is similar to CUI interface.

... When cursor is at the end of textarea, executes command which is string from prompt to the end of textarea.
... Otherwise, move cursor to the end of textarea.
... The part before prompt can't edit.


... Context menus for this component has the standard commands.


### Result Set Table

The result set table displays command results, usually query results from database.

The row header (the leftmost of the table) displays the row number.
... display "+" sign instead of the row number in the row header.

Automatic column width adjustment ...


The context menu for this component are listed below:


Sorts clicked column
:   This command sorts clicked column, not a selected one.
    The same column is sorted continuously, ... reverse.

Copy (Ctrl-C)
:   This command copies selected cell values as text into the clipboard.

Copy With Escape (Ctrl+Shift-C)
:   This command copies selected cell values as quoted strings into the clipboard.

Paste (Ctrl-V)
:   This command pastes values into selected cells from the clipboard.
    Type conversion from the text to column types of the cells depends on each JDBC driver implementation.

Select All (Ctrl-A)
:   This command selects all cells in the table.

Clear Selected Cell Value
:   This command clears selected cell values (actually sets NULL).

Set Current Time Value
:   This command sets current time (Timestamp) into selected cells.

Copy Column Names
:   This command copies the table header as text into the clipboard.

Find Column Name
:   This command shows the find pane and sets find target ...

Add New (Empty) Row
:   This command adds a new empty row into the table.

Insert From Clipboard
:   This command ... imports data from the clipboard and inserts the data into the linking table.

Duplicate Rows
:   This command adds duplicate rows ... selected cells.

Link Rows Into Database
:   This command make unlink  ...

Delete Rows
:   This command deletes selected rows.
    The deleted rows are also deleted from database.



### Database Info Tree

The database info tree is a tree view of hierarchical structure, which consists
 catalogs, schemas, tables, table type and columns as a tree.
Not connected: blank ...
just connected: connector as a root node and sub nodes ...
Each node expands ...


An "Auto-expansion" is a function to automatically expands nodes in the database info tree when connecting a database.
If you use it, create a TSV file named "autoexpansion.tsv" in the system directory (.stew) and write node name lists as TSV into this file.


The context menu for this component are listed below:


Copy
:   This command copies displayed strings of selected nodes as text into the clipboard.

Copy Simple Name
:   This command copies the simple name of selected nodes as text into the clipboard.

Copy Full Name
:   This command copies the full qualified name of selected nodes as text into the clipboard.

Refresh
:   This command ... 

Generate WHERE Phrase
:   This command generates a WHERE phrase consisting selected tables and columns ... 
    selected same name, generate ...

Generate SELECT Statement (with WHERE)
:   This command generates a SELECT statement with "WHERE" keyword.

Generate UPDATE Statement (with WHERE)
:   This command ... 

Generate INSERT Statement
:   This command ... 

Jump To Column By Name
:   This command ... the name of selected node. Jump to the column which has the same name in current result table if found it.
    Double-clicking the node is the same as this command.

Toggle Show Column Number
:   This command provides the toggle between showing and hiding the column number of all columns nodes.


### Main Menu

See Menus ...


### Status Bar

... Display last command and its execution time.
...This time is not query but command.


### Saving Configuration

...


### Key Binding

...
It is not able to change key bind after opening new window.
... For the keywords, see https://github.com/argius/Stew4/blob/master/src/net/argius/stew/ui/window/Menu.u8p


## Menus in the GUI Mode

### File - New Window (N) Ctrl-N

This command opens a new window.
Current connections are independent of each window.


### File - Close Window (C) Ctrl-W

This command closes current window.
If connected, show confirm dialog.

If current window is only a window, processes as Quit(Q).


### File - Quit (Q) Ctrl-Q

This command quits this application.
When executed, shows confirm dialog. Click "Yes" to quit.


### Edit - Cut (T) Ctrl-X

This command cuts the selection to the clipboard.


### Edit - Copy (C) Ctrl-C

This command copies the selection to the clipboard.


### Edit - Paste (P) Ctrl-V

This command pastes the selection to the clipboard.


### Edit - Select All (A) Ctrl-A

This command selects all text or all elements.


### Edit - Find (F) Ctrl-F

This command finds out a specific string from selected component.

...


### Edit - Toggle focus (G) Ctrl-G

This command toggles focus between the result set table and the input/output area in a current window.

### Edit - Clear Message (M)

This command clears input/output area in a current window.


### View - Show Status Bar (S)

This command toggles the visibility of status bar in a current window.


### View - Show Column Number (C)

This command toggles the visibility of column number in the result set table of a current window.


### View - Show Info Tree (I)

This command toggles the visibility of database info tree in a current window.


### View - Always On Top (T)

This command toggles the state that always stays on top of a current window.


### View - Refresh (R) F5

This command ...


### View - Widen Column Width (W) Ctrl-.(period)

This command widens all column widths in the result set table of a current window.


### View - Narrow Column Width (N) Ctrl-,(comma)

This command narrows all column widths in the result set table of a current window.


### View - Adjust Column Width (A) Ctrl-/(slash)

This command adjusts all column widths in the result set table of a current window.
For about adjust mode, see the next item.


### View - Auto Adjust Mode (M)

This command selects auto-adjusting mode to use when shows result.

There are following modes:

 * None(N): does nothing.
 * Header(H): adjusts column width based on each header string.
 * Value(V): adjusts based on the longest string in column values.
 * Header And Value(A): adjusts based on the longest string in column values and column header string.


### Command - Execute (X) Ctrl-M

This command executes command.
This is same as pressing enter key in the end of the I/O area. (... same behavior)


### Command - BREAK (B) Ctrl-Pause(Break)

This command cancels executing command.
The process on the server is not canceled.


### Command - History Back (P) Ctrl-Up

This command rotates command histories backward.


### Command - History Next (N) Ctrl-Down

This command rotates command histories forward.


### Command - Rollback (R)

This command displays confirm dialog and click "OK" to rollback.


### Command - Commit (M)

This command displays confirm dialog and click "OK" to commit.


### Command - Connect (C) Ctrl-E

This command shows the list of connectors.
Select connector name you want to connect to, and click "OK" to executes connect command.


### Command - Disconnect (D) Ctrl-D

This command disconnects current connection.
This is the same as disconnect command.


### Command - Post-Proccess (0)

This command specifies the configuration of a "Post-Proccess" mode.

The "Post-Proccess" is an action to notify that command finished.
When the window which command is running, is not active, "Post-Proccess" is invoked.

There are following modes:

 * None(N): Does nothing.
 * Focus(F): Focuses window. 
 * Shake(S): Shakes window.
 * Blink(B): Blinks window.


### Command - Encryption Key (K)

This command shows the dialog to input an encryption key.


### Command - Edit Connectors (E)

This command shows the dialog to edit connectors.


### Data - Sort (S) Alt-S

This command sorts records by selected column.


### Data - Import File (I)

This command imports from file into the table of current result.


### Data - Export File (E) Ctrl-Shift-S

This command exports results into file.


### Help - Show Help

This command shows this help file by default browser.


### Help - About Stew

This command shows version dialog about Stew.



## Properties

You can set these system properties into java -D option or stew.properties file.


### net.argius.stew.properties - Location Of Properties File

Property Value: a file path or a directory path

This property specifies the location of "stew.properties" file.
This properties file is used as system properties and searched in the following steps:

+If it specifies a file path, the file will be used as system properties.
+If it specifies a directory path, the file will be searched a "stew.properties" file in the directory.
+Search a file in the classpath.
+Search a file in the system directory.

The first file found is used.
If a file is not found, ... (ignore it).


### net.argius.stew.directory - Working Directory

Property Value: a directory path (default: current directory)

This property specifies the current directory as a internal state at the start, but it is not a platform one.


### net.argius.stew.query.timeout - Query Timeout

Property Value: integer seconds (default: 0)

This property specifies the query timeout which is used in commands.

If it is under 0, the property was not set.

(implementation detail: for java.sql.Statement#setQueryTimeout)


### net.argius.stew.rowcount.limit - Limit Of Display Result Row Count

Property Value: maximum number of rows (default: Integer.MAX_VALUE, about 2,147,000,000)

This property specifies the maximum number of rows which show in the result set table.
It is not used to output files, such as the export command.


### net.argius.stew.command.Import.batch.limit - Limit Of Import Command Using Batch

Property Value: limit number of rows (default: 10000)

This property is used by the Import command.
It is not used to output files, such as the export command.


### net.argius.stew.ui.window.resident - Regident

Property Value: integer minutes

This is an experimental feature.

The app redraws continuously at the specified interval to prevent swap-out.


### Logger Configurations

Use logging.properties in this package.

