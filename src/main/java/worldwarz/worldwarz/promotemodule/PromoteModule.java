package worldwarz.worldwarz.promotemodule;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;
import worldwarz.worldwarz.WorldWarZ;
import worldwarz.worldwarz.datamodule.CommonData;
import worldwarz.worldwarz.datamodule.PlayerData;
import worldwarz.worldwarz.utils.LogUtility;
import worldwarz.worldwarz.utils.MyUtility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class PromoteModule implements Listener {

    Plugin serverPlugin = null;
    final String msgPrefix = "§f[ §b계급 §f] ";

    public PromoteModule(Plugin serverPlugin){
        this.serverPlugin = serverPlugin;
        this.serverPlugin.getServer().getPluginManager().registerEvents(this,serverPlugin);
    }

    public void showPromoteClass(Player player){
        PlayerData playerData = CommonData.getPlayerData(player);
        if(playerData == null) {
            player.sendMessage(msgPrefix + "당신의 플레이어 데이터가 존재하지 않습니다.");
        } else {
            player.sendMessage(msgPrefix + "당신의 계급은 §d" + playerData.promoteClass + "§f입니다.");
        }
    }

    public void setPromote(Player executePlayer, String targetPlayerName, String promoteName){
        promoteName = promoteName.trim();

        PromoteType promoteType = PromoteType.getPromoteTypeFromString(promoteName);

        if(promoteType == null) {
            executePlayer.sendMessage(msgPrefix+promoteName+"은 존재하지 않는 계급입니다.");
            return;
        }

        //TODO 이걸 getPlayerDataFromName으로 바꿀지 말지 결정, Bukkit.getPlayer 는 리소스 비쌈
        String targetUUID = Bukkit.getPlayer(targetPlayerName).getUniqueId().toString();
        PlayerData playerData = CommonData.getPlayerData(targetUUID);

        if(playerData == null){
            executePlayer.sendMessage(msgPrefix+ targetPlayerName + " 의 플레이어 데이터가 존재하지 않습니다.");
            return;
        }

        playerData.promoteClass = promoteName;
        playerData.saveData();

        Player targetPlayer = MyUtility.getOnlinePlayer(targetPlayerName);
        if(targetPlayer != null){
            targetPlayer.sendMessage(msgPrefix + "계급이 §d" + promoteName + "§f으로 바뀌었습니다.");
        }

        executePlayer.sendMessage(msgPrefix + targetPlayerName+"의 계급을 " + promoteName + "으로 변경하였습니다.");

    }

    public void resetAllPromote(){
        for(PlayerData playerData : CommonData.playerDataMap.values()){
            playerData.promoteClass = "민간인";
            playerData.saveData();
        }
    }

    //TODO 나중에 개선할 것
    public void showPromoteScoreboard(Player requestPlayer){

        List<PlayerData> scoreboardList = new ArrayList<PlayerData>();

        Iterator<PlayerData> playerDataIterator = CommonData.playerDataMap.values().iterator();
        if(!playerDataIterator.hasNext()) {
            requestPlayer.sendMessage(msgPrefix + "플레이어 데이터가 한 개도 존재하지 않습니다.");
            return;
        }

        PlayerData firstData = playerDataIterator.next();
        scoreboardList.add(firstData);
        while (playerDataIterator.hasNext()){
            PlayerData targetData = playerDataIterator.next();

            boolean isInserted = false;
            //삽입 정렬
            for(int i = 0; i < scoreboardList.size(); i++){
                PlayerData basePlayerData = scoreboardList.get(i);
                PromoteType basePromote = PromoteType.getPromoteTypeFromString(basePlayerData.promoteClass);
                if(basePromote == null){
                    requestPlayer.sendMessage(msgPrefix + "순위 정렬 중, 존재하지 않는 계급의 플레이어 데이터가 있었습니다.");
                    LogUtility.printLog(LogUtility.LogLevel.ERROR, msgPrefix+"showPromoteScoreboard error, promote class fault, basePromote, uuid: "+basePlayerData.uuid + ", name: " + basePlayerData.playerName);
                    continue;
                }
                PromoteType targetPromote = PromoteType.getPromoteTypeFromString(targetData.promoteClass);
                if(basePromote == null){
                    requestPlayer.sendMessage(msgPrefix + "순위 정렬 중, 존재하지 않는 계급의 플레이어 데이터가 있었습니다.");
                    LogUtility.printLog(LogUtility.LogLevel.ERROR, msgPrefix+"showPromoteScoreboard error, promote class fault, targetPromote, uuid: "+targetData.uuid + ", name: " + targetData.playerName);
                    continue;
                }

                if(basePromote.priority < targetPromote.priority){ //우선 순위 높으면 삽입
                    scoreboardList.add(i, targetData);
                    isInserted = true;
                    break;
                }
            }

            if(!isInserted){ //삽입 못햇으면 뒤에 붙이기
                scoreboardList.add(targetData);
            }
        }

        if(scoreboardList.size() != 0){
            requestPlayer.sendMessage(msgPrefix+"============ 계급 정보 ============");
            int maxCount = 15;
            for(int i = 0; i < maxCount && i < scoreboardList.size(); i++){
                PlayerData playerData = scoreboardList.get(i);
                requestPlayer.sendMessage(msgPrefix+playerData.playerName + " : §d" + playerData.promoteClass);
            }

        }

    }

    @EventHandler
    public void onCommandInput(PlayerCommandPreprocessEvent evt) {

        Player player = evt.getPlayer();
        String inputArgs[] = evt.getMessage().split(" ");
        String command = inputArgs[0];
        if (command.equalsIgnoreCase("/계급")) {
            evt.setCancelled(true);

            if (inputArgs.length == 1) {
                showPromoteClass(player);
            } else {
                if (inputArgs[1].equalsIgnoreCase("도움말")) {
                    player.sendMessage(msgPrefix + "/계급 순위");
                    if (player.isOp()) {
                        player.sendMessage(msgPrefix + "OP인 당신이 추가로 사용 가능한 명령어: ");
                        player.sendMessage(msgPrefix + "/계급 설정 <닉네임> <계급명>");
                        player.sendMessage(msgPrefix + "/계급 해제 <닉네임>");
                        player.sendMessage(msgPrefix + "/계급 전체초기화");
                    }
                } else if (inputArgs[1].equalsIgnoreCase("순위")){
                    showPromoteScoreboard(player);
                } else if (inputArgs[1].equalsIgnoreCase("설정") && player.isOp()) {
                    if (inputArgs.length == 2) {
                        player.sendMessage(msgPrefix + "닉네임을 입력해주세요.");
                    } else if (inputArgs.length == 3) {
                        player.sendMessage(msgPrefix + "계급을 입력해주세요.");
                    } else if (inputArgs.length >= 4) {
                        setPromote(player, inputArgs[2], inputArgs[3]);
                    }
                } else if (inputArgs[1].equalsIgnoreCase("해제") && player.isOp()) {
                    if (inputArgs.length == 2) {
                        player.sendMessage(msgPrefix + "닉네임을 입력해주세요.");
                    } else if(inputArgs.length >= 3){
                        setPromote(player, inputArgs[2], "민간인");
                    }
                } else if(inputArgs[1].equalsIgnoreCase("전체초기화") &&player .isOp()){
                    resetAllPromote();
                    player.sendMessage(msgPrefix + "모든 플레이어의 계급을 민간인으로 설정하였습니다.");
                }
            }
        }

    }

}
