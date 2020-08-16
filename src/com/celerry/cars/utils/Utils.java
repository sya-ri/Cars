package com.celerry.cars.utils;

import com.celerry.cars.Cars;
import com.celerry.cars.obj.GroundVehicle;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class Utils {

    private static final Pattern NEW_LINE = Pattern.compile("\n");
    public static final Map<UUID, GroundVehicle> cars = new HashMap<>();

    public static void addGroundVehicle(UUID uuid, GroundVehicle car) {
        cars.put(uuid, car);
    }

    public static void removeGroundVehicle(UUID uuid) {
        cars.remove(uuid);
    }

    public static Boolean checkIfHas(UUID uuid) {
        return cars.containsKey(uuid);
    }

    public static GroundVehicle getGroundVehicle(UUID uuid) {
        return cars.get(uuid);
    }

    public static void config(Player player, Message message) {
        msg(player, message.toString());
    }

    public static void config(Player player, Message message, Object... replacements) {
        msg(player, message.toString(), replacements);
    }

    public static void msg(CommandSender sender, String message) {
        NEW_LINE.splitAsStream(color(message)).forEach(sender::sendMessage);
    }

    public static void msg(CommandSender sender, String message, Object... replacements) {
        String msg = color(replace(message, replacements));
        NEW_LINE.splitAsStream(msg).forEach(sender::sendMessage);
    }

    public static String replace(String string, Object... replacements) {
        if (replacements.length < 2 || replacements.length % 2 != 0) {
            throw new IllegalArgumentException("Invalid Replacements");
        }

        for (int i = 0; i < replacements.length; i += 2) {
            String key = String.valueOf(replacements[i]), value = String.valueOf(replacements[i + 1]);
            if (value == null) value = "";
            string = string.replaceAll(key, value);
        }
        return string;
    }

    public static void broadcast(String message) {
        for(Player player : Bukkit.getOnlinePlayers()) {
            msg(player, message);
        }
    }

    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static List<String> color(List<String> strings) {
        return strings.stream().map(Utils::color).collect(Collectors.toList());
    }

    public static void log(String message) { System.out.println("[Cars] "+message); }

    public static void clean() {
        for(Map.Entry<UUID, GroundVehicle> entry : cars.entrySet()) {
            GroundVehicle value = entry.getValue();
            value.getSeats().forEach(Entity::eject);
            value.getSeats().forEach(Entity::remove);
        }
    }

    public static File getDataFile(Player player) {
        String uuid = player.getUniqueId().toString();
        File userdata = new File(Cars.getPlugin().getDataFolder(), File.separator + "playerdata");
        return (new File(userdata, File.separator + uuid + ".yml"));
    }

    public static ArrayList<String> getOwnsArray(Player player) {
        File f = getDataFile(player);
        FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);
        return (ArrayList<String>) playerData.getList("cars.owns");
    }
}