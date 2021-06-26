package com.celerry.cars.listeners;

import com.celerry.cars.Cars;
import com.celerry.cars.utils.Message;
import com.celerry.cars.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

import java.util.UUID;

public class ChunkUnload implements Listener {

    private final Cars plugin;

    public ChunkUnload(final Cars plugin) { this.plugin = plugin; }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        Chunk chunk = event.getChunk();
        Entity[] entities = chunk.getEntities();
        for (Entity entity : entities) {
            if(entity.getType().equals(EntityType.ARMOR_STAND)) {
                ArmorStand armorstand = (ArmorStand) entity;
                String name = null;
                if (armorstand.getName() != null) {
                    name = armorstand.getName();
                }
                if (name.length() == 41) {
                    // Message player if online, remove from world and memory
                    String uuidstr = name.substring(Math.max(0, name.length() - 36));
                    UUID uuid = UUID.fromString(uuidstr);
                    OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);

                    if (Utils.checkIfHas(uuid)) {
                        if(player.isOnline()) {
                            Utils.msg((Player) player, "%prefix% Your car was removed as it its chunk was unloaded", "%prefix%", Message.PREFIX);
                        }
                        Utils.removeGroundVehicle(uuid);
                    }
                    armorstand.remove();

                }
            }
        }
    }
}
