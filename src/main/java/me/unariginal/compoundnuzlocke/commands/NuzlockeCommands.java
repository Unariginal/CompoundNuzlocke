package me.unariginal.compoundnuzlocke.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.brigadier.context.CommandContext;
import me.lucko.fabric.api.permissions.v0.Permissions;
import me.unariginal.compoundnuzlocke.CompoundNuzlocke;
import me.unariginal.compoundnuzlocke.config.Config;
import me.unariginal.compoundnuzlocke.rules.CancelTrade;
import me.unariginal.compoundnuzlocke.rules.LimitedEncounters;
import me.unariginal.compoundnuzlocke.rules.PokemonDeath;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NuzlockeCommands {
    CompoundNuzlocke cn = CompoundNuzlocke.getInstance();

    public NuzlockeCommands() {
        CommandRegistrationCallback.EVENT.register(((commandDispatcher, commandRegistryAccess, registrationEnvironment) -> {
            commandDispatcher.register(
                    CommandManager.literal("nuzlocke")
                            .then(
                                    CommandManager.literal("start")
                                            .executes(this::start)
                            )
                            .then(
                                    CommandManager.literal("reload")
                                            .requires(Permissions.require("nuzlocke.reload", 4))
                                            .executes(this::reload)
                            )

            );
        }));
    }

    private int reload(CommandContext<ServerCommandSource> ctx) {
        // TODO: Reload
        cn.config = new Config();
        return 1;
    }

    private int start(CommandContext<ServerCommandSource> ctx) {
        ServerPlayerEntity player = ctx.getSource().getPlayer();
        String uuid = player.getUuidAsString();
        String username = player.getName().getString();

        // TODO: Start
        PokemonDeath deathRule = new PokemonDeath();
        CancelTrade cancelTrade = new CancelTrade();
        LimitedEncounters encountersRule = new LimitedEncounters();

        if (ctx.getSource().isExecutedByPlayer()) {
            if (ctx.getSource().getPlayer() != null) {
                try {
                    File playerFile = cn.config.createPlayerFile(uuid);

                    Path playerDataPath = FabricLoader.getInstance().getConfigDir().resolve("CompoundNuzlocke/playerdata/" + uuid + ".json");
                    File playerData = playerDataPath.toFile();
//                    HashMap<String, Object> data = new HashMap<>();
//                    data.put("uuid", uuid);
//                    data.put("username", username);
//
//                    HashMap<String, Object> rules = new HashMap<>();
//
//                    HashMap<String, Object> limited_encounters = new HashMap<>();
//                    limited_encounters.put("enabled", true);
//                    List<String> biomesUsed = new ArrayList<>();
//                    limited_encounters.put("biomesUsed", biomesUsed);
//                    limited_encounters.put("shiny_override", true);
//
//                    HashMap<String, Object> pokemon_death = new HashMap<>();
//                    pokemon_death.put("enabled", true);
//
//                    HashMap<String, Object> disable_trades = new HashMap<>();
//                    disable_trades.put("enabled", false);
//
//                    rules.put("limited_encounters", limited_encounters);
//                    rules.put("pokemon_death", pokemon_death);
//                    rules.put("disable_trades", disable_trades);
//
//                    data.put("rules", rules);

                    JsonObject root = new JsonObject();
                    root.addProperty("uuid", uuid);
                    root.addProperty("username", username);

                    JsonObject rulesObj = new JsonObject();

                    JsonObject limited_encountersObj = new JsonObject();
                    limited_encountersObj.addProperty("enabled", true);
                    JsonArray biomesUsedArr = new JsonArray();
                    limited_encountersObj.add("biomesUsed", biomesUsedArr);
                    limited_encountersObj.addProperty("shiny_override", true);

                    JsonObject pokemon_deathObj = new JsonObject();
                    pokemon_deathObj.addProperty("enabled", true);

                    JsonObject disable_tradesObj = new JsonObject();
                    disable_tradesObj.addProperty("enabled", true);

                    rulesObj.add("limited_encounters", limited_encountersObj);
                    rulesObj.add("pokemon_death", pokemon_deathObj);
                    rulesObj.add("disable_trades", disable_tradesObj);

                    root.add("rules", rulesObj);

                    playerData.setWritable(true);
                    playerData.setReadable(true);

                    Gson gson = new GsonBuilder()
                            .setPrettyPrinting()
                            .create();
                    Writer writer = new FileWriter(playerData);
                    gson.toJson(root, writer);

                    ArrayList<JsonObject> ruleList = new ArrayList<>();
                    for (String ruleName : rulesObj.keySet()) {
                        JsonObject ruleObject = rulesObj.getAsJsonObject(ruleName);
                        ruleList.add(ruleObject);
                    }

                    cn.config.updatePlayerData(uuid, username, true, ruleList);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return 1;
    }
}
