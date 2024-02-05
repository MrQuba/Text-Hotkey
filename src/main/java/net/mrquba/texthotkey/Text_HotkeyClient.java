package net.mrquba.texthotkey;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

import static net.fabricmc.fabric.impl.command.client.ClientCommandInternals.executeCommand;
import static net.fabricmc.fabric.impl.command.client.ClientCommandInternals.getActiveDispatcher;
import static net.mrquba.texthotkey.Text_Hotkey.command_value;

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
            assert client.player != null;

            while (keyBinding.wasPressed()) {
                String message = command_value;

                if (message.startsWith("/")) {
                    String command_message = message.substring(1);
                    assert MinecraftClient.getInstance().player != null;
                    MinecraftClient.getInstance().player.networkHandler.sendChatCommand(command_message);
                }
                else {
                    assert MinecraftClient.getInstance().player != null;
                    MinecraftClient.getInstance().player.networkHandler.sendChatMessage(message);
                }
            }
        });
    }
}