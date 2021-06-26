package com.celerry.cars.listeners;

import com.celerry.cars.Cars;
import com.celerry.cars.obj.GroundVehicle;
import com.celerry.cars.utils.Message;
import com.celerry.cars.utils.Utils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.UUID;

public class StopLeavingInAir implements Listener {

    private final Cars plugin;

    public StopLeavingInAir(final Cars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onExitCarInAir(EntityDismountEvent event) {
        Entity entity = event.getEntity();
        if(entity.getType().equals(EntityType.PLAYER)) {
            Player player = (Player) entity;
            Entity dismounted = event.getDismounted();
            if(dismounted.getType().equals(EntityType.ARMOR_STAND)) {
                    if(dismounted.getLocation().getBlock().getType() == Material.AIR) {
                        if(!dismounted.isDead()) {
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Utils.color("&4&l! &eYou can't get out of your car mid-air!")));
                            dismounted.addPassenger(player);
                        }
                    }
            }
        }
    }

    // Destroy car on log out
    @EventHandler
    public void playerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if(player.getVehicle() != null) {
            player.getVehicle().eject();
            player.eject();
        }
        if(Utils.checkIfHas(uuid)) {
            GroundVehicle car = Utils.getGroundVehicle(uuid);
            Utils.removeGroundVehicle(uuid);
            car.getSeats().forEach(Entity::remove);
        }
    }

}
