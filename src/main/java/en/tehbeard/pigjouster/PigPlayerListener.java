package en.tehbeard.pigjouster;

import org.bukkit.entity.Pig;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerListener;

public class PigPlayerListener extends PlayerListener {

	public void onPlayerInteractEntity(PlayerInteractEntityEvent event){
		//if player is active for making jousting pigs
		if(PigJouster.isPlayer(event.getPlayer())){
			if(event.getRightClicked() instanceof Pig){
				Pig piggy = (Pig)event.getRightClicked();
				if(!PigJouster.isPig(piggy)){
					//ad pig to jousting roster and add a saddle
					PigJouster.addPig(piggy);
					piggy.setSaddle(true);
					event.getPlayer().sendMessage("The pig magically transforms into a jousting pig!");
				}
				else
				{
					//remove pig from jousting roster and remove the saddle
					PigJouster.removePig(piggy);
					piggy.setSaddle(false);
					piggy.eject();
					event.getPlayer().sendMessage("The pig magically transforms into a regular pig!");
				}
				//canel event so they don't jump on the pig
				event.setCancelled(true);
			}
		}
	}
}
