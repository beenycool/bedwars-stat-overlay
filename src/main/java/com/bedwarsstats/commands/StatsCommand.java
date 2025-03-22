package com.bedwarsstats.commands;

import com.bedwarsstats.api.HypixelAPI;
import com.bedwarsstats.api.StatProvider;
import com.bedwarsstats.api.StatProvider.PlayerStats;
import com.bedwarsstats.ui.Leaderboard;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

public class StatsCommand {
    private final HypixelAPI hypixelAPI;
    private final Leaderboard leaderboard;

    public StatsCommand(HypixelAPI hypixelAPI, Leaderboard leaderboard) {
        this.hypixelAPI = hypixelAPI;
        this.leaderboard = leaderboard;
    }

    public void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("stats")
                .then(Commands.argument("player", StringArgumentType.string())
                        .executes(this::executeStats))
                .then(Commands.literal("topplayers")
                        .executes(this::executeTopPlayers));

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

    private int executeTopPlayers(CommandContext<CommandSource> context) {
        CommandSource source = context.getSource();

        leaderboard.fetchAndDisplayTopPlayers();
        source.sendFeedback(new StringTextComponent("Fetching top players' stats..."), false);

        return 1;
    }
}
