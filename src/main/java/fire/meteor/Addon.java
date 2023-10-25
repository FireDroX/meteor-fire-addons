package fire.meteor;

import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Items;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fire.meteor.modules.*;

//import java.lang.invoke.MethodHandles;

public class Addon extends MeteorAddon {
    public static final Logger LOG = LoggerFactory.getLogger("FireAddon");
    public static final Category CATEGORY = new Category("Fire", Items.TNT.getDefaultStack());

    @Override
    public void onInitialize() {
        LOG.info("Initializing Fire's Addons.");

        Modules.get().add(new AutoFarm());
        Modules.get().add(new AutoReply());
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
    }

    @Override
    public String getWebsite() {
        return "https://github.com/FireDroX/meteor-fire-addons";
    }

    @Override
    public GithubRepo getRepo() {
        return new GithubRepo("FireDroX", "meteor-fire-addons");
    }

    @Override
    public String getCommit() {
        String commit = FabricLoader
            .getInstance()
            .getModContainer("meteor-fire-addons")
            .get().getMetadata()
            .getCustomValue("github:sha")
            .getAsString();
        return commit.isEmpty() ? null : commit.trim();

    }

    public String getPackage() {
        return "fire.meteor";
    }
}
