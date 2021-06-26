package com.celerry.cars.commands;

import com.celerry.cars.Cars;
import com.celerry.cars.inventory.CarList;
import com.celerry.cars.obj.CarConfig;
import com.celerry.cars.obj.GroundVehicle;
import com.celerry.cars.utils.CarUtils;
import com.celerry.cars.utils.Message;
import com.celerry.cars.utils.Utils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public final class CarsCommand implements CommandExecutor {

    private final Cars plugin;

    public CarsCommand(final Cars plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command");
            return true;
        }

        Player player = (Player) sender;
        if (args.length == 0) {
            return false;
        }

        for (int i = 0; i < args.length; i++) {
            args[i] = args[i].toLowerCase();
        }

        if (args[0].equals("lock")) {
            if (checkVehicle(player)) {
                Utils.msg(player, "%prefix% You're not in a car", "%prefix%", Message.PREFIX);
                return checkVehicle(player);
            }
            GroundVehicle car = CarUtils.getGroundVehicle(player.getVehicle());
            assert car != null;
            if (player.getVehicle().equals(car.getDriversSeat())) {
                car.lock();
                Utils.msg(player, "%prefix% Car is now %status%", "%prefix%", Message.PREFIX, "%status%", "locked");
            } else {
                Utils.msg(player, "%prefix% You're not in a car", "%prefix%", Message.PREFIX);
            }
            return true;
        }

        if (args[0].equals("unlock")) {
            if (checkVehicle(player)) {
                Utils.msg(player, "%prefix% You're not in a car", "%prefix%", Message.PREFIX);
                return checkVehicle(player);
            }
            GroundVehicle car = CarUtils.getGroundVehicle(player.getVehicle());
            assert car != null;
            if (player.getVehicle().equals(car.getDriversSeat())) {
                car.unlock();
                Utils.msg(player, "%prefix% Car is now %status%", "%prefix%", Message.PREFIX, "%status%", "unlocked");
            } else {
                Utils.msg(player, "%prefix% You're not in a car", "%prefix%", Message.PREFIX);
            }
            return true;
        }

        if (args[0].equals("despawn")) {
            if (checkVehicle(player)) {
                Utils.msg(player, "%prefix% You're not in a car", "%prefix%", Message.PREFIX);
                return checkVehicle(player);
            }
            GroundVehicle car = CarUtils.getGroundVehicle(player.getVehicle());
            assert car != null;
            if (player.getVehicle().equals(car.getDriversSeat())) {
                car.getSeats().forEach(Entity::eject);
                car.getSeats().forEach(Entity::remove);
                Utils.removeGroundVehicle(car.getUniqueId());
                Utils.msg(player, "%prefix% Car removed", "%prefix%", Message.PREFIX);
            } else {
                Utils.msg(player, "%prefix% You're not in a car", "%prefix%", Message.PREFIX);
            }
            return true;
        }

        if (args[0].equals("list")) {
            new CarList(player).open(player);
            return true;
        }

        if (args[0].equals("reset")) {
            if (Utils.checkIfHas(player.getUniqueId())) {
                GroundVehicle car = Utils.getGroundVehicle(player.getUniqueId());
                car.getSeats().forEach(Entity::eject);
                car.getSeats().forEach(Entity::remove);
                Utils.msg(player, "%prefix% Your car was removed", "%prefix%", Message.PREFIX);
                Utils.removeGroundVehicle(player.getUniqueId());
                return true;
            }
            Utils.msg(player, "%prefix% You don't have a car", "%prefix%", Message.PREFIX);
            return true;
        }

        if (args[0].equals("buy")) {
            if (args.length < 2) {
                Utils.msg(player, "%prefix% Missing car argument", "%prefix%", Message.PREFIX);
                return true;
            }
            if (Utils.getOwnsArray(player).contains(args[1])) { // Already own the car
                Utils.msg(player, "%prefix% You already own that car", "%prefix%", Message.PREFIX);
                return true;
            }
            Object[] fields = Cars.getPlugin().getCarConfig().getKeys(false).toArray();
            boolean fieldsContainsCar = false;
            for (Object field: fields) {
                if (field.equals(args[1])) {
                    fieldsContainsCar = true;
                }
            }
            if (!fieldsContainsCar) { // Car doesnt exist
                Utils.msg(player, "%prefix% That car does not exist", "%prefix%", Message.PREFIX);
                return true;
            }

            CarConfig carConfig = new CarConfig(args[1]);
            double cost = carConfig.getCost();
            Economy econ = Cars.getPlugin().getEconomy();
            if (econ.getBalance(player) < cost) { // Cannot afford
                Utils.msg(player, "%prefix% You can't afford this car", "%prefix%", Message.PREFIX);
                return true;
            } else { // Can afford
                File f = Utils.getDataFile(player);
                FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);

                ArrayList < String > newarr = new ArrayList < > (Utils.getOwnsArray(player));

                newarr.add(args[1].toLowerCase());

                try {

                    playerData.createSection("cars");
                    playerData.set("cars.owns", newarr);

                    playerData.save(f);

                    econ.withdrawPlayer(player, cost);
                    Utils.msg(player, "%prefix% You purchased %name% &efor &a%cost%&e. New balance: &a%balance%", "%name%", carConfig.getName(), "%cost%", cost, "%balance%", econ.getBalance(player), "%prefix%", Message.PREFIX);
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, SoundCategory.MASTER, 1, 1);
                    return true;
                } catch (IOException exception) {

                    exception.printStackTrace();
                    return true;
                }
            }
        }


        if (args[0].equals("sell")) {
            if (args.length < 2) {
                Utils.msg(player, "%prefix% Missing car argument", "%prefix%", Message.PREFIX);
                return true;
            }
            if (!Utils.getOwnsArray(player).contains(args[1])) { // Don't own
                Utils.msg(player, "%prefix% You don't own that car", "%prefix%", Message.PREFIX);
                return true;
            }
            Object[] fields = Cars.getPlugin().getCarConfig().getKeys(false).toArray();
            boolean fieldsContainsCar = false;
            for (Object field: fields) {
                if (field.equals(args[1])) {
                    fieldsContainsCar = true;
                }
            }
            if (!fieldsContainsCar) { // Car doesnt exist
                Utils.msg(player, "%prefix% That car does not exist", "%prefix%", Message.PREFIX);
                return true;
            }

            CarConfig carConfig = new CarConfig(args[1]);
            double cost = carConfig.getCost();
            Economy econ = Cars.getPlugin().getEconomy();

            File f = Utils.getDataFile(player);
            FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);

            ArrayList < String > newarr = new ArrayList < > (Utils.getOwnsArray(player));

            newarr.remove(args[1].toLowerCase());

            try {

                playerData.createSection("cars");
                playerData.set("cars.owns", newarr);

                playerData.save(f);

                cost = cost * 0.75;

                econ.depositPlayer(player, cost);
                Utils.msg(player, "%prefix% You sold %name% &efor &a%cost%&e. New balance: &a%balance%", "%name%", carConfig.getName(), "%cost%", cost, "%balance%", econ.getBalance(player), "%prefix%", Message.PREFIX);
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, SoundCategory.MASTER, 1, 1);
                return true;
            } catch (IOException exception) {

                exception.printStackTrace();
                return true;
            }
        }

        if(args[0].equals("add")) {
            if (args.length < 2) {
                Utils.msg(player, "%prefix% Missing car argument", "%prefix%", Message.PREFIX);
                return true;
            }
            if (checkVehicle(player)) {
                Utils.msg(player, "%prefix% You're not in a car", "%prefix%", Message.PREFIX);
                return checkVehicle(player);
            }
            String playerName = args[1];
            if(Bukkit.getPlayerExact(playerName) == null) {
                Utils.msg(player, "%prefix% That player is not online", "%prefix%", Message.PREFIX);
                return true;
            }
            Player playerArg = Bukkit.getPlayerExact(playerName);
            UUID uuid = playerArg.getUniqueId();
            GroundVehicle car = CarUtils.getGroundVehicle(player.getVehicle());
            assert car != null;
            if (player.getVehicle().equals(car.getDriversSeat())) {
                car.addFriend(uuid);
                Utils.msg(player, "%prefix% %name% can now drive the car", "%prefix%", Message.PREFIX, "%name%", playerArg.getName());
            } else {
                Utils.msg(player, "%prefix% You're not in a car", "%prefix%", Message.PREFIX);
            }
            return true;
        }

        if(args[0].equals("remove")) {
            if (args.length < 2) {
                Utils.msg(player, "%prefix% Missing car argument", "%prefix%", Message.PREFIX);
                return true;
            }
            if (checkVehicle(player)) {
                Utils.msg(player, "%prefix% You're not in a car", "%prefix%", Message.PREFIX);
                return checkVehicle(player);
            }
            String playerName = args[1];
            if(Bukkit.getPlayerExact(playerName) == null) {
                Utils.msg(player, "%prefix% That player is not online", "%prefix%", Message.PREFIX);
                return true;
            }
            Player playerArg = Bukkit.getPlayerExact(playerName);
            UUID uuid = playerArg.getUniqueId();
            GroundVehicle car = CarUtils.getGroundVehicle(player.getVehicle());
            assert car != null;
            if (player.getVehicle().equals(car.getDriversSeat())) {
                car.removeFriend(uuid);
                Utils.msg(player, "%prefix% %name% can no longer drive the car", "%prefix%", Message.PREFIX, "%name%", playerArg.getName());
            } else {
                Utils.msg(player, "%prefix% You're not in a car", "%prefix%", Message.PREFIX);
            }
            return true;
        }
        return false;
    }

    public static boolean checkVehicle(Player player) {
        if (player.getVehicle() == null) {
            return true;
        }
        if (CarUtils.getGroundVehicle(player.getVehicle()) == null) {
            return true;
        }
        return false;
    }
}