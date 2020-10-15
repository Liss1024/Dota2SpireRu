package dota2Spire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;
import dota2Spire.Dota2Spire;
import dota2Spire.util.StringUtil;
import dota2Spire.util.TextureLoader;

import static dota2Spire.Dota2Spire.makeRelicPath;

public class HandOfMidas extends CustomRelic {
    // ID, images, text.
    public static final String ID = Dota2Spire.makeID("HandOfMidas");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("HandOfMidas.png"));
//    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("placeholder_relic.png"));

    private static final int _Gold = 30;
    private static final int _CD = 5;

    public HandOfMidas() {
//        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
        super(ID, IMG, RelicTier.COMMON, LandingSound.MAGICAL);
    }

    @Override
    public void onEquip() {
        setCount(0);
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
    public void onMonsterDeath(AbstractMonster m) {
        if (this.counter > 0 || m.hasPower("Minion")) {
            return;
        } else {
            CardCrawlGame.sound.play("GOLD_GAIN");
            AbstractDungeon.player.gainGold(_Gold);
            flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));

            for (int i = 0; i < _Gold; ++i) {
                AbstractDungeon.effectList.add(new GainPennyEffect(m, m.hb.cX, m.hb.cY, m.hb.cX, m.hb.cY, true));
            }
            setCount(_CD);
        }
    }

    @Override
    public void atTurnStart() {
        setCount(this.counter - 1);
    }

    @Override
    public String getUpdatedDescription() {
        return StringUtil.format(DESCRIPTIONS[0], _Gold, _CD);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new HandOfMidas();
    }
}
