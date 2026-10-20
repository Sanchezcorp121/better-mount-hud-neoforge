package bettermounthud.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Hud;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PlayerRideableJumping;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Hud.class)
public abstract class HudMixin {
    @Shadow @Final private Minecraft minecraft;

    // keep the hunger bar visible while riding
    @Redirect(method = "extractFoodLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Hud;getVehicleMaxHearts(Lnet/minecraft/world/entity/LivingEntity;)I"))
    private int bettermounthud$alwaysRenderFood(Hud hud, LivingEntity vehicle) {
        return 0;
    }

    // only switch to the jump bar while actually jumping
    @Redirect(method = "nextContextualInfoState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;jumpableVehicle()Lnet/minecraft/world/entity/PlayerRideableJumping;"))
    private PlayerRideableJumping bettermounthud$switchBar(LocalPlayer player) {
        PlayerRideableJumping vehicle = player.jumpableVehicle();
        if (vehicle == null) return null;
        return bettermounthud$wantsJumpBar(player) ? vehicle : null;
    }

    // hide the level only while the jump bar is up
    @Redirect(method = "extractExperienceLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;hasExperience()Z"))
    private boolean bettermounthud$renderExperienceLevel(MultiPlayerGameMode gameMode) {
        LocalPlayer player = minecraft.player;
        return gameMode.hasExperience() && (player == null || player.jumpableVehicle() == null || !bettermounthud$wantsJumpBar(player));
    }

    private boolean bettermounthud$wantsJumpBar(LocalPlayer player) {
        return !minecraft.gameMode.hasExperience() || minecraft.options.keyJump.isDown() || player.getJumpRidingScale() > 0;
    }
}
