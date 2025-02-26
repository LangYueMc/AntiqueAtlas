package hunternif.mc.impl.atlas;

import hunternif.mc.impl.atlas.client.gui.GuiAtlas;
import hunternif.mc.impl.atlas.network.AntiqueAtlasNetworking;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;

@Environment(EnvType.CLIENT)
public class AntiqueAtlasModClient {

	private static GuiAtlas guiAtlas;

	public static GuiAtlas getAtlasGUI() {
		if (guiAtlas == null) {
			guiAtlas = new GuiAtlas();
			guiAtlas.setMapScale(AntiqueAtlasMod.CONFIG.defaultScale);
		}
		return guiAtlas;
	}

	public static void openAtlasGUI(ItemStack stack) {
		openAtlasGUI(getAtlasGUI().prepareToOpen(stack));
	}

	public static void openAtlasGUI() {
		openAtlasGUI(getAtlasGUI().prepareToOpen());
	}

	private static void openAtlasGUI(GuiAtlas gui) {
		MinecraftClient mc = MinecraftClient.getInstance();
		if (mc.currentScreen == null) { // In-game screen
			guiAtlas.updateL18n();
			mc.setScreen(gui);
		}
	}

	public static void init() {
		ClientProxy clientProxy = new ClientProxy();
		clientProxy.initClient();

		AntiqueAtlasNetworking.registerS2CListeners();
	}
}
