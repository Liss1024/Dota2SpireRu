package dota2Spire;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import dota2Spire.potions.Clarity;
import dota2Spire.potions.HealingSalve;
import dota2Spire.relics.*;
import dota2Spire.util.TextureLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

@SpireInitializer
public class Dota2Spire implements EditRelicsSubscriber, EditCardsSubscriber,
        EditStringsSubscriber, EditKeywordsSubscriber, PostInitializeSubscriber {

    public static final Logger logger = LogManager.getLogger(Dota2Spire.class.getName());
    private static String modID;
    // Mod-settings settings. This is if you want an on/off savable button
    public static Properties theDefaultDefaultSettings = new Properties();
    public static final String ENABLE_PLACEHOLDER_SETTINGS = "enablePlaceholder";
    private static final String MODNAME = "Dota2Spire";
    private static final String AUTHOR = "tt"; // And pretty soon - You!
    private static final String DESCRIPTION = "Dota2Spire.";
    public static boolean enablePlaceholder = true;

    public static void initialize() {
        logger.info("========================= Initializing Dota2Spire Mod. =========================");
        Dota2Spire mod = new Dota2Spire();
    }

    public static String makeCardPath(String resourcePath) {
        return getModID() + "Resources/images/cards/" + resourcePath;
    }

    public static String makeRelicPath(String resourcePath) {
        return getModID() + "Resources/images/relics/" + resourcePath;
    }

    public static String makePowerPath(String resourcePath) {
        return getModID() + "Resources/images/powers/" + resourcePath;
    }

    public static String makeRelicOutlinePath(String resourcePath) {
        return getModID() + "Resources/images/relics/outline/" + resourcePath;
    }

    public static String makeEventPath(String resourcePath) {
        return getModID() + "Resources/images/events/" + resourcePath;
    }

    public Dota2Spire() {
        logger.info("Subscribe to BaseMod hooks");
        BaseMod.subscribe(this);
        setModID("Dota2Spire");
        logger.info("Adding mod settings");
        // This loads the mod settings.
        // The actual mod Button is added below in receivePostInitialize()
        theDefaultDefaultSettings.setProperty(ENABLE_PLACEHOLDER_SETTINGS, "FALSE"); // This is the default setting. It's actually set...
        try {
            SpireConfig config = new SpireConfig("Dota2Spire", "Dota2SpireConfig", theDefaultDefaultSettings); // ...right here
            // the "fileName" parameter is the name of the file MTS will create where it will save our setting.
            config.load(); // Load the setting and set the boolean to equal it
            enablePlaceholder = config.getBool(ENABLE_PLACEHOLDER_SETTINGS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("Done adding mod settings");
        logger.info("Done subscribing");
    }

    @Override
    public void receiveEditRelics() {
        logger.info("Adding relics");

        // This adds a relic to the Shared pool. Every character can find this relic.
        BaseMod.addRelic(new EchoSabre(), RelicType.SHARED);
        BaseMod.addRelic(new Butterfly(), RelicType.SHARED);
        BaseMod.addRelic(new Desolator(), RelicType.SHARED);
        BaseMod.addRelic(new MagicBottle(), RelicType.SHARED);
        BaseMod.addRelic(new HeartOfTarrasque(), RelicType.SHARED);
        BaseMod.addRelic(new Satanic(), RelicType.SHARED);
        BaseMod.addRelic(new MaskOfMadness(), RelicType.SHARED);
        BaseMod.addRelic(new ShadowBlade(), RelicType.SHARED);
        BaseMod.addRelic(new LotusOrb(), RelicType.SHARED);
        BaseMod.addRelic(new LinkenSphere(), RelicType.SHARED);
        BaseMod.addRelic(new HandOfMidas(), RelicType.SHARED);
        BaseMod.addRelic(new BattleFury(), RelicType.SHARED);
        BaseMod.addRelic(new UrnOfShadows(), RelicType.SHARED);
        BaseMod.addRelic(new DrumOfEndurance(), RelicType.SHARED);
        BaseMod.addRelic(new Bloodstone(), RelicType.SHARED);
        BaseMod.addRelic(new RefresherOrb(), RelicType.SHARED);
        BaseMod.addRelic(new MonkeyKingBar(), RelicType.SHARED);
        BaseMod.addRelic(new Vanguard(), RelicType.SHARED);
        BaseMod.addRelic(new BladeMail(), RelicType.SHARED);
        BaseMod.addRelic(new ArmletOfMordiggian(), RelicType.SHARED);
        BaseMod.addRelic(new Dagon(), RelicType.SHARED);
        BaseMod.addRelic(new CrimsonGuard(), RelicType.SHARED);
        BaseMod.addRelic(new ShivaGuard(), RelicType.SHARED);
        BaseMod.addRelic(new AssaultCuirass(), RelicType.SHARED);
        BaseMod.addRelic(new MantaStyle(), RelicType.SHARED);
        BaseMod.addRelic(new Mjollnir(), RelicType.SHARED);
        BaseMod.addRelic(new EyeOfSkadi(), RelicType.SHARED);
        BaseMod.addRelic(new DivineRapier(), RelicType.SHARED);
        BaseMod.addRelic(new BlackKingBar(), RelicType.SHARED);
        BaseMod.addRelic(new PhaseBoots(), RelicType.SHARED);
        BaseMod.addRelic(new PowerTreads(), RelicType.SHARED);
        BaseMod.addRelic(new OctarineCore(), RelicType.SHARED);

        System.out.println("Dota2Spire relic start");

        RelicLibrary.commonList.forEach(it -> {
            if (it.relicId.startsWith("Dota2Spire")) {
                System.out.println(it.name + " (" + it.tier.toString() + ")");
                System.out.println(it.getUpdatedDescription().replaceAll("#y", "").replaceAll("#r", "").replaceAll("#b", "").replaceAll(" NL ", ""));
                System.out.println();
            }
        });
        RelicLibrary.uncommonList.forEach(it -> {
            if (it.relicId.startsWith("Dota2Spire")) {
                System.out.println(it.name + " (" + it.tier.toString() + ")");
                System.out.println(it.getUpdatedDescription().replaceAll("#y", "").replaceAll("#r", "").replaceAll("#b", "").replaceAll(" NL ", ""));
                System.out.println();
            }
        });
        RelicLibrary.rareList.forEach(it -> {
            if (it.relicId.startsWith("Dota2Spire")) {
                System.out.println(it.name + " (" + it.tier.toString() + ")");
                System.out.println(it.getUpdatedDescription().replaceAll("#y", "").replaceAll("#r", "").replaceAll("#b", "").replaceAll(" NL ", ""));
                System.out.println();
            }
        });
        RelicLibrary.bossList.forEach(it -> {
            if (it.relicId.startsWith("Dota2Spire")) {
                System.out.println(it.name + " (" + it.tier.toString() + ")");
                System.out.println(it.getUpdatedDescription().replaceAll("#y", "").replaceAll("#r", "").replaceAll("#b", "").replaceAll(" NL ", ""));
                System.out.println();
            }
        });

        // Mark relics as seen (the others are all starters so they're marked as seen in the character file
        UnlockTracker.markRelicAsSeen(EchoSabre.ID);
        UnlockTracker.markRelicAsSeen(Butterfly.ID);
        UnlockTracker.markRelicAsSeen(Desolator.ID);
        UnlockTracker.markRelicAsSeen(MagicBottle.ID);
        UnlockTracker.markRelicAsSeen(HeartOfTarrasque.ID);
        UnlockTracker.markRelicAsSeen(Satanic.ID);
        UnlockTracker.markRelicAsSeen(MaskOfMadness.ID);
        UnlockTracker.markRelicAsSeen(ShadowBlade.ID);
        UnlockTracker.markRelicAsSeen(LotusOrb.ID);
        UnlockTracker.markRelicAsSeen(LinkenSphere.ID);
        UnlockTracker.markRelicAsSeen(HandOfMidas.ID);
        UnlockTracker.markRelicAsSeen(BattleFury.ID);
        UnlockTracker.markRelicAsSeen(UrnOfShadows.ID);
        UnlockTracker.markRelicAsSeen(DrumOfEndurance.ID);
        UnlockTracker.markRelicAsSeen(Bloodstone.ID);
        UnlockTracker.markRelicAsSeen(RefresherOrb.ID);
        UnlockTracker.markRelicAsSeen(MonkeyKingBar.ID);
        UnlockTracker.markRelicAsSeen(Vanguard.ID);
        UnlockTracker.markRelicAsSeen(BladeMail.ID);
        UnlockTracker.markRelicAsSeen(ArmletOfMordiggian.ID);
        UnlockTracker.markRelicAsSeen(Dagon.ID);
        UnlockTracker.markRelicAsSeen(CrimsonGuard.ID);
        UnlockTracker.markRelicAsSeen(ShivaGuard.ID);
        UnlockTracker.markRelicAsSeen(AssaultCuirass.ID);
        UnlockTracker.markRelicAsSeen(MantaStyle.ID);
        UnlockTracker.markRelicAsSeen(Mjollnir.ID);
        UnlockTracker.markRelicAsSeen(EyeOfSkadi.ID);
        UnlockTracker.markRelicAsSeen(DivineRapier.ID);
        UnlockTracker.markRelicAsSeen(BlackKingBar.ID);
        UnlockTracker.markRelicAsSeen(PhaseBoots.ID);
        UnlockTracker.markRelicAsSeen(PowerTreads.ID);
        UnlockTracker.markRelicAsSeen(OctarineCore.ID);
        logger.info("Done adding relics!");

        receiveEditPotions();
    }

    private void receiveEditPotions() {
        logger.info("Beginning to edit potions");

        BaseMod.addPotion(HealingSalve.class, CardHelper.getColor(0, 255, 127), CardHelper.getColor(245, 245, 220), CardHelper.getColor(245, 245, 220), HealingSalve.POTION_ID);
        BaseMod.addPotion(Clarity.class, CardHelper.getColor(0, 191, 255), CardHelper.getColor(245, 245, 220), CardHelper.getColor(245, 245, 220), Clarity.POTION_ID);

        logger.info("Done editing potions");
    }

    @Override
    public void receiveEditStrings() {
        logger.info("Beginning to edit strings for mod with ID: " + getModID());
        Settings.GameLanguage language = languageSupport();

        // RelicStrings
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                getModID() + "Resources/localization/" + language.toString().toLowerCase() + "/Dota2Spire-Relic-Strings.json");

        // PowerStrings
        BaseMod.loadCustomStringsFile(PowerStrings.class,
                getModID() + "Resources/localization/" + language.toString().toLowerCase() + "/Dota2Spire-Power-Strings.json");

        // PotionStrings
        BaseMod.loadCustomStringsFile(PotionStrings.class,
                getModID() + "Resources/localization/" + language.toString().toLowerCase() + "/Dota2Spire-Potion-Strings.json");

        // CardStrings
        BaseMod.loadCustomStringsFile(CardStrings.class,
                getModID() + "Resources/localization/" + language.toString().toLowerCase() + "/Dota2Spire-Card-Strings.json");


    }

    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();
        Settings.GameLanguage language = languageSupport();

        String json = Gdx.files.internal(getModID() + "Resources/localization/" + language.toString().toLowerCase() + "/Dota2Spire-Keyword-Strings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);

        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }

    @Override
    public void receivePostInitialize() {
        logger.info("Loading badge image and mod options");
        // Load the Mod Badge
        Texture badgeTexture = TextureLoader.getTexture("Dota2SpireResources/images/Badge.png");
        // Create the Mod Menu
        ModPanel settingsPanel = new ModPanel();

        // Create the on/off button:
        ModLabeledToggleButton enableNormalsButton = new ModLabeledToggleButton("nothing",
                350.0f, 700.0f, Settings.CREAM_COLOR, FontHelper.charDescFont, // Position (trial and error it), color, font
                enablePlaceholder, // Boolean it uses
                settingsPanel, // The mod panel in which this button will be in
                (label) -> {
                }, // thing??????? idk
                (button) -> { // The actual button:

                    enablePlaceholder = button.enabled; // The boolean true/false will be whether the button is enabled or not
                    try {
                        // And based on that boolean, set the settings and save them
                        SpireConfig config = new SpireConfig("Dota2Spire", "Dota2SpireConfig", theDefaultDefaultSettings);
                        config.setBool(ENABLE_PLACEHOLDER_SETTINGS, enablePlaceholder);
                        config.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

        settingsPanel.addUIElement(enableNormalsButton); // Add the button to the settings panel. Button is a go.

        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);
        logger.info("Done loading badge Image and mod options");
    }

    @Override
    public void receiveEditCards() {
        new AutoAdd("Dota2Spire")
                .packageFilter("dota2Spire.cards")
                .setDefaultSeen(true)
                .cards();
    }

    public static void setModID(String ID) {
        modID = ID;
        logger.info("Success! ID is " + modID);
    }

    public static String getModID() {
        return modID;
    }

    public static String makeID(String idText) {
        return getModID() + ":" + idText;
    }

    private Settings.GameLanguage languageSupport() {
        switch (Settings.language) {
            case ZHS:
                return Settings.language;
        }
        return Settings.GameLanguage.ENG;
    }

}
