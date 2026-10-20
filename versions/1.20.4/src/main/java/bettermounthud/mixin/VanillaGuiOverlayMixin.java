package bettermounthud.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.neoforged.neoforge.client.gui.overlay.VanillaGuiOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(VanillaGuiOverlay.class)
public abstract class VanillaGuiOverlayMixin {
    // keep the hunger bar visible while riding
    @Redirect(method = "*", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;showVehicleHealth()Z"))
    private static boolean bettermounthud$alwaysRenderFood(Entity vehicle) {
        return false;
    }

    // only switch to the jump bar while actually jumping
    @Redirect(method = "*", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;jumpableVehicle()Lnet/minecraft/world/entity/PlayerRideableJumping;"))
    private static PlayerRideableJumping bettermounthud$switchBar(LocalPlayer player) {
        PlayerRideableJumping vehicle = player.jumpableVehicle();
        if (vehicle == null) return null;
        Minecraft minecraft = Minecraft.getInstance();
        if (!minecraft.gameMode.hasExperience() || minecraft.options.keyJump.isDown() || player.getJumpRidingScale() > 0) {
            return vehicle;
        }
        return null;
    }
}
