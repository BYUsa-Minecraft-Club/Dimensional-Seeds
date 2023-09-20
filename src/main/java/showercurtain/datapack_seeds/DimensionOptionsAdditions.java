package showercurtain.datapack_seeds;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGenerator;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public interface DimensionOptionsAdditions {
    ConcurrentHashMap<Identifier, Long> seeds = new ConcurrentHashMap<>();

    default long datapack_seeds$getSeed(long def) {
        return def;
    }

    default void datapack_seeds$setSeed(Long seed) {}

    default Optional<Long> datapack_seeds$alwaysEmpty() { return Optional.empty(); }

    static DimensionOptions datapack_seeds$createOptions(RegistryEntry<DimensionType> type, ChunkGenerator generator, Optional<Long> seed) {
        DimensionOptions out = new DimensionOptions(type, generator);
        seed.ifPresent(out::datapack_seeds$setSeed);
        return out;
    }
}
