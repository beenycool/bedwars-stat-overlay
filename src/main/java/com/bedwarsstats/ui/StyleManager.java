package com.bedwarsstats.ui;

import com.bedwarsstats.config.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class StyleManager {
    private final Minecraft mc;
    private final FontRenderer fontRenderer;
    private final ConfigManager configManager;

    public StyleManager(ConfigManager configManager) {
        this.mc = Minecraft.getMinecraft();
        this.fontRenderer = mc.fontRenderer;
        this.configManager = configManager;
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        int width = scaledResolution.getScaledWidth();
        int height = scaledResolution.getScaledHeight();

        GlStateManager.pushMatrix();
        GlStateManager.translate(width / 2.0f, height / 2.0f, 0);
        GlStateManager.scale(1.0f, 1.0f, 1.0f);

        String hudText = "Bedwars Stats Overlay";
        int textWidth = fontRenderer.getStringWidth(hudText);
        fontRenderer.drawStringWithShadow(hudText, -textWidth / 2, -4, 0xFFFFFF);

        GlStateManager.popMatrix();
    }

    public void applyCustomStyle() {
        // Apply custom styles from configManager
        // Example: background color, border color, font size, etc.
    }
}
