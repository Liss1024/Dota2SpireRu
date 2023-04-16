package dota2Spire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import dota2Spire.Dota2Spire;
import dota2Spire.util.StringUtil;
import dota2Spire.util.TextureLoader;

import static dota2Spire.Dota2Spire.makeRelicPath;

/**
 * 林肯
 * 每4回合给一层人工制品
 */
public class LinkenSphere extends CustomRelic {
    // ID, images, text.
    public static final String ID = Dota2Spire.makeID("LinkenSphere");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("LinkenSphere.png"));
    private static final int _BuffStack = 1;
    private static final int _CD = 4;

    public LinkenSphere() {
        super(ID, IMG, RelicTier.UNCOMMON, LandingSound.MAGICAL);
    }

    @Override
    public void onEquip() {
        setCount(0);
    }

    @Override
    public void atTurnStart() {
        if (this.counter <= 0) {
            flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ArtifactPower(AbstractDungeon.player, _BuffStack)));
            setCount(_CD);
        } else {
            setCount(this.counter - 1);
        }
    }

    private void setCount(int count) {
        if (count <= 0) {
            this.counter = -1;
            beginLongPulse();
        } else {
            this.counter = count;
            stopPulse();
        }
    }

    @Override
    public String getUpdatedDescription() {
        return StringUtil.format(DESCRIPTIONS[0], _BuffStack, _CD);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new LinkenSphere();
    }

}
