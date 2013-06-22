% New Features And Changes In Stew4


## New Features

The new features in version 4 are listed below:

Special command "-s" - Executing file as script
:   Execute a file as script (JavaScript).
    This feature can be used instead of a command class.

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
