
/*
2022-23, Overclocked 22059
*/

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

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


//15 chars tall

@TeleOp(name = "Tele-op 2023 Test Font")
public class CharWidthTest extends LinearOpMode {

    @Override
    public void runOpMode() {
        Renderer fancyTelemetry = new Renderer(telemetry);
        LogoAnimation wordmark = new LogoAnimation();

        while (opModeInInit()) {

            fancyTelemetry.insert(wordmark.getFrame(), 0, 0, false, false, false);
            fancyTelemetry.insert(new Surface(18, 3, " ! ").drawText(1, 1, "Arm Initialized!"), 36, 10, false, false, false);

            fancyTelemetry.loop();
        }

        // Wait till we press the start button
        waitForStart();

        while (opModeIsActive()) {
            Surface testBed = new Surface(30, 15);
//            testBed.setChar(0, 0, new Fill(1, Orientations.VERTICAL), true);
//            testBed.setChar(0, 1, new Fill(2, Orientations.VERTICAL), true);
//            testBed.setChar(0, 2, new Fill(3, Orientations.VERTICAL), true);
//            testBed.setChar(0, 3, new Fill(4, Orientations.VERTICAL), true);
//            testBed.setChar(0, 4, new Fill(5, Orientations.VERTICAL), true);
//            testBed.setChar(0, 5, new Fill(6, Orientations.VERTICAL), true);
//            testBed.setChar(0, 6, new Fill(7, Orientations.VERTICAL), true);
//            testBed.setChar(0, 7, new Fill(8, Orientations.VERTICAL), true);
//            testBed.setChar(4, 0, new Fill(1, Orientations.HORIZONTAL), true);
//            testBed.setChar(4, 1, new Fill(2, Orientations.HORIZONTAL), true);
//            testBed.setChar(4, 2, new Fill(3, Orientations.HORIZONTAL), true);
//            testBed.setChar(4, 3, new Fill(4, Orientations.HORIZONTAL), true);
//            testBed.setChar(4, 4, new Fill(5, Orientations.HORIZONTAL), true);
//            testBed.setChar(4, 5, new Fill(6, Orientations.HORIZONTAL), true);
//            testBed.setChar(4, 6, new Fill(7, Orientations.HORIZONTAL), true);
//            testBed.setChar(4, 7, new Fill(8, Orientations.HORIZONTAL), true);
//            testBed.setChar(0, 14, new Texel('m'), true);
//            testBed.setChar(0, 15, new Texel('i'), true);
//            testBed.setChar(4, 14, new Frame(new int[]{1, 2, 0, 0}, true), true);
//            testBed.setChar(4, 15, new Frame(new int[]{1, 2, 2, 1}, true), true);
//
//            for (int i = 0; i < 50; i++) {
//                testBed.setChar(2, i, new Texel('|'), true);
//                testBed.setChar(6, i, new Texel('|'), true);
//            }

            testBed.drawText(0, 0, "abcdefghijklmnopqrstuvwxyz!@#$%^&*()[]{}/=\\?+|-_'\",<.;:", new int[]{30, 15});

            fancyTelemetry.insert(testBed, 0, 0, false, false, false);
            fancyTelemetry.loop();
        }

    }
}