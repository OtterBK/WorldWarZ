package worldwarz.worldwarz.timealertmodule;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import worldwarz.worldwarz.utils.MyUtility;

public class TimeAlertModule {

    private Plugin serverPlugin;

    public TimeAlertModule(Plugin serverPlugin){
        this.serverPlugin = serverPlugin;
        timeAlertScheduler();
    }

    public void timeAlertScheduler(){

        World world = Bukkit.getWorld("world"); //world 가져오기

        //하루 길이는 1200초
        //낮은 50초에 시작하니 1190 초에 알람
        //밤은 629초에 시작하니, 569초에 알람
        Bukkit.getScheduler().scheduleSyncRepeatingTask(serverPlugin, new Runnable(){
            public void run(){
                long timeTick = world.getFullTime() % 24000;
                long timeSecond = (long)(timeTick / 20);
                if(timeSecond == 1190){
                    Bukkit.broadcastMessage("§f[§6곧 날이 밝습니다.§f]");
                    MyUtility.playSoundForAllPlayer(Sound.ENTITY_CHICKEN_AMBIENT, 1.5f, 0.8f);
                } else if(timeSecond == 569){
                    Bukkit.broadcastMessage("§f[§7곧 밤이 되어 좀비가 몰려옵니다...§f]");
                    MyUtility.playSoundForAllPlayer(Sound.ENTITY_ZOMBIE_AMBIENT, 1.5f, 0.6f);
                }
            }
        }, 0l, 20l); //TODO 이러면 오차 생기지 않을까?
    }

}
