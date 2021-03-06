package org.firstinspires.ftc.teamcode.ChallengeFile;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.utils.drive_at_angle_psudo;
import org.firstinspires.ftc.teamcode.utils.gyroCompass;
import org.firstinspires.ftc.teamcode.utils.turnTo;

import static java.lang.Thread.sleep;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOp", group = "ChallengeFile")
public class TeleOp extends OpMode {
//Declaration
    public DcMotor stanley;

    public DcMotor fl;
    public DcMotor fr;
    public DcMotor br;
    public DcMotor bl;
    Servo leftIntakeFlipper;
    Servo rightIntakeFlipper;
    Servo conveyerTop;
    DcMotor intakeDrive;
    DcMotor fwoppers;
    DcMotor conveyor;
    gyroCompass testGyro;
    drive_at_angle_psudo thing;
    public turnTo turn;
    Servo intakeBucket;
    Servo jewelStick;
    int floppers = 0;
    double position =0.0;
    int conveyorP = 0;
    int column1 = 0;
    int column2 = 0;
    int column3 = 0;
    int xComp = 0;
    boolean balanceEnabled = false;
    boolean offBalance;
    double angle2 = 0;
    boolean omnidrive = true;
    boolean startPressed = false;
    boolean gyroReset = false;
    boolean Driving;
    double i;
    double p;
    double intake;
//    double ratio = glyphColor.blue() / glyphColor.red();
    boolean align = true;
    boolean pressAlign = false;
    boolean lock =false;

    @Override
    public void init() {
//initiation
        intake = 0;
        jewelStick = hardwareMap.servo.get("jewelStick");
        fr = hardwareMap.dcMotor.get("fr");
        fl = hardwareMap.dcMotor.get("fl");
        bl = hardwareMap.dcMotor.get("bl");
        br = hardwareMap.dcMotor.get("br");

        fr.setDirection(DcMotor.Direction.REVERSE);
        fl.setDirection(DcMotor.Direction.REVERSE);
        bl.setDirection(DcMotor.Direction.REVERSE);
        br.setDirection(DcMotor.Direction.REVERSE);

        leftIntakeFlipper = hardwareMap.servo.get("leftIntakeFlipper");
        rightIntakeFlipper = hardwareMap.servo.get("rightIntakeFlipper");
        conveyerTop = hardwareMap.servo.get("conveyerTop"); //ye i kno its speld rong
        intakeBucket = hardwareMap.servo.get("intakeBucket");

        intakeDrive = hardwareMap.dcMotor.get("intakeDrive");
        intakeDrive.setDirection(DcMotor.Direction.REVERSE);

        fwoppers = hardwareMap.dcMotor.get("fwopperDrive");
        conveyor = hardwareMap.dcMotor.get("conveyer");

        testGyro = new gyroCompass(hardwareMap);

        thing = new drive_at_angle_psudo(hardwareMap);
        turn = new turnTo(hardwareMap, testGyro);
    }

    @Override
    public void loop() {
//gyro code
        if (gamepad1.start && !gyroReset && !gamepad1.a && !gamepad1.b) {
            gyroReset = true;
            testGyro.reset();
        } else if (!gamepad1.start) {
            gyroReset = false;
        }
        telemetry.addData("x", gamepad1.left_stick_x);
        telemetry.addData("y", gamepad1.left_stick_y);
        telemetry.addData("x2", gamepad1.right_stick_x);
        if ((Math.abs(gamepad1.left_stick_x) > .05) || (Math.abs(gamepad1.left_stick_y) > .05) || (Math.abs(gamepad1.right_stick_x) > .05)) {
            Driving = true;
            turn.reset();
        } else {
            Driving = false;
        }
        double speed = 0.4;
        double speed2 = 0.7;
        speed = speed + gamepad1.right_trigger * 0.65 - gamepad1.left_trigger * 0.25;
        speed2 = speed2 + gamepad2.right_trigger / 1.65;

        if (gamepad1.y && startPressed == false) {
            startPressed = true;
            omnidrive = !omnidrive;
        } else if (!gamepad1.y) {
            startPressed = false;
        }


        telemetry.addData("Omnidrive", omnidrive);
        telemetry.addData("balanceEnabled", balanceEnabled);
        telemetry.addData("driving", Driving);
        if (gamepad1.a) {
            balanceEnabled = true;

        }
        if (gamepad1.b) {
            balanceEnabled = false;
        }

        //System.out.println(gamepad1.right_bumper);
//Field Centric Code
        if (!omnidrive && Driving) {
            double x2 = gamepad1.left_stick_x;
            double y2 = -1 * gamepad1.left_stick_y;
            if (x2 > 0 && y2 > 0) {
                angle2 = Math.toDegrees(Math.atan(x2 / y2));
            } else if (x2 > 0 && y2 <= 0) {
                angle2 = 90 + -1 * Math.toDegrees(Math.atan(y2 / x2));
            } else if (x2 < 0 && y2 <= 0) {
                angle2 = -90 + -1 * Math.toDegrees(Math.atan(y2 / x2));
            } else if (x2 < 0 && y2 > 0) {
                angle2 = Math.toDegrees(Math.atan(x2 / y2));
            } else if (x2 == 0 && y2 < 0.15) {
                angle2 = 180.0;
            } else if (x2 == 0 && y2 > 0.15) {
                angle2 = 0.0;
            }
            double magnitude = Math.sqrt(Math.pow(x2, 2) + Math.pow(y2, 2));
            telemetry.addData("gyro", testGyro.getHeading());

            if (((Math.abs(x2) > .08) || (Math.abs(y2) > .08))) {
                thing.angle(angle2 - (-1 * testGyro.getHeading()), magnitude * speed, -1 * gamepad1.right_stick_x * (speed + .03));
                telemetry.addData("angle", angle2);
                telemetry.addData("target angle", angle2 - (-1 * testGyro.getHeading()));
//Driving
            } else if (Math.abs(gamepad1.right_stick_x) > .05) {
                fr.setPower(-1 * gamepad1.right_stick_x * speed);
                fl.setPower(-1 * gamepad1.right_stick_x * speed);
                br.setPower(-1 * gamepad1.right_stick_x * speed);
                bl.setPower(-1 * gamepad1.right_stick_x * speed);
            }
        } else if (omnidrive && Driving) {
            double frSpeed = (speed) * (-gamepad1.left_stick_y - gamepad1.left_stick_x - gamepad1.right_stick_x);
            double flSpeed = (speed) * (+gamepad1.left_stick_y - gamepad1.left_stick_x - gamepad1.right_stick_x);
            double brSpeed = (speed) * (-gamepad1.left_stick_y + gamepad1.left_stick_x - gamepad1.right_stick_x);
            double blSpeed = (speed) * (+gamepad1.left_stick_y + gamepad1.left_stick_x - gamepad1.right_stick_x);
            fr.setPower(frSpeed);
            fl.setPower(flSpeed);
            br.setPower(brSpeed);
            bl.setPower(blSpeed);
//Turn To Position
        } else if (gamepad1.dpad_up) {
            telemetry.addData("i", turn.turnT(0, 0.0052, 0.002, 0.0, 1));
        } else if (gamepad1.dpad_down) {
            telemetry.addData("i", turn.turnT(179.9, 0.0052, 0.002, 0.0, 1));
        } else if (gamepad1.dpad_right) {
            telemetry.addData("i", turn.turnT(-90, 0.0052, 0.002, 0.0, 1));
        } else if (gamepad1.dpad_left) {
            telemetry.addData("i", turn.turnT(90, 0.0052, 0.002, 0.0, 1));
//Balancing
        } else if (!Driving && balanceEnabled) {
            turn.reset();
            double pitch = -1 * testGyro.getPitch();
            double roll = -1 * (testGyro.getRoll());

            telemetry.addData("roll", roll);
            telemetry.addData("pitch", pitch);
            //telemetry.addData("heading", asdf);

            if ((roll > 2) || (roll < -2) || (pitch > 2) || (pitch < -2)) {

                if (roll < 2 && roll > -2) {
                    roll = 0;
                }
                if (pitch < 2 && pitch > -2) {
                    pitch = 0;
                }

                fr.setPower((speed - .3) * (-roll + pitch));
                fl.setPower((speed - .3) * (+roll + pitch));
                br.setPower((speed - .3) * (-roll - pitch));
                bl.setPower((speed - .3) * (+roll - pitch));
                telemetry.addData("balancing", ".");
            } else {
                fr.setPower(0);
                fl.setPower(0);
                br.setPower(0);
                bl.setPower(0);
                telemetry.addData("Level", "");
                offBalance = false;
            }
        } else if (!Driving) {
            turn.reset();
            fr.setPower(0);
            fl.setPower(0);
            br.setPower(0);
            bl.setPower(0);
        }

        //other stuff thats not drive
//conveyer
        if (gamepad2.dpad_down) {
            conveyorP = 1;
        } else if (gamepad2.dpad_up) {
            conveyorP = -1;
        } else if (gamepad2.dpad_left || gamepad2.dpad_right) {
            conveyorP = 0;
        }

        if (conveyorP == 1) {
            conveyor.setPower(speed2 - .31);
        } else if (conveyorP == -1) {
            conveyor.setPower(-speed2 + .245);
        } else {
            conveyor.setPower(0);
        }

        //intake wheel
        intakeDrive.setPower(gamepad2.left_stick_y * (speed2 + .3));

        //agliner/pusher toggle
        if(gamepad1.x&&!pressAlign){
            if (align) {
                align = false;
            } else {
                align=true;
            }
            pressAlign=true;
        }
        else if(!gamepad1.x){
            pressAlign=false;
        }

        if(align){
            conveyerTop.setPosition(.45);
        }
        else if(!align){
            conveyerTop.setPosition(1);
        }

        //fWoppers
        if (gamepad1.left_bumper) {
            fwoppers.setPower(1);
        } else if (gamepad1.right_bumper) {
            fwoppers.setPower(-1);
        }
        else if (gamepad2.left_bumper) {
            fwoppers.setPower(1);
        } else if (gamepad2.right_bumper) {
            fwoppers.setPower(-1);
        } else {
            fwoppers.setPower(0);
        }

        //intake linear servos
        position = position + (gamepad2.right_stick_y * .1);
        if (position < 0) {
            position = 0;
        }
        if (position > 1) {
            position = 1;
        }
        leftIntakeFlipper.setPosition(position);
        rightIntakeFlipper.setPosition(position);

        //intake bucket
        if(gamepad1.left_trigger<.05) {
            intakeBucket.setPosition(.62 - (gamepad2.left_trigger * .61));
        }
        else {
            intakeBucket.setPosition(.62 - (gamepad1.left_trigger * .61));
        }
        //control jewel arm
        if (gamepad1.left_bumper) {
            jewelStick.setPosition(1);
        } else {
            jewelStick.setPosition(.5);

        }

        //lock drivetrain in place
        if(Driving){
            lock=false;
        }
        else if(gamepad1.left_stick_button && lock ==false && !Driving){
             lock=true;
        }
        if (lock) {
            if(fr.getMode() == DcMotor.RunMode.RUN_WITHOUT_ENCODER) {
                fr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                bl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                fl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                br.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                telemetry.addData("run usin encoder","ye");
            }
            if(fr.getMode() == DcMotor.RunMode.RUN_USING_ENCODER) {
                fr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                fl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                br.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                telemetry.addData("reset encoder","j");
            }
            else if(fr.getMode() == DcMotor.RunMode.STOP_AND_RESET_ENCODER){
                br.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                telemetry.addData("run2pos","ition");
                br.setTargetPosition(0);
                fr.setTargetPosition(0);
                bl.setTargetPosition(0);
                fl.setTargetPosition(0);

            }
            else if(fr.getMode() == DcMotor.RunMode.RUN_TO_POSITION){


                br.setPower(1);
                bl.setPower(1);
                fr.setPower(1);
                fl.setPower(1);
                telemetry.addData("settin powe","r");

            }
        }
        else{
            fr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            fl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            br.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            bl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            telemetry.addData("runwoencoder","aw");
        }
        telemetry.update();

    }
}
