% Readme - Stew4


## 1. Outline

Stew is the database (SQL) tool with JDBC.

It had the following features:

 * Requires only Core Libraries except JDBC driver.
 * Console Mode. Not only GUI.
 * Never keep the cursor:
    Statement and ResultSet will be released immediately.
 * Uses only Core APIs.
 * No dependency (version 2 and later):
    No DBMS specific code.
 * Supports Windows, MacOSX and Linux (version 3 and later):
    Excepts "dock mode" of MacOSX.

For more information, see MANUAL.html and FEATURE.md.


## 2. How to Install, Migrate and Uninstall

How to Install
:   Just need to extract zip file. At least "stew.jar".

How to Migrate
:   Just overwrite up-to-date files.

How to Uninstall
:   Just delete installed file and ".stew" dir.
    To see the location of ".stew", exec "@" command in Stew.


## 3. License

Uses Apache License 2.0.
See LICENSE file.

We omitted to write the License header in every souce file.


## 4. Known Issues

Checking the privilege to update (SQL).

Checked only default Look and Feels.
