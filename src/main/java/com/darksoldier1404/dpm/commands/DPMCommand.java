package com.darksoldier1404.dpm.commands;

import com.darksoldier1404.dpm.Menu;
import com.darksoldier1404.dpm.functions.DPMFunction;
import com.darksoldier1404.dppc.builder.command.CommandBuilder;
import com.darksoldier1404.dppc.lang.DLang;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("all")
public class DPMCommand {
    private final CommandBuilder builder;
    private final Menu plugin = Menu.getInstance();
    private final String prefix;
    private final DLang lang;

    public DPMCommand() {
        prefix = plugin.data.getPrefix();
        lang = plugin.data.getLang();
        builder = new CommandBuilder(plugin.data.getPrefix());

        builder.addSubCommand("open", "dpm_use", lang.get("help_open"), true, (p, args) -> {
            if (args.length == 2) DPMFunction.openMenu((Player) p, args[1]);
            else p.sendMessage(prefix + lang.get("help_open"));
        });
        builder.addSubCommand("create", "dpm_admin", lang.get("help_create"), true, (p, args) -> {
            if (args.length == 3) DPMFunction.createMenu((Player) p, args[1], args[2]);
            else p.sendMessage(prefix + lang.get("help_create"));
        });
        builder.addSubCommand("delete", "dpm_admin", lang.get("help_delete"), true, (p, args) -> {
            if (args.length == 2) DPMFunction.deleteMenu((Player) p, args[1]);
            else p.sendMessage(prefix + lang.get("help_delete"));
        });
        builder.addSubCommand("title", "dpm_admin", lang.get("help_title"), true, (p, args) -> {
            if (args.length >= 3) DPMFunction.setTitle((Player) p, args[1], args);
            else p.sendMessage(prefix + lang.get("help_title"));
        });
        builder.addSubCommand("row", "dpm_admin", lang.get("help_row"), true, (p, args) -> {
            if (args.length == 3) DPMFunction.setRow((Player) p, args[1], args[2]);
            else p.sendMessage(prefix + lang.get("help_row"));
        });
        builder.addSubCommand("items", "dpm_admin", lang.get("help_items"), true, (p, args) -> {
            if (args.length == 2) DPMFunction.openItemSettingGUI((Player) p, args[1]);
            else p.sendMessage(prefix + lang.get("help_items"));
        });
        builder.addSubCommand("price", "dpm_admin", lang.get("help_price"), true, (p, args) -> {
            if (args.length == 2) DPMFunction.openPriceSettingGUI((Player) p, args[1]);
            else p.sendMessage(prefix + lang.get("help_price"));
        });
        builder.addSubCommand("aliases", "dpm_admin", lang.get("help_aliases"), true, (p, args) -> {
            if (args.length == 3) DPMFunction.setAliases((Player) p, args[1], args[2]);
            else p.sendMessage(prefix + lang.get("help_aliases"));
        });
        builder.addSubCommand("list", "dpm_admin", lang.get("help_list"), true, (p, args) -> {
            if (args.length == 1) {
                p.sendMessage(prefix + lang.get("menu_list_title"));
                plugin.menus.keySet().forEach(key -> {
                    p.sendMessage(prefix + " - " + key);
                });
            } else p.sendMessage(prefix + lang.get("help_list"));
        });
        builder.addSubCommand("reload", "dpm_admin", lang.get("help_reload"), true, (p, args) -> {
            if (args.length == 3) {
                plugin.data.reload();
                p.sendMessage(prefix + lang.get("reload_message"));
            } else p.sendMessage(prefix + lang.get("help_reload"));
        });

        builder.addSubCommand("action", "dpm_admin", lang.get("help_action"), false, (p, args) -> {
            if (args.length == 2) {
                DPMFunction.openActionSettingGUI((Player) p, args[1]);
            } else p.sendMessage(prefix + lang.get("help_action"));
        });
        for (String c : builder.getSubCommandNames()) {
            builder.addTabCompletion(c, args -> plugin.menus.keySet().stream().toList());
        }
    }

    public CommandExecutor getExecuter() {
        return builder;
    }
}