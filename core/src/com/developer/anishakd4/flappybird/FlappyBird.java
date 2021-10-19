package com.developer.anishakd4.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {

    SpriteBatch batch;
    Texture backGround, topTube, bottomTube, gameOver;
    Texture[] birds;
    int flatState = 0, gameState = 1, numberOfTubes = 4, score = 0, scoringTube = 0;
    float velocity = 0, birdX, birdY, gravity = 2, gap, maxTubeOffset, tubeVelocity = 4, distanceBetweenTubes, gameOverHeight, gamoOverWidth;
    float[] tubeOffset, tubeX;
    Random random;
    //ShapeRenderer shapeRenderer;
    Circle birdCircle;
    Rectangle topTubeRectangles[];
    Rectangle bottomTubeRectangles[];
    BitmapFont bitmapFont;

    @Override
    public void create() {
        batch = new SpriteBatch();
        random = new Random();
        backGround = new Texture("background.png");
        gameOver = new Texture("gameover.png");
        topTube = new Texture("toptube.png");
        bottomTube = new Texture("bottomtube.png");
        birds = new Texture[2];
        birds[0] = new Texture("bird.png");
        birds[1] = new Texture("bird2.png");
        //shapeRenderer = new ShapeRenderer();
        birdCircle = new Circle();
        bitmapFont = new BitmapFont();
        bitmapFont.setColor(Color.RED);
        bitmapFont.getData().setScale(10);
        gamoOverWidth = Gdx.graphics.getWidth()/2;
        gameOverHeight = (Gdx.graphics.getHeight() / 2 * gameOver.getWidth()) / (Gdx.graphics.getWidth() / 2);
        tubeX = new float[numberOfTubes];
        tubeOffset = new float[numberOfTubes];
        gap = (float) ((Gdx.graphics.getHeight() / 4)*(1.5));
        maxTubeOffset = (Gdx.graphics.getHeight() / 2) - (gap / 2) - 200;
        distanceBetweenTubes = (Gdx.graphics.getWidth() * 3) / 4;
        topTubeRectangles = new Rectangle[numberOfTubes];
        bottomTubeRectangles = new Rectangle[numberOfTubes];
        resetGame();
    }

    public void resetGame(){
        birdX = Gdx.graphics.getWidth()/2 - birds[flatState].getWidth()/2;
        birdY = Gdx.graphics.getHeight()/2 - birds[flatState].getHeight()/2;
        for (int i=0 ;i <numberOfTubes; i++){
            tubeOffset[i] = (random.nextFloat() - 0.5f) * maxTubeOffset;
            tubeX[i] = Gdx.graphics.getWidth() + (i * distanceBetweenTubes);
            topTubeRectangles[i] = new Rectangle();
            bottomTubeRectangles[i] = new Rectangle();
        }
        gameState = 1;
        score = 0;
        scoringTube = 0;
    }

    @Override
    public void render() {
        //Gdx.app.log("anisham","anisham touched");
        batch.begin();
        batch.draw(backGround, 0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if(gameState == 1){
            if(Gdx.input.justTouched()){
                velocity = -30;
            }
            for (int i=0; i < numberOfTubes; i++){
                if(tubeX[i] < -topTube.getWidth()){
                    tubeX[i] = tubeX[i] + numberOfTubes * distanceBetweenTubes;
                    tubeOffset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
                }else {
                    tubeX[i] = tubeX[i] - tubeVelocity;
                }
                batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight()/2 + gap / 2 + tubeOffset[i]);
                batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight()/2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);
                topTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight()/2 + gap / 2 + tubeOffset[i],
                        topTube.getWidth(), topTube.getHeight());
                bottomTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight()/2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i],
                        bottomTube.getWidth(), bottomTube.getHeight());
            }
            if(birdY > 0 || velocity < 0){
                velocity = velocity + gravity;
                birdY = birdY - velocity;
            }
            if(flatState == 0){
                flatState = 1;
            }else {
                flatState = 0;
            }
            batch.draw(birds[flatState], birdX, birdY);
            bitmapFont.draw(batch, String.valueOf(score), 100, 200);
            if(tubeX[scoringTube] < (Gdx.graphics.getWidth() / 2 - birds[flatState].getWidth())){
                score++;
                if(scoringTube < numberOfTubes - 1){
                    scoringTube++;
                }else {
                    scoringTube = 0;
                }
            }
        }else{
            batch.draw(gameOver, Gdx.graphics.getWidth()/4,Gdx.graphics.getHeight()/4,
                    Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
            if(Gdx.input.justTouched()){
                resetGame();
            }
        }
        batch.end();

        birdCircle.set(birdX + birds[flatState].getWidth()/2,birdY + birds[flatState].getHeight()/2,
                birds[flatState].getWidth()/2);

        //shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //shapeRenderer.setColor(Color.RED);
        //shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);
        for (int i = 0; i< numberOfTubes; i++){
            //shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight()/2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
            //shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight()/2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i],bottomTube.getWidth(), bottomTube.getHeight());
            if(Intersector.overlaps(birdCircle, topTubeRectangles[i]) || Intersector.overlaps(birdCircle, bottomTubeRectangles[i])){
                //Gdx.app.log("anisham","anisham=collision=top");
                gameState = 2;
            }
        }
        //shapeRenderer.end();
    }

}
