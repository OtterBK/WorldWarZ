package worldwarz.worldwarz;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;
import worldwarz.worldwarz.datamodule.CommonData;
import worldwarz.worldwarz.killcountmodule.KillCountModule;
import worldwarz.worldwarz.promotemodule.PromoteModule;
import worldwarz.worldwarz.timealertmodule.TimeAlertModule;
import worldwarz.worldwarz.utils.LogUtility;
import worldwarz.worldwarz.utils.MyUtility;

import java.util.UUID;

public final class WorldWarZ extends JavaPlugin implements Listener {

    public static PromoteModule promoteModule;
    public static KillCountModule killCountModule;
    public static TimeAlertModule timeAlertModule;

    @Override
    public void onEnable() {
        // Plugin startup logic
        CustomEntities.registerEntities();
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new WorldWarZEvent(), this);

        // CommonData 설정 건들지 말 것 ///
        LogUtility.printLog(LogUtility.LogLevel.INFO, "CommonData 초기화 중...");
        CommonData.setServerPlugin(this);
        CommonData.loadAllPlayerData();
        CommonData.autoSaveScheduler();
        //////////////////////////////

        //계급 시스템 관련
        promoteModule = new PromoteModule(this);
        LogUtility.printLog(LogUtility.LogLevel.INFO, "계급 시스템 모듈 로드됨");
        ////////////////////////////////////

        //킬 카운트 시스템 관련
        killCountModule = new KillCountModule(this);
        LogUtility.printLog(LogUtility.LogLevel.INFO, "킬 카운트 시스템 모듈 로드됨");
        ////////////////////////////////////

        //시간 알람 시스템 관련
        timeAlertModule = new TimeAlertModule(this);
        LogUtility.printLog(LogUtility.LogLevel.INFO, "시간 알림 시스템 모듈 로드됨");
        ////////////////////////////////////

        LogUtility.printLog(LogUtility.LogLevel.INFO, "[좀비아포칼립스] 플러그인 로드됨");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        CustomEntities.unregisterEntities();
        Bukkit.getLogger().info("[좀비아포칼립스] 플러그인 언로드됨");
    }

    public void testfunc(Player player){
        TestZombie zombie = new TestZombie(player.getLocation().clone().add(50,0,0));
        ((CraftWorld)player.getLocation().getWorld()).getHandle().addEntity(zombie, CreatureSpawnEvent.SpawnReason.CUSTOM);

    }

    @EventHandler
    public void onCommandInput(PlayerCommandPreprocessEvent evt){
        Player player = evt.getPlayer();
        String[] args = evt.getMessage().split(" ");
        String cmd = args[0];
        if(cmd.equalsIgnoreCase("/test") && player.isOp()){
            player.sendMessage("test");
            testfunc(player);
        } else if(cmd.equalsIgnoreCase("/playerdatareload")){
            CommonData.loadAllPlayerData();
        } else if(cmd.equalsIgnoreCase("/uuidtest")){
            String uuid = args[1];
            OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
            player.sendMessage("name: " + targetPlayer.getPlayer().getName());
        }
    }

}
