package party.lemons.biomemakeover.neoforge.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.compat.BMRecipeViewerData;
import party.lemons.biomemakeover.crafting.AltarScreen;
import party.lemons.biomemakeover.crafting.witch.menu.WitchScreen;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.init.BMItems;

import java.util.List;

@JeiPlugin
public class BMJEIPlugin implements IModPlugin {

    private static final RecipeType<BMRecipeViewerData.AltarInfo> ALTAR = RecipeType.create("biomemakeover", "altar_cursing", BMRecipeViewerData.AltarInfo.class);
    private static final RecipeType<BMRecipeViewerData.WitchQuestInfo> WITCH_REQUESTS = RecipeType.create("biomemakeover", "witch_requests", BMRecipeViewerData.WitchQuestInfo.class);
    private static final RecipeType<BMRecipeViewerData.WitchRewardInfo> WITCH_REWARDS = RecipeType.create("biomemakeover", "witch_rewards", BMRecipeViewerData.WitchRewardInfo.class);

    @Override
    public ResourceLocation getPluginUid() {
        return BiomeMakeover.ID("jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new AltarCategory(helper), new WitchQuestCategory(helper), new WitchRewardCategory(helper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(ALTAR, BMRecipeViewerData.altarDisplays());
        registration.addRecipes(WITCH_REQUESTS, BMRecipeViewerData.witchQuestDisplays());
        registration.addRecipes(WITCH_REWARDS, BMRecipeViewerData.witchRewardDisplays());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(BMBlocks.ALTAR.get(), ALTAR);
        registration.addRecipeCatalyst(BMItems.WITCH_HAT.get(), WITCH_REQUESTS, WITCH_REWARDS);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(AltarScreen.class, 98, 53, 18, 18, ALTAR);
        registration.addRecipeClickArea(WitchScreen.class, 7, 17, 102, 78, WITCH_REQUESTS, WITCH_REWARDS);
    }

    private abstract static class BaseCategory<T> implements IRecipeCategory<T> {
        private final RecipeType<T> type;
        private final Component title;
        private final IDrawable icon;
        private final IDrawable background;

        private BaseCategory(RecipeType<T> type, Component title, IDrawable icon, IGuiHelper helper) {
            this.type = type;
            this.title = title;
            this.icon = icon;
            this.background = helper.createBlankDrawable(150, 58);
        }

        @Override
        public RecipeType<T> getRecipeType() {
            return type;
        }

        @Override
        public Component getTitle() {
            return title;
        }

        @Override
        public @Nullable IDrawable getBackground() {
            return background;
        }

        @Override
        public IDrawable getIcon() {
            return icon;
        }

        protected void drawText(GuiGraphics graphics, Component component, int x, int y, int color) {
            graphics.drawString(Minecraft.getInstance().font, component, x, y, color, false);
        }
    }

    private static class AltarCategory extends BaseCategory<BMRecipeViewerData.AltarInfo> {
        private AltarCategory(IGuiHelper helper) {
            super(ALTAR, BMRecipeViewerData.displayName("altar_cursing"), helper.createDrawableItemLike(BMBlocks.ALTAR.get()), helper);
        }

        @Override
        public void setRecipe(IRecipeLayoutBuilder builder, BMRecipeViewerData.AltarInfo recipe, IFocusGroup focuses) {
            builder.addSlot(RecipeIngredientRole.INPUT, 20, 30).setStandardSlotBackground().addItemStack(recipe.inputs().get(0));
            builder.addSlot(RecipeIngredientRole.INPUT, 48, 30).setStandardSlotBackground().addItemStack(recipe.inputs().get(1));
            builder.addSlot(RecipeIngredientRole.OUTPUT, 115, 30).setOutputSlotBackground().addItemStack(recipe.output());
        }

        @Override
        public void createRecipeExtras(IRecipeExtrasBuilder builder, BMRecipeViewerData.AltarInfo recipe, IFocusGroup focuses) {
            builder.addRecipeArrow(76, 31);
        }

        @Override
        public void draw(BMRecipeViewerData.AltarInfo recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
            drawText(graphics, recipe.note(), 5, 5, 0x404040);
        }
    }

    private static class WitchQuestCategory extends BaseCategory<BMRecipeViewerData.WitchQuestInfo> {
        private WitchQuestCategory(IGuiHelper helper) {
            super(WITCH_REQUESTS, BMRecipeViewerData.displayName("witch_requests"), helper.createDrawableItemLike(BMItems.WITCH_HAT.get()), helper);
        }

        @Override
        public void setRecipe(IRecipeLayoutBuilder builder, BMRecipeViewerData.WitchQuestInfo recipe, IFocusGroup focuses) {
            builder.addSlot(RecipeIngredientRole.INPUT, 31, 31).setStandardSlotBackground().addItemStacks(recipe.requests());
            builder.addSlot(RecipeIngredientRole.OUTPUT, 104, 31).setOutputSlotBackground().addItemStack(BMItems.WITCH_HAT.get().getDefaultInstance());
        }

        @Override
        public void createRecipeExtras(IRecipeExtrasBuilder builder, BMRecipeViewerData.WitchQuestInfo recipe, IFocusGroup focuses) {
            builder.addRecipeArrow(64, 32);
        }

        @Override
        public void draw(BMRecipeViewerData.WitchQuestInfo recipe, mezz.jei.api.gui.ingredient.IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
            drawText(graphics, recipe.note(), 5, 5, 0x404040);
        }
    }

    private static class WitchRewardCategory extends BaseCategory<BMRecipeViewerData.WitchRewardInfo> {
        private WitchRewardCategory(IGuiHelper helper) {
            super(WITCH_REWARDS, BMRecipeViewerData.displayName("witch_rewards"), helper.createDrawableItemLike(BMItems.WITCH_HAT.get()), helper);
        }

        @Override
        public void setRecipe(IRecipeLayoutBuilder builder, BMRecipeViewerData.WitchRewardInfo recipe, IFocusGroup focuses) {
            builder.addSlot(RecipeIngredientRole.INPUT, 31, 32).setStandardSlotBackground().addItemStack(BMItems.WITCH_HAT.get().getDefaultInstance());
            builder.addSlot(RecipeIngredientRole.OUTPUT, 104, 32).setOutputSlotBackground().addItemStacks(recipe.rewards());
        }

        @Override
        public void createRecipeExtras(IRecipeExtrasBuilder builder, BMRecipeViewerData.WitchRewardInfo recipe, IFocusGroup focuses) {
            builder.addRecipeArrow(64, 33);
        }

        @Override
        public void draw(BMRecipeViewerData.WitchRewardInfo recipe, mezz.jei.api.gui.ingredient.IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
            drawText(graphics, recipe.note(), 5, 5, 0x404040);
        }
    }
}
