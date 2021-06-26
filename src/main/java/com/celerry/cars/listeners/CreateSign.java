package com.celerry.cars.listeners;

import com.celerry.cars.Cars;
import com.celerry.cars.utils.Message;
import com.celerry.cars.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class CreateSign implements Listener {
    private final Cars plugin;

    public CreateSign(Cars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSignCreate(SignChangeEvent event) {
        Player player = event.getPlayer();
        String line1 = plugin.getConfig().getString("sign.line1");
        String line2 = plugin.getConfig().getString("sign.line2");
        String line3 = plugin.getConfig().getString("sign.line3");
        String line4 = plugin.getConfig().getString("sign.line4");
        if (event.getLine(0).equals(line1)) {
            if(event.getLine(1).equals(line2)) {
                if(event.getLine(2).equals(line3)) {
                    if(event.getLine(3).equals(line4)) {
                        if(!player.hasPermission("cars.createsign")) {
                            event.setLine(0, "You don't have");
                            event.setLine(1, "permission to");
                            event.setLine(2, "create car");
                            event.setLine(3, "spawn signs :(");
                        }
                    }
                }
            }
        }
    }
}
