package showercurtain.datapack_seeds;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.DimensionArgumentType;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import static net.minecraft.server.command.CommandManager.*;

public class Commands implements ModInitializer {
    private static final CommandSyntaxException FIGHT_STARTED = new SimpleCommandExceptionType(Text.literal("There's already an ender dragon in this world")).create();

    @Override
    public void onInitialize() {
       CommandRegistrationCallback.EVENT.register(this::register);
    }

    private void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(
                literal("startDragonFight").requires(src -> src.hasPermissionLevel(4)).executes(this::startDragonFight)
                        .then(argument("pos", BlockPosArgumentType.blockPos()).executes(this::startDragonFightHere))
                        .then(argument("dimension", DimensionArgumentType.dimension()).executes(this::startDragonFightDimension)
                                .then(argument("pos", BlockPosArgumentType.blockPos()).executes(this::startDragonFightHereDimension))));
    }

    private int startDragonFight(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        ServerWorld world = ctx.getSource().getWorld();
        if (world.getEnderDragonFight() != null) throw FIGHT_STARTED;
        ctx.getSource().getWorld().setEnderDragonFight(new EnderDragonFight(world, world.getSeed(), EnderDragonFight.Data.DEFAULT));
        return 1;
    }

    private int startDragonFightHere(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        ServerWorld world = ctx.getSource().getWorld();
        if (world.getEnderDragonFight() != null) throw FIGHT_STARTED;
        BlockPos origin = BlockPosArgumentType.getValidBlockPos(ctx, "pos");
        ctx.getSource().getWorld().setEnderDragonFight(new EnderDragonFight(world, world.getSeed(), EnderDragonFight.Data.DEFAULT, origin));
        return 1;
    }

    private int startDragonFightDimension(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        ServerWorld world = DimensionArgumentType.getDimensionArgument(ctx, "dimension");
        if (world.getEnderDragonFight() != null) throw FIGHT_STARTED;
        ctx.getSource().getWorld().setEnderDragonFight(new EnderDragonFight(world, world.getSeed(), EnderDragonFight.Data.DEFAULT));
        return 1;
    }

    private int startDragonFightHereDimension(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        ServerWorld world = DimensionArgumentType.getDimensionArgument(ctx, "dimension");
        if (world.getEnderDragonFight() != null) throw FIGHT_STARTED;
        BlockPos origin = BlockPosArgumentType.getValidBlockPos(ctx, "pos");
        ctx.getSource().getWorld().setEnderDragonFight(new EnderDragonFight(world, world.getSeed(), EnderDragonFight.Data.DEFAULT, origin));
        return 1;
    }
}
