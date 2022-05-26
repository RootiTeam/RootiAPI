package ratnikov;

import cn.nukkit.event.Listener;
import cn.nukkit.Player;
import cn.nukkit.event.player.PlayerPreLoginEvent;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;

public class Economy implements Listener {
    
    private static Economy instance = null;
    private Loader main;

    public Economy(Loader main) {
        this.main = main;
        instance = this;
    }

    public static Economy getInstance() {
        return instance;
    }

    public Double getMoney(Player player) {
        return (double) this.main.money.get(player.getName().toLowerCase());
    }

    public Double getMoneyToNick(String nickname) {
        return (double) this.main.money.get(nickname);
    }

    public void addMoney(Player player, Double value) {
        this.main.money.set(player.getName().toLowerCase(), (this.getMoney(player) + value));
        this.main.money.save();
    }

    public void addMoneyToNick(String nickname, Double value) {
        this.main.money.set(nickname, (this.getMoneyToNick(nickname) + value));
        this.main.money.save();
    }

    public void remMoney(Player player, Double value) {
        this.main.money.set(player.getName().toLowerCase(), (this.getMoney(player) - value));
        this.main.money.save();
    }

    public void remMoneyToNick(String nickname, Double value) {
        this.main.money.set(nickname, (this.getMoneyToNick(nickname)- value));
        this.main.money.save();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void handleLogin(PlayerPreLoginEvent event) {
        String nickname = event.getPlayer().getName().toLowerCase();
        if (!this.main.money.exists(nickname)) {
            this.main.money.set(nickname, 0);
            this.main.money.save();
        }
    }
}

