package com.darksoldier1404.dpm.functions;

import com.darksoldier1404.dppc.api.inventory.DInventory;
import com.darksoldier1404.dppc.api.placeholder.PlaceholderUtils;
import com.darksoldier1404.dppc.utils.*;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.*;

import static com.darksoldier1404.dpm.Menu.plugin;

public class DPMFunction {
    public static final Map<UUID, Quadruple<String, ItemStack, String, Integer>> currentEditItem = new HashMap<>();

    public static void openMenu(Player p, String name) {
        if (!isValid(name)) return;
        DInventory inv = getMenuInventory(name);
        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) == null) continue;
            inv.setItem(i, initPlaceHolder(inv.getItem(i), p));
        }
        p.openInventory(inv.getInventory());
    }

    public static void createMenu(Player p, String name, String srow) {
        if (isValid(name)) {
            p.sendMessage(plugin.getPrefix() + plugin.getLang().get("menu_exists"));
            return;
        }
        int row;
        try {
            row = Integer.parseInt(srow);
        } catch (NumberFormatException e) {
            p.sendMessage(plugin.getPrefix() + plugin.getLang().get("menu_srow_wrong_num"));
            return;
        }
        if (row < 1 || row > 6) {
            p.sendMessage(plugin.getPrefix() + plugin.getLang().get("menu_srow_wrong"));
            return;
        }
        YamlConfiguration data = new YamlConfiguration();
        data.set("Menu.NAME", name);
        data.set("Menu.ROWS", row);
        plugin.menus.put(name, data);
        saveMenu(name);
        p.sendMessage(plugin.getPrefix() + name + plugin.getLang().get("menu_create"));
    }

    public static void deleteMenu(Player p, String name) {
        if (!isValid(name)) {
            p.sendMessage(plugin.getPrefix() + plugin.getLang().get("menu_not_exists"));
            return;
        }
        new File(plugin.getDataFolder() + "/menus/" + name + ".yml").delete();
        plugin.menus.remove(name);
        p.sendMessage(plugin.getPrefix() + name + plugin.getLang().get("menu_delete"));
    }

    public static void setTitle(Player p, String name, String... args) {
        if (!isValid(name)) {
            p.sendMessage(plugin.getPrefix() + plugin.getLang().get("menu_not_exists"));
            return;
        }
        String title = ColorUtils.applyColor(getText(args, 2));
        plugin.menus.get(name).set("Menu.TITLE", title);
        p.sendMessage(plugin.getPrefix() + name + plugin.getLang().get("menu_title") + title);
        saveMenu(name);
    }

    public static void setAliases(Player p, String name, String aliases) {
        if (!plugin.menus.containsKey(name)) {
            p.sendMessage(plugin.getPrefix() + plugin.getLang().get("menu_not_exists"));
            return;
        }
        YamlConfiguration data = plugin.menus.get(name);
        data.set("Menu.ALIASES", aliases);
        plugin.menus.put(name, data);
        plugin.getLogger().info(plugin.getLang().get("menu_aliases_description") + aliases);
        p.sendMessage(plugin.getPrefix() + name + plugin.getLang().get("menu_aliases") + aliases);
        saveMenu(name);
    }

    public static void setRow(Player p, String name, String srow) {
        if (!isValid(name)) {
            p.sendMessage(plugin.getPrefix() + plugin.getLang().get("menu_not_exists"));
            return;
        }
        int row;
        try {
            row = Integer.parseInt(srow);
        } catch (NumberFormatException e) {
            p.sendMessage(plugin.getPrefix() + plugin.getLang().get("menu_srow_wrong_num"));
            return;
        }
        if (row < 1 || row > 6) {
            p.sendMessage(plugin.getPrefix() + plugin.getLang().get("menu_srow_wrong"));
            return;
        }
        plugin.menus.get(name).set("Menu.ROWS", row);
        p.sendMessage(plugin.getPrefix() + name + plugin.getLang().get("menu_row_set") + row);
        saveMenu(name);
    }

    public static void openItemSettingGUI(Player p, String name) { // 1
        if (!isValid(name)) {
            p.sendMessage(plugin.getPrefix() + plugin.getLang().get("menu_not_exists"));
            return;
        }
        DInventory inv = getMenuInventory(name);
        inv.setObj(Tuple.of(name, "ITEMS"));
        p.openInventory(inv.getInventory());
    }

    public static void saveItemSetting(Player p, String name, DInventory inv) {
        if (!isValid(name)) return;

        YamlConfiguration data = plugin.menus.get(name);
        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack item = inv.getItem(i);
            if (item == null || item.getType() == Material.AIR) {
                data.set("Menu.ITEMS." + i, null);
            } else {
                data.set("Menu.ITEMS." + i, item);
            }
        }
        saveMenu(name);
        p.sendMessage(plugin.getPrefix() + name + plugin.getLang().get("menu_items"));
    }

    public static DInventory getMenuInventory(String name) {
        YamlConfiguration data = plugin.menus.get(name);
        String rows = data.getString("Menu.ROWS");
        String title = data.getString("Menu.TITLE") == null ? plugin.getLang().get("menu_title_not_set") : data.getString("Menu.TITLE");
        title = ColorUtils.applyColor(title);
        DInventory inv = new DInventory(title, Integer.parseInt(rows) * 9, plugin);
        if (data.get("Menu.ITEMS") != null) {
            data.getConfigurationSection("Menu.ITEMS").getKeys(false).forEach(key -> {
                inv.setItem(Integer.parseInt(key), data.getItemStack("Menu.ITEMS." + key));
            });
        }
        return inv;
    }

    public static void openPriceSettingGUI(Player p, String name) {
        if (!isValid(name)) {
            p.sendMessage(plugin.getPrefix() + plugin.getLang().get("menu_not_exists"));
            return;
        }
        DInventory inv = getMenuInventory(name);
        inv.setObj(Tuple.of(name, "PRICE"));
        p.openInventory(inv.getInventory());
    }

    public static void openPriceSettingGUI(Player p, String name, ItemStack item, int slot) {
        if (!isValid(name)) {
            p.sendMessage(plugin.getPrefix() + plugin.getLang().get("menu_not_exists"));
            return;
        }
        DInventory inv = getMenuInventory(name);
        inv.setItem(slot, item);
        inv.setObj(Tuple.of(name, "PRICE"));
        p.openInventory(inv.getInventory());
    }

    public static ItemStack setPrice(ItemStack item, String price) {
        return NBT.setStringTag(item, "dpm.price", price);
    }

    public static ItemStack setAction(ItemStack item, String action) {
        return NBT.setStringTag(item, "dpm.action", action);
    }

    public static boolean isValid(String name) {
        return plugin.menus.containsKey(name);
    }

    public static void loadAllMenus() {
        List<YamlConfiguration> menus = ConfigUtils.loadCustomDataList(plugin, "menus");
        menus.forEach(menu -> {
            plugin.menus.put(menu.getString("Menu.NAME"), menu);
        });
    }

    public static void saveMenu(String name) {
        ConfigUtils.saveCustomData(plugin, plugin.menus.get(name), name, "menus");
    }

    public static String getText(String[] args, int line) {
        StringBuilder s = new StringBuilder();
        args = Arrays.copyOfRange(args, line, args.length);
        Iterator<String> i = Arrays.stream(args).iterator();
        while (i.hasNext()) {
            s.append(i.next()).append(" ");
        }
        if (s.charAt(s.length() - 1) == ' ') {
            s.deleteCharAt(s.length() - 1);
        }
        return s.toString();
    }

    public static ItemStack initPlaceHolder(ItemStack item, Player p) {
        if (item == null || item.getType() == Material.AIR) return item;
        ItemMeta im = item.getItemMeta();
        if (im.hasDisplayName()) {
            im.setDisplayName(initReplacer(item, im.getDisplayName(), p));
        }
        if (im.hasLore()) {
            List<String> lore = new ArrayList<>();
            for (String s : im.getLore()) {
                lore.add(initReplacer(item, s, p));
            }
            im.setLore(lore);
        }
        item.setItemMeta(im);
        return item;
    }

    public static String initReplacer(ItemStack item, String text, Player p) {
        if (item == null || item.getType() == Material.AIR) return text;
        text = PlaceholderUtils.applyPlaceholder(p, text);
        return text;
    }

    public static void openActionSettingGUI(Player p, String name) {
        if (!isValid(name)) {
            p.sendMessage(plugin.getPrefix() + plugin.getLang().get("menu_not_exists"));
            return;
        }
        DInventory inv = getMenuInventory(name);
        inv.setObj(Tuple.of(name, "ACTION"));
        p.openInventory(inv.getInventory());
    }
}
