
/*
2022-23, Overclocked 22059
*/

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.library.Arm;
import org.firstinspires.ftc.teamcode.library.Drive;
import org.firstinspires.ftc.teamcode.library.Gripper;
import org.firstinspires.ftc.teamcode.library.Imu;
import com.overclockedftc.typographer.*;

//  ▒█████   ██▒   █▓ ▓█████ ██▀███    ▄████▄   ██▓    ▒█████    ▄████▄  ██ ▄█▀ ▓█████▓█████▄
// ▒██▒  ██▒▓██░   █▒ ▓█   ▀▓██ ▒ ██▒ ▒██▀ ▀█  ▓██▒   ▒██▒  ██▒ ▒██▀ ▀█  ██▄█▒  ▓█   ▀▒██▀ ██▌
// ▒██░  ██▒ ▓██  █▒░ ▒███  ▓██ ░▄█ ▒ ▒▓█    ▄ ▒██░   ▒██░  ██▒ ▒▓█    ▄▓███▄░  ▒███  ░██   █▌
// ▒██   ██░  ▒██ █░░ ▒▓█  ▄▒██▀▀█▄  ▒▒▓▓▄ ▄██ ▒██░   ▒██   ██░▒▒▓▓▄ ▄██▓██ █▄  ▒▓█  ▄░▓█▄   ▌
// ░ ████▓▒░   ▒▀█░  ▒░▒████░██▓ ▒██▒░▒ ▓███▀ ▒░██████░ ████▓▒░░▒ ▓███▀ ▒██▒ █▄▒░▒████░▒████▓
// ░ ▒░▒░▒░    ░ ▐░  ░░░ ▒░ ░ ▒▓ ░▒▓░░░ ░▒ ▒  ░░ ▒░▓  ░ ▒░▒░▒░ ░░ ░▒ ▒  ▒ ▒▒ ▓▒░░░ ▒░  ▒▒▓  ▒
//   ░ ▒ ▒░    ░ ░░  ░ ░ ░    ░▒ ░ ▒    ░  ▒  ░░ ░ ▒    ░ ▒ ▒░    ░  ▒  ░ ░▒ ▒░░ ░ ░   ░ ▒  ▒
// ░ ░ ░ ▒        ░      ░    ░░   ░  ░          ░ ░  ░ ░ ░ ▒   ░       ░ ░░ ░     ░   ░ ░  ░
//     ░ ░        ░  ░   ░     ░      ░ ░     ░    ░      ░ ░   ░ ░     ░  ░   ░   ░     ░


@TeleOp(name = "Telemetry Test")
public class TelemetryTest extends LinearOpMode {

    @Override
    public void runOpMode() {
        // Wait till we press the start button
        waitForStart();

        while (opModeIsActive()) {

            telemetry.setDisplayFormat(Telemetry.DisplayFormat.HTML);
            telemetry.addLine("<b>b</b>");
            telemetry.addLine("<code>code</code>");
            telemetry.addLine("<pre>pre <span style=\"color:#ff0000\">#ff0000</span> <span style=\"color:red\">red</span></pre>");
            telemetry.addLine("<span style=\"backround: #ff0000;\">span background:#ff0000</span>");
            telemetry.addLine("<span style=\"backround: red;\">span background:red</span>");

            telemetry.update();
        }
    }
}