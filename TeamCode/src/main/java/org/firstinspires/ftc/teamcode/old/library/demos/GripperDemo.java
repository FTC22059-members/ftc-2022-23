/*
2020-2021 FIRST Tech Challenge Team 14853
Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the "Software"), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute,
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
furnished to do so.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package org.firstinspires.ftc.teamcode.old.library.demos;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.old.library.Gripper;

@TeleOp(name = "Test Gripper")
public class GripperDemo extends LinearOpMode {
    private DcMotor gripperMotor;
    
    @Override
    public void runOpMode() {
        Gripper gripperTest = new Gripper(hardwareMap, telemetry);
        gripperTest.init(gamepad1, gamepad2);
        
        // Wait for the start button
        telemetry.addData(">", "Press Start to energize the robot with electrons that make it MOVE!");
        telemetry.update();
        
        
        waitForStart();
        
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            gripperTest.gripperLoop();
            telemetry.addData("Gripper open", gripperTest.getIsOpen());
            
            telemetry.update();
            
            idle();
        }
    }
}
