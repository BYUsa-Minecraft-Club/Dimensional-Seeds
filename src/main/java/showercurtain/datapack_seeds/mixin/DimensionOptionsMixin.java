package showercurtain.datapack_seeds.mixin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import showercurtain.datapack_seeds.DimensionOptionsAdditions;


@Mixin(DimensionOptions.class)
public class DimensionOptionsMixin implements DimensionOptionsAdditions {
	static {
		DimensionOptions.CODEC = RecordCodecBuilder.create(
				instance -> instance.group(
						DimensionType.REGISTRY_CODEC.fieldOf("type").forGetter(DimensionOptions::dimensionTypeEntry),
						ChunkGenerator.CODEC.fieldOf("generator").forGetter(DimensionOptions::chunkGenerator),
						Codec.LONG.optionalFieldOf("seed").forGetter(DimensionOptionsAdditions::datapack_seeds$alwaysEmpty)
				).apply(instance, instance.stable(DimensionOptionsAdditions::datapack_seeds$createOptions))
		);
	}

	@Nullable
	@Unique
	Long seed;

	@Override
	public long datapack_seeds$getSeed(long def) {
		return seed == null ? def : seed;
	}

	@Override
	public void datapack_seeds$setSeed(Long seed) {
		this.seed = seed;
	}
}