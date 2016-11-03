package me.belakede.thesis.server.game.converter;

import me.belakede.thesis.game.equipment.Case;
import me.belakede.thesis.internal.game.equipment.DefaultCase;
import me.belakede.thesis.internal.game.equipment.DefaultSuspicion;
import me.belakede.thesis.server.game.domain.Mystery;
import org.springframework.stereotype.Component;

@Component
public class MysteryConverter {

    public Case convert(Mystery mystery) {
        return new DefaultCase(new DefaultSuspicion(mystery.getSuspect(), mystery.getRoom(), mystery.getWeapon()));
    }

    public Mystery convert(Case mystery) {
        return new Mystery(mystery.getSuspect(), mystery.getRoom(), mystery.getWeapon());
    }

}
