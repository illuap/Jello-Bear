package com.illuap.jellobear.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.illuap.jellobear.JelloBear;
import com.illuap.jellobear.Scenes.Hud;
import com.illuap.jellobear.Sprites.Player;

/**
 * Created by paul on 02/06/16.
 */
public class PlayScreen implements Screen {

    private JelloBear game;

    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Hud hud;

    private Player player;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private World world;
    private Box2DDebugRenderer b2dr;

    Texture angryBear;

    public PlayScreen (JelloBear game){
        this.game = game;
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(JelloBear.V_WIDTH/JelloBear.PPM,JelloBear.V_HEIGHT/JelloBear.PPM,gameCam);

        angryBear = new Texture("motorcycleBear.png");
        hud = new Hud(game.batch);



        mapLoader = new TmxMapLoader();
        map = mapLoader.load("map0.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1/JelloBear.PPM);
        gameCam.position.set(gamePort.getWorldWidth()/2,gamePort.getWorldHeight()/2,0);

        world = new World(new Vector2(0,-10),true);
        b2dr = new Box2DDebugRenderer();

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        for(MapObject object: map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();

            bdef.type= BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth()/2)/JelloBear.PPM, (rect.getY()+rect.getHeight()/2)/JelloBear.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth()/2/JelloBear.PPM, rect.getHeight()/2/JelloBear.PPM);
            fdef.shape = shape;

            body.createFixture(fdef);
        }


        player = new Player(world);
    }

    @Override
    public void show() {

    }

    public void handleInput(float dt){
        if(Gdx.input.isKeyJustPressed(Input.Keys.W)){
            player.b2body.applyLinearImpulse(new Vector2(0,4f),player.b2body.getWorldCenter(),true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D) && player.b2body.getLinearVelocity().x <= 2){
            player.b2body.applyLinearImpulse(new Vector2(1f,0),player.b2body.getWorldCenter(),true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A) && player.b2body.getLinearVelocity().x >= -2){
            player.b2body.applyLinearImpulse(new Vector2(-1f,0),player.b2body.getWorldCenter(),true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S) && player.b2body.getLinearVelocity().y >= -2){
            player.b2body.applyLinearImpulse(new Vector2(0,-1f),player.b2body.getWorldCenter(),true);
        }
    }

    public void update(float dt){
        handleInput(dt);

        world.step(1/60f,6,2);

        gameCam.position.x = player.b2body.getPosition().x;
        gameCam.update();

        renderer.setView(gameCam);
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();

        b2dr.render(world,gameCam.combined);
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
//        game.batch.setProjectionMatrix(gameCam.combined);
//
//        game.batch.begin();
//        game.batch.draw(angryBear, 0, 0);
//        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
