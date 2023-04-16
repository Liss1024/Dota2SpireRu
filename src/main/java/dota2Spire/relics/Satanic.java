package dota2Spire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import dota2Spire.Dota2Spire;
import dota2Spire.powers.Lifesteal;
import dota2Spire.util.StringUtil;
import dota2Spire.util.TextureLoader;

import static dota2Spire.Dota2Spire.makeRelicPath;

/**
 * 撒旦
 * 战斗开始时获得2力量
 * 回合开始时获得30层吸血 CD5回合
 */
public class Satanic extends CustomRelic{
    // ID, images, text.
    public static final String ID = Dota2Spire.makeID("Satanic");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Satanic.png"));
    private static final int _StrengthStack = 2;
    private static final int _LifestealStack = 30;
    private static final int _CD = 5;

    public Satanic() {
        super(ID, IMG, RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public void onEquip() {
        setCount(0);
    }

    @Override
    public void atBattleStart() {
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, _StrengthStack), _StrengthStack));
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
    public void atTurnStart() {
        if(counter<=0){
            flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                    new Lifesteal(AbstractDungeon.player, _LifestealStack), _LifestealStack));
            setCount(_CD);
        }else {
            setCount(this.counter - 1);
        }
    }

    @Override
    public String getUpdatedDescription() {
        return StringUtil.format(DESCRIPTIONS[0], _StrengthStack, _LifestealStack, _CD);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Satanic();
    }

}
