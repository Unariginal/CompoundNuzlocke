package me.unariginal.compoundnuzlocke.commands;

import com.mojang.brigadier.context.CommandContext;
import me.lucko.fabric.api.permissions.v0.Permissions;
import me.unariginal.compoundnuzlocke.rules.PokemonDeath;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class NuzlockeCommands {
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
        return 1;
    }

    private int start(CommandContext<ServerCommandSource> ctx) {
        // TODO: Start
        PokemonDeath deathRule = new PokemonDeath();
        return 1;
    }
}
