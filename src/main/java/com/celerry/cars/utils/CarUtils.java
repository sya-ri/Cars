package com.celerry.cars.utils;

import com.celerry.cars.obj.GroundVehicle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.UUID;
import java.util.function.Supplier;

public final class CarUtils {

    public static Location calcNewDrivingLocation(double distance, Location standLoc, GroundVehicle car) {
        Location inFront = standLoc.clone().add(standLoc.getDirection().multiply(distance));


        Block b = inFront.getBlock();
        Block block = b.getLocation().add(0, 1, 0).getBlock();
        Block above = b.getLocation().add(0, 2, 0).getBlock();

        if(above.getType() != Material.AIR) {
            if (!isPassable(above.getType())) {
                inFront.setPitch(1); // If pitch is 1, then driving should be cancelled
            }
        }

        if(isTooTall(block.getType())) {
            inFront.setPitch(2);
        }

        if(isFluid(block.getType())) {
            car.damage(1000);
            return inFront;
        }

        if(block.getType() != Material.AIR) {
            if (!isPassable(block.getType())) {
                if(!isFluid(block.getType())) {
                    inFront.setY(inFront.getY() + 1);
                }
            }
        }

        return inFront;
    }

    public static void teleportCar(ArmorStand as, Location loc) {
        try {
            methods[1].invoke(methods[0].invoke(as), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        } catch (Exception ignored) {
        }
    }

    public static final Method[] methods = ((Supplier<Method[]>) () -> {
        try {
            Method getHandle = Class.forName(Bukkit.getServer().getClass().getPackage().getName() + ".entity.CraftEntity").getDeclaredMethod("getHandle");
            return new Method[] {
                    getHandle, getHandle.getReturnType().getDeclaredMethod("setPositionRotation", double.class, double.class, double.class, float.class, float.class)
            };
        } catch (Exception ex) {
            return null;
        }
    }).get();

    public static boolean isPassable(Material block) {
        if (!(block.isBlock())) {
            return false;
        }
        switch (block) {
            case SIGN_POST:
            case WALL_SIGN:
            case STANDING_BANNER:
            case WALL_BANNER:
            case SAPLING:
            case FLOWER_POT:
            case RED_ROSE:
            case YELLOW_FLOWER:
            case TRAP_DOOR:
            case IRON_TRAPDOOR:
            case REDSTONE_TORCH_OFF:
            case REDSTONE_TORCH_ON:
            case REDSTONE_WIRE:
            case REDSTONE_COMPARATOR_OFF:
            case REDSTONE_COMPARATOR_ON:
            case LEVER:
            case WOOD_BUTTON:
            case STONE_BUTTON:
            case LONG_GRASS:
            case RAILS:
            case ACTIVATOR_RAIL:
            case DETECTOR_RAIL:
            case POWERED_RAIL:
            case BROWN_MUSHROOM:
            case RED_MUSHROOM:
            case TORCH:
            case STONE_PLATE:
            case WOOD_PLATE:
            case SNOW:
            case AIR:
            case WATER_LILY:
            case TRIPWIRE_HOOK:
            case IRON_PLATE:
            case GOLD_PLATE:
            case DAYLIGHT_DETECTOR:
            case DAYLIGHT_DETECTOR_INVERTED:
            case CARPET:
            case DOUBLE_PLANT:
            case END_ROD:
            case BED_BLOCK:
            case SKULL:
                return true;
            default:
                return false;
        }
    }

    /**
     * Rotates a vector by a given number of degrees; assume looking from a top down view (around the Y axis)
     * @param vector - The vector to rotate
     * @param degrees - The number of degrees to rotate by
     * @return - A rotated vector around the Y axis
     */
    public static Vector rotateVectorAroundY(Vector vector, double degrees) {
        double rad = Math.toRadians(degrees);

        double currentX = vector.getX();
        double currentZ = vector.getZ();

        double cosine = Math.cos(rad);
        double sine = Math.sin(rad);

        return new Vector((cosine * currentX - sine * currentZ), vector.getY(), (sine * currentX + cosine * currentZ));
    }

    public static void moveAroundMain(GroundVehicle car, ArmorStand mainSeat) {
        Collection<ArmorStand> seats = car.getSeats();
        seats.forEach(stand -> {
            Location mainLoc = mainSeat.getLocation();
            if (stand.equals(car.getBackDriverSeat())) {
                mainLoc.add(mainLoc.getDirection().multiply(car.getConfig().getBackwardsOffset()));
            }
            if (stand.equals(car.getBackPassSeat())) {
                Vector direction = mainLoc.getDirection();
                direction = rotateVectorAroundY(direction, -90);
                mainLoc.add(direction.multiply(car.getConfig().getSideOffset()));
                mainLoc.add(mainLoc.getDirection().multiply(car.getConfig().getBackwardsOffset()));
            }
            if (stand.equals(car.getPassSeat())) {
                Vector direction = mainLoc.getDirection();
                direction = rotateVectorAroundY(direction, -90);
                mainLoc.add(direction.multiply(car.getConfig().getSideOffset()));
            }
            //mainLoc.setYaw(mainLoc.getYaw()+4f);
            teleportCar(stand, mainLoc);
        });
    }

    public static boolean isTooTall(Material block) {
        if (!(block.isBlock())) {
            return false;
        }
        switch (block) {
            case FENCE:
            case FENCE_GATE:
            case ACACIA_FENCE:
            case ACACIA_FENCE_GATE:
            case BIRCH_FENCE:
            case BIRCH_FENCE_GATE:
            case DARK_OAK_FENCE:
            case DARK_OAK_FENCE_GATE:
            case JUNGLE_FENCE:
            case JUNGLE_FENCE_GATE:
            case NETHER_FENCE:
            case SPRUCE_FENCE:
            case SPRUCE_FENCE_GATE:
            case COBBLE_WALL:
                return true;
            default:
                return false;
        }
    }

    public static boolean isFluid(Material block) {
        if (!(block.isBlock())) {
            return false;
        }
        switch (block) {
            case STATIONARY_WATER:
            case WATER:
            case STATIONARY_LAVA:
            case LAVA:
                return true;
            default:
                return false;
        }
    }

    public static GroundVehicle getGroundVehicle(Entity entity) {
        if(entity.getType() == EntityType.ARMOR_STAND) {
            if(entity.getName() == null) {
                return null;
            }
            String name = entity.getName();
            if(name.length() != 41) {
               return null;
            }
            String uuidstr = name.substring(Math.max(0, name.length() - 36));
            UUID uuid = UUID.fromString(uuidstr);
            if(Utils.getGroundVehicle(uuid) == null) {
                return null;
            }
            return Utils.getGroundVehicle(uuid);
        }
        return null;
    }
}
