package dota2Spire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import dota2Spire.Dota2Spire;
import dota2Spire.util.StringUtil;
import dota2Spire.util.TextureLoader;

import java.util.Random;

import static dota2Spire.Dota2Spire.makeRelicPath;

/**
 * 战鼓
 * 战斗开始时随机获得1力量或1敏捷
 */
public class DrumOfEndurance extends CustomRelic{
    // ID, images, text.
    public static final String ID = Dota2Spire.makeID("DrumOfEndurance");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("DrumOfEndurance.png"));
    private static final int _StrengthStack = 1;
    private static final int _DexStack = 1;

    public DrumOfEndurance() {
        super(ID, IMG, RelicTier.COMMON, LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStart() {
        flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        Random random = new Random(Settings.seed + AbstractDungeon.floorNum);
        if (random.nextDouble() <= 0.5f) {
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, _StrengthStack), _StrengthStack));
        } else {
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, _DexStack), _DexStack));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return StringUtil.format(DESCRIPTIONS[0], _StrengthStack, _DexStack);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new DrumOfEndurance();
    }
}
