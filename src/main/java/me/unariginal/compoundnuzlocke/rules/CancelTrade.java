package me.unariginal.compoundnuzlocke.rules;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kotlin.Unit;

/***************************
    * Sets the tradeable property to false on Starter & Caught Pokemon
    ****************************/
    
public class CancelTrade {
        private boolean enabled = true;

        public CancelTrade() {
            CobblemonEvents.POKEMON_CAPTURED.subscribe(Priority.NORMAL,event -> {
                Pokemon pokemon = event.getPokemon();
                pokemon.setTradeable(false);
               return Unit.INSTANCE;
            });
            CobblemonEvents.STARTER_CHOSEN.subscribe(Priority.NORMAL,event -> {
                Pokemon pokemon = event.getPokemon();
                pokemon.setTradeable(false);
                return Unit.INSTANCE;
            });
        }

        public boolean isEnabled() {

            return enabled;
        }

        public void setEnabled(boolean status) {
            enabled = status;
        }
    }
