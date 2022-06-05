package io.github.scifi9902.spawnerchecker;

import com.massivecraft.factions.*;
import com.massivecraft.factions.config.transition.oldclass.v1.OldMainConfigV1;
import io.github.scifi9902.spawnerchecker.util.CC;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SpawnerCheckerPlugin extends JavaPlugin implements Listener {


    private FPlayers factionPlayers;

    public void onEnable() {
        this.factionPlayers = FPlayers.getInstance();

        if (this.factionPlayers == null) {
            this.getServer().getConsoleSender().sendMessage(CC.chat("&cUnable to find the FPlayers instance..."));
            this.getServer().shutdown();
        }

        this.getServer().getPluginManager().registerEvents(this, this);

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (block == null || !block.getType().equals(Material.MOB_SPAWNER)) {
            return;
        }

        FPlayer fPlayer = this.factionPlayers.getByPlayer(player);

        if (fPlayer == null || !fPlayer.hasFaction()) {
            player.sendMessage(CC.chat("&cYou may only place spawners whilst in a faction."));
            return;
        }

        Faction faction = fPlayer.getFaction();

        boolean validPlace = false;

        for (FLocation locations : faction.getAllClaims()) {
            if (locations.isInChunk(block.getLocation())) {
                validPlace = true;
                break;
            }
        }

        if (!validPlace) {
            player.sendMessage(CC.chat("&cYou may only place spawners in your own claim."));
            event.setCancelled(true);
        }
    }


}
