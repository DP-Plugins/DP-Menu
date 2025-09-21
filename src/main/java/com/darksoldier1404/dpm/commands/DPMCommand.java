package com.darksoldier1404.dpm.commands;

import com.darksoldier1404.dpm.functions.DPMFunction;
import com.darksoldier1404.dppc.builder.command.CommandBuilder;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

import static com.darksoldier1404.dpm.Menu.plugin;

@SuppressWarnings("all")
public class DPMCommand {
    private final CommandBuilder builder;

    public DPMCommand() {
        builder = new CommandBuilder(plugin);

        builder.addSubCommand("open", "dpm.use", plugin.getLang().get("help_open"), true, (p, args) -> {
            if (args.length == 2) DPMFunction.openMenu((Player) p, args[1]);
            else p.sendMessage(plugin.getPrefix() + plugin.getLang().get("help_open"));
            return true;
        });
        builder.addSubCommand("create", "dpm.admin", plugin.getLang().get("help_create"), true, (p, args) -> {
            if (args.length == 3) DPMFunction.createMenu((Player) p, args[1], args[2]);
            else p.sendMessage(plugin.getPrefix() + plugin.getLang().get("help_create"));
            return true;
        });
        builder.addSubCommand("delete", "dpm.admin", plugin.getLang().get("help_delete"), true, (p, args) -> {
            if (args.length == 2) DPMFunction.deleteMenu((Player) p, args[1]);
            else p.sendMessage(plugin.getPrefix() + plugin.getLang().get("help_delete"));
            return true;
        });
        builder.addSubCommand("title", "dpm.admin", plugin.getLang().get("help_title"), true, (p, args) -> {
            if (args.length >= 3) DPMFunction.setTitle((Player) p, args[1], args);
            else p.sendMessage(plugin.getPrefix() + plugin.getLang().get("help_title"));
            return true;
        });
        builder.addSubCommand("row", "dpm.admin", plugin.getLang().get("help_row"), true, (p, args) -> {
            if (args.length == 3) DPMFunction.setRow((Player) p, args[1], args[2]);
            else p.sendMessage(plugin.getPrefix() + plugin.getLang().get("help_row"));
            return true;
        });
        builder.addSubCommand("items", "dpm.admin", plugin.getLang().get("help_items"), true, (p, args) -> {
            if (args.length == 2) DPMFunction.openItemSettingGUI((Player) p, args[1]);
            else p.sendMessage(plugin.getPrefix() + plugin.getLang().get("help_items"));
            return true;
        });
        builder.addSubCommand("price", "dpm.admin", plugin.getLang().get("help_price"), true, (p, args) -> {
            if (args.length == 2) DPMFunction.openPriceSettingGUI((Player) p, args[1]);
            else p.sendMessage(plugin.getPrefix() + plugin.getLang().get("help_price"));
            return true;
        });
        builder.addSubCommand("aliases", "dpm.admin", plugin.getLang().get("help_aliases"), true, (p, args) -> {
            if (args.length == 3) DPMFunction.setAliases((Player) p, args[1], args[2]);
            else p.sendMessage(plugin.getPrefix() + plugin.getLang().get("help_aliases"));
            return true;
        });
        builder.addSubCommand("list", "dpm.admin", plugin.getLang().get("help_list"), true, (p, args) -> {
            if (args.length == 1) {
                p.sendMessage(plugin.getPrefix() + plugin.getLang().get("menu_list_title"));
                plugin.menus.keySet().forEach(key -> {
                    p.sendMessage(plugin.getPrefix() + " - " + key);
                });
            } else p.sendMessage(plugin.getPrefix() + plugin.getLang().get("help_list"));
            return true;
        });
        builder.addSubCommand("reload", "dpm.admin", plugin.getLang().get("help_reload"), true, (p, args) -> {
            if (args.length == 3) {
                plugin.reload();
                p.sendMessage(plugin.getPrefix() + plugin.getLang().get("reload_message"));
            } else p.sendMessage(plugin.getPrefix() + plugin.getLang().get("help_reload"));
            return true;
        });

        builder.addSubCommand("action", "dpm.admin", plugin.getLang().get("help_action"), false, (p, args) -> {
            if (args.length == 2) {
                DPMFunction.openActionSettingGUI((Player) p, args[1]);
            } else p.sendMessage(plugin.getPrefix() + plugin.getLang().get("help_action"));
            return true;
        });
        for (String c : builder.getSubCommandNames()) {
            builder.addTabCompletion(c, args -> plugin.menus.keySet().stream().toList());
        }
    }

    public CommandExecutor getExecuter() {
        return builder;
    }
}