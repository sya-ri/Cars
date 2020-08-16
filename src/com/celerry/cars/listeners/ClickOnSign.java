package com.celerry.cars.listeners;

import com.celerry.cars.Cars;
import com.celerry.cars.inventory.SummonMenu;
import com.celerry.cars.utils.Utils;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ClickOnSign implements Listener {

    private final Cars plugin;

    public ClickOnSign(Cars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSignClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        String line1 = plugin.getConfig().getString("sign.line1");
        String line2 = plugin.getConfig().getString("sign.line2");
        String line3 = plugin.getConfig().getString("sign.line3");
        String line4 = plugin.getConfig().getString("sign.line4");
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getType() == Material.SIGN || event.getClickedBlock().getType() == Material.SIGN_POST || event.getClickedBlock().getType() == Material.WALL_SIGN) {
                Sign sign = (Sign) event.getClickedBlock().getState();
                if (sign.getLine(0).equals(Utils.color(line1))) {
                    if (sign.getLine(1).equals(Utils.color(line2))) {
                        if (sign.getLine(2).equals(Utils.color(line3))) {
                            if(sign.getLine(3).equals(Utils.color(line4))) {
                                new SummonMenu(player).open(player);
                            }
                        }
                    }
                }
            }
        }
    }
}
