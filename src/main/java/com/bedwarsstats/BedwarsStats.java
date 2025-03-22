package com.bedwarsstats;

import com.bedwarsstats.api.HypixelAPI;
import com.bedwarsstats.commands.StatsCommand;
import com.bedwarsstats.config.ConfigManager;
import com.bedwarsstats.input.KeyBindManager;
import com.bedwarsstats.ui.HUDRenderer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

@Mod(modid = BedwarsStats.MODID, name = BedwarsStats.NAME, version = BedwarsStats.VERSION)
public class BedwarsStats {
    public static final String MODID = "bedwarsstats";
    public static final String NAME = "Bedwars Stats Overlay";
    public static final String VERSION = "1.0";

    private ConfigManager configManager;
    private HypixelAPI hypixelAPI;
    private HUDRenderer hudRenderer;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        configManager = new ConfigManager();
        hypixelAPI = new HypixelAPI(configManager.getString("api.key", ""));
        hudRenderer = new HUDRenderer();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(hudRenderer);
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new StatsCommand(hypixelAPI));
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        KeyBindManager.onKeyInput();
    }
}
