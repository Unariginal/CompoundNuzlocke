package me.unariginal.compoundnuzlocke.rules;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kotlin.Unit;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

public class PokemonDeath {
    private boolean enabled = true;

    /************************
     * Detect Death
     * Remove Dead Pokemon
     ************************/

    public PokemonDeath() {
        CobblemonEvents.POKEMON_FAINTED.subscribe(Priority.NORMAL, event -> {
            Pokemon pokemon = event.getPokemon();
            if (!event.getPokemon().isWild()) {
                if (pokemon.isPlayerOwned()) {
                    UUID pokemonUUID = pokemon.getUuid();
                    ServerPlayerEntity player = pokemon.getOwnerPlayer();
                    if (player != null) {
                        PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(player);
                        Pokemon partyPokemon = party.get(pokemonUUID);
                        if (partyPokemon != null) {
                            party.remove(partyPokemon);
                        }
                    }
                }
            }
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