package net.phantara.prism.server.sidebar;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.scoreboard.Sidebar;

public class Scoreboard {

    private final Sidebar sidebar;

    public Scoreboard(Component component) {
        this.sidebar = new Sidebar(component);
    }

    public void setLines(Component... lines) {
        for (int i = 0; i < lines.length; i++) {
            this.sidebar.createLine(new Sidebar.ScoreboardLine("line-" + i, lines[i], lines.length - i));
        }
    }

    public void addPlayer(Player player) {
        this.sidebar.addViewer(player);
    }

    public void removePlayer(Player player) {
        this.sidebar.removeViewer(player);
    }

    public void addPlayers(Player... players) {
        for (Player player : players) {
            this.sidebar.addViewer(player);
        }
    }

    public void removePlayers(Player... players) {
        for (Player player : players) {
            this.sidebar.removeViewer(player);
        }
    }

    public Sidebar getSidebar() {
        return sidebar;
    }
}
