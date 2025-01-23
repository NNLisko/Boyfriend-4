package main;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.entities.Message;

import static net.dv8tion.jda.api.interactions.commands.OptionType.*;

public class App extends ListenerAdapter {

    @Override
    public void onReady(ReadyEvent event) {
        System.out.println("Bot is online and ready!");
    }

    public static void main(String[] args) {

        /* app token */
        String token = "TOKEN HERE";

        /*
         * servers here. optionally change this to just update commands globally instead
         * of
         * for looping through a list of specified ids
         */
        List<String> servers = Arrays.asList("SERVER IDS HERE");

        EnumSet<GatewayIntent> intents = EnumSet.of(
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.DIRECT_MESSAGES,
                GatewayIntent.MESSAGE_CONTENT);

        JDA jda;

        /* builds project and updates commands */
        try {
            jda = JDABuilder.createDefault(token, intents)
                    .addEventListeners(new App())
                    .setActivity(Activity.playing("with APIs"))
                    .build();

            jda.awaitReady();

            for (String server : servers) {
                Guild guild = jda.getGuildById(server);

                if (guild == null) {
                    System.out.println("Cannot find the server" + server);
                    return;
                }
                System.out.println("Updating commands for guild " + guild.getName());

                guild.updateCommands()
                        .addCommands( /* chain more commands here */
                                Commands.slash("bot-speaks", "Use this to show love :)")
                                        .addOption(STRING, "content", "What will the bot say", true))

                        .queue(success -> {
                            System.out.println("Commands updated for " + guild.getName());
                        }, error -> {
                            System.out.println("Commands update failed for " + guild.getName());
                        });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* receives messages and prints to terminal */
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User user = event.getAuthor();
        MessageChannelUnion server = event.getChannel();
        Message message = event.getMessage();

        if (user.isBot()) {
            return;
        }

        if (event.isFromGuild()) {
            System.out.printf("[%s] [%#s] %#s: %s\n",
                    event.getGuild().getName(),
                    server,
                    user,
                    message.getContentDisplay());
        }
    }

    /* Receives commands */
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        System.out.println("Received command" + event.getName());

        if (event.getGuild() == null) {
            return;
        }

        switch (event.getName()) {
            case "bot-speaks":
                botSpeaks(event, event.getOption("content").getAsString());
                break;

            default:
                break;
        }
    }

    /* actual commands */
    public void botSpeaks(SlashCommandInteractionEvent event, String content) {
        event.reply(content).queue();
    }
}