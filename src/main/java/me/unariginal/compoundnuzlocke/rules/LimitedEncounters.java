package me.unariginal.compoundnuzlocke.rules;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.pokeball.ThrownPokeballHitEvent;
import com.cobblemon.mod.common.api.tags.CobblemonBiomeTags;

public class LimitedEncounters {
    boolean enabled = true;

    /*
    Limited Encounters:
    The player may only catch the first wild Pokémon encountered in each area (biome), and no others
    If the first wild Pokémon encountered faints or flees, there are no second chances.
    --------------------------------------------------------------------------------------------------
    * Check if player has rule enabled
    * Handel's when players catch a pokemon
    * Check what biome they are in
    * Check if they have caught a pokemon in the biome already
    * cancel the catching of the pokemon
    * display message saying they cant catch another pokemon in this biome
    */

    public LimitedEncounters(){
        CobblemonEvents.THROWN_POKEBALL_HIT.subscribe(Priority.NORMAL, ThrownPokeballHitEvent event -> {

        });

    }

    public boolean isEnabled(){
        return enabled;
    }

    public void setEnabled(boolean status){
        enabled = status;
    }
}
