package worldwarz.worldwarz.datamodule;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import worldwarz.worldwarz.utils.LogUtility;

import java.io.File;
import java.util.HashMap;

public class CommonData {

    public static HashMap<String, PlayerData> playerDataMap = new HashMap<String, PlayerData>();

    private static Plugin serverPlugin;
    private static final String playerDataFolderName = "playerdata";

    public static void setServerPlugin(Plugin serverPlugin){
        CommonData.serverPlugin = serverPlugin;
        serverPlugin.getServer().getPluginManager().registerEvents(new CommonDataEvent(), serverPlugin);
    }

    public static PlayerData getPlayerData(String uuid){
        if(playerDataMap.containsKey(uuid)){
            return playerDataMap.get(uuid);
        } else {
            return null;
        }
    }

    public static PlayerData getPlayerData(Player player){
        String uuid = player.getUniqueId().toString();
        return getPlayerData(uuid);
    }

    public static PlayerData getPlayerDataFromName(String playerName){
        for(PlayerData playerData : playerDataMap.values()){
            if(playerData.playerName.equals(playerName)){
                return  playerData;
            }
        }
        return null;
    }

    public static void autoSaveScheduler(){
        LogUtility.printLog(LogUtility.LogLevel.INFO, "플레이어 데이터 자동 저장 스케쥴 시작");
        Bukkit.getScheduler().scheduleSyncRepeatingTask(serverPlugin, new Runnable(){
            public void run(){
                LogUtility.printLog(LogUtility.LogLevel.INFO, "플레이어 데이터 저장 중...[AutoSave]");
                for(PlayerData playerData : playerDataMap.values()){
                    playerData.saveData();
                }
            }
        }, 1200l, 1200l);
    }

    public static void loadAllPlayerData(){

        LogUtility.printLog(LogUtility.LogLevel.INFO, "플레이어 데이터 풀 로드 시작");
        File playerDataFolder = getPlayerDataDirectory();

        for(File playerDataFile : playerDataFolder.listFiles()){
            if(playerDataFile.isDirectory()) return; //디렉터리 무시
            if(!playerDataFile.getAbsolutePath().endsWith(".yml")) return; //확장자 체크

            PlayerData playerData = loadPlayerData(playerDataFile);
            if(playerData != null){
                LogUtility.printLog(LogUtility.LogLevel.INFO, playerData.playerName+" 님의 PlayerData 로드 성공 [UUID:"
                        + playerData.uuid + "]");
            } else {
                LogUtility.printLog(LogUtility.LogLevel.ERROR, playerDataFile.getName()+" PlayerData 로드 실패");
            }
        }

        LogUtility.printLog(LogUtility.LogLevel.INFO, "로드된 플레이어 데이터 수: " + playerDataMap.keySet().size());

    }

    private static File getPlayerDataDirectory(){
        String playerDataFolderPath = serverPlugin.getDataFolder().getAbsolutePath() + "/" + "playerdata" + "/";
        File playerDataFolder = new File(playerDataFolderPath);
        if(!playerDataFolder.exists()) playerDataFolder.mkdirs();

        return playerDataFolder;
    }

    public static String getPlayerDataFilePath(Player player){
        return getPlayerDataFilePath(player.getUniqueId().toString());
    }

    public static String getPlayerDataFilePath(String uuid){
        File playerDataFolder = getPlayerDataDirectory();
        String filePath = playerDataFolder.getAbsolutePath() + "/" +uuid+".yml";
        return filePath;
    }

    public static PlayerData loadPlayerData(Player player){
        String filePath = getPlayerDataFilePath(player);
        File playerDataFile = new File(filePath);
        return loadPlayerData(playerDataFile);
    }

    public static PlayerData loadPlayerData(File playerDataFile){

        if(!playerDataFile.exists()){ //플레이어 데이터 파일 없으면
            return null;
        }

        //어차피 자주 쓰는 명령어 아니니 실시간 file read
        PlayerData playerData = new PlayerData(playerDataFile.getAbsolutePath());
        playerData.loadData();

        if(playerData.uuid != ""){
            playerDataMap.put(playerData.uuid, playerData);
        }

        return playerData;

    }

    public static class CommonDataEvent implements Listener {

        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent evt){
            Player player = evt.getPlayer();
            PlayerData playerData = getPlayerData(player);
            if(playerData == null) { //파일데이터 로드 안된 접속자면
                playerData = loadPlayerData(player); //로드 한번 해봄
                if(playerData == null){ //로드 안되면, 아예 신규
                    String playerDataFilePath = getPlayerDataFilePath(player);
                    playerData = new PlayerData(playerDataFilePath);
                    playerData.playerName = player.getName();
                    playerData.saveData();
                    LogUtility.printLog(LogUtility.LogLevel.INFO, player.getName() + "의 플레이어 데이터 신규 생성됨 [UUID: "
                            + playerData.uuid + "]");
                    loadPlayerData(player); //생성했으니 다시 로드 시도
                }
            }
        }

    }


}
