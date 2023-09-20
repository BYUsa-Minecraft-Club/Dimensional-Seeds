package showercurtain.datapack_seeds.mixin;

import net.minecraft.network.packet.Packet;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.RandomSequencesState;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.level.ServerWorldProperties;
import net.minecraft.world.level.storage.LevelStorage;
import net.minecraft.world.spawner.Spawner;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import showercurtain.datapack_seeds.DimensionOptionsAdditions;

import java.util.List;
import java.util.concurrent.Executor;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {
    @Nullable
    @Unique
    Long seed;

    // Thanks, Fantasy
    @Redirect(
            method = "tickWeather",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/PlayerManager;sendToAll(Lnet/minecraft/network/packet/Packet;)V"
            )

    )
    private void dontSendRainPacketsToAllWorlds(PlayerManager instance, Packet<?> packet) {
        // Vanilla sends rain packets to all players when rain starts in a world,
        // even if they are not in it, meaning that if it is possible to rain in the world they are in
        // the rain effect will remain until the player changes dimension or reconnects.
        instance.sendToDimension(packet, ((ServerWorld)(Object) this).getRegistryKey());
    }

    @Inject(method = "<init>", at = @At(value = "TAIL", shift = At.Shift.AFTER))
    private void setSeed(MinecraftServer server, Executor workerExecutor, LevelStorage.Session session, ServerWorldProperties properties, RegistryKey<World> worldKey, DimensionOptions dimensionOptions, WorldGenerationProgressListener worldGenerationProgressListener, boolean debugWorld, long seed, List<Spawner> spawners, boolean shouldTickTime, RandomSequencesState randomSequencesState, CallbackInfo ci) {
        this.seed = seed;
        System.out.println("Seed: " + this.seed);
    }

    @Inject(method = "getSeed", at = @At("HEAD"), cancellable = true)
    public void getSeed(CallbackInfoReturnable<Long> cir) {
        if (seed != null) {
            cir.setReturnValue(seed);
            return;
        }
        Identifier me = ((ServerWorld)(Object) this).getRegistryKey().getValue();
        if (DimensionOptionsAdditions.seeds.containsKey(me)) {
            seed = DimensionOptionsAdditions.seeds.remove(me);
            cir.setReturnValue(seed);
        }
    }
}
