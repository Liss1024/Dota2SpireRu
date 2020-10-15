package dota2Spire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import dota2Spire.Dota2Spire;
import dota2Spire.util.StringUtil;
import dota2Spire.util.TextureLoader;

import java.util.List;
import java.util.stream.Collectors;

import static dota2Spire.Dota2Spire.makeRelicPath;

public class MantaStyle extends CustomRelic implements ClickableRelic {
    // ID, images, text.
    public static final String ID = Dota2Spire.makeID("MantaStyle");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("MantaStyle.png"));
    //    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("placeholder_relic.png"));
    private boolean isPlayerTurn = false;

    private static final int _CD = 3;

    public MantaStyle() {
//        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
        super(ID, IMG, RelicTier.UNCOMMON, LandingSound.MAGICAL);
    }

    @Override
    public void onEquip() {
        setCount(0);
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
    public void onRightClick() {
        if (!isObtained || !isPlayerTurn || this.counter > 0) {
            return;
        }
        if (AbstractDungeon.getCurrRoom() == null || AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
            return;
        }
        List<AbstractCard> group = AbstractDungeon.player.exhaustPile.group.stream().filter(card -> card.type != AbstractCard.CardType.CURSE && card.type != AbstractCard.CardType.STATUS).collect(Collectors.toList());
        if (!group.isEmpty()) {
            AbstractCard abstractCard = group.get(AbstractDungeon.cardRandomRng.random(group.size() - 1));
            this.addToBot(new MakeTempCardInHandAction(abstractCard));
            flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            setCount(_CD);
        }
    }

    @Override
    public void atTurnStart() {
        isPlayerTurn = true;
        setCount(this.counter - 1);
    }

    @Override
    public void onPlayerEndTurn() {
        isPlayerTurn = false;
        stopPulse();
    }


    @Override
    public String getUpdatedDescription() {
        return StringUtil.format(DESCRIPTIONS[0], _CD);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new MantaStyle();
    }
}
