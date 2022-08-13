package worldwarz.worldwarz.utils;

import org.bukkit.Bukkit;

public class LogUtility {

    public enum LogLevel{
        ERROR, DEBUG, INFO
    }

    public static void printLog(LogLevel logLevel, String logMessage){

        String finalLogMessage = "";

        if(logLevel == LogLevel.ERROR) {
            finalLogMessage += "§f[§cERROR§f] ";
        } else if(logLevel == LogLevel.DEBUG) {
            finalLogMessage += "§f[§bDEBUG§f] ";
        } else if(logLevel == LogLevel.INFO) {
            finalLogMessage += "§f[§eINFO§f] ";
        }

        finalLogMessage += logMessage;

        Bukkit.getLogger().info(finalLogMessage);

    }

}
