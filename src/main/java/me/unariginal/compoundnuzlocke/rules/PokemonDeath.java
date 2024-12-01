package me.unariginal.compoundnuzlocke.rules;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.google.gson.JsonObject;
import kotlin.Unit;
import me.unariginal.compoundnuzlocke.CompoundNuzlocke;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.UUID;

public class PokemonDeath {
    /************************
     * Detect Death
     * Remove Dead Pokemon
     ************************/

    public PokemonDeath() {
        CobblemonEvents.POKEMON_FAINTED.subscribe(Priority.NORMAL, event -> {
            Pokemon pokemon = event.getPokemon();
            UUID pokemonUUID = pokemon.getUuid();
            ServerPlayerEntity player = pokemon.getOwnerPlayer();

            if (player != null) {
                if (getEnabled(player.getUuidAsString())) {
                    if (!event.getPokemon().isWild()) {
                        if (pokemon.isPlayerOwned()) {
                            PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(player);
                            Pokemon partyPokemon = party.get(pokemonUUID);
                            if (partyPokemon != null) {
                                party.remove(partyPokemon);
                            }
                        }
                    }
                }
            }
            return Unit.INSTANCE;
        });
    }

    public boolean getEnabled(String uuid) {
        boolean enabled = false;
        JsonObject rules = CompoundNuzlocke.getInstance().config.getPlayerDataMap().get(uuid).rules();
        for (String obj : rules.keySet()) {
            if (obj.equals("pokemon_death")) {
                JsonObject pokemon_death = rules.getAsJsonObject(obj);
                enabled = pokemon_death.get("enabled").getAsBoolean();
                CompoundNuzlocke.LOGGER.info("[Nuzlocke] Found Rule");
            }
        }

        return enabled;
    }
}