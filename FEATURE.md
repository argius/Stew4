% New Features And Changes In Stew4


## New Features

The new features in version 4.2 are listed below:

Show all histories (GUI)
:   This feature shows all command histories and pastes a chosen item.

Paste file paths into the text area via DnD (GUI)
:   When you drag and drop from file manager into the text area,
    the paths of these files will be pasted to the text area.
    This feature is a part of the similar feature that had been
    implemented in version 3.


The new features in version 4.1 are nothing.


The new features in version 4 are listed below:

Special command "-s" - Executing file as script
:   Execute a file as script.
    This feature can be used instead of a command class.
    (On version 4.0, only JavaScript was supported.)

Special command "?" - display environment info (not OS ENV)
:   Display System Properties. (System.getProperty)
    By defauts, it displays JRE, OS and Locale.

Auto-expanding nodes in the database info tree (GUI)
:   This feature automatically expands nodes in the database info tree
    when connecting a database.
    If you use this feature, make a "autoexpansion.tsv" file
    in system directory.

For more details, see MANUAL.html file.


## Changes

The major changes on version 4.2 are listed below:

change the behavior of toggle-focus (GUI)
:   The command "Toggle focus" will toggle the focus between the result set
    table, the text area and the database info tree after version 4.2.
    Previously, the database info tree was out of the focus group.


The major changes on version 4.1 are listed below:

Enhance Scripting support
:   On version 4.0, the scripting feature (command "-s") was limited,
    such as only JavaScript, only by file, and stateless.
    On version 4.1, it has been improved as follows:

     * Any Script Language (as long as its Script Engine exists)
     * Parameters As Script
     * Script Context (global scope bindings)


Change the layout of Text Search Panel
:   In version 3.0, the information tree had been added, but the search panel
    still had been left below the console window.
    In version 4.1, the search panel has been moved below the status bar
    of the window.


The major changes since version 3 are listed below:

Requires Java6 and later
:   Java5.0 was not supported.
    And replaced with codes including enhancements and new APIs in Java 6.

Implementation
:   Improved implementation with Java6.
    Specifically, Unnecessary classes was removed, and some codes ared
    replaced with more fitting APIs.


## Other Notes

A connector config file (connector.properties) is compatible to version 3.
