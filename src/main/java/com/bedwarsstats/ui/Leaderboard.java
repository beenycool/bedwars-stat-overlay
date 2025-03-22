package com.bedwarsstats.ui;

import com.bedwarsstats.api.StatProvider;
import com.bedwarsstats.config.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class Leaderboard {
    private final Minecraft mc;
    private final FontRenderer fontRenderer;
    private final StatProvider statProvider;
    private List<StatProvider.PlayerStats> topPlayers;

    public Leaderboard(StatProvider statProvider) {
        this.mc = Minecraft.getMinecraft();
        this.fontRenderer = mc.fontRenderer;
        this.statProvider = statProvider;
        this.topPlayers = null;
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
        if (topPlayers == null) return;

        ScaledResolution scaledResolution = new ScaledResolution(mc);
        int width = scaledResolution.getScaledWidth();
        int height = scaledResolution.getScaledHeight();

        GlStateManager.pushMatrix();
        GlStateManager.translate(width / 2.0f, height / 2.0f, 0);
        GlStateManager.scale(1.0f, 1.0f, 1.0f);

        int yOffset = -10;
        for (StatProvider.PlayerStats playerStats : topPlayers) {
            String playerText = playerStats.getPlayerName() + ": FKDR=" + playerStats.getFkdr() + ", Wins=" + playerStats.getWins() + ", Losses=" + playerStats.getLosses();
            int textWidth = fontRenderer.getStringWidth(playerText);
            fontRenderer.drawStringWithShadow(playerText, -textWidth / 2, yOffset, 0xFFFFFF);
            yOffset += 10;
        }

        GlStateManager.popMatrix();
    }

    public void updateTopPlayers(List<StatProvider.PlayerStats> topPlayers) {
        this.topPlayers = topPlayers;
    }

    public void fetchAndDisplayTopPlayers() {
        statProvider.fetchTopPlayers(new StatProvider.StatCallback() {
            @Override
            public void onSuccess(List<StatProvider.PlayerStats> stats) {
                updateTopPlayers(stats);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }
}
