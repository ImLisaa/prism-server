package net.phantara.prism.server.tablist;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import net.minestom.server.network.packet.server.play.TeamsPacket;
import net.minestom.server.scoreboard.Team;
import net.minestom.server.scoreboard.TeamBuilder;
import net.minestom.server.scoreboard.TeamManager;

import java.util.function.Predicate;

/**
 * @author Lisa Kapahnke
 * @created 19.01.2024 | 15:45
 * @contact @imlisaa_ (Discord)
 * <p>
 * You are not allowed to modify or make changes to
 * this file without permission.
 **/

public class TablistTeam {

    private final String name;
    private final int id;
    private final Component prefix;
    private final NamedTextColor textColor;
    private final Predicate<Player> predicate;
    private Team team;

    public TablistTeam(String name, int id, Component prefix, NamedTextColor textColor, Predicate<Player> predicate) {
        this.name = name;
        this.id = id;
        this.prefix = prefix;
        this.textColor = textColor;
        this.predicate = predicate;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Component getPrefix() {
        return prefix;
    }

    public NamedTextColor getTextColor() {
        return textColor;
    }

    public Predicate<Player> getPredicate() {
        return predicate;
    }

    public Team getTeam() {
        return team;
    }

    public void toTeam(TeamManager manager) {
        this.team = new TeamBuilder(this.name, manager).updateTeamColor(this.textColor).updatePrefix(this.prefix).collisionRule(TeamsPacket.CollisionRule.NEVER).build();
    }
}
