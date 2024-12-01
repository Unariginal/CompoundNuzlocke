package me.unariginal.compoundnuzlocke.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.unariginal.compoundnuzlocke.CompoundNuzlocke;
import me.unariginal.compoundnuzlocke.datatypes.PlayerData;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Path;
import java.util.*;

public class Config {
    private final Map<String, PlayerData> playerDataMap = new HashMap<>();

    public Config() {
        try {
            checkFiles();
        } catch (IOException e) {
            e.printStackTrace();
            CompoundNuzlocke.LOGGER.error("Failed to generate default configuration files.");
        }
        //loadConfig();
        getAndLoadPlayerFiles();
    }

    private void checkFiles() throws IOException {
        Path mainPath = FabricLoader.getInstance().getConfigDir().resolve("CompoundNuzlocke");
        File mainFile = mainPath.toFile();
        if (!mainFile.exists()) {
            try {
                mainFile.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Path configPath = FabricLoader.getInstance().getConfigDir().resolve("CompoundNuzlocke/config.json");
        File configFile = configPath.toFile();
        if (!configFile.exists()) {
            configFile.createNewFile();

            InputStream in = CompoundNuzlocke.class.getResourceAsStream("/config.json");
            OutputStream out = new FileOutputStream(configFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }

            in.close();
            out.close();
        }
    }

    private void getAndLoadPlayerFiles() {
        Path playerDataPath = FabricLoader.getInstance().getConfigDir().resolve("CompoundNuzlocke/playerdata");
        File playerDataFile = playerDataPath.toFile();
        if (!playerDataFile.exists()) {
            try {
                playerDataFile.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        CompoundNuzlocke.LOGGER.info("[Nuzlocke] Loading player data files...");
        for (File playerData : Objects.requireNonNull(playerDataFile.listFiles())) {
            if (playerData.getName().contains(".json")) {
                JsonElement root;
                try {
                    root = JsonParser.parseReader(new FileReader(playerData));
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }

                JsonObject playerDataRootObject = root.getAsJsonObject();

                String playerUUID = playerDataRootObject.get("uuid").getAsString();
                String username = playerDataRootObject.get("username").getAsString();
                boolean runActive= playerDataRootObject.get("runActive").getAsBoolean();
                JsonObject rules = playerDataRootObject.getAsJsonObject("rules");
//                ArrayList<JsonObject> ruleList = new ArrayList<>();
//                for (String ruleName : rules.keySet()) {
//                    JsonObject ruleObject = rules.getAsJsonObject(ruleName);
//                    ruleList.add(ruleObject);
//                }

                PlayerData data = new PlayerData(playerUUID, username, runActive, rules);
                playerDataMap.put(playerData.getName().replaceAll(".json", ""), data);
            }
        }
    }

    public File createPlayerFile(String uuid) throws IOException {
        Path playerDataPath = FabricLoader.getInstance().getConfigDir().resolve("CompoundNuzlocke/playerdata/" + uuid + ".json");
        File playerData = playerDataPath.toFile();
        if (!playerData.exists()) {
            playerData.createNewFile();
        }

        return playerData;
    }

    public void updatePlayerData(String uuid, String username, boolean runActive, JsonObject rules) {
        PlayerData data = new PlayerData(uuid,username,runActive,rules);
        playerDataMap.remove(uuid);
        playerDataMap.put(uuid, data);
    }

    public Map<String, PlayerData> getPlayerDataMap() {
        return playerDataMap;
    }
}
