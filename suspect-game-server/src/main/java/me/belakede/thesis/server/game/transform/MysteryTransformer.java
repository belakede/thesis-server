package me.belakede.thesis.server.game.transform;

import me.belakede.thesis.game.equipment.Case;
import me.belakede.thesis.internal.game.equipment.DefaultCase;
import me.belakede.thesis.internal.game.equipment.DefaultSuspicion;
import me.belakede.thesis.server.game.domain.Mystery;
import org.springframework.stereotype.Service;

@Service
public class MysteryTransformer {

    public Case transform(Mystery mystery) {
        DefaultSuspicion suspicion = new DefaultSuspicion(mystery.getSuspect(), mystery.getRoom(), mystery.getWeapon());
        return new DefaultCase(suspicion);
    }

    public Mystery transform(Case mystery) {
        Mystery result = new Mystery();
        result.setSuspect(mystery.getSuspect());
        result.setRoom(mystery.getRoom());
        result.setWeapon(mystery.getWeapon());
        return result;
    }

}
