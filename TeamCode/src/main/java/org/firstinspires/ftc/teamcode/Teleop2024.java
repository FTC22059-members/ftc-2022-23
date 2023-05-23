package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.modules.Drive;
import org.firstinspires.ftc.teamcode.modules.Imu;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


@TeleOp(name = "Tele-op 2023")
public class Teleop2024 extends LinearOpMode{

    @Override
    public void runOpMode() {
        // init modules
        Global global = new Global(hardwareMap, telemetry, gamepad1, gamepad2);

        Imu robotImu = new Imu(global);
        robotImu.init();
        robotImu.resetAngle();

        Drive driveTrain = new Drive(global, robotImu);
        driveTrain.init();

        telemetry.addLine("Roses are red, violets are blue, if you press start on the robot, then it will move");
        telemetry.update();

    }

}
