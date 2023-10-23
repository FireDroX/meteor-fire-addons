package fire.meteor.mixins;

import fire.meteor.modules.AutoReply;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.network.message.MessageHandler;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Mixin(MessageHandler.class)
public class MessageHandlerMixin {
    @SuppressWarnings("unchecked")
	@Inject(method = "onGameMessage", at = @At("HEAD"), cancellable = true)
    private void onGameMessage(Text message, boolean overlay, CallbackInfo ci) {
    	   	
        if (overlay) {
            return;
        }
        
        AutoReply autoReply = Modules.get().get(AutoReply.class);
               
        if(autoReply.isActive()) {
        	List<String> messages = new ArrayList<String>();
            
            message.withoutStyle().forEach(m -> {
            	int startIndex = m.toString().indexOf("literal{");
            	int endIndex = m.toString().indexOf("}", startIndex);
            	
            	if(startIndex >= 0 && endIndex >= 0) {
            		String literalText = m.toString().substring(startIndex + 8, endIndex);
            		messages.add(literalText);
            	}
            });
            
            StringBuilder newMsg = new StringBuilder(String.join("", messages).toString());
                    
            int delayMilliseconds = Integer.parseInt(autoReply.settings.get("delay").get().toString());
            List<String> msgFetch = (List<String>)autoReply.settings.get("fetch-messages").get();
            List<String> msgReply = (List<String>)autoReply.settings.get("reply-messages").get();
            Boolean quote = (Boolean)autoReply.settings.get("remove-quotations").get();
            
            if(quote.booleanValue()) newMsg = new StringBuilder(newMsg.toString().replaceAll("[\"'‘’“”«»„”`]", ""));
            
            String msg = newMsg.toString();
            System.out.println(msg);
                        
            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

            ScheduledFuture<?> scheduledFuture = executor.schedule(() -> {
            	// Handle Reply
                
            	if(msgFetch.size() == msgReply.size()) {
            		msgFetch.forEach(m -> {
            			String fetchMsg = m.toString();
            			String replyMsg = msgReply.get(msgFetch.indexOf(fetchMsg)).toString();
            			
            			if(msg.contains(fetchMsg) && fetchMsg != "" && replyMsg != "") {
            				System.out.println("Step 1");
            				StringBuilder endMsg = new StringBuilder(replyMsg);
            				if (endMsg.toString().split("%(.*?)%").length != 1) {
            				    String script = endMsg.toString().split("%")[1];
            				    String[] args = script.split("_");
            				    StringBuilder result = new StringBuilder("undefined");
            				    
            				    switch (args[0]) {
            				    	case "ARGS" -> {
            				    		if(!args[1].isEmpty() && args[1].matches("[0-9]")) {
            				    			result = new StringBuilder(msg.split(" ")[Integer.parseInt(args[1])]);
            				    		}
            				    	}
            				    	
            				    	case "ARGS-INVERSED" -> {
            				    		if(!args[1].isEmpty() && args[1].matches("[0-9]")) {
            				    			result = new StringBuilder(msg.split(" ")[Integer.parseInt(args[1])]);
            				    			
            				    			String input = result.toString();
            				    			
    			    						result = new StringBuilder();
    			    						for (int i = input.length() - 1; i >= 0; i--) {
    			    					        result.append(input.charAt(i));
    			    					    }
            				    		}
			    					}
            				    }
            				    
            				    endMsg = new StringBuilder(endMsg.toString().replaceAll("%(.*?)%", result.toString()));          				    
            				}
            				ChatUtils.sendPlayerMsg(endMsg.toString());
            			}
            		});     		
            	} else {
            		autoReply.error("You need to have the same amount of messages to Fetch and messages to Reply for the module to work.");
            		autoReply.info("Remember ! The first Fetch will only be replied with the first Reply, the second only with the second, and so on.");
            	}
            	               
            }, delayMilliseconds, TimeUnit.MILLISECONDS);
            executor.shutdown();      
        }
    }
}

