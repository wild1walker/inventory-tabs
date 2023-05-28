package com.kqp.inventorytabs.mixin;

import com.kqp.inventorytabs.init.InventoryTabsClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.client.gui.screen.option.ControlsListWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * The 'Tab' keybinding conflicts with the multiplayer player list keybind, but since you can only see the player list when outside the inventory
 * anyways, the conflict can be soft and not hard.
 */
@Mixin(ControlsListWidget.KeyBindingEntry.class)
public class ControlsListWidget$KeyBindingEntryMixin_SoftConflict {
	@Shadow @Final private KeyBinding binding;
	
	@ModifyArg(method = "render", index = 5, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawableHelper;fill(Lnet/minecraft/client/util/math/MatrixStack;IIIII)V"))
	public int fill(MatrixStack matrices, int x1, int y1, int x2, int y2, int color) {
		if(this.binding == InventoryTabsClient.NEXT_TAB_KEY_BIND) {
			return Formatting.GOLD.getColorValue() | 0xFF000000;
		}
		return color;
	}

	@Redirect(method="update", at=@At(value="INVOKE", target="Lnet/minecraft/text/MutableText;formatted(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/MutableText;"))
	public MutableText formatText(MutableText instance, Formatting formatting) {
		if(formatting == Formatting.RED && this.binding == InventoryTabsClient.NEXT_TAB_KEY_BIND) {
			return instance.formatted(Formatting.GOLD);
		}
		return instance.formatted(formatting);
	}
}
