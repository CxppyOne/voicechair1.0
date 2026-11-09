package com.example.voicechair;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

public class VoiceChairMod implements ModInitializer {
	public static final String MOD_ID = "voicechair";

	public static Block VOICE_CHAIR;
	public static Block NAME_TILE;
	public static Item BUZZER;

	private static Identifier id(String name) {
		return Identifier.fromNamespaceAndPath(MOD_ID, name);
	}

	private static <T extends Block> T registerBlock(String name, Function<BlockBehaviour.Properties, T> factory,
													 BlockBehaviour.Properties props) {
		ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, id(name));
		T block = Registry.register(BuiltInRegistries.BLOCK, blockKey, factory.apply(props.setId(blockKey)));
		ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, id(name));
		Registry.register(BuiltInRegistries.ITEM, itemKey,
			new BlockItem(block, new Item.Properties().setId(itemKey).useBlockDescriptionPrefix()));
		return block;
	}

	@Override
	public void onInitialize() {
		VOICE_CHAIR = registerBlock("voice_chair", VoiceChairBlock::new,
			BlockBehaviour.Properties.of().strength(2.0f).sound(SoundType.WOOD).noOcclusion());

		NAME_TILE = registerBlock("name_tile", NameTileBlock::new,
			BlockBehaviour.Properties.of().strength(1.0f).sound(SoundType.STONE).noOcclusion());

		ResourceKey<Item> buzzerKey = ResourceKey.create(Registries.ITEM, id("buzzer"));
		BUZZER = Registry.register(BuiltInRegistries.ITEM, buzzerKey,
			new BuzzerItem(new Item.Properties().setId(buzzerKey).stacksTo(1)));

		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(entries -> {
			entries.accept(VOICE_CHAIR.asItem());
			entries.accept(NAME_TILE.asItem());
			entries.accept(BUZZER);
		});

		SeatManager.init();
	}
}
