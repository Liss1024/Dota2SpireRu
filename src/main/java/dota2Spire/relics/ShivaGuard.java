package dota2Spire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import dota2Spire.Dota2Spire;
import dota2Spire.util.StringUtil;
import dota2Spire.util.TextureLoader;

import static dota2Spire.Dota2Spire.makeRelicPath;

public class ShivaGuard extends CustomRelic {
    // ID, images, text.
    public static final String ID = Dota2Spire.makeID("ShivaGuard");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("ShivaGuard.png"));
//    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("placeholder_relic.png"));

    private static final int _CD = 3;
    private static final int _WeakStack = 1;
    private static final int _Block = 2;

    public ShivaGuard() {
//        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
        super(ID, IMG, RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public void onEquip() {
        setCount(0);
    }

    private void setCount(int count) {
        if (count <= 0) {
            this.counter = -1;
            flash();
            beginLongPulse();
        } else {
            this.counter = count;
            stopPulse();
        }
    }

    @Override
    public int onPlayerGainedBlock(float blockAmount) {
        if (blockAmount > 0.0F) {
            blockAmount += _Block;
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            if (this.counter <= 0) {
                for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(m, AbstractDungeon.player, new WeakPower(m, _WeakStack, false), _WeakStack));
                }
                setCount(_CD);
            }
            this.flash();
        }
        return super.onPlayerGainedBlock(blockAmount);
    }

    @Override
    public void atTurnStart() {
        setCount(this.counter - 1);
    }

    @Override
    public String getUpdatedDescription() {
        return StringUtil.format(DESCRIPTIONS[0], _Block, _WeakStack, _CD);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new ShivaGuard();
    }
}
