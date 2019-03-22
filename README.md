# CraftShaft X-Ray
Source code on Github: https://github.com/ShabaniAmir/CraftShaftXray

I struggled with finding a proper x-ray detecting plugin for my 1.13.2 server, so I decided to develop one.

> Please note: I am a software developer, but this is my first project with BukkitCraft plugins. If any functionality is missing, or there are any bugs, please contact me through my discord server (link at the bottom)

## How to Install
Simply drag and drop CraftShaftXRay.jar in your plugins folder and restart the server.
This plugin has no dependencies.

## Permissions & Commands
`craftshaft.xray.alert` - Players with this permission receive an alert when a player is suspected of x-raying
`craftshaft.xray.bypass` - Players with this permission will not be monitored
`craftshaft.xray.top` - /csxtop - Get the top 5 highest x-ray ratios of online players
`craftshaft.xray.ratio` - /csxratio <player> - Get diamond to stone ratio of online player
`craftshaft.xray.setratio` - /csxsetratio [ratio] - Get or change the threshold ratio
  
**The way I have the permissions set up on my server is:**
Any staff (Moderators, Admin, etc) that have the power to ban/tempban a player for hacking has the craftshaft.xray.alert, craftshaft.xray.top, and craftshaft.xray.ratio permission.

All staff have the craftshaft.xray.bypass permission. This way if the plugin doesn't bother monitoring their activities.

Only owners or those who understand how this plugin works should have access to craftshaft.xray.setratio

## How the plugin works
Every time a player breaks a stone block or a diamond ore, CraftShaft X-Ray records it. It then calculates the ratio of diamonds to stone mined. I spent some time with a hacked client x-raying and mining diamonds, and found the ratio of hackers is most often between 0.8 to 0.15. The default configuration is 0.8, you can change this with the /csxsetratio command-- bring it higher if you're getting a lot of false positives, or lower if hackers are slipping by.

> Note: In the near future I'll update this plugin to also keep track of other ores, and be more customizable with it's configurations.

**CraftShaft X-Ray automatically deletes it's records of players once they leave the game. It's very efficient and has low impact on servers. It was tested on a local server of a "not-so-good" laptop and it faired well.**

## Changes for next update:
- [ ] Ignore players in creative
- [ ] Have settings for which blocks are most important, not just diamond_ore

*Something you want to see here? contact me or submit a ticket*

## Questions? Suggestions? Let's talk
This plugin was original developed for The Craft Shaft server, and it will be running on there if you want to take a look.

I can be reached on my discord server: [Discord](https://discord.gg/YvBFZJx)
Minecraft server running CraftShaft X-Ray: `mc.thecraftshaft.net:25599`
