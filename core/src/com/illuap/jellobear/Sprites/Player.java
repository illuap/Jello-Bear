package com.illuap.jellobear.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.illuap.jellobear.JelloBear;

/**
 * Created by paul on 06/06/16.
 */
public class Player extends Sprite {
    public World world;
    public Body b2body;

    public Player(World w){
        this.world = w;
        definePlayer();
    }

    public void definePlayer(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(128/ JelloBear.PPM,128/JelloBear.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(50/JelloBear.PPM);


        fdef.shape = shape;
        b2body.createFixture(fdef);
    }
}
