package com.bedwarsstats.commands;

import com.bedwarsstats.config.ConfigManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

public class ConfigCommand {
    private final ConfigManager configManager;

    public ConfigCommand(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("bwconfig")
                .then(Commands.literal("apikey")
                        .then(Commands.argument("key", StringArgumentType.string())
                                .executes(this::setApiKey)))
                .then(Commands.literal("hud")
                        .then(Commands.argument("setting", StringArgumentType.string())
                                .then(Commands.argument("value", StringArgumentType.string())
                                        .executes(this::setHudConfig))))
                .then(Commands.literal("opacity")
                        .then(Commands.argument("value", StringArgumentType.integer(0, 100))
                                .executes(this::setOpacity)))
                .then(Commands.literal("combat")
                        .then(Commands.argument("value", StringArgumentType.bool())
                                .executes(this::setCombatHide)))
                .then(Commands.literal("style")
                        .then(Commands.argument("setting", StringArgumentType.string())
                                .then(Commands.argument("value", StringArgumentType.string())
                                        .executes(this::setStyle))))
                .then(Commands.literal("keybind")
                        .then(Commands.argument("key", StringArgumentType.string())
                                .executes(this::setKeybind)))
                .then(Commands.literal("team")
                        .then(Commands.argument("color", StringArgumentType.string())
                                .then(Commands.argument("hex", StringArgumentType.string())
                                        .executes(this::setTeamColor))))
                .then(Commands.literal("self")
                        .then(Commands.argument("value", StringArgumentType.string())
                                .executes(this::setSelfVisibility)))
                .then(Commands.literal("leaderboard")
                        .then(Commands.argument("setting", StringArgumentType.string())
                                .executes(this::setLeaderboardConfig)))
                .then(Commands.literal("provider")
                        .then(Commands.argument("name", StringArgumentType.string())
                                .executes(this::setApiProvider)))
                .then(Commands.literal("reset")
                        .executes(this::resetConfig));

        dispatcher.register(builder);
    }

    private int setApiKey(CommandContext<CommandSource> context) {
        String key = StringArgumentType.getString(context, "key");
        configManager.setString("api.key", key);
        context.getSource().sendFeedback(new StringTextComponent("API key set to: " + key), false);
        return 1;
    }

    private int setHudConfig(CommandContext<CommandSource> context) {
        String setting = StringArgumentType.getString(context, "setting");
        String value = StringArgumentType.getString(context, "value");
        configManager.setString("hud." + setting, value);
        context.getSource().sendFeedback(new StringTextComponent("HUD setting " + setting + " set to: " + value), false);
        return 1;
    }

    private int setOpacity(CommandContext<CommandSource> context) {
        int value = IntegerArgumentType.getInteger(context, "value");
        configManager.setInt("hud.opacity", value);
        context.getSource().sendFeedback(new StringTextComponent("HUD opacity set to: " + value), false);
        return 1;
    }

    private int setCombatHide(CommandContext<CommandSource> context) {
        boolean value = BoolArgumentType.getBool(context, "value");
        configManager.setBoolean("hud.combatHide", value);
        context.getSource().sendFeedback(new StringTextComponent("Combat hide set to: " + value), false);
        return 1;
    }

    private int setStyle(CommandContext<CommandSource> context) {
        String setting = StringArgumentType.getString(context, "setting");
        String value = StringArgumentType.getString(context, "value");
        configManager.setString("hud.style." + setting, value);
        context.getSource().sendFeedback(new StringTextComponent("HUD style " + setting + " set to: " + value), false);
        return 1;
    }

    private int setKeybind(CommandContext<CommandSource> context) {
        String key = StringArgumentType.getString(context, "key");
        configManager.setString("hud.toggleKeybind", key);
        context.getSource().sendFeedback(new StringTextComponent("Toggle keybind set to: " + key), false);
        return 1;
    }

    private int setTeamColor(CommandContext<CommandSource> context) {
        String color = StringArgumentType.getString(context, "color");
        String hex = StringArgumentType.getString(context, "hex");
        configManager.setString("hud.teamColors." + color, hex);
        context.getSource().sendFeedback(new StringTextComponent("Team color " + color + " set to: " + hex), false);
        return 1;
    }

    private int setSelfVisibility(CommandContext<CommandSource> context) {
        String value = StringArgumentType.getString(context, "value");
        configManager.setString("hud.hideSelf", value);
        context.getSource().sendFeedback(new StringTextComponent("Self visibility set to: " + value), false);
        return 1;
    }

    private int setLeaderboardConfig(CommandContext<CommandSource> context) {
        String setting = StringArgumentType.getString(context, "setting");
        configManager.setString("hud.leaderboard." + setting, setting);
        context.getSource().sendFeedback(new StringTextComponent("Leaderboard setting " + setting + " set to: " + setting), false);
        return 1;
    }

    private int setApiProvider(CommandContext<CommandSource> context) {
        String name = StringArgumentType.getString(context, "name");
        configManager.setString("api.provider", name);
        context.getSource().sendFeedback(new StringTextComponent("API provider set to: " + name), false);
        return 1;
    }

    private int resetConfig(CommandContext<CommandSource> context) {
        configManager.resetConfig();
        context.getSource().sendFeedback(new StringTextComponent("All settings have been reset"), false);
        return 1;
    }
}
