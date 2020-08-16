package com.celerry.cars.obj;

import com.celerry.cars.utils.Message;
import com.celerry.cars.utils.Utils;
import com.sun.istack.internal.NotNull;
import fr.mrmicky.fastinv.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Stream;

public class GroundVehicle {

    @NotNull
    private ArmorStand driversSeat;
    @NotNull
    private ArmorStand passSeat;
    @NotNull
    private ArmorStand backDriverSeat;
    @NotNull
    private ArmorStand backPassSeat;
    @NotNull
    private UUID uuid;
    @NotNull
    private Collection<UUID> friends = new HashSet();
    @NotNull
    private Boolean unlocked;

    @NotNull
    private CarConfig config;

    @NotNull
    private double health = 20.0;

    public GroundVehicle(@NotNull Player player, @NotNull String id) {
        this.uuid = player.getUniqueId();
        this.friends.add(player.getUniqueId());
        this.unlocked = false;

        this.config = new CarConfig(id);

        Location location = player.getLocation();
        location.setYaw(0);
        location.setPitch(0);

        location.add(0, config.getyOffset(), 0);
        Location passLocation = location.clone().add(config.getSideOffset(), 0, 0); // -0.95, 0, 0
        Location backPassLocation = passLocation.clone().add(0, 0, config.getBackwardsOffset()); // 0, 0, -0.95
        Location backDriverLocation = location.clone().add(0, 0, config.getBackwardsOffset()); // 0, 0, -0.568

        this.driversSeat = (ArmorStand) player.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
            this.driversSeat.setHelmet(new ItemBuilder(config.getItem()).data((byte) config.getDurability()).build());
            this.driversSeat.setCustomName("driv-"+this.uuid);
            this.driversSeat.setGravity(false);
            this.driversSeat.setHealth(this.health);
            this.driversSeat.setVisible(false);
            this.driversSeat.setCustomNameVisible(false);

        if(config.isPassSeat()) {
            this.passSeat = (ArmorStand) player.getWorld().spawnEntity(passLocation, EntityType.ARMOR_STAND);
                this.passSeat.setCustomName("seat-"+this.uuid);
                this.passSeat.setGravity(false);
                this.passSeat.setVisible(false);
                this.passSeat.setCustomNameVisible(false);
        }
        if(config.isBackPassSeat()) {
            this.backPassSeat = (ArmorStand) player.getWorld().spawnEntity(backPassLocation, EntityType.ARMOR_STAND);
                this.backPassSeat.setCustomName("seat-"+this.uuid);
                this.backPassSeat.setGravity(false);
                this.backPassSeat.setVisible(false);
                this.backPassSeat.setCustomNameVisible(false);
        }
        if(config.isBackDriverSeat()) {
            this.backDriverSeat = (ArmorStand) player.getWorld().spawnEntity(backDriverLocation, EntityType.ARMOR_STAND);
                this.backDriverSeat.setCustomName("seat-"+this.uuid);
                this.backDriverSeat.setGravity(false);
                this.backDriverSeat.setVisible(false);
                this.backDriverSeat.setCustomNameVisible(false);
        }

    }

    @NotNull
    public UUID getUniqueId() {
        return this.uuid;
    }

    @NotNull
    public boolean isUnlocked() {
        return this.unlocked;
    }

    @NotNull
    public Collection<UUID> getFriends() {
        return Collections.unmodifiableCollection(this.friends);
    }

    @NotNull
    public ArmorStand getDriversSeat() {
        return this.driversSeat;
    }

    @NotNull
    public ArmorStand getPassSeat() {
        return this.passSeat;
    }

    @NotNull
    public ArmorStand getBackDriverSeat() {
        return this.backDriverSeat;
    }

    @NotNull
    public ArmorStand getBackPassSeat() {
        return this.backPassSeat;
    }

    @NotNull
    public Collection<ArmorStand> getSeats() {
        Collection<ArmorStand> seats = new HashSet();
        Stream.of(this.driversSeat, this.passSeat, this.backDriverSeat, this.backPassSeat).forEach(seat -> {
            if(seat != null) {
                seats.add(seat);
            }
        });
        return seats;
    }

    @NotNull
    public void lock() { this.unlocked = false; }

    @NotNull
    public void unlock() { this.unlocked = true; }

    @NotNull
    public void addFriend(UUID uuid) { this.friends.add(uuid); }

    @NotNull
    public void removeFriend(UUID uuid) { this.friends.remove(uuid); }

    @NotNull
    public CarConfig getConfig() { return this.config; }

    @NotNull
    public void damage(double damage) {
        this.health = this.health - damage;
        if(this.health < 0) {
            this.getDriversSeat().getWorld().playSound(this.getDriversSeat().getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 15.0F, 1.0F);
            this.getSeats().forEach(Entity::eject);
            this.getSeats().forEach(Entity::remove);
            Player player = Bukkit.getPlayer(this.uuid);
            Utils.msg(player, "%prefix% Your car was destroyed", "%prefix%", Message.PREFIX);
            Utils.removeGroundVehicle(this.getUniqueId());
            return;
        }
        this.getDriversSeat().setHealth(this.health);
    }
}
