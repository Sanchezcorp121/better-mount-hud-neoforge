/*
 * Unofficial NeoForge port of Better Mount HUD by Lortseam (https://github.com/Lortseam/better-mount-hud), GPL-3.0.
 * Same behaviour as the Fabric original, adapted to NeoForge's layered HUD.
 */
package bettermounthud.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
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
    @Redirect(method = "extractFoodLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;getVehicleMaxHearts(Lnet/minecraft/world/entity/LivingEntity;)I"))
    private int bettermounthud$alwaysRenderFood(Gui gui, LivingEntity vehicle) {
        return 0;
    }

    /**
     * Vanilla swaps the experience bar for the jump bar for the whole ride. Only report a jumpable vehicle
     * while the jump key is held or a jump is charging, so the experience bar stays the rest of the time.
     */
    @Redirect(method = "nextContextualInfoState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;jumpableVehicle()Lnet/minecraft/world/entity/PlayerRideableJumping;"))
    private PlayerRideableJumping bettermounthud$switchBar(LocalPlayer player) {
        PlayerRideableJumping vehicle = player.jumpableVehicle();
        if (vehicle == null) return null;
        return bettermounthud$wantsJumpBar(player) ? vehicle : null;
    }

    /** Hide the level number while the jump bar is up, as vanilla does; show it whenever the experience bar is. */
    @Redirect(method = "extractExperienceLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;hasExperience()Z"))
    private boolean bettermounthud$renderExperienceLevel(MultiPlayerGameMode gameMode) {
        LocalPlayer player = minecraft.player;
        return gameMode.hasExperience() && (player == null || player.jumpableVehicle() == null || !bettermounthud$wantsJumpBar(player));
    }

    private boolean bettermounthud$wantsJumpBar(LocalPlayer player) {
        return !minecraft.gameMode.hasExperience() || minecraft.options.keyJump.isDown() || player.getJumpRidingScale() > 0;
    }
}
