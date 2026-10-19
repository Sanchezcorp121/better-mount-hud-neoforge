/*
 * Unofficial NeoForge port of Better Mount HUD by Lortseam (https://github.com/Lortseam/better-mount-hud), GPL-3.0.
 * Same behaviour as the Fabric original, adapted to NeoForge's layered HUD.
 */
package bettermounthud.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PlayerRideableJumping;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Gui.class)
public abstract class GuiMixin {
    @Shadow @Final private Minecraft minecraft;

    /**
     * Vanilla hides the hunger bar while riding a mount that has hearts. Pretend the mount has none so the bar
     * is always drawn. NeoForge stacks the right-hand bars through {@code rightHeight}, so the mount health and
     * the air bubbles move up by a row on their own once the hunger bar is drawn.
     */
    @Redirect(method = "renderFoodLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;getVehicleMaxHearts(Lnet/minecraft/world/entity/LivingEntity;)I"))
    private int bettermounthud$alwaysRenderFood(Gui gui, LivingEntity vehicle) {
        return 0;
    }

    /**
     * Vanilla replaces the experience bar with the jump bar for the whole ride. Only switch to the jump bar
     * while the jump key is held or a jump is charging; otherwise keep the experience bar and level visible.
     * The three methods below are every place NeoForge's HUD asks which bar to draw.
     */
    @Redirect(method = {"maybeRenderJumpMeter", "maybeRenderExperienceBar", "isExperienceBarVisible"},
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;jumpableVehicle()Lnet/minecraft/world/entity/PlayerRideableJumping;"))
    private PlayerRideableJumping bettermounthud$switchBar(LocalPlayer player) {
        PlayerRideableJumping vehicle = player.jumpableVehicle();
        if (vehicle == null) return null;
        if (!minecraft.gameMode.hasExperience() || minecraft.options.keyJump.isDown() || player.getJumpRidingScale() > 0) {
            return vehicle;
        }
        return null;
    }
}
