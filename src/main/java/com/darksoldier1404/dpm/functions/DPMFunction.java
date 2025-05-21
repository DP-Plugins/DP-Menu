package com.darksoldier1404.dpm.functions;

import com.darksoldier1404.dpm.Menu;
import com.darksoldier1404.dppc.api.essentials.MoneyAPI;
import com.darksoldier1404.dppc.api.inventory.DInventory;
import com.darksoldier1404.dppc.lang.DLang;
import com.darksoldier1404.dppc.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.*;

@SuppressWarnings("all")
public class DPMFunction {
    private static final Menu plugin = Menu.getInstance();
    public static final Map<UUID, Quadruple<String, ItemStack, String, Integer>> currentEditItem = new HashMap<>();
    private static final DLang lang = plugin.data.getLang();
    public static void openMenu(Player p, String name) {
        if (!isValid(name)) return;
        DInventory inv = getMenuInventory(name);
        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) == null) continue;
            int finalI = i;
            Bukkit.getScheduler().runTask(plugin, () -> {
                inv.setItem(finalI, initPlaceHolder(inv.getItem(finalI), p));
            });
        }
        p.openInventory(inv);
    }

    public static void createMenu(Player p, String name, String srow) {
        if (isValid(name)) {
            p.sendMessage(plugin.data.getPrefix() + lang.get("menu_exists"));
            return;
        }
        int row;
        try {
            row = Integer.parseInt(srow);
        } catch (NumberFormatException e) {
            p.sendMessage(plugin.data.getPrefix() + lang.get("menu_srow_wrong_num"));
            return;
        }
        if (row < 1 || row > 6) {
            p.sendMessage(plugin.data.getPrefix() + lang.get("menu_srow_wrong"));
            return;
        }
        YamlConfiguration data = new YamlConfiguration();
        data.set("Menu.NAME", name);
        data.set("Menu.ROWS", row);
        plugin.menus.put(name, data);
        saveMenu(name);
        p.sendMessage(plugin.data.getPrefix() + name + lang.get("menu_create"));
    }

    public static void deleteMenu(Player p, String name) {
        if (!isValid(name)) {
            p.sendMessage(plugin.data.getPrefix() + lang.get("menu_not_exists"));
            return;
        }
        new File(plugin.getDataFolder() + "/menus/" + name + ".yml").delete();
        plugin.menus.remove(name);
        p.sendMessage(plugin.data.getPrefix() + name + lang.get("menu_delete"));
    }

    public static void setTitle(Player p, String name, String... args) {
        if (!isValid(name)) {
            p.sendMessage(plugin.data.getPrefix() + lang.get("menu_not_exists"));
            return;
        }
        String title = ColorUtils.applyColor(getText(args, 2));
        plugin.menus.get(name).set("Menu.TITLE", title);
        p.sendMessage(plugin.data.getPrefix() + name + lang.get("menu_title")+ title);
        saveMenu(name);
    }

    public static void setAliases(Player p, String name, String aliases) {
        if (!plugin.menus.containsKey(name)) {
            p.sendMessage(plugin.data.getPrefix() + lang.get("menu_not_exists"));
            return;
        }
        YamlConfiguration data = plugin.menus.get(name);
        data.set("Menu.ALIASES", aliases);
        plugin.menus.put(name, data);
        plugin.getLogger().info(lang.get("menu_aliases_description") + aliases);
        p.sendMessage(plugin.data.getPrefix() + name + lang.get("menu_aliases") + aliases);
        saveMenu(name);
    }

    public static String getMenuNameByAliases(String aliases) {
        for (String name : plugin.menus.keySet()) {
            if (plugin.menus.get(name).getConfigurationSection("Menu.ALIASES").getKeys(false) == null) continue;
            if (plugin.menus.get(name).getString("Menu.ALIASES").equalsIgnoreCase(aliases)) {
                return name;
            }
        }
        return null;
    }

    public static void setRow(Player p, String name, String srow) {
        if (!isValid(name)) {
            p.sendMessage(plugin.data.getPrefix() + lang.get("menu_not_exists"));
            return;
        }
        int row;
        try {
            row = Integer.parseInt(srow);
        } catch (NumberFormatException e) {
            p.sendMessage(plugin.data.getPrefix() + lang.get("menu_srow_wrong_num"));
            return;
        }
        if (row < 1 || row > 6) {
            p.sendMessage(plugin.data.getPrefix() + lang.get("menu_srow_wrong"));
            return;
        }
        plugin.menus.get(name).set("Menu.ROWS", row);
        p.sendMessage(plugin.data.getPrefix() + name + lang.get("menu_row_set") + row);
        saveMenu(name);
    }

    public static void openItemSettingGUI(Player p, String name) { // 1
        if (!isValid(name)) {
            p.sendMessage(plugin.data.getPrefix() + lang.get("menu_not_exists"));
            return;
        }
        DInventory inv = getMenuInventory(name);
        inv.setObj(Tuple.of(name, "ITEMS"));
        p.openInventory(inv);
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
        p.sendMessage(plugin.data.getPrefix() + name + lang.get("menu_items"));
    }

    public static DInventory getMenuInventory(String name) {
        YamlConfiguration data = plugin.menus.get(name);
        String rows = data.getString("Menu.ROWS");
        String title = data.getString("Menu.TITLE") == null ? lang.get("menu_title_not_set") : data.getString("Menu.TITLE");
        title = ColorUtils.applyColor(title);
        DInventory inv = new DInventory(null, title, Integer.parseInt(rows) * 9, plugin);
        if (data.get("Menu.ITEMS") != null) {
            data.getConfigurationSection("Menu.ITEMS").getKeys(false).forEach(key -> {
                inv.setItem(Integer.parseInt(key), data.getItemStack("Menu.ITEMS." + key));
            });
        }
        return inv;
    }

    public static void openPriceSettingGUI(Player p, String name) {
        if (!isValid(name)) {
            p.sendMessage(plugin.data.getPrefix() + lang.get("menu_not_exists"));
            return;
        }
        DInventory inv = getMenuInventory(name);
        inv.setObj(Tuple.of(name, "PRICE"));
        p.openInventory(inv);
    }

    public static void openPriceSettingGUI(Player p, String name, ItemStack item, int slot) {
        if (!isValid(name)) {
            p.sendMessage(plugin.data.getPrefix() + lang.get("menu_not_exists"));
            return;
        }
        DInventory inv = getMenuInventory(name);
        inv.setItem(slot, item);
        inv.setObj(Tuple.of(name, "PRICE"));
        p.openInventory(inv);
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

    public static void openCWCSettingGUI(Player p, String name) {
        if (!isValid(name)) {
            p.sendMessage(plugin.data.getPrefix() + lang.get("menu_not_exists"));
            return;
        }
        DInventory inv = getMenuInventory(name);
        inv.setObj(Tuple.of(name, "CWC"));
        p.openInventory(inv);
    }

    public static ItemStack initPlaceHolder(ItemStack item, Player p) {
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
        text = text.replace("<price>", NBT.getStringTag(item, "dpm.price"));
        text = text.replace("<cwc>", NBT.getStringTag(item, "dpm.cwc"));
        text = text.replace("<p_name>", p.getName());
        text = text.replace("<p_displayname>", p.getDisplayName());
        text = text.replace("<p_money>", MoneyAPI.getMoney(p).toString());
        text = text.replace("<p_level>", String.valueOf(p.getLevel()));
        text = text.replace("<p_exp>", String.valueOf(p.getExp()));
        text = text.replace("<p_health>", String.valueOf(p.getHealth()));
        text = text.replace("<p_maxhealth>", String.valueOf(p.getMaxHealth()));
        text = text.replace("<p_food>", String.valueOf(p.getFoodLevel()));
        text = text.replace("<p_gamemode>", String.valueOf(p.getGameMode()));
        text = text.replace("<p_world>", p.getWorld().getName());
        text = text.replace("<p_x>", String.valueOf(p.getLocation().getBlockX()));
        text = text.replace("<p_y>", String.valueOf(p.getLocation().getBlockY()));
        text = text.replace("<p_z>", String.valueOf(p.getLocation().getBlockZ()));
        return text;
    }

    public static void openActionSettingGUI(Player p, String name) {
        if (!isValid(name)) {
            p.sendMessage(plugin.data.getPrefix() + lang.get("menu_not_exists"));
            return;
        }
        DInventory inv = getMenuInventory(name);
        inv.setObj(Tuple.of(name, "ACTION"));
        p.openInventory(inv);
    }
}
