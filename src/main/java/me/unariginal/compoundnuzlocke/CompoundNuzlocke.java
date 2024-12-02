package me.unariginal.compoundnuzlocke;

import me.unariginal.compoundnuzlocke.commands.NuzlockeCommands;
import me.unariginal.compoundnuzlocke.config.Config;
import me.unariginal.compoundnuzlocke.rules.CancelTrade;
import me.unariginal.compoundnuzlocke.rules.LimitedEncounters;
import me.unariginal.compoundnuzlocke.rules.PokemonDeath;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompoundNuzlocke implements ModInitializer {
    public static final String MOD_ID = "compoundnuzlocke";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static CompoundNuzlocke instance;
    public MinecraftServer mcServer;
    public Config config;

    @Override
    public void onInitialize() {
        LOGGER.info("[Nuzlocke] Loading Mod...");
        instance = this;

        new NuzlockeCommands();
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            mcServer = server;
            config = new Config();

            new PokemonDeath();
            new CancelTrade();
            new LimitedEncounters();
        });
    }

    public static CompoundNuzlocke getInstance() {
        return instance;
    }
}
