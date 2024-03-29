package net.mrquba.texthotkey;

import static net.minecraft.server.command.CommandManager.*;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Text_Hotkey implements ModInitializer {
	public static final String MOD_ID = "text_hotkey";
	public static String command_value = "hello";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, dedicated) ->
				dispatcher.register(
						ClientCommandManager.literal("texthotkey")
								.then(ClientCommandManager.literal("set")
										.then(ClientCommandManager.argument("command", StringArgumentType.greedyString())
												.executes(context -> {
													command_value = StringArgumentType.getString(context, "command");
													context.getSource().sendFeedback(Text.literal("Set text: " + command_value));
													return 1;
												})
										)
								)
				)
		);
	}
}
