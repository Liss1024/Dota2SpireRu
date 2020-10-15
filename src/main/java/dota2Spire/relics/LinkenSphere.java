package dota2Spire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.OnReceivePowerRelic;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import dota2Spire.Dota2Spire;
import dota2Spire.util.StringUtil;
import dota2Spire.util.TextureLoader;

import static dota2Spire.Dota2Spire.makeRelicPath;

public class LinkenSphere extends CustomRelic implements OnReceivePowerRelic {
    // ID, images, text.
    public static final String ID = Dota2Spire.makeID("LinkenSphere");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("LinkenSphere.png"));
    //    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("placeholder_relic.png"));

    private static final int _CD = 4;

    public LinkenSphere() {
//        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
        super(ID, IMG, RelicTier.UNCOMMON, LandingSound.MAGICAL);
    }

    @Override
    public void onEquip() {
        setCount(0);
    }

    @Override
    public boolean onReceivePower(AbstractPower power, AbstractCreature source) {
        if (this.counter == 0 && source != null && source != AbstractDungeon.player && power.type == AbstractPower.PowerType.DEBUFF) {
            flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));

            setCount(_CD);
            return false;
        } else {
            return true;
        }
    }

    private void setCount(int count) {
        if (count <= 0) {
            this.counter = 0;
            beginLongPulse();
        } else {
            this.counter = count;
            stopPulse();
        }
    }

    @Override
    public void onPlayerEndTurn() {
        setCount(this.counter - 1);
    }

    @Override
    public String getUpdatedDescription() {
        return StringUtil.format(DESCRIPTIONS[0], _CD);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new LinkenSphere();
    }

}
