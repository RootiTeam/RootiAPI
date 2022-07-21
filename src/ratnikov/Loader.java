package ratnikov;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.event.Listener;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.level.Position;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.utils.Config;
import cn.nukkit.event.player.PlayerJoinEvent;
import java.io.File;

public class Loader extends PluginBase implements Listener{

    private static String SERVER_NAME = "Rooti > ";
	public Config money;

	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(this, this);
		this.getServer().getPluginManager().registerEvents(new Economy(this), this);
		this.money = new Config(new File(this.getDataFolder(), "balances.json"), Config.JSON);
		this.getLogger().notice("Плагин RootiAPI успешно загружен и готов к работе!");
		this.getLogger().info("Следите за актуальной версией плагина -> https://github.com/RootiTeam/RootiAPI");
	}

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void handleJoin(PlayerJoinEvent event) {
	   Player p = event.getPlayer();
	   event.setJoinMessage("");
	   p.sendMessage(SERVER_NAME + "Добро пожаловать на наш сервер!");
	}

	public Economy getEconomy() {
		return Economy.getInstance();
	}

    public static boolean isNumeric(String str) {
		return str.matches("-?\\d+(\\.\\d+)?");
	}

	public Player toPlayer(CommandSender sender) {
		return (Player) sender;
	}

	@Deprecated
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		switch(command.getName()) {
			case "spawn":
			if (!(sender instanceof Player)) {
				sender.sendMessage(SERVER_NAME + "Используйте данную команду в игре!");
				return false;
			}
			toPlayer(sender).teleport(this.getServer().getDefaultLevel().getSafeSpawn());
			sender.sendMessage(SERVER_NAME + "Вы телепортированы на спавн сервера!");
			break;
			case "fly":
			if (!(sender instanceof Player)) {
				sender.sendMessage(SERVER_NAME + "Используйте данную команду в игре!");
				return false;
			}
			if (!(sender.hasPermission("api.fly"))) {
				sender.sendMessage(SERVER_NAME + "Недостаточно прав для использования данной команды.");
				return false;
			}
			sender.sendMessage(SERVER_NAME + "Вы успешно " + (toPlayer(sender).getAllowFlight() ? "выключили" : "включили") + " режим флая.");
			toPlayer(sender).setAllowFlight(!toPlayer(sender).getAllowFlight());
			break;
			case "gm":
			if (!(sender instanceof Player)) {
				sender.sendMessage(SERVER_NAME + "Используйте данную команду в игре!");
				return false;
			}
			if (args.length < 1) {
				sender.sendMessage(SERVER_NAME + "Используйте - /gm (c,s)");
				return false;
			}
			if (!(sender.hasPermission("api.gm"))) {
				sender.sendMessage(SERVER_NAME + "Недостаточно прав для использования данной команды.");
				return false;
			}
			switch(args[0]) {
				case "c":
				toPlayer(sender).setGamemode(1);
				sender.sendMessage(SERVER_NAME + "Ваш игровой режим изменен на Креатив.");
				break;
				case "s":
				toPlayer(sender).setGamemode(0);
				sender.sendMessage(SERVER_NAME + "Ваш игровой режим изменен на Выживание.");
				break;
			}
			break;
			case "money":
			if (!(sender instanceof Player)) {
				sender.sendMessage(SERVER_NAME + "Используйте данную команду в игре!");
				return false;
			}
			sender.sendMessage(SERVER_NAME + "Ваш игровой баланс: " + this.getEconomy().getMoney(toPlayer(sender)) + "$");
			break;
			case "addmoney":
			if (!(sender.hasPermission("api.money.add"))) {
				sender.sendMessage(SERVER_NAME + "У вас недостаточно прав для использования данной команды.");
				return false;
			}
			if (args.length < 2) {
				sender.sendMessage(SERVER_NAME + "Используйте - /addmoney <никнейм игрока> <кол-во>");
				return false;
			}
			if (!isNumeric(args[1])) {
				sender.sendMessage(SERVER_NAME + "Пожалуйста, укажите число в количестве монет.");
				return false;
			}
			if (!this.money.exists(args[0].toLowerCase())) {
				sender.sendMessage(SERVER_NAME + "Данного игрока нет в базе данных нашего сервера!");
				return false;
			}
			double addValue = Double.valueOf(args[1]);
			this.getEconomy().addMoneyToNick(args[0].toLowerCase(), addValue);
			sender.sendMessage(SERVER_NAME + "Игроку " + args[0] + " успешно выдано " + addValue + "$");
			break;
			case "remmoney":
			if (!(sender.hasPermission("api.money.rem"))) {
				sender.sendMessage(SERVER_NAME + "У вас недостаточно прав для использования данной команды.");
				return false;
			}
			if (args.length < 2) {
				sender.sendMessage(SERVER_NAME + "Используйте - /remmoney <никнейм игрока> <кол-во>");
				return false;
			}
			if (!isNumeric(args[1])) {
				sender.sendMessage(SERVER_NAME + "Пожалуйста, укажите число в количестве монет.");
				return false;
			}
			if (!this.money.exists(args[0].toLowerCase())) {
				sender.sendMessage(SERVER_NAME + "Данного игрока нет в базе данных нашего сервера!");
				return false;
			}
			double remValue = Double.valueOf(args[1]);
			this.getEconomy().remMoneyToNick(args[0].toLowerCase(), remValue);
			sender.sendMessage(SERVER_NAME + "У игрока " + args[0] + " успешно отобрано " + remValue + "$");
			break;
			case "getmoney":
			if (!(sender.hasPermission("api.money.see"))) {
				sender.sendMessage(SERVER_NAME + "У вас недостаточно прав для использования данной команды.");
				return false;
			}
			if (args.length < 1) {
				sender.sendMessage(SERVER_NAME + "Используйте - /getmoney <никнейм игрока>");
				return false;
			}
			if (!this.money.exists(args[0].toLowerCase())) {
				sender.sendMessage(SERVER_NAME + "Данного игрока нет в базе данных нашего сервера!");
				return false;
			}
			Double balance = this.getEconomy().getMoneyToNick(args[0].toLowerCase());
			sender.sendMessage("Баланс игрока "+ args[0] +" составляет " + balance);
			break;
		}
		return false;
	}
}
