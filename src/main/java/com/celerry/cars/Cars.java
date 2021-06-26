package com.celerry.cars;

import com.celerry.cars.commands.CarsCommand;
import com.celerry.cars.listeners.*;
import com.celerry.cars.listeners.ClickOnSign;
import com.celerry.cars.listeners.CreateSign;
import com.celerry.cars.utils.Utils;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import fr.mrmicky.fastinv.FastInvManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

public class Cars extends JavaPlugin {
    private static Cars plugin = null;

    public static Cars getPlugin() {
        return plugin;
    }

    File carsConfig = new File(getDataFolder(), "cars.yml");

    @Override
    public void onLoad() {
        plugin = this;
        if(!carsConfig.exists()) {
            saveResource("cars.yml", false); // moves the file from the jar to the plugin data folder
        }
        getCarConfig().options().copyDefaults(true);
        // Save custom config
        try {
            getCarConfig().save(carsConfig);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Default config
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        // Player data
        File usersFolder = new File(this.getDataFolder(), "playerdata");
        if (!usersFolder.exists()) {
            usersFolder.mkdirs();
        }
    }

    public FileConfiguration getCarConfig() {
        return YamlConfiguration.loadConfiguration(carsConfig);
    }

    @Override
    public void onEnable(){
        FastInvManager.register(this);
        getCommand("car").setExecutor(new CarsCommand(this));
        Stream.of(new ClickOnSign(this), new CreateSign(this), new DeleteOnJoin(this), new ChunkLoad(this), new CarDeath(this), new InitPlayerData(this), new DamageCar(this), new ClickListener(this), new StopLeavingInAir(this), new ChunkUnload(this)).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
        ProtocolLibrary.getProtocolManager().addPacketListener(new CarControls(this, ListenerPriority.NORMAL, PacketType.Play.Client.STEER_VEHICLE));
    }

    @Override
    public void onDisable() {
        Utils.clean();
        plugin = null;
    }

    public Economy getEconomy() {
        return getServer().getServicesManager().getRegistration(Economy.class).getProvider();
    }



}
