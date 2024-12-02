package me.unariginal.compoundnuzlocke.rules;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.pokeball.ThrownPokeballHitEvent;
import com.cobblemon.mod.common.api.spawning.CobblemonSpawnPools;
import com.cobblemon.mod.common.api.spawning.condition.SpawningCondition;
import com.cobblemon.mod.common.api.spawning.detail.PokemonSpawnDetail;
import com.cobblemon.mod.common.api.spawning.detail.SpawnDetail;
import com.cobblemon.mod.common.api.spawning.detail.SpawnPool;
import com.cobblemon.mod.common.api.tags.CobblemonBiomeTags;
import com.cobblemon.mod.common.entity.pokeball.EmptyPokeBallEntity;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.Species;
import com.cobblemon.mod.common.registry.BiomeTagCondition;
import kotlin.Unit;
import me.unariginal.compoundnuzlocke.CompoundNuzlocke;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

import java.util.List;

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
//                List<Biome> validBiomes = eventWorld.getRegistryManager().get(RegistryKeys.BIOME).stream().filter(e -> getSpawnDetails(eventPokemon.getPokemon().getSpecies()).stream().anyMatch(s -> s.getConditions().stream().anyMatch(c -> canSpawnAt(e, eventWorld, c)))).toList();
//                validBiomes.forEach(biome1 -> {
//                    if (biome1.equals(biome.value()));
//                    switch (biome1) {
//                        case (BiomeTagCondition):
//                            CompoundNuzlocke.LOGGER.info("Forest");
//                    }
//                });

                biome.streamTags().toList().forEach(biomeTagKey -> {
                    CompoundNuzlocke.LOGGER.info("Tag Key: {}", biomeTagKey.toString());
                });

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

//    public List<PokemonSpawnDetail> getSpawnDetails(Species species) {
//        SpawnPool spawnPool = CobblemonSpawnPools.WORLD_SPAWN_POOL;
//        return spawnPool.getDetails().stream().filter(e -> e instanceof PokemonSpawnDetail).map(e -> (PokemonSpawnDetail)e).filter(e -> {
//            if (e.getPokemon().getSpecies() != null) {
//                return e.getPokemon().getSpecies().equals((species.resourceIdentifier.getPath()));
//            }
//            return false;
//        }).toList();
//    }
//
//    public boolean canSpawnAt(Biome biome, World world, SpawningCondition<?> condition) {
//
//        Registry<Biome> registry = world.getRegistryManager().get(RegistryKeys.BIOME);
//        if (condition.getBiomes() == null) return false;
//
//        long count = condition.getBiomes().stream().filter(e -> e.fits(biome, registry)).count();
//
//        return count > 0;
//    }
}
