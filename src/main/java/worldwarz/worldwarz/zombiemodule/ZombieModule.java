package worldwarz.worldwarz.zombiemodule;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import worldwarz.worldwarz.utils.MyUtility;

public class ZombieModule {

    public Plugin serverPlugin;

    final public String msgPrefix = "§f[ §c좀비 §f] ";

    public ZombieModule(Plugin serverPlugin){
        this.serverPlugin = serverPlugin;

        ZombieActiveTimer();
    }

    public void ZombieActiveTimer(){ //밤이 되면 좀비 움직이도록록

       World world = Bukkit.getWorld("world"); //world 가져오기

        //하루 길이는 1200초
        //밤은 629초 ~ 익일 50초
        Bukkit.getScheduler().scheduleSyncRepeatingTask(serverPlugin, new Runnable() {
            @Override
            public void run() {
                long timeTick = world.getFullTime() % 24000;
                long timeSecond = (long)(timeTick / 20);

                if(timeSecond >= 629 || timeSecond <= 50){ //밤 시간 대
                    //패스 파인더 적용
                }
            }
        }, 0l, 20l);
    }

}
