package dota2Spire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import dota2Spire.Dota2Spire;
import dota2Spire.util.StringUtil;
import dota2Spire.util.TextureLoader;

import java.util.Random;

import static dota2Spire.Dota2Spire.makeRelicPath;

/**
 * 蝴蝶
 * 战斗开始时获得2敏捷
 * 15%几率闪避伤害
 */
public class Butterfly extends CustomRelic {
    // ID, images, text.
    public static final String ID = Dota2Spire.makeID("Butterfly");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Butterfly.png"));
    public static Random random;
    private static final int _DexStack = 2;
    private static final int _DodgePercent = 15;

    public Butterfly() {
        super(ID, IMG, RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStart() {
        random = new Random(Settings.seed + AbstractDungeon.floorNum);
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, _DexStack), _DexStack));
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.owner != null && info.type != DamageInfo.DamageType.HP_LOSS && info.type != DamageInfo.DamageType.THORNS && damageAmount > 0 &&
                roll()) {
            flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            return 0;
        }
        return damageAmount;
    }

    private boolean roll() {
        if (random != null) {
            return random.nextDouble() <= (_DodgePercent / 100f);
        }
        return false;
    }

    @Override
    public String getUpdatedDescription() {
        return StringUtil.format(DESCRIPTIONS[0], _DexStack, _DodgePercent);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Butterfly();
    }
}
