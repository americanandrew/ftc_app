package org.firstinspires.ftc.teamcode.ChallengeFile;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.utils.GlyphVision;
import org.firstinspires.ftc.teamcode.utils.gyroCompass;
import org.firstinspires.ftc.teamcode.utils.motorDeclaration;
import org.firstinspires.ftc.teamcode.utils.turnTo;

@Autonomous (name= "AutotestEncoder", group= "competition")

public class AutoTestEncoder extends LinearOpMode {

    public Servo jewelStick;
    public ColorSensor leftJewel;
    public ColorSensor rightJewel;
    public DcMotor fl;
    public DcMotor fr;
    public DcMotor br;
    public DcMotor bl;
    public motorDeclaration Motors;
    public gyroCompass testGyro;
    ElapsedTime runtime = new ElapsedTime();
    public turnTo turn;
    public GlyphVision glyph;

    @Override
    public void runOpMode() throws InterruptedException {

        double COUNTS_PER_MOTOR_REV = 1440;    // eg: TETRIX Motor Encoder
        double DRIVE_GEAR_REDUCTION = 2.0;     // This is < 1.0 if geared UP
        double WHEEL_DIAMETER_INCHES = 4.0;     // For figuring circumference
        double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                (WHEEL_DIAMETER_INCHES * 3.1415);

        fr = hardwareMap.dcMotor.get("fr");
        fl = hardwareMap.dcMotor.get("fl");
        bl = hardwareMap.dcMotor.get("bl");
        br = hardwareMap.dcMotor.get("br");
        fr.setDirection(DcMotor.Direction.REVERSE);
        fl.setDirection(DcMotor.Direction.REVERSE);
        bl.setDirection(DcMotor.Direction.REVERSE);
        br.setDirection(DcMotor.Direction.REVERSE);
        glyph= new GlyphVision(hardwareMap);
        testGyro = new gyroCompass(hardwareMap);
        turn = new turnTo(hardwareMap, testGyro);
        Motors = new motorDeclaration(hardwareMap);
        fr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        telemetry.addData("Path0", "Starting at %7d :%7d",
                fr.getCurrentPosition(),
                bl.getCurrentPosition());
        telemetry.update();


        jewelStick = hardwareMap.servo.get("jewelStick");
        leftJewel = hardwareMap.colorSensor.get("lefwdatJewel");
        rightJewel = hardwareMap.colorSensor.get("rightJewel");
        int newLeftTarget = 0;
        int newRightTarget = 0;
        double target = 0;
        waitForStart();

        jewelStick.setPosition(1);
        int completed = 1;
        int color = 0; //1 is red 2 is blu (left side)
        double curr = 0.0;
        int stage = 0;
        double isComplete=1;
        double p = .0019;
        double i = .0003;
        int updatin = 0;
        int Timer = 0;
        RelicRecoveryVuMark matchGlyph = null;
        while (opModeIsActive()) {

            if(completed==0){

              matchGlyph= glyph.getGlyph();

                if(matchGlyph!=null){
                    completed++;
                }
            }
            telemetry.addData("glyph: ",matchGlyph);
            telemetry.addData("Completedvar", completed);

            if (completed == 1) {
                if (color == 0) {


                    if (leftJewel.red() - 1 > rightJewel.red() && rightJewel.blue() - 1 > leftJewel.blue()) {
                        telemetry.addData("left side is red", "right side is blue");
                        color = 2;
                        curr = testGyro.getHeading();
                        target = -25;
                        // sleep(1000);
                    } else if (leftJewel.red() + 1 < rightJewel.red() && rightJewel.blue() + 1 < leftJewel.blue()) {
                        telemetry.addData("right side is red", "left side is blue");
                        color = 1;
                        curr = testGyro.getHeading();
                        target = 25;
                        //sleep(1000);

                    } else {
                        telemetry.addData("No reading", "");
                    }

                    telemetry.addData("Left Red", leftJewel.red());
                    telemetry.addData("Left Blue", leftJewel.blue());
                    telemetry.addData("Right Red", rightJewel.red());
                    telemetry.addData("Right Blue", rightJewel.blue());
                    telemetry.addData("color:", color);
                    telemetry.addData("update:", updatin);
                    updatin++;

                }
                if (color >= 1) {
                    telemetry.addData("color", color);
                    telemetry.addData("gyroC1", testGyro.getHeading());
                    telemetry.addData("target", target);
                    telemetry.addData("curr", curr);
                    telemetry.addData("POWer", isComplete);
                    telemetry.addData("stage", stage);


                    // if(testGyro.getHeading()<target && stage==0) {
                    if (stage == 0) {
                        //Motors.setP(0, 0, target/50);
                        telemetry.addData("turning to", target);
                       // telemetry.addData("i", turn.turnT(target, 0.004, 0.001, 0.0, 1));
                        // telemetry.addData("i", turn.turnT(0, 0.0052, 0.002, 0.0, 1));
                        isComplete = turn.turnT(target, 0.007, 0.0005, 0.0, 3);
                        telemetry.addData("i",isComplete);
                        //isComplete = turn.turnT(target, p, 0, 0.0, 1);
                    }
                    if (isComplete == 0.0) {
                        jewelStick.setPosition(0);
                        isComplete = 1;
                        //sleep(1000);
                        stage++;
                    }
                    if (stage == 1) {
                        telemetry.addData("turning to 0", "ye");
                        isComplete = turn.turnT(0, 0.007, .0005, 0.0, 1);

                        // completed++;
                    }

                    if (stage >= 2) {
                        telemetry.addData("completed", "yay");
                        Motors.setP(0, 0, 0);
                        fr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        fr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                        bl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


                        completed++;
                    }
                    telemetry.addData("comp:", completed);
                }
            }
//
//
//
//
            if (completed == 2) {
                telemetry.addData("Path0", "Starting at %7d :%7d",
                        fr.getCurrentPosition(),
                        bl.getCurrentPosition());
                if(fr.getCurrentPosition()<500) {
                    Motors.setP(0, 0.3, 0);
                }
                else{
                    Motors.setP(0,0,0);
                }

//                if (fr.getCurrentPosition()<1000) {
//                    Motors.setP(0.3,0,0);
//                }
//                fr.setTargetPosition(-500);
//                fr.setPower(0.3);
//
//                fl.setTargetPosition(500);
//                fl.setPower(0.3);
//
//                br.setTargetPosition(500);
//                br.setPower(0.3);
//
//                bl.setTargetPosition(-500);
//                bl.setPower(0.3);

                    telemetry.addData("done!","ye");


            }

telemetry.update();
        }

    }
}