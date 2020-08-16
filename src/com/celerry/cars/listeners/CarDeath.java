package com.celerry.cars.listeners;

import com.celerry.cars.Cars;
import com.celerry.cars.utils.Message;
import com.celerry.cars.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.UUID;

public class CarDeath implements Listener {

    private final Cars plugin;

    public CarDeath(Cars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeathOfCar(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        if (entity.getName() != null && entity.getName().length() == 41) {
            String name = entity.getName();
            String uuidstr = name.substring(Math.max(0, name.length() - 36));
            UUID uuid = UUID.fromString(uuidstr);
            // Notify player
            OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
            if (player.isOnline()) {
                if (name.substring(0, 4).equals("driv")) {
                    Utils.msg((Player) player, "%prefix% Your car was destroyed", "%prefix%", Message.PREFIX);
                }
            }
            // Remove
            Utils.removeGroundVehicle(uuid);
        }
    }
}
