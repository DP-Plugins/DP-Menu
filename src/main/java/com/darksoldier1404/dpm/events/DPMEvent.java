package com.darksoldier1404.dpm.events;

import com.darksoldier1404.dpm.Menu;
import com.darksoldier1404.dpm.functions.DPMFunction;
import com.darksoldier1404.dppc.DPPCore;
import com.darksoldier1404.dppc.api.essentials.MoneyAPI;
import com.darksoldier1404.dppc.api.inventory.DInventory;
import com.darksoldier1404.dppc.lang.DLang;
import com.darksoldier1404.dppc.utils.NBT;
import com.darksoldier1404.dppc.utils.Quadruple;
import com.darksoldier1404.dppc.utils.Tuple;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("all")
public class DPMEvent implements Listener {
    private final Menu plugin = Menu.getInstance();
    private final String prefix = plugin.data.getPrefix();
    private final DLang lang = plugin.data.getLang();

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        String cmd = e.getMessage().split(" ")[0].substring(1);
        plugin.menus.values().forEach(m -> {
            if (m.getString("Menu.ALIASES") != null && m.getString("Menu.ALIASES").equalsIgnoreCase(cmd)) {
                e.setCancelled(true);
                plugin.getServer().dispatchCommand(e.getPlayer(), "dpm open " + m.getString("Menu.NAME"));
            }
        });
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getInventory().getHolder() instanceof DInventory) {
            DInventory inv = (DInventory) e.getInventory().getHolder();
            if (!inv.isValidHandler(plugin)) return;
            if (inv.getObj() == null) {
                return;
            }
            DPMFunction.saveItemSetting((Player) e.getPlayer(), ((Tuple<String, String>) inv.getObj()).getA(), inv);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getInventory().getHolder() instanceof DInventory) {
            DInventory inv = (DInventory) e.getInventory().getHolder();
            if (!inv.isValidHandler(plugin)) return;
            if (e.getCurrentItem() == null) return;
            ItemStack item = e.getCurrentItem();
            Player p = (Player) e.getWhoClicked();
            if (inv.getObj() == null) {
                e.setCancelled(true);
                if (NBT.hasTagKey(item, "dpm.price")) {
                    String sprice = NBT.getStringTag(item, "dpm.price");
                    try {
                        double price = Double.parseDouble(sprice);
                        if (MoneyAPI.hasEnoughMoney(p, price)) {
                            MoneyAPI.takeMoney(p, price);
                        } else {
                            p.sendMessage(prefix + lang.get("no_money"));
                            return;
                        }
                    } catch (Exception ex) {
                        p.sendMessage(prefix + lang.get("money_setting_wrong"));
                        p.sendMessage(prefix + lang.get("money_wrong_lore") + NBT.getStringTag(item, "dpm.price"));
                        return;
                    }
                }
                if (NBT.hasTagKey(item, "dpm.action")) {
                    String actionName = NBT.getStringTag(item, "dpm.action");
                    DPPCore.actions.get(actionName).execute(p);
                    return;
                }
                return;
            }
            if (!(inv.getObj() instanceof Tuple)) return;
            Tuple<String, String> t = (Tuple<String, String>) inv.getObj();
            String b = t.getB();
            if (!b.equalsIgnoreCase("ITEMS")) {
                if (e.getClickedInventory().getType() == InventoryType.PLAYER) {
                    return;
                }
                e.setCancelled(true);
                if (b.equalsIgnoreCase("price")) {
                    DPMFunction.currentEditItem.put(p.getUniqueId(), Quadruple.of(t.getA(), item, "price", e.getSlot()));
                    p.closeInventory();
                    p.sendMessage(prefix + lang.get("money_setting"));
                }
                if (b.equalsIgnoreCase("action")) {
                    DPMFunction.currentEditItem.put(p.getUniqueId(), Quadruple.of(t.getA(), item, "action", e.getSlot()));
                    p.closeInventory();
                    p.sendMessage(prefix + lang.get("action_setting"));
                }
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (DPMFunction.currentEditItem.containsKey(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            Quadruple<String, ItemStack, String, Integer> t = DPMFunction.currentEditItem.get(e.getPlayer().getUniqueId());
            if (t.getC().equalsIgnoreCase("price")) {
                t.setB(DPMFunction.setPrice(t.getB(), e.getMessage()));
                Bukkit.getScheduler().runTask(plugin, () -> {
                    DPMFunction.openPriceSettingGUI(e.getPlayer(), t.getA(), t.getB(), t.getD());
                    DPMFunction.currentEditItem.remove(e.getPlayer().getUniqueId());
                });
            }
            if (t.getC().equalsIgnoreCase("action")) {
                t.setB(DPMFunction.setAction(t.getB(), e.getMessage()));
                Bukkit.getScheduler().runTask(plugin, () -> {
                    DPMFunction.openActionSettingGUI(e.getPlayer(), t.getA());
                    DPMFunction.currentEditItem.remove(e.getPlayer().getUniqueId());
                });
            }
        }
    }
}
