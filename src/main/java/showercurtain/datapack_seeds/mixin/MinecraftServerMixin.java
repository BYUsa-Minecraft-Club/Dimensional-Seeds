package showercurtain.datapack_seeds.mixin;

import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.dimension.DimensionOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import showercurtain.datapack_seeds.DimensionOptionsAdditions;

import java.util.Map;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Shadow public abstract SaveProperties getSaveProperties();

    @ModifyVariable(method = "createWorlds", at=@At(value="STORE"))
    private Map.Entry<RegistryKey<DimensionOptions>, DimensionOptions> getEntry(Map.Entry<RegistryKey<DimensionOptions>, DimensionOptions> entry) {
        DimensionOptionsAdditions.seeds.put(entry.getKey().getValue(), entry.getValue().datapack_seeds$getSeed(getSaveProperties().getGeneratorOptions().getSeed()));
        return entry;
    }
}
