package bettermounthud;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

/** Client-only mod. All behaviour lives in the HUD mixin; this class only exists so FML has an entry point. */
@Mod(value = BetterMountHud.MOD_ID, dist = Dist.CLIENT)
public final class BetterMountHud {
    public static final String MOD_ID = "bettermounthud";
}
