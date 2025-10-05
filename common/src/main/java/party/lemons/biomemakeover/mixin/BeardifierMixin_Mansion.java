package party.lemons.biomemakeover.mixin;

import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import party.lemons.biomemakeover.level.feature.mansion.MansionFeature;

import java.util.Iterator;

@Mixin(Beardifier.class)
public class BeardifierMixin_Mansion {

    @Inject(at = @At(value = "TAIL"), method = "method_42694(Lnet/minecraft/world/level/ChunkPos;Lit/unimi/dsi/fastutil/objects/ObjectList;IILit/unimi/dsi/fastutil/objects/ObjectList;Lnet/minecraft/world/level/levelgen/structure/StructureStart;)V", locals = LocalCapture.CAPTURE_FAILSOFT)
    private static void forStructuresInChunk(ChunkPos chunkPos, ObjectList objectList, int i, int j, ObjectList objectList2, StructureStart structureStart, CallbackInfo ci, TerrainAdjustment terrainAdjustment, Iterator var7, StructurePiece structurePiece)
    {

        if(structurePiece instanceof MansionFeature.Piece mp)
        {
            if(!mp.doesModifyGround()) {
                objectList.remove(objectList.size() - 1);
                objectList.add(new Beardifier.Rigid(structurePiece.getBoundingBox(), TerrainAdjustment.NONE, 0));
            }
        }
    }
}
