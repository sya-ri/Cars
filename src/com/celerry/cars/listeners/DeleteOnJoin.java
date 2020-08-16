package com.celerry.cars.listeners;

import com.celerry.cars.Cars;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

public class DeleteOnJoin implements Listener {

    private final Cars plugin;

    public DeleteOnJoin(Cars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(player.getVehicle() != null) {
            Entity ent = player.getVehicle();
            player.getVehicle().eject();
            ent.remove();
        }

        List<Entity> entities = player.getNearbyEntities(24, 400, 24);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() { // There is some sort of strange delay in mounting the car on relog, so schedule task to run in 2 seconds
            @Override
            public void run() {
                if(entities == null) { return; }
                entities.forEach(entity -> {
                    if(player.getVehicle() != null) {
                        Entity ent = player.getVehicle();
                        player.getVehicle().eject();
                        ent.remove();
                    }
                });
            }
        }, 40L);
    }
}
