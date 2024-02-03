package net.mrquba.texthotkey;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.command.CommandSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

public class Text_HotkeyClient implements ClientModInitializer{

    @Override
    public void onInitializeClient() {
        KeyBinding keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Command Hotkey",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                "Text Hotkey"
        ));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            MinecraftServer server = MinecraftClient.getInstance().getServer();

            while (keyBinding.wasPressed()) {
                assert client.player != null;
                String message = Text_Hotkey.command_value;

                if (message.startsWith("/")) {
                    String commandWithoutSlash = message.substring(1);
                    server = client.player.getServer();
                    if(server != null) {
                        CommandSource commandSource = client.player.getCommandSource();
                        assert server != null;
                        CommandDispatcher<ServerCommandSource> dispatcher = server.getCommandManager().getDispatcher();
                        ParseResults<ServerCommandSource> parseResults = dispatcher.parse(commandWithoutSlash, client.player.getCommandSource());

                        try {
                            dispatcher.execute(parseResults);
                        } catch (CommandSyntaxException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else {
                        client.player.sendMessage(Text.literal("Cannot execute command. Not connected to a server."), false);
                    }
                }  else {
                    client.player.sendMessage(Text.literal(message), false);
                }
            }
        });
    }
}
