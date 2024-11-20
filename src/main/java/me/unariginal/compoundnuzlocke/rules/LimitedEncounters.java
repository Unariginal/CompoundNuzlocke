package me.unariginal.compoundnuzlocke.rules;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.pokeball.ThrownPokeballHitEvent;
import com.cobblemon.mod.common.api.spawning.detail.PokemonSpawnDetail;
import com.cobblemon.mod.common.api.tags.CobblemonBiomeTags;
import com.cobblemon.mod.common.entity.pokeball.EmptyPokeBallEntity;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kotlin.Unit;
import me.unariginal.compoundnuzlocke.CompoundNuzlocke;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

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
        CobblemonEvents.THROWN_POKEBALL_HIT.subscribe(Priority.NORMAL, event -> {
            PokemonEntity eventPokemon = event.getPokemon();
            World eventWorld = eventPokemon.getWorld();
            RegistryEntry<Biome> biome = eventWorld.getBiome(eventPokemon.getBlockPos());


            if (biome.getKey().isPresent()) {
                if (biome.getKey().get() == BiomeKeys.FOREST){
                    CompoundNuzlocke.LOGGER.info("It's in a forest!");
                    if (eventPokemon.isBattling()) {
                        if (eventPokemon.getBattleId() != null) {
                            PokemonBattle eventBattle = Cobblemon.INSTANCE.getBattleRegistry().getBattle(eventPokemon.getBattleId());
                            if (eventBattle != null) {
                                eventBattle.end();
                            }
                        }
                    }
                    event.getPokeBall().setCaptureState(EmptyPokeBallEntity.CaptureState.NOT);
                }
            }

            return Unit.INSTANCE;
        });
    }

    public boolean isEnabled(){
        return enabled;
    }

    public void setEnabled(boolean status){
        enabled = status;
    }
}
