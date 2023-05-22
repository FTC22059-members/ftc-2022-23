
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
        Renderer fancyTelemetry = new Renderer(telemetry, 34, 15);
        LogoAnimation wordmark = new LogoAnimation();

        while (opModeInInit()) {

            fancyTelemetry.insert(wordmark.getFrame(), 0, 0, false, false, false);
            fancyTelemetry.insert(new Surface(18, 3, " ! ").drawText(1, 1, "Arm Initialized!"), 36, 10, false, false, false);

            fancyTelemetry.loop();
        }

        // Wait till we press the start button
        waitForStart();

        while (opModeIsActive()) {
            Surface testBed = new Surface(34, 15);
            testBed.setChar(0, 0, new Fill(3, Orientations.VERTICAL), true);
            testBed.setChar(0, 1, '\u0009', true);
            testBed.setChar(0, 2, '\u0020', true);
            testBed.setChar(0, 3, '\u00a0', true);
            testBed.setChar(0, 4, '\u1680', true);
            testBed.setChar(0, 5, '\u2000', true);
            testBed.setChar(0, 6, '\u2001', true);
            testBed.setChar(0, 7, '\u2002', true);
            testBed.setChar(2, 0, new Fill(3, Orientations.HORIZONTAL), true);
            testBed.setChar(2, 1, '\u0009', true);
            testBed.setChar(2, 2, '\u0009', true);
            testBed.setChar(2, 3, '\u0009', true);
            testBed.setChar(2, 4, '\u0009', true);
            testBed.setChar(2, 5, '\u0009', true);
            testBed.setChar(2, 6, '\u0009', true);
            testBed.setChar(2, 7, '\u0009', true);
            testBed.setChar(0, 8, 'l', true);
            testBed.setChar(0, 9, 'm', true);
            testBed.setChar(2, 8, new Frame(new int[]{1, 2, 0, 0}, true), true);
            testBed.setChar(2, 9, new Frame(new int[]{1, 2, 2, 1}, true), true);

            for (int i = 0; i < 50; i++) {
                testBed.setChar(1, i, new Texel('|'), true);
                testBed.setChar(3, i, new Texel('|'), true);
            }

//            testBed.drawText(0, 0, "abcdefghijklmnopqrstuvwxyz!@#$%^&*()[]{}/=\\?+|-_'\",<.;:", new int[]{30, 15});

            fancyTelemetry.insert(testBed, 0, 0, false, false, false);
            fancyTelemetry.loop();
        }

    }
}