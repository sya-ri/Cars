package com.celerry.cars.inventory;

import com.celerry.cars.Cars;
import com.celerry.cars.obj.CarConfig;
import com.celerry.cars.utils.Utils;
import fr.mrmicky.fastinv.FastInv;
import fr.mrmicky.fastinv.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class CarList extends FastInv {
    ;


    public CarList(Player player) {
        super(54, "Cars List");

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
}