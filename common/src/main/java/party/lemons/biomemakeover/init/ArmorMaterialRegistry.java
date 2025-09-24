package party.lemons.biomemakeover.init;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.Constants;

import java.util.EnumMap;
import java.util.List;

public class ArmorMaterialRegistry {
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIAL =
            DeferredRegister.create(Constants.MOD_ID, Registries.ARMOR_MATERIAL);

    public static final ArmorMaterial CLADDED_MATERIAL = new ArmorMaterial(
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 2);
                map.put(ArmorItem.Type.LEGGINGS, 5);
                map.put(ArmorItem.Type.CHESTPLATE, 6);
                map.put(ArmorItem.Type.HELMET, 2);
                map.put(ArmorItem.Type.BODY, 5);
            }),
            15,
            SoundEvents.ARMOR_EQUIP_LEATHER,
            () -> ArmorMaterials.LEATHER.value().repairIngredient().get(),
            List.of(new ArmorMaterial.Layer(BiomeMakeover.ID("cladded"))),
            0.0F,
            0.07F
    );

    public static final ArmorMaterial HAT_ARMOR_MATERIAL = new ArmorMaterial(
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.HELMET, 2);
            }),
            15,
            SoundEvents.ARMOR_EQUIP_LEATHER,
            () -> ArmorMaterials.LEATHER.value().repairIngredient().get(),
            List.of(new ArmorMaterial.Layer(BiomeMakeover.ID("hat"))),
            0.0F,
            0.0F
    );

    public static final RegistrySupplier<ArmorMaterial> CLADDED =
            ARMOR_MATERIAL.register("cladded", () -> CLADDED_MATERIAL);

    public static final RegistrySupplier<ArmorMaterial> HAT =
            ARMOR_MATERIAL.register("hat", () -> HAT_ARMOR_MATERIAL);

    public static void init() {
        ARMOR_MATERIAL.register();
    }
}
