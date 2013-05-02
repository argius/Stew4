package net.argius.stew.ui.window;

/**
 * @see AnyAction
 */
public enum AnyActionKey {

    cut, copy, paste, selectAll, undo, redo,
    //
    execute,
    refresh,
    //
    newWindow,
    closeWindow,
    quit,
    find,
    toggleFocus,
    clearMessage,
    showStatusBar,
    showInfoTree,
    showColumnNumber,
    showAlwaysOnTop,
    widenColumnWidth,
    narrowColumnWidth,
    adjustColumnWidth,
    autoAdjustMode,
    autoAdjustModeNone,
    autoAdjustModeHeader,
    autoAdjustModeValue,
    autoAdjustModeHeaderAndValue,
    executeCommand,
    breakCommand,
    lastHistory,
    nextHistory,
    sendRollback,
    sendCommit,
    connect,
    disconnect,
    postProcessMode,
    postProcessModeNone,
    postProcessModeFocus,
    postProcessModeShake,
    postProcessModeBlink,
    inputEcryptionKey,
    editConnectors,
    sortResult,
    importFile,
    exportFile,
    showHelp,
    showAbout,
    unknown;

    static AnyActionKey of(String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException ex) {
            return unknown;
        }
    }

}