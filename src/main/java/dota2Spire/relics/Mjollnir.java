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
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import dota2Spire.Dota2Spire;
import dota2Spire.util.StringUtil;
import dota2Spire.util.TextureLoader;

import java.util.Random;

import static dota2Spire.Dota2Spire.makeRelicPath;

/**
 * 大雷锤
 * 使用攻击牌时35%几率对所有敌人造成4伤害
 */
public class Mjollnir extends CustomRelic {
    // ID, images, text.
    public static final String ID = Dota2Spire.makeID("Mjollnir");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Mjollnir.png"));
//    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("placeholder_relic.png"));

    private static final int _Damage = 4;
    public static Random random;

    public Mjollnir() {
//        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
        super(ID, IMG, RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStart() {
        random = new Random(Settings.seed + AbstractDungeon.floorNum);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (m.currentHealth > 0 && roll()) {
                    addToBot(new RelicAboveCreatureAction(m, this));
                    addToBot(new VFXAction(new LightningEffect(m.drawX, m.drawY)));
                    addToBot(new VFXAction(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, AbstractGameAction.AttackEffect.LIGHTNING)));
                    addToBot(new DamageAction(m, new DamageInfo(AbstractDungeon.player, _Damage, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.NONE));
                }
            }
        }

    }

    private boolean roll() {
        return random.nextDouble() <= 0.35;
    }


    @Override
    public String getUpdatedDescription() {
        return StringUtil.format(DESCRIPTIONS[0], _Damage);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Mjollnir();
    }
}
