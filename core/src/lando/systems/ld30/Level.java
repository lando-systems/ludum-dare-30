package lando.systems.ld30;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import lando.systems.ld30.screens.GameScreen;
import lando.systems.ld30.utils.*;

import java.util.ArrayList;

/**
 * Brian Ploeckelman created on 8/24/2014.
 */
public class Level implements Collidable {

    GameScreen screen;
    Body body;

    ArrayList<ParticleEffect> particleEffects;
    ParticleEffectPool particleEffectPool;

    public Level(GameScreen screen) {
        this.screen = screen;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0,0);

        body = Globals.world.createBody(bodyDef);
        body.setUserData(this);

        ChainShape chainShape = new ChainShape();
        chainShape.createLoop(new Vector2[] {
                new Vector2( 100,  100),
                new Vector2( 100, -100),
                new Vector2(-100, -100),
                new Vector2(-100,  100)
        });

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = chainShape;
        fixtureDef.density = 1;
        fixtureDef.filter.categoryBits = Box2dContactListener.CATEGORY_WORLD;
        fixtureDef.filter.maskBits = Box2dContactListener.MASK_WORLD;

        body.createFixture(fixtureDef);

        chainShape.dispose();

        particleEffectPool = new ParticleEffectPool(Assets.explodeParticleEffect, 0, 20);
        particleEffects = new ArrayList<ParticleEffect>();
    }

    public void update(float dt) {
        ParticleEffect effect;
        for (int i = particleEffects.size() - 1; i >= 0; --i) {
            effect = particleEffects.get(i);
            if (effect.isComplete()) {
                particleEffects.remove(i);
            } else {
                effect.update(dt);
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (ParticleEffect effect : particleEffects) {
            effect.draw(batch);
        }
    }

    @Override
    public CollidableType getType() {
        return CollidableType.WORLD;
    }

    @Override
    public void shotByPlayer(Color color) {

    }

    @Override
    public void shotByEnemy(Color color) {

    }

    @Override
    public boolean collideWithBullet(Bullet bullet) {
        ParticleEffect particleEffect = particleEffectPool.obtain();
        particleEffect.setPosition(bullet.body.getPosition().x, bullet.body.getPosition().y);
        particleEffect.scaleEffect(0.075f);
        particleEffects.add(particleEffect);
        return true;
    }
}
