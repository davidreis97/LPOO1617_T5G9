package com.drfl.twinstickshooter.view.entities;

import com.drfl.twinstickshooter.game.TSSGame;
import com.drfl.twinstickshooter.model.entities.EntityModel;

import java.util.HashMap;
import java.util.Map;

import static com.drfl.twinstickshooter.model.entities.EntityModel.ModelType.*;

/**
 * A factory for EntityView objects with cache.
 */
public class ViewFactory {

    /**
     * Cache for the factory.
     */
    private static Map<EntityModel.ModelType, EntityView> cache = new HashMap<EntityModel.ModelType, EntityView>();

    /**
     * Create new view if not present on cache.
     *
     * @param game The current game instance
     * @param model The entity model to create a view of
     * @return The created or fetched view
     */
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

    /**
     * Reset the factory's cache.
     */
    public static void initViewFactory() {
        cache = new HashMap<>();
    }
}