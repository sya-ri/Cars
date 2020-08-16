package com.celerry.cars.inventory;

import com.celerry.cars.Cars;
import com.celerry.cars.obj.CarConfig;
import com.celerry.cars.obj.GroundVehicle;
import fr.mrmicky.fastinv.FastInv;
import fr.mrmicky.fastinv.ItemBuilder;
import com.celerry.cars.utils.Message;
import com.celerry.cars.utils.Utils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class SummonMenu extends FastInv {
    ;


    public SummonMenu(Player player) {
        super(54, "Cars Menu");

        Object[] fields = Cars.getPlugin().getCarConfig().getKeys(false).toArray();

        for (int i = 0; i < 54; i++) {
            ItemStack empty = new ItemBuilder(Material.STAINED_GLASS_PANE)
                    .name(" ")
                    .data((byte) 7)
                    .build();

            setItem(i, empty);
        }

        for (int i = 0; i < fields.length; i++) {

            CarConfig config = new CarConfig((String) fields[i]);
            String name = config.getName();
            double speed = config.getSpeed();
            double cost = config.getCost();

            int durability = config.getDurability();
            Material item = config.getItem();

            ItemStack saddle = new ItemBuilder(item)
                    .name(Utils.color(name))
                    .durability((byte) durability)
                    .addLore(Utils.color("&7Speed: &6" + speed), Utils.color("&7ID: &f" + fields[i]))
                    .meta(itemMeta -> itemMeta.setUnbreakable(true))
                    .flags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES)
                    .build();


            ItemStack display;
            ItemStack display2;

            if(config.isNeedsPerm()) {
                display = new ItemBuilder(saddle)
                        .addLore(Utils.color("&7Needs Permission: &a"+config.isNeedsPerm()))
                        .build();
            }
            else {
                display = new ItemBuilder(saddle)
                        .addLore(Utils.color("&7Needs Permission: &c"+config.isNeedsPerm()))
                        .build();
            }

            if (!Utils.getOwnsArray(player).contains(fields[i])) {
                display2 = new ItemBuilder(display)
                        .addLore(Utils.color("&7Cost: &a" + cost))
                        .build();
            } else {
                display2 = new ItemBuilder(display)
                        .addLore(Utils.color("&7Cost: &aPURCHASED"))
                        .enchant(Enchantment.DURABILITY, 1)
                        .build();
            }
            setItem(i, display2);

        }
        setItem(53, new ItemBuilder(Material.PAPER).name(Utils.color("&fWant one?")).addLore(Utils.color("&7Use the command"), Utils.color("&b/car buy <id>")).build());
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if(event.getCurrentItem().getType() != Material.valueOf(Cars.getPlugin().getConfig().getString("options.caritem"))) {
            return;
        }
        if(!player.isOnGround()) {
            Utils.msg(player, "%prefix% You must be on the ground when spawning a car", "%prefix%", Message.PREFIX);
            player.closeInventory();
            return;
        }
        player.closeInventory();
        Object[] fields = Cars.getPlugin().getCarConfig().getKeys(false).toArray();
        String item = event.getCurrentItem().getItemMeta().getDisplayName();
        String itemName = ChatColor.stripColor(item);
        for (Object field : fields) {
            CarConfig config = new CarConfig((String) field);
            String nametemp = Utils.color(config.getName());
            String name = ChatColor.stripColor(nametemp);

            if (Utils.checkIfHas(player.getUniqueId())) {
                Utils.msg(player, "%prefix% You already have a car spawned in", "%prefix%", Message.PREFIX);
                return;
            }
            if (isUnderBlock(player)) {
                Utils.msg(player, "%prefix% You can not be indoors when spawning a car", "%prefix%", Message.PREFIX);
                return;
            }
            if(!event.getCurrentItem().getEnchantments().containsKey(Enchantment.DURABILITY)) {
                Utils.msg(player, "%prefix% You don't own that car", "%prefix%", Message.PREFIX);
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, SoundCategory.MASTER, 1, 1);
                return;
            }
            Economy econ = Cars.getPlugin().getEconomy();
            double costToSpawn = Cars.getPlugin().getConfig().getDouble("options.costtospawn");
            if(!(econ.getBalance(player) >= costToSpawn)) {
                Utils.msg(player, "%prefix% You do not have enough money to spawn a car", "%prefix%", Message.PREFIX);
                return;
            }

            if (itemName.equalsIgnoreCase(name)) {
                if(config.isNeedsPerm()) {
                    if(!player.hasPermission(config.getPerm())) {
                        Utils.msg(player, "%prefix% You don't have permission to spawn this car", "%prefix%", Message.PREFIX);
                        return;
                    }
                }
                econ.withdrawPlayer(player, costToSpawn);
                GroundVehicle car = new GroundVehicle(player, (String) field);
                Utils.addGroundVehicle(player.getUniqueId(), car);
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_HAT, SoundCategory.MASTER, 1, 1);
                Utils.msg(player, "%prefix% Spawned a "+config.getName(), "%prefix%", Message.PREFIX);
                break;
            }
        }
    }

    public boolean isUnderBlock(Player p){
        for (int y = p.getLocation().getBlockY(); y < 255; y++) {
            Location l = new Location(p.getWorld(),p.getLocation().getX(),y,p.getLocation().getZ());
            if (l.getBlock().getType() != Material.AIR) {
                return true;
            }
        }
        return false;
    }

}