package com.example.voicechair;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

/**
 * Buzzer-Knopf.
 * - Rechtsklick in die Luft: alle Stuehle im Umkreis drehen sich um (Lied zu Ende)
 * - Shift + Rechtsklick: alle Stuehle zurueck auf rot
 * - Rechtsklick auf einen Stuhl: nur dieser Stuhl dreht sich
 */
public class BuzzerItem extends Item {
	public BuzzerItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		if (level instanceof ServerLevel serverLevel) {
			boolean reset = player.isShiftKeyDown();
			int changed = ChairNetwork.turnAll(serverLevel, player.blockPosition(), !reset);
			serverLevel.playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP,
				SoundSource.PLAYERS, 1.0f, 0.5f);
			player.displayClientMessage(Component.literal(reset
				? changed + " Stuehle zurueckgesetzt (rot)"
				: changed + " Stuehle umgedreht"), true);
		}
		return InteractionResult.SUCCESS;
	}
}
