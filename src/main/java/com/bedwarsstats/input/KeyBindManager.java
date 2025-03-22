package com.bedwarsstats.input;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class KeyBindManager {
    private static final Map<String, KeyBinding> keyBindings = new HashMap<>();
    private static final Map<String, Consumer<Void>> keyActions = new HashMap<>();

    public static void registerKeybind(String description, int keyCode, Consumer<Void> action) {
        KeyBinding keyBinding = new KeyBinding(description, keyCode, "Bedwars Stats Overlay");
        ClientRegistry.registerKeyBinding(keyBinding);
        keyBindings.put(description, keyBinding);
        keyActions.put(description, action);
    }

    public static void onKeyInput() {
        for (Map.Entry<String, KeyBinding> entry : keyBindings.entrySet()) {
            if (entry.getValue().isPressed()) {
                keyActions.get(entry.getKey()).accept(null);
            }
        }
    }
}
