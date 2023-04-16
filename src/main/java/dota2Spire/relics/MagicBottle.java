package dota2Spire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import dota2Spire.Dota2Spire;
import dota2Spire.powers.HpRecovery;
import dota2Spire.powers.MpRecovery;
import dota2Spire.util.StringUtil;
import dota2Spire.util.TextureLoader;

import static dota2Spire.Dota2Spire.makeRelicPath;

/**
 * 魔瓶
 */
public class MagicBottle extends CustomRelic {
    // ID, images, text.
    public static final String ID = Dota2Spire.makeID("MagicBottle");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("MagicBottle.png"));

    private static final int _HpRecovery = 2;
    private static final int _MpRecovery = 2;
    private static final int _MaxPoint = 3;

    public MagicBottle() {
        super(ID, IMG, RelicTier.SHOP, LandingSound.MAGICAL);
    }

    @Override
    public void onEquip() {
        setCount(_MaxPoint);
    }

    private void setCount(int count) {
        this.counter = Math.max(count, 0);
    }

    @Override
    public void atBattleStart() {
        if (this.counter > 0) {
            flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));

            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                    new HpRecovery(AbstractDungeon.player, _HpRecovery)));
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                    new MpRecovery(AbstractDungeon.player, _MpRecovery)));
            setCount(this.counter - 1);
        }
    }

    @Override
    public void onEnterRestRoom() {
        flash();
        setCount(_MaxPoint);
    }

    @Override
    public String getUpdatedDescription() {
        return StringUtil.format(DESCRIPTIONS[0], _MaxPoint, _HpRecovery, _MpRecovery);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new MagicBottle();
    }
}
