package me.unariginal.compoundnuzlocke.rules;

public class NotepadClause {
    private boolean enabled = false;

    /*********************************************
     * Handle when player catches a pokemon
     * Check if party is full
     * Cancel adding pokemon to party
     * Instead ask the player (gui?) if they'd like to replace a pokemon in their party or cancel the catch
     * Do the option
     *********************************************/

    public NotepadClause() {

    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean status) {
        enabled = status;
    }
}
