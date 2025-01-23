package main;

import java.util.EnumSet;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.entities.Message;

public class App extends ListenerAdapter {

    @Override
    public void onReady(ReadyEvent event) {
        System.out.println("Bot is online and ready!");
    }

    public static void main(String[] args) {
        String token = "MTMzMTg5Njk4MjA4MzQ2OTM3Mw.G4uzxE.yS5IlVWdK5rv64ocFlrNAcG_2JBK-lqJ0cIc7g";

        EnumSet<GatewayIntent> intents = EnumSet.of(
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.DIRECT_MESSAGES,
                GatewayIntent.MESSAGE_CONTENT);

        JDABuilder.createDefault(token, intents)
                .addEventListeners(new App())
                .setActivity(Activity.playing("with APIs"))
                .build();

    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User user = event.getAuthor();
        MessageChannelUnion server = event.getChannel();
        Message message = event.getMessage();

    }
}