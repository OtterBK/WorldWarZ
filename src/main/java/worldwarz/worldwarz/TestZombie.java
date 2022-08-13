package worldwarz.worldwarz;

import net.minecraft.server.v1_12_R1.EntityZombie;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;

public class TestZombie extends EntityZombie {

    public TestZombie(World world){
        super(((CraftWorld) world).getHandle());
    }

    public TestZombie(Location location){
        super(((CraftWorld) location.getWorld()).getHandle());
        this.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

}
