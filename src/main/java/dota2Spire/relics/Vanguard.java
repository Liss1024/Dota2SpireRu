package dota2Spire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import dota2Spire.Dota2Spire;
import dota2Spire.util.StringUtil;
import dota2Spire.util.TextureLoader;

import static dota2Spire.Dota2Spire.makeRelicPath;

/**
 * 先锋盾
 * 每回合第一次获得格挡时额外获得4点
 */
public class Vanguard extends CustomRelic {
    // ID, images, text.
    public static final String ID = Dota2Spire.makeID("Vanguard");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Vanguard.png"));
    private boolean active = false;
    private static final int _Block = 4;

    public Vanguard() {
        super(ID, IMG, RelicTier.COMMON, LandingSound.MAGICAL);
    }

    @Override
    public int onPlayerGainedBlock(float blockAmount) {
        if (blockAmount > 0.0F && !active) {
            active = true;
            this.flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            blockAmount += _Block;
        }
        return super.onPlayerGainedBlock(blockAmount);
    }

    @Override
    public void atTurnStart() {
        active = false;
    }

    @Override
    public String getUpdatedDescription() {
        return StringUtil.format(DESCRIPTIONS[0], _Block);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Vanguard();
    }
}
