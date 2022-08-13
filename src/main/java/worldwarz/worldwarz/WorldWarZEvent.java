package worldwarz.worldwarz;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import worldwarz.worldwarz.utils.MyUtility;

public class WorldWarZEvent implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent evt){
        evt.setDeathMessage("§c누군가 사망하였습니다...");
        MyUtility.playSoundForAllPlayer(Sound.ENTITY_GHAST_SCREAM, 1.5F, 0.5F);
    }

//    @EventHandler
//    public void onEntityDamagedEvent(EntityDamageEvent evt){
//        Entity entity = evt.getEntity();
//        if(entity.getType() == EntityType.ZOMBIE || entity.getType() == EntityType.ZOMBIE_VILLAGER){
//            if(evt.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || evt.getCause() == EntityDamageEvent.DamageCause.FIRE){
//                evt.setCancelled(true);
//            }
//        }
//    }

    @EventHandler
    public void onEntityBurn(EntityCombustEvent evt){
        Entity entity = evt.getEntity();
        if(entity.getType() == EntityType.ZOMBIE
                || entity.getType() == EntityType.ZOMBIE_VILLAGER
                || entity.getType() == EntityType.HUSK){
            evt.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityRespawned(EntitySpawnEvent evt){
        Entity entity = evt.getEntity();
        if(entity instanceof Player) {
            return;
        }
        if(
            entity.getType() == EntityType.ENDER_DRAGON ||
            entity.getType() == EntityType.BLAZE ||
            entity.getType() == EntityType.CAVE_SPIDER ||
            entity.getType() == EntityType.CHICKEN ||
            entity.getType() == EntityType.COW ||
            entity.getType() == EntityType.CREEPER ||
            entity.getType() == EntityType.DONKEY ||
            entity.getType() == EntityType.ELDER_GUARDIAN ||
            entity.getType() == EntityType.ENDERMAN ||
            entity.getType() == EntityType.ENDERMITE ||
            entity.getType() == EntityType.EVOKER ||
            entity.getType() == EntityType.EVOKER_FANGS ||
            entity.getType() == EntityType.GHAST ||
            entity.getType() == EntityType.GUARDIAN ||
            entity.getType() == EntityType.HORSE ||
            entity.getType() == EntityType.IRON_GOLEM ||
            entity.getType() == EntityType.ILLUSIONER ||
            entity.getType() == EntityType.LLAMA ||
            entity.getType() == EntityType.LLAMA_SPIT ||
            entity.getType() == EntityType.MAGMA_CUBE ||
            entity.getType() == EntityType.MUSHROOM_COW ||
            entity.getType() == EntityType.MULE ||
            entity.getType() == EntityType.OCELOT ||
            entity.getType() == EntityType.PIG ||
            entity.getType() == EntityType.PIG_ZOMBIE ||
            entity.getType() == EntityType.POLAR_BEAR ||
            entity.getType() == EntityType.SHEEP ||
            entity.getType() == EntityType.SILVERFISH ||
            entity.getType() == EntityType.SKELETON ||
            entity.getType() == EntityType.SKELETON_HORSE ||
            entity.getType() == EntityType.SLIME ||
            entity.getType() == EntityType.SNOWMAN ||
            entity.getType() == EntityType.SPIDER ||
            entity.getType() == EntityType.SQUID ||
            entity.getType() == EntityType.VILLAGER ||
            entity.getType() == EntityType.WITCH ||
            entity.getType() == EntityType.STRAY ||
            entity.getType() == EntityType.VINDICATOR ||
            entity.getType() == EntityType.WITHER ||
            entity.getType() == EntityType.WITHER_SKULL ||
            entity.getType() == EntityType.ZOMBIE_HORSE
        ){ //모드 엔티티는 소환 가능하도록 일치하는 것만 리스폰 취소
            evt.setCancelled(true);
        }

        // 커스텀 요청 by 아재민
        // 모던워페어 모드 적용 시 bandit 리스폰 안되게
        if(entity.getType().toString().contains("bandit") || entity.getType().toString().contains("BANDIT")){
            evt.setCancelled(true);
        }
    }

}
