package com.celerry.cars.listeners;

import com.celerry.cars.Cars;
import com.celerry.cars.obj.GroundVehicle;
import com.celerry.cars.utils.Utils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.UUID;

public class DamageCar implements Listener {

    private final Cars plugin;

    public DamageCar(Cars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamageEntity(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        Entity damager = event.getDamager();
        if (entity.getName() == null) {
            return;
        }
        if(!(damager instanceof Player)) {
            return;
        }
        Player player = (Player) damager;
        String name = entity.getName();
        if (name.length() == 41) {
            String uuidstr = name.substring(Math.max(0, name.length() - 36));
            UUID uuid = UUID.fromString(uuidstr);
            GroundVehicle car = Utils.getGroundVehicle(uuid);
            if(entity.equals(car.getDriversSeat())) {
                car.damage(event.getDamage());
            }

        }
    }
}
