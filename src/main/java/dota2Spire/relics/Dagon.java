package dota2Spire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import dota2Spire.Dota2Spire;
import dota2Spire.cards.DagonPower;
import dota2Spire.util.StringUtil;
import dota2Spire.util.TextureLoader;

import static dota2Spire.Dota2Spire.makeRelicPath;

public class Dagon extends CustomRelic implements ClickableRelic {
    // ID, images, text.
    public static final String ID = Dota2Spire.makeID("Dagon");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Dagon.png"));
    //    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("placeholder_relic.png"));

    private static final int _Gold = 75;
    private static final int _MaxLevel = 5;

    public Dagon() {
//        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
        super(ID, IMG, RelicTier.UNCOMMON, LandingSound.MAGICAL);
    }

    @Override
    public void onEquip() {
        this.counter = 1;
    }

    @Override
    public void onRightClick() {
        if (!isObtained) {
            return;
        }

        if (AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom() instanceof ShopRoom) {
            if (AbstractDungeon.player.gold > _Gold && this.counter < _MaxLevel) {
                AbstractDungeon.player.loseGold(_Gold);
                flash();
                this.counter++;
            }
        }
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        if (room instanceof ShopRoom) {
            flash();
        }
    }

    @Override
    public void atBattleStartPreDraw() {
        flash();
        addToBot(new MakeTempCardInDrawPileAction(new DagonPower(this.counter), 1, true, true));
    }

    @Override
    public String getUpdatedDescription() {
        return StringUtil.format(DESCRIPTIONS[0], _Gold, _MaxLevel);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Dagon();
    }
}
