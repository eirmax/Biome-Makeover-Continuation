package party.lemons.biomemakeover.mixin.multipart;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import party.lemons.biomemakeover.entity.mutipart.EntityPart;
import party.lemons.biomemakeover.entity.mutipart.MultiPartEntity;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin
{
    @Shadow private ClientLevel level;

    @Inject(method = "handleAddEntity", at = @At(value = "TAIL"))
    private void onHandleMobSpawn(ClientboundAddEntityPacket packet, CallbackInfo cbi)
    {
        Entity entity = this.level.getEntity(packet.getId());
        if(entity instanceof MultiPartEntity<?> mpe)
        {
            MultiPartEntity.handleClientSpawn(packet, mpe);

            for(EntityPart<?> p : mpe.getParts())
                this.level.addEntity(p);
        }
    }
}
