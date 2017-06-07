package com.drfl.twinstickshooter.view.entities;

import com.drfl.twinstickshooter.TSSGame;
import com.drfl.twinstickshooter.model.entities.EntityModel;

import java.util.HashMap;
import java.util.Map;

import static com.drfl.twinstickshooter.model.entities.EntityModel.ModelType.*;

/**
 * A factory for EntityView objects with cache
 */

public class ViewFactory {

    private static Map<EntityModel.ModelType, EntityView> cache = new HashMap<EntityModel.ModelType, EntityView>();

    public static EntityView makeView(TSSGame game, EntityModel model) {

        if (!cache.containsKey(model.getType())) {

            if (model.getType() == MAINCHAR) {
                cache.put(model.getType(), new MainCharView(game));
            }
            if (model.getType() == BULLET) {
                cache.put(model.getType(), new BulletView(game));
            }
        }

        return cache.get(model.getType());
    }

    public static void initViewFactory() {
        cache = new HashMap<EntityModel.ModelType, EntityView>();
    }
}