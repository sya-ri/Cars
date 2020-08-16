package com.celerry.cars.listeners;


import com.celerry.cars.Cars;
import com.isnakebuzz.servernpc.Events.NPCInteractEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.UUID;

public class ClickOnShopNPC implements Listener {

    private final Cars plugin;
    private HashMap<UUID, Long> cooldownTimes = new HashMap<>();
    int cooldown = 1;

    public ClickOnShopNPC(final Cars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onNPCClick(NPCInteractEvent event) {
        String id = event.getSnakeNPC().getName();
        Player player = event.getPlayer();
        if(cooldownTimes.get(player.getUniqueId()) != null) {
            if ((cooldownTimes.get(player.getUniqueId()) / 1000) + cooldown > (System.currentTimeMillis() / 1000)) {
                return;
            }
            cooldownTimes.put(player.getUniqueId(), System.currentTimeMillis());
        }
        if(id.contains("parachute")) {
            player.sendMessage("Click on Parachute NPC");
        }
    }
}