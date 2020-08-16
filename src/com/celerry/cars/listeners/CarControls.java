package com.celerry.cars.listeners;

import com.celerry.cars.Cars;
import com.celerry.cars.obj.GroundVehicle;
import com.celerry.cars.utils.Utils;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.celerry.cars.utils.CarUtils.*;

public class CarControls extends PacketAdapter {

    public static final Map<UUID, Boolean> honking = new HashMap<>();

    public CarControls(Cars plugin, ListenerPriority listenerPriority, PacketType types) {
        super(plugin, listenerPriority, types);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        if(event.getPacketType().equals(PacketType.Play.Client.STEER_VEHICLE)){
            Player player = event.getPlayer();
            if(player.getVehicle() != null) {
                if (player.getVehicle().getName().substring(0, 4).equals("driv") && player.getVehicle().getType() == EntityType.ARMOR_STAND) {

                    double leftOrRight = event.getPacket().getFloat().readSafely(0);
                    boolean jumping = event.getPacket().getBooleans().readSafely(0);
                    double forwardOrBack = event.getPacket().getFloat().readSafely(1);

                    ArmorStand mainSeat = (ArmorStand) player.getVehicle();

                    String uuidstr = mainSeat.getName().substring(Math.max(0, mainSeat.getName().length() - 36));
                    UUID uuid = UUID.fromString(uuidstr);

                    GroundVehicle car = Utils.getGroundVehicle(uuid);
                    if(car == null) { return; }
                    Collection<ArmorStand> seats = car.getSeats();

                    if(seats == null) {return;}

                    AtomicBoolean goingUp = new AtomicBoolean(false);

                    if (leftOrRight < 0) { // Right
                        Location mainLoc = mainSeat.getLocation();
                        mainLoc.setYaw(mainLoc.getYaw() + 4f);
                        teleportCar(mainSeat, mainLoc);
                        moveAroundMain(car, mainSeat);
                    }
                    if (leftOrRight > 0) { // Left
                        Location mainLoc = mainSeat.getLocation();
                        mainLoc.setYaw(mainLoc.getYaw() - 4f);
                        teleportCar(mainSeat, mainLoc);
                        moveAroundMain(car, mainSeat);
                    }
                    if (forwardOrBack < 0) { // Back
                        double distance = -car.getConfig().getSpeed();
                        Location standLoc = mainSeat.getLocation();
                        Location inFront = calcNewDrivingLocation(distance, standLoc, car);

                        if(inFront.getPitch() == 1 || inFront.getPitch() == 2) {
                            return;
                        }
                        if(inFront.getY() != standLoc.getY()) {
                            goingUp.set(true);
                        }
                        teleportCar(mainSeat, inFront);
                        moveAroundMain(car, mainSeat);
                    }
                    if (forwardOrBack > 0) { // Forwards
                        double distance = car.getConfig().getSpeed();
                        Location standLoc = mainSeat.getLocation();
                        Location inFront = calcNewDrivingLocation(distance, standLoc, car);

                        if(inFront.getPitch() == 1 || inFront.getPitch() == 2) {
                            return;
                        }
                        if(inFront.getY() != standLoc.getY()) {
                            goingUp.set(true);
                        }
                        teleportCar(mainSeat, inFront);
                        moveAroundMain(car, mainSeat);
                    }

                    // Honking horn
                    if(plugin.getConfig().getBoolean("options.honking")) {
                        if (jumping) {
                            if (!honking.containsKey(player.getUniqueId())) {
                                honking.put(player.getUniqueId(), true);
                                player.getWorld().playSound(player.getLocation(), Sound.ITEM_BOTTLE_FILL_DRAGONBREATH, 15, 2);
                            }
                        }
                        if (!jumping) {
                            honking.remove(player.getUniqueId());
                        }
                    }

                    if(!goingUp.get()) { // Gravity
                        Location standLoc = mainSeat.getLocation();
                        Block b = standLoc.getBlock();

                        if (isPassable(b.getType()) || isFluid(b.getType())) {
                            standLoc.setY(standLoc.getY() + car.getConfig().getyOffset());
                            teleportCar(mainSeat, standLoc);
                            moveAroundMain(car, mainSeat);
                        }
                    }

                }
            }
        }
    }
}
