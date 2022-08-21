package worldwarz.worldwarz.datamodule;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import worldwarz.worldwarz.utils.LogUtility;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PlayerData{

    private String filePath = null;

    public String playerName = "";
    public String uuid = "";
    public String promoteClass = "민간인";
    public int killZombieCount = 0;

    public PlayerData(String filePath){

        this.filePath = filePath;

    }

    public void loadData(){
        File playerDataFile = new File(filePath);
        //int 타입도 있으니 그냥 하드코딩으로 불러오자
        FileConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);

        String uuid = playerDataConfig.getString("uuid");
        String playerName = playerDataConfig.getString("playerName");
        String promoteClass = playerDataConfig.getString("promoteClass");
        int killZombieCount = playerDataConfig.getInt("killZombieCount");

        this.uuid = uuid;
        this.playerName = playerName;
        this.promoteClass = promoteClass;
        this.killZombieCount = killZombieCount;

        if(uuid == null) {
            LogUtility.printLog(LogUtility.LogLevel.ERROR, uuid+" PlayerData 로드 실패");
            return;
        }

        if(playerName == null && uuid != ""){
//            Bukkit.getLogger().info("get playername from uuid: [" + uuid + "]");
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
            if(offlinePlayer == null) {
                LogUtility.printLog(LogUtility.LogLevel.ERROR, uuid + " 데이터 존재하지 않음");
            } else {
                this.playerName = offlinePlayer.getName();
            }
        }
    }

    public void saveData(){

        File playerDataFile = new File(filePath);
        this.uuid = playerDataFile.getName().replace(".yml", "").trim();

        if(this.playerName == null){
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
            if(offlinePlayer == null){
                LogUtility.printLog(LogUtility.LogLevel.ERROR, uuid + " 데이터 존재하지 않음");
            } else {
                this.playerName = offlinePlayer.getName();
            }
        }


        if(!playerDataFile.exists()) { //파일 없으면 생성
            try {
                if(!playerDataFile.createNewFile()){
                    LogUtility.printLog(LogUtility.LogLevel.ERROR, playerDataFile.getName()+" PlayerData 생성 실패");
                }
            } catch (IOException e) {
                LogUtility.printLog(LogUtility.LogLevel.ERROR, playerDataFile.getName()+" PlayerData 생성 실패");
                e.printStackTrace();
            }

        }

        FileConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
        playerDataConfig.set("uuid", uuid);
        playerDataConfig.set("playerName", playerName);
        playerDataConfig.set("promoteClass", promoteClass);
        playerDataConfig.set("killZombieCount", killZombieCount);

        try {
            playerDataConfig.save(playerDataFile);
        } catch (IOException e) {
            LogUtility.printLog(LogUtility.LogLevel.ERROR, playerDataFile.getName()+" PlayerData 저장 실패");
            e.printStackTrace();
        }
    }

}