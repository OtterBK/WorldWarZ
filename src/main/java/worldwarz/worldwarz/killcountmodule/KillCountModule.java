package worldwarz.worldwarz.killcountmodule;

import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;
import worldwarz.worldwarz.datamodule.CommonData;
import worldwarz.worldwarz.datamodule.PlayerData;

import javax.swing.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class KillCountModule implements Listener {

    Plugin serverPlugin = null;
    final String msgPrefix = "§f[ §b킬스택 §f] ";

    public KillCountModule(Plugin serverPlugin){
        this.serverPlugin = serverPlugin;
        this.serverPlugin.getServer().getPluginManager().registerEvents(this,serverPlugin);
    }

    public void showZombieKillCount(Player player){
        PlayerData playerData = CommonData.getPlayerData(player);
        player.sendMessage(msgPrefix + "좀비 처치 수 : §c" +  playerData.killZombieCount + "킬");
    }

    public void showZombieKillScoreboard(Player player){

        //TODO 실시간 계산 말고 초기로드로 하면 빠를 것 같음, 귀찮으니 패스
        HashMap<String, Integer> zombieKillMap = new HashMap<String, Integer>();
        for(PlayerData playerData : CommonData.playerDataMap.values()){
            zombieKillMap.put(playerData.playerName, playerData.killZombieCount);
        }

        List<Map.Entry<String, Integer>> entryList = new LinkedList<>(zombieKillMap.entrySet());
        entryList.sort(Map.Entry.comparingByValue()); //정렬

        int showCount = 0;
        player.sendMessage(msgPrefix + "좀비 처치 순위:");
        for(Map.Entry<String, Integer> entry : entryList){
            player.sendMessage(msgPrefix + "[§b"+entry.getKey()+" §f: §c"+entry.getValue()+"킬§f]");
            if(++showCount >= 5) break;
        }
    }

    public void addZombieKillCount(Player player){

        player.playSound(player.getLocation(), Sound.ENTITY_IRONGOLEM_HURT, 1.0f, 2.5f);

        PlayerData playerData = CommonData.getPlayerData(player);
        playerData.killZombieCount++;
        playerData.saveData();

    }

    public void resetAllZombieKillCount(){
        for(PlayerData playerData : CommonData.playerDataMap.values()){
            playerData.killZombieCount = 0;
            playerData.saveData();
        }
    }

    @EventHandler
    public void onCommandInput(PlayerCommandPreprocessEvent evt){
        Player player = evt.getPlayer();
        String inputArgs[] = evt.getMessage().split(" ");
        String command = inputArgs[0];
        if(command.equalsIgnoreCase("/좀비")){
            evt.setCancelled(true);
            if(inputArgs.length == 1){
                player.sendMessage(msgPrefix + "/좀비 <킬/순위/초기화>");
            } else {
                if(inputArgs[1].equalsIgnoreCase("킬")){
                    showZombieKillCount(player);
                } else if(inputArgs[1].equalsIgnoreCase("순위")){
                    showZombieKillScoreboard(player);
                } else if(inputArgs[1].equalsIgnoreCase("초기화") ){
                    if(player.isOp()){
                        resetAllZombieKillCount();
                        player.sendMessage(msgPrefix+"모든 플레이어 데이터의 좀비 킬 카운트를 초기화했습니다.");
                    } else {
                        player.sendMessage(msgPrefix+"권한이 부족합니다.");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent evt){
        LivingEntity entity = evt.getEntity();
        LivingEntity killerEntity = entity.getKiller();

        if(killerEntity == null || !(killerEntity instanceof Player)) return;

        Player killerPlayer = (Player) killerEntity;

        if(entity.getType() == EntityType.ZOMBIE || entity.getType() == EntityType.ZOMBIE_VILLAGER
            || entity.getType() == EntityType.HUSK){

            addZombieKillCount(killerPlayer);

        }
    }

}
