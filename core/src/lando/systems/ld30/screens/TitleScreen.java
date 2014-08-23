package lando.systems.ld30.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import lando.systems.ld30.LudumDare30;
import lando.systems.ld30.utils.Assets;
import lando.systems.ld30.utils.Config;


/**
 * Created by dsgraham on 8/23/14.
 */
public class TitleScreen implements Screen {

    private final OrthographicCamera camera;
    private final LudumDare30 game;
    private final FrameBuffer fbo;



    public TitleScreen(LudumDare30 game){
        super();
         this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Config.window_width, Config.window_height);


        fbo = new FrameBuffer(Pixmap.Format.RGB888, Config.window_width, Config.window_height, false);

    }

    public void update(float dt){
        if (Gdx.input.justTouched()){
            game.setScreen(new GameScreen(game));
        }
    }


    float accum = 0;

    @Override
    public void render(float delta) {

        update(delta);
        fbo.begin();
        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Assets.batch.begin();
        Assets.batch.draw(Assets.badlogic, 500, 100);
        Assets.batch.end();
        fbo.end();

        Gdx.gl20.glClearColor(.1f, .1f, .1f, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Assets.batch.begin();
        Assets.batch.setShader(Assets.prismProgram);
        Assets.prismProgram.setUniformf("u_scale", .1f);
        accum+=delta*2;
        Assets.prismProgram.setUniformf("u_dir", (float)Math.sin(accum), (float)Math.cos(accum));

        Assets.batch.draw(fbo.getColorBufferTexture(), 0, fbo.getHeight(), fbo.getWidth(), -fbo.getHeight());
        Assets.batch.end();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
