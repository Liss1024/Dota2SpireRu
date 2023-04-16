package dota2Spire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import dota2Spire.Dota2Spire;
import dota2Spire.powers.Invisibility;
import dota2Spire.util.StringUtil;
import dota2Spire.util.TextureLoader;

import static dota2Spire.Dota2Spire.makeRelicPath;

/**
 * 隐刀
 * 获得影身1回合
 * CD 4回合
 */
public class ShadowBlade extends CustomRelic {
    // ID, images, text.
    public static final String ID = Dota2Spire.makeID("ShadowBlade");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("ShadowBlade.png"));
    private static final int _CD = 4;
    private static final int _BuffStack = 1;


    public ShadowBlade() {
//        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
        super(ID, IMG, RelicTier.COMMON, LandingSound.MAGICAL);
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
    public void onPlayerEndTurn() {
        if (counter<=0){
            flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                    new Invisibility(AbstractDungeon.player, _BuffStack)));
            setCount(_CD);
        } else {
            setCount(this.counter - 1);
        }
    }

    @Override
    public String getUpdatedDescription() {
        return StringUtil.format(DESCRIPTIONS[0], _BuffStack, _CD);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new ShadowBlade();
    }

}
