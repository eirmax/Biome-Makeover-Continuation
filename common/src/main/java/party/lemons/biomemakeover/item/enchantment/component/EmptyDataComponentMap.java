package party.lemons.biomemakeover.item.enchantment.component;


import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;

public class EmptyDataComponentMap implements DataComponentMap {
    public static final DataComponentMap EMPTY = new EmptyDataComponentMap();

    private EmptyDataComponentMap() {}

    @Override
    public <T> Optional<T> get(DataComponentType<T> type) {
        return Optional.empty();
    }

    @Override
    public @Nullable <T> T get(DataComponentType<? extends T> dataComponentType) {
        return null;
    }

    @Override
    public Set<DataComponentType<?>> keySet() {
        return Set.of();
    }
}