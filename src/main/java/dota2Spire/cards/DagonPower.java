package dota2Spire.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import dota2Spire.Dota2Spire;

import static dota2Spire.Dota2Spire.makeCardPath;

public class DagonPower extends CustomCard {

    public static final String Card_ID = Dota2Spire.makeID("DagonPower");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(Card_ID);
    public static final String IMG = makeCardPath("DagonPower.png");
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final int COST = 1;

    private static final int DAMAGE = 15;

    private static final int UPGRADE_PLUS_DMG = 10;

    public DagonPower() {
        super(Card_ID, NAME, IMG, COST, DESCRIPTION, CardType.ATTACK, CardColor.COLORLESS, CardRarity.BASIC, CardTarget.ENEMY);

        baseDamage = DAMAGE;

        upgraded = true;
    }

    public DagonPower(int upgradeTimes) {
        super(Card_ID, NAME, IMG, COST, DESCRIPTION, CardType.ATTACK, CardColor.COLORLESS, CardRarity.BASIC, CardTarget.ENEMY);

        baseDamage = DAMAGE + UPGRADE_PLUS_DMG * (upgradeTimes - 1);

        upgraded = true;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToTop(new VFXAction(new LightningEffect(m.drawX, m.drawY)));
        this.addToTop(new VFXAction(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, AbstractGameAction.AttackEffect.LIGHTNING)));
        this.addToTop(new SFXAction("ORB_LIGHTNING_EVOKE", 0.1F));
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
    }
}
