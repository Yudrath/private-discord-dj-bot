<h1 align="center">Self-hosted Private Discord DJ Bot</h1><br>
<p align="center">
A for private use Discord DJ bot written in Java, that is meant to be hosted on your own machine for free.
</p>

# How it works
The bot is meant to be used among friends in a private Discord server. As it relies on being self-hosted, someone in the server has to create an instance of the bot, in other words, self-host it, which is done by just starting the program and clicking the **power on** button. But we want to avoid having more than one person hosting an instance of the bot and we achieve that by utilising two bots: the DJ bot and a helper bot. The helper bot's purpose is to check the DJ bot's status (online/offline) before allowing a user to host the DJ bot. If the status of the DJ bot is offline, only then the option to start it will be present.

# How to set up the bot

*You might want to create an alt Discord account for the next steps, because they involve integrating Discord tokens into the code. If you don't consider sharing the build of the app with friends, this mustn't be an issue, otherwise, bear in mind that the application can be reverse engineered and your tokens can be possibly used maliciously (deleting your server, deleting the members, sharing something illegal on the server etc), but there are ways to mitigate or essentially eliminate the possible damage which are explained further.*

**NEVER SHARE THE APPLICATION WITH PEOPLE YOU DON'T TRUST!**

### Setting up the bot:
If you plan on using a separate Discord account, you must create it and login into it now.

1. Go to the [Discord Developer Portal](https://discord.com/developers/applications). There you must create 2 applications — the DJ application and the Helper application.
2. For each application, go to Settings -> Bot, find Privileged Gateway Intents and enable all intents.
3. In the same setting category (Bot), scroll a bit up and find the button "Reset token". Click it and copy the token. Now, go inside your IDE, where the project from this repository should be loaded, and find the classes DiscordBot and HelperBot, there you should paste each respective token in the constant variable BOT_TOKEN.
4. The Helper bot should be able to run a check on the status of the DJ bot, so they must be on the same Discord server. You can create your own Discord server just for the bots to hide this process "behind the scenes" or you can use a server, where the DJ will operate. You **DON'T** need to add the Helper bot to every Discord server the DJ is part of, this is why I recommend a separate server for the bots only (and you as the creator, of course). Now, create a separate Discord server for the bots, if you wish. Otherwise, read on.
5. It's time to invite the bots to a server. Let's first start with the Helper bot. Switch to the Helper bot application in the Discord Developer Portal, navigate to Settings -> OAuth2 and scroll down until you reach OAuth2 URL Generator. We have to create the bot's invite URL and set its permissions. From Scopes, mark the "bot" box, scroll to the bottom of the page and copy the generated URL. Place the generated URL inside the search bar of your browser, press Enter and add the Helper bot to a server, where you also plan to add the DJ bot.
6. Next, we do the same with the DJ bot, but this time in Scopes choose bot and applications.commands, then scroll to the Bot Permissions panel and choose:
   * View channels
   * Send Messages
   * Manage Messages
   * Embed Links
   * Read Message History
   * Connect
   * Speak
   * Use Voice Activity
7. After the bots are added to a common Discord server, open the Discord app, go to your settings and enable Developer mode.
8. Go to the Discord server where the bots are added, get the server's ID, open the project in your IDE and assign the copied ID to GUILD_ID in the HelperBot class. After pasting it, attach an "L" to the ID value (000000000**L**).
9. Repeat step 8, this time copying the DJ bot's ID and assigning it to DJ_ID in the same class.
10. You can finally build the project and use it.

# Commands
<table>
  <tr>
    <th>Command</th>
    <th>Description</th>
  </tr>
  <tr>
    <td>/play</td>
    <td>You can feed a URL of a track/playlist directly to the /play command, or alternatively, you can use a search query to find a desired song. Only Spotify, Soundcloud and YouTube links are allowed.</td>
  </tr>
  <tr>
    <td>/clear</td>
    <td>Empties the queue.</td>
  </tr>
  <tr>
    <td>/join</td>
    <td>Joins the bot to your audio channel. If a track was playing when the bot left, it resumes playing it.</td>
  </tr>
  <tr>
    <td>/leave</td>
    <td>Makes the bot leave its current audio channel. It also pauses the currently playing track, if one is playing.</td>
  </tr>
  <tr>
    <td>/loop</td>
    <td>Sets loop on or off.</td>
  </tr>
  <tr>
    <td>/nowplaying</td>
    <td>Returns a message with information about the currently playing track.</td>
  </tr>
  <tr>
    <td>/pause</td>
    <td>Pauses the player.</td>
  </tr>
  <tr>
    <td>/unpause</td>
    <td>Unpauses the player.</td>
  </tr>
  <tr>
    <td>/previous</td>
    <td>Plays the previous track.</td>
  </tr>
  <tr>
    <td>/queue</td>
    <td>Displays the queue of tracks.</td>
  </tr>
  <tr>
    <td>/restart</td>
    <td>Restart the current track from the start.</td>
  </tr>
  <tr>
    <td>/skip</td>
    <td>Skips the current track.</td>
  </tr>
  <tr>
    <td>/skipto (number)</td>
    <td>Skips to the given track. You can check /queue for the number of the desired track.</td>
  </tr>
  <tr>
    <td>/stop</td>
    <td>Stops the player and clears the queue.</td>
  </tr>
</table>

There are also many UI controls, that are attached to the messages of the bot.
