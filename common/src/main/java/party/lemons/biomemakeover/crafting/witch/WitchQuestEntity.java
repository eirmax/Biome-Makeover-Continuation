package party.lemons.biomemakeover.crafting.witch;

import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import party.lemons.biomemakeover.crafting.witch.menu.WitchMenu;

public interface WitchQuestEntity
{
    void setCurrentCustomer(Player customer);

    boolean hasCustomer();

    Player getCurrentCustomer();

    WitchQuestList getQuests();

    void setQuestsFromServer(WitchQuestList quests);

    void completeQuest(WitchQuest quest);

    SoundEvent getYesSound();

    boolean canInteract(Player playerEntity);

    Level getWitchLevel();

    default void sendQuests(Player player, Component text)
    {
        WitchQuestList quests = this.getQuests();
        MenuRegistry.openExtendedMenu((ServerPlayer) player,
                new SimpleMenuProvider((ix, playerInventory, playerEntityx)->new WitchMenu(ix, playerInventory, this), text),
                quests::toPacket
        );

        if(!quests.isEmpty())
            WitchQuestHandler.sendQuests(player, player.containerMenu.containerId, quests);
    }
}
