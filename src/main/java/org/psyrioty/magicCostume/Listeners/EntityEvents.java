package org.psyrioty.magicCostume.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.psyrioty.magicCostume.MagicCostume;
import org.psyrioty.magicCostume.Objects.ActiveCostumeEntity;

public class EntityEvents implements Listener {
    @EventHandler
    private void playerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        for(ActiveCostumeEntity activeCostumeEntity: MagicCostume.getPlugin().getActiveCostumeEntities()){
            if(activeCostumeEntity.getEntity().getUniqueId().equals(player.getUniqueId())){
                activeCostumeEntity.setEntity(player);
                return;
            }
        }

        new ActiveCostumeEntity(player);
    }
}
