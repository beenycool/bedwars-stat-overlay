package com.bedwarsstats.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CombatDetector {
    private final Minecraft mc;
    private boolean inCombat;

    public CombatDetector() {
        this.mc = Minecraft.getMinecraft();
        this.inCombat = false;
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
        if (inCombat) {
            // Hide HUD during combat
            return;
        }

        // Render HUD
    }

    @SubscribeEvent
    public void onPlayerHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            if (player == mc.player) {
                inCombat = true;
                // Reset combat status after a delay
                mc.addScheduledTask(() -> {
                    try {
                        Thread.sleep(5000); // 5 seconds delay
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    inCombat = false;
                });
            }
        }
    }

    private boolean isPlayerHoldingSword(EntityPlayer player) {
        return player.getHeldItemMainhand().getItem() instanceof ItemSword;
    }
}
