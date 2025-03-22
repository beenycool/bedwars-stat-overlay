package com.bedwarsstats.ui;

import com.bedwarsstats.config.ConfigManager;
import com.bedwarsstats.input.KeyBindManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class HUDRenderer {
    private final Minecraft mc;
    private final FontRenderer fontRenderer;
    private boolean hudVisible;

    public HUDRenderer() {
        this.mc = Minecraft.getMinecraft();
        this.fontRenderer = mc.fontRenderer;
        this.hudVisible = true;
        KeyBindManager.registerKeybind("Toggle HUD", Keyboard.KEY_V, this::toggleHUD);
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
        if (!hudVisible) return;

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

    private void toggleHUD() {
        hudVisible = !hudVisible;
    }
}
