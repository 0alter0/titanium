# Titanium
My take on Tungsten

# What?
A fork from the Tungsten project

# New features:
Player hologram

# In development:
Break/place blocks - won't trigger in game but is in code

# TODO:
Add new rendering properties for new features (if they require)

Add break/place mechanics - Unsure if it should be rule based or generate what to do off of the environment. Rule based = perfect execution but wouldn't be fit. Generated = might not always work but would fit

Add "Optimize" Mode - Makes the pathfinding algorithm go for the quickest path it can, by doing trial and error.

Add 45 Strafe moves - Does the insane 45 strafe.

Fix nodes running out when there is a possible path

Add Triple Neo jumps - Gives the pathfinder the ability to perform Triple Neos

Add interact - Gives the pathfinder the ability to interact with blocks if they will aid in efficiency

Add bounce support - Gives the pathfinder the ability to know how to use slime blocks or beds if present

Add Auto-Clutch - Gives the pathfinder the ability to place down a water bucket, slime block, bed, ladder, web, etc... which will dynamically change depending on the situation (Example: Path requires a way to get down safely, so place down a cobweb right before collision with the floor. Then the path requires a way to get across a 10 block gap, a slime block would get you there but it's not there so place it). This would be build off of break/place mechanics.

Add follow command - Follows players or entities

Add full TAS level parkour - Execute some of the HARDEST jumps flawlessly using perfectly timed inputs

Add Multi PV - Similar to a chess engine, run multiple paths at the same time if they are the best, kill them if they may be bad in the future and make a new path. So calculate multiple paths, see which ones do the best, if one fails, kill it, and replace it, repeat.

Add Prune - Prune paths that lead to failure (Using the detection algorithm from the Kill Line function) and focus on other things.

Edit Pathfinding Logic (The big one) - By using the way chess engines get depth, we can use LMR (Late Move Reduction) to figure out paths most likely better than the rest. Using LMR, allocate reduced effort to less-promising paths, but keep them tracked in saved lines (paths are not locked; they can be updated or replaced if better options are found later), but put way more effort into the saved lines. Then expand upon those moves with Branch and Bound to find potential better ones (if none could be found, move on to the next node, expand nodes while pruning paths worse than the best found so far; paths can still be updated if a better option appears or a failure occurs later). Once it makes the best path it could find, compare all fully expanded paths and select the fastest valid one for execution. Another thing to add: edit the basic node "grid" that the bot tries to follow by making it dynamic. Not applying everything said above but by just changing it to something else if the bot couldn't find a path.

Add Neural Network - Allows the bot to learn using a neural network. This would basically be trained off of the user using the bot. It needs to log the good paths and nodes, then use that data to find better paths faster (Only being implemented after the Pathfinding Logic edit)

Add Custom Node Placement - Allows the user to set the basic A* node blocks so the parkour pathfinder knows where to do something.
