package com.cometproject.server.modules;

import com.cometproject.api.config.ModuleConfig;
import com.cometproject.api.events.EventHandler;
import com.cometproject.api.game.GameContext;
import com.cometproject.api.modules.BaseModule;
import com.cometproject.api.server.IGameService;
import com.cometproject.api.utilities.Initialisable;
import com.cometproject.api.utilities.JsonUtil;
import com.cometproject.game.groups.GroupsModule;
import com.cometproject.game.rooms.RoomsModule;
import com.cometproject.server.modules.events.EventHandlerService;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.CharStreams;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ModuleManager implements Initialisable {
    private static final Logger log = LogManager.getLogger(ModuleManager.class.getName());
    private static ModuleManager moduleManagerInstance;
    private final EventHandler eventHandler;
    private final CometGameService gameService;

    private Map<String, BaseModule> modules;

    public ModuleManager() {
        this.eventHandler = new EventHandlerService();
        this.gameService = new CometGameService(this.eventHandler);
    }

    public static ModuleManager getInstance() {
        if (moduleManagerInstance == null) {
            moduleManagerInstance = new ModuleManager();
        }

        return moduleManagerInstance;
    }

    @Override
    public void initialize() {
        if (this.modules != null) {
            this.modules.clear();
        } else {
            this.modules = new ConcurrentHashMap<>();
        }

        ModuleManager.getInstance().getEventHandler().initialize();

        this.loadCoreModule(GroupsModule.class);
        this.loadCoreModule(RoomsModule.class);

//        this.loadModules();

//        for (String moduleName : this.findModules()) {
//            try {
//                this.loadModule(moduleName);
//            } catch (Exception e) {
//                log.warn("Error while loading module: " + moduleName, e);
//            }
//        }
    }

    private void loadCoreModule(Class<? extends BaseModule> moduleClass) {
        try {
            Constructor<? extends BaseModule> ctor = moduleClass.getConstructor(ModuleConfig.class, IGameService.class);

            // Null module config, it's a system module so no config needed.
            BaseModule cometModule = ctor.newInstance(null, this.gameService);

            cometModule.loadModule();

            this.modules.put(moduleClass.getSimpleName(), cometModule);
        } catch (Exception e) {
            log.error("Failed to load system module: " + moduleClass.getName(), e);
        }
    }

    public void setupModules() {
        for (BaseModule baseModule : this.modules.values()) {
            baseModule.setup();

            baseModule.initialiseServices(GameContext.getCurrent());
        }
    }

    private List<CometModule> loadModules() throws IOException {

        final String configContent;
        try (var inputStream = Files.newInputStream(Paths.get("./config/modules.json"))) {
            configContent = new String(inputStream.readAllBytes());
        }

        final ModulesConfig modulesConfig = JsonUtil.getInstance().fromJson(configContent, ModulesConfig.class);

        final List<CometModule> cometModules = Lists.newArrayList();

        for (CometModule module : modulesConfig.getModules()) {
            try {
                loadModule(module);
                cometModules.add(module);
            } catch (Exception e) {
                log.error("Failed to load module: " + module.getAlias(), e);
            }
        }

        return cometModules;
    }
//
//    private List<String> findModules() {
//       List<String> results = new ArrayList<>();
//
//        File[] files = new File("./modules").listFiles();
//
//        if (files == null) return results;
//
//        for (File file : files) {
//            if (file.isFile() && file.getName().endsWith(".jar")) {
//                results.add(file.getName());
//            }
//        }
//
//        return results;
//    }

    private void loadModule(CometModule module) throws Exception {
        final File file = new File(module.getPath());

        URLClassLoader loader = URLClassLoader.newInstance(
                new URL[]{file.toURI().toURL()}
        );

        InputStream configJsonLocation = loader.getResourceAsStream("module.json");
        if (configJsonLocation == null) {
            throw new Exception("module.json does not exist");
        }

        final String configContent = CharStreams.toString(new InputStreamReader(configJsonLocation, Charsets.UTF_8));

        final ModuleConfig moduleConfig = JsonUtil.getInstance().fromJson(configContent, ModuleConfig.class);
        if (this.modules.containsKey(moduleConfig.getName())) {
            if (!this.modules.get(moduleConfig.getName()).getConfig().getVersion().equals(moduleConfig.getVersion())) {
                log.warn("Modules with same name but different version was detected: " + moduleConfig.getName());
            }

            return;
        }

        log.info("Loaded module: " + moduleConfig.getName() + ", alias: " + module.getAlias());

        Class<?> clazz = Class.forName(moduleConfig.getEntryPoint(), true, loader);
        Class<? extends BaseModule> runClass = clazz.asSubclass(BaseModule.class);
        Constructor<? extends BaseModule> ctor = runClass.getConstructor(ModuleConfig.class, IGameService.class);

        BaseModule cometModule = ctor.newInstance(moduleConfig, this.gameService);

        cometModule.loadModule();

        //module.setClassLoader(loader);
        this.modules.put(moduleConfig.getName(), cometModule);

        loader.close();
    }

    public EventHandler getEventHandler() {
        return eventHandler;
    }

    private class ModulesConfig {
        private final List<CometModule> modules;

        public ModulesConfig(final List<CometModule> modules) {
            this.modules = modules;
        }

        public List<CometModule> getModules() {
            return modules;
        }
    }
}
