# Chat Plays Minecraft
let your stream chat play minecraft\
(very creative name, I know)

This is my first minecraft mod and GitHub repository :)\
the code might not be the best(but atleast it works)

<b>This project REQUIRES [Fabric API](https://modrinth.com/mod/fabric-api) to work</b>

This project uses [YoutubeLiveChat library by kusaanko](https://github.com/kusaanko/YouTubeLiveChat)

## How to use?
Drop the mod into your minecraft 'mods' folder, and run minecraft with fabric loader.\
Then in game press the M key, input your twitch username or youtube live id, press connect and you're good to go.\
You can then press the R button to toggle chat controlling.

## Commands
When chat controlling is enabled chat can type different messages to do different stuff.\
The commands are NOT case sensitive.

### Movement
The movement commands consist of: 1.the key 2.the amount of ticks (20 ticks = 1 second) that key will be pressed.\
The ticks value is NOT required
```
w [ticks]
a [ticks]
s [ticks]
d [ticks]
jump [ticks]
sprint [ticks] // spr [ticks]
sneak [ticks] // snk [ticks]
```

### Player rotation
* `ry [degrees]` to rotate left/right
* `rp [degrees]` to rotate up/down

`[degrees]` can be any number in range of [-360, 360]

### Place/Use/Break
You can send:
* `place` to simulate a single right click
* `use [ticks]` to simulate right click being pressed for [ticks] (1sec = 20ticks). Defaults to 40 ticks.
* `break [ticks]` to simulate left click being pressed for [ticks] (1sec = 20ticks). Defaults to 200 ticks.

### Inventory management
You can open/close inventory by using `e` command. It does have 5 second chat wide cooldown though.

When in inventory you will notice the slots have a number written over them.\
You are able to switch items in slots by using `i [firstslot] [secondslot]` where firstslot and secondslot are the numbers that are shown over slots.

When you are in screen that supports crafting you can use `c [searchprompt]` to craft.\
In [searchprompt] you can type the item name, and it will automatically craft the first item that matches your [searchprompt] just like in the recipe book.

### Other
* `respawn` to... well... respawn.
* `h [slot]` to select a slot on hotbar.