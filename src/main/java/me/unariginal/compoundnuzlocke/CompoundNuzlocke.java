package me.unariginal.compoundnuzlocke;

import me.unariginal.compoundnuzlocke.commands.NuzlockeCommands;
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

    @Override
    public void onInitialize() {
        LOGGER.info("[Nuzlocke] Loading Mod...");
        instance = this;

        new NuzlockeCommands();
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            mcServer = server;
        });
    }

    public static CompoundNuzlocke getInstance() {
        return instance;
    }
}
