package fire.meteor.modules;

import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.events.entity.player.PlayerMoveEvent;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.meteorclient.systems.modules.player.AutoClicker;
import meteordevelopment.orbit.EventHandler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import fire.meteor.Addon;

public class AutoFarm extends Module {
	private Boolean once = false;
	
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    
    private final Setting<Directions> direction = sgGeneral.add(new EnumSetting.Builder<Directions>()
    		.name("direction-to-walk")
    		.description("The directions you will walk.")
    		.defaultValue(Directions.NONE)
    		.build());
    
    private final Setting<Boolean> click = sgGeneral.add(new BoolSetting.Builder()
    		.name("hold-left-click")
    		.description("Hold Left click or not.")
    		.defaultValue(false)
    		.build());
    
    
    private final SettingGroup sgWarp = settings.createGroup("Auto Warp");
  
    private final Setting<String> coords = sgWarp.add(new StringSetting.Builder()
    		.name("coords")
    		.description("X ; Y ; Z")
    		.defaultValue("0 ; 0 ; 0")
    		.build());
    
    private final Setting<Boolean> sendCommand = sgWarp.add(new BoolSetting.Builder()
    		.name("send-command-on-coords")
    		.description("Send a command or not when the coords matches.")
    		.defaultValue(false)
    		.build());
    
    private final Setting<String> command = sgWarp.add(new StringSetting.Builder()
    		.name("command")
    		.description("Just a command")
    		.defaultValue("/is warp")
    		.visible(() -> sendCommand.get().booleanValue())
    		.build());
    
    private final Setting<Integer> delaySend = sgWarp.add(new IntSetting.Builder()
    		.name("delay-send")
    		.description("The delay after it send the command (in milliseconds).")
    		.min(0)
    		.defaultValue(500)
    		.sliderMax(10000)
    		.visible(() -> sendCommand.get().booleanValue())
    		.build());
    
    private final Setting<Boolean> pressGUI = sgWarp.add(new BoolSetting.Builder()
    		.name("press-in-gui")
    		.description("Press or not in a GUI after the command.")
    		.defaultValue(false)
    		.visible(() -> sendCommand.get().booleanValue())
    		.build());
    
    private final Setting<Integer> id = sgWarp.add(new IntSetting.Builder()
    		.name("id")
    		.description("The id of the slot to press.")
    		.min(0)
    		.defaultValue(20)
    		.sliderMax(89)
    		.visible(() -> pressGUI.get().booleanValue())
    		.build());
    
    private final Setting<Integer> delayClick = sgWarp.add(new IntSetting.Builder()
    		.name("delay-click")
    		.description("The delay after it click on the slot (in milliseconds).")
    		.min(0)
    		.defaultValue(500)
    		.sliderMax(10000)
    		.visible(() -> pressGUI.get().booleanValue())
    		.build());

    public AutoFarm() {
        super(Addon.CATEGORY, "Auto-Farm", "On specific coords, send a command and then press somewhere in the GUI.");
    }
    
    @Override
    public void onActivate() {
    	switch(direction.get()) {
	    	case NONE -> {
	    		mc.options.forwardKey.setPressed(false);
	    		mc.options.rightKey.setPressed(false);
	    		mc.options.backKey.setPressed(false);
	    		mc.options.leftKey.setPressed(false);
	    	}
	    	case FORWARD -> mc.options.forwardKey.setPressed(true);
	    	case FORWARD_RIGHT -> {
	    		mc.options.forwardKey.setPressed(true);
	    		mc.options.rightKey.setPressed(true);
	    	}
	    	case RIGHT -> mc.options.rightKey.setPressed(true);
	    	case BACK_RIGHT -> {
	    		mc.options.backKey.setPressed(true);
	    		mc.options.rightKey.setPressed(true);
	    	}
	    	case BACK -> mc.options.backKey.setPressed(true);
	    	case BACK_LEFT -> {
	    		mc.options.backKey.setPressed(true);
	    		mc.options.leftKey.setPressed(true);
	    	}
	    	case LEFT -> mc.options.leftKey.setPressed(true);
	    	case FORWARD_LEFT -> {
	    		mc.options.forwardKey.setPressed(true);
	    		mc.options.leftKey.setPressed(true);
	    	}
    	}
    	
    	if(click.get().booleanValue()) mc.options.attackKey.setPressed(true);
    	else mc.options.attackKey.setPressed(false);
    }
    
    @Override
    public void onDeactivate() {
        mc.options.attackKey.setPressed(false);
    	mc.options.forwardKey.setPressed(false);
		mc.options.rightKey.setPressed(false);
		mc.options.backKey.setPressed(false);
		mc.options.leftKey.setPressed(false);
    }
        
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
    	
    	if(sendCommand.get().booleanValue()) {
    		double x = Math.round(mc.player.getX());
        	double y = Math.round(mc.player.getY());
        	double z = Math.round(mc.player.getZ());
        	    	
        	String[] where = coords.get().split(";");
        	double _x = Double.parseDouble(where[0]);
        	double _y = Double.parseDouble(where[1]);
        	double _z = Double.parseDouble(where[2]);
        	   	
        	if(x == _x && y == _y && z == _z && !once) {
        		once = true;
        		toggle();
        		ChatUtils.sendPlayerMsg(command.get());
        		if(pressGUI.get().booleanValue()) {
        			ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
                    executor.schedule(() -> {   
                    	InvUtils.click().slotId(id.get());
                    	once = false;
                    	executor.schedule(() -> {
                    		toggle();
                    		System.out.println(mc.options.attackKey.isPressed());
                    		executor.shutdown();
                    	}, delayClick.get(), TimeUnit.MILLISECONDS);
                    }, delaySend.get(), TimeUnit.MILLISECONDS);
        		}
        	}
    	}
    }
    
    public enum Directions {
    	NONE,
    	FORWARD,
    	FORWARD_RIGHT,
    	RIGHT,
    	BACK_RIGHT,
    	BACK,
    	BACK_LEFT,
    	LEFT,
    	FORWARD_LEFT
    }
}
