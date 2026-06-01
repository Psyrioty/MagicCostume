package org.psyrioty.magicCostume.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.psyrioty.magicCostume.Database.DatabaseManager;
import org.psyrioty.magicCostume.Database.Requests;
import org.psyrioty.magicCostume.MagicCostume;
import org.psyrioty.magicCostume.Objects.ActiveCostumeEntity;
import org.psyrioty.magicCostume.Objects.ActiveSlot;
import org.psyrioty.magicCostume.Objects.Costume;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class EntityEvents implements Listener {
    @EventHandler
    private void playerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        ActiveCostumeEntity activeCostumeEntityNew = null;

        for(ActiveCostumeEntity activeCostumeEntity: MagicCostume.getPlugin().getActiveCostumeEntities()){
            if(activeCostumeEntity.getEntity().getUniqueId().equals(player.getUniqueId())){
                activeCostumeEntity.setEntity(player);
                activeCostumeEntityNew = activeCostumeEntity;
                break;
            }
        }
        if(activeCostumeEntityNew == null) {
            activeCostumeEntityNew = new ActiveCostumeEntity(player);
        }

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
                            (float) offsetX,
                            (float) offsetY,
                            (float) offsetZ,
                            isHead
                    );
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @EventHandler
    private void playerExit(PlayerQuitEvent event){
        Player player = event.getPlayer();

        Set<ActiveCostumeEntity> activeCostumeEntityList = MagicCostume.getPlugin().getActiveCostumeEntities();

        for(ActiveCostumeEntity activeCostumeEntity: activeCostumeEntityList){
            if(activeCostumeEntity.getEntity().getUniqueId().equals(player.getUniqueId())){
                activeCostumeEntityList.remove(activeCostumeEntity);
                return;
            }
        }
    }
}
