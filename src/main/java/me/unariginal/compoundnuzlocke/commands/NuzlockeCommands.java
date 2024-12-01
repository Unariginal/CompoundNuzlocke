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
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.*;
import java.util.ArrayList;

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

                    JsonObject root = new JsonObject();
                    root.addProperty("uuid", uuid);
                    root.addProperty("username", username);
                    root.addProperty("runActive", true);

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

                    Gson gson = new GsonBuilder()
                            .setPrettyPrinting()
                            .create();
                    Writer writer = new FileWriter(playerFile);
                    gson.toJson(root, writer);

//                    ArrayList<JsonObject> ruleList = new ArrayList<>();
//                    for (String ruleName : rulesObj.keySet()) {
//                        JsonObject ruleObject = rulesObj.getAsJsonObject(ruleName);
//                        ruleList.add(ruleObject);
//                    }

                    writer.close();
                    cn.config.updatePlayerData(uuid, username, true, rulesObj);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return 1;
    }
}
