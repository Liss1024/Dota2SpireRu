package dota2Spire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import dota2Spire.Dota2Spire;
import dota2Spire.util.StringUtil;
import dota2Spire.util.TextureLoader;

import java.util.Random;

import static dota2Spire.Dota2Spire.makeRelicPath;

/**
 * 虚灵刀
 * 使用能力牌后随机给与一个怪物1脆弱或1虚弱
 */
public class EtherealBlade extends CustomRelic {
    // ID, images, text.
    public static final String ID = Dota2Spire.makeID("EtherealBlade");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("EtherealBlade.png"));
    private static final int _VulnerableStack = 1;
    private static final int _WeakStack = 1;

    public EtherealBlade() {
        super(ID, IMG, RelicTier.UNCOMMON, LandingSound.MAGICAL);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.POWER) {
            AbstractMonster randomMonster = AbstractDungeon.getCurrRoom().monsters.getRandomMonster(true);
            if (randomMonster != null) {
                this.flash();
                addToBot(new RelicAboveCreatureAction(randomMonster, this));
                Random random = new Random(Settings.seed + AbstractDungeon.floorNum);
                if (random.nextDouble() <= 0.5f) {
                    addToBot(new ApplyPowerAction(randomMonster, AbstractDungeon.player, new WeakPower(randomMonster, _WeakStack, false)));
                } else {
                    addToBot(new ApplyPowerAction(randomMonster, AbstractDungeon.player, new VulnerablePower(randomMonster, _VulnerableStack, false)));
                }
            }
        }

    }

    @Override
    public String getUpdatedDescription() {
        return StringUtil.format(DESCRIPTIONS[0], _WeakStack, _VulnerableStack);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new EtherealBlade();
    }
}
