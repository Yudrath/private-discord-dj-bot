<h1 align="center">Self-hosted Private Discord DJ Bot</h1><br>
<p align="center">
A for private use Discord DJ bot written in Java, that is meant to be hosted on your own machine for free.
</p>

# How it works
The bot is meant to be used among friends in a private Discord server. As it relies on being self-hosted, someone in the server has to create an instance of the bot, in other words, self-host it, which is done by just starting the program and clicking the **power on** button. But we want to avoid having more than one person hosting an instance of the bot and we achieve that by utilising two bots: the DJ bot and a helper bot. The helper bot's purpose is to check the DJ bot's status (online/offline) before allowing a user to host the DJ bot. If the status of the DJ bot is offline, only then the option to start it will be present.

# How to set up the bot

*You might want to create an alt Discord account for the next steps, because they involve integrating Discord tokens into the code. If you don't consider sharing the build of the app with friends, this mustn't be an issue, otherwise bear in mind that the application can be reverse engineered and your tokens can be possibly used maliciously. Or you can create a separate text file on your machine to store and load the tokens from.* 

**NEVER SHARE THE APPLICATION WITH PEOPLE YOU DON'T TRUST!**

Steps to 
* You need to create two applications, the first application will be 
