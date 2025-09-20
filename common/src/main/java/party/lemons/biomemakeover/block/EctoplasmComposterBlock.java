package party.lemons.biomemakeover.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.init.BMAdvancements;

public class EctoplasmComposterBlock extends ComposterBlock
{
    public EctoplasmComposterBlock(Properties properties) {
        super(properties);
    }

    @Override
    public WorldlyContainer getContainer(BlockState blockState, LevelAccessor levelAccessor, BlockPos blockPos) {
        int currentLevel = blockState.getValue(LEVEL);
        if(currentLevel == 8)
            return new FullComposterContainer(levelAccessor, blockPos, new ItemStack(Items.SOUL_SOIL));
        else
            return super.getContainer(blockState, levelAccessor, blockPos);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        int currentLevel = blockState.getValue(LEVEL);
        ItemStack itemStack1 = player.getItemInHand(interactionHand);

        if(currentLevel < 8 && COMPOSTABLES.containsKey(itemStack1.getItem()))
        {
            if(currentLevel < 7 && !level.isClientSide())
            {
                BlockState blockState1 = addItem(blockState, level, blockPos, itemStack1);
                level.levelEvent(1500, blockPos, blockState1 != blockState ? 1 : 0);
                if(!player.isCreative())
                {
                    itemStack.shrink(1);
                }
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide());
        }else if(currentLevel == 8)
        {
            emptyFullComposter(level, blockPos, new ItemStack(Blocks.SOUL_SOIL));
            if(!level.isClientSide())
                BMAdvancements.ECTOPLASM_COMPOST.trigger((ServerPlayer) player);

            return ItemInteractionResult.sidedSuccess(level.isClientSide());
        }else
        {
            return ItemInteractionResult.FAIL;
        }
    }

    public static void emptyFullComposter(Level level, BlockPos pos, ItemStack stack)
    {
        if(!level.isClientSide())
        {
            float offset = 0.7F;
            double offsetX = (double) (level.random.nextFloat() * offset) + 0.15D;
            double offsetY = (double) (level.random.nextFloat() * offset) + 0.06D + 0.6D;
            double offsetZ = (double) (level.random.nextFloat() * offset) + 0.15D;
            ItemEntity itemEntity = new ItemEntity(level, (double) pos.getX() + offsetX, (double) pos.getY() + offsetY, (double) pos.getZ() + offsetZ, stack);
            itemEntity.setDefaultPickUpDelay();
            level.addFreshEntity(itemEntity);
        }

        BlockState originalState = Blocks.COMPOSTER.defaultBlockState();
        level.setBlock(pos, originalState, 3);
        level.playSound(null, pos, SoundEvents.COMPOSTER_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    private static BlockState addItem(BlockState state, Level world, BlockPos pos, ItemStack item)
    {
        int currentLevel = state.getValue(LEVEL);
        float increaseChance = COMPOSTABLES.getFloat(item.getItem());
        if((currentLevel != 0 || increaseChance <= 0.0F) && world.getRandom().nextDouble() >= (double) increaseChance)
        {
            return state;
        }else
        {
            int nextLevel = currentLevel + 1;
            BlockState blockState = state.setValue(LEVEL, nextLevel);
            world.setBlock(pos, blockState, 3);
            if(nextLevel == 7) world.scheduleTick(pos, state.getBlock(), 20);

            return blockState;
        }
    }

    static class FullComposterContainer extends SimpleContainer implements WorldlyContainer
    {
        private boolean dirty = false;
        private LevelAccessor level;
        private BlockPos pos;
        private final Item item;

        public FullComposterContainer(LevelAccessor level, BlockPos pos, ItemStack stack)
        {
            super(stack);

            this.level = level;
            this.pos = pos;
            this.item = stack.getItem();
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }

        @Override
        public int[] getSlotsForFace(Direction direction) {
            return direction == Direction.DOWN ? new int[]{0} : new int[0];
        }

        @Override
        public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction) {
            return false;
        }

        @Override
        public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction dir) {
            return !dirty && dir == Direction.DOWN && itemStack.getItem() == item;
        }

        @Override
        public void setChanged() {
            dirty = true;

            BlockState blockState = Blocks.COMPOSTER.defaultBlockState();
            level.setBlock(pos, blockState, 3);
            super.setChanged();
        }
    }

}
