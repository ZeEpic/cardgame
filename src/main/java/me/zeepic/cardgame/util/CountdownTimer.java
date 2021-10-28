package me.zeepic.cardgame.util;

import lombok.Getter;
import lombok.Setter;
import me.zeepic.cardgame.Main;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.function.Consumer;

public class CountdownTimer implements Runnable {

    @Getter @Setter private int taskId;
    @Getter @Setter private int timeRemaining;
    @Getter final private int startTime;
    @Getter final Consumer<Integer> onFinish;
    @Getter static final BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

    public CountdownTimer(int startTime, Consumer<Integer> onFinish) {
        this.startTime = startTime;
        this.onFinish = onFinish;
    }

    public void startTimer(Main plugin) {
        setTimeRemaining(startTime);
        taskId = getScheduler().scheduleSyncRepeatingTask(plugin, this, 0L, 20L);
    }

    public void stopTimer() {
       getScheduler().cancelTask(getTaskId());
    }

    @Override
    public void run() {

        if (getTimeRemaining() <= 0) {
            getOnFinish().accept(getStartTime());
            stopTimer();
            return;
        }
        timeRemaining--;

    }
}
