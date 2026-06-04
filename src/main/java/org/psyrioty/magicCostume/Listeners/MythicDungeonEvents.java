package org.psyrioty.magicCostume.Listeners;

import io.papermc.paper.event.player.PlayerClientLoadedWorldEvent;
import net.playavalon.mythicdungeons.api.events.dungeon.DungeonEvent;
import net.playavalon.mythicdungeons.api.events.dungeon.PlayerLeaveDungeonEvent;
import net.playavalon.mythicdungeons.api.events.dungeon.PlayerStartDungeonEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.psyrioty.magicCostume.Database.DatabaseManager;
import org.psyrioty.magicCostume.Database.Requests;
import org.psyrioty.magicCostume.MagicCostume;
import org.psyrioty.magicCostume.Objects.ActiveCostume;
import org.psyrioty.magicCostume.Objects.ActiveCostumeEntity;
import org.psyrioty.magicCostume.Objects.ActiveSlot;
import org.psyrioty.magicCostume.Objects.Costume;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class MythicDungeonEvents implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    private void PlayerStartDungeonEvent(PlayerStartDungeonEvent event){
        ActiveCostumeEntity activeCostumeEntity = MagicCostume.getPlugin().findActiveCostumeEntityForEntity(event.getPlayer());
        if(activeCostumeEntity == null){
            return;
        }

        for(ActiveSlot activeSlot: activeCostumeEntity.getActiveSlotList()){
            ActiveCostume activeCostume = activeSlot.getActiveCostume();
            if(activeCostume == null){
                continue;
            }
            activeCostume.remove();
        }
    }

    @EventHandler
    private void PlayerLeaveDungeonEvent(PlayerLeaveDungeonEvent event){
        Player player = event.getPlayer();
        ActiveCostumeEntity activeCostumeEntity = MagicCostume.getPlugin().findActiveCostumeEntityForEntity(player);
        if(activeCostumeEntity == null){
            return;
        }
        Bukkit.getScheduler().runTaskLater(MagicCostume.getPlugin(), () -> {
            Bukkit.getScheduler().runTaskAsynchronously(MagicCostume.getPlugin(), () -> {
                Connection connection = DatabaseManager.getConnection();
                try (PreparedStatement ps = Requests.selectCostumePartsByEntityUUID(connection, player.getUniqueId().toString());
                     ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String name = rs.getString("name");
                        double scale = rs.getDouble("scale");
                        int brightness = rs.getInt("brightness");
                        double offsetX = rs.getDouble("offsetX");
                        double offsetY = rs.getDouble("offsetY");
                        double offsetZ = rs.getDouble("offsetZ");
                        int slotId = rs.getInt("slot_id");


                        boolean isHead = false;
                        Costume cost = null;
                        for(Costume costume: MagicCostume.getPlugin().getCostumes()){
                            if(costume.getId().equals(name)){
                                isHead = costume.isHeadModel();
                                cost = costume;
                            }
                        }

                        HashMap<UUID, Integer> boneBrightness = MagicCostume.getPlugin().setAllBoneBrightness(
                                cost.getModel().getHeadBones(),
                                null,
                                brightness
                        );


                        MagicCostume.getPlugin().spawnActiveCostume(
                                player,
                                cost,
                                boneBrightness,
                                (float) scale,
                                (float) offsetX + (float) cost.getOffsetX(),
                                (float) offsetY + (float) cost.getOffsetY(),
                                (float) offsetZ + (float) cost.getOffsetZ(),
                                isHead
                        );
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }, 1L);
    }
}
