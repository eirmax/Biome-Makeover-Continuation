package party.lemons.biomemakeover.mixin;

import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import party.lemons.biomemakeover.level.feature.mansion.MansionFeature;

@Mixin(Beardifier.class)
public class BeardifierMixin_Mansion {

    @Inject(
            at = @At(value = "TAIL"),
            method = "method_42694(Lnet/minecraft/world/level/ChunkPos;Lit/unimi/dsi/fastutil/objects/ObjectList;IILit/unimi/dsi/fastutil/objects/ObjectList;Lnet/minecraft/world/level/levelgen/structure/StructureStart;)V",
            remap = false,
            require = 0
    )
    private static void forStructuresInChunk(ChunkPos chunkPos, ObjectList<Beardifier.Rigid> rigidPieces, int i, int j, ObjectList objectList2, StructureStart structureStart, CallbackInfo ci)
    {
        for(StructurePiece structurePiece : structureStart.getPieces())
        {
            if(structurePiece instanceof MansionFeature.Piece mp && !mp.doesModifyGround())
                replaceRigid(rigidPieces, structurePiece);
        }
    }

    private static void replaceRigid(ObjectList<Beardifier.Rigid> rigidPieces, StructurePiece structurePiece)
    {
        BoundingBox pieceBox = structurePiece.getBoundingBox();

        for(int index = 0; index < rigidPieces.size(); index++)
        {
            Beardifier.Rigid rigid = rigidPieces.get(index);
            if(sameBox(rigid.box(), pieceBox))
                rigidPieces.set(index, new Beardifier.Rigid(rigid.box(), TerrainAdjustment.NONE, rigid.groundLevelDelta()));
        }
    }

    private static boolean sameBox(BoundingBox left, BoundingBox right)
    {
        return left.minX() == right.minX()
                && left.minY() == right.minY()
                && left.minZ() == right.minZ()
                && left.maxX() == right.maxX()
                && left.maxY() == right.maxY()
                && left.maxZ() == right.maxZ();
    }
}
