package dota2Spire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import dota2Spire.Dota2Spire;
import dota2Spire.util.StringUtil;
import dota2Spire.util.TextureLoader;

import static dota2Spire.Dota2Spire.makeRelicPath;

public class Radiance extends CustomRelic {
    // ID, images, text.
    public static final String ID = Dota2Spire.makeID("Radiance");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Radiance.png"));
    //    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("placeholder_relic.png"));

    private static final int _amount = 2;

    public Radiance() {
//        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
        super(ID, IMG, RelicTier.UNCOMMON, LandingSound.MAGICAL);
    }

    @Override
    public void atTurnStart() {
        this.counter = _amount;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        this.counter++;
    }

    @Override
    public void onPlayerEndTurn() {
        if (this.counter > 0) {
            flash();
            for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (m.currentHealth > 0) {
                    addToBot(new RelicAboveCreatureAction(m, this));
                    this.addToTop(new VFXAction(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, AbstractGameAction.AttackEffect.FIRE)));
                    addToBot(new DamageAction(m, new DamageInfo(AbstractDungeon.player, this.counter, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.NONE));
                }
            }
            this.counter = _amount;
        }
    }

    @Override
    public String getUpdatedDescription() {
        return StringUtil.format(DESCRIPTIONS[0], _amount);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Radiance();
    }
}
