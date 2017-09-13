package com.wheezygold.happybot.commands;

import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;

public class RulesCommand extends Command {
    public RulesCommand() {
        this.name = "rules";
        this.help = "Links you to the rules";
        this.arguments = "";
        this.guildOnly = false;
        this.category = new Category("General");
    }

    @Override
    protected void execute(CommandEvent e) {
        e.replySuccess("Here are the rules: <http://bit.ly/2ihUfAc>");
    }
}
