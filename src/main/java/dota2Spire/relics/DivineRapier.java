package dota2Spire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import dota2Spire.Dota2Spire;
import dota2Spire.util.StringUtil;
import dota2Spire.util.TextureLoader;

import static dota2Spire.Dota2Spire.makeRelicPath;

public class DivineRapier extends CustomRelic {
    // ID, images, text.
    public static final String ID = Dota2Spire.makeID("DivineRapier");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("DivineRapier.png"));
//    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("placeholder_relic.png"));

    private static final int _Damage = 6;
    private static final int _VulnerableStack = 3;
    private static final int _FrailStack = 3;

    public DivineRapier() {
//        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
        super(ID, IMG, RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStart() {
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                new VulnerablePower(AbstractDungeon.player, _VulnerableStack, false), _VulnerableStack));
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                new FrailPower(AbstractDungeon.player, _FrailStack, false), _FrailStack));
    }

    @Override
    public float atDamageModify(float damage, AbstractCard c) {
        if (c.type == AbstractCard.CardType.ATTACK) {
            return damage + _Damage;
        }
        return damage;
    }

    @Override
    public String getUpdatedDescription() {
        return StringUtil.format(DESCRIPTIONS[0], _Damage, _VulnerableStack, _FrailStack);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new DivineRapier();
    }
}
