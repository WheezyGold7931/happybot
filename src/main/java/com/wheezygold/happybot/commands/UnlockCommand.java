package com.wheezygold.happybot.commands;

import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;
import com.wheezygold.happybot.util.C;
import com.wheezygold.happybot.util.Roles;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.PermissionOverride;
import net.dv8tion.jda.core.managers.PermOverrideManager;

public class UnlockCommand extends Command {
    public UnlockCommand() {
        this.name = "unlock";
        this.help = "Unlocks the current channel!";
        this.guildOnly = true;
        this.category = new Category("Staff Tools");
    }

    @Override
    protected void execute(CommandEvent e) {
        if (C.hasRole(e.getMember(), Roles.ADMIN)) {
            PermissionOverride permissionOverride = e.getTextChannel().getPermissionOverride(Roles.EVERYONE.getRole());

            PermOverrideManager manager = permissionOverride.getManager();

            manager.grant(Permission.MESSAGE_WRITE).queue();

            e.replySuccess(":unlock: Channel has been unlocked!");
        } else {
            e.replyError(C.permMsg(Roles.ADMIN));
        }
    }
}
