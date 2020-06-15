package hu.montlikadani.tablist.bukkit.listeners;

import java.lang.reflect.Field;
import java.util.List;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

import hu.montlikadani.tablist.bukkit.ConfigValues;
import hu.montlikadani.tablist.bukkit.Perm;
import hu.montlikadani.tablist.bukkit.TabList;
import hu.montlikadani.tablist.bukkit.utils.ReflectionUtils;
import hu.montlikadani.tablist.bukkit.utils.ServerVersion.Version;

public class SpectatorVisible {

	public void onSpectatorChange() {
		if (!ConfigValues.isRemoveGrayColorFromTabInSpec()) {
			return;
		}

		ProtocolLibrary.getProtocolManager()
				.addPacketListener(new PacketAdapter(TabList.getInstance(), PacketType.Play.Server.PLAYER_INFO) {
					@SuppressWarnings("unchecked")
					@Override
					public void onPacketSending(PacketEvent ev) {
						if (ev.getPlayer().hasPermission(Perm.SEESPECTATOR.getPerm())) {
							return;
						}

						try {
							Object packetPlayOutPlayerInfo = ev.getPacket().getHandle();
							Class<?> enumPlayerInfoAction = null;
							if (Version.isCurrentLower(Version.v1_9_R1)) {
								enumPlayerInfoAction = ReflectionUtils.getNMSClass("EnumPlayerInfoAction");
							} else {
								enumPlayerInfoAction = packetPlayOutPlayerInfo.getClass().getDeclaredClasses()[1];
							}

							if (enumPlayerInfoAction == null) {
								enumPlayerInfoAction = packetPlayOutPlayerInfo.getClass().getDeclaredClasses()[2];
							}

							Class<?> enumGameMode = ReflectionUtils.getNMSClass("EnumGamemode");
							if (enumGameMode == null) {
								enumGameMode = ReflectionUtils.getNMSClass("WorldSettings$EnumGamemode");
							}

							Object action = ReflectionUtils.getField(packetPlayOutPlayerInfo, "a")
									.get(packetPlayOutPlayerInfo);

							if (action == ReflectionUtils.getField(enumPlayerInfoAction, "UPDATE_GAME_MODE")
									.get(enumPlayerInfoAction)
									|| action == ReflectionUtils.getField(enumPlayerInfoAction, "ADD_PLAYER")
											.get(enumPlayerInfoAction)) {
								List<Object> infoList = (List<Object>) ReflectionUtils
										.getField(packetPlayOutPlayerInfo, "b").get(packetPlayOutPlayerInfo);
								for (Object infoData : infoList) {
									Field c = ReflectionUtils.getField(infoData, "c");
									Object profile = ReflectionUtils.invokeMethod(infoData, "a");
									Object id = ReflectionUtils.invokeMethod(profile, "getId");
									if (c.get(infoData).equals(
											ReflectionUtils.getField(enumGameMode, "SPECTATOR").get(enumGameMode))
											&& !(id.equals(ev.getPlayer().getUniqueId()))) {
										ReflectionUtils.modifyFinalField(c, infoData,
												ReflectionUtils.getField(enumGameMode, "SURVIVAL").get(enumGameMode));
									}
								}
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				});
	}
}