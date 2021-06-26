package com.celerry.cars.listeners;

import com.celerry.cars.Cars;
import com.celerry.cars.inventory.SummonMenu;
import com.celerry.cars.obj.GroundVehicle;
import com.celerry.cars.utils.Message;
import com.celerry.cars.utils.Utils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.UUID;


public class ClickListener implements Listener {

    public ClickListener(final Cars plugin) {}

    @EventHandler
    public void onClickOnCar(PlayerInteractAtEntityEvent event) {
        Entity entity = event.getRightClicked();
        Player player = event.getPlayer();
        if (entity.getType() == EntityType.ARMOR_STAND) {
            if (entity.getPassengers() != null) {
                if (entity.getPassengers().size() > 0) {
                    Utils.msg(player, "%prefix% That seat is taken!", "%prefix%", Message.PREFIX);
                    return;
                }
            }
            String name = entity.getName();
            if (name != null && name.length() == 41) {
                String first4 = name.substring(0, 4);
                String uuid = name.substring(Math.max(0, name.length() - 36));
                if (first4.equals("seat")) {
                    UUID real = UUID.fromString(uuid);
                    GroundVehicle car = Utils.getGroundVehicle(real);

                    if (car.getFriends().contains(player.getUniqueId()) || car.isUnlocked()) {
                        entity.addPassenger(player);
                    } else {
                        Utils.msg(player, "%prefix% You don't have permission to enter this car", "%prefix%", Message.PREFIX);
                    }

                    event.setCancelled(true);
                }
                if (first4.equals("driv")) {
                    UUID real = UUID.fromString(uuid);
                    GroundVehicle car = Utils.getGroundVehicle(real);

                    if (car.getFriends().contains(player.getUniqueId())) {
                        entity.addPassenger(player);
                    } else {
                        Utils.msg(player, "%prefix% You don't own this car", "%prefix%", Message.PREFIX);
                    }

                    event.setCancelled(true);
                }
            }
        }
    }


}
