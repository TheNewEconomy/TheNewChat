# Creating a Chat Channel
This will introduce you to how to successfully create a chat channel for The New Chat using the chats.yml configuration
file.

## The format
The format for a channel in chats.yml looks like the following:
```yaml
  Sample: #A random unqiue identifier.
    Commands: #The commands that will be used to join/send messages to this channel.
    - your
    - commands
    - here
    Handler: handler name #The name of the handler for your channel use https://github.com/TheNewEconomy/TheNewChat/blob/master/Variables.md
    Type: type name #The name of the type for your your channel, found under the handler at https://github.com/TheNewEconomy/TheNewChat/blob/master/Variables.md
    Format: "Your format here" #The format of your channel using core/handler-specific variables, and/or PAPI Placeholders.
```

## Making a /townchat command for Towny Servers
In this example we'll make a simple Town Chat Channel for a towny server.

Using [our variables page](https://github.com/TheNewEconomy/TheNewChat/blob/master/Variables.md) will tell you the handler for Towny is "towny". From
there we find the correct type we need, which is town.

As seen here:
![towny image](http://prntscr.com/l51iad "Towny Image")

```yaml
Chats:
  Town:
    Handler: towny
    Type: town
```

We'll also want the channel to be accessible using the /tc, and /townchat commands.
```yaml
Chats:
  Town:
    Commands:
    - townchat
    - tc
    Handler: towny
    Type: town
```

From here we'll have to decide what format to use. For this we'll need to reference our [variables page again](https://github.com/TheNewEconomy/TheNewChat/blob/master/Variables.md) for proper
colour codes, and variables. For our format we'll want something like \[player name\]: message. This leads us to our final
configuration as seen below.
```yaml
Chats:
  Town:
    Commands:
    - townchat
    - tc
    Handler: towny
    Type: town
    Format: "<blue>[<white>$display<blue>]<white>: $message"
```

From here we may use some of the optional configurations as follows:
```yaml
    Ignorable: true #Whether or not players may ignore this channel using /ignorechannel
    WorldBased: false #Whether chat in this channel is only sent to players in the same world.
    Radial: false #Whether chat in this channel is based on the player's location.
    Radius: 20 #The max distance away a player may be and here this channel's chat if Radial is true.
    Permission: "examplechat.chat" #The permission node required to use this channel.
```