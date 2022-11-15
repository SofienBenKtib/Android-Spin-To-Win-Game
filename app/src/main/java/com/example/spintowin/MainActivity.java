package com.example.spintowin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //Wheel values
    final int[] sectors={1,2,3,4,5,6,7,8,9,10,11,12};
    final int[] sectorDegrees=new int[sectors.length];

    //random index
    int randomSectorIndex=0;

    //what to spin
    ImageView wheel;
    boolean spinning=false;
    int earningsRecord=0;

    //random to help generate random index
    Random random=new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //to spin
        wheel=findViewById(R.id.wheel);
        //value detector
        ImageView belt=findViewById(R.id.belt);
        //generate sector degrees to use
        generateSectorDegrees();

        //click belt to spin the wheel
        belt.setOnClickListener(v->{
            //only if it's not spinning
            if(!spinning){
                //if it's not spinning we have to spin it
                spin();
                spinning=true; //now spinning... :)
            }
        });

        //withdraw
        Button withdrawBtn=findViewById(R.id.spin);
        withdrawBtn.setOnClickListener(v->{
            String text="Ready to withdraw " + earningsRecord + " coins ?";
            toast(text);
        });
    }
    private void generateSectorDegrees(){
        //for 1 sector
        int sectorDegree=360/ sectors.length;
        for (int i=0;i<sectors.length;i++){
            sectorDegrees[i]=(i+1)*sectorDegree;
        }
    }

    private void spin(){
        //get a random value from the wheel
        randomSectorIndex=random.nextInt(sectors.length);

        //generate a random degree to spin the wheel
        int randomDegree=generateRandomDegreeToSpinTo();

        //doing the spinning using the rotation
        RotateAnimation rotateAnimation=new RotateAnimation(0,randomDegree,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);

        //spinning time
        rotateAnimation.setDuration(3500); //3.5 seconds
        rotateAnimation.setFillAfter(true);

        //interpolator
         //start with high speed then decreasing it by time
        rotateAnimation.setInterpolator(new DecelerateInterpolator());

        //spinning listener
        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //getting earns
                int earnedCoins=sectors[sectors.length - (randomSectorIndex-1)];

                //save the earnings
                saveEarnings(earnedCoins);

                //displaying a toast message
                String msg="You have earned : " + earnedCoins + " coins.";
                toast(msg);

                //ending the spinning
                spinning=false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        //applying the animation to the wheel
        wheel.startAnimation(rotateAnimation);
    }

    private void toast(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    private void saveEarnings(int earnedCoins) {
        //save earnings in the earning record
        earningsRecord=earningsRecord+earnedCoins;
        //set the value to the textview and display it
        TextView tv=findViewById(R.id.earned);
        tv.setText(String.valueOf(earningsRecord));
    }

    private int generateRandomDegreeToSpinTo(){
        return (360*sectors.length)+sectorDegrees[randomSectorIndex];
    }
}