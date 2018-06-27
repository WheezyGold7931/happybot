package io.github.jroy.happybot.commands.base;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import io.github.jroy.happybot.util.C;
import io.github.jroy.happybot.util.Roles;
import net.dv8tion.jda.core.entities.Member;
import org.jetbrains.annotations.NotNull;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

/**
 * A custom implementation of JDA-Utilities's {@link com.jagrosh.jdautilities.command.Command Command} class that makes our use-case easier.
 */
public abstract class CommandBase extends Command {

    /**
     * The role that is required to run the target command.
     *
     * Will be null if no role is required
     */
    private final Roles permissionRole;

    /**
     * The command's category
     */
    private final CommandCategory commandCategory;

    /**
     * Storage for command cooldowns.
     * Null if the command has no cooldown.
     */
    private HashMap<Member, OffsetDateTime> commandCooldowns;

    /**
     * Unit of time the cooldown is relative to.
     */
    private ChronoUnit cooldownUnit;

    /**
     * Relative cooldown delay.
     */
    private Integer cooldownDelay;

    /**
     *
     * Constructor for commands with no role permissions requires for execution.
     *
     * @param commandName Command's name to be used for execution.
     * @param arguments Arguments of the command to be used for command help.
     * @param helpMessage Command description to be used inside the command list.
     * @param category Command's category to be used inside the command list.
     */
    public CommandBase(@NotNull String commandName, String arguments, String helpMessage, CommandCategory category) {
        this.name = commandName;
        this.arguments = arguments;
        this.help = helpMessage;
        this.category = new Category(category.toString());
        this.commandCategory = category;
        this.permissionRole = null;
        this.commandCooldowns = null;
        this.cooldownUnit = null;
        this.cooldownDelay = null;
    }

    /**
     *
     * Constructor for commands with certain role requires for execution.
     *
     * @param commandName Command's name to be used for execution.
     * @param arguments Arguments of the command to be used for command help.
     * @param helpMessage Command description to be used inside the command list.
     * @param category Command's category to be used inside the command list.
     * @param permissionRole The role requires to execute the command.
     */
    public CommandBase(@NotNull String commandName, String arguments, String helpMessage, CommandCategory category, Roles permissionRole) {
        this.name = commandName;
        this.arguments = arguments;
        this.help = helpMessage;
        this.category = new Category(category.toString());
        this.commandCategory = category;
        this.permissionRole = permissionRole;
        this.commandCooldowns = null;
        this.cooldownUnit = null;
        this.cooldownDelay = null;
    }

    public void setCooldown(int seconds) {
        setCooldown(seconds, ChronoUnit.SECONDS);
    }

    public void setCooldown(int amount, ChronoUnit chronoUnit) {
        commandCooldowns = new HashMap<>();
        cooldownUnit = chronoUnit;
        cooldownDelay = amount;
    }

    @Override
    protected void execute(CommandEvent event) {
        if (permissionRole != null) {
            if (!C.hasRole(event.getMember(), permissionRole)) {
                event.replyError(C.permMsg(permissionRole));
                return;
            }
        }
        if ((int) OffsetDateTime.now().until(commandCooldowns.get(event.getMember()), cooldownUnit) > 0) {
            event.replyError("You must wait before doing that command again!");
            return;
        }
        commandCooldowns.put(event.getMember(), OffsetDateTime.now().plus(cooldownDelay, cooldownUnit));
        executeCommand(new io.github.jroy.happybot.commands.base.CommandEvent(event.getEvent(), event.getArgs(), event.getClient()));
    }

    /**
     * Triggered when the command is ran and the user has the required permission.
     * @param event The information associated with the command calling
     */
    protected abstract void executeCommand(io.github.jroy.happybot.commands.base.CommandEvent event);

    /**
     * @return Command usage.
     */
    protected String invalid() {
        return "**Correct Usage:** ^" + name + " " + arguments;
    }

    /**
     * @return Command's Permission
     */
    public Roles getPermissionRole() {
        return permissionRole;
    }

    /**
     * @return Command's Category
     */
    public CommandCategory getCommandCategory() {
        return commandCategory;
    }
}
