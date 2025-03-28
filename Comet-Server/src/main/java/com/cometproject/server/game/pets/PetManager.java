package com.cometproject.server.game.pets;

import com.cometproject.api.game.pets.IPetData;
import com.cometproject.api.game.pets.IPetRace;
import com.cometproject.api.utilities.Initialisable;
import com.cometproject.server.game.pets.data.PetData;
import com.cometproject.server.game.pets.data.PetMonsterPlantData;
import com.cometproject.server.game.pets.data.PetSpeech;
import com.cometproject.server.game.pets.races.PetBreedLevel;
import com.cometproject.server.game.pets.races.PetRace;
import com.cometproject.server.game.pets.races.plants.PetMonsterPlant;
import com.cometproject.server.game.pets.races.plants.PetMonsterPlantColor;
import com.cometproject.server.storage.queries.pets.PetDao;
import com.google.common.collect.Maps;
import gnu.trove.map.hash.THashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


public class PetManager implements Initialisable {
    private static PetManager petManagerInstance;
    private final Map<Integer, IPetData> pendingPetDataSaves = Maps.newConcurrentMap();
    private final Logger log = LogManager.getLogger(PetManager.class.getName());
    private List<PetRace> petRaces;
    private Map<Integer, PetSpeech> petMessages;
    private Map<String, String> transformablePets;
    private Map<Integer, Map<PetBreedLevel, Set<Integer>>> petBreedPallets;
    private ArrayList<PetMonsterPlant> monsterPlantBodies;
    private ArrayList<PetMonsterPlantColor> monsterPlantColors;

    public static PetManager getInstance() {
        if (petManagerInstance == null)
            petManagerInstance = new PetManager();

        return petManagerInstance;
    }

    public static int maxEnergy(int level) {
        //TODO: Add energy calculation.
        return 100 * level;
    }

    @Override
    public void initialize() {
        this.loadPetRaces();
        this.loadPetBreedPallets();
        this.loadPetSpeech();
        this.loadTransformablePets();
        this.loadMonsterPlantsRaces();

        // Set up the queue for saving pet data
        // CometThreadManager.getInstance().executePeriodic(this::savePetStats, 1000, 1000, TimeUnit.MILLISECONDS);

        log.info("PetManager initialized");
    }

    public void loadPetRaces() {
        if (this.petRaces != null) {
            this.petRaces.clear();
        }

        try {
            this.petRaces = PetDao.getRaces();

            log.info("Loaded " + this.petRaces.size() + " pet races");
        } catch (Exception e) {
            log.error("Error while loading pet races", e);
        }
    }

    public void loadPetBreedPallets() {
        if (this.petBreedPallets != null) {
            this.petBreedPallets.clear();
        }

        try {
            this.petBreedPallets = PetDao.getPetBreedPallets();

            log.info("Loaded " + this.petBreedPallets.size() + " pet breed pallet sets");
        } catch (Exception e) {
            log.error("Error while loading pet breed pallets", e);
        }
    }

    public void loadPetSpeech() {
        if (this.petMessages != null) {
            this.petMessages.clear();
        }

        try {
            AtomicInteger petSpeechCount = new AtomicInteger(0);
            this.petMessages = PetDao.getMessages(petSpeechCount);

            log.info("Loaded " + this.petMessages.size() + " pet message sets and " + petSpeechCount.get() + " total messages");
        } catch (Exception e) {
            log.error("Error while loading pet messages");
        }
    }

    public void loadTransformablePets() {
        if (this.transformablePets != null) {
            this.transformablePets.clear();
        }

        try {
            this.transformablePets = PetDao.getTransformablePets();

            log.info("Loaded " + this.transformablePets.size() + " transformable pets");
        } catch (Exception e) {
            log.error("Error while loading transformable pets");
        }
    }

    public int validatePetName(String petName) {
        String pattern = "^[a-zA-Z0-9]*$";

        if (petName.length() <= 0) {
            return 1;
        }

        if (petName.length() > 16) {
            return 2;
        }

        if (!petName.matches(pattern)) {
            return 3;
        }

        // WORD FILTER

        return 0;
    }

    public List<IPetRace> getRacesByRaceId(int raceId) {
        List<IPetRace> races = new ArrayList<>();

        for (PetRace race : this.getPetRaces()) {
            if (raceId == race.getRaceId())
                races.add(race);
        }

        return races;
    }

    public void loadMonsterPlantsRaces() {
        this.monsterPlantBodies = new ArrayList();
        this.monsterPlantBodies.add(new PetMonsterPlant(1, "Amnesia", 0, PetMonsterPlantData.timeToLive, 300));
        this.monsterPlantBodies.add(new PetMonsterPlant(5, "Supersilver", 1, PetMonsterPlantData.timeToLive, 300));
        this.monsterPlantBodies.add(new PetMonsterPlant(2, "Cookies", 2, PetMonsterPlantData.timeToLive, 300));
        this.monsterPlantBodies.add(new PetMonsterPlant(3, "Stumpy", 3, PetMonsterPlantData.timeToLive, 300));
        this.monsterPlantBodies.add(new PetMonsterPlant(4, "Calamity", 4, PetMonsterPlantData.timeToLive, 480));
        this.monsterPlantBodies.add(new PetMonsterPlant(9, "Blueberry", 5, PetMonsterPlantData.timeToLive, 480));
        this.monsterPlantBodies.add(new PetMonsterPlant(6, "Shroomer", 6, PetMonsterPlantData.timeToLive, 480));
        this.monsterPlantBodies.add(new PetMonsterPlant(7, "Moby Dick", 7, PetMonsterPlantData.timeToLive, 960));
        this.monsterPlantBodies.add(new PetMonsterPlant(10, "AK 47", 8, PetMonsterPlantData.timeToLive, 960));
        this.monsterPlantBodies.add(new PetMonsterPlant(11, "Skywalker", 8, PetMonsterPlantData.timeToLive, 1920));
        this.monsterPlantBodies.add(new PetMonsterPlant(12, "Gorilla Glue", 9, PetMonsterPlantData.timeToLive, 1920));
        this.monsterPlantBodies.add(new PetMonsterPlant(8, "Hindu", 10, PetMonsterPlantData.timeToLive, 1920));

        this.monsterPlantColors = new ArrayList();
        this.monsterPlantColors.add(new PetMonsterPlantColor("OG", 0));
        this.monsterPlantColors.add(new PetMonsterPlantColor("Gelatto", 1));
        this.monsterPlantColors.add(new PetMonsterPlantColor("Goat", 2));
        this.monsterPlantColors.add(new PetMonsterPlantColor("G13", 3));
        this.monsterPlantColors.add(new PetMonsterPlantColor("Kush", 4));
        this.monsterPlantColors.add(new PetMonsterPlantColor("Incarnatus", 5));
        this.monsterPlantColors.add(new PetMonsterPlantColor("Amethyst", 6));
        this.monsterPlantColors.add(new PetMonsterPlantColor("Haze", 7));
        this.monsterPlantColors.add(new PetMonsterPlantColor("Atamasc", 8));
        this.monsterPlantColors.add(new PetMonsterPlantColor("Azureus", 9));
        this.monsterPlantColors.add(new PetMonsterPlantColor("Cyaneus", 10));
    }

    public ArrayList<PetMonsterPlant> getMonsterPlantBodies() {
        return this.monsterPlantBodies;
    }

    public ArrayList<PetMonsterPlantColor> getMonsterPlantColors() {
        return this.monsterPlantColors;
    }

    public List<PetRace> getPetRaces() {
        return this.petRaces;
    }

    public PetSpeech getSpeech(int petType) {
        return this.petMessages.get(petType);
    }

    public Map<String, String> getTransformablePets() {
        return transformablePets;
    }

    public String getTransformationData(String type) {
        return this.transformablePets.get(type);
    }

    public Map<Integer, Map<PetBreedLevel, Set<Integer>>> getPetBreedPallets() {
        return petBreedPallets;
    }

    //    public String[] getSpeech(int petType) {
//        return this.petSpeech.get(petType);
//    }
}
