package fire.meteor.modules;

import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;

import java.util.List;

import fire.meteor.Addon;

public class AutoReply extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgFetch = settings.createGroup("Message to Fetch");
    private final SettingGroup sgReply = settings.createGroup("Message to Reply");


    private final Setting<Integer> delay = sgGeneral.add(new IntSetting.Builder()
            .name("delay")
            .description("Time between the reply in ms.")
            .defaultValue(1169)
            .min(0)
            .sliderMax(10000)
            .build());
    
    private final Setting<Boolean> remove = sgGeneral.add(new BoolSetting.Builder()
    		.name("remove-quotations")
    		.description("Auto remove quotation marks (\"'`))")
    		.defaultValue(true)
    		.build());
    
    private final Setting<List<String>> msgFetch = sgFetch.add(new StringListSetting.Builder()
    		.name("fetch-messages")
    		.description("Each fetch are linked with each reply (1 -> 1; 2 -> 2; ...)")
    		.defaultValue("Hello !")
    		.build());
    
    private final Setting<List<String>> msgReply = sgReply.add(new StringListSetting.Builder()
    		.name("reply-messages")
    		.description("Each fetch are linked with each reply (1 -> 1; 2 -> 2; ...)")
    		.defaultValue("Hi !")
    		.build());

    public AutoReply() {
        super(Addon.CATEGORY, "Auto-Reply", "Fetch messages from chat to automatically respond to them.");
    }
    
    @Override
    public void onActivate() {
    		
    	
    }
            
}
