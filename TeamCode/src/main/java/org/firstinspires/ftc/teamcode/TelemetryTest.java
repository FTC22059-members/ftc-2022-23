
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
import org.firstinspires.ftc.teamcode.library.fancyTelemetry.*;

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

        telemetry.setDisplayFormat(Telemetry.DisplayFormat.MONOSPACE);
        telemetry.addLine("\u001b[0;35m I should be purple");
        telemetry.addLine("\u001b[1;34m I should be bold");

//        telemetry.setDisplayFormat(Telemetry.DisplayFormat.HTML);
//        telemetry.addLine("<b>b</b>");
//        telemetry.addLine("<code>code</code>");
//        telemetry.addLine("<span>span</span>");
//        telemetry.addLine("<p>p</p>");
//        telemetry.addLine("<p style=\"color:#ff0000\">p color:red</p>");
//        telemetry.addLine("<div style=\"background:#00ff00;border: 5px solid white\"><p style=\"color:#ff0000\">div background:green, border:5px solid white p color:red</p></div>");

        telemetry.update();
    }
}