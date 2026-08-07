package party.lemons.biomemakeover.neoforge.compat;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.forge.REIPluginClient;
import net.minecraft.network.chat.Component;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.compat.BMRecipeViewerData;
import party.lemons.biomemakeover.crafting.AltarScreen;
import party.lemons.biomemakeover.crafting.witch.menu.WitchScreen;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.init.BMItems;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@REIPluginClient
public class BMREIPlugin implements REIClientPlugin {
    private static final CategoryIdentifier<AltarDisplay> ALTAR = CategoryIdentifier.of(BiomeMakeover.ID("altar_cursing"));
    private static final CategoryIdentifier<WitchQuestDisplay> WITCH_REQUESTS = CategoryIdentifier.of(BiomeMakeover.ID("witch_requests"));
    private static final CategoryIdentifier<WitchRewardDisplay> WITCH_REWARDS = CategoryIdentifier.of(BiomeMakeover.ID("witch_rewards"));

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new AltarCategory());
        registry.add(new WitchQuestCategory());
        registry.add(new WitchRewardCategory());
        registry.addWorkstations(ALTAR, EntryStacks.of(BMBlocks.ALTAR.get()));
        registry.addWorkstations(WITCH_REQUESTS, EntryStacks.of(BMItems.WITCH_HAT.get()));
        registry.addWorkstations(WITCH_REWARDS, EntryStacks.of(BMItems.WITCH_HAT.get()));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        BMRecipeViewerData.altarDisplays().forEach(info -> registry.add(new AltarDisplay(info)));
        BMRecipeViewerData.witchQuestDisplays().forEach(info -> registry.add(new WitchQuestDisplay(info)));
        BMRecipeViewerData.witchRewardDisplays().forEach(info -> registry.add(new WitchRewardDisplay(info)));
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerContainerClickArea(new Rectangle(98, 53, 18, 18), AltarScreen.class, ALTAR);
        registry.registerContainerClickArea(new Rectangle(7, 17, 102, 78), WitchScreen.class, WITCH_REQUESTS, WITCH_REWARDS);
    }

    private static class AltarDisplay extends BasicDisplay {
        private final BMRecipeViewerData.AltarInfo info;

        private AltarDisplay(BMRecipeViewerData.AltarInfo info) {
            super(List.of(EntryIngredients.of(info.inputs().get(0)), EntryIngredients.of(info.inputs().get(1))),
                    List.of(EntryIngredients.of(info.output())),
                    Optional.of(info.id()));
            this.info = info;
        }

        @Override
        public CategoryIdentifier<?> getCategoryIdentifier() {
            return ALTAR;
        }
    }

    private static class WitchQuestDisplay extends BasicDisplay {
        private final BMRecipeViewerData.WitchQuestInfo info;

        private WitchQuestDisplay(BMRecipeViewerData.WitchQuestInfo info) {
            super(List.of(EntryIngredients.ofItemStacks(info.requests())),
                    List.of(EntryIngredients.of(BMItems.WITCH_HAT.get())),
                    Optional.of(info.id()));
            this.info = info;
        }

        @Override
        public CategoryIdentifier<?> getCategoryIdentifier() {
            return WITCH_REQUESTS;
        }
    }

    private static class WitchRewardDisplay extends BasicDisplay {
        private final BMRecipeViewerData.WitchRewardInfo info;

        private WitchRewardDisplay(BMRecipeViewerData.WitchRewardInfo info) {
            super(List.of(EntryIngredients.of(BMItems.WITCH_HAT.get())),
                    List.of(EntryIngredients.ofItemStacks(info.rewards())),
                    Optional.of(info.id()));
            this.info = info;
        }

        @Override
        public CategoryIdentifier<?> getCategoryIdentifier() {
            return WITCH_REWARDS;
        }
    }

    private abstract static class BaseCategory<T extends BasicDisplay> implements DisplayCategory<T> {
        protected List<Widget> base(Rectangle bounds, Component note) {
            List<Widget> widgets = new ArrayList<>();
            widgets.add(Widgets.createRecipeBase(bounds));
            widgets.add(Widgets.createLabel(new Point(bounds.x + 5, bounds.y + 5), note).leftAligned().color(0x404040).noShadow());
            return widgets;
        }

        protected void slot(List<Widget> widgets, EntryIngredient ingredient, int x, int y, boolean output) {
            var slot = Widgets.createSlot(new Point(x, y)).entries(ingredient);
            widgets.add(output ? slot.markOutput() : slot.markInput());
        }

        @Override
        public int getDisplayWidth(T display) {
            return 150;
        }

        @Override
        public int getDisplayHeight() {
            return 58;
        }
    }

    private static class AltarCategory extends BaseCategory<AltarDisplay> {
        @Override
        public CategoryIdentifier<? extends AltarDisplay> getCategoryIdentifier() {
            return ALTAR;
        }

        @Override
        public Component getTitle() {
            return BMRecipeViewerData.displayName("altar_cursing");
        }

        @Override
        public Renderer getIcon() {
            return EntryStacks.of(BMBlocks.ALTAR.get());
        }

        @Override
        public List<Widget> setupDisplay(AltarDisplay display, Rectangle bounds) {
            List<Widget> widgets = base(bounds, display.info.note());
            int y = bounds.y + 30;
            slot(widgets, display.getInputEntries().get(0), bounds.x + 20, y, false);
            slot(widgets, display.getInputEntries().get(1), bounds.x + 48, y, false);
            widgets.add(Widgets.createArrow(new Point(bounds.x + 74, y)));
            slot(widgets, display.getOutputEntries().get(0), bounds.x + 115, y, true);
            return widgets;
        }
    }

    private static class WitchQuestCategory extends BaseCategory<WitchQuestDisplay> {
        @Override
        public CategoryIdentifier<? extends WitchQuestDisplay> getCategoryIdentifier() {
            return WITCH_REQUESTS;
        }

        @Override
        public Component getTitle() {
            return BMRecipeViewerData.displayName("witch_requests");
        }

        @Override
        public Renderer getIcon() {
            return EntryStacks.of(BMItems.WITCH_HAT.get());
        }

        @Override
        public List<Widget> setupDisplay(WitchQuestDisplay display, Rectangle bounds) {
            List<Widget> widgets = base(bounds, display.info.note());
            int y = bounds.y + 31;
            slot(widgets, display.getInputEntries().get(0), bounds.x + 31, y, false);
            widgets.add(Widgets.createArrow(new Point(bounds.x + 61, y)));
            slot(widgets, display.getOutputEntries().get(0), bounds.x + 104, y, true);
            return widgets;
        }
    }

    private static class WitchRewardCategory extends BaseCategory<WitchRewardDisplay> {
        @Override
        public CategoryIdentifier<? extends WitchRewardDisplay> getCategoryIdentifier() {
            return WITCH_REWARDS;
        }

        @Override
        public Component getTitle() {
            return BMRecipeViewerData.displayName("witch_rewards");
        }

        @Override
        public Renderer getIcon() {
            return EntryStacks.of(BMItems.WITCH_HAT.get());
        }

        @Override
        public List<Widget> setupDisplay(WitchRewardDisplay display, Rectangle bounds) {
            List<Widget> widgets = base(bounds, display.info.note());
            int y = bounds.y + 32;
            slot(widgets, display.getInputEntries().get(0), bounds.x + 31, y, false);
            widgets.add(Widgets.createArrow(new Point(bounds.x + 61, y)));
            slot(widgets, display.getOutputEntries().get(0), bounds.x + 104, y, true);
            return widgets;
        }
    }
}
