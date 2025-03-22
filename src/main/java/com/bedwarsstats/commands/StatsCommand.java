package com.bedwarsstats.commands;

import com.bedwarsstats.api.HypixelAPI;
import com.bedwarsstats.api.StatProvider;
import com.bedwarsstats.api.StatProvider.PlayerStats;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

public class StatsCommand {
    private final HypixelAPI hypixelAPI;

    public StatsCommand(HypixelAPI hypixelAPI) {
        this.hypixelAPI = hypixelAPI;
    }

    public void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("stats")
                .then(Commands.argument("player", StringArgumentType.string())
                        .executes(this::executeStats));

        dispatcher.register(builder);
    }

    private int executeStats(CommandContext<CommandSource> context) {
        String playerName = StringArgumentType.getString(context, "player");
        CommandSource source = context.getSource();

        hypixelAPI.fetchPlayerStats(playerName, new StatProvider.StatCallback() {
            @Override
            public void onSuccess(PlayerStats stats) {
                source.sendFeedback(new StringTextComponent("Stats for " + playerName + ": " + stats.toString()), false);
            }

            @Override
            public void onError(Exception e) {
                source.sendErrorMessage(new StringTextComponent("Failed to fetch stats for " + playerName));
            }
        });

        return 1;
    }
}
