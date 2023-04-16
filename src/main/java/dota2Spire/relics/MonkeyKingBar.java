package dota2Spire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import dota2Spire.Dota2Spire;
import dota2Spire.util.StringUtil;
import dota2Spire.util.TextureLoader;

import static dota2Spire.Dota2Spire.makeRelicPath;

/**
 * 金箍棒
 * 额外造成3点伤害
 */
public class MonkeyKingBar extends CustomRelic {
    // ID, images, text.
    public static final String ID = Dota2Spire.makeID("MonkeyKingBar");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("MonkeyKingBar.png"));
//    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("placeholder_relic.png"));

    private static final int _Damage = 3;

    public MonkeyKingBar() {
//        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
        super(ID, IMG, RelicTier.UNCOMMON, LandingSound.MAGICAL);
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
        return StringUtil.format(DESCRIPTIONS[0], _Damage);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new MonkeyKingBar();
    }
}
