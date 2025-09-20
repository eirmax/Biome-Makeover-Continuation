package party.lemons.biomemakeover.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WaterlilyBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.taniwha.block.TBlockExtension;
import party.lemons.taniwha.block.modifier.BlockModifier;
import party.lemons.taniwha.block.modifier.BlockWithModifiers;
import party.lemons.taniwha.registry.ModifierContainer;

public class SmallLilyPadBlock extends WaterlilyBlock implements BlockWithModifiers<SmallLilyPadBlock>, TBlockExtension
{
    public static final IntegerProperty PADS = IntegerProperty.create("pads", 0, 3);

    public SmallLilyPadBlock(Properties properties)
    {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(PADS, 0));
    }


    @Override
    protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        ItemStack stack = player.getItemInHand(interactionHand);
        BlockPos pos = blockHitResult.getBlockPos();

        if(level.mayInteract(player, pos) && blockState.getValue(PADS) < 3 && !stack.isEmpty() && stack.getItem() == BMBlocks.SMALL_LILY_PAD.get().asItem())
        {
            level.setBlock(pos, blockState.setValue(PADS, blockState.getValue(PADS) + 1), 3);
            if(!player.isCreative()) stack.shrink(1);

            SoundType blockSoundGroup = blockState.getSoundType();
            level.playSound(player, pos, blockSoundGroup.getPlaceSound(), SoundSource.BLOCKS, (blockSoundGroup.getVolume() + 1.0F) / 2.0F, blockSoundGroup.getPitch() * 0.8F);

            return ItemInteractionResult.SUCCESS;
        }
        return super.useItemOn(itemStack, blockState, level, blockPos, player, interactionHand, blockHitResult);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(PADS);
    }

    private ModifierContainer<Block> modifierContainer;

    @Override
    public @Nullable ModifierContainer<Block> getModifierContainer()
    {
        return modifierContainer;
    }

    @Override
    public SmallLilyPadBlock modifiers(BlockModifier... modifiers)
    {
        modifierContainer = new ModifierContainer<>(this, modifiers);
        return this;
    }

    @Override
    public PathType getNodePathType()
    {
        return PathType.DANGER_OTHER;
    }
}
