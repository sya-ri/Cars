package com.celerry.cars.obj;

import com.celerry.cars.Cars;
import com.celerry.cars.utils.Utils;
import org.jetbrains.annotations.NotNull;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

public class CarConfig {

    private Cars plugin;
    private FileConfiguration config;

    @NotNull
    boolean passSeat;
    @NotNull
    boolean backPassSeat;
    @NotNull
    boolean backDriverSeat;
    @NotNull
    double sideOffset;
    @NotNull
    double backwardsOffset;
    @NotNull
    String name;
    @NotNull
    double speed;
    @NotNull
    double yOffset;
    @NotNull
    int durability;
    @NotNull
    Material item;
    @NotNull
    double cost;

    @NotNull
    boolean needsPerm;
    @NotNull
    String perm;

    public CarConfig(@NotNull String id) {
        this.plugin = Cars.getPlugin();
        this.config = plugin.getCarConfig();

        this.needsPerm = false;
        this.perm = null;

        this.passSeat = config.getBoolean(id+".passengerseat");
        this.backPassSeat = config.getBoolean(id+".backpassengerseat");
        this.backDriverSeat = config.getBoolean(id+".backdriverseat");

        this.name = Utils.color(config.getString(id+".name"));
        this.speed = config.getDouble(id+".speed");
        this.durability = config.getInt(id+".durability");
        this.cost = config.getDouble(id+".cost");
        this.item = Material.valueOf(plugin.getConfig().getString("options.caritem"));

        if(config.getString(id+".requiredpermission") != null) {
            this.needsPerm = true;
            this.perm = config.getString(id+".requiredpermission");
        }

        this.yOffset = config.getDouble(id+".yoffset");
        this.sideOffset = config.getDouble(id+".sideoffset");
        this.backwardsOffset = config.getDouble(id+".backwardsoffset");
    }

    public boolean isBackDriverSeat() {
        return backDriverSeat;
    }

    public boolean isBackPassSeat() {
        return backPassSeat;
    }

    public Material getItem() {
        return item;
    }

    public boolean isPassSeat() {
        return passSeat;
    }

    public double getBackwardsOffset() {
        return backwardsOffset;
    }

    public double getSideOffset() {
        return sideOffset;
    }

    public double getSpeed() {
        return speed;
    }

    public double getCost() { return cost; }
    public double getyOffset() {
        return yOffset;
    }

    public String getName() {
        return name;
    }

    public int getDurability() {
        return durability;
    }

    public boolean isNeedsPerm() {
        return needsPerm;
    }

    public String getPerm() {
        return perm;
    }
}
