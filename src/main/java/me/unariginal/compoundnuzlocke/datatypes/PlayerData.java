package me.unariginal.compoundnuzlocke.datatypes;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.UUID;

public record PlayerData(UUID uuid, String username, boolean runActive, ArrayList<JsonObject> rules) {
}
