package com.celerry.cars.listeners;

import com.celerry.cars.Cars;
import com.celerry.cars.utils.Utils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class InitPlayerData implements Listener {

    private final Cars plugin;

    public InitPlayerData(Cars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void initData(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        File f = Utils.getDataFile(player);
        FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);

        //When the player file is created for the first time...
        if (!f.exists()) {
            try {

                playerData.createSection("cars");
                playerData.set("cars.owns", new ArrayList<String>());

                playerData.save(f);
            } catch (IOException exception) {

                exception.printStackTrace();
            }
        }
    }
}
