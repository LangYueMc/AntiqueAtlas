package hunternif.mc.impl.atlas.network.packet.s2c.play;

import dev.architectury.networking.NetworkManager;
import hunternif.mc.impl.atlas.AntiqueAtlasMod;
import hunternif.mc.impl.atlas.core.AtlasData;
import hunternif.mc.impl.atlas.core.TileInfo;
import hunternif.mc.impl.atlas.network.packet.s2c.S2CPacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DimensionUpdateS2CPacket extends S2CPacket {
	public static final Identifier ID = AntiqueAtlasMod.id("packet", "s2c", "dimension", "update");

	public DimensionUpdateS2CPacket(int atlasID, RegistryKey<World> world, Collection<TileInfo> tiles) {
		this.writeVarInt(atlasID);
		this.writeIdentifier(world.getValue());
		this.writeVarInt(tiles.size());

		for (TileInfo tile : tiles) {
			this.writeVarInt(tile.x);
			this.writeVarInt(tile.z);
			this.writeIdentifier(tile.id);
		}
	}

	@Override
	public Identifier getId() {
		return ID;
	}

	public static void apply(PacketByteBuf buf, NetworkManager.PacketContext context) {
		int atlasID = buf.readVarInt();
		RegistryKey<World> world = RegistryKey.of(Registry.WORLD_KEY, buf.readIdentifier());
		int tileCount = buf.readVarInt();

		if (world == null) {
			// TODO FABRIC
			return;
		}

		List<TileInfo> tiles = new ArrayList<>();
		for (int i = 0; i < tileCount; ++i) {
			tiles.add(new TileInfo(
					buf.readVarInt(),
					buf.readVarInt(),
					buf.readIdentifier())
			);
		}

		context.queue(() -> {
			PlayerEntity player = context.getPlayer();
			assert player != null;
			AtlasData data = AntiqueAtlasMod.tileData.getData(atlasID, player.getEntityWorld());

			for (TileInfo info : tiles) {
				data.getWorldData(world).setTile(info.x, info.z, info.id);
			}
		});
	}
}
