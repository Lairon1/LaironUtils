package com.lairon.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.function.Consumer;

public class MessageUtils {

    public static void sendMessageByTimer(Player player, String[] messages, int period, Consumer<Player> finish, Plugin plugin){
        int await = 0;
        for (int messageCount = 0; messageCount < messages.length; messageCount++) {
            int finalMessageCount = messageCount;
            Bukkit.getScheduler().runTaskLater(plugin, ()-> player.sendMessage(messages[finalMessageCount]), await);
            await += period;
        }
        await -= period;
        Bukkit.getScheduler().runTaskLater(plugin, ()-> {
            if(finish != null)
            finish.accept(player);
        }, await);
    }

}
