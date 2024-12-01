package me.unariginal.compoundnuzlocke.rules;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.google.gson.JsonObject;
import kotlin.Unit;
import me.unariginal.compoundnuzlocke.CompoundNuzlocke;
import net.minecraft.server.network.ServerPlayerEntity;

/***************************
    * Sets the trade-able property to false on Starter & Caught Pokemon
    ****************************/
    
public class CancelTrade {
    public CancelTrade() {
        CobblemonEvents.POKEMON_CAPTURED.subscribe(Priority.NORMAL,event -> {
            ServerPlayerEntity player = event.getPlayer();
            if (getEnabled(player.getUuidAsString())) {
                Pokemon pokemon = event.getPokemon();
                pokemon.setTradeable(false);
            }
            return Unit.INSTANCE;
        });

        CobblemonEvents.STARTER_CHOSEN.subscribe(Priority.NORMAL,event -> {
            ServerPlayerEntity player = event.getPlayer();
            if (getEnabled(player.getUuidAsString())) {
                Pokemon pokemon = event.getPokemon();
                pokemon.setTradeable(false);
            }
            return Unit.INSTANCE;
        });
    }

    public boolean getEnabled(String uuid) {
        boolean enabled = false;
        JsonObject rules = CompoundNuzlocke.getInstance().config.getPlayerDataMap().get(uuid).rules();
        for (String obj : rules.keySet()) {
            if (obj.equals("disable_trades")) {
                JsonObject pokemon_death = rules.getAsJsonObject(obj);
                enabled = pokemon_death.get("enabled").getAsBoolean();
                CompoundNuzlocke.LOGGER.info("[Nuzlocke] Found Rule");
            }
        }

        return enabled;
    }
}